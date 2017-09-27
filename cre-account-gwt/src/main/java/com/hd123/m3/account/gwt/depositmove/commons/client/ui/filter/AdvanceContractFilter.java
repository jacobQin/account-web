/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BDepositContractFilter.java
 * 模块说明：	
 * 修改历史：
 * 2014-02-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.commons.client.ui.filter;

import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * @author zhuhairui
 * 
 */
public class AdvanceContractFilter extends QueryFilter {
  private static final long serialVersionUID = 3655257124455800931L;

  private String billNumber;
  private String counterpart;
  private String counterpartType;
  private String accountUnit;
  private String title;
  private String position;
  private String brand;
  private String floor;
  private String state;

  private String accountUnitUuid;

  // 收付方向
  private int directionType;
  // 是否从预存款账户查询合同
  private boolean isQueryAdvance;

  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }

  public String getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(String counterpart) {
    this.counterpart = counterpart;
  }
  
  
  public String getCounterpartType() {
    return counterpartType;
  }

  public void setCounterpartType(String counterpartType) {
    this.counterpartType = counterpartType;
  }

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

  public boolean isQueryAdvance() {
    return isQueryAdvance;
  }

  public void setQueryAdvance(boolean isQueryAdvance) {
    this.isQueryAdvance = isQueryAdvance;
  }

  public String getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(String accountUnit) {
    this.accountUnit = accountUnit;
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

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

}
