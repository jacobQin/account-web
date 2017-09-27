/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BizInvoiceReceiveConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.receive.server.converter;

import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceLine;
import com.hd123.m3.account.gwt.ivc.receive.client.biz.BInvoiceReceive;
import com.hd123.m3.account.service.ivc.common.InvoiceLine;
import com.hd123.m3.account.service.ivc.receive.InvoiceReceive;
import com.hd123.m3.commons.gwt.base.server.convert.BizEntityConverter;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * 发票领用单|转换器。
 * <p>
 * BInvoiceReceive-->InvoiceReceive.
 * 
 * @author lixiaohong
 * @since 1.0
 * 
 */
public class BizInvoiceReceiveConverter extends BizEntityConverter<BInvoiceReceive, InvoiceReceive>{
  private static BizInvoiceReceiveConverter instance = null;

  private BizInvoiceReceiveConverter() {
    super();
  }

  public static BizInvoiceReceiveConverter getInstance() {
    if (instance == null)
      instance = new BizInvoiceReceiveConverter();
    return instance;
  }
  
  @Override
  public InvoiceReceive convert(BInvoiceReceive source) throws ConversionException {
    if (source == null) {
      return null;
    }
    // 发票明细转换器
    Converter<BInvoiceLine, InvoiceLine> lineConverter = ConverterBuilder.newBuilder(
        BInvoiceLine.class, InvoiceLine.class).build();
    // 发票入库单转换器
    Converter<BInvoiceReceive, InvoiceReceive> converter = ConverterBuilder
        .newBuilder(BInvoiceReceive.class, InvoiceReceive.class)
        .ignore("createInfo", "lastModifyInfo","accountUnit", "receiveLines")
        .map("issuer", BizUCNConverter.getInstance())
        .map("receiver", BizUCNConverter.getInstance()).build();
    InvoiceReceive target = converter.convert(source);
    target.setReceiveLines(ArrayListConverter.newConverter(lineConverter).convert(
        source.getReceiveLines()));
    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    super.inject(source, target);
    return target;
  }
}
