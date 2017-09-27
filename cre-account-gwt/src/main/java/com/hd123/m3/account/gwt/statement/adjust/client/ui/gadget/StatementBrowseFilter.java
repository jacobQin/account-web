/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementBrowseFilter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-10 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.ui.gadget;

import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * 账单查询filter
 * 
 * @author zhuhairui
 * 
 */
public class StatementBrowseFilter extends QueryFilter {
  private static final long serialVersionUID = -2268929599674473700L;

  private String statementNum;
  private String settleNo;
  private String counterpart;
  private String counterpartType;
  private String contractNum;
  private String accountUnit;

  public String getStatementNum() {
    return statementNum;
  }

  public void setStatementNum(String statementNum) {
    this.statementNum = statementNum;
  }

  public String getSettleNo() {
    return settleNo;
  }

  public void setSettleNo(String settleNo) {
    this.settleNo = settleNo;
  }

  public String getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(String counterpart) {
    this.counterpart = counterpart;
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

  public String getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(String accountUnit) {
    this.accountUnit = accountUnit;
  }

}
