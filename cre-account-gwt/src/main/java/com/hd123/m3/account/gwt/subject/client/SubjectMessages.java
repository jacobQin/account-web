/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-basic
 * 文件名：	SubjectWidgetRes.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-10 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.subject.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author chenrizhang
 * 
 */
public interface SubjectMessages extends CommonsMessages {
  public static SubjectMessages M = GWT.create(SubjectMessages.class);

  @DefaultMessage("获取科目用途")
  String getUsages();

  @DefaultMessage("根据代码查询科目失败")
  String queryException();

  @DefaultMessage("已经存在代码为“{0}”的科目")
  String codeRepeat(String code);

  @DefaultMessage("项目科目税率")
  String storeSubjectTaxRate();
  
  @DefaultMessage("{0}：不允许空值")
  String notNull(String caption);
  
  @DefaultMessage("代码为 {0} 的项目已存在")
  String storeRepeat(String caption);
  
  @DefaultMessage("{0}：未找到代码 {1}")
  String cannotFindCode(String caption, String code);
}
