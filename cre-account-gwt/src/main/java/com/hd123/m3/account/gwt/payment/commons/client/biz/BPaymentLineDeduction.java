/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BPaymentLineDeduction.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-5 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.biz;

/**
 * @author subinzhu
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
