/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	BizSalePaymentConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月16日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.server.converter;

import com.hd123.m3.account.gwt.rebate.client.biz.BSalesPayment;
import com.hd123.m3.account.service.rebate.SalesPayment;
import com.hd123.m3.account.service.report.product.SalesReceiver;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author chenganbang
 */
public class BizSalePaymentConverter implements Converter<BSalesPayment, SalesPayment> {

  @Override
  public SalesPayment convert(BSalesPayment source) throws ConversionException {
    SalesPayment target = new SalesPayment();
    target.setPayment(BizUCNConverter.getInstance().convert(source.getPayment()));
    target.setBank(BizUCNConverter.getInstance().convert(source.getBank()));
    target.setTotal(source.getTotal());
    target.setReceiver(SalesReceiver.valueOf(source.getReceiver().name()));
    return target;
  }

}
