/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceRegQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月15日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.reg;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.ivc.reg.InvoiceRegs;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author LiBin
 *
 */
@Component
public class InvoiceRegQueryBuilder extends FlecsQueryDefinitionBuilder {

  private static final String FIELD_BILLNUMBER = "billNumber";
  public static final String FIELD_BIZSTATE = "bizState";
  public static final String FIELD_INVOICE_TYPE = "invoiceType";
  public static final String FIELD_ACCOUNT_UNIT = "accountUnit";
  public static final String FIELD_COUNTERPART = "counterpart";
  public static final String FIELD_SOURCEBILL_BILLNUMBER = "sourceNumber";
  public static final String FIELD_INVOICE_CODE = "invoiceCode";
  public static final String FIELD_INVOICE_NUNBER = "invoiceNumber";
  public static final String FIELD_REGDATE = "regDate";

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null)
      return;
    if (FIELD_BILLNUMBER.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceRegs.FIELD_BILLNUMBER, Basices.OPERATOR_START_WITH, value);
    } else if (FIELD_BIZSTATE.equals(fieldName)) {
      if (value instanceof List) {
        for (Object state : ((List) value)) {
          queryDef.addFlecsCondition(InvoiceRegs.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, state);
        }
      } else if (value instanceof String) {
        queryDef.addFlecsCondition(InvoiceRegs.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, value);
      }
    } else if (FIELD_ACCOUNT_UNIT.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceRegs.FIELD_STORE, Basices.OPERATOR_EQUALS, value);
    }  else if (FIELD_COUNTERPART.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceRegs.FIELD_COUNTERPART, Basices.OPERATOR_EQUALS, value);
    }else if (FIELD_REGDATE.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceRegs.FIELD_REGDATE, Basices.OPERATOR_EQUALS,
          StringUtil.toDate(value.toString()));
    } else if (FIELD_INVOICE_TYPE.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceRegs.FIELD_INVOICETYPE, Basices.OPERATOR_EQUALS, value);
    } else if (FIELD_INVOICE_CODE.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceRegs.FIELD_INVOICECODE, Basices.OPERATOR_START_WITH, value);
    } else if (FIELD_INVOICE_NUNBER.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceRegs.FIELD_INVOICENUMBER, Basices.OPERATOR_START_WITH,
          value);
    } else if (FIELD_SOURCEBILL_BILLNUMBER.equals(fieldName)) {
      queryDef.addFlecsCondition(InvoiceRegs.FIELD_SOURCEBILL_BILLNUMBER, Basices.OPERATOR_START_WITH,
          value);
    }else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null)
      return;

    if (FIELD_BIZSTATE.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceRegs.ORDER_BY_BIZSTATE, dir);
    } else if (FIELD_BILLNUMBER.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceRegs.ORDER_BY_BILLNUMBER, dir);
    } else if (FIELD_ACCOUNT_UNIT.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceRegs.ORDER_BY_STORE, dir);
    } else if (FIELD_COUNTERPART.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceRegs.ORDER_BY_COUNTERPART, dir);
    } else if (FIELD_REGDATE.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceRegs.ORDER_BY_REGDATE, dir);
    } else if (FIELD_INVOICE_TYPE.equals(sort.getProperty())) {
      queryDef.addOrder(InvoiceRegs.ORDER_BY_INVOICETYPE, dir);
    } else {
      super.buildSort(queryDef, sort, dir);
    }
  }
}
