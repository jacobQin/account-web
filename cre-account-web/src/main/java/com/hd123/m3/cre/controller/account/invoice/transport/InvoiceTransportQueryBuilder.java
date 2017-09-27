/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceTransportQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月12日 - xiahongjian - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.transport;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.ivc.transport.InvoiceTransports;
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
public class InvoiceTransportQueryBuilder extends FlecsQueryDefinitionBuilder {
  private static final String FILTER_BILL_NUMBER = "billNumber";
  private static final String FILTER_BIZ_STATE = "bizState";
  private static final String FILTER_ACCOUNT_UNIT = "accountUnit";
  private static final String FILTER_INVOICE_TYPE = "invoiceType";
  private static final String FILTER_INVOICE_CODE = "invoiceCode";
  private static final String FILTER_INVOICE_NUMBER = "invoiceNumber";
  private static final String FILTER_TRANSPORTOR = "transportor";
  private static final String FILTER_TRANSPORT_DATE = "transportDate";
  private static final String FILTER_IN_ACCOUNTUNIT = "inAccountUnit";
  private static final String FILTER_RECEIVER = "receiver";

  private static final String SORT_BILL_NUMBER = "billNumber";
  private static final String SORT_BIZ_STATE = "bizState";
  private static final String SORT_INVOICE_TYPE = "invoiceType";
  private static final String SORT_ACCOUNT_UNIT = "accountUnit";
  private static final String SORT_TRANSPORTOR = "transportor";
  private static final String SORT_TRANSPORT_DATE = "transportDate";
  private static final String SORT_REMARK = "reamrk";

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null)
      return;

    if (FILTER_BILL_NUMBER.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceTransports.FIELD_BILLNUMBER,
          InvoiceTransports.OPERATOR_START_WITH, value);
    } else if (FILTER_BIZ_STATE.equals(fieldName)) {
      if (value instanceof List) {
        for (Object state : (List) value)
          queryDef.addFlecsCondition(InvoiceTransports.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS,
              state);
      } else {
        queryDef
            .addFlecsCondition(InvoiceTransports.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, value);
      }
    } else if (FILTER_ACCOUNT_UNIT.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceTransports.FIELD_STORE, InvoiceTransports.OPERATOR_EQUALS,
          value);
    } else if (FILTER_INVOICE_TYPE.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceTransports.FIELD_INVOICETYPE,
          InvoiceTransports.OPERATOR_EQUALS, value);
    } else if (FILTER_INVOICE_CODE.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceTransports.FIELD_INVOICECODE,
          InvoiceTransports.OPERATOR_EQUALS, value.toString().trim());
    } else if (FILTER_INVOICE_NUMBER.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceTransports.FIELD_INVOICENUMBER,
          InvoiceTransports.OPERATOR_EQUALS, value.toString().trim());
    } else if (FILTER_TRANSPORTOR.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceTransports.FIELD_TRANSPORTOR,
          InvoiceTransports.OPERATOR_EQUALS, value);
    } else if (FILTER_TRANSPORT_DATE.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceTransports.FIELD_TRANSPORTDATE,
          InvoiceTransports.OPERATOR_EQUALS, StringUtil.toDate(value.toString()));
    } else if (FILTER_IN_ACCOUNTUNIT.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceTransports.FIELD_IN_ACCOUNTUNIT,
          InvoiceTransports.OPERATOR_INCLUDE, value);
    } else if (FILTER_RECEIVER.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceTransports.FIELD_RECEIVER,
          InvoiceTransports.OPERATOR_INCLUDE, value);
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null)
      return;
    String prop = sort.getProperty();
    if (SORT_BILL_NUMBER.equals(prop)) {
      queryDef.addOrder(InvoiceTransports.ORDER_BY_BILLNUMBER, dir);
      return;
    }

    if (SORT_BIZ_STATE.equals(prop)) {
      queryDef.addOrder(InvoiceTransports.ORDER_BY_BIZSTATE, dir);
      return;
    }

    if (SORT_INVOICE_TYPE.equals(prop)) {
      queryDef.addOrder(InvoiceTransports.ORDER_BY_INVOICETYPE, dir);
      return;
    }

    if (SORT_ACCOUNT_UNIT.equals(prop)) {
      queryDef.addOrder(InvoiceTransports.ORDER_BY_STORE, dir);
      return;
    }

    if (SORT_TRANSPORTOR.equals(prop)) {
      queryDef.addOrder(InvoiceTransports.ORDER_BY_TRANSPORTOR, dir);
      return;
    }

    if (SORT_TRANSPORT_DATE.equals(prop)) {
      queryDef.addOrder(InvoiceTransports.ORDER_BY_TRANSPORTDATE, dir);
      return;
    }

    if (SORT_REMARK.equals(prop)) {
      queryDef.addOrder(InvoiceTransports.ORDER_BY_REMARK, dir);
      return;
    }
    super.buildSort(queryDef, sort, dir);
  }
}
