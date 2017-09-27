/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	AccountingObjectBillQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月30日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.accobject.common;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.accobject.AccountingObjectBills;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;

/**
 * 核算主体单插叙条件构造器
 * 
 * @author LiBin
 *
 */
@Component
public class AccountingObjectBillQueryBuilder extends FlecsQueryDefinitionBuilder {

  private static final String FILTER_BILLNUMBER = "billNumber";
  private static final String FILTER_BIZSTATE = "bizState";
  private static final String FILTER_STORE_UUID = "storeUuid";
  private static final String FILTER_CONTRACT_UUID = "contractUuid";
  private static final String FILTER_SUBJECT = "subject";
  /** 查询条件(String)：核算主体。 */
  public static final String FILTER_ACCOBJECT = "accObjectUuid";

  /** =========================排序字段============================ */
  private static final String SORT_BILLNUMBER = "billNumber";
  private static final String SORT_BIZSTATE = "bizState";
  private static final String SORT_STORE = "store";
  private static final String SORT_LASTMODIFYINFO = "lastModifyInfo";
  private static final String SORT_REMARK = "remark";

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null)
      return;

    if (FILTER_BILLNUMBER.equals(fieldName)) {
      queryDef.addCondition(AccountingObjectBills.CONDITION_BILLNUMBER_STARTWITH, value);
    } else if (FILTER_BIZSTATE.equals(fieldName)) {
      if (value instanceof List) {
        for (Object state : ((List) value)) {
          queryDef.addCondition(AccountingObjectBills.CONDITION_BIZSTATE_EQUALS, state);
        }
      } else if (value instanceof String) {
        queryDef.addCondition(AccountingObjectBills.CONDITION_BIZSTATE_EQUALS, value);
      }
    } else if (FILTER_STORE_UUID.equals(fieldName)) {
      queryDef.addCondition(AccountingObjectBills.CONDITION_STORE_EQUALS, value);
    } else if (FILTER_CONTRACT_UUID.equals(fieldName)) {
      queryDef.addCondition(AccountingObjectBills.CONDITION_CONTRACT_EQUALS, value);
    } else if (FILTER_SUBJECT.equals(fieldName)) {
      queryDef.addCondition(AccountingObjectBills.CONDITION_SUBJECT_EQUALS, value);
    } else if (FILTER_ACCOBJECT.equals(fieldName)) {
      queryDef.addCondition(AccountingObjectBills.CONDITION_ACCOBJECT_EQUALS, value);
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null)
      return;

    if (SORT_BIZSTATE.equals(sort.getProperty())) {
      queryDef.addOrder(AccountingObjectBills.ORDER_BY_BIZSTATE, dir);
    } else if (SORT_BILLNUMBER.equals(sort.getProperty())) {
      queryDef.addOrder(AccountingObjectBills.ORDER_BY_BILLNUMBER, dir);
    } else if (SORT_STORE.equals(sort.getProperty())) {
      queryDef.addOrder(AccountingObjectBills.ORDER_BY_STORE, dir);
    } else if (SORT_REMARK.equals(sort.getProperty())) {
      queryDef.addOrder(AccountingObjectBills.ORDER_BY_REMARK, dir);
    } else if (SORT_LASTMODIFYINFO.equals(sort.getProperty())) {
      queryDef.addOrder(AccountingObjectBills.FIELD_LAST_MODIFY_TIME, dir);
    } else {
      super.buildSort(queryDef, sort, dir);
    }
  }

}
