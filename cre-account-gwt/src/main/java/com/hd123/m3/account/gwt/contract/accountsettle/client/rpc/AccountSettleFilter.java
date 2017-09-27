/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountSettleFilter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-10 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.contract.accountsettle.client.rpc;

import com.hd123.m3.account.gwt.contract.accountsettle.intf.client.AccountSettleUrlParams.Filter;
import com.hd123.rumba.gwt.base.client.QueryFilter;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * @author huangjunxian
 * 
 */
public class AccountSettleFilter extends QueryFilter implements Filter {

  private static final long serialVersionUID = 300200L;

  public AccountSettleFilter() {
    super();
    setPage(0);
    setPage(DEFAULT_PAGE_SIZE);
  }

  public AccountSettleFilter(QueryFilter filter) {
    super();
    assign(filter);
  }

  public String getAccountUnitUuid() {
    return (String) get(KEY_ACCOUNTUNIT);
  }

  public void setAccountUnitUuid(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      remove(KEY_ACCOUNTUNIT);
    else
      put(KEY_ACCOUNTUNIT, uuid.trim());
  }

  public String getCounterpartUuid() {
    return (String) get(KEY_COUNTERPART);
  }

  public void setCounterpartUuid(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      remove(KEY_COUNTERPART);
    else
      put(KEY_COUNTERPART, uuid.trim());
  }

  public String getContractNumber() {
    return (String) get(KEY_CONTRACT_BILLNUMBER);
  }

  public void setContractNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      remove(KEY_CONTRACT_BILLNUMBER);
    else
      put(KEY_CONTRACT_BILLNUMBER, billNumber.trim());
  }

  public String getContractTitle() {
    return (String) get(KEY_CONTRACT_TITLE);
  }

  public void setContractTitle(String title) {
    if (StringUtil.isNullOrBlank(title))
      remove(KEY_CONTRACT_TITLE);
    else
      put(KEY_CONTRACT_TITLE, title.trim());
  }

  public String getFloorUuid() {
    return (String) get(KEY_FLOOR);
  }

  public void setFloorUuid(String floor) {
    if (StringUtil.isNullOrBlank(floor))
      remove(KEY_FLOOR);
    else
      put(KEY_FLOOR, floor.trim());
  }

  public String getCategory() {
    return (String) get(KEY_CATEGORY);
  }

  public void setCategory(String category) {
    if (StringUtil.isNullOrBlank(category)) {
      remove(KEY_CATEGORY);
    } else {
      put(KEY_CATEGORY, category.trim());
    }
  }

  public String getBuildingType() {
    return (String) get(KEY_BUILDING_TYPE);
  }

  public void setBuildingType(String buildingType) {
    if (StringUtil.isNullOrBlank(buildingType)) {
      remove(KEY_BUILDING_TYPE);
    } else {
      put(KEY_BUILDING_TYPE, buildingType.trim());
    }
  }

  public String getBuildingUuid() {
    return (String) get(KEY_BUILDING);
  }

  public void setBuildingUuid(String building) {
    if (StringUtil.isNullOrBlank(building)) {
      remove(KEY_BUILDING);
    } else {
      put(KEY_BUILDING, building.trim());
    }
  }

  public String getPositionType() {
    return (String) get(KEY_POSITION_TYPE);
  }

  public void setPostionType(String positionType) {
    if (StringUtil.isNullOrBlank(positionType))
      remove(KEY_POSITION_TYPE);
    else
      put(KEY_POSITION_TYPE, positionType.trim());
  }

  public String getPositionSubType() {
    return (String) get(KEY_POSITION_SUBTYPE);
  }

  public void setPostionSubType(String positionSubType) {
    if (StringUtil.isNullOrBlank(positionSubType))
      remove(KEY_POSITION_SUBTYPE);
    else
      put(KEY_POSITION_SUBTYPE, positionSubType.trim());
  }

  public String getPositionUuid() {
    return (String) get(KEY_POSITION);
  }

  public void setPostionUuid(String position) {
    if (StringUtil.isNullOrBlank(position))
      remove(KEY_POSITION);
    else
      put(KEY_POSITION, position.trim());
  }

  public String getCoopMode() {
    return (String) get(KEY_COOPMODE);
  }

  public void setCoopMode(String coopMode) {
    if (StringUtil.isNullOrBlank(coopMode))
      remove(KEY_COOPMODE);
    else
      put(KEY_COOPMODE, coopMode.trim());
  }

  public String getContractCategory() {
    return (String) get(KEY_CONTRACT_CATEGORY);
  }

  public void setContractCategory(String contractCategory) {
    if (StringUtil.isNullOrBlank(contractCategory))
      remove(KEY_CONTRACT_CATEGORY);
    else
      put(KEY_CONTRACT_CATEGORY, contractCategory.trim());
  }

  public String getSettleName() {
    return (String) get(KEY_SETTLEMENT_NAME);
  }

  public void setSettleName(String name) {
    if (StringUtil.isNullOrBlank(name))
      remove(KEY_SETTLEMENT_NAME);
    else
      put(KEY_SETTLEMENT_NAME, name.trim());
  }

  public String getBillCalculateType() {
    return (String) get(KEY_BILLCALCULATETYPE);
  }

  public void setBillCalculateType(String type) {
    if (StringUtil.isNullOrBlank(type))
      remove(KEY_BILLCALCULATETYPE);
    else
      put(KEY_BILLCALCULATETYPE, type.trim());
  }

  public String getCounterpartType() {
    return (String) get(KEY_COUNTERPARTTYPE);
  }

  public void setCounterpartType(String type) {
    if (StringUtil.isNullOrBlank(type))
      remove(KEY_COUNTERPARTTYPE);
    else
      put(KEY_COUNTERPARTTYPE, type.trim());
  }
}
