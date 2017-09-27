/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentMessages.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author subinzhu
 * 
 */
public interface CommonMessages extends CommonsMessages {
  public static CommonMessages M = GWT.create(CommonMessages.class);

  @DefaultMessage("第{0}行")
  String lineNumber(int line);

  @DefaultMessage("余额")
  String remainTotal();

  @DefaultMessage("付款信息")
  String paymentInfo();

  @DefaultMessage("起止日期")
  String dateRange();

  @DefaultMessage("{0}：{1}必须大于{2}。")
  String minValue2(String caption, String a, String b);

  @DefaultMessage("{0}不能小于{1}。")
  String cannotLessThan(String a, String b);

  @DefaultMessage("{0}：第{1}行的{2}不能小于{3}。")
  String cannotLessThan2(String caption, int i, String a, String b);

  @DefaultMessage("{0}必须大于{1}。")
  String mustMoreThan(String a, String b);

  @DefaultMessage("{0}：第{1}行的{2}必须大于{3}。")
  String mustMoreThan2(String caption, int i, String a, String b);

  @DefaultMessage("{0}：第{1}行的{2}不能大于{3}。")
  String cannotMoreThan2(String caption, int i, String a, String b);

  @DefaultMessage("{0}：第{1}行的{2}的绝对值不能大于{3}的绝对值。")
  String absCannotMoreThan2(String caption, int i, String a, String b);

  @DefaultMessage("{0}不能大于{1}。")
  String cannotMoreThan(String a, String b);

  @DefaultMessage("{0}的绝对值不能大于{1}的绝对值。")
  String absCannotMoreThan(String a, String b);

  @DefaultMessage("{0}必须等于{1}。")
  String mustBeEquals(String a, String b);

  @DefaultMessage("{0}不能等于{1}。")
  String cannotBeEquals(String a, String b);

  @DefaultMessage("{0}：与第{1}行重复。")
  String lineRepeatError(String caption, int line);

  @DefaultMessage("预付款冲扣")
  String depositLinePay();

  @DefaultMessage("扣预付款")
  String depositBillPay();

  @DefaultMessage("实付")
  String defrayal();

  @DefaultMessage("按总额付款")
  String billPay();

  @DefaultMessage("按科目付款")
  String linePay();

  @DefaultMessage("[科目+合同]")
  String subjectAndContract();

  @DefaultMessage("{0}：第{1}行的{2}超过最大长度128。")
  String remarkError(String caption, int i, String field);
  
  @DefaultMessage("{0}：第{1}行的{2}")
  String addCaption(String caption, int i, String a);
}
