/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BStatementAdjustLine.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.biz;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.util.client.ObjectUtil;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxCalculator;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxRate;
import com.hd123.rumba.commons.gwt.entity.client.BEntity;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.DateUtil;

/**
 * 账单调整单明细|表示层实体
 * 
 * @author zhuhairui
 * 
 */
public class BStatementAdjustLine extends BEntity {
  private static final long serialVersionUID = 2634028644874902215L;

  /** 验证信息字段名 */
  public static final String FN_STATEMENTSUBJECT = "subject";
  public static final String FN_TOTAL = "total";
  public static final String FN_TAXRATE = "taxrate";
  public static final String FN_STATEMENTBILLNUM = "statementNum";
  public static final String FN_CONTRACTBILLNUM = "contractNum";
  public static final String FN_COUNTERPART = "counterpart";
  public static final String FN_BEGINDATE = "beginDate";
  public static final String FN_ENDDATE = "endDate";
  public static final String FN_REMARK = "remark";
  public static final String FN_ACCOUNTDATE = "accountDate";

  private int lineNumber;
  private BUCN subject;
  private int direction;
  private BTotal total = BTotal.zero();
  private BTaxRate taxRate;
  private boolean invoice;
  private Date beginDate;
  private Date endDate;
  private String remark;
  private String sourceAccountId;
  private Date accountDate;
  private Date lastPayDate;

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNmuber) {
    this.lineNumber = lineNmuber;
  }

  public BUCN getSubject() {
    return subject;
  }

  public void setSubject(BUCN subject) {
    this.subject = subject;
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public BTotal getTotal() {
    return total;
  }

  public void setTotal(BTotal total) {
    this.total = total;
  }

  public BTaxRate getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(BTaxRate taxRate) {
    this.taxRate = taxRate;
  }

  public boolean isInvoice() {
    return invoice;
  }

  public void setInvoice(boolean invoice) {
    this.invoice = invoice;
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

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getSourceAccountId() {
    return sourceAccountId;
  }

  public void setSourceAccountId(String sourceAccountId) {
    this.sourceAccountId = sourceAccountId;
  }

  public Date getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(Date accountDate) {
    this.accountDate = accountDate;
  }

  public Date getLastPayDate() {
    return lastPayDate;
  }

  public void setLastPayDate(Date lastPayDate) {
    this.lastPayDate = lastPayDate;
  }

  public boolean equalsKey(BStatementAdjustLine target) {
    return (ObjectUtil.isEquals(getSubject(), target.getSubject())
        && ObjectUtil.isEquals(getDirection(), target.getDirection())
        && ObjectUtil.isEquals(getTaxRate(), target.getTaxRate())
        && ObjectUtil.isEquals(getBeginDate(), target.getBeginDate()) && ObjectUtil.isEquals(
        DateUtil.truncateToDate(getEndDate()), DateUtil.truncateToDate(target.getEndDate())));
  }

  public void changeTotal(BigDecimal total, int scale, RoundingMode roundingMode) {
    if (getTaxRate() != null) {
      BTotal t = new BTotal(total, BTaxCalculator.tax(total, getTaxRate(), scale, roundingMode));
      setTotal(t);
    } else {
      setTotal(BTotal.zero());
    }
  }
}
