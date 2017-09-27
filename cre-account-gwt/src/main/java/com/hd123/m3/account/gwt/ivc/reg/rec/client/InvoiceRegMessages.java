/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceCommonMessages.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * @author lixiaohong
 *
 */
public interface InvoiceRegMessages extends Messages {
  public static InvoiceRegMessages M = GWT.create(InvoiceRegMessages.class);

  @DefaultMessage("行号")
  String lineNo();

  @DefaultMessage("在上方插入行")
  String insertBefore();

  @DefaultMessage("登记信息将被清空，是否继续？")
  String confirmToChange();
  
  @DefaultMessage("发票信息将会被清空，是否继续？")
  String confirmTypeChange();


  @DefaultMessage("发票信息")
  String invoiceInfo();

  @DefaultMessage("合计")
  String summary();

  @DefaultMessage("共计 {0}  条")
  String totalCount(int count);

  @DefaultMessage("登记信息")
  String regDetails();

  @DefaultMessage("科目")
  String regDetail_subject();

  @DefaultMessage("起止日期")
  String regDetail_period();

  @DefaultMessage("{0} ~ {1}")
  String period(String begin, String end);

  @DefaultMessage("来源单号")
  String regDetail_sourceBillNumber();

  @DefaultMessage("单据类型")
  String regDetail_sourceBillType();

  @DefaultMessage("{0}：不允许空值;")
  String fieldNotNull(String caption);

  @DefaultMessage("发票[{0}]的开票金额合计必须大于0;")
  String regInvoiceTotalLessZero(String invoiceNumber);

}
