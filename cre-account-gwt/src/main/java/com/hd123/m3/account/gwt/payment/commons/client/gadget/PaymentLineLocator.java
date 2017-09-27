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

import com.hd123.m3.commons.gwt.util.client.validation.LineLocator;

/**
 * 收付款单明细行行定位器
 * 
 * @author subinzhu
 * 
 */
public class PaymentLineLocator extends LineLocator {

  /** 账款明细tab */
  public static Integer ACCOUNTTAB_ID = 0;
  /** 账款明细科目多收款tab */
  public static Integer ACCOUNTMULTITAB_ID = 1;
  /** 滞纳金明细tab */
  public static Integer OVERDUETAB_ID = 2;
  /**代收明细tab*/
  public static Integer COLLECTIONTAB_ID = 3;

  private Integer tabId;
  private int lineNumber;
  private String fieldId;

  public Integer getTabId() {
    return tabId;
  }

  public void setTabId(Integer tabId) {
    this.tabId = tabId;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public String getFieldId() {
    return fieldId;
  }

  public void setFieldId(String fieldId) {
    this.fieldId = fieldId;
  }

  /**
   * 给只需要单层行定位的控件使用。这里是给收付款单明细行编辑控件用。
   * 
   * @param tabId
   *          收付款单明细信息中tab的Id
   * @param lineNumber
   *          收付款单明细行行号
   * @param fieldId
   *          控件id
   */
  public PaymentLineLocator(Integer tabId, int lineNumber, String fieldId) {
    this.tabId = tabId;
    this.lineNumber = lineNumber;
    this.fieldId = fieldId;
  }

}
