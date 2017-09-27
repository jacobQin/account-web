/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BInvoiceLine.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月5日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.common;

import com.hd123.m3.account.service.ivc.common.InvoiceLine;
import com.hd123.m3.account.service.ivc.stock.InvoiceStock;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * 发票明细行页面B对象 （通用）
 * @author wangyibo
 *
 */
public class BInvoiceLine extends InvoiceLine{
  
  private static final long serialVersionUID = 1554425846824167746L;
  
  private static final Converter<InvoiceLine, BInvoiceLine> CONVETER = ConverterBuilder.newBuilder(
      InvoiceLine.class, BInvoiceLine.class).build();
  
  public static BInvoiceLine newInstance(InvoiceLine source) {
    return CONVETER.convert(source);
  }
  
  private InvoiceStock stockNumber;

  /**
   * 起始号码库存对象
   */
  public InvoiceStock getStockNumber() {
    return stockNumber;
  }

  public void setStockNumber(InvoiceStock stockNumber) {
    this.stockNumber = stockNumber;
  }
  
  
}
