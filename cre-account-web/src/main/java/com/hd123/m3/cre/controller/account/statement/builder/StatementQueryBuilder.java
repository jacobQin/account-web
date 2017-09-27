/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	SalesInputQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月10日 - cRazy - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.builder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.statement.ConfirmState;
import com.hd123.m3.account.service.statement.SettleState;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementType;
import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.query.ValueRange;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.cre.controller.account.statement.StatementConstants;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author cRazy
 * 
 */

@Component
public class StatementQueryBuilder extends FlecsQueryDefinitionBuilder {

  private static final String FILTER_BILLNUMBER = "billNumber";
  private static final String FILTER_SIGNNUMBER = "signNumber";
  private static final String FILTER_STATEMENT_STATE = "statementState";
  private static final String FILTER_EFFECT_UNSETTLED = "effectAndUnsettled";
  private static final String FILTER_EFFECT_PARTSETTLED = "effectAndPartSettled";
  private static final String FILTER_EFFECT_SETTLED = "effectAndSettled";
  private static final String FILTER_TYPE = "type";
  private static final String FILTER_CONFIRMSTATE = "confirmState";

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null)
      return;

    if (FILTER_BILLNUMBER.equals(fieldName)) {
      queryDef
          .addFlecsCondition(Statements.FIELD_BILLNUMBER, Statements.OPERATOR_START_WITH, value);
    } else if (FILTER_STATEMENT_STATE.equals(fieldName)) {
      if (value instanceof List) {
        for (Object state : ((List) value)) {
          String stateStr = (String) state;
          addStatementStateCondition(queryDef, stateStr);
        }
      } else if (value instanceof String) {
        addStatementStateCondition(queryDef, (String) value);
      }
    } else if (StatementConstants.FILTER_STORE_UUID.equals(fieldName)) {
      queryDef.addFlecsCondition(Statements.FIELD_ACCOUNTUNIT, Statements.OPERATOR_EQUALS, value);
    } else if (StatementConstants.FILTER_STATEMENT_TYPE.equals(fieldName)) {
      queryDef.addCondition(Statements.CONDITION_TYPE_EQUALS,
          StatementType.valueOf(value.toString()));
    } else if (StatementConstants.FILTER_CONTRACT_UUID.equals(fieldName)) {
      queryDef.addCondition(Statements.CONDITION_CONTRACT_ID_EQUALS, value);
    } else if (StatementConstants.FILTER_COUNTERPART_UUID.equals(fieldName)) {
      queryDef.addCondition(Statements.CONDITION_COUNTERPAR_EQUALS, value);
    } else if (StatementConstants.FILTER_ACCOUNTTIME.equals(fieldName)) {
      queryDef.addFlecsCondition(Statements.FIELD_ACCOUNTTIME, Statements.OPERATOR_EQUALS,
          StringUtil.toDate((String) value, "yyyy-MM-dd"));
    } else if (StatementConstants.FIELD_ACCOUNTRANGE.equals(fieldName)) {
      queryDef.addFlecsCondition(Statements.FIELD_ACCOUNTRANGE, Statements.OPERATOR_INCLUDE,
          StringUtil.toDate((String) value, "yyyy-MM-dd"));
    } else if (StatementConstants.FILTER_INVOICEREG.equals(fieldName)) {
      String invoiceReg = (String) value;
      if (!StringUtil.isNullOrBlank(invoiceReg)) {
        queryDef.addFlecsCondition(Statements.FIELD_INVOICED, Statements.OPERATOR_EQUALS,
            StringUtil.toBoolean(invoiceReg));
      }
    } else if (StatementConstants.FILTER_BUILDING_FIELD.equals(fieldName)) {
      LinkedHashMap<String, Object> object = (LinkedHashMap) value;
      String buildingType = (String) object.get("buildingType");
      LinkedHashMap<String, String> building = (LinkedHashMap<String, String>) object.get("entity");

      if (!StringUtil.isNullOrBlank(buildingType)) {
        queryDef.addFlecsCondition(Statements.FIELD_BUILDING_TYPE, Statements.OPERATOR_EQUALS,
            buildingType);
      }
      if (building != null) {
        queryDef.addFlecsCondition(Statements.FIELD_BUILDING, Statements.OPERATOR_EQUALS,
            building.get("uuid"));
      }
    } else if (StatementConstants.FILTER_POSITION_FIELD.equals(fieldName)) {
      LinkedHashMap<String, LinkedHashMap<String, String>> object = (LinkedHashMap) value;
      LinkedHashMap<String, String> positionType = object.get("positionType");
      LinkedHashMap<String, String> position = object.get("entity");
      if (positionType != null) {
        queryDef.addFlecsCondition(Statements.FIELD_POSITION_TYPE, Statements.OPERATOR_EQUALS,
            positionType.get("positionType"));
        if (positionType.get("subType") != null) {
          queryDef.addFlecsCondition(Statements.FIELD_POSITION_SUBTYPE, Statements.OPERATOR_EQUALS,
              positionType.get("subType"));
        }
      }
      if (position != null) {
        queryDef.addFlecsCondition(Statements.FIELD_POSITION, Statements.OPERATOR_EQUALS,
            position.get("uuid"));
      }
    } else if (StatementConstants.FILTER_PAYTOTAL.equals(fieldName)) {
      LinkedHashMap<String, Object> payTotal = (LinkedHashMap<String, Object>) value;
      BigDecimal begin = payTotal.get("beginValue") == null ? null : new BigDecimal(payTotal.get(
          "beginValue").toString());
      BigDecimal end = payTotal.get("endValue") == null ? null : new BigDecimal(payTotal.get(
          "endValue").toString());
      queryDef.addFlecsCondition(Statements.FIELD_PAYTOTAL, Statements.OPERATOR_BETWEEN,
          new ValueRange(begin, end));
    } else if (StatementConstants.FILTER_RECEIPTTOTAL.equals(fieldName)) {
      LinkedHashMap<String, Object> receiptTotal = (LinkedHashMap<String, Object>) value;
      BigDecimal begin = receiptTotal.get("beginValue") == null ? null : new BigDecimal(
          receiptTotal.get("beginValue").toString());
      BigDecimal end = receiptTotal.get("endValue") == null ? null : new BigDecimal(receiptTotal
          .get("endValue").toString());
      queryDef.addFlecsCondition(Statements.FIELD_RECEIPTTOTAL, Statements.OPERATOR_BETWEEN,
          new ValueRange(begin, end));
    } else if (FILTER_TYPE.equals(fieldName)) {
      String type = (String) value;
      if (!StringUtil.isNullOrBlank(type)) {
        queryDef.addFlecsCondition(Statements.FIELD_TYPE, Statements.OPERATOR_EQUALS, type);
      }
    } else if ("sourceBillNumber".equals(fieldName)) {
      queryDef.addFlecsCondition(Statements.FIELD_SOURCEBILLNUMBER, Statements.OPERATOR_START_WITH,
          value);
    } else if (FILTER_SIGNNUMBER.equals(fieldName)) {
      queryDef.addCondition(Statements.CONDITION_CONTRACT_SIGNNUMBER_LIKE, value);
    } else if ("principalStore".equals(fieldName)) {
      queryDef.addCondition(Statements.CONDITION_PRINCIPALSTORE_EQUALS, value);
    } else if ("floor".equals(fieldName)) {
      queryDef.addCondition(Statements.CONDITION_CONTRACT_FLOOR_UUID_EQUALS, value);
    } else if ("receiptAccDate".equals(fieldName)) {
      Date planDate = StringUtil.toDate((String) value, "yyyy-MM-dd");
      queryDef.addCondition(Statements.CONDITION_RECEIPTACCDATE_EQUALS, planDate);
    } else if (FILTER_CONFIRMSTATE.equals(fieldName)) {
      String confirmState = (String) value;
      if (!StringUtil.isNullOrBlank(confirmState)) {
        queryDef.addCondition(Statements.CONDITION_CONFIRMSTATE_EQUALS,
            ConfirmState.valueOf(confirmState));
      }
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null) {
      return;
    }

    if (StatementConstants.ORDER_BY_STORE.equals(sort.getProperty())) {
      queryDef.addOrder(Statements.ORDER_BY_ACCOUNTUNIT_CODE, dir);
    } else if (StatementConstants.ORDER_BY_CONTRACT.equals(sort.getProperty())) {
      queryDef.addOrder(Statements.ORDER_BY_CONTRACTNUMBER, dir);
    } else if (StatementConstants.ORDER_BY_COUNTERPART.equals(sort.getProperty())) {
      queryDef.addOrder(Statements.ORDER_BY_COUNTERPART_CODE, dir);
    } else if (StatementConstants.ORDER_BY_SETTLENO.equals(sort.getProperty())) {
      queryDef.addOrder(Statements.ORDER_BY_SETTLE, dir);
    } else if (StatementConstants.ORDER_BY_STATEMENTSTATE.equals(sort.getProperty())) {
      queryDef.addOrder(Statements.ORDER_BY_BIZSTATE, dir);
      queryDef.addOrder(Statements.ORDER_BY_SETTLESTATE, dir);
    } else {
      super.buildSort(queryDef, sort, dir);
    }

  }

  @Override
  public FlecsQueryDefinition build(FlecsQueryDefinition queryDef, QueryFilter queryFilter) {
    if (queryDef == null || queryFilter == null) {
      return queryDef;
    }

    buildFilter(queryDef, queryFilter.getFilter());
    buildSort(queryDef, queryFilter.getSorts());

    if (StringUtil.isNullOrBlank(queryFilter.getKeyword()) == false) {
      queryDef.addCondition(Statements.CONDITION_BILLNUMBER_STARTWITH, queryFilter.getKeyword()
          .trim());
    }
    if (StringUtil.isNullOrBlank(queryFilter.getId()) == false) {
      queryDef.addCondition(Basices.CONDITION_ID_EQUALS, queryFilter.getId());
    }

    queryDef.setPage(queryFilter.getCurrentPage());
    queryDef.setPageSize(queryFilter.getPageSize());
    queryDef.getFetchParts().add(Statement.FETCH_PART_RANGES);
    queryDef.getFetchParts().add(Statement.FETCH_PART_LINES);
    return queryDef;
  }

  /**
   * 添加状态过滤条件
   * 
   * @param queryDef
   * @param state
   */
  private void addStatementStateCondition(FlecsQueryDefinition queryDef, String state) {
    if (BizStates.INEFFECT.equals(state) || BizStates.ABORTED.equals(state)) {
      queryDef.addCondition(Statements.CONDITION_BIZSTATE_EQUALS, state);
    } else if (FILTER_EFFECT_UNSETTLED.equals(state)) {
      queryDef.addCondition(Statements.CONDITION_BIZSTATE_EQUALS, BizStates.EFFECT);
      queryDef.addCondition(Statements.CONDITION_SETTLESTATE_EQUALS, SettleState.initial);
    } else if (FILTER_EFFECT_PARTSETTLED.equals(state)) {
      queryDef.addCondition(Statements.CONDITION_BIZSTATE_EQUALS, BizStates.EFFECT);
      queryDef.addCondition(Statements.CONDITION_SETTLESTATE_EQUALS, SettleState.partialSettled);
    } else if (FILTER_EFFECT_SETTLED.equals(state)) {
      queryDef.addCondition(Statements.CONDITION_BIZSTATE_EQUALS, BizStates.EFFECT);
      queryDef.addCondition(Statements.CONDITION_SETTLESTATE_EQUALS, SettleState.settled);
    }
  }
}
