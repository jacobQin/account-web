/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BContract.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月15日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.rebate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.commons.biz.date.DateRange;
import com.hd123.m3.commons.biz.tax.TaxRate;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * @author chenganbang
 *
 */
public class BContract extends UCN {

  private static final long serialVersionUID = 6546971275539816415L;

  private UCN store;
  private UCN tenant;
  private String positions;

  private boolean rebateBack = false;// 是否存在销售额返款
  private boolean rebateInvoice = false;
  private UCN rebateSubject;
  private int rebateDirection = 0;// 收付方向

  private boolean bankRate = false;// 是否存在银行手续费扣率条款
  private boolean poundageInvoice = false;
  private UCN poundageSubject;
  private int poundageDirection = 0;

  private Date lastestAccountDate;// 最后出账日期
  private Date beginDate;// 合同起始日期
  private Date endDate;// 合同截止日期

  private Date rebateBeginDate;// 未开的最小销售额返款的开始时间
  private Date rebateEndDate;// 未开的最小销售额返款的开始时间

  private List<DateRange> ranges = new ArrayList<DateRange>();// 账款类型为销售返款的账期明细
  private String posMode;// 收银
  private TaxRate poundageTaxRate;
  private TaxRate backTaxRate;

  public UCN getStore() {
    return store;
  }

  public void setStore(UCN store) {
    this.store = store;
  }

  public UCN getTenant() {
    return tenant;
  }

  public void setTenant(UCN tenant) {
    this.tenant = tenant;
  }

  public String getPositions() {
    return positions;
  }

  public void setPositions(String positions) {
    this.positions = positions;
  }

  public boolean isRebateBack() {
    return rebateBack;
  }

  public void setRebateBack(boolean rebateBack) {
    this.rebateBack = rebateBack;
  }

  public boolean isRebateInvoice() {
    return rebateInvoice;
  }

  public void setRebateInvoice(boolean rebateInvoice) {
    this.rebateInvoice = rebateInvoice;
  }

  public UCN getRebateSubject() {
    return rebateSubject;
  }

  public void setRebateSubject(UCN rebateSubject) {
    this.rebateSubject = rebateSubject;
  }

  public int getRebateDirection() {
    return rebateDirection;
  }

  public void setRebateDirection(int rebateDirection) {
    this.rebateDirection = rebateDirection;
  }

  public boolean isBankRate() {
    return bankRate;
  }

  public void setBankRate(boolean bankRate) {
    this.bankRate = bankRate;
  }

  public boolean isPoundageInvoice() {
    return poundageInvoice;
  }

  public void setPoundageInvoice(boolean poundageInvoice) {
    this.poundageInvoice = poundageInvoice;
  }

  public UCN getPoundageSubject() {
    return poundageSubject;
  }

  public void setPoundageSubject(UCN poundageSubject) {
    this.poundageSubject = poundageSubject;
  }

  public int getPoundageDirection() {
    return poundageDirection;
  }

  public void setPoundageDirection(int poundageDirection) {
    this.poundageDirection = poundageDirection;
  }

  public Date getLastestAccountDate() {
    return lastestAccountDate;
  }

  public void setLastestAccountDate(Date lastestAccountDate) {
    this.lastestAccountDate = lastestAccountDate;
  }

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

  public List<DateRange> getRanges() {
    return ranges;
  }

  public void setRanges(List<DateRange> ranges) {
    this.ranges = ranges;
  }

  public String getPosMode() {
    return posMode;
  }

  public void setPosMode(String posMode) {
    this.posMode = posMode;
  }

  public TaxRate getPoundageTaxRate() {
    return poundageTaxRate;
  }

  public void setPoundageTaxRate(TaxRate poundageTaxRate) {
    this.poundageTaxRate = poundageTaxRate;
  }

  public TaxRate getBackTaxRate() {
    return backTaxRate;
  }

  public void setBackTaxRate(TaxRate backTaxRate) {
    this.backTaxRate = backTaxRate;
  }

}
