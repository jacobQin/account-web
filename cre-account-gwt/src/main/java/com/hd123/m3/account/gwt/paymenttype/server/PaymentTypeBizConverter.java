/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	PaymentTypeBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymenttype.server;

import com.hd123.m3.account.gwt.paymenttype.client.biz.BPaymentType;
import com.hd123.m3.account.service.paymenttype.PaymentType;
import com.hd123.rumba.commons.gwt.entity.server.BStandardEntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * 付款方式转换器
 * 
 * PaymentType ---> BPaymentType
 * 
 * @author zhuhairui
 * 
 */
public class PaymentTypeBizConverter extends BStandardEntityConverter<PaymentType, BPaymentType> {

  private static PaymentTypeBizConverter instance = null;

  public static PaymentTypeBizConverter getInstance() {
    if (instance == null)
      instance = new PaymentTypeBizConverter();
    return instance;
  }

  @Override
  public BPaymentType convert(PaymentType source) throws ConversionException {
    if (source == null)
      return null;

    BPaymentType target = new BPaymentType();
    super.inject(source, target);
    target.setCode(source.getCode());
    target.setName(source.getName());
    target.setEnabled(source.isEnabled());
    target.setRemark(source.getRemark());

    return target;
  }

}
