/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BPaymentLine.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.rumba.commons.gwt.entity.client.BEntity;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 收款单明细行
 * 
 * @author subinzhu
 * 
 */
public class BPaymentLine extends BEntity {
  private static final long serialVersionUID = 6674598084238556416L;

  private int lineNumber;
  private BTotal unpayedTotal = BTotal.zero();
  private BTotal total = BTotal.zero();
  private BigDecimal defrayalTotal = BigDecimal.ZERO;
  private BigDecimal depositTotal = BigDecimal.ZERO;
  private BUCN depositSubject;
  private String remark;

  private String invoiceCodeStr;
  private String invoiceNumberStr;

  /** 付款方式明细行 */
  private List<BPaymentLineCash> cashs = new ArrayList<BPaymentLineCash>();
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

  public BTotal getUnpayedTotal() {
    return unpayedTotal;
  }

  public void setUnpayedTotal(BTotal unpayedTotal) {
    this.unpayedTotal = unpayedTotal;
  }

  public BTotal getTotal() {
    return total;
  }

  public void setTotal(BTotal total) {
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
  public BUCN getDepositSubject() {
    return depositSubject;
  }

  public void setDepositSubject(BUCN depositSubject) {
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

  public List<BPaymentLineCash> getCashs() {
    return cashs;
  }

  public void setCashs(List<BPaymentLineCash> cashs) {
    this.cashs = cashs;
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
  public BTotal getLeftTotal() {
    if (unpayedTotal == null || total == null)
      return BTotal.zero();
    return unpayedTotal.subtract(total);
  }
}
