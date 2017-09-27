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
 * 收付款单行定位器
 * 
 * @author subinzhu
 * 
 */
public class PaymentLineDefrayalLineLocator extends LineLocator {

  /** 账款明细tab */
  public static Integer ACCOUNTTAB_ID = 0;
  
  public static Integer ACCOUNTMULTITAB_ID=1;
  /** 滞纳金明细tab */
  public static Integer OVERDUETAB_ID = 2;
  /** 代收明细tab */
  public static Integer COLLECTION_ID = 3;

  private Integer tabId;
  /** 外层控件行号 */
  private Integer outLineNumber;
  private int lineNumber;
  private String fieldId;

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

  public Integer getTabId() {
    return tabId;
  }

  public void setTabId(Integer tabId) {
    this.tabId = tabId;
  }

  public Integer getOutLineNumber() {
    return outLineNumber;
  }

  public void setOutLineNumber(Integer outLineNumber) {
    this.outLineNumber = outLineNumber;
  }

  /**
   * 给需要双层行定位的控件使用。这里是给收付款单按科目收付款时的明细行中的收付明细控件用。
   * 
   * @param tabId
   *          收付款单明细信息中tab的Id
   * @param outLineNumber
   *          外层控件行号，这里是收付款单明细行行号
   * @param lineNumber
   *          里层控件行号，这里是收付明细行行号
   * @param fieldId
   *          控件id
   */
  public PaymentLineDefrayalLineLocator(Integer tabId, Integer outLineNumber, int lineNumber,
      String fieldId) {
    this.tabId = tabId;
    this.outLineNumber = outLineNumber;
    this.lineNumber = lineNumber;
    this.fieldId = fieldId;
  }

}
