/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AdvanceSubjectFilter.java
 * 模块说明：	
 * 修改历史：
 * 2013-12-11 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client;

import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * 科目查询筛选器
 * 
 * @author subinzhu
 * 
 */
public class AdvanceSubjectFilter extends QueryFilter {
  private static final long serialVersionUID = -6459326283073889549L;

  // 对方结算中心uuid
  private String accountUnitUuid;
  // 收付方向
  private int directionType;
  // 对方结算中心uuid
  private String counterpartUuid;

  public String getAccountUnitUuid() {
    return accountUnitUuid;
  }

  public void setAccountUnitUuid(String accountUnitUuid) {
    this.accountUnitUuid = accountUnitUuid;
  }

  public int getDirectionType() {
    return directionType;
  }

  public void setDirectionType(int directionType) {
    this.directionType = directionType;
  }

  public String getCounterpartUuid() {
    return counterpartUuid;
  }

  public void setCounterpartUuid(String counterpartUuid) {
    this.counterpartUuid = counterpartUuid;
  }

}
