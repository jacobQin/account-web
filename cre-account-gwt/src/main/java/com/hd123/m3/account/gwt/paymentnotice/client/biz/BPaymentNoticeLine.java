/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BPaymentNoticeLine.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-14 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.biz;

import java.math.BigDecimal;

import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.m3.account.gwt.util.client.ObjectUtil;
import com.hd123.rumba.commons.gwt.entity.client.BEntity;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 收付款通知单明细|表示层实体
 * 
 * @author zhuhairui
 * 
 */
public class BPaymentNoticeLine extends BEntity {

  private static final long serialVersionUID = 3054459764020548878L;

  private int lineNumber;
  private BBill statement;
  private BUCN contract;
  private BUCN accountUnit;
  private BigDecimal receiptTotal;
  private BigDecimal receiptTax;
  private BigDecimal receiptIvcTotal;
  private BigDecimal receiptIvcTax;
  private BigDecimal paymentTotal;
  private BigDecimal paymentTax;
  private BigDecimal paymentIvcTotal;
  private BigDecimal paymentIvcTax;
  private String remark;

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public BBill getStatement() {
    return statement;
  }

  public void setStatement(BBill statement) {
    this.statement = statement;
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

  public BigDecimal getReceiptIvcTotal() {
    return receiptIvcTotal;
  }

  public void setReceiptIvcTotal(BigDecimal receiptIvcTotal) {
    this.receiptIvcTotal = receiptIvcTotal;
  }

  public BigDecimal getReceiptIvcTax() {
    return receiptIvcTax;
  }

  public void setReceiptIvcTax(BigDecimal receiptIvcTax) {
    this.receiptIvcTax = receiptIvcTax;
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

  public BigDecimal getPaymentIvcTotal() {
    return paymentIvcTotal;
  }

  public void setPaymentIvcTotal(BigDecimal paymentIvcTotal) {
    this.paymentIvcTotal = paymentIvcTotal;
  }

  public BigDecimal getPaymentIvcTax() {
    return paymentIvcTax;
  }

  public void setPaymentIvcTax(BigDecimal paymentIvcTax) {
    this.paymentIvcTax = paymentIvcTax;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public boolean equalsKey(BPaymentNoticeLine target) {
    if (getStatement() != null && getStatement().getBillNumber() != null)
      return ObjectUtil.isEquals(getStatement().getBillNumber(), target.getStatement()
          .getBillNumber());
    else
      return false;
  }

}
