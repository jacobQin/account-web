/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	BConfigOption.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.commons.client.biz;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.m3.account.gwt.option.client.PaymentDefrayalType;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author zhuhairui
 * 
 */
public class BConfigOption implements Serializable {

  private static final long serialVersionUID = 7404456169397593514L;
  /** 财务月天数 */
  private BigDecimal monthDays = BigDecimal.ZERO;
  /** 非整月月末按实际天数计算 */
  private boolean incompleteMonthByRealDays = false;
  /** 计算精度 */
  private int scale = 2;
  /** 舍入算法 */
  private String roundingMode = RoundingMode.HALF_UP.toString();
  /** 账单默认流程Key */
  private String statementDefaultBPMKey;
  /** 账单默认流程 */
  private String statementDefaultBPM;
  /** 收付款通知单默认流程Key */
  private String paymentNoticeDefaultBPMKey;
  /** 收付款通知单默认流程 */
  private String paymentNoticeDefaultBPM;
  /** 账单结转期规则日 */
  private Integer settleNoRuleDay;
  /** 已终止合同是否产生0账单 */
  private boolean genZeroStatement = false;
  /** 收款单账款产生滞纳金默认值 */
  private BReceiptOverdueDefault receiptOverdueDefaults = BReceiptOverdueDefault.calcValue;
  /** 长短款付款方式 配置项 */
  private BUCN receiptPaymentType;
  /** 长短款付款方式限额值配置项 */
  private BigDecimal receiptPaymentTypeLimits = BigDecimal.ZERO;
  /** 收款单收款明细银行是否必填 */
  private boolean receiptBankRequired = false;
  /** 是否启用外部发票系统 */
  private boolean extinvSystemRequired = false;
  /** 是否启用核算主体 */
  private boolean accObjectEnbaled = false;
  /** 是否启用保证金按单管理 */
  private boolean byDepositEnabled = false;
  /** 收款单默认收款方式 */
  private String defalutReceiptPaymentType;
  /** 返款单是否按笔返款 */
  private boolean rebateByBill = false;
  /** 收款金额默认等于应收金额 */
  private boolean receiptEqualsUnpayed = false;
  /** 收款发票登记单默认发票 */
  private String defalutInvoiceType;

  public BigDecimal getMonthDays() {
    return monthDays;
  }

  public void setMonthDays(BigDecimal monthDays) {
    if (monthDays != null)
      monthDays.setScale(6);

    this.monthDays = monthDays;
  }

  public boolean isIncompleteMonthByRealDays() {
    return incompleteMonthByRealDays;
  }

  public void setIncompleteMonthByRealDays(boolean incompleteMonthByRealDays) {
    this.incompleteMonthByRealDays = incompleteMonthByRealDays;
  }

  public int getScale() {
    return scale;
  }

  public void setScale(int scale) {
    this.scale = scale;
  }

  public String getRoundingMode() {
    return roundingMode;
  }

  public void setRoundingMode(String roundingMode) {
    this.roundingMode = roundingMode;
  }

  public String getStatementDefaultBPMKey() {
    return statementDefaultBPMKey;
  }

  public void setStatementDefaultBPMKey(String statementDefaultBPM) {
    this.statementDefaultBPMKey = statementDefaultBPM;
  }

  public String getStatementDefaultBPM() {
    return statementDefaultBPM;
  }

  public void setStatementDefaultBPM(String statementDefaultBPM) {
    this.statementDefaultBPM = statementDefaultBPM;
  }

  public String getPaymentNoticeDefaultBPMKey() {
    return paymentNoticeDefaultBPMKey;
  }

  public void setPaymentNoticeDefaultBPMKey(String paymentNoticeDefaultBPM) {
    this.paymentNoticeDefaultBPMKey = paymentNoticeDefaultBPM;
  }

  public Integer getSettleNoRuleDay() {
    return settleNoRuleDay;
  }

  public void setSettleNoRuleDay(Integer settleNoRuleDay) {
    this.settleNoRuleDay = settleNoRuleDay;
  }

  public String getPaymentNoticeDefaultBPM() {
    return paymentNoticeDefaultBPM;
  }

  public void setPaymentNoticeDefaultBPM(String paymentNoticeDefaultBPM) {
    this.paymentNoticeDefaultBPM = paymentNoticeDefaultBPM;
  }

  public boolean isGenZeroStatement() {
    return genZeroStatement;
  }

  public void setGenZeroStatement(boolean genZeroStatement) {
    this.genZeroStatement = genZeroStatement;
  }

  public BReceiptOverdueDefault getReceiptOverdueDefaults() {
    return receiptOverdueDefaults;
  }

  public void setReceiptOverdueDefaults(BReceiptOverdueDefault receiptOverdueDefaults) {
    this.receiptOverdueDefaults = receiptOverdueDefaults;
  }

  public BUCN getReceiptPaymentType() {
    return receiptPaymentType;
  }

  public void setReceiptPaymentType(BUCN receiptPaymentType) {
    this.receiptPaymentType = receiptPaymentType;
  }

  public BigDecimal getReceiptPaymentTypeLimits() {
    return receiptPaymentTypeLimits;
  }

  public void setReceiptPaymentTypeLimits(BigDecimal receiptPaymentTypeLimits) {
    this.receiptPaymentTypeLimits = receiptPaymentTypeLimits;
  }

  public boolean isReceiptBankRequired() {
    return receiptBankRequired;
  }

  public void setReceiptBankRequired(boolean receiptBankRequired) {
    this.receiptBankRequired = receiptBankRequired;
  }

  public boolean isExtinvSystemRequired() {
    return extinvSystemRequired;
  }

  public void setExtinvSystemRequired(boolean extinvSystemRequired) {
    this.extinvSystemRequired = extinvSystemRequired;
  }

  public boolean isAccObjectEnbaled() {
    return accObjectEnbaled;
  }

  public void setAccObjectEnbaled(boolean accObjectEnbaled) {
    this.accObjectEnbaled = accObjectEnbaled;
  }

  public boolean isByDepositEnabled() {
    return byDepositEnabled;
  }

  public void setByDepositEnabled(boolean byDepositEnabled) {
    this.byDepositEnabled = byDepositEnabled;
  }

  public String getDefalutReceiptPaymentType() {
    return defalutReceiptPaymentType;
  }

  public void setDefalutReceiptPaymentType(String defalutReceiptPaymentType) {
    if (defalutReceiptPaymentType==null || PaymentDefrayalType.bill.name().equals(defalutReceiptPaymentType)) {
      this.defalutReceiptPaymentType = defalutReceiptPaymentType;
    } else {
      this.defalutReceiptPaymentType = PaymentDefrayalType.lineSingle.name();
    }
  }

  public boolean isRebateByBill() {
    return rebateByBill;
  }

  public void setRebateByBill(boolean rebateByBill) {
    this.rebateByBill = rebateByBill;
  }

  public boolean isReceiptEqualsUnpayed() {
    return receiptEqualsUnpayed;
  }

  public void setReceiptEqualsUnpayed(boolean receiptEqualsUnpayed) {
    this.receiptEqualsUnpayed = receiptEqualsUnpayed;
  }

  public String getDefalutInvoiceType() {
    return defalutInvoiceType;
  }

  public void setDefalutInvoiceType(String defalutInvoiceType) {
    this.defalutInvoiceType = defalutInvoiceType;
  }

}
