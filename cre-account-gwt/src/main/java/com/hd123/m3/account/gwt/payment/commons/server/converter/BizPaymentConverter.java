/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BizPaymentConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-20 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.server.converter;

import java.math.BigDecimal;
import java.util.Iterator;

import org.hibernate.collection.PersistentBag;

import com.hd123.m3.account.gwt.acc.server.BizAcc1Converter;
import com.hd123.m3.account.gwt.acc.server.BizAcc2Converter;
import com.hd123.m3.account.gwt.commons.server.BizAccStandardBillConverter;
import com.hd123.m3.account.gwt.commons.server.BizTotalConverter;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccOverdue;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCashDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCollectionLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDepositDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineCash;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDeduction;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDeposit;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueLine;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.service.acc.Accounts;
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
import com.hd123.m3.commons.biz.date.DateInterval;
import com.hd123.m3.commons.biz.tax.TaxRate;
import com.hd123.m3.commons.gwt.base.server.tax.BizTaxRateConverter;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * @author subinzhu
 * 
 */
public class BizPaymentConverter extends BizAccStandardBillConverter<BPayment, Payment> {

  private static BizPaymentConverter instance = null;

  public static BizPaymentConverter getInstance() {
    if (instance == null)
      instance = new BizPaymentConverter();
    return instance;
  }

