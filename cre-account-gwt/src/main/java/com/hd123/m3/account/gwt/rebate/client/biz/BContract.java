/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	BContractDoc.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月12日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.commons.gwt.base.client.tax.BTaxRate;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author chenganbang
 */
public class BContract extends BUCN {
  private static final long serialVersionUID = 2733877921997662693L;

  private BUCN accountUnit;
  private BUCN counterpart;
  private String positions;

  private boolean rebateBack = false;// 是否存在销售额返款
  private boolean rebateInvoice = false;
  private BUCN rebateSubject;
  private int rebateDirection = 0;// 收付方向

  private boolean bankRate = false;// 是否存在银行手续费扣率条款
  private boolean poundageInvoice = false;
  private BUCN poundageSubject;
  private int poundageDirection = 0;

  private Date lastestAccountDate;// 最后出账日期
  private Date beginDate;// 合同起始日期
  private Date endDate;// 合同截止日期

  private Date rebateBeginDate;// 未开的最小销售额返款的开始时间
  private Date rebateEndDate;// 未开的最小销售额返款的开始时间

  private List<BDateRange> ranges = new ArrayList<BDateRange>();// 账款类型为销售返款的账期明细
  private String posMode;// 收银
  private BTaxRate poundageTaxRate;
  private BTaxRate backTaxRate;

  public BTaxRate getPoundageTaxRate() {
    return poundageTaxRate;
  }

  public void setPoundageTaxRate(BTaxRate poundageTaxRate) {
    this.poundageTaxRate = poundageTaxRate;
  }

  public BTaxRate getBackTaxRate() {
    return backTaxRate;
  }

  public void setBackTaxRate(BTaxRate backTaxRate) {
    this.backTaxRate = backTaxRate;
  }

  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  public BUCN getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BUCN counterpart) {
    this.counterpart = counterpart;
  }

  public String getPositions() {
    return positions;
  }

  public void setPositions(String positions) {
    this.positions = positions;
  }

  /** 销售额返款是否开发票 */
  public boolean isRebateInvoice() {
    return rebateInvoice;
  }

  public void setRebateInvoice(boolean rebateInvoice) {
    this.rebateInvoice = rebateInvoice;
  }

  /** 手续费是否开发票 */
  public boolean isPoundageInvoice() {
    return poundageInvoice;
  }

  public void setPoundageInvoice(boolean poundageInvoice) {
    this.poundageInvoice = poundageInvoice;
  }

  /** 返款科目 */
  public BUCN getRebateSubject() {
    return rebateSubject;
  }

  public void setRebateSubject(BUCN rebateSubject) {
    this.rebateSubject = rebateSubject;
  }

  /** 手续费科目，手续费不为0时禁止为null */
  public BUCN getPoundageSubject() {
    return poundageSubject;
  }

  public void setPoundageSubject(BUCN poundageSubject) {
    this.poundageSubject = poundageSubject;
  }

  /**
   * 是否存在销售额返款,默认为false
   * 
   * @return
   */
  public boolean isRebateBack() {
    return rebateBack;
  }

  public void setRebateBack(boolean rebateBack) {
    this.rebateBack = rebateBack;
  }

  /**
   * 是否存在银行手续费扣率条款,默认为false
   * 
   * @return
   */
  public boolean isBankRate() {
    return bankRate;
  }

  public void setBankRate(boolean bankRate) {
    this.bankRate = bankRate;
  }

  /**
   * 账款类型为销售返款的账期明细
   * 
   * @return
   */
  public List<BDateRange> getRanges() {
    return ranges;
  }

  public void setRanges(List<BDateRange> ranges) {
    this.ranges = ranges;
  }

  /**
   * 最后出账日期
   * 
   * @return
   */
  public Date getLastestAccountDate() {
    return lastestAccountDate;
  }

  public void setLastestAccountDate(Date lastestAccountDate) {
    this.lastestAccountDate = lastestAccountDate;
  }

  /**
   * 合同起始日期
   * 
   * @return
   */
  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Date getRebateBeginDate() {
    return rebateBeginDate;
  }

  public void setRebateBeginDate(Date rebateBeginDate) {
    this.rebateBeginDate = rebateBeginDate;
  }

  public Date getRebateEndDate() {
    return rebateEndDate;
  }

  public void setRebateEndDate(Date rebateEndDate) {
    this.rebateEndDate = rebateEndDate;
  }

  /**
   * 收银方式
   * 
   * @return
   */
  public String getPosMode() {
    return posMode;
  }

  public void setPosMode(String posMode) {
    this.posMode = posMode;
  }

  public int getRebateDirection() {
    return rebateDirection;
  }

  public void setRebateDirection(int rebateDirection) {
    this.rebateDirection = rebateDirection;
  }

  public int getPoundageDirection() {
    return poundageDirection;
  }

  public void setPoundageDirection(int poundageDirection) {
    this.poundageDirection = poundageDirection;
  }

}
