/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceExchangeAccountAdjLine.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月24日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.biz;

import java.io.Serializable;
import java.util.Date;

import com.hd123.m3.account.gwt.acc.client.BAcc1;
import com.hd123.m3.account.gwt.acc.client.BAcc2;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;

/**
 * 账款调整|B对象
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class BInvoiceExchangeAccountAdjLine implements Serializable {

  private static final long serialVersionUID = -7765265222342746987L;

  private BAcc1 acc1;
  private BAcc2 acc2;
  private BTotal total;

  private String adjId;
  private Date adjTime;
  private String adjRemark;

  private String newType;
  private String newCode;
  private String newNumber;

  public BAcc1 getAcc1() {
    return acc1;
  }

  public void setAcc1(BAcc1 acc1) {
    this.acc1 = acc1;
  }

  public BAcc2 getAcc2() {
    return acc2;
  }

  public void setAcc2(BAcc2 acc2) {
    this.acc2 = acc2;
  }

  public BTotal getTotal() {
    return total;
  }

  public void setTotal(BTotal total) {
    this.total = total;
  }

  public String getAdjId() {
    return adjId;
  }

  public void setAdjId(String adjId) {
    this.adjId = adjId;
  }

  public Date getAdjTime() {
    return adjTime;
  }

  public void setAdjTime(Date adjTime) {
    this.adjTime = adjTime;
  }

  public String getAdjRemark() {
    return adjRemark;
  }

  public void setAdjRemark(String adjRemark) {
    this.adjRemark = adjRemark;
  }

  public String getNewType() {
    return newType;
  }

  public void setNewType(String newType) {
    this.newType = newType;
  }

  public String getNewCode() {
    return newCode;
  }

  public void setNewCode(String newCode) {
    this.newCode = newCode;
  }

  public String getNewNumber() {
    return newNumber;
  }

  public void setNewNumber(String newNumber) {
    this.newNumber = newNumber;
  }

}
