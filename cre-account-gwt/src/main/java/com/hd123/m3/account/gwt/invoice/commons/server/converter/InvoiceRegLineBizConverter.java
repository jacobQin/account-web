/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegLineBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-27 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.server.converter;

import com.hd123.m3.account.gwt.acc.server.Acc1BizConverter;
import com.hd123.m3.account.gwt.acc.server.Acc2BizConverter;
import com.hd123.m3.account.gwt.commons.server.TotalBizConverter;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegLine;
import com.hd123.m3.account.service.invoice.InvoiceRegLine;
import com.hd123.rumba.commons.gwt.entity.server.BEntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author chenpeisi
 * 
 */
public class InvoiceRegLineBizConverter extends BEntityConverter<InvoiceRegLine, BInvoiceRegLine> {

  private static InvoiceRegLineBizConverter instance = null;

  public static InvoiceRegLineBizConverter getInstnce() {
    if (instance == null)
      instance = new InvoiceRegLineBizConverter();
    return instance;
  }

  @Override
  public BInvoiceRegLine convert(InvoiceRegLine source) throws ConversionException {
    if (source == null)
      return null;
    try {
      BInvoiceRegLine target = new BInvoiceRegLine();
      inject(source, target);

      target.setLineNumber(source.getLineNumber());
      target.setRemark(source.getRemark());
      target.setRegTotal(TotalBizConverter.getInstance().convert(source.getTotal()));
      target.setUnregTotal(TotalBizConverter.getInstance().convert(source.getUnregTotal()));
      target.setAcc1(Acc1BizConverter.getInstance().convert(source.getAcc1()));
      target.setAcc2(Acc2BizConverter.getInstance().convert(source.getAcc2()));

      return target;
    } catch (Exception e) {
      throw new ConversionException(e);
    }
  }
}
