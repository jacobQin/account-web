/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	SettleNoFilter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-18 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.filter;

import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * @author subinzhu
 * 
 */
public class SettleNoFilter extends QueryFilter {
  private static final long serialVersionUID = 2364760609310418929L;

  private String number;
  private BDateRange beginTimeDateRange;
  private BDateRange endTimeDateRange;

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public BDateRange getBeginTimeDateRange() {
    return beginTimeDateRange;
  }

  public void setBeginTimeDateRange(BDateRange beginTimeDateRange) {
    this.beginTimeDateRange = beginTimeDateRange;
  }

  public BDateRange getEndTimeDateRange() {
    return endTimeDateRange;
  }

  public void setEndTimeDateRange(BDateRange endTimeDateRange) {
    this.endTimeDateRange = endTimeDateRange;
  }

}
