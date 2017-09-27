/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BPaymentNotice.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-11 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.commons.client.biz.BAccStandardBill;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.paymentnotice.client.PaymentNoticeMessages;
import com.hd123.m3.commons.gwt.base.client.biz.HasStore;
import com.hd123.rumba.commons.gwt.entity.client.HasUCN;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.message.Message;

/**
 * 收付款通知单|表示层实体
 * 
 * @author zhuhairui
 * 
 */
public class BPaymentNotice extends BAccStandardBill implements HasStore {

  private static final long serialVersionUID = -6357270168927632240L;

  private BUCN accountUnit;
  private String bizState;
  private BCounterpart counterpart;
  private BigDecimal receiptTotal;
  private BigDecimal receiptTax;
  private BigDecimal receiptIvcTotal;
  private BigDecimal receiptIvcTax;
  private BigDecimal paymentTotal;
  private BigDecimal paymentTax;
  private BigDecimal paymentIvcTotal;
  private BigDecimal paymentIvcTax;
  /** 验证实体时的报错信息 */
  private List<Message> messages = new ArrayList<Message>();

  private List<BPaymentNoticeLine> lines = new ArrayList<BPaymentNoticeLine>();

  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  public String getBizState() {
    return bizState;
  }

  public void setBizState(String bizState) {
    this.bizState = bizState;
  }

  public BCounterpart getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BCounterpart counterpart) {
    this.counterpart = counterpart;
  }

  /** 应收金额 */
  public BigDecimal getReceiptTotal() {
    return receiptTotal;
  }

  public void setReceiptTotal(BigDecimal receiptTotal) {
    this.receiptTotal = receiptTotal;
  }

  /** 应收税额 */
  public BigDecimal getReceiptTax() {
    return receiptTax;
  }

  public void setReceiptTax(BigDecimal receiptTax) {
    this.receiptTax = receiptTax;
  }

  /** 应收开票金额 */
  public BigDecimal getReceiptIvcTotal() {
    return receiptIvcTotal;
  }

  public void setReceiptIvcTotal(BigDecimal receiptIvcTotal) {
    this.receiptIvcTotal = receiptIvcTotal;
  }

  /** 应收开票税额 */
  public BigDecimal getReceiptIvcTax() {
    return receiptIvcTax;
  }

  public void setReceiptIvcTax(BigDecimal receiptIvcTax) {
    this.receiptIvcTax = receiptIvcTax;
  }

  /** 应付金额 */
  public BigDecimal getPaymentTotal() {
    return paymentTotal;
  }

  public void setPaymentTotal(BigDecimal paymentTotal) {
    this.paymentTotal = paymentTotal;
  }

  /** 应付税额 */
  public BigDecimal getPaymentTax() {
    return paymentTax;
  }

  public void setPaymentTax(BigDecimal paymentTax) {
    this.paymentTax = paymentTax;
  }

  /** 应付开票金额 */
  public BigDecimal getPaymentIvcTotal() {
    return paymentIvcTotal;
  }

  public void setPaymentIvcTotal(BigDecimal paymentIvcTotal) {
    this.paymentIvcTotal = paymentIvcTotal;
  }

  /** 应付开票税额 */
  public BigDecimal getPaymentIvcTax() {
    return paymentIvcTax;
  }

  public void setPaymentIvcTax(BigDecimal paymentIvcTax) {
    this.paymentIvcTax = paymentIvcTax;
  }

  /** 收付款通知单明细 */
  public List<BPaymentNoticeLine> getLines() {
    return lines;
  }

  public void setLines(List<BPaymentNoticeLine> lines) {
    this.lines = lines;
  }

  public void aggregate() {
    BigDecimal receiveTotal = BigDecimal.ZERO;
    BigDecimal receiveTax = BigDecimal.ZERO;
    BigDecimal receiptempIvcTotal = BigDecimal.ZERO;
    BigDecimal receiptempIvcTax = BigDecimal.ZERO;
    BigDecimal paymentempTotal = BigDecimal.ZERO;
    BigDecimal paymentempTax = BigDecimal.ZERO;
    BigDecimal paymentempIvcTotal = BigDecimal.ZERO;
    BigDecimal paymentempIvcTax = BigDecimal.ZERO;

    for (BPaymentNoticeLine line : getLines()) {
      receiveTotal = receiveTotal.add(line.getReceiptTotal());
      receiveTax = receiveTax.add(line.getReceiptTax());
      receiptempIvcTotal = receiptempIvcTotal.add(line.getReceiptIvcTotal());
      receiptempIvcTax = receiptempIvcTax.add(line.getReceiptIvcTax());
      paymentempTotal = paymentempTotal.add(line.getPaymentTotal());
      paymentempTax = paymentempTax.add(line.getPaymentTax());
      paymentempIvcTotal = paymentempIvcTotal.add(line.getPaymentIvcTotal());
      paymentempIvcTax = paymentempIvcTax.add(line.getPaymentIvcTax());
    }

    setReceiptTotal(receiveTotal);
    setReceiptTax(receiveTax);
    setReceiptIvcTotal(receiptempIvcTotal);
    setReceiptIvcTax(receiptempIvcTax);
    setPaymentTotal(paymentempTotal);
    setPaymentTax(paymentempTax);
    setPaymentIvcTotal(paymentempIvcTotal);
    setPaymentIvcTax(paymentempIvcTax);
  }

  public boolean validateLine() {
    messages.clear();
    boolean noRecord = true;
    for (int i = 0; i < getLines().size(); i++) {
      BPaymentNoticeLine line = getLines().get(i);
      if (line.getStatement() == null)
        continue;

      noRecord = false;
      for (int j = i + 1; j < getLines().size(); j++) {
        if (line.equalsKey(getLines().get(j))) {
          Message message = Message.error(PaymentNoticeMessages.M.lineNumber(i + 1) + "与"
              + PaymentNoticeMessages.M.lineNumber(j + 1) + PaymentNoticeMessages.M.repeat());
          messages.add(message);
        }
      }

    }
    if (noRecord) {
      Message message = Message.error(PaymentNoticeMessages.M.lineNotNull());
      messages.add(message);
    }

    return isValid();

  }

  public List<Message> getInvalidMessages() {
    return messages;
  }

  public boolean isValid() {
    return messages.isEmpty();
  }

  public void clearValidResults() {
    messages.clear();
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
