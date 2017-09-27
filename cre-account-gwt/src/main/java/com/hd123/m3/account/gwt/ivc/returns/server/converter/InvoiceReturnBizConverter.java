/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceReturnBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.returns.server.converter;

import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceLine;
import com.hd123.m3.account.gwt.ivc.returns.client.biz.BInvoiceReturn;
import com.hd123.m3.account.service.ivc.common.InvoiceLine;
import com.hd123.m3.account.service.ivc.returns.InvoiceReturn;
import com.hd123.m3.commons.gwt.base.server.convert.EntityBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * 发票入库单|转换器。
 * <p>
 * InvoiceReturn-->BInvoiceReturn.
 * 
 * @author lixiaohong
 * @since 1.0
 * 
 */
public class InvoiceReturnBizConverter extends EntityBizConverter<InvoiceReturn, BInvoiceReturn> {

  private static InvoiceReturnBizConverter instance = null;

  private InvoiceReturnBizConverter() {
    super();
  }

  public static InvoiceReturnBizConverter getInstance() {
    if (instance == null)
      instance = new InvoiceReturnBizConverter();
    return instance;
  }

  @Override
  public BInvoiceReturn convert(InvoiceReturn source) throws ConversionException {
    if (source == null) {
      return null;
    }
    // 发票明细转换器
    Converter<InvoiceLine, BInvoiceLine> lineConverter = ConverterBuilder.newBuilder(
        InvoiceLine.class, BInvoiceLine.class).build();
    // 发票入库单转换器
    Converter<InvoiceReturn, BInvoiceReturn> converter = ConverterBuilder
        .newBuilder(InvoiceReturn.class, BInvoiceReturn.class)
        .ignore("createInfo", "lastModifyInfo", "accountUnit", "returnLines")
        .map("receiver", UCNBizConverter.getInstance())
        .map("returnor", UCNBizConverter.getInstance()).build();
    BInvoiceReturn target = converter.convert(source);
    target.setReturnLines(ArrayListConverter.newConverter(lineConverter).convert(
        source.getReturnLines()));
    target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
    super.inject(source, target);
    return target;
  }

}
