/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-27 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.receipt.server.querybuilder;

import com.hd123.m3.account.gwt.invoice.receipt.intf.client.RecInvoiceRegUrlParams;
import com.hd123.m3.account.service.invoice.InvoiceRegs;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * @author chenpeisi
 * 
 */
public class RecInvoiceRegQueryBuilder extends FlecsConditionDecoderImpl {

  private static RecInvoiceRegQueryBuilder instance = null;

  public static RecInvoiceRegQueryBuilder getInstance() {
    if (instance == null)
      instance = new RecInvoiceRegQueryBuilder();
    return instance;
  }

  @Override
  protected String getDefaultOrderField() {
    return InvoiceRegs.FIELD_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (RecInvoiceRegUrlParams.Search.FIELD_BILLNUMBER.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_BILLNUMBER;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_BIZSTATE;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_COUNTERPARTTYPE.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_COUNTERPART_TYPE;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_COUNTERPART.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_COUNTERPARTCENTER;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_INVOICENUMBER.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_INVOICENUMBER;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_SOURCEBILLNUMBER.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_SOURCEBILLNUMBER;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_SETTLENO.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_SETTLENO;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_REGDATE.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_REGDATE;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_CREATE_INFO_OPER_ID.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_CREATOR_CODE;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_CREATOR_NAME;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_CREATE_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_CREATED;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_ID.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_LAST_MODIFIER_CODE;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName
        .trim())) {
      return InvoiceRegs.FIELD_LAST_MODIFIER_NAME;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_LAST_MODIFY_TIME;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_PERMGROUP.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_PERMGROUP;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_ACCOUNTUNIT.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_ACCOUNTUNIT;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (RecInvoiceRegUrlParams.Search.FIELD_BILLNUMBER.equals(fieldName.trim())) {
      return InvoiceRegs.ORDER_BY_BILLNUMBER;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return InvoiceRegs.ORDER_BY_BIZSTATE;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_COUNTERPART.equals(fieldName.trim())) {
      return InvoiceRegs.ORDER_BY_COUNTERPARTCENTERCODE;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_SETTLENO.equals(fieldName.trim())) {
      return InvoiceRegs.ORDER_BY_SETTLENO;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_REGDATE.equals(fieldName.trim())) {
      return InvoiceRegs.ORDER_BY_REGDATE;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_INVOICETOTAL_TOTAL.equals(fieldName.trim())) {
      return InvoiceRegs.ORDER_BY_INVOICETOTAL;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_INVOICETOTAL_TAX.equals(fieldName.trim())) {
      return InvoiceRegs.ORDER_BY_INVOICETAX;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_ACCOUNTTOTAL_TOTAL.equals(fieldName.trim())) {
      return InvoiceRegs.ORDER_BY_ACCOUNTTOTAL;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_ACCOUNTTOTAL_TAX.equals(fieldName.trim())) {
      return InvoiceRegs.ORDER_BY_ACCOUNTTAX;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_TOTALDIFF.equals(fieldName.trim())) {
      return InvoiceRegs.ORDER_BY_TOTALDIFF;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_TAXDIFF.equals(fieldName.trim())) {
      return InvoiceRegs.ORDER_BY_TAXDIFF;
    } else if (RecInvoiceRegUrlParams.Search.FIELD_ACCOUNTUNIT.equals(fieldName.trim())) {
      return InvoiceRegs.ORDER_BY_ACCOUNTUNITCODE;
    }
    return null;
  }
}
