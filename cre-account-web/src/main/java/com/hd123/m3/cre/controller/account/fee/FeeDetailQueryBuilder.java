/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	FeeLineQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月19日 - lizongyi - 创建。
 */
package com.hd123.m3.cre.controller.account.fee;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.fee.FeeDetails;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;

/**
 * @author lizongyi
 *
 */
@Component
public class FeeDetailQueryBuilder extends FlecsQueryDefinitionBuilder {

  private static final String FILTER_BIZSTATE = "bizState";
  private static final String FILTER_STORE_UUID = "storeUuid";
  private static final String FILTER_CONTRACT_UUID = "contractUuid";
  private static final String FILTER_SUBJECT = "subject";

  /** =========================排序字段============================ */
  private static final String SORT_BIZSTATE = "bizState";
  private static final String SORT_STORE = "store";
  private static final String SORT_CONTRACT = "contract";
  private static final String SORT_COUNTERPART = "counterpart";
  private static final String SORT_SUBJECT = "subject";
  private static final String SORT_DATERANGE = "dateRange";
  private static final String SORT_TOTAL = "total";
  private static final String SORT_BEGINDATE = "beginDate";

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null)
      return;
    if (FILTER_BIZSTATE.equals(fieldName)) {
      if (value instanceof List) {
        for (Object state : ((List) value)) {
          queryDef.addCondition(FeeDetails.CONDITION_BIZSTATE_EQUALS, state);
        }
      } else if (value instanceof String) {
        queryDef.addCondition(FeeDetails.CONDITION_BIZSTATE_EQUALS, value);
      }
    } else if (FILTER_STORE_UUID.equals(fieldName)) {
      queryDef.addCondition(FeeDetails.CONDITION_ACCOUNTUNIT_EQUALS, value);
    } else if (FILTER_CONTRACT_UUID.equals(fieldName)) {
      queryDef.addCondition(FeeDetails.CONDITION_CONTRACT_EQUALS, value);
    } else if (FILTER_SUBJECT.equals(fieldName)) {
      queryDef.addCondition(FeeDetails.CONDITION_SUBJECT_EQUALS, value);
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null)
      return;

    if (SORT_STORE.equals(sort.getProperty())) {
      queryDef.addOrder(FeeDetails.ORDER_BY_ACCOUNTUNIT, dir);
    } else if (SORT_CONTRACT.equals(sort.getProperty())) {
      queryDef.addOrder(FeeDetails.ORDER_BY_CONTRACT_NUMBER, dir);
    } else if (SORT_COUNTERPART.equals(sort.getProperty())) {
      queryDef.addOrder(FeeDetails.ORDER_BY_COUNTERPART, dir);
    } else if (SORT_BIZSTATE.equals(sort.getProperty())) {
      queryDef.addOrder(FeeDetails.ORDER_BY_BIZSTATE, dir);
    } else if (SORT_SUBJECT.equals(sort.getProperty())) {
      queryDef.addOrder(FeeDetails.ORDER_BY_SUBJECT, dir);
    } else if (SORT_DATERANGE.equals(sort.getProperty())) {
      queryDef.addOrder(FeeDetails.ORDER_BY_DATERANGE, dir);
    } else if (SORT_TOTAL.equals(sort.getProperty())) {
      queryDef.addOrder(FeeDetails.ORDER_BY_TOTAL, dir);
    } else if (SORT_BEGINDATE.equals(sort.getProperty())) {
      queryDef.addOrder(FeeDetails.ORDER_BY_DATERANGE, dir);
    } else {
      super.buildSort(queryDef, sort, dir);
    }
  }
}
