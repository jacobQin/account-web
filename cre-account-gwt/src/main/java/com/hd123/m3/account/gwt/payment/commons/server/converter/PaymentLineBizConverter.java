/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentLineBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-5 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.server.converter;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.acc.server.Acc1BizConverter;
import com.hd123.m3.account.gwt.acc.server.Acc2BizConverter;
import com.hd123.m3.account.gwt.commons.server.TotalBizConverter;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccOverdue;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCollectionLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineCash;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDeduction;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDeposit;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueTerm;
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
import com.hd123.m3.commons.gwt.base.server.tax.TaxRateBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.gwt.entity.server.BEntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * @author subinzhu
 * 
 */
public class PaymentLineBizConverter extends BEntityConverter<PaymentLine, BPaymentLine> {

  private static PaymentLineBizConverter instance;

  public static PaymentLineBizConverter getInstance() {
    if (instance == null)
      instance = new PaymentLineBizConverter();
    return instance;
  }

  @Override
  public BPaymentLine convert(PaymentLine source) throws ConversionException {
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
    target.setAcc1(Acc1BizConverter.getInstance().convert(source.getAcc1()));
    target.setAcc2(Acc2BizConverter.getInstance().convert(source.getAcc2()));
    target.setDefrayalTotal(source.getDefrayalTotal());
    target.setDepositTotal(source.getDepositTotal());
    target.setDepositSubject(UCNBizConverter.getInstance().convert(source.getDepositSubject()));
    target.setLineNumber(source.getLineNumber());
    target.setOverdueTotal(TotalBizConverter.getInstance().convert(source.getOverdueTotal()));
    target.setRemark(source.getRemark());
    target.setOriginTotal(TotalBizConverter.getInstance().convert(source.getOriginTotal()));
    target.setUnpayedTotal(TotalBizConverter.getInstance().convert(source.getUnpayedTotal()));
    target.setTotal(TotalBizConverter.getInstance().convert(source.getTotal()));
    target.setIvcCode(source.getPivcCode());
    target.setIvcDate(source.getPivcDate());
    target.setIvcNumber(source.getPivcNumber());
    target.setIvcTotal(TotalBizConverter.getInstance().convert(source.getPivcTotal()));
    target.setIvcType(source.getPivcType());

    for (PaymentAccOverdue overdue : source.getOverdues()) {
      BPaymentAccOverdue boverdue = convertAccOverdue(overdue);
      boverdue.setLine(target);
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
        target.getCashs().add(pcash);
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
    target.setDepositSubject(UCNBizConverter.getInstance().convert(source.getDepositSubject()));
    target.setLineNumber(source.getLineNumber());
    target.setRemark(source.getRemark());
    target.setUnpayedTotal(TotalBizConverter.getInstance().convert(source.getUnpayedTotal()));
    target.setTotal(TotalBizConverter.getInstance().convert(source.getTotal()));
    target.setIvcCode(source.getPivcCode());
    target.setIvcDate(source.getPivcDate());
    target.setIvcNumber(source.getPivcNumber());
    target.setIvcTotal(TotalBizConverter.getInstance().convert(source.getPivcTotal()));
    target.setIvcType(source.getPivcType());
    target.setBeginDate(source.getBeginEndDate().getBeginDate());
    target.setEndDate(source.getBeginEndDate().getEndDate());
    target.setSubject(UCNBizConverter.getInstance().convert(source.getSubject()));
    target.setContract(UCNBizConverter.getInstance().convert(source.getContract()));
    target.setRate(TaxRateBizConverter.getInstance().convert(source.getRate()));
    target.setAccountId(source.getAccountId());

    for (PaymentLineDefrayal defrayal : source.getDefrayals()) {
      if (defrayal instanceof PaymentLineCash) {
        BPaymentLineCash pcash = convertLineCash((PaymentLineCash) defrayal);
        pcash.setLineUuid(target.getUuid());
        target.getCashs().add(pcash);
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
    target.setContract(UCNBizConverter.getInstance().convert(source.getContract()));
    target.setSubject(UCNBizConverter.getInstance().convert(source.getSubject()));
    target.setDirection(source.getDirection());
    target.setRate(source.getRate());
    target.setTaxRate(TaxRateBizConverter.getInstance().convert(source.getTaxRate()));
    target.setInvoice(source.isInvoice());
    target.setAllSubjects(source.isAllSubjects());

    List<BUCN> subjects = new ArrayList<BUCN>();
    for (UCN subject : source.getSubjects()) {
      subjects.add(UCNBizConverter.getInstance().convert(subject));
    }
    target.setSubjects(subjects);

    return target;
  }

  private BPaymentOverdueLine convertOverdueLine(PaymentOverdueLine source) {
    BPaymentOverdueLine target = new BPaymentOverdueLine();
    target.inject(source);
    target.setUuid(source.getUuid());
    target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
    target.setCounterpart(UCNBizConverter.getInstance().convert(source.getCounterpart()));
    target.setContract(UCNBizConverter.getInstance().convert(source.getContract()));
    target.setDefrayalTotal(source.getDefrayalTotal());
    target.setDepositTotal(source.getDepositTotal());
    target.setDepositSubject(UCNBizConverter.getInstance().convert(source.getDepositSubject()));
    target.setIssueInvoice(source.isIssueInvoice());
    target.setLineNumber(source.getLineNumber());
    target.setUnpayedTotal(TotalBizConverter.getInstance().convert(source.getUnpayedTotal()));
    target.setRemark(source.getRemark());
    target.setSubject(UCNBizConverter.getInstance().convert(source.getSubject()));
    target.setTaxRate(TaxRateBizConverter.getInstance().convert(source.getTaxRate()));
    target.setTotal(TotalBizConverter.getInstance().convert(source.getTotal()));
    target.setOverdueTotal(TotalBizConverter.getInstance().convert(source.getOverdueTotal()));
    target.setAccountId(source.getAccountId());

    for (PaymentLineDefrayal defrayal : source.getDefrayals()) {
      if (defrayal instanceof PaymentLineCash) {
        BPaymentLineCash pcash = convertLineCash((PaymentLineCash) defrayal);
        pcash.setLineUuid(target.getUuid());
        target.getCashs().add(pcash);
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
    target.setContract(UCNBizConverter.getInstance().convert(source.getContract()));
    target.setDirection(source.getDirection());
    target.setIssueInvoice(source.isIssueInvoice());
    target.setSubject(UCNBizConverter.getInstance().convert(source.getSubject()));
    target.setTaxRate(TaxRateBizConverter.getInstance().convert(source.getTaxRate()));
    target.setTotal(TotalBizConverter.getInstance().convert(source.getTotal()));
    return target;
  }

  private BPaymentLineCash convertLineCash(PaymentLineCash source) {
    BPaymentLineCash target = new BPaymentLineCash();
    target.inject(source);
    if (!StringUtil.isNullOrBlank(source.getBankCode())) {
      BBank bank = new BBank();
      bank.setCode(source.getBankCode());
      bank.setName(source.getBankName());
      bank.setBankAccount(source.getBankAccount());
      target.setBank(bank);
    }
    target.setItemNo(source.getItemNo());
    target.setLineUuid(source.getLineUuid());
    target.setPaymentType(UCNBizConverter.getInstance().convert(source.getPaymentType()));
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
    target.setContract(UCNBizConverter.getInstance().convert(source.getContract()));
    target.setItemNo(source.getItemNo());
    target.setLineUuid(source.getLineUuid());
    target.setRemark(source.getRemark());
    target.setSubject(UCNBizConverter.getInstance().convert(source.getSubject()));
    target.setTotal(source.getTotal());
    return target;
  }
}
