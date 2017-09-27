package com.hd123.m3.cre.controller.account.payment.model;

import java.math.BigDecimal;

import com.hd123.rumba.commons.biz.entity.Entity;

/**
 * 收付款单按总额收款时的收付明细
 * 
 * @author LiBin
 * 
 */
public class BPaymentDefrayal extends Entity {
  private static final long serialVersionUID = 7917895069906474631L;

  private int lineNumber;
  private BigDecimal total = BigDecimal.ZERO;
  private String remark;

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

}
