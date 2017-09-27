/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： AccountDataFilter.java
 * 模块说明：    
 * 修改历史：
 * 2013-11-8 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.rpc;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.commons.gwt.mini.client.util.Pair;
import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * 账款搜索条件
 * 
 * @author subinzhu
 * 
 */
public class IvcRegAccountFilter extends QueryFilter {
  private static final long serialVersionUID = -4176236262184993000L;

  private String invoiceRegUuid;
  private String noticeBillNumber;
  private String paymentBillNumber;
  private String statementBillNumber;

  private String accountUnitUuid;
  private String counterpartUuid;
  private String counterpart;
  private String counterpartType;
  private String contract;
  private String contractTitle;
  private String sourceBillType;
  private String sourceBillNumber;
  private String subject;
  private Integer accDirection;
  private BDateRange statementAccountTime;

  private List<Pair<String, String>> excepts = new ArrayList<Pair<String, String>>();

  /** 发票登记单uuid */
  public String getInvoiceRegUuid() {
    return invoiceRegUuid;
  }

  public void setInvoiceRegUuid(String invoiceRegUuid) {
    this.invoiceRegUuid = invoiceRegUuid;
  }

  /** 收款通知单单 */
  public String getNoticeBillNumber() {
    return noticeBillNumber;
  }

  public void setNoticeBillNumber(String noticeBillNumber) {
    this.noticeBillNumber = noticeBillNumber;
  }

  /** 收款单 */
  public String getPaymentBillNumber() {
    return paymentBillNumber;
  }

  public void setPaymentBillNumber(String paymentBillNumber) {
    this.paymentBillNumber = paymentBillNumber;
  }

  /** 账单 */
  public String getStatementBillNumber() {
    return statementBillNumber;
  }

  public void setStatementBillNumber(String statementBillNumber) {
    this.statementBillNumber = statementBillNumber;
  }

  /** 项目 */
  public String getAccountUnitUuid() {
    return accountUnitUuid;
  }

  public void setAccountUnitUuid(String accountUnitUuid) {
    this.accountUnitUuid = accountUnitUuid;
  }

  /** 对方单位 */
  public String getCounterpartUuid() {
    return counterpartUuid;
  }

  public void setCounterpartUuid(String counterpartUuid) {
    this.counterpartUuid = counterpartUuid;
  }

  /** 对方单位（代码名称 */
  public String getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(String counterpart) {
    this.counterpart = counterpart;
  }

  /** 对方单位类型 */
  public String getCounterpartType() {
    return counterpartType;
  }

  public void setCounterpartType(String counterpartType) {
    this.counterpartType = counterpartType;
  }

  /** 合同 */
  public String getContract() {
    return contract;
  }

  public void setContract(String contract) {
    this.contract = contract;
  }

  /** 合同店招 */
  public String getContractTitle() {
    return contractTitle;
  }

  public void setContractTitle(String contractTitle) {
    this.contractTitle = contractTitle;
  }

  /** 来源单据类型 */
  public String getSourceBillType() {
    return sourceBillType;
  }

  public void setSourceBillType(String sourceBillType) {
    this.sourceBillType = sourceBillType;
  }

  /** 来源单据 */
  public String getSourceBillNumber() {
    return sourceBillNumber;
  }

  public void setSourceBillNumber(String sourceBillNumber) {
    this.sourceBillNumber = sourceBillNumber;
  }

  /** 科目 */
  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  /** 账款科目方向 */
  public Integer getAccDirection() {
    return accDirection;
  }

  public void setAccDirection(Integer accDirection) {
    this.accDirection = accDirection;
  }

  /** 账单出账日期 */
  public BDateRange getStatementAccountTime() {
    return statementAccountTime;
  }

  public void setStatementAccountTime(BDateRange statementAccountTime) {
    this.statementAccountTime = statementAccountTime;
  }

  /** 除外的账款<accid,paymentUuid> */
  public List<Pair<String, String>> getExcepts() {
    return excepts;
  }

  public void setExcepts(List<Pair<String, String>> excepts) {
    this.excepts = excepts;
  }

}
