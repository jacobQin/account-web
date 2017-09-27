/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRecycleQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.server;

import com.hd123.m3.account.gwt.ivc.recycle.intf.client.InvoiceRecycleUrlParams;
import com.hd123.m3.account.service.ivc.recycle.InvoiceRecycles;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * 发票回收单|查询条件构造。
 * 
 * @author lixiaohong
 * @since 1.9
 * 
 */
public class InvoiceRecycleQueryBuilder extends FlecsConditionDecoderImpl {
  private static InvoiceRecycleQueryBuilder instance;

  public static InvoiceRecycleQueryBuilder getInstance() {
    if (instance == null) {
      instance = new InvoiceRecycleQueryBuilder();
    }
    return instance;
  }

  @Override
  protected String getDefaultOrderField() {
    return InvoiceRecycles.ORDER_BY_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }

    if (InvoiceRecycleUrlParams.Flecs.PN_BILLNUMBER.equals(fieldName.trim())) {
      return InvoiceRecycles.FIELD_BILLNUMBER;
    } else if (InvoiceRecycleUrlParams.Flecs.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return InvoiceRecycles.FIELD_BIZSTATE;
    } else if (InvoiceRecycleUrlParams.Flecs.FIELD_ACCOUNT_UNIT.equals(fieldName.trim())) {
      return InvoiceRecycles.FIELD_STORE;
    } else if (InvoiceRecycleUrlParams.Flecs.FIELD_RECEIVER.equals(fieldName.trim())) {
      return InvoiceRecycles.FIELD_RECEIVER;
    } else if (InvoiceRecycleUrlParams.Flecs.FIELD_RETURNOR.equals(fieldName.trim())) {
      return InvoiceRecycles.FIELD_RETURNOR;
    } else if (InvoiceRecycleUrlParams.Flecs.FIELD_INVOICE_CODE.equals(fieldName.trim())) {
      return InvoiceRecycles.FIELD_INVOICECODE;
    } else if (InvoiceRecycleUrlParams.Flecs.FIELD_INVOICE_NUNBER.equals(fieldName.trim())) {
      return InvoiceRecycles.FIELD_INVOICENUMBER;
    } else if (InvoiceRecycleUrlParams.Flecs.FIELD_RECYCLEDATE.equals(fieldName.trim())) {
      return InvoiceRecycles.FIELD_RECYCLEDATE;
    } else if (InvoiceRecycleUrlParams.Flecs.FIELD_INVOICE_TYPE.equals(fieldName.trim())) {
      return InvoiceRecycles.FIELD_INVOICETYPE;
    } else if (InvoiceRecycleUrlParams.Flecs.FIELD_PERMGROUP.equals(fieldName.trim())) {
      return InvoiceRecycles.FIELD_PERMGROUP;
    } else if (InvoiceRecycleUrlParams.Flecs.FIELD_CREATE_INFO_OPER_ID.equals(fieldName.trim())) {
      return InvoiceRecycles.FIELD_CREATOR_CODE;
    } else if (InvoiceRecycleUrlParams.Flecs.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName.trim())) {
      return InvoiceRecycles.FIELD_CREATOR_NAME;
    } else if (InvoiceRecycleUrlParams.Flecs.FIELD_CREATE_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceRecycles.FIELD_CREATED;
    } else if (InvoiceRecycleUrlParams.Flecs.FIELD_LASTMODIFY_INFO_OPER_ID.equals(fieldName
        .trim())) {
      return InvoiceRecycles.FIELD_LAST_MODIFIER_CODE;
    } else if (InvoiceRecycleUrlParams.Flecs.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName
        .trim())) {
      return InvoiceRecycles.FIELD_LAST_MODIFIER_NAME;
    } else if (InvoiceRecycleUrlParams.Flecs.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceRecycles.FIELD_LAST_MODIFY_TIME;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {

    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }
    if (fieldName.trim().equals(InvoiceRecycleUrlParams.Flecs.PN_BILLNUMBER)) {
      return InvoiceRecycles.ORDER_BY_BILLNUMBER;
    } else if (fieldName.trim().equals(InvoiceRecycleUrlParams.Flecs.FIELD_BIZSTATE)) {
      return InvoiceRecycles.ORDER_BY_BIZSTATE;
    } else if (fieldName.trim().equals(InvoiceRecycleUrlParams.Flecs.FIELD_INVOICE_TYPE)) {
      return InvoiceRecycles.ORDER_BY_INVOICETYPE;
    } else if (fieldName.trim().equals(InvoiceRecycleUrlParams.Flecs.FIELD_ACCOUNT_UNIT)) {
      return InvoiceRecycles.ORDER_BY_STORE;
    } else if (fieldName.trim().equals(InvoiceRecycleUrlParams.Flecs.FIELD_RECEIVER)) {
      return InvoiceRecycles.ORDER_BY_RECEIVER;
    } else if (fieldName.trim().equals(InvoiceRecycleUrlParams.Flecs.FIELD_RETURNOR)) {
      return InvoiceRecycles.ORDER_BY_RETURNOR;
    } else if (fieldName.trim().equals(InvoiceRecycleUrlParams.Flecs.FIELD_RECYCLEDATE)) {
      return InvoiceRecycles.ORDER_BY_RECYCLEDATE;
    } else if (fieldName.trim().equals(InvoiceRecycleUrlParams.Flecs.FIELD_REMARK)) {
      return InvoiceRecycles.ORDER_BY_REMARK;
    }
    return null;

  }

}
