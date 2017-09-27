/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	PaymentTypeReductionRatioBillConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年11月15日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.paymenttype.reductionratio;

import com.hd123.m3.account.service.paymenttype.reductionratio.PaymentTypeReductionRatioBill;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * BPaymentTypeReductionRatioBill-->PaymentTypeReductionRatioBill
 * 
 * @author mengyinkun
 * 
 */
public class PaymentTypeReductionRatioBillConverter implements
    Converter<BPaymentTypeReductionRatioBill, PaymentTypeReductionRatioBill> {
  private static PaymentTypeReductionRatioBillConverter instance = null;

  public static PaymentTypeReductionRatioBillConverter getInstance() {
    if (instance == null)
      return new PaymentTypeReductionRatioBillConverter();
    return instance;
  }

  @Override
  public PaymentTypeReductionRatioBill convert(BPaymentTypeReductionRatioBill source)
      throws ConversionException {
    if (source == null)
      return null;
    PaymentTypeReductionRatioBill target = new PaymentTypeReductionRatioBill();
    target.inject(source);
    target.setContract(source.getContract());
    target.setStore(source.getStore());
    target.setLines(source.getDetails());
    target.setRemark(source.getRemark());
    return target;
  }

}
