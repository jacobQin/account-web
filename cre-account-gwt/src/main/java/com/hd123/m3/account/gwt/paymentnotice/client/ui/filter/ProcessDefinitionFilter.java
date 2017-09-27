/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	ProcessDefinitionFilter.java
 * 模块说明：	
 * 修改历史：
 * 2014-4-1 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.ui.filter;

import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * @author zhuhairui
 * 
 */
public class ProcessDefinitionFilter extends QueryFilter {
  private static final long serialVersionUID = -3809811697557968025L;

  private String category;

  /** 分类 */
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }
}
