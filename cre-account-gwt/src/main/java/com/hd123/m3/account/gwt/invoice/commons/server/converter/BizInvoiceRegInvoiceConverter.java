/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BizDepositLineConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-20 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.server.converter;

import com.hd123.m3.account.gwt.commons.server.BizTotalConverter;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegInvoice;
import com.hd123.m3.account.service.invoice.InvoiceRegInvoice;
import com.hd123.m3.commons.gwt.base.server.tax.BizTaxRateConverter;
import com.hd123.rumba.commons.gwt.entity.server.EntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author chenpeisi
 * 
 */
public class BizInvoiceRegInvoiceConverter extends
    EntityConverter<BInvoiceRegInvoice, InvoiceRegInvoice> {
  private static BizInvoiceRegInvoiceConverter instance;

  public static BizInvoiceRegInvoiceConverter getInstance() {
    if (instance == null)
      instance = new BizInvoiceRegInvoiceConverter();
    return instance;
  }

  @Override
  public InvoiceRegInvoice convert(BInvoiceRegInvoice source) throws ConversionException {
    if (source == null)
      return null;

    InvoiceRegInvoice target = new InvoiceRegInvoice();
    super.inject(source, target);

    target.setLineNumber(source.getLineNumber());
    target.setInvoiceDate(source.getInvoiceDate());
    target.setInvoiceNumber(source.getInvoiceNumber());
    target.setInvoiceType(source.getInvoiceType());
    target.setRemark(source.getRemark());
    target.setTaxRate(BizTaxRateConverter.getInstance().convert(source.getTaxRate()));
    target.setTotal(BizTotalConverter.getInstance().convert(source.getTotal()));

    return target;
  }
}
