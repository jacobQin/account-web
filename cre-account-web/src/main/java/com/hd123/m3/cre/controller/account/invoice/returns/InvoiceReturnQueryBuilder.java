/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceReturnQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月30日 - xiahongjian - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.returns;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.ivc.returns.InvoiceReturns;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author xiahongjian
 *
 */
@Component
public class InvoiceReturnQueryBuilder extends FlecsQueryDefinitionBuilder {
  private static final String FILTER_BILL_NUMBER = "billNumber";
  private static final String FILTER_BIZ_STATE = "bizState";
  private static final String FILTER_ACCOUNT_UNIT = "accountUnit";
  private static final String FILTER_INVOICE_TYPE = "invoiceType";
  private static final String FILTER_INVOICE_CODE = "invoiceCode";
  private static final String FILTER_INVOICE_NUMBER = "invoiceNumber";
  private static final String FILTER_RECEIVER = "receiver";
  private static final String FILTER_RETURNOR = "returnor";
  private static final String FILTER_RETURN_DATE = "returnDate";

  private static final String SORT_BILL_NUMBER = "billNumber";
  private static final String SORT_BIZ_STATE = "bizState";
  private static final String SORT_INVOICE_TYPE = "invoiceType";
  private static final String SORT_ACCOUNT_UNIT = "accountUnit";
  private static final String SORT_RECEIVER = "receiver";
  private static final String SORT_RETURNOR = "returnor";
  private static final String SORT_RETURN_DATE = "returnDate";
  private static final String SORT_REMARK = "reamrk";

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null)
      return;

    if (FILTER_BILL_NUMBER.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceReturns.FIELD_BILLNUMBER,
          InvoiceReturns.OPERATOR_START_WITH, value);
      return;
    }

    if (FILTER_BIZ_STATE.equals(fieldName)) {
      if (value instanceof List) {
        for (Object state : (List) value)
          queryDef.addFlecsCondition(InvoiceReturns.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, state);
      } else {
        queryDef.addFlecsCondition(InvoiceReturns.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, value);
      }
      return;
    }

    if (FILTER_ACCOUNT_UNIT.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceReturns.FIELD_STORE, InvoiceReturns.OPERATOR_EQUALS, value);
      return;
    }

    if (FILTER_INVOICE_TYPE.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceReturns.FIELD_INVOICETYPE, InvoiceReturns.OPERATOR_EQUALS,
          value);
      return;
    }

    if (FILTER_INVOICE_CODE.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceReturns.FIELD_INVOICECODE,
          InvoiceReturns.OPERATOR_EQUALS, value.toString().trim());
      return;
    }

    if (FILTER_INVOICE_NUMBER.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceReturns.FIELD_INVOICENUMBER,
          InvoiceReturns.OPERATOR_EQUALS, value.toString().trim());
      return;
    }

    if (FILTER_RECEIVER.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceReturns.FIELD_RECEIVER, InvoiceReturns.OPERATOR_EQUALS,
          value);
      return;
    }

    if (FILTER_RETURNOR.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceReturns.FIELD_RETURNOR, InvoiceReturns.OPERATOR_EQUALS,
          value);
      return;
    }

    if (FILTER_RETURN_DATE.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceReturns.FIELD_RETURNDATE, InvoiceReturns.OPERATOR_EQUALS,
          StringUtil.toDate(value.toString()));
      return;
    }
    super.buildFilter(queryDef, fieldName, value);
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null)
      return;
    String prop = sort.getProperty();
    if (SORT_BILL_NUMBER.equals(prop)) {
      queryDef.addOrder(InvoiceReturns.ORDER_BY_BILLNUMBER, dir);
      return;
    }

    if (SORT_BIZ_STATE.equals(prop)) {
      queryDef.addOrder(InvoiceReturns.ORDER_BY_BIZSTATE, dir);
      return;
    }

    if (SORT_INVOICE_TYPE.equals(prop)) {
      queryDef.addOrder(InvoiceReturns.ORDER_BY_INVOICETYPE, dir);
      return;
    }

    if (SORT_ACCOUNT_UNIT.equals(prop)) {
      queryDef.addOrder(InvoiceReturns.ORDER_BY_STORE, dir);
      return;
    }

    if (SORT_RECEIVER.equals(prop)) {
      queryDef.addOrder(InvoiceReturns.ORDER_BY_RECEIVER, dir);
      return;
    }

    if (SORT_RETURNOR.equals(prop)) {
      queryDef.addOrder(InvoiceReturns.ORDER_BY_RETURNOR, dir);
      return;
    }

    if (SORT_RETURN_DATE.equals(prop)) {
      queryDef.addOrder(InvoiceReturns.ORDER_BY_RETURNDATE, dir);
      return;
    }

    if (SORT_REMARK.equals(prop)) {
      queryDef.addOrder(InvoiceReturns.ORDER_BY_REMARK, dir);
      return;
    }
    super.buildSort(queryDef, sort, dir);
  }

}
