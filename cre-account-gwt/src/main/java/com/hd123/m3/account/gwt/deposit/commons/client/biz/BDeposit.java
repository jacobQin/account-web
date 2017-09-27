/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： BCollection.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-23- chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.commons.client.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.hd123.m3.account.gwt.commons.client.biz.BAccStandardBill;
import com.hd123.m3.account.gwt.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.commons.gwt.base.client.biz.HasStore;
import com.hd123.rumba.commons.gwt.entity.client.HasUCN;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.HasMessages;
import com.hd123.rumba.gwt.util.client.RDateUtil;
import com.hd123.rumba.gwt.util.client.message.Message;

/**
 * 
 * 预存款|B对象
 * 
 * @author chenpeisi
 * 
 */
public class BDeposit extends BAccStandardBill implements HasStore, HasMessages {
  private static final long serialVersionUID = 300200L;

  private BUCN accountUnit;
  private BCounterpart counterpart;
  private BUCN contract;
  private BUCN paymentType;
  private Date depositDate = RDateUtil.truncate(new Date(), RDateUtil.DAY_OF_MONTH);
  private BUCN dealer;
  private BBank bank;
  private String counterContact;
  private BigDecimal depositTotal = BigDecimal.ZERO;
  private int direction;
  private Date accountDate;
  private String settleNo;
  private String remark;

  private List<Message> messages;
  List<BDepositLine> lines = new ArrayList<BDepositLine>();

  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  public BCounterpart getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BCounterpart counterpart) {
    this.counterpart = counterpart;
  }

  public BUCN getContract() {
    return contract;
  }

  public void setContract(BUCN contract) {
    this.contract = contract;
  }

  public BUCN getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(BUCN paymentType) {
    this.paymentType = paymentType;
  }

  public Date getDepositDate() {
    return depositDate;
  }

  public void setDepositDate(Date depositDate) {
    this.depositDate = depositDate;
  }

  public BUCN getDealer() {
    return dealer;
  }

  public void setDealer(BUCN dealer) {
    this.dealer = dealer;
  }

  public BBank getBank() {
    return bank;
  }

  public void setBank(BBank bank) {
    this.bank = bank;
  }

  public String getCounterContact() {
    return counterContact;
  }

  public void setCounterContact(String counterContact) {
    this.counterContact = counterContact;
  }

  public BigDecimal getDepositTotal() {
    return depositTotal;
  }

  public void setDepositTotal(BigDecimal depositTotal) {
    this.depositTotal = depositTotal;
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public Date getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(Date accountDate) {
    this.accountDate = accountDate;
  }

  public String getSettleNo() {
    return settleNo;
  }

  public void setSettleNo(String settleNo) {
    this.settleNo = settleNo;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public List<BDepositLine> getLines() {
    return lines;
  }

  public void setLines(List<BDepositLine> lines) {
    this.lines = lines;
  }

  public boolean hasValues() {
    for (BDepositLine line : lines) {
      if (line.getSubject() != null && line.getSubject().getUuid() != null) {
        return true;
      }
    }
    return false;
  }

  private void refreshLineNumber() {
    for (int i = 0; i < this.getLines().size(); i++) {
      this.getLines().get(i).setLineNumber(i + 1);
    }
  }

  public BDeposit getFilterData() {
    Iterator<BDepositLine> it = lines.iterator();
    while (it.hasNext()) {
      BDepositLine line = it.next();
      if (line.getSubject() == null || line.getSubject().getUuid() == null)
        it.remove();
    }
    refreshLineNumber();
    return this;
  }

  public BigDecimal caculateTotal() {
    BigDecimal temp = BigDecimal.ZERO;
    for (BDepositLine line : lines) {
      if (line.getTotal() != null)
        temp = temp.add(line.getTotal());
    }
    return temp;
  }

  public String toFriendlyStr() {
    return getBillNumber();
  }

  @Override
  public String toString() {
    return toFriendlyStr();
  }

  @Override
  public HasUCN getStore() {
    return accountUnit;
  }

  @Override
  public List<Message> getMessages() {
    if (messages == null) {
      return new ArrayList<Message>();
    }
    return messages;
  }

  @Override
  public void addMessage(Message message) {
    if (message == null) {
      return;
    }

    if (messages == null) {
      messages = new ArrayList<Message>();
    }

    messages.add(message);
  }

  @Override
  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  @Override
  public boolean isValid() {
    if (messages == null || messages.isEmpty()) {
      return true;
    }

    for (Message message : messages) {
      if (message.isValid() == false) {
        return false;
      }
    }
    return true;
  }

}
