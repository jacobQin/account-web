/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BPaymentTypeReductionRatioBillConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年11月14日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.paymenttype.reductionratio;

import com.hd123.m3.account.service.paymenttype.reductionratio.PaymentTypeReductionRatioBill;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * PaymentTypeReductionRatioBill-->BPaymentTypeReductionRatioBill
 * 
 * @author mengyinkun
 * 
 */
public class BPaymentTypeReductionRatioBillConverter implements
    Converter<PaymentTypeReductionRatioBill, BPaymentTypeReductionRatioBill> {
  private static BPaymentTypeReductionRatioBillConverter instance = null;

  public static BPaymentTypeReductionRatioBillConverter getInstance() {
    if (instance == null)
      return new BPaymentTypeReductionRatioBillConverter();
    return instance;
  }

  @Override
  public BPaymentTypeReductionRatioBill convert(PaymentTypeReductionRatioBill source)
      throws ConversionException {
    if (source == null)
      return null;
    BPaymentTypeReductionRatioBill target = new BPaymentTypeReductionRatioBill();
    target.inject(source);
    target.setStore(source.getStore());
    target.setContract(source.getContract());
    target.setRemark(source.getRemark());
    target.setDetails(source.getLines());
    return target;
  }

}
