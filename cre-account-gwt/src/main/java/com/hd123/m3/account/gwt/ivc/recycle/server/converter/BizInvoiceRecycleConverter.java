/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BizInvoiceRecycleConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月11日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.server.converter;

import com.hd123.m3.account.gwt.ivc.recycle.client.biz.BInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.client.biz.BInvoiceRecycleLine;
import com.hd123.m3.account.service.ivc.recycle.InvoiceRecycle;
import com.hd123.m3.account.service.ivc.recycle.InvoiceRecycleLine;
import com.hd123.m3.commons.gwt.base.server.convert.BizEntityConverter;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * 发票回收单|转换器。
 * <p>
 * BInvoiceRecycle-->InvoiceRecycle.
 * 
 * @author lixiaohong
 * @since 1.9
 * 
 */
public class BizInvoiceRecycleConverter extends BizEntityConverter<BInvoiceRecycle, InvoiceRecycle> {
  private static BizInvoiceRecycleConverter instance = null;

  private BizInvoiceRecycleConverter() {
    super();
  }

  public static BizInvoiceRecycleConverter getInstance() {
    if (instance == null)
      instance = new BizInvoiceRecycleConverter();
    return instance;
  }

  @Override
  public InvoiceRecycle convert(BInvoiceRecycle source) throws ConversionException {
    if (source == null) {
      return null;
    }
    // 回收明细转换器
    Converter<BInvoiceRecycleLine, InvoiceRecycleLine> lineConverter = ConverterBuilder.newBuilder(
        BInvoiceRecycleLine.class, InvoiceRecycleLine.class).build();
    // 发票回收单转换器
    Converter<BInvoiceRecycle, InvoiceRecycle> converter = ConverterBuilder
        .newBuilder(BInvoiceRecycle.class, InvoiceRecycle.class)
        .ignore("createInfo", "lastModifyInfo", "accountUnit", "lines")
        .map("receiver", BizUCNConverter.getInstance())
        .map("returnor", BizUCNConverter.getInstance()).build();
    InvoiceRecycle target = converter.convert(source);
    target.setLines(ArrayListConverter.newConverter(lineConverter).convert(source.getLines()));
    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    super.inject(source, target);
    return target;
  }
}
