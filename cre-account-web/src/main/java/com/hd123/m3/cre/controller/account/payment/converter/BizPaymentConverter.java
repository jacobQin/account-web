package com.hd123.m3.cre.controller.account.payment.converter;

import java.math.BigDecimal;
import java.util.Iterator;

import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.bank.Bank;
import com.hd123.m3.account.service.payment.Payment;
import com.hd123.m3.account.service.payment.PaymentAccOverdue;
import com.hd123.m3.account.service.payment.PaymentAccountLine;
import com.hd123.m3.account.service.payment.PaymentCashDefrayal;
import com.hd123.m3.account.service.payment.PaymentCollectionLine;
import com.hd123.m3.account.service.payment.PaymentDefrayalType;
import com.hd123.m3.account.service.payment.PaymentDepositDefrayal;
import com.hd123.m3.account.service.payment.PaymentLineCash;
import com.hd123.m3.account.service.payment.PaymentLineDeduction;
import com.hd123.m3.account.service.payment.PaymentLineDeposit;
import com.hd123.m3.account.service.payment.PaymentOverdueLine;
import com.hd123.m3.account.service.payment.PaymentOverdueTerm;
import com.hd123.m3.commons.biz.date.DateInterval;
import com.hd123.m3.commons.biz.tax.TaxRate;
import com.hd123.m3.cre.controller.account.payment.model.BPayment;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentAccOverdue;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentAccountLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentCashDefrayal;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentCollectionLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentDepositDefrayal;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentLineCash;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentLineDeduction;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentLineDeposit;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentOverdueLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentOverdueTerm;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * @author LiBin
 * 
 */
public class BizPaymentConverter implements Converter<BPayment, Payment> {

  private static BizPaymentConverter instance = null;

  private static Converter<BPayment, Payment> paymentConverter = ConverterBuilder.newBuilder(
      BPayment.class, Payment.class).build();

  private static Converter<BPaymentOverdueTerm, PaymentOverdueTerm> overdueTermConverter = ConverterBuilder
      .newBuilder(BPaymentOverdueTerm.class, PaymentOverdueTerm.class).build();

  public static BizPaymentConverter getInstance() {
    if (instance == null) {
      instance = new BizPaymentConverter();
    }
    return instance;
  }

  @Override
  public Payment convert(BPayment source) throws ConversionException {
    if (source == null) {
      return null;
    }

    prepareBeforConvert(source);

    Payment target = paymentConverter.convert(source);

    if (source.getAccountLines() != null && !source.getAccountLines().isEmpty()) {
      for (BPaymentAccountLine bline : source.getAccountLines()) {
        PaymentAccountLine line = writeAccountLine(bline);
        target.getLines().add(line);
      }
    }
    if (source.getCollectionLines() != null) {
      for (BPaymentCollectionLine bline : source.getCollectionLines()) {
        PaymentCollectionLine line = writeCollectionLine(bline);
        target.getLines().add(line);
      }
    }
    if (source.getOverdueLines() != null && !source.getOverdueLines().isEmpty()) {
      for (BPaymentOverdueLine bline : source.getOverdueLines()) {
        PaymentOverdueLine line = writeOverdueLine(bline);
        if (source.getCounterpart() != null) {
          line.setCounterpartType(source.getCounterpartType());
        }
        target.getLines().add(line);
      }
    }

    for (BPaymentCashDefrayal cash : source.getCashes()) {
      PaymentCashDefrayal defrayal = writeCashDefrayal(cash);
      target.getDefrayals().add(defrayal);
    }

    for (BPaymentDepositDefrayal deposit : source.getDeposits()) {
      PaymentDepositDefrayal defrayal = writeDepositDefrayal(deposit);
      target.getDefrayals().add(defrayal);
    }

    return target;
  }

