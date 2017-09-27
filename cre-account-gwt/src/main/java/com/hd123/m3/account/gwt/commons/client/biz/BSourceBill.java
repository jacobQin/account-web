/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BSourceBill.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.commons.client.biz;


/**
 * @author chenwenfeng
 * 
 */
public class BSourceBill extends BBill {
  private static final long serialVersionUID = 2814562201305009964L;

  private String billType;

  public BSourceBill() {
  }

  public BSourceBill(String billUuid, String billNumber, String billType) {
    super(billUuid, billNumber);
    this.billType = billType;
  }

  public String getBillType() {
    return billType;
  }

  public void setBillType(String billType) {
    this.billType = billType;
  }

}
