package com.hd123.m3.cre.controller.account.payment.converter;

import com.hd123.m3.account.service.bank.Bank;
import com.hd123.m3.account.service.payment.PaymentCashDefrayal;
import com.hd123.m3.account.service.payment.PaymentDefrayal;
import com.hd123.m3.account.service.payment.PaymentDepositDefrayal;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentCashDefrayal;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentDefrayal;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentDepositDefrayal;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author LiBin
 * 
 */
public class PaymentDefrayalBizConverter implements Converter<PaymentDefrayal, BPaymentDefrayal> {

  private static PaymentDefrayalBizConverter instance;

  public static PaymentDefrayalBizConverter getInstance() {
    if (instance == null)
      instance = new PaymentDefrayalBizConverter();
    return instance;
  }

  @Override
  public BPaymentDefrayal convert(PaymentDefrayal source) {
    if (source instanceof PaymentCashDefrayal) {
      return convertCashDefrayal((PaymentCashDefrayal) source);
    } else if (source instanceof PaymentDepositDefrayal) {
      return convertDepositDefrayal((PaymentDepositDefrayal) source);
    }
    return null;
  }

  private BPaymentDefrayal convertCashDefrayal(PaymentCashDefrayal source) {
    BPaymentCashDefrayal target = new BPaymentCashDefrayal();
    target.inject(source);

    if(StringUtil.isNullOrBlank(source.getBankCode()) == false){
      Bank bank = new Bank();
      bank.setCode(source.getBankCode());
      bank.setName(source.getBankName());
      bank.setAccount(source.getBankAccount());
      target.setBank(bank);
    }
    target.setLineNumber(source.getLineNumber());
    target.setPaymentType(source.getPaymentType());
    target.setPoundage(source.getPoundage());
    target.setRemark(source.getRemark());
    target.setTotal(source.getTotal());
    return target;
  }

  private BPaymentDefrayal convertDepositDefrayal(PaymentDepositDefrayal source) {
    BPaymentDepositDefrayal target = new BPaymentDepositDefrayal();
    target.inject(source);
    target.setContract(source.getContract());
    target.setLineNumber(source.getLineNumber());
    target.setRemark(source.getRemark());
    target.setSubject(source.getSubject());
    target.setTotal(source.getTotal());
    return target;
  }

}
