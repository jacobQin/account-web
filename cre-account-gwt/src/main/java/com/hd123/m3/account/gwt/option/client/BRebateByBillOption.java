/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	BRebatebillOption.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月18日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.option.client;

import java.io.Serializable;

import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 返款单是否按笔返款的配置
 * 
 * @author chenganbang
 */
public class BRebateByBillOption implements Serializable {

  private static final long serialVersionUID = -8716019612061769475L;

  private BUCN store;
  private boolean rebateByBill = false;

  /**
   * 项目
   * @return
   */
  public BUCN getStore() {
    return store;
  }

  public void setStore(BUCN store) {
    this.store = store;
  }

  /**
   * 配置值
   * @return
   */
  public boolean isRebateByBill() {
    return rebateByBill;
  }

  public void setRebateByBill(boolean rebateByBill) {
    this.rebateByBill = rebateByBill;
  }

}
