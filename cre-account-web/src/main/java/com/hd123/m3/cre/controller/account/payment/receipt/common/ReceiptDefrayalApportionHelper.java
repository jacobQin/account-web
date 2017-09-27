/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	ReceiptDefrayalApportionHelper.java
 * 模块说明：	
 * 修改历史：
 * 2016年10月13日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.payment.receipt.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.service.payment.PaymentDefrayalType;
import com.hd123.m3.commons.biz.tax.TaxCalculator;
import com.hd123.m3.cre.controller.account.payment.model.BPayment;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentAccountLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentCashDefrayal;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentCollectionLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentDepositDefrayal;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentLineCash;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentLineDeduction;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentLineDeposit;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * 按总额收款分摊帮助类
 * 
 * @author LiBin
 *
 */
@Component
public class ReceiptDefrayalApportionHelper {

  public BPayment apportion(BPayment payment, int scale, RoundingMode roundingMode) {
    if (PaymentDefrayalType.bill.equals(payment.getDefrayalType()) == false) {
      return payment;
    }

    /** 计算之前将实收明细行和扣预存款明细金额保存下来 */
    Map<Integer, BigDecimal> cashs = new HashMap<Integer, BigDecimal>();
    Map<Integer, BigDecimal> deposits = new HashMap<Integer, BigDecimal>();
    for (int i = 0; i < payment.getCashes().size(); i++) {
      cashs.put(i, payment.getCashes().get(i).getTotal());
    }
    for (int i = 0; i < payment.getDeposits().size(); i++) {
      deposits.put(i, payment.getDeposits().get(i).getTotal());
    }

    /** 进行分摊计算 ：1、分摊代收明细 ; 2、分摊账款明细 */
    apportionCollectionLines(payment, scale, roundingMode);
    apportionAccountLines(payment, scale, roundingMode);

    /** 计算完成后恢复实收明细行和扣预存款明细行 */
    for (int i = 0; i < payment.getCashes().size(); i++) {
      BPaymentCashDefrayal defrayal = payment.getCashes().get(i);
      defrayal.setTotal(cashs.get(i));
    }
    for (int i = 0; i < payment.getDeposits().size(); i++) {
      BPaymentDepositDefrayal defrayal = payment.getDeposits().get(i);
      defrayal.setTotal(deposits.get(i));
    }
    
    // 合计金额
    payment.aggregate(scale, roundingMode);
    
    return payment;
  }

  public void apportionCollectionLines(BPayment payment, int scale, RoundingMode roundingMode) {
    for (BPaymentCollectionLine line : payment.getCollectionLines()) {
      line.getCashes().clear();
      line.getDeposits().clear();
      line.getDeductions().clear();
      line.setDefrayalTotal(BigDecimal.ZERO);
      line.setDepositTotal(BigDecimal.ZERO);
      // 重置账款明细账款实收金额
      line.setTotal(Total.zero());
    }
    if (needApportionCollectionLines(payment) == false) {
      return;
    }
    apportionCash(payment, false, true);
  }

  public void apportionAccountLines(BPayment payment, int scale, RoundingMode roundingMode) {
    BigDecimal unpayedTotal = BigDecimal.ZERO;
    for (BPaymentAccountLine line : payment.getAccountLines()) {
      line.getCashes().clear();
      line.getDeposits().clear();
      line.getDeductions().clear();
      line.setDefrayalTotal(BigDecimal.ZERO);
      line.setDepositTotal(BigDecimal.ZERO);
      // 重置账款明细账款实收金额
      line.setTotal(Total.zero());
      unpayedTotal = unpayedTotal.add(line.getUnpayedTotal().getTotal());
    }
    if (needApportion(payment, unpayedTotal) == false)
      return;
    boolean negative = unpayedTotal.compareTo(BigDecimal.ZERO) < 0;
    if (!negative)
      apportionDeposit(payment, true, false);
    apportionDeduction(payment, negative);
    apportionCash(payment, negative, false);
    if (!negative)
      apportionDeposit(payment, false, false);
    // 重算税额
    for (BPaymentAccountLine line : payment.getAccountLines()) {
      line.getTotal().setTax(
          TaxCalculator.tax(line.getTotal().getTotal(), line.getAcc1().getTaxRate(), scale,
              roundingMode));
    }
  
  }

