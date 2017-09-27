/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	BInvoiceStock.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月8日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.invoice;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.rumba.commons.gwt.entity.client.BEntity;
import com.hd123.rumba.commons.gwt.entity.client.HasUCN;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author chenrizhang
 *
 */
public class BInvoiceStock extends BEntity implements HasUCN {

  private static final long serialVersionUID = -365032781060799949L;

  public static final String KEY_FILTER_STATE = "filter_state";
  public static final String KEY_FILTER_INVOICE_TYPE = "filter_invoiceType";
  public static final String KEY_FILTER_USE_TYPE = "filter_useType";
  public static final String KEY_FILTER_ACCOUNTUNIT = "filter_accountUnit";
  public static final String KEY_FILTER_SORT = "filter_sort";

  private BUCN accountUnit;
  private String invoiceType;
  private String remark;

  private BUCN holder;
  private String invoiceCode;
  private String invoiceNumber;
  private Date abortDate;
  private BInvoiceStockState state;
  private BigDecimal amount;
  private int useType;
  private String balanceCode;
  private String balanceNumber;
  private Date instockTime;

  private int sort;

  /** 结算单位（项目） */
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

  @Override
  public String getName() {
    return invoiceCode;
  }

  @Override
  public void setName(String name) {
    invoiceCode = name;
  }

  /** 发票号码 */
  public String getInvoiceNumber() {
    return invoiceNumber;
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  @Override
  public String getCode() {
    return invoiceNumber;
  }

  @Override
  public void setCode(String code) {
    this.invoiceNumber = code;
  }

  /** 作废日期 */
  public Date getAbortDate() {
    return abortDate;
  }

  public void setAbortDate(Date abortDate) {
    this.abortDate = abortDate;
  }

  /** 状态 */
  public BInvoiceStockState getState() {
    return state;
  }

  public void setState(BInvoiceStockState state) {
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

  public void setUseType(int useType) {
    this.useType = useType;
  }

  /** 红冲代码 */
  public String getBalanceCode() {
    return balanceCode;
  }

  public void setBalanceCode(String balanceCode) {
    this.balanceCode = balanceCode;
  }

  /** 红冲号码 */
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

  /** 票据类型:0-发票，1-收据 */
  public int getSort() {
    return sort;
  }

  public void setSort(int sort) {
    this.sort = sort;
  }
}
