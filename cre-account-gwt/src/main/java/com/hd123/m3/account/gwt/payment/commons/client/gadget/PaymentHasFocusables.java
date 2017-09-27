/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentFocusable.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-29 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.gadget;

import com.google.gwt.user.client.ui.Focusable;

/**
 * @author subinzhu
 * 
 */
public interface PaymentHasFocusables {

  /**
   * 取得指定行、指定字段名对应的可定位焦点控件。
   * 
   * @param lineNumber
   * @param field
   * @return
   */
  Focusable getFocusable(int lineNumber, String field);

}
