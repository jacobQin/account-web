/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	ReceiptConfig.java
 * 模块说明：	
 * 修改历史：
 * 2016年10月2日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.payment.receipt.common;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.m3.account.service.paymenttype.PaymentType;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 收款单配置项
 * 
 * @author LiBin
 *
 */
public class ReceiptOption implements Serializable {

  private static final long serialVersionUID = -2543159587295014480L;

  private PaymentType defaultPaymentType;
  private UCN receiptPaymentType;
  private BigDecimal receiptPaymentTypeLimit = BigDecimal.ZERO;
  private boolean bankRequired;
  private String defrayalType;
  private UCN depositSubject;
  private boolean receiptEqualsUnpayed;
  private boolean accObjectEnabled;
  
  /**默认财务付款方式*/
  public PaymentType getDefaultPaymentType() {
    return defaultPaymentType;
  }
  
  public void setDefaultPaymentType(PaymentType defaultPaymentType) {
    this.defaultPaymentType = defaultPaymentType;
  }

  /** 长短款付款方式 */
  public UCN getReceiptPaymentType() {
    return receiptPaymentType;
  }

  public void setReceiptPaymentType(UCN receiptPaymentType) {
    this.receiptPaymentType = receiptPaymentType;
  }

  /** 长短款限额 */
  public BigDecimal getReceiptPaymentTypeLimit() {
    return receiptPaymentTypeLimit;
  }

  public void setReceiptPaymentTypeLimit(BigDecimal receiptPaymentTypeLimit) {
    this.receiptPaymentTypeLimit = receiptPaymentTypeLimit;
  }

  /** 收款明细银行是否必填 */
  public boolean isBankRequired() {
    return bankRequired;
  }

  public void setBankRequired(boolean bankRequired) {
    this.bankRequired = bankRequired;
  }

  /** 收款单默认收款方式 */
  public String getDefrayalType() {
    return defrayalType;
  }
  
  public void setDefrayalType(String defrayalType) {
    this.defrayalType = defrayalType;
  }
  
  /** 默认预存款科目 */
  public UCN getDepositSubject() {
    return depositSubject;
  }

  public void setDepositSubject(UCN depositSubject) {
    this.depositSubject = depositSubject;
  }
  
  /** 收款金额默认等于应收金额 */
  public boolean isReceiptEqualsUnpayed() {
    return receiptEqualsUnpayed;
  }

  public void setReceiptEqualsUnpayed(boolean receiptEqualsUnpayed) {
    this.receiptEqualsUnpayed = receiptEqualsUnpayed;
  }
  
  /** 是否开启核算主体 */
  public boolean isAccObjectEnabled() {
    return accObjectEnabled;
  }

  public void setAccObjectEnabled(boolean accObjectEnabled) {
    this.accObjectEnabled = accObjectEnabled;
  }

}
