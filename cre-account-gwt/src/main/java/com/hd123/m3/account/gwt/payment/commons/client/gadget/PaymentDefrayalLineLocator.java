/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentLineLocator.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-29 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.gadget;

import com.google.gwt.user.client.ui.Focusable;
import com.hd123.m3.commons.gwt.util.client.validation.LineLocator;

/**
 * 收付款单单头收付明细行行定位器
 * 
 * @author subinzhu
 * 
 */
public class PaymentDefrayalLineLocator extends LineLocator {

  /** 此处的控件用于添加错误消息的时候用 */
  private Focusable field;

  public Focusable getField() {
    return field;
  }

  public void setField(Focusable field) {
    this.field = field;
  }

  public PaymentDefrayalLineLocator(int lineNumber, Focusable field) {
    super(lineNumber, field);
    this.field = field;
  }

}
