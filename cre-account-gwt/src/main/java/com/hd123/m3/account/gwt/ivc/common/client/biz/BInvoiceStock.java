/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceStock.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月10日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.common.client.biz;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.m3.commons.gwt.base.client.biz.BStandardBill;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * B对象|发票库存
 * 
 * @author lixiaohong
 * @since 1.7
 * 
 */
public class BInvoiceStock extends BStandardBill {
  private static final long serialVersionUID = 6831578990101338744L;

  private String sourceBillNumber;
  private BUCN accountUnit;
  private String invoiceType;
  private String remark;

  private BUCN holder;
  private String invoiceCode;
  private String invoiceNumber;
  private Date abortDate;
  private String state = BStockState.instock.name();
  private BigDecimal amount;
  private int useType;
  private String balanceCode;
  private String balanceNumber;
  private Date instockTime;

  private int sort;

  /** 来源单据单号 */
  public String getSourceBillNumber() {
    return sourceBillNumber;
  }

  public void setSourceBillNumber(String sourceBillNumber) {
    this.sourceBillNumber = sourceBillNumber;
  }

  /** 所属项目 */
  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  /** 票据类型 */
  public String getInvoiceType() {
    return invoiceType;
  }

  public void setInvoiceType(String invoiceType) {
    this.invoiceType = invoiceType;
  }

  /** 说明 */
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  /** 持有人 */
  public BUCN getHolder() {
    return holder;
  }

  public void setHolder(BUCN holder) {
    this.holder = holder;
  }

  /** 发票代码 */
  public String getInvoiceCode() {
    return invoiceCode;
  }

  public void setInvoiceCode(String invoiceCode) {
    this.invoiceCode = invoiceCode;
  }

  /** 发票号码 */
  public String getInvoiceNumber() {
    return invoiceNumber;
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  /** 作废日期 */
  public Date getAbortDate() {
    return abortDate;
  }

  public void setAbortDate(Date abortDate) {
    this.abortDate = abortDate;
  }

  /** 发票库存状态 */
  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  /** 登记金额 */
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  /** 使用方式 */
  public int getUseType() {
    return useType;
  }

  public void setBalanceCode(int useType) {
    this.useType = useType;
  }
  
  /** 红冲发票代码 */
  public String getBalanceCode() {
    return balanceCode;
  }

  public void setBalanceCode(String balanceCode) {
    this.balanceCode = balanceCode;
  }

  /** 红冲发票号码 */
  public String getBalanceNumber() {
    return balanceNumber;
  }

  public void setBalanceNumber(String balanceNumber) {
    this.balanceNumber = balanceNumber;
  }

  /** 入库时间 */
  public Date getInstockTime() {
    return instockTime;
  }

  public void setInstockTime(Date instockTime) {
    this.instockTime = instockTime;
  }

  /** 票据类型：0-发票，1-收据 */
  public int getSort() {
    return sort;
  }

  public void setSort(int sort) {
    this.sort = sort;
  }
}
