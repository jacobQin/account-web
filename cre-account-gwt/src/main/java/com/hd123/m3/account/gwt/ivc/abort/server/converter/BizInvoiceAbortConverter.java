/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BizInvoiceAbortConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月15日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.abort.server.converter;

import com.hd123.m3.account.gwt.ivc.abort.client.biz.BInvoiceAbort;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceLine;
import com.hd123.m3.account.service.ivc.abort.InvoiceAbort;
import com.hd123.m3.account.service.ivc.common.InvoiceLine;
import com.hd123.m3.commons.gwt.base.server.convert.BizEntityConverter;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * 
 * 发票作废单|转换器。
 * <p>
 * BInvoiceAbort-->InvoiceAbort.
 * 
 * @author lixiaohong
 * @since 1.7
 *
 */
public class BizInvoiceAbortConverter extends BizEntityConverter<BInvoiceAbort, InvoiceAbort>{
  private static BizInvoiceAbortConverter instance = null;

  private BizInvoiceAbortConverter() {
    super();
  }

  public static BizInvoiceAbortConverter getInstance() {
    if (instance == null)
      instance = new BizInvoiceAbortConverter();
    return instance;
  }
  
  @Override
  public InvoiceAbort convert(BInvoiceAbort source) throws ConversionException {
    if(source==null){
      return null;
    }
    // 作废明细转换器
    Converter<BInvoiceLine, InvoiceLine> lineConverter = ConverterBuilder.newBuilder(
        BInvoiceLine.class, InvoiceLine.class).build();
    // 发票作废单转换器
    Converter<BInvoiceAbort, InvoiceAbort> converter = ConverterBuilder
        .newBuilder(BInvoiceAbort.class, InvoiceAbort.class)
        .ignore("createInfo", "lastModifyInfo","accountUnit", "abortLines")
        .map("aborter", BizUCNConverter.getInstance()).build();
    InvoiceAbort target = converter.convert(source);
    target.setAbortLines(ArrayListConverter.newConverter(lineConverter).convert(
        source.getAbortLines()));
    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    super.inject(source, target);
    return target;
  }
}
