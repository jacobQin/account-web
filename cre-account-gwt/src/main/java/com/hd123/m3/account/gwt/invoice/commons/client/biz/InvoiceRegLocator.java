/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	InvoiceRegInvoiceLocator.java
 * 模块说明：	
 * 修改历史：
 * 2013-12-5 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.client.biz;

import com.google.gwt.user.client.ui.Focusable;
import com.hd123.m3.commons.gwt.util.client.validation.LineLocator;

/**
 * @author chenpeisi
 * 
 */
public class InvoiceRegLocator extends LineLocator {

  public static final String invoiceType = "invoice";
  public static final String lineType = "line";

  private String locatorType;

  public InvoiceRegLocator() {
  }

  public InvoiceRegLocator(String locatorType, int lineNumber, Focusable field) {
    super(lineNumber, field);
    this.setLocatorType(locatorType);
  }

  public String getLocatorType() {
    return locatorType;
  }

  public void setLocatorType(String locatorType) {
    this.locatorType = locatorType;
  }
}
