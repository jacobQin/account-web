/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BPaymentCollectionLine.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月9日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.payment.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.service.bank.Bank;
import com.hd123.m3.commons.biz.tax.TaxRate;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 收款单代收明细
 * 
 * @author LiBin
 * 
 */
public class BPaymentCollectionLine extends BPaymentLine {

  private static final long serialVersionUID = 2065885918596584178L;

  private UCN subject;
  private UCN contract;
  private TaxRate rate;
  private Date beginDate;
  private Date endDate;
  private String ivcType;
  private String ivcCode;
  private String ivcNumber;
  private String accountId;

  /** 科目 */
  public UCN getSubject() {
    return subject;
  }

  public void setSubject(UCN subject) {
    this.subject = subject;
  }

  /** 合同 */
  public UCN getContract() {
    return contract;
  }

  public void setContract(UCN contract) {
    this.contract = contract;
  }

  /** 科目税率 */
  public TaxRate getRate() {
    return rate;
  }

  public void setRate(TaxRate rate) {
    this.rate = rate;
  }

  /** 起始日期 */
  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  /** 截止日期 */
  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  /** 发票类型 */
  public String getIvcType() {
    return ivcType;
  }

  public void setIvcType(String ivcType) {
    this.ivcType = ivcType;
  }

  /** 发票代码 */
  public String getIvcCode() {
    return ivcCode;
  }

  public void setIvcCode(String ivcCode) {
    this.ivcCode = ivcCode;
  }

  /** 发票号码 */
  public String getIvcNumber() {
    return ivcNumber;
  }

  public void setIvcNumber(String ivcNumber) {
    this.ivcNumber = ivcNumber;
  }

  /** 账款Id，只读 */
  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public BigDecimal getUnReceivableTotal() {

    BigDecimal unReceivableTotal = BigDecimal.ZERO;

    if (getUnpayedTotal() != null && getUnpayedTotal().getTotal() != null) {
      unReceivableTotal = unReceivableTotal.add(getUnpayedTotal().getTotal());
    }

    if (getTotal() != null && getTotal().getTotal() != null) {
      unReceivableTotal = unReceivableTotal.subtract(getTotal().getTotal());
    }

    return unReceivableTotal;
  }

  // 冗余数据
  private BigDecimal shouldReceiptTotal = BigDecimal.ZERO;
  private Bank bank;
  private UCN paymentType;

  /** 未收金额，用于与界面保持数据一致，注意 */
  public BigDecimal getShouldReceiptTotal() {
    return shouldReceiptTotal;
  }

  public void setShouldReceiptTotal(BigDecimal shouldReceiptTotal) {
    this.shouldReceiptTotal = shouldReceiptTotal;
  }
  
  public Bank getBank() {
    return bank;
  }
  
  public void setBank(Bank bank) {
    this.bank = bank;
  }
  
  public UCN getPaymentType() {
    return paymentType;
  }
  
  public void setPaymentType(UCN paymentType) {
    this.paymentType = paymentType;
  }

  // 预处理工作（界面到服务）：进行一些冗余数据处理
  @JsonIgnore
  public void preProcess() {
    if (getUnpayedTotal() == null) {
      setUnpayedTotal(Total.zero());
    }
    getUnpayedTotal().setTotal(shouldReceiptTotal);
  }

}
