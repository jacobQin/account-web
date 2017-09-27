/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceTransportQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.transport.server;

import com.hd123.m3.account.gwt.ivc.transport.intf.client.InvoiceTransportUrlParams;
import com.hd123.m3.account.service.ivc.transport.InvoiceTransports;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * 发票调拨单|查询条件构造。
 * 
 * @author lixiaohong
 * @since 1.9
 * 
 */
public class InvoiceTransportQueryBuilder extends FlecsConditionDecoderImpl {
  private static InvoiceTransportQueryBuilder instance;

  public static InvoiceTransportQueryBuilder getInstance() {
    if (instance == null) {
      instance = new InvoiceTransportQueryBuilder();
    }
    return instance;
  }

  @Override
  protected String getDefaultOrderField() {
    return InvoiceTransports.ORDER_BY_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }

    if (InvoiceTransportUrlParams.Flecs.PN_BILLNUMBER.equals(fieldName.trim())) {
      return InvoiceTransports.FIELD_BILLNUMBER;
    } else if (InvoiceTransportUrlParams.Flecs.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return InvoiceTransports.FIELD_BIZSTATE;
    } else if (InvoiceTransportUrlParams.Flecs.FIELD_ACCOUNT_UNIT.equals(fieldName.trim())) {
      return InvoiceTransports.FIELD_STORE;
    } else if (InvoiceTransportUrlParams.Flecs.FIELD_TRANSPORTOR.equals(fieldName.trim())) {
      return InvoiceTransports.FIELD_TRANSPORTOR;
    } else if (InvoiceTransportUrlParams.Flecs.FIELD_INVOICE_CODE.equals(fieldName.trim())) {
      return InvoiceTransports.FIELD_INVOICECODE;
    } else if (InvoiceTransportUrlParams.Flecs.FIELD_INVOICE_NUNBER.equals(fieldName.trim())) {
      return InvoiceTransports.FIELD_INVOICENUMBER;
    } else if (InvoiceTransportUrlParams.Flecs.FIELD_TRANSPORTDATE.equals(fieldName.trim())) {
      return InvoiceTransports.FIELD_TRANSPORTDATE;
    } else if (InvoiceTransportUrlParams.Flecs.FIELD_INVOICE_TYPE.equals(fieldName.trim())) {
      return InvoiceTransports.FIELD_INVOICETYPE;
    } else if (InvoiceTransportUrlParams.Flecs.FIELD_IN_ACCOUNT_UNIT.equals(fieldName.trim())) {
      return InvoiceTransports.FIELD_IN_ACCOUNTUNIT;
    } else if (InvoiceTransportUrlParams.Flecs.FIELD_RECEIVER.equals(fieldName.trim())) {
      return InvoiceTransports.FIELD_RECEIVER;
    } else if (InvoiceTransportUrlParams.Flecs.FIELD_PERMGROUP.equals(fieldName.trim())) {
      return InvoiceTransports.FIELD_PERMGROUP;
    } else if (InvoiceTransportUrlParams.Flecs.FIELD_CREATE_INFO_OPER_ID.equals(fieldName.trim())) {
      return InvoiceTransports.FIELD_CREATOR_CODE;
    } else if (InvoiceTransportUrlParams.Flecs.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName.trim())) {
      return InvoiceTransports.FIELD_CREATOR_NAME;
    } else if (InvoiceTransportUrlParams.Flecs.FIELD_CREATE_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceTransports.FIELD_CREATED;
    } else if (InvoiceTransportUrlParams.Flecs.FIELD_LASTMODIFY_INFO_OPER_ID.equals(fieldName
        .trim())) {
      return InvoiceTransports.FIELD_LAST_MODIFIER_CODE;
    } else if (InvoiceTransportUrlParams.Flecs.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName
        .trim())) {
      return InvoiceTransports.FIELD_LAST_MODIFIER_NAME;
    } else if (InvoiceTransportUrlParams.Flecs.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceTransports.FIELD_LAST_MODIFY_TIME;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {

    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }
    if (fieldName.trim().equals(InvoiceTransportUrlParams.Flecs.PN_BILLNUMBER)) {
      return InvoiceTransports.ORDER_BY_BILLNUMBER;
    } else if (fieldName.trim().equals(InvoiceTransportUrlParams.Flecs.FIELD_BIZSTATE)) {
      return InvoiceTransports.ORDER_BY_BIZSTATE;
    } else if (fieldName.trim().equals(InvoiceTransportUrlParams.Flecs.FIELD_INVOICE_TYPE)) {
      return InvoiceTransports.ORDER_BY_INVOICETYPE;
    } else if (fieldName.trim().equals(InvoiceTransportUrlParams.Flecs.FIELD_ACCOUNT_UNIT)) {
      return InvoiceTransports.ORDER_BY_STORE;
    } else if (fieldName.trim().equals(InvoiceTransportUrlParams.Flecs.FIELD_TRANSPORTOR)) {
      return InvoiceTransports.ORDER_BY_TRANSPORTOR;
    } else if (fieldName.trim().equals(InvoiceTransportUrlParams.Flecs.FIELD_TRANSPORTDATE)) {
      return InvoiceTransports.ORDER_BY_TRANSPORTDATE;
    } else if (fieldName.trim().equals(InvoiceTransportUrlParams.Flecs.FIELD_REMARK)) {
      return InvoiceTransports.ORDER_BY_REMARK;
    }
    return null;

  }

}
