/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AdvanceMessages.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-30 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.adv.intf.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author zhuhairui
 * 
 */
public interface AdvanceMessages extends CommonsMessages {

  public static AdvanceMessages M = GWT.create(AdvanceMessages.class);

  @DefaultMessage("{0} 类似于")
  String like(String fieldName);

  @DefaultMessage("{0} 等于")
  String equal(String fieldName);

  @DefaultMessage("{0} 介于")
  String between(String fieldName);

  @DefaultMessage("清除")
  String clear();

  @DefaultMessage("搜索条件")
  String searchFilter();

  @DefaultMessage("搜索结果")
  String resultSearch();

  @DefaultMessage("发生日期")
  String time();

  @DefaultMessage("预存款")
  String advance();

  @DefaultMessage("使用记录")
  String useLog();

  @DefaultMessage("{0}列表")
  String list(String captionName);

  @DefaultMessage("科目")
  String subject();

  @DefaultMessage("存款")
  String advanceItem();

  @DefaultMessage("取款")
  String withdrawalItem();

  @DefaultMessage("存款转移")
  String moveItem();

  @DefaultMessage("合计：")
  String summary();

  @DefaultMessage("结算单位")
  String accountUnit();

}
