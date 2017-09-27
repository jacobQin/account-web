package com.hd123.m3.cre.controller.account.payment.model;

/**
 * 抵扣明细
 * 
 * @author LiBin
 * 
 */
public class BPaymentLineDeduction extends BPaymentLineDefrayal {

  private static final long serialVersionUID = 1011600338056320473L;

  private String accountId;
  private String bizId;

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getBizId() {
    return bizId;
  }

  public void setBizId(String bizId) {
    this.bizId = bizId;
  }

}
