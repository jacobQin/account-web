/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountDataFilter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-30 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.client.ui.filter;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.acc.client.BAccountId;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * 账款搜索条件
 * 
 * @author chenpeisi
 * 
 */
public class AccountDataFilter extends QueryFilter {
  private static final long serialVersionUID = -4176236262184993000L;

  private String statement;
  private String paymentNotice;
  private String sourceBillNumber;
  private String sourceBillType;
  private String accountUnit;
  private String counterpart;
  private String contract;
  private String contractName;
  private BDateRange accountTime;
  private String subject;

  private int directionType;

  /** 记录发票登记单的uuid， 允许为空。 用于查询账款时，将被当前发票登记单锁定的账款查询出来。 */
  private String invoiceRegUuid;

  /** 已导入账款id集合 */
  private List<BAccountId> accountIds = new ArrayList<BAccountId>();

  public String getSourceBillNumber() {
    return sourceBillNumber;
  }

  public void setSourceBillNumber(String sourceBillNumber) {
    this.sourceBillNumber = sourceBillNumber;
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

  public String getPaymentNotice() {
    return paymentNotice;
  }

  public void setPaymentNotice(String paymentNotice) {
    this.paymentNotice = paymentNotice;
  }

  public List<BAccountId> getAccountIds() {
    return accountIds;
  }

  public void setAccountIds(List<BAccountId> accountIds) {
    this.accountIds = accountIds;
  }

  public String getInvoiceRegUuid() {
    return invoiceRegUuid;
  }

  public void setInvoiceRegUuid(String invoiceRegUuid) {
    this.invoiceRegUuid = invoiceRegUuid;
  }

  public String getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(String accountUnit) {
    this.accountUnit = accountUnit;
  }
}
