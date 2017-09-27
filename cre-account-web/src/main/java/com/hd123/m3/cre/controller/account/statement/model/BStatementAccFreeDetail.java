/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BAccountDetail.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-1 - huangjunxian- 创建。
 */
package com.hd123.m3.cre.controller.account.statement.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.m3.commons.gwt.util.client.date.BDateRange;

/**
 * 免租记录明细
 * 
 * @author chenganbang
 * 
 */
public class BStatementAccFreeDetail implements Serializable {

  private static final long serialVersionUID = 300100L;

  private BigDecimal total;
  private BDateRange freeRange;
  private BigDecimal rate;

  /** 免租金额 */
  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public BDateRange getFreeRange() {
    return freeRange;
  }

  public void setFreeRange(BDateRange freeRange) {
    this.freeRange = freeRange;
  }

  public BigDecimal getRate() {
    return rate;
  }

  public void setRate(BigDecimal rate) {
    this.rate = rate;
  }

}
