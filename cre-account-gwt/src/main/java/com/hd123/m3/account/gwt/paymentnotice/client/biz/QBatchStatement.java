/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	QBatchStatement.java
 * 模块说明：	
 * 修改历史：
 * 2014-4-1 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.biz;

import java.io.Serializable;

import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author zhuhairui
 * 
 */
public class QBatchStatement implements Serializable {
  private static final long serialVersionUID = 3768792896879975898L;

  private BUCN accountUnit;
  private BCounterpart counterpart;
  private String contractUuid;
  private String contractBillNumber;
  private String contractTitle;
  private BUCN floor;
  private String coopMode;

  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  public BCounterpart getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BCounterpart counterpart) {
    this.counterpart = counterpart;
  }

  public String getContractUuid() {
    return contractUuid;
  }

  public void setContractUuid(String contractUuid) {
    this.contractUuid = contractUuid;
  }

  public String getContractBillNumber() {
    return contractBillNumber;
  }

  public void setContractBillNumber(String contractBillNumber) {
    this.contractBillNumber = contractBillNumber;
  }

  public String getContractTitle() {
    return contractTitle;
  }

  public void setContractTitle(String contractTitle) {
    this.contractTitle = contractTitle;
  }

  public BUCN getFloor() {
    return floor;
  }

  public void setFloor(BUCN floor) {
    this.floor = floor;
  }

  public String getCoopMode() {
    return coopMode;
  }

  public void setCoopMode(String coopMode) {
    this.coopMode = coopMode;
  }

  public String getUuids() {
    StringBuilder sb = new StringBuilder();
    sb.append(accountUnit.getUuid());
    sb.append(counterpart.getUuid());
    return sb.toString();
  }

}
