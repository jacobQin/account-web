/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountFilter.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.freeze.client.ui.filter;

import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * 账款查询filter
 * 
 * @author zhuhairui
 * 
 */
public class AccountFilter extends QueryFilter {

  private static final long serialVersionUID = 5478887785933499293L;

  private String statementNum;
  private String contractNum;
  private String subjectUuid;
  private String invoiceBillNum;
  private String invoiceCode;
  private String invoiceNumber;
  private String sourceBillType;
  private String sourceBillNumber;
  private String counterpartUuid;
  private String accountUnitUuid;
  private String counterpartType;

  public String getStatementNum() {
    return statementNum;
  }

  public void setStatementNum(String statementNum) {
    this.statementNum = statementNum;
  }

  public String getContractNum() {
    return contractNum;
  }

  public void setContractNum(String contractNum) {
    this.contractNum = contractNum;
  }

  public String getSubjectUuid() {
    return subjectUuid;
  }

  public void setSubjectUuid(String subjectUuid) {
    this.subjectUuid = subjectUuid;
  }

  public String getInvoiceBillNum() {
    return invoiceBillNum;
  }

  public void setInvoiceBillNum(String invoiceBillNum) {
    this.invoiceBillNum = invoiceBillNum;
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

  public String getSourceBillType() {
    return sourceBillType;
  }

  public void setSourceBillType(String sourceBillType) {
    this.sourceBillType = sourceBillType;
  }

  public String getSourceBillNumber() {
    return sourceBillNumber;
  }

  public void setSourceBillNumber(String sourceBillNumber) {
    this.sourceBillNumber = sourceBillNumber;
  }

  public String getCounterpartUuid() {
    return counterpartUuid;
  }

  public void setCounterpartUuid(String counterpartUuid) {
    this.counterpartUuid = counterpartUuid;
  }

  public String getCounterpartType() {
    return counterpartType;
  }

  public void setCounterpartType(String counterpartType) {
    this.counterpartType = counterpartType;
  }

  public String getAccountUnitUuid() {
    return accountUnitUuid;
  }

  public void setAccountUnitUuid(String accountUnitUuid) {
    this.accountUnitUuid = accountUnitUuid;
  }

}
