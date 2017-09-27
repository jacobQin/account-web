/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-basic
 * 文件名：	BincompleteMonthByRealDays.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月7日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.option.client;

import java.io.Serializable;

import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 非整月月末按实际天数计算
 * 
 * @author LiBin
 * 
 */
public class BIncompleteMonthByRealDaysOption implements Serializable {

  private static final long serialVersionUID = -5589637297479258008L;

  private BUCN store;
  private boolean incompleteMonthByRealDays = false;

  /** 项目 */
  public BUCN getStore() {
    return store;
  }

  public void setStore(BUCN store) {
    this.store = store;
  }

  /** 配置值 */
  public boolean isIncompleteMonthByRealDays() {
    return incompleteMonthByRealDays;
  }

  public void setIncompleteMonthByRealDays(boolean incompleteMonthByRealDays) {
    this.incompleteMonthByRealDays = incompleteMonthByRealDays;
  }

}
