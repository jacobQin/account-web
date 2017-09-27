/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BAccountId.java
 * 模块说明：	
 * 修改历史：
 * 2013-12-2 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.acc.client;

import java.io.Serializable;

/**
 * @author chenpeisi
 * 
 */
public class BAccountId implements Serializable {
  private static final long serialVersionUID = 7038335596787285400L;

  private String accId;
  private String bizId;

  public String getAccId() {
    return accId;
  }

  public void setAccId(String accId) {
    this.accId = accId;
  }

  /** 用于收付款单、收付款发票登记单的账款过滤时不加入锁定uuid */
  public String getBizId() {
    return bizId;
  }

  public void setBizId(String bizId) {
    this.bizId = bizId;
  }
}
