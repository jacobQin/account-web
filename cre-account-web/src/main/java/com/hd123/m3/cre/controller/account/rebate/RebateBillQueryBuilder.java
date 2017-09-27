/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	RebateBillQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月15日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.rebate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.rebate.RebateBills;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.query.ValueRange;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.sales.service.moonstar.order2.OrderState;
import com.hd123.m3.sales.service.moonstar.order2.Orders;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.webframe.session.Session;

/**
 * @author chenganbang
 *
 */
@Component
public class RebateBillQueryBuilder extends FlecsQueryDefinitionBuilder {

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null) {
      return;
    }
    if ("bizState".equals(fieldName)) {
      if (value instanceof List && !((List) value).isEmpty()) {
        for (Object val : (List) value) {
          queryDef.addFlecsCondition(RebateBills.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, val);
        }
      } else if (value instanceof String) {
        queryDef.addFlecsCondition(RebateBills.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, value);
      }
    } else if ("billNumber".equals(fieldName)) {
      queryDef.addCondition(RebateBills.CONDITION_BILLNUMBER_STARTWITH, value);
    } else if ("storeUuid".equals(fieldName)) {
      queryDef.addCondition(RebateBills.CONDITION_STOREUUID_EQUALS, value);
    } else if ("tenantUuid".equals(fieldName)) {
      queryDef.addFlecsCondition(RebateBills.FIELD_TENANT, Basices.OPERATOR_EQUALS, value);
    } else if ("contractUuid".equals(fieldName)) {
      queryDef.addCondition(RebateBills.CONDITION_CONTRACTUUID_EQUALS, value);
    } else if ("positionCode".equals(fieldName)) {
      queryDef.addFlecsCondition(RebateBills.FIELD_POSITION, Basices.OPERATOR_INCLUDE, value);
    } else if ("accountDate".equals(fieldName)) {
      queryDef.addFlecsCondition(RebateBills.FIELD_AccountDate, Basices.OPERATOR_EQUALS,
          StringUtil.toDate((String) value, "yyyy-MM-dd"));
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null) {
      return;
    }
    String propety = sort.getProperty();
    if ("store".equals(propety)) {
      queryDef.addOrder(RebateBills.ORDER_BY_STORE, dir);
    } else if ("tenant".equals(propety)) {
      queryDef.addOrder(RebateBills.ORDER_BY_TENANT, dir);
    } else if ("contract".equals(propety)) {
      queryDef.addOrder(RebateBills.ORDER_BY_CONTRACT, dir);
    } else if ("rebateTotal".equals(propety)) {
      queryDef.addOrder(RebateBills.ORDER_BY_BACKTOTAL, dir);
    } else if ("dateRange".equals(propety)) {
      queryDef.addOrder(RebateBills.ORDER_BY_BEGINDATE, dir);
      queryDef.addOrder(RebateBills.ORDER_BY_ENDDATE, dir);
    } else {
      super.buildSort(queryDef, sort, dir);
    }
  }

  public FlecsQueryDefinition build4Query(QueryFilter queryFilter) throws Exception {
    if (queryFilter == null) {
      return null;
    }
    FlecsQueryDefinition definition = new FlecsQueryDefinition();
    definition.addFlecsCondition(Orders.FIELD_BIZSTATE, Basices.OPERATOR_NOTEQUALS,
        OrderState.aborted.name());
    definition.addCondition(Orders.CONDITION_ACCOUNTTOTAL_BETWEEN, BigDecimal.ZERO, null);
    Map<String, Object> filter = queryFilter.getFilter();
    if (filter.get("contractUuid") != null) {
      definition.addFlecsCondition(Orders.FIELD_CONTRACT_UUID, Basices.OPERATOR_EQUALS,
          filter.get("contractUuid"));
    }
    if (filter.get("storeUuid") != null) {
      definition.addFlecsCondition(Orders.FIELD_STORE, Basices.OPERATOR_EQUALS,
          filter.get("storeUuid"));
    }
    if (filter.get("billNumber") != null) {
      definition.addFlecsCondition(Orders.FIELD_BILLNUMBER, Basices.OPERATOR_START_WITH,
          filter.get("billNumber"));
    }
    Object payDate = filter.get("payDate");
    if (payDate != null) {
      if (payDate instanceof String) {
        definition.addFlecsCondition(Orders.FIELD_PAY_DATE, Basices.OPERATOR_EQUALS,
            StringUtil.toDate((String) filter.get("payDate"), "yyyy-MM-dd HH:mm:ss"));
      } else if (payDate instanceof LinkedHashMap) {
        LinkedHashMap<Object, Object> payDateMap = (LinkedHashMap<Object, Object>) payDate;
        Date beginDate = StringUtil.toDate(payDateMap.get("beginDate").toString(),
            "yyyy-MM-dd HH:mm:ss");
        Date endDate = StringUtil.toDate(payDateMap.get("endDate").toString(),
            "yyyy-MM-dd HH:mm:ss");
        ValueRange range = new ValueRange(beginDate, endDate);
        definition.addFlecsCondition(Orders.FIELD_PAY_DATE, Basices.OPERATOR_BETWEEN, range);
      }
    }

    // definition.addOrder(ProductRpts.SRCBILLCODE_ORDER,
    // QueryOrderDirection.asc);
    definition.setPage(queryFilter.getCurrentPage());
    definition.setPageSize(queryFilter.getPageSize());
    return definition;
  }

  protected String getSessionUserId() {
    Session session = Session.getInstance();
    IsOperator user = session != null ? session.getCurrentUser() : null;
    return user != null ? user.getId() : null;
  }
}
