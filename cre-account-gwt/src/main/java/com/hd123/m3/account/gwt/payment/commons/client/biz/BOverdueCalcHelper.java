/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BOverdueCalcHelper.java
 * 模块说明：	
 * 修改历史：
 * 2015-1-11 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.biz;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.hd123.m3.account.gwt.commons.client.biz.BReceiptOverdueDefault;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.rumba.gwt.base.client.util.DateUtil;

/**
 * 滞纳金计算辅助类
 * 
 * @author huangjunxian
 * 
 */
public class BOverdueCalcHelper {

  /** 来源单据类型为收款单的账款（滞纳金）不计算滞纳金 */
  private static final String SOURCEBILL_TYPE = "receiptPayment";

  /**
   * 计算滞纳金
   * 
   * @param bill
   *          收付款单
   * @param reCalc
   *          重算时滞纳金明细会发生删除操作，否则只更新滞纳金明细的金额。
   * @scale 精度
   * @roundingMode 舍入算法
   */
  public static void calculate(BPayment bill, int scale, RoundingMode roundingMode,
      BReceiptOverdueDefault overdueDefault) {
    // 收付款日期为空跳过
    bill.setOverdueTotal(BTotal.zero());
    bill.getOverdueLines().clear();

    if (bill.getPaymentDate() == null) {
      return;
    }

    boolean needCalculate = false;
    for (BPaymentAccountLine line : bill.getAccountLines()) {
      line.getOverdues().clear();
      line.setOverdueTotal(BTotal.zero());
      if (line.getDefrayalTotal().compareTo(BigDecimal.ZERO) == 0) {
        continue;
      }

      // 来源单据为收款单跳过
      if (SOURCEBILL_TYPE.equals(line.getAcc1().getSourceBill().getBillType())) {
        continue;
      }
      needCalculate = true;
    }

    if (needCalculate == false) {
      return;
    }
    // 遍历计算账款明细的滞纳金
    for (BPaymentAccountLine accLine : bill.getAccountLines()) {
      if (SOURCEBILL_TYPE.equals(accLine.getAcc1().getSourceBill().getBillType())) {
        continue;
      }
      calculateAccLine(accLine, bill, false, scale, roundingMode, overdueDefault);
    }

    // 重算滞纳金明细
    resetOverdueLines(bill, overdueDefault);

    // 合计滞纳金金额
    bill.aggregateOverdue(scale, roundingMode);

  }

  private static void calculateAccLine(BPaymentAccountLine accLine, BPayment bill,
      boolean onlyUpdateBlance, int scale, RoundingMode roundingMode,
      BReceiptOverdueDefault overdueDefault) {
    // 不存在对应的滞纳金条款跳过
    if (accLine.getOverdueTerms().isEmpty()) {
      return;
    }
    // 账款明细最后结算日为空跳过
    Date lastPayDate = accLine.getAcc2().getLastPayDate();
    int graceDay = accLine.getAcc2().getGraceDays();
    if (lastPayDate == null) {
      return;
    }
    // 在宽限日内跳过
    if (!bill.getPaymentDate().after(DateUtil.addDay(lastPayDate, graceDay))) {
      accLine.calculateOverdue(0, scale, roundingMode);
      return;
    }
    long overdueDays = (bill.getPaymentDate().getTime() - lastPayDate.getTime())
        / (24 * 60 * 60 * 1000);
    accLine.calculateOverdue(overdueDays, scale, roundingMode);

    if (!onlyUpdateBlance) {
      // 添加或更新滞纳金明细
      updateOverdueLines(accLine.getOverdues(), bill.getOverdueLines(), overdueDefault);
    }
    // 合并滞纳金差额
    updateBalanceOverdues(accLine, bill, overdueDefault);
  }

  private static void updateBalanceOverdues(BPaymentAccountLine accLine, BPayment bill,
      BReceiptOverdueDefault overdueDefault) {
    for (BPaymentAccOverdue balanceOverdueLine : accLine.getBalanceOverdues()) {
      for (int i = 0; i < bill.getOverdueLines().size(); i++) {
        BPaymentOverdueLine overdueLine = bill.getOverdueLines().get(i);
        if (overdueLine.getSubject().equals(balanceOverdueLine.getSubject())
            && overdueLine.getContract().getCode()
                .equals(balanceOverdueLine.getContract().getCode())
            && overdueLine.getTaxRate().equals(balanceOverdueLine.getTaxRate())) {
          overdueLine.setOverdueTotal(overdueLine.getOverdueTotal().add(
              balanceOverdueLine.getTotal()));
          overdueLine.setUnpayedTotal(overdueLine.getOverdueTotal().clone());
          overdueLine.setTotal(overdueDefault == null ? overdueLine.getUnpayedTotal().clone()
              : (BReceiptOverdueDefault.calcValue.equals(overdueDefault) ? overdueLine
                  .getUnpayedTotal().clone() : BTotal.zero()));

          if (overdueLine.getOverdueTotal().equals(BTotal.zero())) {
            bill.getOverdueLines().remove(i);
          }
          break;
        }
      }
    }
  }

