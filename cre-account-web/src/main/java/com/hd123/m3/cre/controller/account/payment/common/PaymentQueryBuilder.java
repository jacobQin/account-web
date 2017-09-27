/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	PaymentQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月14日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.payment.common;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.payment.Payments;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 收付款单查询条件构造器
 * 
 * @author LiBin
 *
 */
@Component
public class PaymentQueryBuilder extends FlecsQueryDefinitionBuilder {

  private static final String FILTER_BIZSTATE = "bizState";

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null) {
      return;
    }

    if (Payments.CONDITION_DIRECTION_EQUALS.equals(fieldName)) {
      queryDef.addCondition(Payments.CONDITION_DIRECTION_EQUALS, Integer.valueOf(value.toString()));
    } else if (FILTER_BIZSTATE.equals(fieldName)) {
      if (value instanceof List) {
        for (Object state : ((List) value)) {
          queryDef.addFlecsCondition(Payments.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, state);
        }
      } else if (value instanceof String) {
        queryDef.addFlecsCondition(Payments.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, value);
      }
    } else if (PaymentConstants.FILTER_BILLNUMBER.equals(fieldName)) {
      queryDef.addFlecsCondition(Payments.FIELD_BILLNUMBER, Payments.OPERATOR_START_WITH, value);
    } else if (PaymentConstants.FILTER_STORE_UUID.equals(fieldName)) {
      queryDef.addFlecsCondition(Payments.FIELD_ACCOUNTCENTER, Payments.OPERATOR_EQUALS, value);
    } else if (PaymentConstants.FILTER_CONTRACT_UUID.equals(fieldName)) {
      queryDef.addFlecsCondition(Payments.FIELD_CONTRACTUUID, Payments.OPERATOR_EQUALS, value);
    } else if (PaymentConstants.FILTER_COUNTERPART_UUID.equals(fieldName)) {
      queryDef.addCondition(Payments.CONDITION_COUNTERPARTCENTER_EQUALS, value);
    } else if (PaymentConstants.FILTER_COOPMODE.equals(fieldName)) {
      queryDef.addFlecsCondition(Payments.FIELD_COOPMODE, Payments.OPERATOR_EQUALS, value);
    } else if (PaymentConstants.FILTER_PAYMENTDATE.equals(fieldName)) {
      queryDef.addFlecsCondition(Payments.FIELD_PAYMENTDATE, Payments.OPERATOR_EQUALS,
          StringUtil.toDate(value.toString()));
    } else if (PaymentConstants.FILTER_SETTLENO.equals(queryDef)) {
      queryDef.addFlecsCondition(Payments.FIELD_SETTLENO, Payments.OPERATOR_EQUALS, value);
    } else if ("position".equals(fieldName)) {
      queryDef.addCondition(Payments.CONDITION_POSITION_UUID_EQUALS, value);
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null)
      return;

    if (PaymentConstants.SORT_STORE.equals(sort.getProperty())) {
      queryDef.addOrder(Payments.ORDER_BY_ACCOUNTCENTERCODE, dir);
    } else if (PaymentConstants.SORT_BILLNUMBER.equals(sort.getProperty())) {
      queryDef.addOrder(Payments.ORDER_BY_BILLNUMBER, dir);
    } else if (PaymentConstants.SORT_BIZSTATE.equals(sort.getProperty())) {
      queryDef.addOrder(Payments.ORDER_BY_BIZSTATE, dir);
    } else if (PaymentConstants.SORT_COUNTERPART.equals(sort.getProperty())) {
      queryDef.addOrder(Payments.ORDER_BY_COUNTERPARTCENTERCODE, dir);
    } else if (PaymentConstants.SORT_PAYMENTDATE.equals(sort.getProperty())) {
      queryDef.addOrder(Payments.ORDER_BY_PAYMENTDATE, dir);
    } else if (PaymentConstants.SORT_UNPAYEDTOTAL.equals(sort.getProperty())) {
      queryDef.addOrder(Payments.ORDER_BY_UNPAYEDTOTAL, dir);
    } else if (PaymentConstants.SORT_TOTAL.equals(sort.getProperty())) {
      queryDef.addOrder(Payments.ORDER_BY_TOTAL, dir);
    } else if (PaymentConstants.SORT_SETTLENO.equals(sort.getProperty())) {
      queryDef.addOrder(Payments.ORDER_BY_SETTLENO, dir);
    } else {
      super.buildSort(queryDef, sort, dir);
    }
  }
}
