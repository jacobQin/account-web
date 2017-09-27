/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	CounterpartFilter.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-26 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.ui.filter;

import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * 对方单位查询filter
 * 
 * @author huangjunxian
 * 
 */
public class BatchFilter extends QueryFilter {

  private static final long serialVersionUID = 300100L;

  private String counterpartUuid;
  private String accountUnitUuid;
  private String positionType;
  private String positionUuid;
  private String contractTitle;
  private String floor;
  private String coopMode;
  private String counterpartType;

  /** 对方单位等于条件 */
  public String getCounterpartUuid() {
    return counterpartUuid;
  }

  public void setCounterpartUuid(String counterpartUuid) {
    this.counterpartUuid = counterpartUuid;
  }

  /** 结算单位等于条件 */
  public String getAccountUnitUuid() {
    return accountUnitUuid;
  }

  public void setAccountUnitUuid(String accountUnitUuid) {
    this.accountUnitUuid = accountUnitUuid;
  }

  /** 位置类型等于条件 */
  public String getPositionType() {
    return positionType;
  }

  public void setPositionType(String positionType) {
    this.positionType = positionType;
  }

  /** 位置等于条件 */
  public String getPositionUuid() {
    return positionUuid;
  }

  public void setPositionUuid(String positionUuid) {
    this.positionUuid = positionUuid;
  }

  /** 店招类似于条件 */
  public String getContractTitle() {
    return contractTitle;
  }

  public void setContractTitle(String contractTitle) {
    this.contractTitle = contractTitle;
  }

  /** 楼层等于条件 */
  public String getFloor() {
    return floor;
  }

  public void setFloor(String floor) {
    this.floor = floor;
  }

  /** 合作方式等于条件 */
  public String getCoopMode() {
    return coopMode;
  }

  public void setCoopMode(String coopMode) {
    this.coopMode = coopMode;
  }

  public String getCounterpartType() {
    return counterpartType;
  }

  public void setCounterpartType(String counterpartType) {
    this.counterpartType = counterpartType;
  }

}
