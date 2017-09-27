/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BSaveStatement.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月5日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.model;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.m3.account.commons.SourceBill;
import com.hd123.m3.account.commons.Total;
import com.hd123.m3.commons.biz.tax.TaxRate;
import com.hd123.rumba.commons.biz.entity.Entity;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 账单科目明细行
 * 
 * @author chenganbang
 *
 */
public class BStatementDetail extends Entity {
  private static final long serialVersionUID = 8290961136999290235L;

  private UCN subject;
  private int direction;
  private TaxRate taxRate;
  private BigDecimal total;
  private BigDecimal tax;
  private Date beginDate;
  private Date endDate;
  private String accId;
  private boolean issueInvoice;
  private Date lastPayDate;
  private Total freeTotal = Total.zero();
  private SourceBill sourceBill;

  private String statementType;
  private boolean fromStatement = false;
  private Date accountDate;

  public UCN getSubject() {
    return subject;
  }

  public void setSubject(UCN subject) {
    this.subject = subject;
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public TaxRate getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(TaxRate taxRate) {
    this.taxRate = taxRate;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  
  public String getAccId() {
    return accId;
  }
  
  public void setAccId(String accId) {
    this.accId = accId;
  }

  public boolean isIssueInvoice() {
    return issueInvoice;
  }

  public void setIssueInvoice(boolean issueInvoice) {
    this.issueInvoice = issueInvoice;
  }

  public Date getLastPayDate() {
    return lastPayDate;
  }

  public void setLastPayDate(Date lastPayDate) {
    this.lastPayDate = lastPayDate;
  }

  /**
   * 是否来自账单
   * 
   * @return
   */
  public boolean isFromStatement() {
    return fromStatement;
  }

  public void setFromStatement(boolean fromStatement) {
    this.fromStatement = fromStatement;
  }

  /**
   * 免租金额
   * 
   * @return
   */
  public Total getFreeTotal() {
    return freeTotal;
  }

  public void setFreeTotal(Total freeTotal) {
    this.freeTotal = freeTotal;
  }

  /**
   * 来源单据
   * 
   * @return
   */
  public SourceBill getSourceBill() {
    return sourceBill;
  }

  public void setSourceBill(SourceBill sourceBill) {
    this.sourceBill = sourceBill;
  }

  /**
   * 账单类型
   * 
   * @return
   */
  public String getStatementType() {
    return statementType;
  }

  public void setStatementType(String statementType) {
    this.statementType = statementType;
  }

  /**
   * 记账日期
   * @return
   */
  public Date getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(Date accountDate) {
    this.accountDate = accountDate;
  }

}
