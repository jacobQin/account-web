/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountDefrayalMessage.java
 * 模块说明：	
 * 修改历史：
 * 2013-12-9 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.report.accountdefrayal.intf.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author zhuhairui
 * 
 */
public interface AccountDefrayalMessages extends CommonsMessages {
  public static AccountDefrayalMessages M = GWT.create(AccountDefrayalMessages.class);

  @DefaultMessage("科目收付情况")
  String moduleCaption();

  @DefaultMessage("账单结转期")
  String statementSettleNo();

  @DefaultMessage("起止日期")
  String dateRange();

  @DefaultMessage("最后缴款日期")
  String lastReceiptDate();

  @DefaultMessage("授权组")
  String authorizeGroup();

  @DefaultMessage("收付方向")
  String direction();

  @DefaultMessage("出账时间")
  String accountTime();

  @DefaultMessage("只显示未结清科目")
  String showSubject();

  @DefaultMessage("只显示未登记发票科目")
  String showInvoice();

  @DefaultMessage("收")
  String receipet();

  @DefaultMessage("付")
  String payment();

  @DefaultMessage("是")
  String yes();

  @DefaultMessage("否")
  String no();

  @DefaultMessage("-")
  String noneBillNumber();

  @DefaultMessage("~")
  String waveLine();

  @DefaultMessage("显示{0}页面时发生错误。")
  String showPageError(String page);
}
