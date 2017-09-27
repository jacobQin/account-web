/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceStockQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月27日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.stock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.ivc.stock.InvoiceStocks;
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
public class InvoiceStockQueryBuilder extends FlecsQueryDefinitionBuilder {

  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null) {
      return;
    }

    if ("invoiceCode".equals(fieldName)) {
      String code = value.toString();
      if (StringUtil.isNullOrBlank(code)) {
        return;
      } else {
        queryDef.addFlecsCondition(InvoiceStocks.FIELD_INVOICECODE,
            InvoiceStocks.OPERATOR_START_WITH, value);
      }
    } else if ("invoiceNumber".equals(fieldName)) {
      String code = value.toString();
      if (StringUtil.isNullOrBlank(code)) {
        return;
      } else if (value instanceof List) {
        queryDef.addCondition(InvoiceStocks.CONDITION_INVOICENUMBER_IN, ((List) value).toArray());
      }  else {
        queryDef.addFlecsCondition(InvoiceStocks.FIELD_INVOICENUMBER,
            InvoiceStocks.OPERATOR_START_WITH, value);        
      }
    } else if ("store".equals(fieldName) || "storeUuid".equals(fieldName)) {
      if(value instanceof List) {
        for (Object storeUuid : (List)value) {
          if(storeUuid == null) {
            queryDef.addFlecsCondition(InvoiceStocks.FIELD_STORE, InvoiceStocks.OPERATOR_IS_NULL, storeUuid);
          }
          queryDef.addFlecsCondition(InvoiceStocks.FIELD_STORE, InvoiceStocks.OPERATOR_EQUALS, storeUuid);
        }
      } else if (value instanceof String) {
        queryDef.addFlecsCondition(InvoiceStocks.FIELD_STORE, InvoiceStocks.OPERATOR_IS_NULL, value);
      }
      
      queryDef.addFlecsCondition(InvoiceStocks.FIELD_STORE, InvoiceStocks.OPERATOR_EQUALS, value);
    } else if ("date".equals(fieldName)) {
      Date date = StringUtil.toDate((String) value);
      queryDef
          .addFlecsCondition(InvoiceStocks.FIELD_ABORTDATE, InvoiceStocks.OPERATOR_EQUALS, date);
    } else if ("state".equals(fieldName)) {

      if (value instanceof List) {
        for (Object state : (List) value) {
            queryDef.addCondition(InvoiceStocks.CONDITION_INVOICESTATE_EQUAL, state);
        }
      } else if (value instanceof String) {
          queryDef.addCondition(InvoiceStocks.CONDITION_INVOICESTATE_EQUAL, value);
      }
    } else if ("invoiceType".equals(fieldName)) {
      if (value instanceof List) {
        for (Object invoiceType : (List) value) {
          if(!invoiceType.equals("all")) {
          queryDef.addCondition(InvoiceStocks.CONDITION_INVOICETYPE_EQUAL, invoiceType);
          }
        }
      } else if (value instanceof String) {
        if(!value.equals("all")){
        queryDef.addCondition(InvoiceStocks.CONDITION_INVOICETYPE_EQUAL, value);
        }
      }
    } else if ("useType".equals(fieldName)) {
      if (value instanceof List) {
        for (Object useType : (List) value) {
          queryDef.addCondition(InvoiceStocks.CONDITION_USETYPE_EQUAL, useType);
        }
      } else if (value instanceof Integer) {
        queryDef.addCondition(InvoiceStocks.CONDITION_USETYPE_EQUAL, value);
      }
    } else if ("expects".equals(fieldName)) {
      List<String> numbers = new ArrayList<String>();
      if(value instanceof List) {
        for (String number : (List<String>)value) {
          numbers.add(number);
        }
      queryDef.addCondition(InvoiceStocks.CONDITION_INVOICENUMBER_NOT_IN, numbers.toArray());
      }
    } else if ("numbers".equals(fieldName)) {
      List<String> numbers = new ArrayList<String>();
      if(value instanceof List) {
        for (String number : (List<String>)value) {
          numbers.add(number);
        }
      queryDef.addCondition(InvoiceStocks.CONDITION_INVOICENUMBER_IN, numbers.toArray());
      }
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort,
      QueryOrderDirection direction) {
    if ("invoiceType".equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceStocks.ORDER_BY_INVOICETYPE, direction);
    } else if ("invoiceCode".equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceStocks.ORDER_BY_INVOICECODE, direction);
    } else if ("invoiceNumber".equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceStocks.ORDER_BY_INVOICENUMBER, direction);
    } else if ("state".equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceStocks.ORDER_BY_STATE, direction);
    } else if ("remark".equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceStocks.ORDER_BY_REMARK, direction);
    } else if ("accountUnit".equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceStocks.ORDER_BY_STORE, direction);
    } else if ("holder".equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceStocks.ORDER_BY_HOLDER, direction);
    } else if ("amount".equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceStocks.ORDER_BY_AMOUNT, direction);
    } else {
      super.buildSort(queryDef, sort, direction);
    }
  }

}
