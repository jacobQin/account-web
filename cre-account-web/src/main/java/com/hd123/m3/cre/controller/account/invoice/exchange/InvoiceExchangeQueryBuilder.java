/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceExchangeQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月29日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.exchange;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.ivc.exchange.InvoiceExchanges;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author wangyibo
 *
 */
@Component
public class InvoiceExchangeQueryBuilder extends FlecsQueryDefinitionBuilder {

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null) {
      return;
    }

    if ("billNumber".equals(fieldName)) {
      String number = value.toString();
      if (!StringUtil.isNullOrBlank(number)) {
        queryDef.addFlecsCondition(InvoiceExchanges.FIELD_BILLNUMBER,
            InvoiceExchanges.OPERATOR_START_WITH, number);
      }
    } else if ("bizState".equals(fieldName)) {
      String bizState = "";
      if (value instanceof List) {
        for (Object object : (List) value) {
          queryDef.addFlecsCondition(InvoiceExchanges.FIELD_BIZSTATE,
              InvoiceExchanges.OPERATOR_EQUALS, object);
        }
      } else if (value instanceof String) {
        bizState = (String) value;
      }
      queryDef.addFlecsCondition(InvoiceExchanges.FIELD_BIZSTATE, InvoiceExchanges.OPERATOR_EQUALS,
          bizState);
    } else if ("store".equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceExchanges.FIELD_STORE, InvoiceExchanges.OPERATOR_EQUALS,
          value);
    } else if ("exchanger".equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceExchanges.FIELD_EXCHANGER,
          InvoiceExchanges.OPERATOR_EQUALS, value);
    } else if ("oldCode".equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceExchanges.FIELD_INVOICECODE,
          InvoiceExchanges.OPERATOR_FUZZY_LIKE, value);
    } else if ("oldNumber".equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceExchanges.FIELD_INVOICENUMBER,
          InvoiceExchanges.OPERATOR_FUZZY_LIKE, value);
    } else if ("exchangeDate".equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceExchanges.FIELD_EXCHANGEDATE,
          InvoiceExchanges.OPERATOR_EQUALS, StringUtil.toDate(value.toString()));
    } else if ("type".equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceExchanges.FIELD_EXCHANGETYPE,
          InvoiceExchanges.OPERATOR_EQUALS, value);
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort,
      QueryOrderDirection direction) {
    if (queryDef == null || sort == null) {
      return;
    }

    if ("billNumber".equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceExchanges.ORDER_BY_BILLNUMBER, direction);
    } else if ("bizState".equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceExchanges.ORDER_BY_BIZSTATE, direction);
    } else if ("type".equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceExchanges.ORDER_BY_EXCHANGETYPE, direction);
    } else if ("accountUnit".equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceExchanges.ORDER_BY_STORE, direction);
    } else if ("exchanger".equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceExchanges.ORDER_BY_EXCHANGER, direction);
    } else if ("exchangeDate".equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceExchanges.ORDER_BY_EXCHANGEDATE, direction);
    } else if ("remark".equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceExchanges.ORDER_BY_REMARK, direction);
    } else {
      super.buildSort(queryDef, sort, direction);
    }
  }
}
