/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BFreeze.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-3 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.freeze.client.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.commons.client.biz.BAccStandardBill;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.freeze.intf.client.FreezeMessages;
import com.hd123.m3.commons.gwt.base.client.biz.HasStore;
import com.hd123.rumba.commons.gwt.entity.client.BOperateInfo;
import com.hd123.rumba.commons.gwt.entity.client.HasUCN;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.message.Message;

/**
 * 账款冻结单|表示层实体
 * 
 * @author zhuhairui
 * 
 */
public class BFreeze extends BAccStandardBill implements HasStore {
  private static final long serialVersionUID = -1875730958251101827L;

  private BUCN accountUnit;
  private String state;
  private BCounterpart counterpart;
  private BigDecimal freezePayTotal;
  private BigDecimal freezeRecTotal;
  private BigDecimal freezePayTax;
  private BigDecimal freezeRecTax;
  private BOperateInfo freezeInfo;
  private BOperateInfo unfreezeInfo;
  private String freezeReason;
  private String unfreezeReason;
  /** 验证实体时的报错信息 */
  private List<Message> messages = new ArrayList<Message>();

  private List<BFreezeLine> lines = new ArrayList<BFreezeLine>();

  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public BCounterpart getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BCounterpart counterpart) {
    this.counterpart = counterpart;
  }

  public BigDecimal getFreezePayTotal() {
    return freezePayTotal;
  }

  public void setFreezePayTotal(BigDecimal freezePayTotal) {
    this.freezePayTotal = freezePayTotal;
  }

  public BigDecimal getFreezeRecTotal() {
    return freezeRecTotal;
  }

  public void setFreezeRecTotal(BigDecimal freezeRecTotal) {
    this.freezeRecTotal = freezeRecTotal;
  }

  public BigDecimal getFreezePayTax() {
    return freezePayTax;
  }

  public void setFreezePayTax(BigDecimal freezePayTax) {
    this.freezePayTax = freezePayTax;
  }

  public BigDecimal getFreezeRecTax() {
    return freezeRecTax;
  }

  public void setFreezeRecTax(BigDecimal freezeRecTax) {
    this.freezeRecTax = freezeRecTax;
  }

  public BOperateInfo getFreezeInfo() {
    return freezeInfo;
  }

  public void setFreezeInfo(BOperateInfo freezeInfo) {
    this.freezeInfo = freezeInfo;
  }

  public BOperateInfo getUnfreezeInfo() {
    return unfreezeInfo;
  }

  public void setUnfreezeInfo(BOperateInfo unfreezeInfo) {
    this.unfreezeInfo = unfreezeInfo;
  }

  public String getFreezeReason() {
    return freezeReason;
  }

  public void setFreezeReason(String freezeReason) {
    this.freezeReason = freezeReason;
  }

  public String getUnfreezeReason() {
    return unfreezeReason;
  }

  public void setUnfreezeReason(String unfreezeReason) {
    this.unfreezeReason = unfreezeReason;
  }

  public List<Message> getMessages() {
    return messages;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  public List<BFreezeLine> getLines() {
    return lines;
  }

  public void setLines(List<BFreezeLine> lines) {
    this.lines = lines;
  }

  public void aggregate() {
    BigDecimal payTotal = BigDecimal.ZERO;
    BigDecimal receiptTotal = BigDecimal.ZERO;
    BigDecimal payTax = BigDecimal.ZERO;
    BigDecimal receiptTax = BigDecimal.ZERO;

    for (BFreezeLine line : getLines()) {
      if (line.getAcc1().getDirection() == DirectionType.payment.getDirectionValue()) {
        payTotal = payTotal.add(line.getTotal());
        payTax = payTax.add(line.getTax());
      } else if (line.getAcc1().getDirection() == DirectionType.receipt.getDirectionValue()) {
        receiptTotal = receiptTotal.add(line.getTotal());
        receiptTax = receiptTax.add(line.getTax());
      }
    }

    setFreezePayTotal(payTotal);
    setFreezePayTax(payTax);
    setFreezeRecTotal(receiptTotal);
    setFreezeRecTax(receiptTax);
  }

  public boolean isValid() {
    return messages.isEmpty();
  }

  public void clearValidResults() {
    messages.clear();
  }

  public boolean validateLine() {
    messages.clear();
    boolean noRecord = true;

    for (int i = 0; i < getLines().size(); i++) {
      BFreezeLine line = getLines().get(i);
      if (line.getAcc1() == null || line.getAcc1().getSubject() == null)
        continue;

      noRecord = false;
    }
    if (noRecord) {
      Message message = Message.error(FreezeMessages.M.lineNotNull());
      messages.add(message);
    }

    return isValid();
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
