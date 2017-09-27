/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	ContractQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月23日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.server.query;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContractState;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.ContractFilter;
import com.hd123.m3.account.gwt.util.client.ObjectUtil;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryOrder;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.gwt.base.client.QueryFilter.Order;
import com.hd123.rumba.gwt.util.client.OrderDir;

/**
 * 合同筛选条件
 * 
 * @author chenganbang
 *
 */
public class ContractQueryBuilder extends CommonQueryBuilder {

  public static FlecsQueryDefinition buildDefinition(FlecsQueryDefinition queryDef,
      ContractFilter filter) {
    if (filter == null) {
      return null;
    }

    if (filter.isUnlocked()) {
      queryDef.addCondition(Contracts.CONDITION_CONTRACT_UNLOCKED);
    }

    String accountUnitUuid = filter.getAccountUnitUuid();
    if (StringUtil.isNullOrBlank(accountUnitUuid) == false) {
      queryDef.addCondition(Contracts.CONDITION_BUSINESSUNIT_UUID_EQUALS, accountUnitUuid);
    }

    String counterpartUuid = filter.getCounterpartUuid();
    if (StringUtil.isNullOrBlank(counterpartUuid) == false) {
      queryDef.addCondition(Contracts.CONDITION_COUNTERPART_UUID_EQUALS, counterpartUuid);
    }

    String couterpartType = filter.getCouterpartType();
    if (StringUtil.isNullOrBlank(couterpartType) == false) {
      queryDef.addCondition(Contracts.CONDITION_COUNTERPART_TYPE_EQUALS, couterpartType);
    }

    String billNumber = filter.getBillNumber();
    if (StringUtil.isNullOrBlank(billNumber) == false) {
      queryDef.addCondition(Contracts.CONDITION_BILLNUMBER_STARTWITH, billNumber);
    }

    String accountUnit = filter.getAccountUnit();
    if (StringUtil.isNullOrBlank(accountUnit) == false) {
      queryDef.addCondition(Contracts.CONDITION_BUSINESSUNIT_LIKE, accountUnit);
    }

    String counterpart = filter.getCounterpart();
    if (StringUtil.isNullOrBlank(counterpart) == false) {
      queryDef.addCondition(Contracts.CONDITION_COUNTERPART_LIKE, counterpart);
    }

    String title = filter.getTitle();
    if (StringUtil.isNullOrBlank(title) == false) {
      queryDef.addCondition(Contracts.CONDITION_TITLE_LIKE, title);
    }

    String floor = filter.getFloor();
    if (StringUtil.isNullOrBlank(floor) == false) {
      queryDef.addCondition(Contracts.CONDITION_FLOOR_LIKE, floor);
    }

    String brand = filter.getBrand();
    if (StringUtil.isNullOrBlank(brand) == false) {
      queryDef.addCondition(Contracts.CONDITION_BRAND_LIKE, brand);
    }

    String position = filter.getPosition();
    if (StringUtil.isNullOrBlank(position) == false) {
      queryDef.addCondition(Contracts.CONDITION_POSITION_LIKE, position);
    }

    String coopMode = filter.getCoopMode();
    if (StringUtil.isNullOrBlank(coopMode) == false) {
      queryDef.addCondition(Contracts.CONDITION_COOPMODE_LIKE, coopMode);
    }

    String contractCategory = filter.getContractCategory();
    if (StringUtil.isNullOrBlank(contractCategory) == false) {
      queryDef.addCondition(Contracts.CONDITION_CONTRACT_CATEGORY_EQUALS, contractCategory);
    }

    String state = filter.getState();
    if (BContractState.INEFFECT.equals(state)) {
      queryDef.addCondition(Contracts.CONDITION_EFFECT_STATE_EQUALS,
          Contracts.EFFECT_STATE_INEFFECT);
    } else if (BContractState.EFFECTED.equals(state)) {
      queryDef.addCondition(Contracts.CONDITION_EFFECT_STATE_EQUALS,
          Contracts.EFFECT_STATE_EFFECTED);
    } else if (BContractState.FINISHED.equals(state)) {
      queryDef.addCondition(Contracts.CONDITION_EFFECT_STATE_EQUALS,
          Contracts.EFFECT_STATE_FINISHED);
    }

    List<QueryOrder> orders = new ArrayList<QueryOrder>();
    if (filter.getOrders() != null && filter.getOrders().size() > 0) {
      for (Order order : filter.getOrders()) {
        String orderField = null;
        if (ObjectUtil.isEquals(BContract.FIELD_ACCOUNTUNIT, order.getFieldName())) {
          orderField = Contracts.ORDER_BY_BUSINESSUNIT;
        } else if (ObjectUtil.isEquals(BContract.FIELD_COUNTERPART, order.getFieldName())) {
          orderField = Contracts.ORDER_BY_COUNTERPART;
        } else if (ObjectUtil.isEquals(BContract.FIELD_TITLE, order.getFieldName())) {
          orderField = Contracts.ORDER_BY_TITLE;
        } else if (ObjectUtil.isEquals(BContract.FIELD_FLOOR, order.getFieldName())) {
          orderField = Contracts.ORDER_BY_FLOOR;
        } else if (ObjectUtil.isEquals(BContract.FIELD_COOPMODE, order.getFieldName())) {
          orderField = Contracts.ORDER_BY_COOPMODE;
        } else if (ObjectUtil.isEquals(BContract.FIELD_CONTRACT_CATEGORY, order.getFieldName())) {
          orderField = Contracts.ORDER_BY_CONTRACT_CATEGORY;
        } else {
          orderField = Contracts.ORDER_BY_BILLNUMBER;
        }
        QueryOrderDirection dir = OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
            : QueryOrderDirection.desc;
        orders.add(new QueryOrder(orderField, dir));
      }
    } else {
      String orderField = Contracts.ORDER_BY_BILLNUMBER;
      QueryOrderDirection dir = QueryOrderDirection.asc;
      orders.add(new QueryOrder(orderField, dir));
    }

    queryDef.setOrders(orders);
    queryDef.setPage(filter.getPage());
    queryDef.setPageSize(filter.getPageSize());
    return queryDef;
  }
}
