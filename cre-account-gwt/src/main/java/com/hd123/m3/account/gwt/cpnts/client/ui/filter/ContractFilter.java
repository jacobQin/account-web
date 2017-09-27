/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	ContractFilter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-17 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.filter;

import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * @author subinzhu
 * 
 */
public class ContractFilter extends QueryFilter {

  private static final long serialVersionUID = 5431821183509326684L;

  private String billNumber;
  private String accountUnit;
  private String counterpart;
  private String title;
  private String position;
  private String brand;
  private String floor;
  private String coopMode;
  private String contractCategory;
  private String state;
  private String couterpartType;

  private boolean unlocked;
  private String accountUnitUuid;
  private String counterpartUuid;
  private boolean requiresAccountUnitAndCountpart;
  private boolean tenant = false;

  /**
   * 单号
   * 
   * @return
   */
  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }

  /**
   * 项目
   * 
   * @return
   */
  public String getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(String accountUnit) {
    this.accountUnit = accountUnit;
  }

  /**
   * 商户
   * 
   * @return
   */
  public String getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(String counterpart) {
    this.counterpart = counterpart;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getFloor() {
    return floor;
  }

  public void setFloor(String floor) {
    this.floor = floor;
  }

  public String getCounterpartUuid() {
    return counterpartUuid;
  }

  public void setCounterpartUuid(String counterpartUuid) {
    this.counterpartUuid = counterpartUuid;
  }

  public String getAccountUnitUuid() {
    return accountUnitUuid;
  }

  public void setAccountUnitUuid(String accountUnitUuid) {
    this.accountUnitUuid = accountUnitUuid;
  }

  public String getCoopMode() {
    return coopMode;
  }

  public void setCoopMode(String coopMode) {
    this.coopMode = coopMode;
  }

  public String getContractCategory() {
    return contractCategory;
  }

  public void setContractCategory(String contractCategory) {
    this.contractCategory = contractCategory;
  }

  /**
   * 状态
   * 
   * @return
   */
  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public boolean isUnlocked() {
    return unlocked;
  }

  public void setUnlocked(boolean unlocked) {
    this.unlocked = unlocked;
  }

  public String getCouterpartType() {
    return couterpartType;
  }

  public void setCouterpartType(String couterpartType) {
    this.couterpartType = couterpartType;
  }

  /** 项目及对方单位是否必须，否则不返回数据 */
  public boolean isRequiresAccountUnitAndCountpart() {
    return requiresAccountUnitAndCountpart;
  }

  public void setRequiresAccountUnitAndCountpart(boolean requiresAccountUnitAndCountpart) {
    this.requiresAccountUnitAndCountpart = requiresAccountUnitAndCountpart;
  }

  /**
   * 限制couterpartType为"_Tenant"，用于返款单，默认为false
   * 
   * @return
   */
  public boolean isTenant() {
    return tenant;
  }

  public void setTenant(boolean tenant) {
    this.tenant = tenant;
  }

}
