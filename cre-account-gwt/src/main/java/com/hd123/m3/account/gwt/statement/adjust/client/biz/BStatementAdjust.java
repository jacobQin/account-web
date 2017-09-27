/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BStatementAdjust.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.commons.client.biz.BAccStandardBill;
import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.commons.gwt.base.client.biz.HasStore;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.commons.gwt.entity.client.HasUCN;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 账单调整单|表示层实体
 * 
 * @author zhuhairui
 * 
 */
public class BStatementAdjust extends BAccStandardBill implements HasStore {
  private static final long serialVersionUID = 4016922239357642654L;

  private BUCN accountUnit;
  private BCounterpart counterpart;
  private BContract contract;
  private BBill statement;
  private String bizState;
  private BigDecimal receiptTotal;
  private BigDecimal receiptTax;
  private BigDecimal paymentTotal;
  private BigDecimal paymentTax;
  private List<BDateRange> accountRanges = new ArrayList<BDateRange>();

  private List<BStatementAdjustLine> lines = new ArrayList<BStatementAdjustLine>();

  /** 结算单位 */
  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  /** 商户 */
  public BCounterpart getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BCounterpart counterpart) {
    this.counterpart = counterpart;
  }

  /** 合同 */
  public BContract getBcontract() {
    return contract;
  }

  public void setBcontract(BContract bcontract) {
    this.contract = bcontract;
  }

  /** 账单编号 */
  public BBill getStatement() {
    return statement;
  }

  public void setStatement(BBill statement) {
    this.statement = statement;
  }

  /** 业务状态 */
  public String getBizState() {
    return bizState;
  }

  public void setBizState(String bizState) {
    this.bizState = bizState;
  }

  public BigDecimal getReceiptTotal() {
    return receiptTotal;
  }

  public void setReceiptTotal(BigDecimal receiptTotal) {
    this.receiptTotal = receiptTotal;
  }

  public BigDecimal getReceiptTax() {
    return receiptTax;
  }

  public void setReceiptTax(BigDecimal receiptTax) {
    this.receiptTax = receiptTax;
  }

  public BigDecimal getPaymentTotal() {
    return paymentTotal;
  }

  public void setPaymentTotal(BigDecimal paymentTotal) {
    this.paymentTotal = paymentTotal;
  }

  public BigDecimal getPaymentTax() {
    return paymentTax;
  }

  public void setPaymentTax(BigDecimal paymentTax) {
    this.paymentTax = paymentTax;
  }

  /** 帐单周期 */
  public List<BDateRange> getAccountRanges() {
    return accountRanges;
  }

  public void setAccountRanges(List<BDateRange> accountRanges) {
    this.accountRanges = accountRanges;
  }

  /** 账单调整明细 */
  public List<BStatementAdjustLine> getLines() {
    return lines;
  }

  public void setLines(List<BStatementAdjustLine> lines) {
    this.lines = lines;
  }

  /**
   * 按照付款登记方向合计。
   */
  public void aggregate() {
    BigDecimal receiveTotal = BigDecimal.ZERO;
    BigDecimal receiveTax = BigDecimal.ZERO;
    BigDecimal localPaymentTotal = BigDecimal.ZERO;
    BigDecimal localPaymentTax = BigDecimal.ZERO;

    for (BStatementAdjustLine line : getLines()) {
      if (BDirection.PAY.equals(line.getDirection())) {
        localPaymentTotal = localPaymentTotal.add(line.getTotal().getTotal());
        localPaymentTax = localPaymentTax.add(line.getTotal().getTax());
      }

      if (BDirection.RECEIVE.equals(line.getDirection())) {
        receiveTotal = receiveTotal.add(line.getTotal().getTotal());
        receiveTax = receiveTax.add(line.getTotal().getTax());
      }
    }
    setReceiptTotal(receiveTotal);
    setReceiptTax(receiveTax);
    setPaymentTotal(localPaymentTotal);
    setPaymentTax(localPaymentTax);
  }

  @Override
  public String toString() {
    return this.getBillNumber();
  }

  @Override
  public HasUCN getStore() {
    return accountUnit;
  }
}
