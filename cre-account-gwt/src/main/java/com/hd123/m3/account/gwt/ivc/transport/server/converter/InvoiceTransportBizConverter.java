/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceTransportBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月15日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.transport.server.converter;

import com.hd123.m3.account.gwt.ivc.transport.client.biz.BInvoiceTransport;
import com.hd123.m3.account.gwt.ivc.transport.client.biz.BInvoiceTransportLine;
import com.hd123.m3.account.service.ivc.transport.InvoiceTransport;
import com.hd123.m3.account.service.ivc.transport.InvoiceTransportLine;
import com.hd123.m3.commons.gwt.base.server.convert.EntityBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * 发票调拨单|转换器。
 * <p>
 * InvoiceTransport-->BInvoiceTransport.
 * 
 * @author lixiaohong
 * @since 1.9
 * 
 */
public class InvoiceTransportBizConverter extends EntityBizConverter<InvoiceTransport, BInvoiceTransport> {
  private static InvoiceTransportBizConverter instance = null;

  private InvoiceTransportBizConverter() {
    super();
  }

  public static InvoiceTransportBizConverter getInstance() {
    if (instance == null)
      instance = new InvoiceTransportBizConverter();
    return instance;
  }

  @Override
  public BInvoiceTransport convert(InvoiceTransport source) throws ConversionException {
    if (source == null) {
      return null;
    }
    // 调拨明细转换器
    Converter<InvoiceTransportLine, BInvoiceTransportLine> lineConverter = ConverterBuilder
        .newBuilder(InvoiceTransportLine.class, BInvoiceTransportLine.class)
        .map("inAccountUnit", UCNBizConverter.getInstance())
        .map("receiver", UCNBizConverter.getInstance()).build();
    // 发票调拨单转换器
    Converter<InvoiceTransport, BInvoiceTransport> converter = ConverterBuilder
        .newBuilder(InvoiceTransport.class, BInvoiceTransport.class)
        .ignore("createInfo", "lastModifyInfo", "accountUnit", "transportLines")
        .map("transportor", UCNBizConverter.getInstance()).build();
    BInvoiceTransport target = converter.convert(source);
    target.setTransportLines(ArrayListConverter.newConverter(lineConverter).convert(
        source.getTransportLines()));
    target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
    super.inject(source, target);
    return target;
  }
}
