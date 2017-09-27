/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BInvoiceRegLine.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-27 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.client.biz;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.hd123.m3.account.gwt.acc.client.BAcc1;
import com.hd123.m3.account.gwt.acc.client.BAcc2;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxCalculator;
import com.hd123.rumba.commons.gwt.entity.client.BEntity;

/**
 * 发票登记单明细行
 * 
 * @author chenpeisi
 * 
 */
public class BInvoiceRegLine extends BEntity {
  private static final long serialVersionUID = 300200L;

  public static final String FN_ACCOUNTTOTAL = "accountTotal";
  public static final String FN_ACCOUNTTAX = "accountTax";

  private int lineNumber;
  private BAcc1 acc1 = new BAcc1();
  private BAcc2 acc2 = new BAcc2();

  private BTotal unregTotal = BTotal.zero();
  private BTotal regTotal = BTotal.zero();
  private BigDecimal amount = BigDecimal.ZERO;
  private String remark;

  private boolean isNewAdded;

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public BAcc1 getAcc1() {
    return acc1;
  }

  public void setAcc1(BAcc1 acc1) {
    this.acc1 = acc1;
  }

  public BAcc2 getAcc2() {
    return acc2;
  }

  public void setAcc2(BAcc2 acc2) {
    this.acc2 = acc2;
  }

  public BTotal getUnregTotal() {
    return unregTotal;
  }

  public void setUnregTotal(BTotal unregTotal) {
    this.unregTotal = unregTotal;
  }

  public BTotal getRegTotal() {
    return regTotal;
  }

  public void setRegTotal(BTotal regTotal) {
    this.regTotal = regTotal;
  }

  public BigDecimal getAmount() {
    if (BigDecimal.ZERO.compareTo(amount) == 0)
      amount = regTotal.getTotal().subtract(regTotal.getTax()).setScale(2, RoundingMode.HALF_UP);
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void changeTotal(BigDecimal total, int scale, RoundingMode roundingMode) {
    if (total == null)
      return;
    if (acc1.getTaxRate() == null)
      return;

    BTotal t = new BTotal(total, BTaxCalculator.tax(total, acc1.getTaxRate(), scale, roundingMode));

    setRegTotal(t);

    BigDecimal amountLocal = t.getTotal().subtract(t.getTax()).setScale(2, RoundingMode.HALF_UP);
    setAmount(amountLocal);
  }

  public void changeTax(BigDecimal tax) {
    if (tax == null)
      return;
    if (regTotal == null || regTotal.getTotal() == null)
      setRegTotal(BTotal.zero());
    regTotal.setTax(tax);

    BigDecimal amountLocal = regTotal.getTotal().subtract(tax).setScale(2, RoundingMode.HALF_UP);
    setAmount(amountLocal);
  }

  public void changeAmount(BigDecimal amount, int scale, RoundingMode roundingMode) {
    if (amount == null)
      return;
    setAmount(amount);

    if (regTotal == null || regTotal.getTotal() == null)
      setRegTotal(BTotal.zero());

    BigDecimal tax = regTotal.getTotal().subtract(amount).setScale(scale, roundingMode);
    regTotal.setTax(tax);
  }

  public boolean isNewAdded() {
    return isNewAdded;
  }

  public void setNewAdded(boolean isNewAdded) {
    this.isNewAdded = isNewAdded;
  }
}
