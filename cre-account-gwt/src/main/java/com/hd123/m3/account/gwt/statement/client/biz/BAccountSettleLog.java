/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BAccountSettleLog.java
 * 模块说明：	
 * 修改历史：
 * 2014年9月18日 - zhr - 创建。
 */
package com.hd123.m3.account.gwt.statement.client.biz;

import java.util.Date;

import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.commons.gwt.entity.client.BEntity;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author zhr
 * 
 */
public class BAccountSettleLog extends BEntity {
  private static final long serialVersionUID = -7281360091140878611L;

  private BUCN accountUnit;
  private BCounterpart countpart;
  private String contractTitle;
  private String contractBillNumber;
  private BUCN floor;
  private String coopMode;
  private String settlementCaption;
  private BDateRange settlementDateRange;
  private Date planDate;
  private Date accountTime;
  private String billCalculateType;
  private String statementBillNumber;
  private boolean empty;

  /** 项目 */
  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  /** 商户 */
  public BCounterpart getCountpart() {
    return countpart;
  }

  public void setCountpart(BCounterpart countpart) {
    this.countpart = countpart;
  }

  /** 店招，即合同标题 */
  public String getContractTitle() {
    return contractTitle;
  }

  public void setContractTitle(String contractTitle) {
    this.contractTitle = contractTitle;
  }

  /** 合同编号 */
  public String getContractBillNumber() {
    return contractBillNumber;
  }

  public void setContractBillNumber(String contractBillNumber) {
    this.contractBillNumber = contractBillNumber;
  }

  /** 楼层 */
  public BUCN getFloor() {
    return floor;
  }

  public void setFloor(BUCN floor) {
    this.floor = floor;
  }

  /** 合作方式 */
  public String getCoopMode() {
    return coopMode;
  }

  public void setCoopMode(String coopMode) {
    this.coopMode = coopMode;
  }

  /** 结算周期名称 */
  public String getSettlementCaption() {
    return settlementCaption;
  }

  public void setSettlementCaption(String settlementCaption) {
    this.settlementCaption = settlementCaption;
  }

  /** 结算周期起止日期 */
  public BDateRange getSettlementDateRange() {
    return settlementDateRange;
  }

  public void setSettlementDateRange(BDateRange settlementDateRange) {
    this.settlementDateRange = settlementDateRange;
  }

  /** 计划出账日期 */
  public Date getPlanDate() {
    return planDate;
  }

  public void setPlanDate(Date planDate) {
    this.planDate = planDate;
  }

  /** 出账时间 */
  public Date getAccountTime() {
    return accountTime;
  }

  public void setAccountTime(Date accountTime) {
    this.accountTime = accountTime;
  }

  /** 出账方式 */
  public String getBillCalculateType() {
    return billCalculateType;
  }

  public void setBillCalculateType(String billCalculateType) {
    this.billCalculateType = billCalculateType;
  }

  /** 账单单号 */
  public String getStatementBillNumber() {
    return statementBillNumber;
  }

  public void setStatementBillNumber(String statementBillNumber) {
    this.statementBillNumber = statementBillNumber;
  }

  /** 是否是空账单 */
  public boolean isEmpty() {
    return empty;
  }

  public void setEmpty(boolean empty) {
    this.empty = empty;
  }

}
