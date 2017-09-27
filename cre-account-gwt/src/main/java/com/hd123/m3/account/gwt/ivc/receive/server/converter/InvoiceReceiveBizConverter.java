/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceReceiveBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.receive.server.converter;

import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceLine;
import com.hd123.m3.account.gwt.ivc.receive.client.biz.BInvoiceReceive;
import com.hd123.m3.account.service.ivc.common.InvoiceLine;
import com.hd123.m3.account.service.ivc.receive.InvoiceReceive;
import com.hd123.m3.commons.gwt.base.server.convert.EntityBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * 发票入库单|转换器。
 * <p>
 * InvoiceReceive-->BInvoiceReceive.
 * 
 * @author lixiaohong
 * @since 1.0
 * 
 */
public class InvoiceReceiveBizConverter extends EntityBizConverter<InvoiceReceive, BInvoiceReceive> {

  private static InvoiceReceiveBizConverter instance = null;

  private InvoiceReceiveBizConverter() {
    super();
  }

  public static InvoiceReceiveBizConverter getInstance() {
    if (instance == null)
      instance = new InvoiceReceiveBizConverter();
    return instance;
  }

  @Override
  public BInvoiceReceive convert(InvoiceReceive source) throws ConversionException {
    if (source == null) {
      return null;
    }
    // 发票明细转换器
    Converter<InvoiceLine, BInvoiceLine> lineConverter = ConverterBuilder.newBuilder(
        InvoiceLine.class, BInvoiceLine.class).build();
    // 发票入库单转换器
    Converter<InvoiceReceive, BInvoiceReceive> converter = ConverterBuilder
        .newBuilder(InvoiceReceive.class, BInvoiceReceive.class)
        .ignore("createInfo", "lastModifyInfo", "accountUnit", "receiveLines")
        .map("issuer", UCNBizConverter.getInstance())
        .map("receiver", UCNBizConverter.getInstance()).build();
    BInvoiceReceive target = converter.convert(source);
    target.setReceiveLines(ArrayListConverter.newConverter(lineConverter).convert(
        source.getReceiveLines()));
    target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
    super.inject(source, target);
    return target;
  }

}
