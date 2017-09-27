/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	CPInvoiceRecycle.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.intf.client.dd;

import com.hd123.m3.account.gwt.ivc.intf.client.dd.CPInvoiceStandardBill;

/**
 * @author lixiaohong
 *
 */
public interface CPInvoiceRecycle extends CPInvoiceStandardBill{
  public String tableCaption();
  public String receiver();
  public String returnor();
  public String recycleDate();
}
