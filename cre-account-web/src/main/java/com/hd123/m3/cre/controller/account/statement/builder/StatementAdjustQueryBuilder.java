/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	StatementAdjustQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月11日 - renjingzhan - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.builder;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.account.service.statement.adjust.StatementAdjusts;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.m3.cre.controller.account.statement.StatementConstants;
import com.hd123.m3.sales.service.prom.modify.ModifyPromRequests;
import com.hd123.m3.sales.service.prom.modify.ModifyType;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;

/**
 * @author renjingzhan
 *
 */
@Component
public class StatementAdjustQueryBuilder extends FlecsQueryDefinitionBuilder {

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if ("billNumber".equals(fieldName)) {
      if (value != null) {
        queryDef.addFlecsCondition(StatementAdjusts.FIELD_BILLNUMBER,
            StatementAdjusts.OPERATOR_START_WITH, value);
      }
    } else if ("settleNo".equals(fieldName)) {
      queryDef.addFlecsCondition(StatementAdjusts.FIELD_SETTLENO, StatementAdjusts.OPERATOR_EQUALS,
          value);
    } else if ("counterpart".equals(fieldName)) {
      queryDef.addFlecsCondition(StatementAdjusts.FIELD_COUNTERPART,
          StatementAdjusts.OPERATOR_EQUALS, value);
    } else if ("bizState".equals(fieldName)) {
      if (value instanceof List && !((List) value).isEmpty()) {
        queryDef.addCondition(StatementAdjusts.CONDITION_BIZSTATE_IN, ((List) value).toArray());
      } else if (value instanceof String) {
        queryDef.addCondition(StatementAdjusts.CONDITION_BIZSTATE_EQUALS, value);
      }
    } else if ("accountUnit".equals(fieldName)) {
      queryDef.addFlecsCondition(StatementAdjusts.FIELD_ACCOUNTUNIT,
          StatementAdjusts.OPERATOR_EQUALS, value);
    } else if ("contract".equals(fieldName)) {
      queryDef.addCondition(StatementAdjusts.CONDITION_CONTRACT_EQUALS, value);
    } else if ("statementNumber".equals(fieldName)) {
      queryDef.addFlecsCondition(StatementAdjusts.FIELD_STATEMENTNUMBER,
          StatementAdjusts.OPERATOR_START_WITH, value);
    } else if ("subject".equals(fieldName)) {
      queryDef.addFlecsCondition(StatementAdjusts.FIELD_SUBJECT, StatementAdjusts.OPERATOR_INCLUDE,
          value);
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null) {
      return;
    }

    if ("contract".equals(sort.getProperty())) {
      queryDef.addOrder(StatementAdjusts.ORDER_BY_CONTRACTNUMBER, dir);
    } else if ("counterpart".equals(sort.getProperty())) {
      queryDef.addOrder(StatementAdjusts.ORDER_BY_COUNTERPARTCODE, dir);
    } else if ("statement".equals(sort.getProperty())) {
      queryDef.addOrder(StatementAdjusts.ORDER_BY_STATEMENTNUMBER, dir);
    } else {
      super.buildSort(queryDef, sort, dir);
    }
  }
}
