/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	CPInvoiceStock.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月10日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.stock.intf.client.dd;

import com.google.gwt.i18n.client.Constants;

/**
 * @author lixiaohong
 * 
 */
public interface CPInvoiceStock extends Constants {
  public String tableCaption();

  public String sourceBillNumber();

  public String accountUnit();

  public String invoiceType();

  public String remark();

  public String holder();

  public String invoiceCode();

  public String invoiceNumber();

  public String abortDate();

  public String state();

  public String amount();

  public String useType();

  public String balanceCode();

  public String balanceNumber();

  public String instockTime();

  public String sort();
  
  public String createInfo();

  public String createInfo_operator();

  public String createInfo_operator_fullName();

  public String createInfo_operator_id();

  public String createInfo_operator_namespace();

  public String createInfo_time();

  public String lastModifyInfo();

  public String lastModifyInfo_operator();

  public String lastModifyInfo_operator_fullName();

  public String lastModifyInfo_operator_id();

  public String lastModifyInfo_operator_namespace();

  public String lastModifyInfo_time();
}
