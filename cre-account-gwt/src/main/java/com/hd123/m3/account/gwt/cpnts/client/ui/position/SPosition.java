/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	SPosition.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-7 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.position;

import java.math.BigDecimal;

import com.hd123.rumba.commons.gwt.entity.client.BEntity;
import com.hd123.rumba.commons.gwt.entity.client.HasCode;
import com.hd123.rumba.commons.gwt.entity.client.HasName;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.BWithUCN;

/**
 * @author chenrizhang
 * 
 */
public class SPosition extends BEntity implements BWithUCN {
  private static final long serialVersionUID = 300200L;
  private String code;
  private String name;
  private BUCN store;
  private String positionType;
  private String positionSubType;
  private PositionSubType searchType;

  private BUCN building;
  private BUCN floor;
  private BigDecimal rentArea;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BUCN getStore() {
    return store;
  }

  public void setStore(BUCN store) {
    this.store = store;
  }

  public String getPositionType() {
    return positionType;
  }

  public void setPositionType(String positionType) {
    this.positionType = positionType;
  }

  public BUCN getBuilding() {
    return building;
  }

  public void setBuilding(BUCN building) {
    this.building = building;
  }

  public BUCN getFloor() {
    return floor;
  }

  public void setFloor(BUCN floor) {
    this.floor = floor;
  }

  public BigDecimal getRentArea() {
    return rentArea;
  }

  public void setRentArea(BigDecimal rentArea) {
    this.rentArea = rentArea;
  }

  /** 位置子类型 */
  public String getPositionSubType() {
    return positionSubType;
  }

  public void setPositionSubType(String positionSubType) {
    this.positionSubType = positionSubType;
  }

  /** 特殊用途，无实际含义 */
  public PositionSubType getSearchType() {
    return searchType;
  }

  /** 特殊用途，无实际含义 */
  public void setSearchType(PositionSubType searchType) {
    this.searchType = searchType;
  }

  @Override
  public String toFriendlyStr() {
    return BUCN.toFriendlyStr(this);
  }

  @Override
  public void inject(Object source) {
    super.inject(source);
    if (source instanceof HasCode) {
      setCode(((HasCode) source).getCode());
    }
    if (source instanceof HasName) {
      setName(((HasName) source).getName());
    }
    if (source instanceof SPosition) {
      setPositionType(((SPosition) source).getPositionType());
      setSearchType(((SPosition) source).getSearchType());
      setStore(new BUCN(((SPosition) source).getStore()));
      setBuilding(new BUCN(((SPosition) source).getBuilding()));
      setFloor(new BUCN(((SPosition) source).getFloor()));
      setRentArea(((SPosition) source).getRentArea());
    }
  }

}
