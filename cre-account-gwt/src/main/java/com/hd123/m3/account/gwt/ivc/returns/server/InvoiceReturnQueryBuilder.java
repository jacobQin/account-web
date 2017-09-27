/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceReturnQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.returns.server;

import com.hd123.m3.account.gwt.ivc.returns.intf.client.InvoiceReturnUrlParams;
import com.hd123.m3.account.service.ivc.returns.InvoiceReturns;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * 发票领退单|查询条件构造。
 * 
 * @author lixiaohong
 * @since 1.0
 *
 */
public class InvoiceReturnQueryBuilder extends FlecsConditionDecoderImpl{
  private static InvoiceReturnQueryBuilder instance;

  public static InvoiceReturnQueryBuilder getInstance() {
    if (instance == null) {
      instance = new InvoiceReturnQueryBuilder();
    }
    return instance;
  }

  @Override
  protected String getDefaultOrderField() {
    return InvoiceReturns.ORDER_BY_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }

    if (InvoiceReturnUrlParams.Search.PN_BILLNUMBER.equals(fieldName.trim())) {
      return InvoiceReturns.FIELD_BILLNUMBER;
    } else if (InvoiceReturnUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return InvoiceReturns.FIELD_BIZSTATE;
    } else if (InvoiceReturnUrlParams.Search.FIELD_ACCOUNT_UNIT.equals(fieldName.trim())) {
      return InvoiceReturns.FIELD_STORE;
    } else if (InvoiceReturnUrlParams.Search.FIELD_RECEIVER.equals(fieldName.trim())) {
      return InvoiceReturns.FIELD_RECEIVER;
    } else if (InvoiceReturnUrlParams.Search.FIELD_RETURNOR.equals(fieldName.trim())) {
      return InvoiceReturns.FIELD_RETURNOR;
    } else if (InvoiceReturnUrlParams.Search.FIELD_INVOICE_CODE.equals(fieldName.trim())) {
      return InvoiceReturns.FIELD_INVOICECODE;
    } else if (InvoiceReturnUrlParams.Search.FIELD_INVOICE_NUNBER.equals(fieldName.trim())) {
      return InvoiceReturns.FIELD_INVOICENUMBER;
    } else if (InvoiceReturnUrlParams.Search.FIELD_RETURNDATE.equals(fieldName.trim())) {
      return InvoiceReturns.FIELD_RETURNDATE;
    } else if (InvoiceReturnUrlParams.Search.FIELD_INVOICE_TYPE.equals(fieldName.trim())) {
      return InvoiceReturns.FIELD_INVOICETYPE;
    }else if (InvoiceReturnUrlParams.Search.FIELD_PERMGROUP.equals(fieldName.trim())) {
      return InvoiceReturns.FIELD_PERMGROUP;
    } else if (InvoiceReturnUrlParams.Search.FIELD_CREATE_INFO_OPER_ID.equals(fieldName.trim())) {
      return InvoiceReturns.FIELD_CREATOR_CODE;
    } else if (InvoiceReturnUrlParams.Search.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName.trim())) {
      return InvoiceReturns.FIELD_CREATOR_NAME;
    } else if (InvoiceReturnUrlParams.Search.FIELD_CREATE_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceReturns.FIELD_CREATED;
    } else if (InvoiceReturnUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_ID
        .equals(fieldName.trim())) {
      return InvoiceReturns.FIELD_LAST_MODIFIER_CODE;
    } else if (InvoiceReturnUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName
        .trim())) {
      return InvoiceReturns.FIELD_LAST_MODIFIER_NAME;
    } else if (InvoiceReturnUrlParams.Search.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceReturns.FIELD_LAST_MODIFY_TIME;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {

    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }
    if (fieldName.trim().equals(InvoiceReturnUrlParams.Search.PN_BILLNUMBER)) {
      return InvoiceReturns.ORDER_BY_BILLNUMBER;
    } else if (fieldName.trim().equals(InvoiceReturnUrlParams.Search.FIELD_BIZSTATE)) {
      return InvoiceReturns.ORDER_BY_BIZSTATE;
    }else if (fieldName.trim().equals(InvoiceReturnUrlParams.Search.FIELD_INVOICE_TYPE)) {
      return InvoiceReturns.ORDER_BY_INVOICETYPE;
    } else if (fieldName.trim().equals(InvoiceReturnUrlParams.Search.FIELD_ACCOUNT_UNIT)) {
      return InvoiceReturns.ORDER_BY_STORE;
    } else if (fieldName.trim().equals(InvoiceReturnUrlParams.Search.FIELD_RECEIVER)) {
      return InvoiceReturns.ORDER_BY_RECEIVER;
    }else if (fieldName.trim().equals(InvoiceReturnUrlParams.Search.FIELD_RETURNOR)) {
      return InvoiceReturns.ORDER_BY_RETURNOR;
    }  else if (fieldName.trim().equals(InvoiceReturnUrlParams.Search.FIELD_RETURNDATE)) {
      return InvoiceReturns.ORDER_BY_RETURNDATE;
    } else if (fieldName.trim().equals(InvoiceReturnUrlParams.Search.FIELD_REMARK)) {
      return InvoiceReturns.ORDER_BY_REMARK;
    } else if (fieldName.trim().equals(InvoiceReturnUrlParams.Search.FIELD_LASTMODIFY_INFO_TIME)) {
      return InvoiceReturns.ORDER_BY_LAST_MODIFY_TIME;
    }
    return null;

  }

}
