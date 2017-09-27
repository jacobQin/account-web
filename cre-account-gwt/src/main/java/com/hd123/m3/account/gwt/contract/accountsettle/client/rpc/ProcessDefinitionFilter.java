/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	ProcessDefinitionFilter.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-14 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.contract.accountsettle.client.rpc;

import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * @author zhuhairui
 * 
 */
public class ProcessDefinitionFilter extends QueryFilter {
  private static final long serialVersionUID = 7584483793152648812L;

  private String category;

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

}
