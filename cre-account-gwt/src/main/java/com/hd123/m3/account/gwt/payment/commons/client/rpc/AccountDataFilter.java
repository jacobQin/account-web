/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： AccountDataFilter.java
 * 模块说明：    
 * 修改历史：
 * 2013-11-8 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.rpc;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.acc.client.BAccountId;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * 账款搜索条件
 * 
 * @author subinzhu
 * 
 */
public class AccountDataFilter extends QueryFilter {
  private static final long serialVersionUID = -4176236262184993000L;

  private String statement;
  private String invoiceRegNumber;
  private String paymentNotice;
  private String sourceBillNumber;

  private String accountUnitUuid;
  private String counterpartUuid;
  private String counterpart;
  private String counterpartType;
  private String contract;
  private String contractName;
  private BDateRange accountTime;
  private BDateRange regTime;
  private String sourceBillType;
  private String subject;
  private String invoiceCode;
  private String invoiceNumber;

  private int directionType;
  
  private boolean ignoreDirectionType = false;

  /** 记录收付款单的uuid， 允许为空。 用于查询账款时，将被当前收付款单锁定的账款查询出来。 */
  private String paymentUuid;

  /**
   * 已导入的来源单据uuid集合
   */
  private List<BAccountId> hasAddedAccIds = new ArrayList<BAccountId>();

  public String getSourceBillNumber() {
    return sourceBillNumber;
  }

  public void setSourceBillNumber(String sourceBillNumber) {
    this.sourceBillNumber = sourceBillNumber;
  }

  public String getAccountUnitUuid() {
    return accountUnitUuid;
  }

  public void setAccountUnitUuid(String accountUnitUuid) {
    this.accountUnitUuid = accountUnitUuid;
  }

  public String getCounterpartUuid() {
    return counterpartUuid;
  }

  public void setCounterpartUuid(String counterpartUuid) {
    this.counterpartUuid = counterpartUuid;
  }

  public String getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(String counterpart) {
    this.counterpart = counterpart;
  }

  public String getContract() {
    return contract;
  }

  public void setContract(String contract) {
    this.contract = contract;
  }

  public String getContractName() {
    return contractName;
  }

  public void setContractName(String contractName) {
    this.contractName = contractName;
  }

  public BDateRange getAccountTime() {
    return accountTime;
  }

  public void setAccountTime(BDateRange accountTime) {
    this.accountTime = accountTime;
  }

  public BDateRange getRegTime() {
    return regTime;
  }

  public void setRegTime(BDateRange regTime) {
    this.regTime = regTime;
  }

  public String getSourceBillType() {
    return sourceBillType;
  }

  public void setSourceBillType(String sourceBillType) {
    this.sourceBillType = sourceBillType;
  }

  public int getDirectionType() {
    return directionType;
  }

  public void setDirectionType(int directionType) {
    this.directionType = directionType;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getStatement() {
    return statement;
  }

  public void setStatement(String statement) {
    this.statement = statement;
  }

  public List<BAccountId> getHasAddedAccIds() {
    return hasAddedAccIds;
  }

  public void setHasAddedAccIds(List<BAccountId> accountIds) {
    this.hasAddedAccIds = accountIds;
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

  public String getInvoiceRegNumber() {
    return invoiceRegNumber;
  }

  public void setInvoiceRegNumber(String invoiceRegNumber) {
    this.invoiceRegNumber = invoiceRegNumber;
  }

  public String getPaymentUuid() {
    return paymentUuid;
  }

  public void setPaymentUuid(String paymentUuid) {
    this.paymentUuid = paymentUuid;
  }

  public String getPaymentNotice() {
    return paymentNotice;
  }

  public void setPaymentNotice(String paymentNotice) {
    this.paymentNotice = paymentNotice;
  }

  public String getCounterpartType() {
    return counterpartType;
  }

  public void setCounterpartType(String counterpartType) {
    this.counterpartType = counterpartType;
  }
  
  public boolean isIgnoreDirectionType() {
    return ignoreDirectionType;
  }

  public void setIgnoreDirectionType(boolean ignoreDirectionType) {
    this.ignoreDirectionType = ignoreDirectionType;
  }

}
