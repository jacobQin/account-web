/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceReceive.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.receive.client.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceLine;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceStandardBill;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * B对象|发票领用单
 * 
 * @author lixiaohong
 * @since 1.7
 *
 */
public class BInvoiceReceive extends BInvoiceStandardBill{
  private static final long serialVersionUID = 1084446363761469663L;
  private BUCN issuer;
  private BUCN receiver;
  private Date receiveDate;
  
  private List<BInvoiceLine> receiveLines = new ArrayList<BInvoiceLine>();

  /** 发放人 */
  public BUCN getIssuer() {
    return issuer;
  }

  public void setIssuer(BUCN issuer) {
    this.issuer = issuer;
  }

  /** 领用人 */
  public BUCN getReceiver() {
    return receiver;
  }

  public void setReceiver(BUCN receiver) {
    this.receiver = receiver;
  }

  /** 领用日期 */
  public Date getReceiveDate() {
    return receiveDate;
  }

  public void setReceiveDate(Date receiveDate) {
    this.receiveDate = receiveDate;
  }

  /** 发票领用单明细 */
  public List<BInvoiceLine> getReceiveLines() {
    return receiveLines;
  }

  public void setReceiveLines(List<BInvoiceLine> receiveLines) {
    this.receiveLines = receiveLines;
  }
}
