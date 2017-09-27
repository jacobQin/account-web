/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceReceiveQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.receive.server;

import com.hd123.m3.account.gwt.ivc.receive.intf.client.InvoiceReceiveUrlParams;
import com.hd123.m3.account.service.ivc.receive.InvoiceReceives;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * 发票领用单|查询条件构造。
 * 
 * @author lixiaohong
 * @since 1.0
 *
 */
public class InvoiceReceiveQueryBuilder extends FlecsConditionDecoderImpl{
  private static InvoiceReceiveQueryBuilder instance;

  public static InvoiceReceiveQueryBuilder getInstance() {
    if (instance == null) {
      instance = new InvoiceReceiveQueryBuilder();
    }
    return instance;
  }

  @Override
  protected String getDefaultOrderField() {
    return InvoiceReceives.ORDER_BY_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }

    if (InvoiceReceiveUrlParams.Search.PN_BILLNUMBER.equals(fieldName.trim())) {
      return InvoiceReceives.FIELD_BILLNUMBER;
    } else if (InvoiceReceiveUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return InvoiceReceives.FIELD_BIZSTATE;
    } else if (InvoiceReceiveUrlParams.Search.FIELD_ACCOUNT_UNIT.equals(fieldName.trim())) {
      return InvoiceReceives.FIELD_STORE;
    } else if (InvoiceReceiveUrlParams.Search.FIELD_ISSUER.equals(fieldName.trim())) {
      return InvoiceReceives.FIELD_ISSUER;
    } else if (InvoiceReceiveUrlParams.Search.FIELD_RECEIVER.equals(fieldName.trim())) {
      return InvoiceReceives.FIELD_receiver;
    } else if (InvoiceReceiveUrlParams.Search.FIELD_INVOICE_CODE.equals(fieldName.trim())) {
      return InvoiceReceives.FIELD_INVOICECODE;
    } else if (InvoiceReceiveUrlParams.Search.FIELD_INVOICE_NUNBER.equals(fieldName.trim())) {
      return InvoiceReceives.FIELD_INVOICENUMBER;
    } else if (InvoiceReceiveUrlParams.Search.FIELD_RECEIVEDATE.equals(fieldName.trim())) {
      return InvoiceReceives.FIELD_RECEIVEDATE;
    } else if (InvoiceReceiveUrlParams.Search.FIELD_INVOICE_TYPE.equals(fieldName.trim())) {
      return InvoiceReceives.FIELD_INVOICETYPE;
    }else if (InvoiceReceiveUrlParams.Search.FIELD_PERMGROUP.equals(fieldName.trim())) {
      return InvoiceReceives.FIELD_PERMGROUP;
    } else if (InvoiceReceiveUrlParams.Search.FIELD_CREATE_INFO_OPER_ID.equals(fieldName.trim())) {
      return InvoiceReceives.FIELD_CREATOR_CODE;
    } else if (InvoiceReceiveUrlParams.Search.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName.trim())) {
      return InvoiceReceives.FIELD_CREATOR_NAME;
    } else if (InvoiceReceiveUrlParams.Search.FIELD_CREATE_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceReceives.FIELD_CREATED;
    } else if (InvoiceReceiveUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_ID
        .equals(fieldName.trim())) {
      return InvoiceReceives.FIELD_LAST_MODIFIER_CODE;
    } else if (InvoiceReceiveUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName
        .trim())) {
      return InvoiceReceives.FIELD_LAST_MODIFIER_NAME;
    } else if (InvoiceReceiveUrlParams.Search.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceReceives.FIELD_LAST_MODIFY_TIME;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {

    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }
    if (fieldName.trim().equals(InvoiceReceiveUrlParams.Search.PN_BILLNUMBER)) {
      return InvoiceReceives.ORDER_BY_BILLNUMBER;
    } else if (fieldName.trim().equals(InvoiceReceiveUrlParams.Search.FIELD_BIZSTATE)) {
      return InvoiceReceives.ORDER_BY_BIZSTATE;
    }else if (fieldName.trim().equals(InvoiceReceiveUrlParams.Search.FIELD_INVOICE_TYPE)) {
      return InvoiceReceives.ORDER_BY_INVOICETYPE;
    } else if (fieldName.trim().equals(InvoiceReceiveUrlParams.Search.FIELD_ACCOUNT_UNIT)) {
      return InvoiceReceives.ORDER_BY_STORE;
    } else if (fieldName.trim().equals(InvoiceReceiveUrlParams.Search.FIELD_ISSUER)) {
      return InvoiceReceives.ORDER_BY_issuer;
    }else if (fieldName.trim().equals(InvoiceReceiveUrlParams.Search.FIELD_RECEIVER)) {
      return InvoiceReceives.ORDER_BY_receiver;
    }  else if (fieldName.trim().equals(InvoiceReceiveUrlParams.Search.FIELD_RECEIVEDATE)) {
      return InvoiceReceives.ORDER_BY_RECEIVEDATE;
    } else if (fieldName.trim().equals(InvoiceReceiveUrlParams.Search.FIELD_REMARK)) {
      return InvoiceReceives.ORDER_BY_REMARK;
    } else if (fieldName.trim().equals(InvoiceReceiveUrlParams.Search.FIELD_LASTMODIFY_INFO_TIME)) {
      return InvoiceReceives.ORDER_BY_LAST_MODIFY_TIME;
    }
    return null;

  }

}
