/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BStatementAccRange.java
 * 模块说明：	
 * 修改历史：
 * 2015-4-21 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.biz;

import java.io.Serializable;

import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;

/**
 * 账单周期明细
 * 
 * @author huangjunxian
 * 
 */
public class BStatementAccRange implements Serializable {
  private static final long serialVersionUID = 3107513763109109240L;

  private String caption;
  private String settlementId;
  private BDateRange dateRange;

  /** 结算周期定义标题 */
  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  /** 结算周期定义id */
  public String getSettlementId() {
    return settlementId;
  }

  public void setSettlementId(String settlementId) {
    this.settlementId = settlementId;
  }

  /** 出账周期 */
  public BDateRange getDateRange() {
    return dateRange;
  }

  public void setDateRange(BDateRange dateRange) {
    this.dateRange = dateRange;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (StringUtil.isNullOrBlank(caption) == false) {
      sb.append(caption).append(":");
    }

    if (buildDateRange() != null) {
      sb.append(buildDateRange());
    }
    return sb.toString();
  }

  private String buildDateRange() {
    if (dateRange == null) {
      return null;
    }
    if (dateRange.getBeginDate() == null && dateRange.getEndDate() == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    if (dateRange.getBeginDate() != null) {
      sb.append(M3Format.fmt_yMd.format(dateRange.getBeginDate()));
      sb.append("~ ");
    }
    
    if (dateRange.getEndDate() != null) {
      sb.append(M3Format.fmt_yMd.format(dateRange.getEndDate()));
    }
    return sb.toString();
  }

}
