/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BInvoiceRegInvoice.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-27 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.client.biz;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxCalculator;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxRate;
import com.hd123.rumba.commons.gwt.entity.client.BEntity;

/**
 * 发票登记单发票明细行
 * 
 * @author chenpeisi
 * 
 */
public class BInvoiceRegInvoice extends BEntity {
  private static final long serialVersionUID = 300200L;
  public static final int SCALE_MONEY = 2;
  public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

  public static final String PN_INVOICENUMBER = "invoiceNumber";
  public static final String PN_INVOICEDATE = "invoiceDate";
  public static final String PN_INVOICETOTAL = "invoiceTotal";
  public static final String PN_INVOICETAX = "invoiceTax";

  private int lineNumber;
  private String invoiceType;
  private String invoiceNumber;
  private Date invoiceDate;
  private String remark;

  private BTaxRate taxRate = new BTaxRate();
  private BTotal total = BTotal.zero();
  private BigDecimal amount = BigDecimal.ZERO;

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public String getInvoiceType() {
    return invoiceType;
  }

  public void setInvoiceType(String invoiceType) {
    this.invoiceType = invoiceType;
  }

  public String getInvoiceNumber() {
    return invoiceNumber;
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public Date getInvoiceDate() {
    return invoiceDate;
  }

  public void setInvoiceDate(Date invoiceDate) {
    this.invoiceDate = invoiceDate;
  }

  public BTaxRate getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(BTaxRate taxRate) {
    this.taxRate = taxRate;
  }

  public BTotal getTotal() {
    return total;
  }

  public void setTotal(BTotal total) {
    this.total = total;
  }

  /** 去税金额 */
  public BigDecimal getAmount() {
    if (BigDecimal.ZERO.compareTo(amount) == 0)
      amount = total.getTotal().subtract(total.getTax()).setScale(SCALE_MONEY, ROUNDING_MODE);
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

  public void changetaxRate(BTaxRate taxRate, int scale, RoundingMode roundingMode) {
    if (taxRate == null)
      return;
    setTaxRate(taxRate);

    if (total == null || total.getTotal() == null)
      setTotal(BTotal.zero());

    BTotal t = new BTotal(total.getTotal(), BTaxCalculator.tax(getTotal().getTotal(), getTaxRate(),
        scale, roundingMode));
    setTotal(t);

    BigDecimal amountLocal = t.getTotal().subtract(t.getTax()).setScale(SCALE_MONEY, ROUNDING_MODE);
    setAmount(amountLocal);
  }

  public void changeTotal(BigDecimal total, int scale, RoundingMode roundingMode) {
    if (total == null)
      return;
    if (taxRate == null)
      return;

    BTotal t = new BTotal(total, BTaxCalculator.tax(total, taxRate, SCALE_MONEY, ROUNDING_MODE));
    setTotal(t);

    BigDecimal amountLocal = t.getTotal().subtract(t.getTax()).setScale(SCALE_MONEY, ROUNDING_MODE);
    setAmount(amountLocal);
  }

  public void changeTax(BigDecimal tax) {
    if (tax == null)
      return;
    if (total == null || total.getTotal() == null)
      setTotal(BTotal.zero());
    total.setTax(tax.setScale(SCALE_MONEY, ROUNDING_MODE));

    BigDecimal amountLocal = total.getTotal().subtract(tax).setScale(SCALE_MONEY, ROUNDING_MODE);
    setAmount(amountLocal);
  }

  public void changeAmount(BigDecimal amount) {
    if (amount == null)
      return;
    setAmount(amount.setScale(SCALE_MONEY, ROUNDING_MODE));

    if (total == null || total.getTotal() == null)
      setTotal(BTotal.zero());

    BigDecimal tax = total.getTotal().subtract(amount).setScale(SCALE_MONEY, ROUNDING_MODE);
    total.setTax(tax);
  }
}
