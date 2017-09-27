/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	SalesInputQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月10日 - cRazy - 创建。
 */
package com.hd123.m3.cre.controller.account.fee;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.fee.Fees;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author cRazy
 *
 */

@Component
public class FeeQueryBuilder extends FlecsQueryDefinitionBuilder {

  private static final String FILTER_BILLNUMBER = "billNumber";
  private static final String FILTER_BIZSTATE = "bizState";
  private static final String FILTER_STORE_UUID = "storeUuid";
  private static final String FILTER_COUNTERPART_UUID = "counterpartUuid";
  private static final String FILTER_CONTRACT_UUID = "contractUuid";
  private static final String FILTER_SETTLENO = "settleNo";
  private static final String FILTER_ACCOUNTDATE = "accountDate";
  private static final String FILTER_COOPMODE = "coopMode";
  private static final String FILTER_SUBJECT = "subject";

  /** =========================排序字段============================ */
  private static final String SORT_BILLNUMBER = "billNumber";
  private static final String SORT_BIZSTATE = "bizState";
  private static final String SORT_STORE = "store";
  private static final String SORT_CONTRACT = "contract";
  private static final String SORT_COUNTERPART = "counterpart";
  private static final String SORT_ACCOUNTDATE = "accountDate";
  private static final String SORT_CONTRACT_SIGNBOARD = "signboard";
  private static final String SORT_TOTAL = "total";
  private static final String SORT_SETTLENO = "settleNo";

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null)
      return;

    if (FILTER_BILLNUMBER.equals(fieldName)) {
      queryDef.addCondition(Fees.CONDITION_BILLNUMBER_STARTWITH, value);
    } else if (FILTER_BIZSTATE.equals(fieldName)) {
      if (value instanceof List) {
        for (Object state : ((List) value)) {
          queryDef.addFlecsCondition(Fees.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, state);
        }
      } else if (value instanceof String) {
        queryDef.addFlecsCondition(Fees.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, value);
      }
    } else if (FILTER_STORE_UUID.equals(fieldName)) {
      queryDef.addFlecsCondition(Fees.FIELD_ACCOUNTUNIT, Fees.OPERATOR_EQUALS, value);
    } else if (FILTER_ACCOUNTDATE.equals(fieldName)) {
      queryDef.addFlecsCondition(Fees.FIELD_ACCOUNTDATE, Fees.OPERATOR_EQUALS,
          StringUtil.toDate(value.toString()));
    } else if (FILTER_CONTRACT_UUID.equals(fieldName)) {
      queryDef.addCondition(Fees.CONDITION_CONTRACT_EQUALS, value);
    } else if (FILTER_COOPMODE.equals(fieldName)) {
      queryDef.addFlecsCondition(Fees.FIELD_COOPMODE, Fees.OPERATOR_EQUALS, value);
    } else if (FILTER_COUNTERPART_UUID.equals(fieldName)) {
      queryDef.addFlecsCondition(Fees.FIELD_COUNTERPART, Fees.OPERATOR_EQUALS, value);
    } else if (FILTER_SETTLENO.equals(fieldName)) {
      queryDef.addFlecsCondition(Fees.FIELD_SETTLENO, Fees.OPERATOR_EQUALS, value);
    } else if (FILTER_SUBJECT.equals(fieldName)) {
      queryDef.addFlecsCondition(Fees.FIELD_SUBJECT, Fees.OPERATOR_EQUALS, value);
    } else if ("position".equals(fieldName)) {
      queryDef.addCondition(Fees.CONDITION_POSITION_UUID_EQUALS, value);
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null)
      return;

    if (SORT_STORE.equals(sort.getProperty())) {
      queryDef.addOrder(Fees.ORDER_BY_ACCOUNTUNIT, dir);
    } else if (SORT_CONTRACT.equals(sort.getProperty())) {
      queryDef.addOrder(Fees.ORDER_BY_CONTRACT_NUMBER, dir);
    } else if (SORT_ACCOUNTDATE.equals(sort.getProperty())) {
      queryDef.addOrder(Fees.ORDER_BY_ACCOUNTDATE, dir);
    } else if (SORT_BIZSTATE.equals(sort.getProperty())) {
      queryDef.addOrder(Fees.ORDER_BY_BIZSTATE, dir);
    } else if (SORT_CONTRACT_SIGNBOARD.equals(sort.getProperty())) {
      queryDef.addOrder(Fees.ORDER_BY_CONTRACT_NAME, dir);
    } else if (SORT_TOTAL.equals(sort.getProperty())) {
      queryDef.addOrder(Fees.ORDER_BY_TOTAL, dir);
    } else if (SORT_BILLNUMBER.equals(sort.getProperty())) {
      queryDef.addOrder(Fees.ORDER_BY_BILLNUMBER, dir);
    } else if (SORT_COUNTERPART.equals(sort.getProperty())) {
      queryDef.addOrder(Fees.ORDER_BY_COUNTERPART, dir);
    } else if (SORT_SETTLENO.equals(sort.getProperty())) {
      queryDef.addOrder(Fees.ORDER_BY_SETTLENO, dir);
    } else {
      super.buildSort(queryDef, sort, dir);
    }
  }
}
