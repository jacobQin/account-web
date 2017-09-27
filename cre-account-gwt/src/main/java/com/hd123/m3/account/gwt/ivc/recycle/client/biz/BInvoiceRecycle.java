/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceReceive.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.client.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceStandardBill;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * B对象|发票回收单
 * 
 * @author lixiaohong
 * @since 1.9
 *
 */
public class BInvoiceRecycle extends BInvoiceStandardBill{
  private static final long serialVersionUID = 1084446363761469663L;
  private BUCN receiver;
  private BUCN returnor;
  private Date recycleDate;
  
  private List<BInvoiceRecycleLine> lines = new ArrayList<BInvoiceRecycleLine>();

  /**接受人*/
  public BUCN getReceiver() {
    return receiver;
  }

  public void setReceiver(BUCN receiver) {
    this.receiver = receiver;
  }

  /**退票人*/
  public BUCN getReturnor() {
    return returnor;
  }

  public void setReturnor(BUCN returnor) {
    this.returnor = returnor;
  }

  /**回收时间*/
  public Date getRecycleDate() {
    return recycleDate;
  }

  public void setRecycleDate(Date recycleDate) {
    this.recycleDate = recycleDate;
  }

  /**回收明细*/
  public List<BInvoiceRecycleLine> getLines() {
    return lines;
  }

  public void setLines(List<BInvoiceRecycleLine> lines) {
    this.lines = lines;
  }
}
