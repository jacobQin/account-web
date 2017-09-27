/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	QStatementLine.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-7 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.biz;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hd123.m3.account.gwt.commons.client.biz.BSourceBill;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxRate;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author zhuhairui
 * 
 */
public class QStatementLine implements Serializable {

  private static final long serialVersionUID = -8253078924339543444L;

  private BUCN subject;
  private int direction;
  private BigDecimal total;
  private BigDecimal tax;
  private Date beginDate;
  private Date endDate;
  private BSourceBill sourceBill;
  private BTaxRate taxRate;
  private boolean invoice;
  private String remark;
  private String accId;
  private Date accountDate;

  public BUCN getSubject() {
    return subject;
  }

  public void setSubject(BUCN subject) {
    this.subject = subject;
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public BSourceBill getSourceBill() {
    return sourceBill;
  }

  public void setSourceBill(BSourceBill sourceBill) {
    this.sourceBill = sourceBill;
  }

  public BTaxRate getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(BTaxRate taxRate) {
    this.taxRate = taxRate;
  }

  public boolean isInvoice() {
    return invoice;
  }

  public void setInvoice(boolean invoice) {
    this.invoice = invoice;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getAccId() {
    return accId;
  }

  public void setAccId(String accId) {
    this.accId = accId;
  }

  public Date getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(Date accountDate) {
    this.accountDate = accountDate;
  }

  public String getDateRange() {
    StringBuilder sb = new StringBuilder();
    sb.append(beginDate == null ? "" : M3Format.fmt_yMd.format(beginDate) + " ~ ");
    sb.append(endDate == null ? "" : M3Format.fmt_yMd.format(endDate));
    return sb.toString();
  }

}
