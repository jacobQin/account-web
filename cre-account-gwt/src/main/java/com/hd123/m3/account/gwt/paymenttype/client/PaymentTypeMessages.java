/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	PaymentTypeMessages.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymenttype.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author zhuhairui
 * 
 */
public interface PaymentTypeMessages extends CommonsMessages {
  public static PaymentTypeMessages M = GWT.create(PaymentTypeMessages.class);

  @DefaultMessage("财务付款方式")
  String moduleCaption();

  @DefaultMessage("启用")
  String enable();

  @DefaultMessage("正在启用...")
  String enabling();

  @DefaultMessage("已启用")
  String enabed();

  @DefaultMessage("使用中")
  String isUsing();

  @DefaultMessage("停用")
  String disable();

  @DefaultMessage("正在停用...")
  String disabling();

  @DefaultMessage("已停用")
  String disabled();

  @DefaultMessage("确定")
  String confirm();

  @DefaultMessage("显示{0}页面时发生错误。")
  String showPageError(String page);

  @DefaultMessage("根据代码查询付款方式失败")
  String queryException();

  @DefaultMessage("已经存在代码为“{0}”的付款方式")
  String codeRepeat(String code);
}
