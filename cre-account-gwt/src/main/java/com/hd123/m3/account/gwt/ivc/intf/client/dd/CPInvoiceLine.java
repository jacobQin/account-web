/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	CPInvoiceLine.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月8日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.intf.client.dd;

import com.google.gwt.i18n.client.Constants;

/**
 * @author lixiaohong
 *
 */
public interface CPInvoiceLine extends Constants{
  public String invoiceCode();
  
  public String startNumber();
  
  public String endNumber();
  
  public String quantity();
  
  public String receive_quantity();
  
  public String abort_quantity();
  
  public String return_quantity();
  
  public String transport_quantity();
  
  public String recycle_quantity();
  
  public String realQty();
  
  public String balanceQty();
  
  public String remark();
}
