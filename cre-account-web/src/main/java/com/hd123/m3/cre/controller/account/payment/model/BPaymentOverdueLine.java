package com.hd123.m3.cre.controller.account.payment.model;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.commons.biz.tax.TaxRate;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 收付款单滞纳金明细
 * 
 * @author LiBin
 * 
 */
public class BPaymentOverdueLine extends BPaymentLine{
  private static final long serialVersionUID = -8142077474275056695L;

  public static final String FN_OVERDUELINE_TOTAL = "overdueLineTotal";
  public static final String FN_OVERDUELINE_UNPAYEDTOTAL = "overdueLineUnpayedTotal";
  public static final String FN_OVERDUELINE_REMARK = "overdueLineRemark";

  private UCN accountUnit;
  private UCN counterpart;
  private UCN contract;
  private String accountId;
  private UCN subject;
  private TaxRate taxRate;
  private Total overdueTotal = Total.zero();
  private boolean issueInvoice;
  private boolean calculated = false;

  public UCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(UCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  public UCN getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(UCN counterpart) {
    this.counterpart = counterpart;
  }

  public UCN getContract() {
    return contract;
  }

  public void setContract(UCN contract) {
    this.contract = contract;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public UCN getSubject() {
    return subject;
  }

  public void setSubject(UCN subject) {
    this.subject = subject;
  }

  public TaxRate getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(TaxRate taxRate) {
    this.taxRate = taxRate;
  }

  public Total getOverdueTotal() {
    return overdueTotal;
  }

  public void setOverdueTotal(Total overdueTotal) {
    this.overdueTotal = overdueTotal;
  }

  public boolean isIssueInvoice() {
    return issueInvoice;
  }

  public void setIssueInvoice(boolean issueInvoice) {
    this.issueInvoice = issueInvoice;
  }
  
  /**标记是否计算过*/
  public boolean isCalculated() {
    return calculated;
  }
  
  public void setCalculated(boolean calculated) {
    this.calculated = calculated;
  }
}
