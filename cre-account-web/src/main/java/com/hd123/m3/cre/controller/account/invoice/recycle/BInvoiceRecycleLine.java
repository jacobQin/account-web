/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BRecycleLine.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月8日 - xiahongjian - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.recycle;

import com.hd123.m3.account.service.ivc.recycle.InvoiceRecycleLine;
import com.hd123.m3.cre.controller.account.invoice.common.BInvoiceLine;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * @author xiahongjian
 *
 */
public class BInvoiceRecycleLine extends BInvoiceLine {
  private static final long serialVersionUID = -8282423213205280216L;
  private int realQty;

  private static final Converter<InvoiceRecycleLine, BInvoiceRecycleLine> CONVERTER = ConverterBuilder
      .newBuilder(InvoiceRecycleLine.class, BInvoiceRecycleLine.class)
      .build();

  public static BInvoiceRecycleLine newInstance(InvoiceRecycleLine source) {
    return CONVERTER.convert(source);
  }

  /** 实际回收张数 */
  public int getRealQty() {
    return realQty;
  }

  public void setRealQty(int realQty) {
    this.realQty = realQty;
  }
}
