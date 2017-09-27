/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BInvoiceTransportLine.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月8日 - xiahongjian - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.transport;

import com.hd123.m3.account.service.ivc.transport.InvoiceTransportLine;
import com.hd123.m3.cre.controller.account.invoice.common.BInvoiceLine;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * @author xiahongjian
 *
 */
public class BInvoiceTransportLine extends BInvoiceLine {
  private static final long serialVersionUID = 7294399788588075152L;
  private UCN inAccountUnit;
  private UCN receiver;

  private static final Converter<InvoiceTransportLine, BInvoiceTransportLine> CONVERTER = ConverterBuilder
      .newBuilder(InvoiceTransportLine.class, BInvoiceTransportLine.class)
      .build();

  public static BInvoiceTransportLine newInstance(InvoiceTransportLine source) {
    return CONVERTER.convert(source);
  }
  
  
  /** 调入项目 */
  public UCN getInAccountUnit() {
    return inAccountUnit;
  }

  public void setInAccountUnit(UCN inAccountUnit) {
    this.inAccountUnit = inAccountUnit;
  }

  /** 领用人 */
  public UCN getReceiver() {
    return receiver;
  }

  public void setReceiver(UCN receiver) {
    this.receiver = receiver;
  }
}