  /**
   * 根据账款明细的滞纳金明细，重算滞纳金明细行。
   * 
   * @param accLines
   *          账款明细。
   * @param overdueLines
   *          旧滞纳金明细。
   */
  private static void resetOverdueLines(BPayment bill, BReceiptOverdueDefault overdueDefault) {
    // 先合并出新的滞纳金明细行
    List<BPaymentOverdueLine> overdueTempLines = new ArrayList<BPaymentOverdueLine>();
    for (BPaymentAccountLine line : bill.getAccountLines()) {
      if (SOURCEBILL_TYPE.equals(line.getAcc1().getSourceBill().getBillType())) {
        continue;
      }
      updateOverdueLines(line.getOverdues(), overdueTempLines, overdueDefault);
    }

    // 移除旧的并且不在新的滞纳金明细行中的滞纳金明细
    Iterator<BPaymentOverdueLine> iterator = bill.getOverdueLines().iterator();
    while (iterator.hasNext()) {
      boolean find = false;
      BPaymentOverdueLine overdueLine = iterator.next();
      for (BPaymentOverdueLine line : overdueTempLines) {
        if (overdueLine.getSubject().equals(line.getSubject())
            && overdueLine.getContract().getCode().equals(line.getContract().getCode())
            && overdueLine.getTaxRate().equals(line.getTaxRate())) {
          find = true;
          break;
        }
      }
      if (!find) {
        iterator.remove();
      }
    }
    // 将新的滞纳金明细行更新上去
    for (BPaymentOverdueLine newLine : overdueTempLines) {
      if (bill.getOverdueLines().isEmpty()) {
        bill.getOverdueLines().add(newLine);
      } else {
        boolean find = false;
        for (BPaymentOverdueLine oldLine : bill.getOverdueLines()) {
          if (newLine.getSubject().equals(oldLine.getSubject())
              && newLine.getContract().getCode().equals(oldLine.getContract().getCode())
              && newLine.getTaxRate().equals(oldLine.getTaxRate())) {
            oldLine.setOverdueTotal(newLine.getOverdueTotal().clone());
            oldLine.setUnpayedTotal(newLine.getUnpayedTotal().clone());
            oldLine.setTotal(newLine.getTotal().clone());
            find = true;
            break;
          }
        }
        if (!find) {
          bill.getOverdueLines().add(newLine);
        }
      }
    }

  }

  // 添加或更新滞纳金明细
  private static void updateOverdueLines(List<BPaymentAccOverdue> accOverdueLines,
      List<BPaymentOverdueLine> overdueLines, BReceiptOverdueDefault overdueDefault) {
    for (BPaymentAccOverdue accOverdueLine : accOverdueLines) {
      updateOverdueLine(accOverdueLine, overdueLines, overdueDefault);
    }
  }

  // 更新明细滞纳金到滞纳金明细
  private static void updateOverdueLine(BPaymentAccOverdue accOverdueLine,
      List<BPaymentOverdueLine> overdueLines, BReceiptOverdueDefault overdueDefault) {
    boolean find = false;
    for (BPaymentOverdueLine ol : overdueLines) {
      if (ol.getSubject().equals(accOverdueLine.getSubject())
          && ol.getContract().getCode().equals(accOverdueLine.getContract().getCode())
          && ol.getTaxRate().equals(accOverdueLine.getTaxRate())) {
        ol.setOverdueTotal(ol.getOverdueTotal().add(accOverdueLine.getTotal()));
        ol.setUnpayedTotal(ol.getOverdueTotal().clone());
        ol.setTotal(overdueDefault == null ? ol.getUnpayedTotal().clone()
            : (BReceiptOverdueDefault.calcValue.equals(overdueDefault) ? ol.getUnpayedTotal()
                .clone() : BTotal.zero()));

        find = true;
        break;
      }
    }
    if (!find) {
      addOverdueLine(accOverdueLine, overdueLines, overdueDefault);
    }
  }

  // 添加明细滞纳金到滞纳金明细
  private static void addOverdueLine(BPaymentAccOverdue accOverdueLine,
      List<BPaymentOverdueLine> overdueLines, BReceiptOverdueDefault overdueDefault) {
    BPaymentOverdueLine overdueLine = new BPaymentOverdueLine();
    overdueLine.setAccountId(accOverdueLine.getLine().getAcc1().getId());
    overdueLine.setAccountUnit(accOverdueLine.getLine().getAcc1().getAccountUnit());
    overdueLine.setContract(accOverdueLine.getLine().getAcc1().getContract());
    overdueLine.setCounterpart(accOverdueLine.getLine().getAcc1().getCounterpart());
    overdueLine.setIssueInvoice(accOverdueLine.isIssueInvoice());
    overdueLine.setOverdueTotal(accOverdueLine.getTotal().clone());
    overdueLine.setSubject(accOverdueLine.getSubject());
    overdueLine.setTaxRate(accOverdueLine.getTaxRate());
    overdueLine.setUnpayedTotal(overdueLine.getOverdueTotal().clone());
    overdueLine.setTotal(overdueDefault == null ? overdueLine.getUnpayedTotal().clone()
        : (BReceiptOverdueDefault.calcValue.equals(overdueDefault) ? overdueLine.getUnpayedTotal()
            : BTotal.zero()));
    overdueLines.add(overdueLine);
  }
}