  private boolean needApportion(BPayment payment, BigDecimal unpayedTotal) {
    BigDecimal depositTotal = BigDecimal.ZERO;
    boolean negCash = false;
    boolean posCash = false;
    BigDecimal cashDefrayal = BigDecimal.ZERO;
    for (BPaymentCashDefrayal defrayal : payment.getCashes()) {
      cashDefrayal = cashDefrayal.add(defrayal.getTotal());
    }
    if (cashDefrayal.compareTo(BigDecimal.ZERO) < 0) {
      negCash = true;
    }
    if (cashDefrayal.compareTo(BigDecimal.ZERO) > 0) {
      posCash = true;
    }
    for (BPaymentDepositDefrayal defrayal : payment.getDeposits()) {
      depositTotal = depositTotal.add(defrayal.getTotal());
    }
    boolean negative = unpayedTotal.compareTo(BigDecimal.ZERO) < 0;
    if ((!negative && negCash)
        || (negative && (posCash || depositTotal.compareTo(BigDecimal.ZERO) > 0))) {
      return false;
    }
    return true;
  }

  private void apportionDeposit(BPayment payment, boolean hasContract, boolean apportionCollection) {
    for (BPaymentDepositDefrayal depositDefrayal : payment.getDeposits()) {
      if (hasContract) {
        if (depositDefrayal.getContract() == null
            || StringUtil.isNullOrBlank(depositDefrayal.getContract().getUuid()))
          continue;
      } else {
        if (depositDefrayal.getContract() != null
            && StringUtil.isNullOrBlank(depositDefrayal.getContract().getUuid()) == false)
          continue;
      }

      BigDecimal leftTotal = depositDefrayal.getTotal();

      List<BPaymentLine> lines = new ArrayList<BPaymentLine>();
      if (apportionCollection) {
        for (BPaymentLine line : payment.getCollectionLines()) {
          lines.add(line);
        }
      } else {
        for (BPaymentLine line : payment.getAccountLines()) {
          lines.add(line);
        }
      }
      for (BPaymentLine line : lines) {
        if (leftTotal.compareTo(BigDecimal.ZERO) <= 0)
          break;
        // 跳过抵扣明细
        if (line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) <= 0)
          continue;
        BigDecimal undefrayalTotal = line.getUnpayedTotal().getTotal()
            .subtract(line.getDefrayalTotal());
        // 账款明细实收大于应收跳过
        if (undefrayalTotal.compareTo(BigDecimal.ZERO) <= 0)
          continue;
        BPaymentLineDeposit lineDefrayal = buildLineDeposit(depositDefrayal);
        lineDefrayal.setLineUuid(line.getUuid());
        line.getDeposits().add(lineDefrayal);
        if (undefrayalTotal.compareTo(leftTotal) < 0) {
          line.setDefrayalTotal(line.getUnpayedTotal().getTotal());
          leftTotal = leftTotal.subtract(undefrayalTotal);
          lineDefrayal.setTotal(undefrayalTotal);
        } else {
          line.setDefrayalTotal(line.getDefrayalTotal().add(leftTotal));
          lineDefrayal.setTotal(leftTotal);
          leftTotal = BigDecimal.ZERO;
        }
        line.getTotal().setTotal(line.getDefrayalTotal());
      }

      depositDefrayal.setTotal(leftTotal);
    }
  }

  private void apportionDeduction(BPayment payment, boolean negative) {
    List<BPaymentAccountLine> deductionLines = new ArrayList<BPaymentAccountLine>();
    for (BPaymentAccountLine line : payment.getAccountLines()) {
      if ((!negative && line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) < 0)
          || (negative && line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) > 0)) {
        deductionLines.add(line);
        BPaymentLineDeduction lineDeduction = buildLineDeduction(line);
        lineDeduction.setTotal(line.getUnpayedTotal().getTotal());
        line.setDefrayalTotal(line.getUnpayedTotal().getTotal());
        line.getTotal().setTotal(line.getDefrayalTotal());
        line.getDeductions().add(lineDeduction);
      }
    }
    for (BPaymentAccountLine deductionLine : deductionLines) {
      BigDecimal leftTotal = BigDecimal.ZERO.subtract(deductionLine.getTotal().getTotal());
      leftTotal = apportionDeductionOne(deductionLine, payment, leftTotal, negative);
      if (leftTotal.compareTo(BigDecimal.ZERO) != 0) {
        // 预存款冲抵
      }
    }
  }

  private BigDecimal apportionDeductionOne(BPaymentAccountLine deductionLine, BPayment payment,
      BigDecimal leftTotal, boolean negative) {
    for (BPaymentAccountLine line : payment.getAccountLines()) {
      if ((!negative && leftTotal.compareTo(BigDecimal.ZERO) <= 0)
          || (negative && leftTotal.compareTo(BigDecimal.ZERO) >= 0))
        break;
      // 跳过抵扣明细
      if ((!negative && line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) <= 0)
          || (negative && line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) >= 0))
        continue;
      // 账款明细实收大于应收跳过
      if (line.getDefrayalTotal().abs().compareTo(line.getUnpayedTotal().getTotal().abs()) >= 0)
        continue;
      BigDecimal undefrayalTotal = line.getUnpayedTotal().getTotal()
          .subtract(line.getDefrayalTotal());
      BPaymentLineDeduction lineDeduction = buildLineDeduction(deductionLine);
      lineDeduction.setLineUuid(line.getUuid());
      line.getDeductions().add(lineDeduction);
      if (undefrayalTotal.abs().compareTo(leftTotal.abs()) < 0) {
        line.setDefrayalTotal(line.getUnpayedTotal().getTotal());
        leftTotal = leftTotal.subtract(undefrayalTotal);
        lineDeduction.setTotal(undefrayalTotal);
      } else {
        line.setDefrayalTotal(line.getDefrayalTotal().add(leftTotal));
        lineDeduction.setTotal(leftTotal);
        leftTotal = BigDecimal.ZERO;
      }
      line.getTotal().setTotal(line.getDefrayalTotal());
    }
    return leftTotal;
  }

  private void apportionCash(BPayment payment, boolean negative, boolean apportionCollection) {
    for (BPaymentCashDefrayal cashDefrayal : payment.getCashes()) {
      BigDecimal leftTotal = cashDefrayal.getTotal();

      List<BPaymentLine> lines = new ArrayList<BPaymentLine>();
      if (apportionCollection) {
        for (BPaymentLine line : payment.getCollectionLines()) {
          lines.add(line);
        }
      } else {
        for (BPaymentLine line : payment.getAccountLines()) {
          lines.add(line);
        }
      }

      for (BPaymentLine line : lines) {

        /*
         * if ((!negative && leftTotal.compareTo(BigDecimal.ZERO) <= 0 &&
         * paymentType != null && !paymentType
         * .equals(config.getReceiptPaymentType())) || (negative &&
         * leftTotal.compareTo(BigDecimal.ZERO) >= 0 && paymentType != null &&
         * !paymentType .equals(config.getReceiptPaymentType()))){ break; }
         */

        // 如果是长短款付款方式不需要跳过抵扣明细
     /*   if (paymentType != null && !paymentType.equals(config.getReceiptPaymentType())) {
          // Do Nothing
        } else {*/
          // 跳过抵扣明细
          if ((!negative && line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) <= 0)
              || (negative && line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) >= 0))
            continue;
        //}
        // 实收大于应收跳过
        BigDecimal undefrayalTotal = line.getUnpayedTotal().getTotal()
            .subtract(line.getDefrayalTotal());
        if ((!negative && undefrayalTotal.compareTo(BigDecimal.ZERO) <= 0)
            || (negative && undefrayalTotal.compareTo(BigDecimal.ZERO) >= 0))
          continue;
        BPaymentLineCash lineDefrayal = buildLineCash(cashDefrayal);
        lineDefrayal.setLineUuid(line.getUuid());
        line.getCashes().add(lineDefrayal);
        if (undefrayalTotal.abs().compareTo(leftTotal.abs()) < 0) {
          line.setDefrayalTotal(line.getUnpayedTotal().getTotal());
          leftTotal = leftTotal.subtract(undefrayalTotal);
          lineDefrayal.setTotal(undefrayalTotal);
        } else {
          line.setDefrayalTotal(line.getDefrayalTotal().add(leftTotal));
          lineDefrayal.setTotal(leftTotal);
          leftTotal = BigDecimal.ZERO;
        }
        line.getTotal().setTotal(line.getDefrayalTotal());
      }
      cashDefrayal.setTotal(leftTotal);
    }
  }

  private BPaymentLineDeposit buildLineDeposit(BPaymentDepositDefrayal defrayal) {
    BPaymentLineDeposit lineDeposit = new BPaymentLineDeposit();
    lineDeposit.setContract(defrayal.getContract());
    lineDeposit.setSubject(defrayal.getSubject());
    lineDeposit.setRemainTotal(defrayal.getRemainTotal());
    return lineDeposit;
  }

  private BPaymentLineDeduction buildLineDeduction(BPaymentAccountLine line) {
    BPaymentLineDeduction lineDeduction = new BPaymentLineDeduction();
    lineDeduction.setLineUuid(line.getUuid());
    lineDeduction.setAccountId(line.getAcc1().getId());
    lineDeduction.setBizId(line.getAcc2().getId());
    return lineDeduction;
  }

  private BPaymentLineCash buildLineCash(BPaymentCashDefrayal defrayal) {
    BPaymentLineCash lineCash = new BPaymentLineCash();
    lineCash.setBank(defrayal.getBank());
    lineCash.setPaymentType(defrayal.getPaymentType());
    return lineCash;
  }

  private boolean needApportionCollectionLines(BPayment payment) {
    if (payment.getCollectionLines() == null || payment.getCollectionLines().isEmpty()) {
      return false;
    }

    boolean need = false;
    for (BPaymentCollectionLine line : payment.getCollectionLines()) {
      if (line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) > 0) {
        need = true;
        break;
      }
    }
    return need;
  }
}

