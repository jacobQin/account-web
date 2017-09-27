/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceStockBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月10日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.stock.server.converter;

import com.hd123.m3.account.gwt.ivc.stock.client.biz.BInvoiceStock;
import com.hd123.m3.account.service.ivc.stock.InvoiceStock;
import com.hd123.m3.commons.gwt.base.server.convert.EntityBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * 发票库存|转换器。
 * <p>
 * InvoiceStock-->BInvoiceStock.
 * 
 * @author lixiaohong
 * @since 1.7
 * 
 */
public class InvoiceStockBizConverter extends EntityBizConverter<InvoiceStock, BInvoiceStock> {
  private static InvoiceStockBizConverter instance = null;

  private InvoiceStockBizConverter() {
    super();
  }

  public static InvoiceStockBizConverter getInstance() {
    if (instance == null)
      instance = new InvoiceStockBizConverter();
    return instance;
  }

  @Override
  public BInvoiceStock convert(InvoiceStock source) throws ConversionException {
    Converter<InvoiceStock, BInvoiceStock> converter = ConverterBuilder
        .newBuilder(InvoiceStock.class, BInvoiceStock.class)
        .ignore("createInfo", "lastModifyInfo", "accountUnit","state")
        .map("holder", UCNBizConverter.getInstance()).build();
    BInvoiceStock target = converter.convert(source);
    target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
    target.setState(source.getState().name());
    super.inject(source, target);
    return target;
  }
}