  /**
   * 转换前的准备工作
   */
  private void prepareBeforConvert(BPayment bill) {
    // 移除无效的账款明细行
    Iterator<BPaymentAccountLine> iteratorAccount = bill.getAccountLines().iterator();
    while (iteratorAccount.hasNext()) {
      BPaymentAccountLine line = iteratorAccount.next();
      if (line.getAcc1() == null || line.getAcc1().getSubject() == null
          || StringUtil.isNullOrBlank(line.getAcc1().getSubject().getUuid()))
        iteratorAccount.remove();
    }
    // 移除无效的滞纳金明细行
    Iterator<BPaymentOverdueLine> iteratorOverdue = bill.getOverdueLines().iterator();
    while (iteratorOverdue.hasNext()) {
      BPaymentOverdueLine line = iteratorOverdue.next();
      if (line.getSubject() == null || StringUtil.isNullOrBlank(line.getSubject().getUuid()))
        iteratorOverdue.remove();
    }
    // 移除并合并收款信息
    if (PaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      refreshBillPaymentInfo(bill);
    } else {
      refreshLinePaymentInfo(bill);
    }
  }

  private void refreshLines(BPayment bill) {
    for (BPaymentLine line : bill.getAccountLines()) {
      // 移除无效的预存款冲扣行
      Iterator<BPaymentLineDeposit> iteratorDeposit = line.getDeposits().iterator();
      while (iteratorDeposit.hasNext()) {
        BPaymentLineDeposit deposit = iteratorDeposit.next();
        if (deposit.getRemainTotal() == null
            || deposit.getRemainTotal().compareTo(BigDecimal.ZERO) <= 0
            || deposit.getTotal() == null || deposit.getTotal().compareTo(BigDecimal.ZERO) <= 0
            || deposit.getTotal().compareTo(deposit.getRemainTotal()) > 0)
          iteratorDeposit.remove();
      }
    }

    for (BPaymentLine line : bill.getOverdueLines()) {
      // 移除无效的付款方式行
      Iterator<BPaymentLineCash> iteratorCash = line.getCashes().iterator();
      while (iteratorCash.hasNext()) {
        BPaymentLineCash cash = iteratorCash.next();
        if (cash.getPaymentType() == null || cash.getTotal() == null
            || cash.getTotal().compareTo(BigDecimal.ZERO) == 0)
          iteratorCash.remove();
      }

      // 移除无效的预存款冲扣行
      Iterator<BPaymentLineDeposit> iteratorDeposit = line.getDeposits().iterator();
      while (iteratorDeposit.hasNext()) {
        BPaymentLineDeposit deposit = iteratorDeposit.next();
        if (deposit.getRemainTotal() == null
            || deposit.getRemainTotal().compareTo(BigDecimal.ZERO) <= 0
            || deposit.getTotal() == null || deposit.getTotal().compareTo(BigDecimal.ZERO) <= 0
            || deposit.getTotal().compareTo(deposit.getRemainTotal()) > 0)
          iteratorDeposit.remove();
      }
    }
  }

  /**
   * 刷新并合并按总额收款时的收款信息。
   */
  private void refreshBillPaymentInfo(BPayment bill) {
    refreshLines(bill);
    // 移除无效的扣预存款行
    Iterator<BPaymentDepositDefrayal> iteratorDeposit = bill.getDeposits().iterator();
    while (iteratorDeposit.hasNext()) {
      BPaymentDepositDefrayal deposit = iteratorDeposit.next();
      if (deposit.getRemainTotal() == null
          || deposit.getRemainTotal().compareTo(BigDecimal.ZERO) <= 0 || deposit.getTotal() == null
          || deposit.getTotal().compareTo(BigDecimal.ZERO) <= 0
          || deposit.getTotal().compareTo(deposit.getRemainTotal()) > 0)
        iteratorDeposit.remove();
    }
  }

  /**
   * 刷新按科目收款时明细行中的收款信息。
   */
  private void refreshLinePaymentInfo(BPayment bill) {
    refreshLines(bill);
  }

