/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementFilter.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-15 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.ui.filter;

import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * 账单查询filter
 * 
 * @author zhuhairui
 * 
 */
public class StatementFilter extends QueryFilter {

  private static final long serialVersionUID = -8257415303573950433L;

  private String noticeUuid;
  private String billNumber;
  private String settleNo;
  private String accountUnitUuid;
  private String counterPartUuid;
  private String counterpartType;
  private String contractNum;
  private String contractTitle;

  public String getNoticeUuid() {
    return noticeUuid;
  }

  public void setNoticeUuid(String noticeUuid) {
    this.noticeUuid = noticeUuid;
  }

  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }

  public String getSettleNo() {
    return settleNo;
  }

  public void setSettleNo(String settleNo) {
    this.settleNo = settleNo;
  }

  public String getAccountUnitUuid() {
    return accountUnitUuid;
  }

  public void setAccountUnitUuid(String accountUnitUuid) {
    this.accountUnitUuid = accountUnitUuid;
  }

  public String getCounterPartUuid() {
    return counterPartUuid;
  }

  public void setCounterPartUuid(String counterPartUuid) {
    this.counterPartUuid = counterPartUuid;
  }

  public String getCounterpartType() {
    return counterpartType;
  }

  public void setCounterpartType(String counterpartType) {
    this.counterpartType = counterpartType;
  }

  public String getContractNum() {
    return contractNum;
  }

  public void setContractNum(String contractNum) {
    this.contractNum = contractNum;
  }

  public String getContractTitle() {
    return contractTitle;
  }

  public void setContractTitle(String contractTitle) {
    this.contractTitle = contractTitle;
  }

}
