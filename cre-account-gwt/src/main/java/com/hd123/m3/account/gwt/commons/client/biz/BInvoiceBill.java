/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BInvoiceBill.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.commons.client.biz;

/**
 * @author chenwenfeng
 * 
 */
public class BInvoiceBill extends BBill {
  private static final long serialVersionUID = -8370280207930717453L;

  private String invoiceType;
  private String invoiceCode;
  private String invoiceNumber;

  public BInvoiceBill() {
  }

  public BInvoiceBill(String billUuid, String billNumber, String invoiceType, String invoiceCode,
      String invoiceNumber) {
    super(billUuid, billNumber);
    this.invoiceCode = invoiceCode;
    this.invoiceNumber = invoiceNumber;
    this.invoiceType = invoiceType;
  }

  public String getInvoiceType() {
    return invoiceType;
  }

  public void setInvoiceType(String invoiceType) {
    this.invoiceType = invoiceType;
  }

  public String getInvoiceCode() {
    return invoiceCode;
  }

  public void setInvoiceCode(String invoiceCode) {
    this.invoiceCode = invoiceCode;
  }

  public String getInvoiceNumber() {
    return invoiceNumber;
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public String id() {
    StringBuffer sb = new StringBuffer();
    sb.append(super.id());
    sb.append(invoiceCode);
    sb.append(invoiceNumber);
    return sb.toString();
  }

}
