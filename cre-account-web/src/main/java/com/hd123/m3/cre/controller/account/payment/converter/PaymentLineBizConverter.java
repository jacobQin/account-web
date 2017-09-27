package com.hd123.m3.cre.controller.account.payment.converter;

import com.hd123.m3.account.service.bank.Bank;
import com.hd123.m3.account.service.payment.PaymentAccOverdue;
import com.hd123.m3.account.service.payment.PaymentAccountLine;
import com.hd123.m3.account.service.payment.PaymentCollectionLine;
import com.hd123.m3.account.service.payment.PaymentLine;
import com.hd123.m3.account.service.payment.PaymentLineCash;
import com.hd123.m3.account.service.payment.PaymentLineDeduction;
import com.hd123.m3.account.service.payment.PaymentLineDefrayal;
import com.hd123.m3.account.service.payment.PaymentLineDeposit;
import com.hd123.m3.account.service.payment.PaymentOverdueLine;
import com.hd123.m3.account.service.payment.PaymentOverdueTerm;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentAccOverdue;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentAccountLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentCollectionLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentLineCash;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentLineDeduction;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentLineDeposit;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentOverdueLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentOverdueTerm;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * @author Libin
 * 
 */
public class PaymentLineBizConverter implements Converter<PaymentLine, BPaymentLine> {

  private static PaymentLineBizConverter instance;

  public static PaymentLineBizConverter getInstance() {
    if (instance == null)
      instance = new PaymentLineBizConverter();
    return instance;
  }

  @Override
  public BPaymentLine convert(PaymentLine source) {
    if (source instanceof PaymentAccountLine) {
      return convertAccountLine((PaymentAccountLine) source);
    } else if (source instanceof PaymentOverdueLine) {
      return convertOverdueLine((PaymentOverdueLine) source);
    } else if (source instanceof PaymentCollectionLine) {
      return convertCollectionLine((PaymentCollectionLine) source);
    }
    return null;
  }

  private BPaymentAccountLine convertAccountLine(PaymentAccountLine source) {
    BPaymentAccountLine target = new BPaymentAccountLine();
    target.inject(source);
    target.setUuid(source.getUuid());
    target.setAcc1(source.getAcc1());
    target.setAcc2(source.getAcc2());
    target.setDefrayalTotal(source.getDefrayalTotal());
    target.setDepositTotal(source.getDepositTotal());
    target.setDepositSubject(source.getDepositSubject());
    target.setLineNumber(source.getLineNumber());
    target.setOverdueTotal(source.getOverdueTotal());
    target.setRemark(source.getRemark());
    target.setOriginTotal(source.getOriginTotal());
    target.setUnpayedTotal(source.getUnpayedTotal());
    target.setTotal(source.getTotal());
    target.setIvcCode(source.getPivcCode());
    target.setIvcDate(source.getPivcDate());
    target.setIvcNumber(source.getPivcNumber());
    target.setIvcTotal(source.getPivcTotal());
    target.setIvcType(source.getPivcType());
    target.setPriorityDeduction(source.isPriorityDeduction());

    for (PaymentAccOverdue overdue : source.getOverdues()) {
      BPaymentAccOverdue boverdue = convertAccOverdue(overdue);
      target.getOverdues().add(boverdue);
    }
    for (PaymentOverdueTerm term : source.getOverdueTerms()) {
      BPaymentOverdueTerm bterm = convertOverdueTerm(term);
      target.getOverdueTerms().add(bterm);
    }
    for (PaymentLineDefrayal defrayal : source.getDefrayals()) {
      if (defrayal instanceof PaymentLineCash) {
        BPaymentLineCash pcash = convertLineCash((PaymentLineCash) defrayal);
        pcash.setLineUuid(target.getUuid());
        target.getCashes().add(pcash);
      } else if (defrayal instanceof PaymentLineDeduction) {
        BPaymentLineDeduction pdeduction = convertLineDeduction((PaymentLineDeduction) defrayal);
        pdeduction.setLineUuid(target.getUuid());
        target.getDeductions().add(pdeduction);
      } else if (defrayal instanceof PaymentLineDeposit) {
        BPaymentLineDeposit pdeposit = convertLineDeposit((PaymentLineDeposit) defrayal);
        pdeposit.setLineUuid(target.getUuid());
        target.getDeposits().add(pdeposit);
      }
    }

    return target;
  }

  private BPaymentCollectionLine convertCollectionLine(PaymentCollectionLine source) {
    BPaymentCollectionLine target = new BPaymentCollectionLine();
    target.inject(source);
    target.setUuid(source.getUuid());
    target.setDefrayalTotal(source.getDefrayalTotal());
    target.setDepositTotal(source.getDepositTotal());
    target.setDepositSubject(source.getDepositSubject());
    target.setLineNumber(source.getLineNumber());
    target.setRemark(source.getRemark());
    target.setUnpayedTotal(source.getUnpayedTotal());
    target.setTotal(source.getTotal());
    target.setIvcCode(source.getPivcCode());
    target.setIvcNumber(source.getPivcNumber());
    target.setIvcType(source.getPivcType());
    target.setBeginDate(source.getBeginEndDate().getBeginDate());
    target.setEndDate(source.getBeginEndDate().getEndDate());
    target.setSubject(source.getSubject());
    target.setContract(source.getContract());
    target.setRate(source.getRate());
    target.setAccountId(source.getAccountId());

    for (PaymentLineDefrayal defrayal : source.getDefrayals()) {
      if (defrayal instanceof PaymentLineCash) {
        BPaymentLineCash pcash = convertLineCash((PaymentLineCash) defrayal);
        pcash.setLineUuid(target.getUuid());
        target.getCashes().add(pcash);
      } else if (defrayal instanceof PaymentLineDeduction) {
        BPaymentLineDeduction pdeduction = convertLineDeduction((PaymentLineDeduction) defrayal);
        pdeduction.setLineUuid(target.getUuid());
        target.getDeductions().add(pdeduction);
      } else if (defrayal instanceof PaymentLineDeposit) {
        BPaymentLineDeposit pdeposit = convertLineDeposit((PaymentLineDeposit) defrayal);
        pdeposit.setLineUuid(target.getUuid());
        target.getDeposits().add(pdeposit);
      }
    }

    return target;
  }

