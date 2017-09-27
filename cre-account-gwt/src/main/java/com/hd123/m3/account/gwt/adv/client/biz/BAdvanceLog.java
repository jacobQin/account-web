/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BAdvanceLog.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.adv.client.biz;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.BSourceBill;
import com.hd123.rumba.commons.gwt.entity.client.BEntity;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 预存款账户查看日志|表示层实体
 * 
 * @author zhuhairui
 * 
 */
public class BAdvanceLog extends BEntity {
  private static final long serialVersionUID = -2644008598078276021L;

  private Date time;
  private BUCN accountUnit;
  private BCounterpart counterpart;
  private String contractBillNum;
  private BSourceBill sourceBill;
  private BUCN subject;
  private BigDecimal beforeTotal;
  private BigDecimal total;
  private BigDecimal afterTotal;

  public Date getTime() {
    return time;
  }

  public void setTime(Date time) {
    this.time = time;
  }

  /** 项目(结算单位) */
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

  public String getContractBillNum() {
    return contractBillNum;
  }

  public void setContractBillNum(String contractBillNum) {
    this.contractBillNum = contractBillNum;
  }

  public BSourceBill getSourceBill() {
    return sourceBill;
  }

  public void setSourceBill(BSourceBill sourceBill) {
    this.sourceBill = sourceBill;
  }

  public BUCN getSubject() {
    return subject;
  }

  public void setSubject(BUCN subject) {
    this.subject = subject;
  }

  public BigDecimal getBeforeTotal() {
    return beforeTotal;
  }

  public void setBeforeTotal(BigDecimal beforeTotal) {
    this.beforeTotal = beforeTotal;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public BigDecimal getAfterTotal() {
    return afterTotal;
  }

  public void setAfterTotal(BigDecimal afterTotal) {
    this.afterTotal = afterTotal;
  }
}
