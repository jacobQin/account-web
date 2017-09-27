/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceAbortQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.abort.server;

import com.hd123.m3.account.gwt.ivc.abort.intf.client.InvoiceAbortUrlParams;
import com.hd123.m3.account.service.ivc.abort.InvoiceAborts;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * 发票入库单|查询条件构造。
 * 
 * @author lixiaohong
 * @since 1.0
 *
 */
public class InvoiceAbortQueryBuilder extends FlecsConditionDecoderImpl{
  private static InvoiceAbortQueryBuilder instance;

  public static InvoiceAbortQueryBuilder getInstance() {
    if (instance == null) {
      instance = new InvoiceAbortQueryBuilder();
    }
    return instance;
  }

  @Override
  protected String getDefaultOrderField() {
    return InvoiceAborts.ORDER_BY_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }

    if (InvoiceAbortUrlParams.Search.PN_BILLNUMBER.equals(fieldName.trim())) {
      return InvoiceAborts.FIELD_BILLNUMBER;
    } else if (InvoiceAbortUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return InvoiceAborts.FIELD_BIZSTATE;
    } else if (InvoiceAbortUrlParams.Search.FIELD_ACCOUNT_UNIT.equals(fieldName.trim())) {
      return InvoiceAborts.FIELD_STORE;
    } else if (InvoiceAbortUrlParams.Search.FIELD_ABORTER.equals(fieldName.trim())) {
      return InvoiceAborts.FIELD_ABORTER;
    } else if (InvoiceAbortUrlParams.Search.FIELD_INVOICE_CODE.equals(fieldName.trim())) {
      return InvoiceAborts.FIELD_INVOICECODE;
    } else if (InvoiceAbortUrlParams.Search.FIELD_INVOICE_NUNBER.equals(fieldName.trim())) {
      return InvoiceAborts.FIELD_INVOICENUMBER;
    } else if (InvoiceAbortUrlParams.Search.FIELD_ABORTDATE.equals(fieldName.trim())) {
      return InvoiceAborts.FIELD_ABORTDATE;
    } else if (InvoiceAbortUrlParams.Search.FIELD_INVOICE_TYPE.equals(fieldName.trim())) {
      return InvoiceAborts.FIELD_INVOICETYPE;
    }else if (InvoiceAbortUrlParams.Search.FIELD_PERMGROUP.equals(fieldName.trim())) {
      return InvoiceAborts.FIELD_PERMGROUP;
    } else if (InvoiceAbortUrlParams.Search.FIELD_CREATE_INFO_OPER_ID.equals(fieldName.trim())) {
      return InvoiceAborts.FIELD_CREATOR_CODE;
    } else if (InvoiceAbortUrlParams.Search.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName.trim())) {
      return InvoiceAborts.FIELD_CREATOR_NAME;
    } else if (InvoiceAbortUrlParams.Search.FIELD_CREATE_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceAborts.FIELD_CREATED;
    } else if (InvoiceAbortUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_ID
        .equals(fieldName.trim())) {
      return InvoiceAborts.FIELD_LAST_MODIFIER_CODE;
    } else if (InvoiceAbortUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName
        .trim())) {
      return InvoiceAborts.FIELD_LAST_MODIFIER_NAME;
    } else if (InvoiceAbortUrlParams.Search.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceAborts.FIELD_LAST_MODIFY_TIME;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {

    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }
    if (fieldName.trim().equals(InvoiceAbortUrlParams.Search.PN_BILLNUMBER)) {
      return InvoiceAborts.ORDER_BY_BILLNUMBER;
    } else if (fieldName.trim().equals(InvoiceAbortUrlParams.Search.FIELD_BIZSTATE)) {
      return InvoiceAborts.ORDER_BY_BIZSTATE;
    }else if (fieldName.trim().equals(InvoiceAbortUrlParams.Search.FIELD_INVOICE_TYPE)) {
      return InvoiceAborts.ORDER_BY_INVOICETYPE;
    } else if (fieldName.trim().equals(InvoiceAbortUrlParams.Search.FIELD_ACCOUNT_UNIT)) {
      return InvoiceAborts.ORDER_BY_STORE;
    } else if (fieldName.trim().equals(InvoiceAbortUrlParams.Search.FIELD_ABORTER)) {
      return InvoiceAborts.ORDER_BY_ABORTER;
    } else if (fieldName.trim().equals(InvoiceAbortUrlParams.Search.FIELD_ABORTDATE)) {
      return InvoiceAborts.ORDER_BY_ABORTDATE;
    } else if (fieldName.trim().equals(InvoiceAbortUrlParams.Search.FIELD_REMARK)) {
      return InvoiceAborts.ORDER_BY_REMARK;
    }
    return null;

  }

}
