/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： StatementFilter.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-16 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.filter;

import java.util.Date;

import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * @author subinzhu
 * 
 */
public class StatementFilter extends QueryFilter {
  private static final long serialVersionUID = 1L;

  private String counterpart;
  private String billNumberStart;
  private Date genAccBillDateFrom;
  private Date genAccBillDateTo;

  public String getBillNumberStart() {
    return billNumberStart;
  }

  public void setBillNumberStart(String billNumberStart) {
    this.billNumberStart = billNumberStart;
  }

  public String getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(String counterpart) {
    this.counterpart = counterpart;
  }

  public Date getGenAccBillDateFrom() {
    return genAccBillDateFrom;
  }

  public void setGenAccBillDateFrom(Date genAccBillDateFrom) {
    this.genAccBillDateFrom = genAccBillDateFrom;
  }

  public Date getGenAccBillDateTo() {
    return genAccBillDateTo;
  }

  public void setGenAccBillDateTo(Date genAccBillDateTo) {
    this.genAccBillDateTo = genAccBillDateTo;
  }
}
