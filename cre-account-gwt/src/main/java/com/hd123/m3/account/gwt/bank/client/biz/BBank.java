/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2010，所有权利保留。
 * 
 * 项目名：	M3
 * 文件名：	BBank.java
 * 模块说明：	
 * 修改历史：
 * 2013-817 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.bank.client.biz;

import com.hd123.m3.commons.gwt.base.client.biz.HasStore;
import com.hd123.m3.commons.gwt.base.client.biz.HasSummary;
import com.hd123.rumba.commons.gwt.entity.client.BStandardEntity;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.BWithUCN;

/**
 * @author chenrizhang
 * 
 */
public class BBank extends BStandardEntity implements BWithUCN, HasSummary, HasStore {
  private static final long serialVersionUID = 300200L;

  private BUCN store;
  private String code;
  private String name;
  private String bank;
  private String account;
  private String address;
  private String remark;

  @Override
  public String toFriendlyStr() {
    return BUCN.toFriendlyStr(this);
  }

  @Override
  public String toString() {
    return toFriendlyStr();
  }

  /** 项目 */
  public BUCN getStore() {
    return store;
  }

  public void setStore(BUCN store) {
    this.store = store;
  }

  /** 代码 */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  /** 名称 */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /** 银行 */
  public String getBank() {
    return bank;
  }

  public void setBank(String bank) {
    this.bank = bank;
  }

  /** 账号 */
  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  /** 银行地址 */
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  /** 说明 */
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