  private PaymentAccountLine writeAccountLine(BPaymentAccountLine source) {
    PaymentAccountLine target = new PaymentAccountLine();
    target.setAcc1(source.getAcc1());
    target.setAcc2(source.getAcc2());
    target.getAcc2().setLocker(Accounts.NONE_LOCKER);
    target.getAcc2().setId(target.getAcc2().bizId().toId());

    target.setUuid(source.getUuid());
    target.setDefrayalTotal(source.getDefrayalTotal());
    target.setDepositTotal(source.getDepositTotal());
    target.setDepositSubject(source.getDepositSubject());
    target.setLineNumber(source.getLineNumber());
    target.setOverdueTotal(source.getOverdueTotal());
    target.setRemark(source.getRemark());
    target.setOriginTotal(source.getOriginTotal());
    target.setUnpayedTotal(source.getUnpayedTotal());
    target.setTotal(source.getTotal());
    target.setPivcCode(source.getIvcCode());
    target.setPivcDate(source.getIvcDate());
    target.setPivcNumber(source.getIvcNumber());
    target.setPivcTotal(source.getIvcTotal());
    target.setPivcType(source.getIvcType());
    target.setPriorityDeduction(source.isPriorityDeduction());

    for (BPaymentAccOverdue boverdue : source.getOverdues()) {
      PaymentAccOverdue overdue = writeAccOverdue(boverdue);
      target.getOverdues().add(overdue);
    }

    if (source.getOverdueTerms().isEmpty() == false) {
      target.setOverdueTerms(ArrayListConverter.newConverter(overdueTermConverter).convert(
          source.getOverdueTerms()));
    }

    for (BPaymentLineCash bdefrayal : source.getCashes()) {
      PaymentLineCash cash = writeLineCash(bdefrayal);
      cash.setLineUuid(target.getUuid());
      target.getDefrayals().add(cash);
    }
    for (BPaymentLineDeposit bdefrayal : source.getDeposits()) {
      PaymentLineDeposit deposit = writeLineDeposit(bdefrayal);
      deposit.setLineUuid(target.getUuid());
      target.getDefrayals().add(deposit);
    }
    for (BPaymentLineDeduction bdefrayal : source.getDeductions()) {
      PaymentLineDeduction deduction = writeLineDeduction(bdefrayal);
      deduction.setLineUuid(target.getUuid());
      target.getDefrayals().add(deduction);
    }

    return target;
  }

  private PaymentCollectionLine writeCollectionLine(BPaymentCollectionLine source) {
    PaymentCollectionLine target = new PaymentCollectionLine();

    target.setUuid(source.getUuid());
    target.setDefrayalTotal(source.getDefrayalTotal());
    target.setDepositTotal(source.getDepositTotal());
    target.setDepositSubject(source.getDepositSubject());
    target.setLineNumber(source.getLineNumber());
    target.setRemark(source.getRemark());
    target.setUnpayedTotal(source.getUnpayedTotal());
    target.setTotal(source.getTotal());
    target.setPivcCode(source.getIvcCode());
    target.setPivcNumber(source.getIvcNumber());
    target.setPivcType(source.getIvcType());
    target.setBeginEndDate(new DateInterval(source.getBeginDate(), source.getEndDate()));
    target.setSubject(source.getSubject());
    target.setContract(source.getContract());
    target.setRate(TaxRate.newInstance(source.getRate()));
    target.setAccountId(source.getAccountId());

    for (BPaymentLineCash bdefrayal : source.getCashes()) {
      PaymentLineCash cash = writeLineCash(bdefrayal);
      cash.setLineUuid(target.getUuid());
      target.getDefrayals().add(cash);
    }
    for (BPaymentLineDeposit bdefrayal : source.getDeposits()) {
      PaymentLineDeposit deposit = writeLineDeposit(bdefrayal);
      deposit.setLineUuid(target.getUuid());
      target.getDefrayals().add(deposit);
    }
    for (BPaymentLineDeduction bdefrayal : source.getDeductions()) {
      PaymentLineDeduction deduction = writeLineDeduction(bdefrayal);
      deduction.setLineUuid(target.getUuid());
      target.getDefrayals().add(deduction);
    }

    return target;
  }

  private PaymentAccOverdue writeAccOverdue(BPaymentAccOverdue source) {
    PaymentAccOverdue target = new PaymentAccOverdue();
    target.setContract(source.getContract());
    target.setDirection(source.getDirection());
    target.setIssueInvoice(source.isIssueInvoice());
    target.setSubject(source.getSubject());
    target.setTaxRate(source.getTaxRate());
    target.setTotal(source.getTotal());
    return target;
  }

