/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BPaymentCollectionLine.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月9日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.biz;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxRate;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridElement;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 收款单代收明细
 * 
 * @author LiBin
 * 
 */
public class BPaymentCollectionLine extends BPaymentLine implements EditGridElement {

  private static final long serialVersionUID = 2065885918596584178L;

  private BUCN subject;
  private BUCN contract;
  private BTaxRate rate;
  private Date beginDate;
  private Date endDate;
  private String ivcType;
  private String ivcCode;
  private String ivcNumber;
  private Date ivcDate;
  private BTotal ivcTotal;
  private String accountId;

  /** 科目 */
  public BUCN getSubject() {
    return subject;
  }

  public void setSubject(BUCN subject) {
    this.subject = subject;
  }

  /** 合同 */
  public BUCN getContract() {
    return contract;
  }

  public void setContract(BUCN contract) {
    this.contract = contract;
  }

  /** 科目税率 */
  public BTaxRate getRate() {
    return rate;
  }

  public void setRate(BTaxRate rate) {
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

  /** 登记日期 */
  public Date getIvcDate() {
    return ivcDate;
  }

  public void setIvcDate(Date ivcDate) {
    this.ivcDate = ivcDate;
  }

  /** 登记金额 */
  public BTotal getIvcTotal() {
    return ivcTotal;
  }

  public void setIvcTotal(BTotal ivcTotal) {
    this.ivcTotal = ivcTotal;
  }

  /** 账款Id，只读 */
  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean match(EditGridElement other) {
    return false;
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

}
