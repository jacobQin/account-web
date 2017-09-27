/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BInvoiceReceive.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月5日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.receive.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.service.ivc.common.InvoiceLine;
import com.hd123.m3.account.service.ivc.common.InvoiceStandardBill;
import com.hd123.m3.account.service.ivc.receive.InvoiceReceive;
import com.hd123.m3.cre.controller.account.invoice.common.BInvoiceLine;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * @author wangyibo
 *
 */
public class BInvoiceReceive extends InvoiceStandardBill {
  
  private static final long serialVersionUID = -4027743402954822047L;
  
  private List<BInvoiceLine> receiveLines = new ArrayList<BInvoiceLine>();

  private UCN issuer;
  private UCN receiver;
  private Date receiveDate;

  private static final Converter<InvoiceReceive, BInvoiceReceive> CONVETER = ConverterBuilder.newBuilder(
      InvoiceReceive.class, BInvoiceReceive.class).build();
  
  public static BInvoiceReceive newInstance(InvoiceReceive source) {
    List<BInvoiceLine> lines = new ArrayList<BInvoiceLine>();
    BInvoiceReceive target =  CONVETER.convert(source);
    for (InvoiceLine line : source.getReceiveLines()) {
      lines.add(BInvoiceLine.newInstance(line));
    }
    target.setReceiveLines(lines);
    return target;
  }
  
  public UCN getIssuer() {
    return issuer;
  }

  public void setIssuer(UCN issuer) {
    this.issuer = issuer;
  }

  public UCN getReceiver() {
    return receiver;
  }

  public void setReceiver(UCN receiver) {
    this.receiver = receiver;
  }

  public Date getReceiveDate() {
    return receiveDate;
  }

  public void setReceiveDate(Date receiveDate) {
    this.receiveDate = receiveDate;
  }

  public List<BInvoiceLine> getReceiveLines() {
    return receiveLines;
  }

  public void setReceiveLines(List<BInvoiceLine> receiveLines) {
    this.receiveLines = receiveLines;
  }
  
  
}