  private PaymentOverdueLine writeOverdueLine(BPaymentOverdueLine source) {
    PaymentOverdueLine target = new PaymentOverdueLine();
    target.setAccountUnit(source.getAccountUnit());
    target.setCounterpart(source.getCounterpart());
    target.setContract(source.getContract());
    target.setDefrayalTotal(source.getDefrayalTotal());
    target.setDepositTotal(source.getDepositTotal());
    target.setDepositSubject(source.getDepositSubject());
    target.setIssueInvoice(source.isIssueInvoice());
    target.setLineNumber(source.getLineNumber());
    target.setRemark(source.getRemark());
    target.setSubject(source.getSubject());
    target.setTaxRate(source.getTaxRate());
    target.setUnpayedTotal(source.getUnpayedTotal());
    target.setTotal(source.getTotal());
    target.setOverdueTotal(source.getOverdueTotal());
    target.setAccountId(source.getAccountId());
    target.setUuid(source.getUuid());

    for (BPaymentLineCash bdefrayal : source.getCashes()) {
      PaymentLineCash cash = writeLineCash(bdefrayal);
      cash.setLineUuid(target.getUuid());
      target.getDefrayals().add(cash);
    }
    for (BPaymentLineDeposit bdefrayal : source.getDeposits()) {
      PaymentLineDeposit deposit = writeLineDeposit(bdefrayal);
      deposit.setLineUuid(target.getUuid());
      target.getDefrayals().add(deposit);
    }
    for (BPaymentLineDeduction bdefrayal : source.getDeductions()) {
      PaymentLineDeduction deduction = writeLineDeduction(bdefrayal);
      deduction.setLineUuid(target.getUuid());
      target.getDefrayals().add(deduction);
    }

    return target;
  }

  private PaymentCashDefrayal writeCashDefrayal(BPaymentCashDefrayal source) {
    PaymentCashDefrayal target = new PaymentCashDefrayal();
    Bank bank = source.getBank();
    if (bank != null) {
      target.setBankAccount(bank.getAccount());
      target.setBankCode(bank.getCode());
      target.setBankName(bank.getName());
    }
    target.setLineNumber(source.getLineNumber());
    target.setPaymentType(source.getPaymentType());
    target.setPoundage(source.getPoundage());
    target.setRemark(source.getRemark());
    target.setTotal(source.getTotal());
    return target;
  }

  private PaymentDepositDefrayal writeDepositDefrayal(BPaymentDepositDefrayal source) {
    PaymentDepositDefrayal target = new PaymentDepositDefrayal();
    target.setContract(source.getContract());
    target.setLineNumber(source.getLineNumber());
    target.setRemark(source.getRemark());
    target.setSubject(source.getSubject());
    target.setTotal(source.getTotal());
    return target;
  }

  private PaymentLineCash writeLineCash(BPaymentLineCash source) {
    PaymentLineCash target = new PaymentLineCash();
    if (source.getBank() != null) {
      target.setBankAccount(source.getBank().getAccount());
      target.setBankCode(source.getBank().getCode());
      target.setBankName(source.getBank().getName());
    }
    target.setItemNo(source.getItemNo());
    target.setLineUuid(source.getLineUuid());
    target.setPaymentType(source.getPaymentType());
    target.setPoundage(source.getPoundage());
    target.setRemark(source.getRemark());
    target.setTotal(source.getTotal());
    return target;
  }

  private PaymentLineDeduction writeLineDeduction(BPaymentLineDeduction source) {
    PaymentLineDeduction target = new PaymentLineDeduction();
    target.setAccountId(source.getAccountId());
    target.setBizId(source.getBizId());
    target.setItemNo(source.getItemNo());
    target.setLineUuid(source.getLineUuid());
    target.setRemark(source.getRemark());
    target.setTotal(source.getTotal());
    return target;
  }

  private PaymentLineDeposit writeLineDeposit(BPaymentLineDeposit source) {
    PaymentLineDeposit target = new PaymentLineDeposit();
    target.setContract(source.getContract());
    target.setItemNo(source.getItemNo());
    target.setLineUuid(source.getLineUuid());
    target.setRemark(source.getRemark());
    target.setSubject(source.getSubject());
    target.setTotal(source.getTotal());
    return target;
  }

}
