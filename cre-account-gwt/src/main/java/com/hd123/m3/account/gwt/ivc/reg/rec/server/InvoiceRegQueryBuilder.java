/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRegQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.server;

import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.InvoiceRegUrlParams;
import com.hd123.m3.account.service.ivc.reg.InvoiceRegs;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmSearchPage2;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * 发票登记单|查询条件构造。
 * 
 * @author chenrizhang
 * @since 1.0
 *
 */
public class InvoiceRegQueryBuilder extends FlecsConditionDecoderImpl {
  private static InvoiceRegQueryBuilder instance;

  public static InvoiceRegQueryBuilder getInstance() {
    if (instance == null) {
      instance = new InvoiceRegQueryBuilder();
    }
    return instance;
  }

  @Override
  protected String getDefaultOrderField() {
    return InvoiceRegs.ORDER_BY_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }

    if (BaseBpmSearchPage2.FIELD_BILLNUMBER.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_BILLNUMBER;
    } else if (InvoiceRegUrlParams.Flecs.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_BIZSTATE;
    } else if (InvoiceRegUrlParams.Flecs.FIELD_ACCOUNT_UNIT.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_STORE;
    } else if (InvoiceRegUrlParams.Flecs.FIELD_COUNTERPART.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_COUNTERPART;
    } else if (InvoiceRegUrlParams.Flecs.FIELD_SOURCEBILL_BILLNUMBER.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_SOURCEBILL_BILLNUMBER;
    } else if (InvoiceRegUrlParams.Flecs.FIELD_INVOICE_CODE.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_INVOICECODE;
    } else if (InvoiceRegUrlParams.Flecs.FIELD_INVOICE_NUNBER.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_INVOICENUMBER;
    } else if (InvoiceRegUrlParams.Flecs.FIELD_REGDATE.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_REGDATE;
    } else if (InvoiceRegUrlParams.Flecs.FIELD_INVOICE_TYPE.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_INVOICETYPE;
    } else if (InvoiceRegUrlParams.Flecs.FIELD_PERMGROUP.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_PERMGROUP;
    } else if (InvoiceRegUrlParams.Flecs.FIELD_CREATE_INFO_OPER_ID.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_CREATOR_CODE;
    } else if (InvoiceRegUrlParams.Flecs.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_CREATOR_NAME;
    } else if (InvoiceRegUrlParams.Flecs.FIELD_CREATE_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_CREATED;
    } else if (InvoiceRegUrlParams.Flecs.FIELD_LASTMODIFY_INFO_OPER_ID.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_LAST_MODIFIER_CODE;
    } else if (InvoiceRegUrlParams.Flecs.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_LAST_MODIFIER_NAME;
    } else if (InvoiceRegUrlParams.Flecs.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceRegs.FIELD_LAST_MODIFY_TIME;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {

    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }
    if (fieldName.trim().equals(BaseBpmSearchPage2.FIELD_BILLNUMBER)) {
      return InvoiceRegs.ORDER_BY_BILLNUMBER;
    } else if (fieldName.trim().equals(InvoiceRegUrlParams.Flecs.FIELD_BIZSTATE)) {
      return InvoiceRegs.ORDER_BY_BIZSTATE;
    } else if (fieldName.trim().equals(InvoiceRegUrlParams.Flecs.FIELD_INVOICE_TYPE)) {
      return InvoiceRegs.ORDER_BY_INVOICETYPE;
    } else if (fieldName.trim().equals(InvoiceRegUrlParams.Flecs.FIELD_COUNTERPART)) {
      return InvoiceRegs.ORDER_BY_COUNTERPART;
    } else if (fieldName.trim().equals(InvoiceRegUrlParams.Flecs.FIELD_REGDATE)) {
      return InvoiceRegs.ORDER_BY_REGDATE;
    } else if (fieldName.trim().equals(InvoiceRegUrlParams.Flecs.FIELD_REMARK)) {
      return InvoiceRegs.ORDER_BY_REMARK;
    } else if (fieldName.trim().equals(InvoiceRegUrlParams.Flecs.FIELD_CREATE_INFO_TIME)) {
      return InvoiceRegs.ORDER_BY_CREATE_INFO_TIME;
    } else if (fieldName.trim().equals(InvoiceRegUrlParams.Flecs.FIELD_LASTMODIFY_INFO_TIME)) {
      return InvoiceRegs.ORDER_BY_LAST_MODIFY_TIME;
    }
    return null;

  }

}
