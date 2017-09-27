/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BizInvoiceTransportConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月15日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.transport.server.converter;

import com.hd123.m3.account.gwt.ivc.transport.client.biz.BInvoiceTransport;
import com.hd123.m3.account.gwt.ivc.transport.client.biz.BInvoiceTransportLine;
import com.hd123.m3.account.service.ivc.transport.InvoiceTransport;
import com.hd123.m3.account.service.ivc.transport.InvoiceTransportLine;
import com.hd123.m3.commons.gwt.base.server.convert.BizEntityConverter;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * 发票调拨单|转换器。
 * <p>
 * BInvoiceTransport-->InvoiceTransport.
 * 
 * @author lixiaohong
 * @since 1.9
 * 
 */
public class BizInvoiceTransportConverter extends
    BizEntityConverter<BInvoiceTransport, InvoiceTransport> {
  private static BizInvoiceTransportConverter instance = null;

  private BizInvoiceTransportConverter() {
    super();
  }

  public static BizInvoiceTransportConverter getInstance() {
    if (instance == null)
      instance = new BizInvoiceTransportConverter();
    return instance;
  }

  @Override
  public InvoiceTransport convert(BInvoiceTransport source) throws ConversionException {
    if (source == null) {
      return null;
    }
    // 调拨明细转换器
    Converter<BInvoiceTransportLine, InvoiceTransportLine> lineConverter = ConverterBuilder
        .newBuilder(BInvoiceTransportLine.class, InvoiceTransportLine.class)
        .map("inAccountUnit", BizUCNConverter.getInstance())
        .map("receiver", BizUCNConverter.getInstance()).build();
    // 发票调拨单转换器
    Converter<BInvoiceTransport, InvoiceTransport> converter = ConverterBuilder
        .newBuilder(BInvoiceTransport.class, InvoiceTransport.class)
        .ignore("createInfo", "lastModifyInfo", "accountUnit", "transportLines")
        .map("transportor", BizUCNConverter.getInstance()).build();
    InvoiceTransport target = converter.convert(source);
    target.setTransportLines(ArrayListConverter.newConverter(lineConverter).convert(
        source.getTransportLines()));
    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    super.inject(source, target);
    return target;
  }
}
