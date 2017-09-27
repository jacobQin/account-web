/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	Calculator.java
 * 模块说明：	
 * 修改历史：
 * 2015年7月9日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.biz;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.gwt.util.client.RDateUtil;

/**
 * 计算器
 * 
 * @author chenrizhang
 *
 */
public class BAccountOption implements Serializable {
  private static final long serialVersionUID = 3196373817577860665L;

  public static final String DEFAULT_OPTION = "-";
  public static final Integer DEFAULT_SCALE = 2;
  public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;
  public static final BigDecimal DEFAULT_MONTH_DAYS = BigDecimal.ZERO;
  public static final Boolean DEFAULT_INCOMPLETE_MONTH_BY_REAL_DAYS = Boolean.FALSE;
  public static final Boolean DEFAULT_REBATE_BY_BILL = Boolean.FALSE;

  private String storeUuid;
  private Integer scale;
  private RoundingMode roundingMode;
  private BigDecimal monthDays;
  private Boolean incompleteMonthByRealDays;
  private Boolean rebateByBill;

  /** 所属项目 */
  public String getStoreUuid() {
    return storeUuid;
  }

  public void setStoreUuid(String storeUuid) {
    this.storeUuid = storeUuid;
  }

  /** 精度 */
  public Integer getScale() {
    return scale;
  }

  public void setScale(Integer scale) {
    this.scale = scale;
  }

  /** 舍入方式 */
  public RoundingMode getRoundingMode() {
    return roundingMode;
  }

  public void setRoundingMode(RoundingMode roundingMode) {
    this.roundingMode = roundingMode;
  }

  /** 账务月天数 */
  public BigDecimal getMonthDays() {
    return monthDays;
  }

  public void setMonthDays(BigDecimal monthDays) {
    this.monthDays = monthDays;
  }

  /** 非整月月末按实际天数计算 */
  public Boolean getIncompleteMonthByRealDays() {
    return incompleteMonthByRealDays;
  }

  public void setIncompleteMonthByRealDays(Boolean incompleteMonthByRealDays) {
    this.incompleteMonthByRealDays = incompleteMonthByRealDays;
  }

  public Boolean getRebateByBill() {
    return rebateByBill;
  }

  public void setRebateByBill(Boolean rebateByBill) {
    this.rebateByBill = rebateByBill;
  }

  public BigDecimal getIntervalDays(BDateRange dateRange, BDateRange monthRange) {
    BigDecimal intervalDays = BigDecimal.valueOf(dateRange.getDays());
    BigDecimal monthDays = getMonthDays();
    if (monthDays == null || monthDays.compareTo(BigDecimal.ZERO) == 0) {
      return intervalDays;
    }

    boolean beginDateEquals = RDateUtil.truncate(dateRange.getBeginDate(), RDateUtil.DAY_OF_MONTH)
        .compareTo(RDateUtil.truncate(monthRange.getBeginDate(), RDateUtil.DAY_OF_MONTH)) == 0;
    boolean endDateEquals = RDateUtil.truncate(dateRange.getEndDate(), RDateUtil.DAY_OF_MONTH)
        .compareTo(RDateUtil.truncate(monthRange.getEndDate(), RDateUtil.DAY_OF_MONTH)) == 0;
    if (beginDateEquals && endDateEquals) {
      return getMonthDays();
    } else if (endDateEquals) {
      // 启用非整月月末按实际天数计算后，月底非整月天数=财务月天数-(实际月天数-实际非整月天数)。
      if (getIncompleteMonthByRealDays()) {
        return monthDays.subtract(BigDecimal.valueOf(monthRange.getDays())).add(intervalDays);
      }
    }

    return intervalDays;
  }

  /** 提供便捷的获取月天数的接口 */
  public BigDecimal getMonthDays(BDateRange month) {
    BigDecimal monthDays = getMonthDays();
    if (monthDays.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.valueOf(month.getDays());
    }
    return monthDays;
  }
}