  @Override
  public Payment convert(BPayment source) throws ConversionException {
    if (source == null)
      return null;

    prepareBeforConvert(source);

    Payment target = new Payment();

    super.inject(source, target);
    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    target.setCounterpart(BizUCNConverter.getInstance().convert(source.getCounterpart()));
    target.setCounterpartType(source.getCounterpart() == null ? null : source.getCounterpart()
        .getCounterpartType());
    target.setDealer(BizUCNConverter.getInstance().convert(source.getDealer()));
    target.setDefrayalType(PaymentDefrayalType.valueOf(source.getDefrayalType()));
    target.setDepositSubject(BizUCNConverter.getInstance().convert(source.getDepositSubject()));
    target.setDepositTotal(source.getDepositTotal());
    target.setDefrayalTotal(source.getDefrayalTotal());
    target.setDirection(source.getDirection());
    target.setOverdueTotal(BizTotalConverter.getInstance().convert(source.getOverdueTotal()));
    target.setPaymentDate(source.getPaymentDate());
    target.setTotal(BizTotalConverter.getInstance().convert(source.getTotal()));
    target.setUnpayedTotal(BizTotalConverter.getInstance().convert(source.getUnpayedTotal()));
    target.setIncomeDate(source.getIncomeDate());
    target.setPivcCode(source.getIvcCode());
    target.setPivcDate(source.getIvcDate());
    target.setPivcNumber(source.getIvcNumber());
    target.setPivcRemark(source.getIvcRemark());
    target.setPivcType(source.getIvcType());

    target.getLines().clear();
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
          line.setCounterpartType(source.getCounterpart().getCounterpartType());
        }
        target.getLines().add(line);
      }
    }

    target.getDefrayals().clear();
    if (source.getCashs() instanceof PersistentBag == false
        || ((PersistentBag) source.getCashs()).wasInitialized()) {
      for (BPaymentCashDefrayal cash : source.getCashs()) {
        PaymentCashDefrayal defrayal = writeCashDefrayal(cash);
        target.getDefrayals().add(defrayal);
      }
    }
    if (source.getDeposits() instanceof PersistentBag == false
        || ((PersistentBag) source.getDeposits()).wasInitialized()) {
      for (BPaymentDepositDefrayal deposit : source.getDeposits()) {
        PaymentDepositDefrayal defrayal = writeDepositDefrayal(deposit);
        target.getDefrayals().add(defrayal);
      }
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
    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      refreshBillPaymentInfo(bill);
    } else {
      refreshLinePaymentInfo(bill);
    }
  }

  private void refreshLines(BPayment bill) {
    for (BPaymentLine line : bill.getAccountLines()) {
      // 移除无效的付款方式行
      Iterator<BPaymentLineCash> iteratorCash = line.getCashs().iterator();
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

    for (BPaymentLine line : bill.getOverdueLines()) {
      // 移除无效的付款方式行
      Iterator<BPaymentLineCash> iteratorCash = line.getCashs().iterator();
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
    // // 移除明细行中的收款信息
    // for (BPaymentLine line : bill.getAccountLines()) {
    // line.getCashs().clear();
    // line.getDeposits().clear();
    // }
    // for (BPaymentLine line : bill.getOverdueLines()) {
    // line.getCashs().clear();
    // line.getDeposits().clear();
    // }

    refreshLines(bill);
    // 移除无效的实收行
    Iterator<BPaymentCashDefrayal> iteratorCash = bill.getCashs().iterator();
    while (iteratorCash.hasNext()) {
      BPaymentCashDefrayal cash = iteratorCash.next();
      if (cash.getPaymentType() == null || cash.getTotal() == null
          || cash.getTotal().compareTo(BigDecimal.ZERO) == 0)
        iteratorCash.remove();
    }
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
   * 刷新并合并按科目收款时明细行中的收款信息。
   */
  private void refreshLinePaymentInfo(BPayment bill) {
    refreshLines(bill);

    // 合并出单头的收款信息
    bill.aggreateCashsAndDepositsFromPaymentLine();
  }

  private PaymentAccountLine writeAccountLine(BPaymentAccountLine source) {
    PaymentAccountLine target = new PaymentAccountLine();
    target.setAcc1(BizAcc1Converter.getInstance().convert(source.getAcc1()));
    target.setAcc2(BizAcc2Converter.getInstance().convert(source.getAcc2()));
    target.getAcc2().setLocker(Accounts.NONE_LOCKER);
    target.getAcc2().setId(target.getAcc2().bizId().toId());

    target.setUuid(source.getUuid());
    target.setDefrayalTotal(source.getDefrayalTotal());
    target.setDepositTotal(source.getDepositTotal());
    target.setDepositSubject(BizUCNConverter.getInstance().convert(source.getDepositSubject()));
    target.setLineNumber(source.getLineNumber());
    target.setOverdueTotal(BizTotalConverter.getInstance().convert(source.getOverdueTotal()));
    target.setRemark(source.getRemark());
    target.setOriginTotal(BizTotalConverter.getInstance().convert(source.getOriginTotal()));
    target.setUnpayedTotal(BizTotalConverter.getInstance().convert(source.getUnpayedTotal()));
    target.setTotal(BizTotalConverter.getInstance().convert(source.getTotal()));
    target.setPivcCode(source.getIvcCode());
    target.setPivcDate(source.getIvcDate());
    target.setPivcNumber(source.getIvcNumber());
    target.setPivcTotal(BizTotalConverter.getInstance().convert(source.getIvcTotal()));
    target.setPivcType(source.getIvcType());

    if (source.getOverdues() instanceof PersistentBag == false
        || ((PersistentBag) source.getOverdues()).wasInitialized()) {
      for (BPaymentAccOverdue boverdue : source.getOverdues()) {
        PaymentAccOverdue overdue = writeAccOverdue(boverdue);
        target.getOverdues().add(overdue);
      }
    }

    for (BPaymentLineCash bdefrayal : source.getCashs()) {
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
    target.setDepositSubject(BizUCNConverter.getInstance().convert(source.getDepositSubject()));
    target.setLineNumber(source.getLineNumber());
    target.setRemark(source.getRemark());
    target.setUnpayedTotal(BizTotalConverter.getInstance().convert(source.getUnpayedTotal()));
    target.setTotal(BizTotalConverter.getInstance().convert(source.getTotal()));
    target.setPivcCode(source.getIvcCode());
    target.setPivcDate(source.getIvcDate());
    target.setPivcNumber(source.getIvcNumber());
    target.setPivcTotal(BizTotalConverter.getInstance().convert(source.getIvcTotal()));
    target.setPivcType(source.getIvcType());
    target.setBeginEndDate(new DateInterval(source.getBeginDate(), source.getEndDate()));
    target.setSubject(BizUCNConverter.getInstance().convert(source.getSubject()));
    target.setContract(BizUCNConverter.getInstance().convert(source.getContract()));
    target.setRate(TaxRate.newInstance(source.getRate()));
    target.setAccountId(source.getAccountId());

    for (BPaymentLineCash bdefrayal : source.getCashs()) {
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
    target.setContract(BizUCNConverter.getInstance().convert(source.getContract()));
    target.setDirection(source.getDirection());
    target.setIssueInvoice(source.isIssueInvoice());
    target.setSubject(BizUCNConverter.getInstance().convert(source.getSubject()));
    target.setTaxRate(BizTaxRateConverter.getInstance().convert(source.getTaxRate()));
    target.setTotal(BizTotalConverter.getInstance().convert(source.getTotal()));
    return target;
  }

  private PaymentOverdueLine writeOverdueLine(BPaymentOverdueLine source) {
    PaymentOverdueLine target = new PaymentOverdueLine();
    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    target.setCounterpart(BizUCNConverter.getInstance().convert(source.getCounterpart()));
    target.setContract(BizUCNConverter.getInstance().convert(source.getContract()));
    target.setDefrayalTotal(source.getDefrayalTotal());
    target.setDepositTotal(source.getDepositTotal());
    target.setDepositSubject(BizUCNConverter.getInstance().convert(source.getDepositSubject()));
    target.setIssueInvoice(source.isIssueInvoice());
    target.setLineNumber(source.getLineNumber());
    target.setRemark(source.getRemark());
    target.setSubject(BizUCNConverter.getInstance().convert(source.getSubject()));
    target.setTaxRate(BizTaxRateConverter.getInstance().convert(source.getTaxRate()));
    target.setUnpayedTotal(BizTotalConverter.getInstance().convert(source.getUnpayedTotal()));
    target.setTotal(BizTotalConverter.getInstance().convert(source.getTotal()));
    target.setOverdueTotal(BizTotalConverter.getInstance().convert(source.getOverdueTotal()));
    target.setAccountId(source.getAccountId());
    target.setUuid(source.getUuid());

    for (BPaymentLineCash bdefrayal : source.getCashs()) {
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
    BBank bank = source.getBank();
    if (bank != null) {
      target.setBankAccount(bank.getBankAccount());
      target.setBankCode(bank.getCode());
      target.setBankName(bank.getName());
    }
    target.setLineNumber(source.getLineNumber());
    target.setPaymentType(BizUCNConverter.getInstance().convert(source.getPaymentType()));
    target.setRemark(source.getRemark());
    target.setTotal(source.getTotal());
    return target;
  }

  private PaymentDepositDefrayal writeDepositDefrayal(BPaymentDepositDefrayal source) {
    PaymentDepositDefrayal target = new PaymentDepositDefrayal();
    target.setContract(BizUCNConverter.getInstance().convert(source.getContract()));
    target.setLineNumber(source.getLineNumber());
    target.setRemark(source.getRemark());
    target.setSubject(BizUCNConverter.getInstance().convert(source.getSubject()));
    target.setTotal(source.getTotal());
    return target;
  }

  private PaymentLineCash writeLineCash(BPaymentLineCash source) {
    PaymentLineCash target = new PaymentLineCash();
    if (source.getBank() != null) {
      target.setBankAccount(source.getBank().getBankAccount());
      target.setBankCode(source.getBank().getCode());
      target.setBankName(source.getBank().getName());
    }
    target.setItemNo(source.getItemNo());
    target.setLineUuid(source.getLineUuid());
    target.setPaymentType(BizUCNConverter.getInstance().convert(source.getPaymentType()));
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
    target.setContract(BizUCNConverter.getInstance().convert(source.getContract()));
    target.setItemNo(source.getItemNo());
    target.setLineUuid(source.getLineUuid());
    target.setRemark(source.getRemark());
    target.setSubject(BizUCNConverter.getInstance().convert(source.getSubject()));
    target.setTotal(source.getTotal());
    return target;
  }
}
