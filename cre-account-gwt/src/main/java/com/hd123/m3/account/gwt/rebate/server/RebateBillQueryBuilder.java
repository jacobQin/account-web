/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebateBillQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月1日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.rebate.server;

import java.util.List;

import com.hd123.m3.account.gwt.cpnts.client.ui.position.SPosition;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.sales.RebateLineFilter;
import com.hd123.m3.account.gwt.rebate.intf.client.RebateBillUrlParams;
import com.hd123.m3.account.service.rebate.RebateBills;
import com.hd123.m3.account.service.report.product.ProductRpts;
import com.hd123.m3.commons.biz.query.FlecsQueryCondition;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.flecs.client.FlecsQueryDef.Condition;

/**
 * 销售额返款单|查询条件构造。
 * 
 * @author lixiaohong
 * @since 1.12
 */
public class RebateBillQueryBuilder extends FlecsConditionDecoderImpl {

  private static RebateBillQueryBuilder instance;

  public static RebateBillQueryBuilder getInstance() {
    if (instance == null) {
      instance = new RebateBillQueryBuilder();
    }
    return instance;
  }

  @Override
  protected String getDefaultOrderField() {
    return RebateBills.ORDER_BY_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }

    if (RebateBillUrlParams.Flecs.PN_BILLNUMBER.equals(fieldName.trim())) {
      return RebateBills.FIELD_BILLNUMBER;
    } else if (RebateBillUrlParams.Flecs.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return RebateBills.FIELD_BIZSTATE;
    } else if (RebateBillUrlParams.Flecs.FIELD_CONTRACT_NAME.equals(fieldName.trim())) {
      return RebateBills.FIELD_CONTRACT_NAME;
    } else if (RebateBillUrlParams.Flecs.FIELD_STORE.equals(fieldName.trim())) {
      return RebateBills.FIELD_STORE;
    } else if (RebateBillUrlParams.Flecs.FIELD_TENANT.equals(fieldName.trim())) {
      return RebateBills.FIELD_TENANT;
    } else if (RebateBillUrlParams.Flecs.FIELD_POSITION.equals(fieldName.trim())) {
      return RebateBills.FIELD_POSITION;
    } else if (RebateBillUrlParams.Flecs.FIELD_ACCOUNT_DATE.equals(fieldName.trim())) {
      return RebateBills.FIELD_AccountDate;
    } else if (RebateBillUrlParams.Flecs.FIELD_PERMGROUP.equals(fieldName.trim())) {
      return RebateBills.FIELD_PERMGROUP;
    } else if (RebateBillUrlParams.Flecs.FIELD_CREATE_INFO_OPER_ID.equals(fieldName.trim())) {
      return RebateBills.FIELD_CREATOR_CODE;
    } else if (RebateBillUrlParams.Flecs.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName.trim())) {
      return RebateBills.FIELD_CREATOR_NAME;
    } else if (RebateBillUrlParams.Flecs.FIELD_CREATE_INFO_TIME.equals(fieldName.trim())) {
      return RebateBills.FIELD_CREATED;
    } else if (RebateBillUrlParams.Flecs.FIELD_LASTMODIFY_INFO_OPER_ID.equals(fieldName.trim())) {
      return RebateBills.FIELD_LAST_MODIFIER_CODE;
    } else if (RebateBillUrlParams.Flecs.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName.trim())) {
      return RebateBills.FIELD_LAST_MODIFIER_NAME;
    } else if (RebateBillUrlParams.Flecs.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName.trim())) {
      return RebateBills.FIELD_LAST_MODIFY_TIME;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {

    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }
    if (fieldName.trim().equals(RebateBillUrlParams.Flecs.PN_BILLNUMBER)) {
      return RebateBills.ORDER_BY_BILLNUMBER;
    } else if (fieldName.trim().equals(RebateBillUrlParams.Flecs.FIELD_BIZSTATE)) {
      return RebateBills.ORDER_BY_BIZSTATE;
    } else if (fieldName.trim().equals(RebateBillUrlParams.Flecs.FIELD_STORE)) {
      return RebateBills.ORDER_BY_STORE;
    } else if (fieldName.trim().equals(RebateBillUrlParams.Flecs.FIELD_TENANT)) {
      return RebateBills.ORDER_BY_TENANT;
    } else if (fieldName.trim().equals(RebateBillUrlParams.Flecs.FIELD_CONTRACT)) {
      return RebateBills.ORDER_BY_CONTRACT;
    } else if (fieldName.trim().equals(RebateBillUrlParams.Flecs.FIELD_POSITIONS)) {
      return RebateBills.ORDER_BY_POSITIONS;
    } else if (fieldName.trim().equals(RebateBillUrlParams.Flecs.FIELD_ACCOUNT_DATE)) {
      return RebateBills.ORDER_BY_ACCOUNTDATE;
    } else if (fieldName.trim().equals(RebateBillUrlParams.Flecs.FIELD_BEGINDATE)) {
      return RebateBills.ORDER_BY_BEGINDATE;
    } else if (fieldName.trim().equals(RebateBillUrlParams.Flecs.FIELD_ENDDATE)) {
      return RebateBills.ORDER_BY_ENDDATE;
    } else if (fieldName.trim().equals(RebateBillUrlParams.Flecs.FIELD_BACKTOTAL)) {
      return RebateBills.ORDER_BY_BACKTOTAL;
    } else if (fieldName.trim().equals(RebateBillUrlParams.Flecs.FIELD_POUNDAGETOTAL)) {
      return RebateBills.ORDER_BY_POUNDAGETOTAL;
    } else if (fieldName.trim().equals(RebateBillUrlParams.Flecs.FIELD_SHOULDBACKTOTAL)) {
      return RebateBills.ORDER_BY_SHOULDBACKTOTAL;
    }
    return null;

  }
  
  @Override
  protected void buildFlecsQueryCondition(List<FlecsQueryCondition> flecsConditions,
      Condition condition) {
    if (condition.getOperand() instanceof SPosition) {
      SPosition position = (SPosition) condition.getOperand();
      if (StringUtil.isNullOrBlank(position.getCode()) == false) {
        FlecsQueryCondition flecsCondition = new FlecsQueryCondition();
        flecsCondition.setFieldName(RebateBills.FIELD_POSITION);
        flecsCondition.setOperation(condition.getOperator().getName());
        flecsCondition.getParameters().add(position.getCode());
        
        flecsConditions.add(flecsCondition);
      }
    } else {
      super.buildFlecsQueryCondition(flecsConditions, condition);
    }
  }

  /**
   * 构造查询条件
   * 
   * @param filter
   * @return
   */
  public FlecsQueryDefinition build4Query(RebateLineFilter filter) {
    if (filter == null) {
      return null;
    }
    FlecsQueryDefinition definition = new FlecsQueryDefinition();
    if (!StringUtil.isNullOrBlank(filter.getContractUuid())) {
      definition.addCondition(ProductRpts.CONDITION_CONTRACT_EQUALS, filter.getContractUuid());
    } else {
      return null;
    }
    if (!StringUtil.isNullOrBlank(filter.getSaleNumber())) {
      definition.addCondition(ProductRpts.CONDITION_SRCBILLCODE_LIKE, filter.getSaleNumber());
    }

    if (filter.getBeginDate() != null && filter.getEndDate() != null) {
      definition.addCondition(ProductRpts.CONDITION_ACCOUNTDATE_BETWEEN, filter.getBeginDate(),
          filter.getEndDate());
    }

    if (filter.getOrcTime() != null) {
      definition.addCondition(ProductRpts.CONDITION_RECEIPTDATE_EQUALS, filter.getOrcTime());
    }

    definition.addOrder(ProductRpts.SRCBILLCODE_ORDER, QueryOrderDirection.asc);
    definition.setPage(filter.getPage());
    definition.setPageSize(filter.getDefaultPageSize());
    return definition;
  }

}
