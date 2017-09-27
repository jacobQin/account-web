/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-web
 * 文件名：	BAccountSettleLog.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月9日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.model;

import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.commons.biz.entity.Entity;
import com.hd123.rumba.gwt.base.client.BUCN;

import java.util.Date;

/**
 * 账单出账日志
 * 
 * @author chenganbang
 *
 */
public class BAccountSettleLog extends Entity {
  private static final long serialVersionUID = 796435638625940847L;

  private BUCN accountUnit;
  private BUCN counterpart;
  private BUCN contract;
  private BUCN floor;
  private String coopMode;
  private String settlementCaption;
  private BDateRange settlementDateRange;
  private Date planDate;
  private Date accountTime;
  private String billCalculateType;
  private String statementBillNumber;
  private String statementBillUuid;
  private boolean empty;
  private String counterpartType;

  /** 对方单位类型 */
  public String getCounterpartType() {
    return counterpartType;
  }

  public void setCounterpartType(String counterpartType) {
    this.counterpartType = counterpartType;
  }

  /** 项目 */
  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  /** 商户 */
  public BUCN getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BUCN counterpart) {
    this.counterpart = counterpart;
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

  /**
   * 合同
   * 
   * @return
   */
  public BUCN getContract() {
    return contract;
  }

  public void setContract(BUCN contract) {
    this.contract = contract;
  }

  public String getStatementBillUuid() {
    return statementBillUuid;
  }

  public void setStatementBillUuid(String statementBillUuid) {
    this.statementBillUuid = statementBillUuid;
  }

}
