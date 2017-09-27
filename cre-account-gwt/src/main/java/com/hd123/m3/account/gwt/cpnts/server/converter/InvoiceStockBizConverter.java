/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceStockBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月10日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.server.converter;

import com.hd123.m3.account.gwt.cpnts.client.ui.invoice.BInvoiceStock;
import com.hd123.m3.account.gwt.cpnts.client.ui.invoice.BInvoiceStockState;
import com.hd123.m3.account.service.ivc.stock.InvoiceStock;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 */
public class InvoiceStockBizConverter implements Converter<InvoiceStock, BInvoiceStock> {

  @Override
  public BInvoiceStock convert(InvoiceStock source) throws ConversionException {
    BInvoiceStock target = new BInvoiceStock();
    target.setUuid(source.getUuid());
    target.setInvoiceCode(source.getInvoiceCode());
    target.setInvoiceNumber(source.getInvoiceNumber());
    target.setInvoiceType(source.getInvoiceType());
    target.setAccountUnit(new UCNBizConverter().convert(source.getAccountUnit()));
    target.setHolder(new UCNBizConverter().convert(source.getHolder()));
    target.setState(source.getState() == null ? null : BInvoiceStockState.valueOf(source.getState()
        .name()));
    target.setAmount(source.getAmount());
    target.setUseType(source.getUseType());
    target.setBalanceCode(source.getBalanceCode());
    target.setBalanceNumber(source.getBalanceNumber());
    target.setInstockTime(source.getInstockTime());
    target.setAbortDate(source.getAbortDate());
    return target;
  }
}
