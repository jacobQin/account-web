/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRecycleBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月15日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.server.converter;

import com.hd123.m3.account.gwt.ivc.recycle.client.biz.BInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.client.biz.BInvoiceRecycleLine;
import com.hd123.m3.account.service.ivc.recycle.InvoiceRecycle;
import com.hd123.m3.account.service.ivc.recycle.InvoiceRecycleLine;
import com.hd123.m3.commons.gwt.base.server.convert.EntityBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * 发票回收单|转换器。
 * <p>
 * InvoiceRecycle-->BInvoiceRecycle.
 * 
 * @author lixiaohong
 * @since 1.9
 * 
 */
public class InvoiceRecycleBizConverter extends EntityBizConverter<InvoiceRecycle, BInvoiceRecycle> {
  private static InvoiceRecycleBizConverter instance = null;

  private InvoiceRecycleBizConverter() {
    super();
  }

  public static InvoiceRecycleBizConverter getInstance() {
    if (instance == null)
      instance = new InvoiceRecycleBizConverter();
    return instance;
  }

  @Override
  public BInvoiceRecycle convert(InvoiceRecycle source) throws ConversionException {
    if (source == null) {
      return null;
    }
    // 回收明细转换器
    Converter<InvoiceRecycleLine, BInvoiceRecycleLine> lineConverter = ConverterBuilder.newBuilder(
        InvoiceRecycleLine.class, BInvoiceRecycleLine.class).build();
    // 发票回收单转换器
    Converter<InvoiceRecycle, BInvoiceRecycle> converter = ConverterBuilder
        .newBuilder(InvoiceRecycle.class, BInvoiceRecycle.class)
        .ignore("createInfo", "lastModifyInfo", "accountUnit", "lines")
        .map("receiver", UCNBizConverter.getInstance())
        .map("returnor", UCNBizConverter.getInstance()).build();
    BInvoiceRecycle target = converter.convert(source);
    target.setLines(ArrayListConverter.newConverter(lineConverter).convert(source.getLines()));
    fillBalanceQty(target);
    target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
    super.inject(source, target);
    return target;
  }

  /**
   * 为回收明细赋值差额
   * 
   * @param recycle
   */
  private void fillBalanceQty(BInvoiceRecycle recycle) {
    for (BInvoiceRecycleLine line : recycle.getLines()) {
      line.setBalanceQty(line.getQuantity() - line.getRealQty());
    }
  }
}
