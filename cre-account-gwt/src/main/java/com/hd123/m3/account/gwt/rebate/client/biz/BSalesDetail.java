/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	BRebateLine.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月1日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.biz;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.hd123.m3.commons.gwt.base.client.biz.BBill;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;

/**
 * B对象|返款明细|将销售数据进行合并后，用于页面展示
 * 
 * @author lixiaohong
 * @since 1.12
 *
 */
public class BSalesDetail extends BBill {
  private static final long serialVersionUID = -2925014799344150984L;

  public static final String FIELD_BILLNUMBER = "billNumber";
  public static final String FIELD_ACCOUNTDATE = "accountDate";
  public static final String FIELD_AMOUNT = "amount";

  private BDateRange period;
  private BigDecimal amount = BigDecimal.ZERO;
  private BigDecimal shouldBackAmount = BigDecimal.ZERO;
  private BigDecimal poundageAmount = BigDecimal.ZERO;
  private Map<String, BigDecimal> paymentTotals = new HashMap<String, BigDecimal>();

  /** 收款日期 */
  public BDateRange getPeriod() {
    return period;
  }

  public void setPeriod(BDateRange period) {
    this.period = period;
  }

  /** 销售额(返款金额) */
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  /** 应返金额 */
  public BigDecimal getShouldBackAmount() {
    return shouldBackAmount;
  }

  public void setShouldBackAmount(BigDecimal shouldBackAmount) {
    this.shouldBackAmount = shouldBackAmount;
  }

  /** 手续费金额 */
  public BigDecimal getPoundageAmount() {
    return poundageAmount;
  }

  public void setPoundageAmount(BigDecimal poundageAmount) {
    this.poundageAmount = poundageAmount;
  }

  public Map<String, BigDecimal> getPaymentTotals() {
    return paymentTotals;
  }

  public void setPaymentTotals(Map<String, BigDecimal> paymentTotals) {
    this.paymentTotals = paymentTotals;
  }
}
