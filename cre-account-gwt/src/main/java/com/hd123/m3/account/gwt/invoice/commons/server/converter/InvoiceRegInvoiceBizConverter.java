/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegInvoiceBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-27 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.server.converter;

import com.hd123.m3.account.gwt.commons.server.TotalBizConverter;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegInvoice;
import com.hd123.m3.account.service.invoice.InvoiceRegInvoice;
import com.hd123.m3.commons.gwt.base.server.tax.TaxRateBizConverter;
import com.hd123.rumba.commons.gwt.entity.server.BEntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author chenpeisi
 * 
 */
public class InvoiceRegInvoiceBizConverter extends
    BEntityConverter<InvoiceRegInvoice, BInvoiceRegInvoice> {
  private static InvoiceRegInvoiceBizConverter instance = null;

  public static InvoiceRegInvoiceBizConverter getInstnce() {
    if (instance == null)
      instance = new InvoiceRegInvoiceBizConverter();
    return instance;
  }

  @Override
  public BInvoiceRegInvoice convert(InvoiceRegInvoice source) throws ConversionException {
    if (source == null)
      return null;
    try {
      BInvoiceRegInvoice target = new BInvoiceRegInvoice();
      inject(source, target);

      target.setLineNumber(source.getLineNumber());
      target.setInvoiceDate(source.getInvoiceDate());
      target.setInvoiceNumber(source.getInvoiceNumber());
      target.setInvoiceType(source.getInvoiceType());
      target.setRemark(source.getRemark());

      target.setTaxRate(TaxRateBizConverter.getInstance().convert(source.getTaxRate()));
      target.setTotal(TotalBizConverter.getInstance().convert(source.getTotal()));

      return target;
    } catch (Exception e) {
      throw new ConversionException(e);
    }
  }
}
