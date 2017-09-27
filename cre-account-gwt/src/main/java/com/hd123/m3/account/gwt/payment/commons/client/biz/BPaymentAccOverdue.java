/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BPaymentAccOverdue.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.biz;

import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxRate;
import com.hd123.rumba.commons.gwt.entity.client.BEntity;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 
 * 收付款单账款明细行滞纳金明细。
 * 
 * @author subinzhu
 * 
 */
public class BPaymentAccOverdue extends BEntity {

  private static final long serialVersionUID = 9041600048864200670L;

  private BPaymentAccountLine line;
  private BUCN contract;
  private BUCN subject;
  private int direction;
  private BTaxRate taxRate;
  private BTotal total = BTotal.zero();
  private boolean issueInvoice;

  public BPaymentAccountLine getLine() {
    return line;
  }

  public void setLine(BPaymentAccountLine line) {
    this.line = line;
  }

  public BUCN getContract() {
    return contract;
  }

  public void setContract(BUCN contract) {
    this.contract = contract;
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

  public BTaxRate getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(BTaxRate taxRate) {
    this.taxRate = taxRate;
  }

  public BTotal getTotal() {
    return total;
  }

  public void setTotal(BTotal total) {
    this.total = total;
  }

  public boolean isIssueInvoice() {
    return issueInvoice;
  }

  public void setIssueInvoice(boolean issueInvoice) {
    this.issueInvoice = issueInvoice;
  }

}
