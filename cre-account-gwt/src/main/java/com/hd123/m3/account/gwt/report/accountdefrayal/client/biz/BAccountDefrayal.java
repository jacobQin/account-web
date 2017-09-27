/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BAccountDefrayal.java
 * 模块说明：	
 * 修改历史：
 * 2013-12-9 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.report.accountdefrayal.client.biz;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.BSourceBill;
import com.hd123.rumba.commons.gwt.entity.client.BEntity;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 科目收付情况|表示层实体
 * 
 * @author zhuhairui
 * 
 */
public class BAccountDefrayal extends BEntity {

  private static final long serialVersionUID = 2698507508627054623L;

  private BSourceBill sourceBill;
  private BUCN subject;
  private int direction;
  private BigDecimal needSettle;
  private BigDecimal settleAdj;
  private BigDecimal settled;
  private BigDecimal needInvoice;
  private BigDecimal invoiceAdj;
  private BigDecimal invoiced;
  private BBill statement;
  private String settleNo;
  private Date beginDate;
  private Date endDate;
  private Date lastReceiptDate;
  private BUCN contract;
  private BCounterpart counterpart;

  public BSourceBill getSourceBill() {
    return sourceBill;
  }

  public void setSourceBill(BSourceBill sourceBill) {
    this.sourceBill = sourceBill;
  }

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

  public BigDecimal getNeedSettle() {
    return needSettle;
  }

  public void setNeedSettle(BigDecimal needSettle) {
    this.needSettle = needSettle;
  }

  public BigDecimal getSettleAdj() {
    return settleAdj;
  }

  public void setSettleAdj(BigDecimal settleAdj) {
    this.settleAdj = settleAdj;
  }

  public BigDecimal getSettled() {
    return settled;
  }

  public void setSettled(BigDecimal settled) {
    this.settled = settled;
  }

  public BigDecimal getNeedInvoice() {
    return needInvoice;
  }

  public void setNeedInvoice(BigDecimal needInvoice) {
    this.needInvoice = needInvoice;
  }

  public BigDecimal getInvoiceAdj() {
    return invoiceAdj;
  }

  public void setInvoiceAdj(BigDecimal invoiceAdj) {
    this.invoiceAdj = invoiceAdj;
  }

  public BigDecimal getInvoiced() {
    return invoiced;
  }

  public void setInvoiced(BigDecimal invoiced) {
    this.invoiced = invoiced;
  }

  public BBill getStatement() {
    return statement;
  }

  public void setStatement(BBill statement) {
    this.statement = statement;
  }

  public String getSettleNo() {
    return settleNo;
  }

  public void setSettleNo(String settleNo) {
    this.settleNo = settleNo;
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

  public Date getLastReceiptDate() {
    return lastReceiptDate;
  }

  public void setLastReceiptDate(Date lastReceiptDate) {
    this.lastReceiptDate = lastReceiptDate;
  }

  public BUCN getContract() {
    return contract;
  }

  public void setContract(BUCN contract) {
    this.contract = contract;
  }

  public BCounterpart getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BCounterpart counterpart) {
    this.counterpart = counterpart;
  }

}
