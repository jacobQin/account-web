/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BizInvoiceReturnConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.returns.server.converter;

import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceLine;
import com.hd123.m3.account.gwt.ivc.returns.client.biz.BInvoiceReturn;
import com.hd123.m3.account.service.ivc.common.InvoiceLine;
import com.hd123.m3.account.service.ivc.returns.InvoiceReturn;
import com.hd123.m3.commons.gwt.base.server.convert.BizEntityConverter;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * 发票领用单|转换器。
 * <p>
 * BInvoiceReturn-->InvoiceReturn.
 * 
 * @author lixiaohong
 * @since 1.0
 * 
 */
public class BizInvoiceReturnConverter extends BizEntityConverter<BInvoiceReturn, InvoiceReturn> {
  private static BizInvoiceReturnConverter instance = null;

  private BizInvoiceReturnConverter() {
    super();
  }

  public static BizInvoiceReturnConverter getInstance() {
    if (instance == null)
      instance = new BizInvoiceReturnConverter();
    return instance;
  }

  @Override
  public InvoiceReturn convert(BInvoiceReturn source) throws ConversionException {
    if (source == null) {
      return null;
    }
    // 发票明细转换器
    Converter<BInvoiceLine, InvoiceLine> lineConverter = ConverterBuilder.newBuilder(
        BInvoiceLine.class, InvoiceLine.class).build();
    // 发票入库单转换器
    Converter<BInvoiceReturn, InvoiceReturn> converter = ConverterBuilder
        .newBuilder(BInvoiceReturn.class, InvoiceReturn.class)
        .ignore("createInfo", "lastModifyInfo", "accountUnit", "returnLines")
        .map("receiver", BizUCNConverter.getInstance())
        .map("returnor", BizUCNConverter.getInstance()).build();
    InvoiceReturn target = converter.convert(source);
    target.setReturnLines(ArrayListConverter.newConverter(lineConverter).convert(
        source.getReturnLines()));
    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    super.inject(source, target);
    return target;
  }
}
