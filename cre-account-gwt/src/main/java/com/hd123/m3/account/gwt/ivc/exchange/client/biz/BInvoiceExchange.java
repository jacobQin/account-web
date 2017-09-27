/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceExchange.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月17日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.commons.gwt.base.client.biz.BStandardBill;
import com.hd123.m3.commons.gwt.base.client.biz.HasStore;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 发票交换单|B对象
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class BInvoiceExchange extends BStandardBill implements HasStore {

  private static final long serialVersionUID = 6414699665431311413L;

  private BUCN accountUnit;
  private BUCN tenant;
  private BExchangeType type = BExchangeType.eviToEvi;
  private BInvToInvType invToInvType = BInvToInvType.balance;
  private BUCN exchanger;
  private Date exchangeDate;
  private String remark;

  private String newInvoiceNumber;

  private List<BInvoiceExchBalanceLine> exchBalanceLines = new ArrayList<BInvoiceExchBalanceLine>();
  private List<BInvoiceExchAccountLine> exchAccountLines = new ArrayList<BInvoiceExchAccountLine>();
  private List<BInvoiceExchAccountLine2> exchAccountLines2 = new ArrayList<BInvoiceExchAccountLine2>();
  private List<BInvoiceExchangeAccountAdjLine> accountAdjLines = new ArrayList<BInvoiceExchangeAccountAdjLine>();

  @Override
  public BUCN getStore() {
    return accountUnit;
  }

  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  public BUCN getTenant() {
    return tenant;
  }
  
  public void setTenant(BUCN tenant) {
    this.tenant = tenant;
  }
  
  public BExchangeType getType() {
    return type;
  }

  public void setType(BExchangeType type) {
    this.type = type;
  }

  public BInvToInvType getInvToInvType() {
    return invToInvType;
  }

  public void setInvToInvType(BInvToInvType invToInvType) {
    this.invToInvType = invToInvType;
  }

  public BUCN getExchanger() {
    return exchanger;
  }

  public void setExchanger(BUCN exchanger) {
    this.exchanger = exchanger;
  }

  public Date getExchangeDate() {
    return exchangeDate;
  }

  public void setExchangeDate(Date exchangeDate) {
    this.exchangeDate = exchangeDate;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  /** 新登记的发票号 */
  public String getNewInvoiceNumber() {
    return newInvoiceNumber;
  }

  public void setNewInvoiceNumber(String newInvoiceNumber) {
    this.newInvoiceNumber = newInvoiceNumber;
  }

  public List<BInvoiceExchBalanceLine> getExchBalanceLines() {
    return exchBalanceLines;
  }

  public void setExchBalanceLines(List<BInvoiceExchBalanceLine> exchBalanceLines) {
    this.exchBalanceLines = exchBalanceLines;
  }

  public List<BInvoiceExchAccountLine> getExchAccountLines() {
    return exchAccountLines;
  }

  public void setExchAccountLines(List<BInvoiceExchAccountLine> exchAccountLines) {
    this.exchAccountLines = exchAccountLines;
  }

  public List<BInvoiceExchAccountLine2> getExchAccountLines2() {
    return exchAccountLines2;
  }

  public void setExchAccountLines2(List<BInvoiceExchAccountLine2> exchAccountLines2) {
    this.exchAccountLines2 = exchAccountLines2;
  }
  
  /**账款调整 */
  public List<BInvoiceExchangeAccountAdjLine> getAccountAdjLines() {
    return accountAdjLines;
  }
  
  public void setAccountAdjLines(List<BInvoiceExchangeAccountAdjLine> accountAdjLines) {
    this.accountAdjLines = accountAdjLines;
  }

}
