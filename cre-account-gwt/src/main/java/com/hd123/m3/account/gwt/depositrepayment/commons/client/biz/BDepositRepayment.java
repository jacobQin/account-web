/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BDepositRepayment.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-19 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.commons.client.biz;

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

/**
 * @author zhuhairui
 * 
 */
public class BDepositRepayment extends BAccStandardBill implements HasStore {

  private static final long serialVersionUID = 6102211192855524815L;

  private BUCN accountUnit;
  private int direction;
  private String bizState;
  private BCounterpart counterpart;
  private BUCN contract;
  private BigDecimal repaymentTotal;
  private BBank bank;
  private BUCN paymentType;
  private Date repaymentDate;
  private Date accountDate;
  private BUCN dealer;
  private String counterContact;
  private String settleNo;
  private String remark;

  private BUCN deposit;

  List<BDepositRepaymentLine> lines = new ArrayList<BDepositRepaymentLine>();

  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public String getBizState() {
    return bizState;
  }

  public void setBizState(String state) {
    this.bizState = state;
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

  public BigDecimal getRepaymentTotal() {
    return repaymentTotal;
  }

  public void setRepaymentTotal(BigDecimal repaymentTotal) {
    this.repaymentTotal = repaymentTotal;
  }

  public BBank getBank() {
    return bank;
  }

  public void setBank(BBank bank) {
    this.bank = bank;
  }

  public BUCN getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(BUCN paymentType) {
    this.paymentType = paymentType;
  }

  public Date getRepaymentDate() {
    return repaymentDate;
  }

  public void setRepaymentDate(Date repaymentDate) {
    this.repaymentDate = repaymentDate;
  }

  public Date getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(Date accountDate) {
    this.accountDate = accountDate;
  }

  public BUCN getDealer() {
    return dealer;
  }

  public void setDealer(BUCN dealer) {
    this.dealer = dealer;
  }

  public String getCounterContact() {
    return counterContact;
  }

  public void setCounterContact(String counterContact) {
    this.counterContact = counterContact;
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

  public BUCN getDeposit() {
    return deposit;
  }

  public void setDeposit(BUCN deposit) {
    this.deposit = deposit;
  }

  public List<BDepositRepaymentLine> getLines() {
    return lines;
  }

  public void setLines(List<BDepositRepaymentLine> lines) {
    this.lines = lines;
  }

  public String toFriendlyStr() {
    return getBillNumber();
  }

  @Override
  public String toString() {
    return toFriendlyStr();
  }

  public BigDecimal caculateTotal() {
    BigDecimal temp = BigDecimal.ZERO;
    for (BDepositRepaymentLine line : lines) {
      if (line.getAmount() != null)
        temp = temp.add(line.getAmount());
    }
    return temp;
  }

  public boolean hasValues() {
    for (BDepositRepaymentLine line : lines) {
      if (line.getSubject() != null && line.getSubject().getUuid() != null) {
        return true;
      }
    }
    return false;
  }

  @Override
  public HasUCN getStore() {
    return accountUnit;
  }

  private void refreshLineNumber() {
    for (int i = 0; i < this.getLines().size(); i++) {
      this.getLines().get(i).setLineNumber(i + 1);
    }
  }

  public BDepositRepayment getFilterData() {
    Iterator<BDepositRepaymentLine> it = lines.iterator();
    while (it.hasNext()) {
      BDepositRepaymentLine line = it.next();
      if (line.getSubject() == null || line.getSubject().getUuid() == null)
        it.remove();
    }
    refreshLineNumber();
    return this;
  }
}
