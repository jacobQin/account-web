/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	OverdueCalcHelper.java
 * 模块说明：	
 * 修改历史：
 * 2016年11月22日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.payment.receipt.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.cre.controller.account.common.AccountOptionComponent;
import com.hd123.m3.cre.controller.account.common.model.ReceiptOverdueDefault;
import com.hd123.m3.cre.controller.account.payment.model.BPayment;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentAccOverdue;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentAccountLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentOverdueLine;
import com.hd123.rumba.util.DateUtil;

/**
 * 滞纳金计算辅助类
 * 
 * @author LiBin
 *
 */
@Component
public class OverdueCalcHelper {

  @Autowired
  AccountOptionComponent optionComponent;

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
  public BPayment calculate(BPayment bill) {
    // 收款日期为空跳过
    bill.setOverdueTotal(Total.zero());
    bill.getOverdueLines().clear();

    if (bill.getPaymentDate() == null) {
      return bill;
    }

    boolean needCalculate = false;
    for (BPaymentAccountLine line : bill.getAccountLines()) {
      line.getOverdues().clear();
      line.setOverdueTotal(Total.zero());
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
      return bill;
    }

    int scale = optionComponent.getScale();
    RoundingMode roundingMode = optionComponent.getRoundingMode();
    ReceiptOverdueDefault overdueDefault = optionComponent.getReceiptOverdueDefault();
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

    return bill;
  }

  private static void calculateAccLine(BPaymentAccountLine accLine, BPayment bill,
      boolean onlyUpdateBlance, int scale, RoundingMode roundingMode,
      ReceiptOverdueDefault overdueDefault) {
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
  }

  /**
   * 根据账款明细的滞纳金明细，重算滞纳金明细行。
   * 
   * @param accLines
   *          账款明细。
   * @param overdueLines
   *          旧滞纳金明细。
   */
  private static void resetOverdueLines(BPayment bill, ReceiptOverdueDefault overdueDefault) {
    // 将账款明细的滞纳金添加到滞纳金明细
    for (BPaymentAccountLine line : bill.getAccountLines()) {
      if (SOURCEBILL_TYPE.equals(line.getAcc1().getSourceBill().getBillType())) {
        continue;
      }

      if (line.getOverdues().isEmpty()) {
        continue;
      }
      for (BPaymentAccOverdue overdue : line.getOverdues()) {
        addOverdueLine(bill, line, overdue, overdueDefault);
      }
    }

  }

  // 添加明细滞纳金到滞纳金明细
  private static void addOverdueLine(BPayment bill, BPaymentAccountLine accLine,
      BPaymentAccOverdue accOverdueLine, ReceiptOverdueDefault overdueDefault) {

    BPaymentOverdueLine overdueLine = new BPaymentOverdueLine();
    overdueLine.setAccountId(accLine.getAcc1().getId());
    overdueLine.setAccountUnit(accLine.getAcc1().getAccountUnit());
    overdueLine.setContract(accLine.getAcc1().getContract());
    overdueLine.setCounterpart(accLine.getAcc1().getCounterpart());
    overdueLine.setIssueInvoice(accOverdueLine.isIssueInvoice());
    overdueLine.setOverdueTotal(accOverdueLine.getTotal().clone());
    overdueLine.setSubject(accOverdueLine.getSubject());
    overdueLine.setTaxRate(accOverdueLine.getTaxRate());
    overdueLine.setUnpayedTotal(overdueLine.getOverdueTotal().clone());
    overdueLine.setTotal(overdueDefault == null ? overdueLine.getUnpayedTotal().clone()
        : (ReceiptOverdueDefault.calcValue.equals(overdueDefault) ? overdueLine.getUnpayedTotal()
            : Total.zero()));
    bill.getOverdueLines().add(overdueLine);
  }
}
