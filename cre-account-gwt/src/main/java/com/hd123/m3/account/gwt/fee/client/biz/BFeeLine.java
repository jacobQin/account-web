/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： BFeeLine.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-16 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.biz;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.hd123.m3.account.gwt.cpnts.client.biz.BSubject;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxCalculator;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxRate;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.m3.commons.gwt.widget.client.biz.BEditableValue;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridElement;
import com.hd123.rumba.commons.gwt.entity.client.BEntity;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 费用单明细
 * 
 * @author subinzhu
 * 
 */
public class BFeeLine extends BEntity implements EditGridElement {
  private static final long serialVersionUID = 300200L;

  private int lineNumber;
  private BUCN subject;
  private BEditableValue<BigDecimal> total = new BEditableValue<BigDecimal>("0.00", BigDecimal.ZERO,
      BigDecimal.ZERO);
  private BigDecimal tax = BigDecimal.ZERO;
  private BTaxRate taxRate;
  private boolean issueInvoice = true;
  private String accountId;
  private String remark;
  private BDateRange dateRange = new BDateRange();
  private BSubject bSubject;

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public BUCN getSubject() {
    return subject;
  }

  public void setSubject(BUCN subject) {
    this.subject = subject;
  }

  public BEditableValue<BigDecimal> getTotal() {
    return total;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  public BTaxRate getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(BTaxRate taxRate) {
    this.taxRate = taxRate;
  }

  public boolean isIssueInvoice() {
    return issueInvoice;
  }

  public void setIssueInvoice(boolean issueInvoice) {
    this.issueInvoice = issueInvoice;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public BDateRange getDateRange() {
    return dateRange;
  }

  public void setDateRange(BDateRange dateRange) {
    this.dateRange = dateRange;
  }

  /** 用于新建页面暂存科目信息 */
  public BSubject getbSubject() {
    return bSubject;
  }

  public void setbSubject(BSubject bSubject) {
    this.bSubject = bSubject;
  }

  public boolean isEmpty() {
    if (subject != null && (StringUtil.isNullOrBlank(subject.getUuid()) == false
        || StringUtil.isNullOrBlank(subject.getCode()) == false)) {
      return false;
    }
    if ("0.00".equals(total.getText()) == false
        && StringUtil.isNullOrBlank(total.getText()) == false) {
      return false;
    }
    if (taxRate != null) {
      return false;
    }
    return StringUtil.isNullOrBlank(remark);
  }

  @Override
  public boolean match(EditGridElement other) {
    return false;
  }

  public void changeTotal(int scale, RoundingMode roundingMode) {
    tax = BTaxCalculator.tax(total.getValue(), taxRate, scale, roundingMode);
  }

  public void copy(BFeeLine target) {
    setUuid(target.getUuid());
    subject = target.subject == null ? null : target.subject.clone();
    total = target.total.clone();
    tax = target.tax;
    setTaxRate(target.getTaxRate());
    setIssueInvoice(target.issueInvoice);
    setAccountId(target.getAccountId());
    setRemark(target.getRemark());
  }

}
