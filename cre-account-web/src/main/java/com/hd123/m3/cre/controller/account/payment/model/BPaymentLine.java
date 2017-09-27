package com.hd123.m3.cre.controller.account.payment.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.commons.Total;
import com.hd123.rumba.commons.biz.entity.Entity;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 收款单明细行
 * 
 * @author LiBin
 * 
 */
public class BPaymentLine extends Entity {
  private static final long serialVersionUID = 6674598084238556416L;

  private int lineNumber;
  private Total unpayedTotal = Total.zero();
  private Total total = Total.zero();
  private BigDecimal defrayalTotal = BigDecimal.ZERO;
  private BigDecimal depositTotal = BigDecimal.ZERO;
  private UCN depositSubject;
  private String remark;

  private String invoiceCodeStr;
  private String invoiceNumberStr;

  /** 付款方式明细行 */
  private List<BPaymentLineCash> cashes = new ArrayList<BPaymentLineCash>();
  /** 预存款冲扣明细行 */
  private List<BPaymentLineDeposit> deposits = new ArrayList<BPaymentLineDeposit>();
  /** 抵扣明细行 */
  private List<BPaymentLineDeduction> deductions = new ArrayList<BPaymentLineDeduction>();

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public Total getUnpayedTotal() {
    return unpayedTotal;
  }

  public void setUnpayedTotal(Total unpayedTotal) {
    this.unpayedTotal = unpayedTotal;
  }

  public Total getTotal() {
    return total;
  }

  public void setTotal(Total total) {
    this.total = total;
  }

  public BigDecimal getDefrayalTotal() {
    return defrayalTotal;
  }

  public void setDefrayalTotal(BigDecimal defrayalTotal) {
    this.defrayalTotal = defrayalTotal;
  }

  public BigDecimal getDepositTotal() {
    return depositTotal;
  }

  public void setDepositTotal(BigDecimal depositTotal) {
    this.depositTotal = depositTotal;
  }

  /** 预存款科目 */
  public UCN getDepositSubject() {
    return depositSubject;
  }

  public void setDepositSubject(UCN depositSubject) {
    this.depositSubject = depositSubject;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  /** 发票代码拼接字符串，用于查看页面显示 */
  public String getInvoiceCodeStr() {
    return invoiceCodeStr;
  }

  public void setInvoiceCodeStr(String invoiceCodeStr) {
    this.invoiceCodeStr = invoiceCodeStr;
  }

  /** 发票号码拼接字符串，用于查看页面显示 */
  public String getInvoiceNumberStr() {
    return invoiceNumberStr;
  }

  public void setInvoiceNumberStr(String invoiceNumberStr) {
    this.invoiceNumberStr = invoiceNumberStr;
  }

  public List<BPaymentLineCash> getCashes() {
    return cashes;
  }

  public void setCashes(List<BPaymentLineCash> cashes) {
    this.cashes = cashes;
  }

  public List<BPaymentLineDeposit> getDeposits() {
    return deposits;
  }

  public void setDeposits(List<BPaymentLineDeposit> deposits) {
    this.deposits = deposits;
  }

  public List<BPaymentLineDeduction> getDeductions() {
    return deductions;
  }

  public void setDeductions(List<BPaymentLineDeduction> deductions) {
    this.deductions = deductions;
  }

  /** 获取计算为收付金额=本次应收付金额-账款是收付金额 */
  public Total getLeftTotal() {
    if (unpayedTotal == null || total == null)
      return Total.zero();
    return unpayedTotal.subtract(total);
  }
}
