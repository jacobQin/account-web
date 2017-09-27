/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceReturn.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月18日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.returns.client.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceLine;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceStandardBill;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * B对象|发票领退单
 * 
 * @author lixiaohong
 * @since 1.7
 * 
 */
public class BInvoiceReturn extends BInvoiceStandardBill {

  private static final long serialVersionUID = 7120233116852240244L;

  private BUCN receiver;
  private BUCN returnor;
  private Date returnDate;

  private List<BInvoiceLine> returnLines = new ArrayList<BInvoiceLine>();

  /** 领用人 */
  public BUCN getReceiver() {
    return receiver;
  }

  public void setReceiver(BUCN receiver) {
    this.receiver = receiver;
  }

  /** 退票人 */
  public BUCN getReturnor() {
    return returnor;
  }

  public void setReturnor(BUCN returnor) {
    this.returnor = returnor;
  }

  /** 领退日期 */
  public Date getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(Date returnDate) {
    this.returnDate = returnDate;
  }

  /** 领退明细 */
  public List<BInvoiceLine> getReturnLines() {
    return returnLines;
  }

  public void setReturnLines(List<BInvoiceLine> returnLines) {
    this.returnLines = returnLines;
  }
}
