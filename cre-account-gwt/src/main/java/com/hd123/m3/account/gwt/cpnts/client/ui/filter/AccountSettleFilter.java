/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	AccountSettleFilter.java
 * 模块说明：	
 * 修改历史：
 * 2016年10月20日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.filter;

import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;

/**
 * @author chenganbang
 *
 */
public class AccountSettleFilter extends CodeNameFilter {

  private static final long serialVersionUID = -4170799681067225054L;

  private boolean resetPage = false;

  /**
   * resetPage为ture时，page和pageSize都将为0
   * 
   * @return
   */
  public boolean isResetPage() {
    return resetPage;
  }

  public void setResetPage(boolean resetPage) {
    this.resetPage = resetPage;
  }

}
