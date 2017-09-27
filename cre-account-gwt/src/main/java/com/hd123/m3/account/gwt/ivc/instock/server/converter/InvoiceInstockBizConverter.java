/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceInstockBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.instock.server.converter;

import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceLine;
import com.hd123.m3.account.gwt.ivc.instock.client.biz.BInvoiceInstock;
import com.hd123.m3.account.service.ivc.common.InvoiceLine;
import com.hd123.m3.account.service.ivc.instock.InvoiceInstock;
import com.hd123.m3.commons.gwt.base.server.convert.EntityBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * 发票入库单|转换器。
 * <p>
 * InvoiceInstock-->BInvoiceInstock.
 * 
 * @author lixiaohong
 * @since 1.0
 * 
 */
public class InvoiceInstockBizConverter extends EntityBizConverter<InvoiceInstock, BInvoiceInstock> {

  private static InvoiceInstockBizConverter instance = null;

  private InvoiceInstockBizConverter() {
    super();
  }

  public static InvoiceInstockBizConverter getInstance() {
    if (instance == null)
      instance = new InvoiceInstockBizConverter();
    return instance;
  }

  @Override
  public BInvoiceInstock convert(InvoiceInstock source) throws ConversionException {
    if (source == null) {
      return null;
    }
    // 发票明细转换器
    Converter<InvoiceLine, BInvoiceLine> lineConverter = ConverterBuilder.newBuilder(
        InvoiceLine.class, BInvoiceLine.class).build();
    // 发票入库单转换器
    Converter<InvoiceInstock, BInvoiceInstock> converter = ConverterBuilder
        .newBuilder(InvoiceInstock.class, BInvoiceInstock.class)
        .ignore("createInfo", "lastModifyInfo", "accountUnit", "instockLines")
        .map("instockor", UCNBizConverter.getInstance()).build();
    BInvoiceInstock target = converter.convert(source);
    target.setInstockLines(ArrayListConverter.newConverter(lineConverter).convert(
        source.getInstockLines()));
    target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
    super.inject(source, target);
    return target;
  }

}
