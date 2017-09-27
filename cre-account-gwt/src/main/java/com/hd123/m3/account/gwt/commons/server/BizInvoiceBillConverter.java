/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BizInvoiceBillConverer.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.commons.server;

import com.hd123.m3.account.commons.InvoiceBill;
import com.hd123.m3.account.gwt.commons.client.biz.BInvoiceBill;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author chenwenfeng
 * 
 */
public class BizInvoiceBillConverter implements Converter<BInvoiceBill, InvoiceBill> {
  private static BizInvoiceBillConverter instance = null;

  public static BizInvoiceBillConverter getInstance() {
    if (instance == null)
      instance = new BizInvoiceBillConverter();
    return instance;
  }

  @Override
  public InvoiceBill convert(BInvoiceBill source) throws ConversionException {
    if (source == null)
      return null;
    InvoiceBill target = new InvoiceBill();
    target.setBillNumber(source.getBillNumber());
    target.setBillUuid(source.getBillUuid());
    target.setInvoiceCode(source.getInvoiceCode());
    target.setInvoiceNumber(source.getInvoiceNumber());
    target.setInvoiceType(source.getInvoiceType());
    return target;
  }

}
