/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BPaymentOverdueLine.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.biz;

import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxRate;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridElement;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 收付款单滞纳金明细
 * 
 * @author subinzhu
 * 
 */
public class BPaymentOverdueLine extends BPaymentLine implements EditGridElement{
  private static final long serialVersionUID = -8142077474275056695L;

  public static final String FN_OVERDUELINE_TOTAL = "overdueLineTotal";
  public static final String FN_OVERDUELINE_UNPAYEDTOTAL = "overdueLineUnpayedTotal";
  public static final String FN_OVERDUELINE_REMARK = "overdueLineRemark";

  private BUCN accountUnit;
  private BUCN counterpart;
  private BUCN contract;
  private String accountId;
  private BUCN subject;
  private BTaxRate taxRate;
  private BTotal overdueTotal = BTotal.zero();
  private boolean issueInvoice;

  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  public BUCN getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BUCN counterpart) {
    this.counterpart = counterpart;
  }

  public BUCN getContract() {
    return contract;
  }

  public void setContract(BUCN contract) {
    this.contract = contract;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public BUCN getSubject() {
    return subject;
  }

  public void setSubject(BUCN subject) {
    this.subject = subject;
  }

  public BTaxRate getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(BTaxRate taxRate) {
    this.taxRate = taxRate;
  }

  public BTotal getOverdueTotal() {
    return overdueTotal;
  }

  public void setOverdueTotal(BTotal overdueTotal) {
    this.overdueTotal = overdueTotal;
  }

  public boolean isIssueInvoice() {
    return issueInvoice;
  }

  public void setIssueInvoice(boolean issueInvoice) {
    this.issueInvoice = issueInvoice;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean match(EditGridElement other) {
    return false;
  }

}
