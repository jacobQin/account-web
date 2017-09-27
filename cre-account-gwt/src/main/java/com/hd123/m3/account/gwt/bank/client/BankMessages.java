/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-basic
 * 文件名：	BankWidgetConstants.java
 * 模块说明：	
 * 修改历史：
 * 2013-9-17 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.bank.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author chenrizhang
 * 
 */
public interface BankMessages extends CommonsMessages {
  public static BankMessages M = GWT.create(BankMessages.class);

  @DefaultMessage("财务信息")
  String account();

  @DefaultMessage("根据代码查询银行资料失败")
  String queryException();

  @DefaultMessage("已经存在代码为“{0}”的银行资料")
  String codeRepeat(String code);
}
