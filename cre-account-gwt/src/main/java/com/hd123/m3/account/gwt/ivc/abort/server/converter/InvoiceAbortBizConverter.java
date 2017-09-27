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
import com.hd123.m3.commons.gwt.base.server.convert.EntityBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
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
public class InvoiceAbortBizConverter extends EntityBizConverter<InvoiceAbort, BInvoiceAbort>{

  private static InvoiceAbortBizConverter instance = null;

  private InvoiceAbortBizConverter() {
    super();
  }

  public static InvoiceAbortBizConverter getInstance() {
    if (instance == null)
      instance = new InvoiceAbortBizConverter();
    return instance;
  }

  @Override
  public BInvoiceAbort convert(InvoiceAbort source) throws ConversionException {
    if (source == null) {
      return null;
    }
    // 作废明细转换器
    Converter<InvoiceLine, BInvoiceLine> lineConverter = ConverterBuilder.newBuilder(
        InvoiceLine.class, BInvoiceLine.class).build();
    // 发票作废单转换器
    Converter<InvoiceAbort, BInvoiceAbort> converter = ConverterBuilder
        .newBuilder(InvoiceAbort.class, BInvoiceAbort.class)
        .ignore("createInfo", "lastModifyInfo", "accountUnit", "abortLines")
        .map("aborter", UCNBizConverter.getInstance()).build();
    BInvoiceAbort target = converter.convert(source);
    target.setAbortLines(ArrayListConverter.newConverter(lineConverter).convert(
        source.getAbortLines()));
    target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
    super.inject(source, target);
    return target;
  }

}
