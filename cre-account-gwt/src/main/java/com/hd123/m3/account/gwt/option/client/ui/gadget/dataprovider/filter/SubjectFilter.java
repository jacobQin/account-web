/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	SubjectFilter.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.option.client.ui.gadget.dataprovider.filter;

import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;

/**
 * @author zhuhairui
 * 
 */
public class SubjectFilter extends CodeNameFilter {

  private static final long serialVersionUID = 4596005083333208238L;

  private String subjectType;
  private int directionType = 0;

  public String getSubjectType() {
    return subjectType;
  }

  public void setSubjectType(String subjectType) {
    this.subjectType = subjectType;
  }

  public int getDirectionType() {
    return directionType;
  }

  public void setDirectionType(int directionType) {
    this.directionType = directionType;
  }

}
