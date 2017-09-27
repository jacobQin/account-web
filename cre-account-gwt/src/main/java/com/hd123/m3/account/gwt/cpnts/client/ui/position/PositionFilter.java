/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	PositionFilter.java
 * 模块说明：	
 * 修改历史：
 * 2015-1-16 - chenfeiya - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.position;

import java.util.ArrayList;
import java.util.List;

import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;

/**
 * 铺位查询条件
 * 
 * @author chenfeiya
 * 
 */
public class PositionFilter extends CodeNameFilter {
  private static final long serialVersionUID = 3650460730415820340L;

  /** 通用排序定义 */
  public static final String ORDER_BY_FIELD_CODE = "code";
  public static final String ORDER_BY_FIELD_NAME = "name";
  public static final String ORDER_BY_FIELD_STORE = "store";

  private String storeUuid;
  private List<String> states = new ArrayList<String>();
  private PositionSubType positionType;

  /**
   * 铺位所属项目<br>
   * 
   * 为空表示不限制。
   */
  public String getStoreUuid() {
    return storeUuid;
  }

  public void setStoreUuid(String storeUuid) {
    this.storeUuid = storeUuid;
  }

  /**
   * 铺位状态<br>
   * 
   * 为空表示不限制
   */
  public List<String> getStates() {
    return states;
  }

  public void setStates(List<String> states) {
    this.states = states;
  }

  /**
   * 铺位类型<br>
   * 
   * 为空表示不限制
   */
  public PositionSubType getPositionType() {
    return positionType;
  }

  public void setPositionType(PositionSubType positionType) {
    this.positionType = positionType;
  }
}
