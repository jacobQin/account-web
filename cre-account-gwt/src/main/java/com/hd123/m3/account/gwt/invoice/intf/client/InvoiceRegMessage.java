/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	InvoiceRegMessage.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-27 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.intf.client;

import java.math.BigDecimal;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author chenpeisi
 * 
 */
public interface InvoiceRegMessage extends CommonsMessages {
  public static InvoiceRegMessage M = GWT.create(InvoiceRegMessage.class);

  @DefaultMessage("单据类型")
  String billType();

  @DefaultMessage("发票类型")
  String invoiceType();

  @DefaultMessage("默认值")
  String defaultValue();

  @DefaultMessage("配置")
  String config();

  @DefaultMessage("原因")
  String reason();

  @DefaultMessage("合计")
  String aggregate();

  @DefaultMessage("请先选择{0}")
  String onSelectCounterpartFirst(String caption);

  @DefaultMessage("对方结算单位")
  String accountUnit();

  @DefaultMessage("单据")
  String bill();

  @DefaultMessage("发票")
  String invoice();

  @DefaultMessage("去税金额/税额")
  String amountTax();

  @DefaultMessage("未登记金额/税额")
  String unRegTotal();

  @DefaultMessage("添加行")
  String addLine();

  @DefaultMessage("在上方插入行")
  String insertLine();

  @DefaultMessage("从账单导入...")
  String addFromStatement();

  @DefaultMessage("从收付款通知单导入...")
  String addFromPaymentNotice();

  @DefaultMessage("从其他单据导入...")
  String addFromBill();

  @DefaultMessage("从账款导入...")
  String addFromAccount();

  @DefaultMessage("至少添加一行")
  String addOneLeast();

  @DefaultMessage("账款单据明细")
  String lineInfo();

  @DefaultMessage("发票明细")
  String invoiceInfo();

  @DefaultMessage("去税金额")
  String amount();

  @DefaultMessage("来源单据行{0}的{1}与{2}不相等。")
  String notEquals(Integer lineNumber, String caption, String caption1);

  @DefaultMessage("发票总额-账款总额{0}必须在上限{1}和下限{2}范围内。")
  String totalDiffError(BigDecimal totalDiff, BigDecimal totalDiffHi, BigDecimal totalDiffLo);

  @DefaultMessage("发票税额-账款税额{0}必须在上限{1}和下限{2}范围内。")
  String taxDiffError(BigDecimal taxDiff, BigDecimal taxDiffHi, BigDecimal taxDiffLo);

  @DefaultMessage("第 {0} 行")
  String lineNumber(int line);

  @DefaultMessage("第{0}行和{1}发票号码行重复。")
  String duplicate(int i, int j);

  @DefaultMessage("第{0}行的{1}不允许空值。")
  String notNull(int i, String fieldName);

  @DefaultMessage("{0}不能等于0.00")
  String mustGreaterThanZreo1(String fieldName);

  @DefaultMessage("第{0}行的{1}不能等于0.00")
  String mustGreaterThanZreo2(int i, String fieldName);

  @DefaultMessage("{0}不能超过{1}:{2}。")
  String max(String fieldName1, String fieldName2, BigDecimal total);

  @DefaultMessage("{0}不能超过{1}。")
  String maxValue1(String fieldName1, String fieldName2);

  @DefaultMessage("第{0}行的{1}不能超过{2}。")
  String maxValue2(int i, String fieldName1, String fieldName2);

  @DefaultMessage("{0}和{1}收付方向不一致。")
  String inconsistent1(String fieldName1, String fieldName2);

  @DefaultMessage("第{0}行的{1}和{2}收付方向不一致。")
  String inconsistent2(int i, String fieldName1, String fieldName2);

  @DefaultMessage("{0}:必须大于等于{1}")
  String greaterThanOrEqual(String fieldName1, String fieldName2);

}
