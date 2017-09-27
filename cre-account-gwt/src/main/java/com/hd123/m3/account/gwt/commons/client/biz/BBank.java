/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BBank.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-16 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.commons.client.biz;

import java.io.Serializable;

import com.hd123.m3.account.gwt.util.client.ObjectUtil;
import com.hd123.m3.commons.gwt.base.client.biz.HasStore;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author chenpeisi
 * 
 */
public class BBank implements Serializable, HasStore {
  private static final long serialVersionUID = 300200L;

  private BUCN store;
  private String code;
  private String name;
  private String bankAccount;

  @Override
  public BUCN getStore() {
    return store;
  }

  public void setStore(BUCN store) {
    this.store = store;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBankAccount() {
    return bankAccount;
  }

  public void setBankAccount(String bankAccount) {
    this.bankAccount = bankAccount;
  }

  public String toFriendlyStr() {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    sb.append(ObjectUtil.nvl(getCode(), ""));
    sb.append("]");
    sb.append(ObjectUtil.nvl(getName(), ""));
    return sb.toString();
  }

  @Override
  public String toString() {
    return toFriendlyStr();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BBank other = (BBank) obj;
    if (code == null) {
      if (other.code != null)
        return false;
    } else if (!code.equals(other.code))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }
}
