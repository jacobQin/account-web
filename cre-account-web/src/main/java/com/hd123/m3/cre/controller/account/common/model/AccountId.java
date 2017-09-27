/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	AccountId.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月26日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.common.model;

import java.io.Serializable;

/**
 * 账款Id
 * 
 * @author LiBin
 *
 */
public class AccountId implements Serializable{

  private static final long serialVersionUID = 8495010693672631159L;
  
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
