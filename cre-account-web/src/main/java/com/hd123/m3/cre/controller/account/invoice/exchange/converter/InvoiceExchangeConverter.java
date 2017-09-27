/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BInvoiceExchangeConverter.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月28日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.exchange.converter;

import com.hd123.m3.account.service.ivc.exchange.InvoiceExchange;
import com.hd123.m3.cre.controller.account.invoice.exchange.biz.BInvoiceExchange;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author wangyibo
 *
 */
public class InvoiceExchangeConverter implements Converter<BInvoiceExchange, InvoiceExchange>{
  
  @Override
  public InvoiceExchange convert(BInvoiceExchange source) throws ConversionException {
    if(source == null)  {
      
    }
    return null;
  }

}