  private BPaymentOverdueTerm convertOverdueTerm(PaymentOverdueTerm source) {
    BPaymentOverdueTerm target = new BPaymentOverdueTerm();
    target.setContract(source.getContract());
    target.setSubject(source.getSubject());
    target.setDirection(source.getDirection());
    target.setRate(source.getRate());
    target.setTaxRate(source.getTaxRate());
    target.setInvoice(source.isInvoice());
    target.setAllSubjects(source.isAllSubjects());

    target.setSubjects(source.getSubjects());

    return target;
  }

  private BPaymentOverdueLine convertOverdueLine(PaymentOverdueLine source) {
    BPaymentOverdueLine target = new BPaymentOverdueLine();
    target.inject(source);
    target.setUuid(source.getUuid());
    target.setAccountUnit(source.getAccountUnit());
    target.setCounterpart(source.getCounterpart());
    target.setContract(source.getContract());
    target.setDefrayalTotal(source.getDefrayalTotal());
    target.setDepositTotal(source.getDepositTotal());
    target.setDepositSubject(source.getDepositSubject());
    target.setIssueInvoice(source.isIssueInvoice());
    target.setLineNumber(source.getLineNumber());
    target.setUnpayedTotal(source.getUnpayedTotal());
    target.setRemark(source.getRemark());
    target.setSubject(source.getSubject());
    target.setTaxRate(source.getTaxRate());
    target.setTotal(source.getTotal());
    target.setOverdueTotal(source.getOverdueTotal());
    target.setAccountId(source.getAccountId());

    for (PaymentLineDefrayal defrayal : source.getDefrayals()) {
      if (defrayal instanceof PaymentLineCash) {
        BPaymentLineCash pcash = convertLineCash((PaymentLineCash) defrayal);
        pcash.setLineUuid(target.getUuid());
        target.getCashes().add(pcash);
      } else if (defrayal instanceof PaymentLineDeduction) {
        BPaymentLineDeduction pdeduction = convertLineDeduction((PaymentLineDeduction) defrayal);
        pdeduction.setLineUuid(target.getUuid());
        target.getDeductions().add(pdeduction);
      } else if (defrayal instanceof PaymentLineDeposit) {
        BPaymentLineDeposit pdeposit = convertLineDeposit((PaymentLineDeposit) defrayal);
        pdeposit.setLineUuid(target.getUuid());
        target.getDeposits().add(pdeposit);
      }
    }

    return target;
  }

  private BPaymentAccOverdue convertAccOverdue(PaymentAccOverdue source) {
    BPaymentAccOverdue target = new BPaymentAccOverdue();
    target.inject(source);
    target.setContract(source.getContract());
    target.setDirection(source.getDirection());
    target.setIssueInvoice(source.isIssueInvoice());
    target.setSubject(source.getSubject());
    target.setTaxRate(source.getTaxRate());
    target.setTotal(source.getTotal());
    return target;
  }

  private BPaymentLineCash convertLineCash(PaymentLineCash source) {
    BPaymentLineCash target = new BPaymentLineCash();
    target.inject(source);
    if (!StringUtil.isNullOrBlank(source.getBankCode())) {
      Bank bank = new Bank();
      bank.setCode(source.getBankCode());
      bank.setName(source.getBankName());
      bank.setAccount(source.getBankAccount());
      target.setBank(bank);
    }
    target.setItemNo(source.getItemNo());
    target.setLineUuid(source.getLineUuid());
    target.setPaymentType(source.getPaymentType());
    target.setPoundage(source.getPoundage());
    target.setRemark(source.getRemark());
    target.setTotal(source.getTotal());
    return target;
  }

  private BPaymentLineDeduction convertLineDeduction(PaymentLineDeduction source) {
    BPaymentLineDeduction target = new BPaymentLineDeduction();
    target.inject(source);
    target.setAccountId(source.getAccountId());
    target.setBizId(source.getBizId());
    target.setItemNo(source.getItemNo());
    target.setLineUuid(source.getLineUuid());
    target.setRemark(source.getRemark());
    target.setTotal(source.getTotal());
    return target;
  }

  private BPaymentLineDeposit convertLineDeposit(PaymentLineDeposit source) {
    BPaymentLineDeposit target = new BPaymentLineDeposit();
    target.inject(source);
    target.setContract(source.getContract());
    target.setItemNo(source.getItemNo());
    target.setLineUuid(source.getLineUuid());
    target.setRemark(source.getRemark());
    target.setSubject(source.getSubject());
    target.setTotal(source.getTotal());
    return target;
  }
}
