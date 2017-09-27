/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名： cre-account-web
 * 文件名： InvoiceAbortQueryBuilder.java
 * 模块说明：    
 * 修改历史：
 * 2017年4月7日 - huangjin - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.abort;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.ivc.abort.InvoiceAborts;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.lang.StringUtil;
/**
 * 
 * @author huangjin
 *
 */
@Component
public class InvoiceAbortQueryBuilder  extends FlecsQueryDefinitionBuilder {
  private static final String FILTER_BILL_NUMBER = "billNumber";
  private static final String FILTER_BIZ_STATE = "bizState";
  private static final String FILTER_ACCOUNT_UNIT = "accountUnit";
  private static final String FILTER_INVOICE_TYPE = "invoiceType";
  private static final String FILTER_INVOICE_CODE = "invoiceCode";
  private static final String FILTER_INVOICE_NUMBER = "invoiceNumber";
  private static final String FILTER_ABORTER= "aborter";
  private static final String FILTER_ABORTDATE = "abortDate";

  private static final String SORT_BILL_NUMBER = "billNumber";
  private static final String SORT_BIZ_STATE = "bizState";
  private static final String SORT_INVOICE_TYPE = "invoiceType";
  private static final String SORT_ACCOUNT_UNIT = "accountUnit";
  private static final String SORT_ABORTER = "aborter";
  private static final String SORT_ABORT_DATE = "abortDate";

  
  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null)
      return;

    if (FILTER_BILL_NUMBER.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceAborts.FIELD_BILLNUMBER,InvoiceAborts.OPERATOR_START_WITH, value);
    } else if (FILTER_BIZ_STATE.equals(fieldName)) {
      if (value instanceof List) {
        for (Object state : ((List) value)) {
          queryDef
              .addFlecsCondition(InvoiceAborts.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, state);
        }
      } else if (value instanceof String) {
        queryDef.addFlecsCondition(InvoiceAborts.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, value);
      }
    } else if    (FILTER_ACCOUNT_UNIT.equals(fieldName)) {
      queryDef.addCondition(InvoiceAborts.CONDITION_STOREUUID_EQUALS, value);
    } else if (FILTER_ABORTER.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceAborts.FIELD_ABORTER, InvoiceAborts.OPERATOR_EQUALS, value);
    } else if (FILTER_ABORTDATE.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceAborts.FIELD_ABORTDATE, InvoiceAborts.OPERATOR_EQUALS,
          StringUtil.toDate(value.toString()));
    } else if (FILTER_INVOICE_TYPE.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceAborts.FIELD_INVOICETYPE, InvoiceAborts.OPERATOR_EQUALS,
          value);
    } else if (FILTER_INVOICE_CODE.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceAborts.FIELD_INVOICECODE, InvoiceAborts.OPERATOR_FUZZY_LIKE,
          value);
    } else if (FILTER_INVOICE_NUMBER.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceAborts.FIELD_INVOICENUMBER, InvoiceAborts.OPERATOR_FUZZY_LIKE,
          value);
    }else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null)
      return;

    if (SORT_BIZ_STATE.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceAborts.ORDER_BY_BIZSTATE, dir);
    } else if (SORT_BILL_NUMBER.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceAborts.ORDER_BY_BILLNUMBER, dir);
    } else if (SORT_ACCOUNT_UNIT.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceAborts.ORDER_BY_STORE, dir);
    } else if (SORT_ABORTER.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceAborts.ORDER_BY_ABORTER, dir);
    } else if (SORT_ABORT_DATE.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceAborts.ORDER_BY_ABORTDATE, dir);
    } else if (SORT_INVOICE_TYPE.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceAborts.ORDER_BY_INVOICETYPE, dir);
    } else {
      super.buildSort(queryDef, sort, dir);
    }
  }
}
