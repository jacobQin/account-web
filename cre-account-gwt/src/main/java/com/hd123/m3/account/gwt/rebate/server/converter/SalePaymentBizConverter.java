/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	SalePaymentBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月16日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.server.converter;

import com.hd123.m3.account.gwt.rebate.client.biz.BSalesPayment;
import com.hd123.m3.account.gwt.rebate.client.biz.BSalesReceiver;
import com.hd123.m3.account.service.rebate.SalesPayment;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author chenganbang
 */
public class SalePaymentBizConverter implements Converter<SalesPayment, BSalesPayment> {

  @Override
  public BSalesPayment convert(SalesPayment source) throws ConversionException {
    BSalesPayment target = new BSalesPayment();
    target.setPayment(UCNBizConverter.getInstance().convert(source.getPayment()));
    target.setBank(UCNBizConverter.getInstance().convert(source.getBank()));
    target.setTotal(source.getTotal());
    target.setReceiver(BSalesReceiver.valueOf(source.getReceiver().name()));
    return target;
  }

}
