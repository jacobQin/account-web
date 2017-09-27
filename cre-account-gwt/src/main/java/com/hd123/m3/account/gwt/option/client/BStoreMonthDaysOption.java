/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-basic
 * 文件名：	StoreMonthDaysOption.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月5日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.option.client;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 项目账务月天数配置项。
 * 
 * @author LiBin
 * 
 */
public class BStoreMonthDaysOption implements Serializable {

  private static final long serialVersionUID = 8023373703429814984L;

  private BUCN store;
  private BigDecimal monthDays = BigDecimal.ZERO;
  private boolean actualMonthDays = false;

  /** 项目 */
  public BUCN getStore() {
    return store;
  }

  public void setStore(BUCN store) {
    this.store = store;
  }

  /** 财务月天数 */
  public BigDecimal getMonthDays() {
    return monthDays;
  }

  public void setMonthDays(BigDecimal monthDays) {
    this.monthDays = monthDays;
  }

  /** 实际月天数 */
  public boolean isActualMonthDays() {
    return actualMonthDays;
  }

  public void setActualMonthDays(boolean actualMonthDays) {
    this.actualMonthDays = actualMonthDays;
  }

}
