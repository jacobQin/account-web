/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名： cre-account
 * 文件名： InvoiceInstockQueryBuilder.java
 * 模块说明：    
 * 修改历史：
 * 2017年3月22日 - huangjin - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.instock;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.ivc.instock.InvoiceInstocks;
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
public class InvoiceInstockQueryBuilder extends FlecsQueryDefinitionBuilder {
  private static final String FILTER_BILLNUMBER = "billNumber";
  private static final String FILTER_BIZSTATE = "bizState";
  private static final String FILTER_ACCOUNTUUIT = "accountUnit";
  private static final String FILTER_INSTOCKOR = "instockor";
  private static final String FILTER_INSTOCKDATE = "instockDate";
  private static final String FILTER_INVOICETYPE = "invoiceType";
  private static final String FILTER_INVOICECODE = "invoiceCode";
  private static final String FILTER_INVOICENUMBER = "invoiceNumber";

  /** =========================排序字段============================ */
  private static final String SORT_BILLNUMBER = "billNumber";
  private static final String SORT_BIZSTATE = "bizState";
  private static final String SORT_ACCOUNTUNIT = "accountUnit";
  private static final String SORT_INSTOCKOR = "instockor";
  private static final String SORT_INSTOCKDATE = "instockDate";
  private static final String SORT_INVOICETYPE = "invoiceType";


  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null)
      return;

    if (FILTER_BILLNUMBER.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceInstocks.FIELD_BILLNUMBER,InvoiceInstocks.OPERATOR_START_WITH, value);
    } else if (FILTER_BIZSTATE.equals(fieldName)) {
      if (value instanceof List) {
        for (Object state : ((List) value)) {
          queryDef.addFlecsCondition(InvoiceInstocks.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, state);
        }
      } else if (value instanceof String) {
        queryDef.addFlecsCondition(InvoiceInstocks.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, value);
      }
    } else if    (FILTER_ACCOUNTUUIT.equals(fieldName)) {
      queryDef.addCondition(InvoiceInstocks.CONDITION_STOREUUID_EQUALS, value);
    } else if (FILTER_INSTOCKOR.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceInstocks.FIELD_INSTOCKOR, InvoiceInstocks.OPERATOR_EQUALS, value);
    } else if (FILTER_INSTOCKDATE.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceInstocks.FIELD_INSTOCKDATE, InvoiceInstocks.OPERATOR_EQUALS,
          StringUtil.toDate(value.toString()));
    } else if (FILTER_INVOICETYPE.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceInstocks.FIELD_INVOICETYPE, InvoiceInstocks.OPERATOR_EQUALS,
          value);
    } else if (FILTER_INVOICECODE.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceInstocks.FIELD_INVOICECODE, InvoiceInstocks.OPERATOR_FUZZY_LIKE,
          value);
    } else if (FILTER_INVOICENUMBER.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceInstocks.FIELD_INVOICENUMBER, InvoiceInstocks.OPERATOR_FUZZY_LIKE,
          value);
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null)
      return;

    if (SORT_BIZSTATE.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceInstocks.ORDER_BY_BIZSTATE, dir);
    } else if (SORT_BILLNUMBER.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceInstocks.ORDER_BY_BILLNUMBER, dir);
    } else if (SORT_ACCOUNTUNIT.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceInstocks.ORDER_BY_STORE, dir);
    } else if (SORT_INSTOCKOR.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceInstocks.ORDER_BY_INSTOCKOR, dir);
    } else if (SORT_INSTOCKDATE.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceInstocks.ORDER_BY_INSTOCKDATE, dir);
    } else if (SORT_INVOICETYPE.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceInstocks.ORDER_BY_INVOICETYPE, dir);
    } else {
      super.buildSort(queryDef, sort, dir);
    }
  }
}
