/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BAcc1.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.acc.client;

import java.io.Serializable;
import java.util.Date;

import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.BSourceBill;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxRate;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author chenwenfeng
 * 
 */
public class BAcc1 implements Serializable {
  private static final long serialVersionUID = 645977892603221103L;

  private String id;
  private BUCN contract;
  private BUCN accountUnit;
  private BCounterpart counterpart;
  private BUCN subject;
  private String usageName;
  private int direction;
  private BTaxRate taxRate;
  private Date accountDate;
  private BSourceBill sourceBill;
  private Date beginTime;
  private Date endTime;
  private Date ocrTime;
  private String remark;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BUCN getContract() {
    return contract;
  }

  public void setContract(BUCN contract) {
    this.contract = contract;
  }

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

  public BUCN getSubject() {
    return subject;
  }

  public void setSubject(BUCN subject) {
    this.subject = subject;
  }

  /** 科目用途名称 */
  public String getUsageName() {
    return usageName;
  }

  public void setUsageName(String usageName) {
    this.usageName = usageName;
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public BTaxRate getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(BTaxRate taxRate) {
    this.taxRate = taxRate;
  }

  public Date getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(Date accountDate) {
    this.accountDate = accountDate;
  }

  public BSourceBill getSourceBill() {
    return sourceBill;
  }

  public void setSourceBill(BSourceBill sourceBill) {
    this.sourceBill = sourceBill;
  }

  public Date getBeginTime() {
    return beginTime;
  }

  public void setBeginTime(Date beginTime) {
    this.beginTime = beginTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public Date getOcrTime() {
    return ocrTime;
  }

  public void setOcrTime(Date ocrTime) {
    this.ocrTime = ocrTime;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public BAcc1 clone() {
    BAcc1 r = new BAcc1();
    r.id = id;
    r.contract = new BUCN(contract.getUuid(), contract.getCode(), contract.getName());
    r.accountUnit = new BUCN(accountUnit.getUuid(), accountUnit.getCode(), accountUnit.getName());
    BCounterpart c = new BCounterpart();
    c.setCode(counterpart.getCode());
    c.setName(counterpart.getName());
    c.setUuid(counterpart.getUuid());
    c.setCounterpartType(counterpart.getCounterpartType());
    r.counterpart = c;
    r.subject = new BUCN(subject.getUuid(), subject.getCode(), subject.getName());
    r.direction = direction;
    r.taxRate = taxRate == null ? null : taxRate.clone();
    r.accountDate = accountDate;
    r.sourceBill = new BSourceBill(sourceBill.getBillUuid(), sourceBill.getBillNumber(),
        sourceBill.getBillType());
    r.beginTime = beginTime;
    r.endTime = endTime;
    r.ocrTime = ocrTime;
    r.remark = remark;
    return r;
  }
}
