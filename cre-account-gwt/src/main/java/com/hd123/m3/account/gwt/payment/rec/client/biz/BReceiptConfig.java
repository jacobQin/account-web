/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BReceiptConfig.java
 * 模块说明：	
 * 修改历史：
 * 2015-9-6 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.biz;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 收款单配置
 * 
 * @author liuguilin
 * 
 */
public class BReceiptConfig implements Serializable {

  private static final long serialVersionUID = 8524355017503723726L;

  private boolean showTax;
  private BUCN receiptPaymentType;
  private BigDecimal receiptPaymentTypeLimit = BigDecimal.ZERO;
  private boolean bankRequired;
  private boolean accObjectEnabled;
  private boolean receiptEqualsUnpayed;
  private String defalutReceiptPaymentType;

  public boolean isShowTax() {
    return showTax;
  }

  public void setShowTax(boolean showTax) {
    this.showTax = showTax;
  }

  /** 长短款付款类型 */
  public BUCN getReceiptPaymentType() {
    return receiptPaymentType;
  }

  public void setReceiptPaymentType(BUCN receiptPaymentType) {
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

  /** 是否开启核算主体 */
  public boolean isAccObjectEnabled() {
    return accObjectEnabled;
  }

  public void setAccObjectEnabled(boolean accObjectEnabled) {
    this.accObjectEnabled = accObjectEnabled;
  }

  /** 收款金额默认等于应收金额 */
  public boolean isReceiptEqualsUnpayed() {
    return receiptEqualsUnpayed;
  }

  public void setReceiptEqualsUnpayed(boolean receiptEqualsUnpayed) {
    this.receiptEqualsUnpayed = receiptEqualsUnpayed;
  }

  /** 收款单默认收款方式 */
  public String getDefalutReceiptPaymentType() {
    return defalutReceiptPaymentType;
  }

  public void setDefalutReceiptPaymentType(String defalutReceiptPaymentType) {
    this.defalutReceiptPaymentType = defalutReceiptPaymentType;
  }

}
