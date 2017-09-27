/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	BSaleBill.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月26日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.commons.gwt.base.client.biz.BBill;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;

/**
 * @author chenganbang
 *
 */
public class BSalesBill extends BBill {
  private static final long serialVersionUID = -2495814051931014335L;

  public static final String FIELD_BILLNUMBER = "billNumber";
  public static final String FIELD_ACCOUNTDATE = "accountDate";
  public static final String FIELD_AMOUNT = "amount";

  private BDateRange accountDate;
  private BigDecimal amount;
  private List<BRebateLine> lines = new ArrayList<BRebateLine>();

  public BDateRange getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(BDateRange accountDate) {
    this.accountDate = accountDate;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public List<BRebateLine> getLines() {
    return lines;
  }

  public void setLines(List<BRebateLine> lines) {
    this.lines = lines;
  }

}
