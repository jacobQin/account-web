/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BDepositMove.java
 * 模块说明：	
 * 修改历史：
 * 2014-02-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.commons.client.biz;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.m3.account.gwt.commons.client.biz.BAccStandardBill;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.commons.gwt.base.client.biz.HasStore;
import com.hd123.rumba.commons.gwt.entity.client.HasUCN;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 预存款转移单|B对象
 * 
 * @author zhuhairui
 * 
 */
public class BDepositMove extends BAccStandardBill implements HasStore {
  private static final long serialVersionUID = -3416945145007941302L;

  private int direction;
  private BUCN accountUnit;

  private BCounterpart outCounterpart;
  private BUCN outContract;
  private BUCN outSubject;
  private BigDecimal outBalance;
  private Date accountDate;
  

  private BUCN inContract;
  private BCounterpart inCounterpart;
  private BUCN inSubject;
  private BigDecimal inBalance;

  private BigDecimal amount;

  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public BCounterpart getOutCounterpart() {
    return outCounterpart;
  }

  public void setOutCounterpart(BCounterpart outCounterpart) {
    this.outCounterpart = outCounterpart;
  }

  public BUCN getOutContract() {
    return outContract;
  }

  public void setOutContract(BUCN outContract) {
    this.outContract = outContract;
  }

  public BUCN getOutSubject() {
    return outSubject;
  }

  public void setOutSubject(BUCN outSubject) {
    this.outSubject = outSubject;
  }

  public BUCN getInContract() {
    return inContract;
  }

  public void setInContract(BUCN inContract) {
    this.inContract = inContract;
  }

  public BCounterpart getInCounterpart() {
    return inCounterpart;
  }

  public void setInCounterpart(BCounterpart inCounterpart) {
    this.inCounterpart = inCounterpart;
  }

  public BUCN getInSubject() {
    return inSubject;
  }

  public void setInSubject(BUCN inSubject) {
    this.inSubject = inSubject;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public BigDecimal getInBalance() {
    return inBalance;
  }

  public void setInBalance(BigDecimal inBalance) {
    this.inBalance = inBalance;
  }

  public BigDecimal getOutBalance() {
    return outBalance;
  }

  public void setOutBalance(BigDecimal outBalance) {
    this.outBalance = outBalance;
  }

  public Date getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(Date accountDate) {
    this.accountDate = accountDate;
  }
  
  public String toFriendlyStr() {
    return getBillNumber();
  }

  @Override
  public String toString() {
    return toFriendlyStr();
  }

  @Override
  public HasUCN getStore() {
    return accountUnit;
  }
}
