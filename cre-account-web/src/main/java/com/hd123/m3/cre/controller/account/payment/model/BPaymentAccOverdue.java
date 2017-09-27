package com.hd123.m3.cre.controller.account.payment.model;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.commons.biz.tax.TaxRate;
import com.hd123.rumba.commons.biz.entity.Entity;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 
 * 收付款单账款明细行滞纳金明细。
 * 
 * @author LiBin
 * 
 */
public class BPaymentAccOverdue extends Entity {

  private static final long serialVersionUID = 9041600048864200670L;

  private UCN contract;
  private UCN subject;
  private int direction;
  private TaxRate taxRate;
  private Total total = Total.zero();
  private boolean issueInvoice;

  public UCN getContract() {
    return contract;
  }

  public void setContract(UCN contract) {
    this.contract = contract;
  }

  public UCN getSubject() {
    return subject;
  }

  public void setSubject(UCN subject) {
    this.subject = subject;
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public TaxRate getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(TaxRate taxRate) {
    this.taxRate = taxRate;
  }

  public Total getTotal() {
    return total;
  }

  public void setTotal(Total total) {
    this.total = total;
  }

  public boolean isIssueInvoice() {
    return issueInvoice;
  }

  public void setIssueInvoice(boolean issueInvoice) {
    this.issueInvoice = issueInvoice;
  }

}
