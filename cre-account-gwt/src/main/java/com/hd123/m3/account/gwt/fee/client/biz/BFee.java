/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： BFee.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-16 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.gwt.commons.client.biz.BAccStandardBill;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.commons.gwt.base.client.biz.HasStore;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.commons.gwt.entity.client.HasUCN;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.RDateUtil;

/**
 * 费用单
 * 
 * @author subinzhu
 * 
 */
public class BFee extends BAccStandardBill implements HasStore {
  private static final long serialVersionUID = 300200L;

  private BContract contract;
  private BUCN accountUnit;
  private BCounterpart counterpart;
  private Date accountDate;
  private BDateRange dateRange = new BDateRange();
  private int direction = DirectionType.receipt.getDirectionValue();
  private BTotal total = BTotal.zero();
  private String dealer;
  private boolean generateStatement = Boolean.TRUE;
  private boolean hasAccounts = false;

  private List<BFeeLine> lines = new ArrayList<BFeeLine>();

  public BContract getContract() {
    return contract;
  }

  public void setContract(BContract contract) {
    this.contract = contract;
  }

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

  public Date getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(Date accountDate) {
    this.accountDate = accountDate;
  }

  public BDateRange getDateRange() {
    return dateRange;
  }

  public void setDateRange(BDateRange dateRange) {
    this.dateRange = dateRange;
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public BTotal getTotal() {
    return total;
  }

  public void setTotal(BTotal total) {
    this.total = total;
  }

  public String getDealer() {
    return dealer;
  }

  public void setDealer(String dealer) {
    this.dealer = dealer;
  }

  public boolean isGenerateStatement() {
    return generateStatement;
  }

  public void setGenerateStatement(boolean generateStatement) {
    this.generateStatement = generateStatement;
  }

  public boolean isHasAccounts() {
    return hasAccounts;
  }

  public void setHasAccounts(boolean hasAccounts) {
    this.hasAccounts = hasAccounts;
  }

  public List<BFeeLine> getLines() {
    return lines;
  }

  public void setLines(List<BFeeLine> lines) {
    this.lines = lines;
  }

  /** 判断费用单是否有有效行 */
  public boolean hasValidLine() {
    for (BFeeLine line : getLines()) {
      if (line.getSubject() != null)
        return true;
    }
    return false;
  }

  /** 合计金额/税额。 */
  public void aggregate() {
    BigDecimal localTotal = BigDecimal.ZERO;
    BigDecimal tax = BigDecimal.ZERO;

    for (BFeeLine line : getLines()) {
      localTotal = localTotal.add(line.getTotal().getValue());
      tax = tax.add(line.getTax());
    }
    this.getTotal().setTotal(localTotal);
    this.getTotal().setTax(tax);
  }

  public void initialize() {
    for (BFeeLine line : lines) {
      line.getTotal().setNumberValue(line.getTotal().getValue(), M3Format.fmt_money);
    }
  }

  @Override
  public String toString() {
    return this.getBillNumber();
  }

  @Override
  public HasUCN getStore() {
    return accountUnit;
  }

  public Date addMonth(Date source, int count) {
    if (source == null || count < 0)
      return null;
    int day = RDateUtil.get(source, RDateUtil.DAY_OF_MONTH);
    RDateUtil.set(source, RDateUtil.DAY_OF_MONTH, 1);
    source = RDateUtil.addMonth(source, count);

    Date maxDate = RDateUtil.getLastDayOfMonth(source);
    int maxDay = RDateUtil.get(maxDate, RDateUtil.DAY_OF_MONTH);
    if (day <= maxDay) {
      RDateUtil.set(source, RDateUtil.DAY_OF_MONTH, day);
      return source;
    } else {
      return maxDate;
    }
  }
}
