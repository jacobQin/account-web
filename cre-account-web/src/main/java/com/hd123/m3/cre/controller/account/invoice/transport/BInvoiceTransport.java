/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BInvoiceTransport.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月8日 - xiahongjian - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.transport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.service.ivc.common.InvoiceStandardBill;
import com.hd123.m3.account.service.ivc.transport.InvoiceTransport;
import com.hd123.m3.account.service.ivc.transport.InvoiceTransportLine;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * @author xiahongjian
 *
 */
public class BInvoiceTransport extends InvoiceStandardBill {
  private static final long serialVersionUID = 1209123336766472534L;

  private UCN transportor;
  private Date transportDate;

  private List<BInvoiceTransportLine> transportLines = new ArrayList<BInvoiceTransportLine>();

  private static final Converter<InvoiceTransport, BInvoiceTransport> CONVERTER = ConverterBuilder
      .newBuilder(InvoiceTransport.class, BInvoiceTransport.class)
      .build();

  public static BInvoiceTransport newInstance(InvoiceTransport source) {
    List<BInvoiceTransportLine> lines = new ArrayList<BInvoiceTransportLine>();
    BInvoiceTransport target = CONVERTER.convert(source);
    if (source != null) {
      for (InvoiceTransportLine line : source.getTransportLines()) {
        lines.add(BInvoiceTransportLine.newInstance(line));
      }      
    }
    target.setTransportLines(lines);
    return target;
  }
  
  /** 调拨人 */
  public UCN getTransportor() {
    return transportor;
  }

  public void setTransportor(UCN transportor) {
    this.transportor = transportor;
  }

  /** 调拨日期 */
  public Date getTransportDate() {
    return transportDate;
  }

  public void setTransportDate(Date transportDate) {
    this.transportDate = transportDate;
  }

  /** 调拨明细 */
  public List<BInvoiceTransportLine> getTransportLines() {
    return transportLines;
  }

  public void setTransportLines(List<BInvoiceTransportLine> transportLines) {
    this.transportLines = transportLines;
  }

}
