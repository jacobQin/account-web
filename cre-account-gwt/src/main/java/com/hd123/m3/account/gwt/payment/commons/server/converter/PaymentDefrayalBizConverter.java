/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentDefrayalBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-5 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.server.converter;

import com.hd123.m3.account.gwt.payment.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCashDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDepositDefrayal;
import com.hd123.m3.account.service.payment.PaymentCashDefrayal;
import com.hd123.m3.account.service.payment.PaymentDefrayal;
import com.hd123.m3.account.service.payment.PaymentDepositDefrayal;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.gwt.entity.server.BEntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author subinzhu
 * 
 */
public class PaymentDefrayalBizConverter extends
    BEntityConverter<PaymentDefrayal, BPaymentDefrayal> {

  private static PaymentDefrayalBizConverter instance;

  public static PaymentDefrayalBizConverter getInstance() {
    if (instance == null)
      instance = new PaymentDefrayalBizConverter();
    return instance;
  }

  @Override
  public BPaymentDefrayal convert(PaymentDefrayal source) throws ConversionException {
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
    BBank bank = new BBank();
    bank.setCode(source.getBankCode());
    bank.setName(source.getBankName());
    bank.setBankAccount(source.getBankAccount());
    target.setBank(bank);
    target.setLineNumber(source.getLineNumber());
    target.setPaymentType(UCNBizConverter.getInstance().convert(source.getPaymentType()));
    target.setRemark(source.getRemark());
    target.setTotal(source.getTotal());
    return target;
  }

  private BPaymentDefrayal convertDepositDefrayal(PaymentDepositDefrayal source) {
    BPaymentDepositDefrayal target = new BPaymentDepositDefrayal();
    target.inject(source);
    target.setContract(UCNBizConverter.getInstance().convert(source.getContract()));
    target.setLineNumber(source.getLineNumber());
    target.setRemark(source.getRemark());
    target.setSubject(UCNBizConverter.getInstance().convert(source.getSubject()));
    target.setTotal(source.getTotal());
    return target;
  }

}
