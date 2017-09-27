/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BRecInvoiceReg.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-27 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.client.biz;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.gwt.acc.client.BAccountId;
import com.hd123.m3.account.gwt.commons.client.biz.BAccStandardBill;
import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.commons.gwt.base.client.biz.HasStore;
import com.hd123.rumba.commons.gwt.entity.client.HasUCN;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;

/**
 * 发票登记单|B对象
 * 
 * @author chenpeisi
 * 
 */
public class BInvoiceReg extends BAccStandardBill implements HasStore {
  private static final long serialVersionUID = 300200L;
  public static final int SCALE_MONEY = 2;
  public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

  private BUCN accountUnit;
  private BCounterpart counterpart;
  private int direction;
  private Date regDate;
  private String invoiceCode;
  private BTotal invoiceTotal = new BTotal();
  private BTotal accountTotal = new BTotal();
  private BigDecimal totalDiff;
  private BigDecimal taxDiff;

  private List<BInvoiceRegLine> lines = new ArrayList<BInvoiceRegLine>();
  private List<BInvoiceRegInvoice> invoices = new ArrayList<BInvoiceRegInvoice>();

  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountCenter) {
    this.accountUnit = accountCenter;
  }

  public BCounterpart getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BCounterpart counterpart) {
    this.counterpart = counterpart;
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public Date getRegDate() {
    return regDate;
  }

  public void setRegDate(Date regDate) {
    this.regDate = regDate;
  }

  public String getInvoiceCode() {
    return invoiceCode;
  }

  public void setInvoiceCode(String invoiceCode) {
    this.invoiceCode = invoiceCode;
  }

  public BTotal getInvoiceTotal() {
    return invoiceTotal;
  }

  public void setInvoiceTotal(BTotal invoiceTotal) {
    this.invoiceTotal = invoiceTotal;
  }

  public BTotal getAccountTotal() {
    return accountTotal;
  }

  public void setAccountTotal(BTotal accountTotal) {
    this.accountTotal = accountTotal;
  }

  public BigDecimal getTotalDiff() {
    return totalDiff;
  }

  public void setTotalDiff(BigDecimal totalDiff) {
    this.totalDiff = totalDiff;
  }

  public BigDecimal getTaxDiff() {
    return taxDiff;
  }

  public void setTaxDiff(BigDecimal taxDiff) {
    this.taxDiff = taxDiff;
  }

  public List<BInvoiceRegLine> getLines() {
    return lines;
  }

  public void setLines(List<BInvoiceRegLine> lines) {
    this.lines = lines;
  }

  public List<BInvoiceRegInvoice> getInvoices() {
    return invoices;
  }

  public void setInvoices(List<BInvoiceRegInvoice> invoices) {
    this.invoices = invoices;
  }

  public List<BAccountId> getAccountIds() {
    if (lines == null || lines.isEmpty())
      return Collections.emptyList();

    List<BAccountId> accountIds = new ArrayList<BAccountId>();
    for (BInvoiceRegLine line : lines) {
      if (line.getAcc1() == null || line.getAcc1().getId() == null || line.getAcc2() == null
          || line.getAcc2().getId() == null)
        continue;

      BAccountId accountId = new BAccountId();
      accountId.setAccId(line.getAcc1().getId());
      ArrayList<String> list = new ArrayList<String>();
      list.add(line.getAcc2().getStatement() == null ? BBill.NONE_ID : line.getAcc2()
          .getStatement().id());
      list.add(line.getAcc2().getPayment() == null ? BBill.NONE_ID : line.getAcc2().getPayment()
          .id());
      accountId.setBizId(CollectionUtil.toString(list));
      accountIds.add(accountId);
    }
    return accountIds;
  }

  /** 计算发票金额 */
  public void caculateInvoiceTotal() {
    if (invoices == null || invoices.isEmpty())
      invoiceTotal = BTotal.zero();
    BigDecimal totalTemp = BigDecimal.ZERO;
    BigDecimal taxTemp = BigDecimal.ZERO;
    for (BInvoiceRegInvoice invoice : invoices) {
      totalTemp = totalTemp.add(invoice.getTotal().getTotal().setScale(SCALE_MONEY, ROUNDING_MODE));
      taxTemp = taxTemp.add(invoice.getTotal().getTax().setScale(SCALE_MONEY, ROUNDING_MODE));
    }
    invoiceTotal = new BTotal(totalTemp, taxTemp);
    setTotalDiff(invoiceTotal.getTotal().setScale(SCALE_MONEY, ROUNDING_MODE)
        .subtract(accountTotal.getTotal().setScale(SCALE_MONEY, ROUNDING_MODE)));
    setTaxDiff(invoiceTotal.getTax().setScale(SCALE_MONEY, ROUNDING_MODE)
        .subtract(accountTotal.getTax().setScale(SCALE_MONEY, ROUNDING_MODE)));
  }

  /** 计算账款金额 */
  public void caculateAccountTotal() {
    if (lines == null || lines.isEmpty())
      accountTotal = BTotal.zero();
    BigDecimal totalTemp = BigDecimal.ZERO;
    BigDecimal taxTemp = BigDecimal.ZERO;
    for (BInvoiceRegLine line : lines) {
      totalTemp = totalTemp.add(line.getRegTotal().getTotal().setScale(SCALE_MONEY, ROUNDING_MODE));
      taxTemp = taxTemp.add(line.getRegTotal().getTax().setScale(SCALE_MONEY, ROUNDING_MODE));
    }
    accountTotal = new BTotal(totalTemp, taxTemp);
    setTotalDiff(invoiceTotal.getTotal().setScale(SCALE_MONEY, ROUNDING_MODE)
        .subtract(accountTotal.getTotal().setScale(SCALE_MONEY, ROUNDING_MODE)));
    setTaxDiff(invoiceTotal.getTax().setScale(SCALE_MONEY, ROUNDING_MODE)
        .subtract(accountTotal.getTax().setScale(SCALE_MONEY, ROUNDING_MODE)));
  }

  /** 判断发票登记单是否有有效的账款单据明细 */
  public boolean hasValidateLine() {
    for (BInvoiceRegLine line : lines) {
      if (line.getAcc1().getSubject() != null) {
        return true;
      }
    }
    return false;
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
}
