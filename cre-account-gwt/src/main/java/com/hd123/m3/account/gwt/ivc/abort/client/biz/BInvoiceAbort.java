/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceAbort.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月15日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.abort.client.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceLine;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceStandardBill;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * B对象|发票作废单
 * 
 * @author lixiaohong
 * @since 1.7
 * 
 */
public class BInvoiceAbort extends BInvoiceStandardBill {

  private static final long serialVersionUID = -716242286649558047L;
  private BUCN aborter;
  private Date abortDate;

  private List<BInvoiceLine> abortLines = new ArrayList<BInvoiceLine>();
  
  /** 作废人 */
  public BUCN getAborter() {
    return aborter;
  }

  public void setAborter(BUCN aborter) {
    this.aborter = aborter;
  }

  /** 作废日期 */
  public Date getAbortDate() {
    return abortDate;
  }

  public void setAbortDate(Date abortDate) {
    this.abortDate = abortDate;
  }

  /** 作废明细 */
  public List<BInvoiceLine> getAbortLines() {
    return abortLines;
  }

  public void setAbortLines(List<BInvoiceLine> abortLines) {
    this.abortLines = abortLines;
  }

}
