/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	CPInvoiceExchBalanceLine.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月20日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.intf.client.dd;

import com.google.gwt.i18n.client.Constants;


/**
 * 票据明细数据字典常量定义
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public interface CPInvoiceExchBalanceLine extends Constants{
  
  public String oldInvoiceNumber();
  public String oldInvoiceCode();
  
  public String subject();
  public String beginEndDate();
  public String sourceBillType();
  public String sourceBill();
  
  public String balanceInvoiceCode();
  public String balanceInvoiceNumber();
  public String amount();
  public String remark();

  public String oldReceiptCode();
  public String oldReceiptNumber();
  public String newReceiptCode();
  public String newReceiptNumber();
  public String newInvoiceCode();
  public String newInvoiceNumber();
  
}
