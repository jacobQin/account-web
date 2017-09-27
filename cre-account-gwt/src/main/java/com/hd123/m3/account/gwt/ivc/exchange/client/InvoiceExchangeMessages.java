/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeMessages.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月20日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;

/**
 * 发票交换单
 * 
 * @author LiBin
 *
 */
public interface InvoiceExchangeMessages extends InvoiceCommonMessages {

  public static InvoiceExchangeMessages M = GWT.create(InvoiceExchangeMessages.class);
  
  @DefaultMessage("票据明细")
  String invoiceDetail();
  
  @DefaultMessage("账款明细")
  String accountDetail();
  
  @DefaultMessage("{0}" + "：" + "{1}")
  String keyValue(String key,String value);
  
  @DefaultMessage("查询账款出错")
  String queryAccountError();
  
  @DefaultMessage("收据号码")
  String receiptNumber();
  
  @DefaultMessage("新收据号码")
  String newReceiptNumber();
  
  @DefaultMessage("原收据号码")
  String oldReceiptNumber();
  
  @DefaultMessage("发票号码")
  String invoiceNumber();
  
  @DefaultMessage("原发票号码")
  String oldInvoiceNumber();
  
  @DefaultMessage("新发票号码")
  String newInvoiceNumber();
  
  @DefaultMessage("红冲发票号码")
  String balanceInvoiceNumber();
  
  @DefaultMessage("明细将被清空，是否继续？")
  String confirmClearDetails();
  
  @DefaultMessage("号码{0}已存在于账款明细交换列表中")
  String numberExistsInAccounts(String number);
  
  @DefaultMessage("号码{0}已存在于票据明细红冲列表中")
  String numberExistsInBalanceLines(String number);
  
  
}

