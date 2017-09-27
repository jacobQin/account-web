/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	BSaleDetails.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月16日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.biz;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 返款明细，也即销售数据，用于保存。
 * 
 * @author chenganbang
 */
public class BRebateLine implements Serializable {

  private static final long serialVersionUID = 1118962415695353744L;

  private int lineNumber;
  private BUCN srcBill;
  private Date salesDate;
  private BigDecimal amount;
  private BigDecimal rebateAmount;
  private BigDecimal poundageAmount;

  private Date accountDate;
  private String rptUuid;
  // 未合并
  private List<BSalesPayment> payments = new ArrayList<BSalesPayment>();

  /** 行号 */
  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  /** 来源单据 */
  public BUCN getSrcBill() {
    return srcBill;
  }

  public void setSrcBill(BUCN srcBill) {
    this.srcBill = srcBill;
  }

  /** 收款日期 */
  public Date getSalesDate() {
    return salesDate;
  }

  public void setSalesDate(Date salesDate) {
    this.salesDate = salesDate;
  }

  /** 销售金额 */
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  /** 返款金额 */
  public BigDecimal getRebateAmount() {
    return rebateAmount;
  }

  public void setRebateAmount(BigDecimal rebateAmount) {
    this.rebateAmount = rebateAmount;
  }

  /** 手续费 */
  public BigDecimal getPoundageAmount() {
    return poundageAmount;
  }

  public void setPoundageAmount(BigDecimal poundageAmount) {
    this.poundageAmount = poundageAmount;
  }

  /** 进账日期 */
  public Date getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(Date accountDate) {
    this.accountDate = accountDate;
  }

  /**
   * 销售数据uuid
   * 
   * @return
   */
  public String getRptUuid() {
    return rptUuid;
  }

  public void setRptUuid(String rptUuid) {
    this.rptUuid = rptUuid;
  }

  /** 付款方式 */
  public List<BSalesPayment> getPayments() {
    return payments;
  }

  public void setPayments(List<BSalesPayment> payments) {
    this.payments = payments;
  }

}
