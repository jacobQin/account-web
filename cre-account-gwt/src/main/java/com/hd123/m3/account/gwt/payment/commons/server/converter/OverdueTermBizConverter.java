/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	OverdueTermBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-11 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.server.converter;

import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueTerm;
import com.hd123.m3.account.service.payment.PaymentOverdueTerm;
import com.hd123.m3.commons.gwt.base.server.tax.TaxRateBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author subinzhu
 * 
 */
public class OverdueTermBizConverter implements Converter<PaymentOverdueTerm, BPaymentOverdueTerm> {

  private static OverdueTermBizConverter instance = null;

  public static OverdueTermBizConverter getInstance() {
    if (instance == null)
      instance = new OverdueTermBizConverter();
    return instance;
  }

  @Override
  public BPaymentOverdueTerm convert(PaymentOverdueTerm source) throws ConversionException {
    if (source == null)
      return null;

    BPaymentOverdueTerm target = new BPaymentOverdueTerm();
    try {
      target.setContract(UCNBizConverter.getInstance().convert(source.getContract()));
      target.setSubject(UCNBizConverter.getInstance().convert(source.getSubject()));
      target.setDirection(source.getDirection());
      target.setRate(source.getRate());
      target.setTaxRate(TaxRateBizConverter.getInstance().convert(source.getTaxRate()));
      target.setInvoice(source.isInvoice());
      return target;
    } catch (Exception e) {
      throw new ConversionException(e.getMessage());
    }
  }

}
