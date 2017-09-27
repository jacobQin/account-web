/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	AccountDefrayalQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月30日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.accountdefrayal.builder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hd123.m3.account.service.report.accountdefrayal.AccountDefrayals;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author chenganbang
 *
 */
public class AccountDefrayalQueryBuilder {
  private static AccountDefrayalQueryBuilder instance = null;

  private AccountDefrayalQueryBuilder() {
  }

  public static AccountDefrayalQueryBuilder getInstance() {
    if (instance == null) {
      instance = new AccountDefrayalQueryBuilder();
    }
    return instance;
  }

  /**
   * @param filter
   * @param ignoreSettleState
   *          是否忽略状态
   * @return
   */
  public FlecsQueryDefinition buildDefinition(QueryFilter filter, boolean ignoreSettleState) {
    FlecsQueryDefinition definition = new FlecsQueryDefinition();
    Map<String, Object> filterMap = filter.getFilter();

    List<String> settleState = (List<String>) filterMap.get("settleState");
    if (!ignoreSettleState && settleState != null && !settleState.isEmpty()) {
      if (settleState.size() == 1) {// 状态单选
        definition.addCondition(settleState.get(0), true);
      } else {// 状态多选
        definition.addCondition(AccountDefrayals.CONDITION_SETTLE_STATES, settleState.toArray());
      }
    }

    String contractUuid = (String) filterMap.get("contractUuid");
    if (!StringUtil.isNullOrBlank(contractUuid)) {
      definition.addCondition(AccountDefrayals.CONDITION_CONTRACT_EQUALS, contractUuid);
    }

    String counterpartUuid = (String) filterMap.get("counterpartUuid");
    if (!StringUtil.isNullOrBlank(counterpartUuid)) {
      definition.addCondition(AccountDefrayals.CONDITION_COUNTERPART_EQUALS, counterpartUuid);
    }

    String settleNo = (String) filterMap.get("settleNo");
    if (!StringUtil.isNullOrBlank(settleNo)) {
      definition.addFlecsCondition(AccountDefrayals.FIELD_STATEMENTSETTLENO,
          AccountDefrayals.OPERATOR_EQUALS, settleNo);
    }

    String bizType = (String) filterMap.get("biztype");
    if (!StringUtil.isNullOrBlank(bizType)) {
      definition.addCondition(AccountDefrayals.CONDITION_BIZTYPE_EQUALS, bizType);
    }

    LinkedHashMap<String, LinkedHashMap<String, String>> map = (LinkedHashMap) filterMap
        .get("position");
    if (map != null) {
      LinkedHashMap<String, String> positionType = map.get("positionType");
      LinkedHashMap<String, String> position = map.get("entity");
      if (positionType != null) {
        definition.addCondition(AccountDefrayals.CONDITION_POSITIONTYPE_EQUALS,
            positionType.get("positionType"));
        if (positionType.get("subType") != null) {
          definition.addCondition(AccountDefrayals.CONDITION_POSITION_SUBTYPE_EQUALS,
              positionType.get("subType"));
        }
      }
      if (position != null) {
        definition.addCondition(AccountDefrayals.CONDITION_POSITION_EQUALS, position.get("uuid"));
      }
    }

    String subjectUuid = (String) filterMap.get("subjectUuid");
    if (!StringUtil.isNullOrBlank(subjectUuid)) {
      definition.addCondition(AccountDefrayals.CONDITION_SUBJECT_EQUALS, subjectUuid);
    }

    String coopMode = (String) filterMap.get("coopMode");
    if (!StringUtil.isNullOrBlank(coopMode)) {
      definition.addCondition(AccountDefrayals.CONDITION_COOPMODE_EQUALS, coopMode);
    }

    String sourceBillNumber = (String) filterMap.get("sourceBillNumber");
    if (!StringUtil.isNullOrBlank(sourceBillNumber)) {
      definition.addFlecsCondition(AccountDefrayals.FIELD_SOURCEBILLNUMBER,
          AccountDefrayals.OPERATOR_START_WITH, sourceBillNumber);
    }

    String sourceBillType = (String) filterMap.get("sourceBillType");
    if (!StringUtil.isNullOrBlank(sourceBillType)) {
      definition.addFlecsCondition(AccountDefrayals.FIELD_SOURCEBILLTYPE,
          AccountDefrayals.OPERATOR_EQUALS, sourceBillType);
    }
    return definition;
  }

  public void buildSort(FlecsQueryDefinition definition, QueryFilter filter) {
    List<OrderSort> sorts = filter.getSorts();
    for (OrderSort sort : sorts) {
      String property = sort.getProperty();
      QueryOrderDirection direction = QueryOrderDirection
          .valueOf(sort.getDirection().toLowerCase());
      if ("contract".equals(property)) {
        definition.addOrder(AccountDefrayals.ORDER_BY_CONTRACTNUMBER, direction);
      } else if ("counterpart".equals(property)) {
        definition.addOrder(AccountDefrayals.ORDER_BY_COUNTERPART, direction);
      } else if ("subject".equals(property)) {
        definition.addOrder(AccountDefrayals.ORDER_BY_SUBJECT, direction);
      } else if ("needSettle".equals(property)) {
        definition.addOrder(AccountDefrayals.ORDER_BY_NEEDSETTLE, direction);
      } else if ("settled".equals(property)) {
        definition.addOrder(AccountDefrayals.ORDER_BY_SETTLED, direction);
      } else if ("invoiced".equals(property)) {
        definition.addOrder(AccountDefrayals.ORDER_BY_INVOICED, direction);
      } else if ("lastPayDate".equals(property)) {
        definition.addOrder(AccountDefrayals.ORDER_BY_LASTPAYDATE, direction);
      } else if ("srcBill".equals(property)) {
        definition.addOrder(AccountDefrayals.ORDER_BY_SOURCEBILLNUMBER, direction);
      } else if ("direction".equals(property)) {
        definition.addOrder(AccountDefrayals.ORDER_BY_DIRECTION, direction);
      }
    }
  }
}
