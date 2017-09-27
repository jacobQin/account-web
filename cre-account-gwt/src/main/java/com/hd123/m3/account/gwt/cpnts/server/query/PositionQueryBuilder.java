/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	PositionQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-18 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.server.query;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.cpnts.client.ui.position.PositionFilter;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.investment.service.res.building.Building;
import com.hd123.m3.investment.service.res.building.Buildings;
import com.hd123.m3.investment.service.res.floor.Floor;
import com.hd123.m3.investment.service.res.floor.Floors;
import com.hd123.m3.investment.service.res.position.Positions;
import com.hd123.rumba.commons.biz.query.QueryCondition;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.base.client.QueryFilter.Order;
import com.hd123.rumba.gwt.util.client.OrderDir;

/**
 * @author chenrizhang
 * 
 */
public class PositionQueryBuilder extends CommonQueryBuilder {
  /**
   * 构建查询的语句条件<br>
   * <li>代码起始于<li>名称类似于<li>项目等于<li>
   * 位置类型等于<li>状态限制
   */
  public static FlecsQueryDefinition build4query(PositionFilter filter, String userId) {
    if (filter == null) {
      return null;
    }

    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef.getConditions().addAll(getPositionConditions(userId));

    if (StringUtil.isNullOrBlank(filter.getStoreUuid()) == false) {
      queryDef.addCondition(Positions.CONDITION_OWNER_UUID_EQUALS, filter.getStoreUuid());
    }
    if (filter.getPositionType() != null) {
      if (StringUtil.isNullOrBlank(filter.getPositionType().getPositionType()) == false) {
        queryDef.addCondition(Positions.CONDITION_POSITIONTYPE_EQUALS, filter.getPositionType()
            .getPositionType());
      }
      if (StringUtil.isNullOrBlank(filter.getPositionType().getPositionSubType()) == false) {
        queryDef.addFlecsCondition(Positions.FIELD_SUBTYPE, Positions.OPERATOR_EQUALS, filter
            .getPositionType().getPositionSubType());
      }
    }
    if (StringUtil.isNullOrBlank(filter.getCode()) == false) {
      queryDef.addCondition(Positions.CONDITION_CODE_START_WITH, filter.getCode());
    }
    if (StringUtil.isNullOrBlank(filter.getName()) == false) {
      queryDef.addCondition(Positions.CONDITION_NAME_LIKE, filter.getName());
    }
    if (filter.getStates() != null) {
      for (String state : filter.getStates())
        queryDef.addCondition(Positions.CONDITION_STATE_EQUALS, state);
    }
    String orderField = Positions.ORDER_BY_CODE;
    QueryOrderDirection dir = QueryOrderDirection.asc;

    if (filter.getOrders().isEmpty() == false) {
      Order order = filter.getOrders().get(0);
      if (ObjectUtil.equals(PositionFilter.ORDER_BY_FIELD_NAME, order.getFieldName())) {
        orderField = Positions.ORDER_BY_NAME;
      } else if (ObjectUtil.equals(PositionFilter.ORDER_BY_FIELD_STORE, order.getFieldName())) {
        orderField = Positions.ORDER_BY_OWNER;
      }

      dir = OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
          : QueryOrderDirection.desc;
    }

    queryDef.addOrder(orderField, dir);
    queryDef.setPage(filter.getPage());
    queryDef.setPageSize(filter.getPageSize());

    return queryDef;
  }

  /**
   * 构建加载的语句条件<br>
   * <li>代码等于(为空返回空)<li>名称类似于 <li>项目等于<li>
   * 位置类型等于<li>状态限制
   */
  public static FlecsQueryDefinition build4load(PositionFilter filter, String userId) {
    if (filter == null || filter.getCode() == null) {
      return null;
    }

    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef.getConditions().addAll(getPositionConditions(userId));

    queryDef.addCondition(Positions.CONDITION_CODE_EQUALS, filter.getCode());

    if (StringUtil.isNullOrBlank(filter.getStoreUuid()) == false) {
      queryDef.addCondition(Positions.CONDITION_OWNER_UUID_EQUALS, filter.getStoreUuid());
    }
    if (filter.getPositionType() != null) {
      if (StringUtil.isNullOrBlank(filter.getPositionType().getPositionType()) == false) {
        queryDef.addCondition(Positions.CONDITION_POSITIONTYPE_EQUALS, filter.getPositionType()
            .getPositionType());
      }
      if (StringUtil.isNullOrBlank(filter.getPositionType().getPositionSubType()) == false) {
        queryDef.addFlecsCondition(Positions.FIELD_SUBTYPE, Positions.OPERATOR_EQUALS, filter
            .getPositionType().getPositionSubType());
      }
    }
    if (StringUtil.isNullOrBlank(filter.getName()) == false) {
      queryDef.addCondition(Positions.CONDITION_NAME_LIKE, filter.getName());
    }
    if (filter.getStates() != null) {
      for (String state : filter.getStates())
        queryDef.addCondition(Positions.CONDITION_STATE_EQUALS, state);
    }

    return queryDef;
  }
  
  private static List<QueryCondition> getPositionConditions(String userId){
    List<QueryCondition> conditions = getPermConditions(Positions.OBJECT_NAME, userId);
    //如果没有开启自身授权组
    if (!isPermEnabled(Positions.OBJECT_NAME)) {
      //查看是否开启楼层授权组
      if (isPermEnabled(Floors.OBJECT_NAME)) {
        QueryCondition qc = new QueryCondition();
        qc.setOperation(Positions.CONDITION_FLOOR_IN);
        //根据楼层授权组的uuid集合从数据库中获取对应的铺位uuid集合
        FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
        List<QueryCondition> floorConditions = getPermConditions(Floors.OBJECT_NAME, userId);
        queryDef.getConditions().addAll(floorConditions);
        List<Floor> floors = getFloorService().query(queryDef).getRecords();
        List<String> list = new ArrayList<String>();
        for (Floor floor : floors) {
          list.add(floor.getUuid());
        }
        if (list != null && list.size() > 0) {
          qc.addParameter(list.toArray());
        }
        conditions.add(qc);
      } else {//没有开启楼层授权组
        //查看是否开启楼宇授权组
        if (isPermEnabled(Buildings.OBJECT_NAME)) {
          QueryCondition qc = new QueryCondition();
          qc.setOperation(Positions.CONDITION_BUILDING_IN);
          //根据楼层授权组的uuid集合从数据库中获取对应的铺位uuid集合
          FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
          List<QueryCondition> buildingList = getPermConditions(Buildings.OBJECT_NAME, userId);
          queryDef.getConditions().addAll(buildingList);
          List<Building> buildings = getBuildingService().query(queryDef).getRecords();
          List<String> list = new ArrayList<String>();
          for(Building building : buildings){
            list.add(building.getUuid());
          }
          if (list != null && list.size() > 0) {
            qc.addParameter(list.toArray());
          }
          conditions.add(qc);
        }
      }
    }
    return conditions;
  }
}
