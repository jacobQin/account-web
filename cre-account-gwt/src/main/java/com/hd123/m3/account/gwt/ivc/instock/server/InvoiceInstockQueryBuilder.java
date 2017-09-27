/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceInstockQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.instock.server;

import com.hd123.m3.account.gwt.ivc.instock.intf.client.InvoiceInstockUrlParams;
import com.hd123.m3.account.service.ivc.instock.InvoiceInstocks;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * 发票入库单|查询条件构造。
 * 
 * @author lixiaohong
 * @since 1.0
 *
 */
public class InvoiceInstockQueryBuilder extends FlecsConditionDecoderImpl{
  private static InvoiceInstockQueryBuilder instance;

  public static InvoiceInstockQueryBuilder getInstance() {
    if (instance == null) {
      instance = new InvoiceInstockQueryBuilder();
    }
    return instance;
  }

  @Override
  protected String getDefaultOrderField() {
    return InvoiceInstocks.ORDER_BY_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }

    if (InvoiceInstockUrlParams.Search.PN_BILLNUMBER.equals(fieldName.trim())) {
      return InvoiceInstocks.FIELD_BILLNUMBER;
    } else if (InvoiceInstockUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return InvoiceInstocks.FIELD_BIZSTATE;
    } else if (InvoiceInstockUrlParams.Search.FIELD_ACCOUNT_UNIT.equals(fieldName.trim())) {
      return InvoiceInstocks.FIELD_STORE;
    } else if (InvoiceInstockUrlParams.Search.FIELD_INSTOCKOR.equals(fieldName.trim())) {
      return InvoiceInstocks.FIELD_INSTOCKOR;
    } else if (InvoiceInstockUrlParams.Search.FIELD_INVOICE_CODE.equals(fieldName.trim())) {
      return InvoiceInstocks.FIELD_INVOICECODE;
    } else if (InvoiceInstockUrlParams.Search.FIELD_INVOICE_NUNBER.equals(fieldName.trim())) {
      return InvoiceInstocks.FIELD_INVOICENUMBER;
    } else if (InvoiceInstockUrlParams.Search.FIELD_INSTOCKDATE.equals(fieldName.trim())) {
      return InvoiceInstocks.FIELD_INSTOCKDATE;
    } else if (InvoiceInstockUrlParams.Search.FIELD_INVOICE_TYPE.equals(fieldName.trim())) {
      return InvoiceInstocks.FIELD_INVOICETYPE;
    }else if (InvoiceInstockUrlParams.Search.FIELD_PERMGROUP.equals(fieldName.trim())) {
      return InvoiceInstocks.FIELD_PERMGROUP;
    } else if (InvoiceInstockUrlParams.Search.FIELD_CREATE_INFO_OPER_ID.equals(fieldName.trim())) {
      return InvoiceInstocks.FIELD_CREATOR_CODE;
    } else if (InvoiceInstockUrlParams.Search.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName.trim())) {
      return InvoiceInstocks.FIELD_CREATOR_NAME;
    } else if (InvoiceInstockUrlParams.Search.FIELD_CREATE_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceInstocks.FIELD_CREATED;
    } else if (InvoiceInstockUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_ID
        .equals(fieldName.trim())) {
      return InvoiceInstocks.FIELD_LAST_MODIFIER_CODE;
    } else if (InvoiceInstockUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName
        .trim())) {
      return InvoiceInstocks.FIELD_LAST_MODIFIER_NAME;
    } else if (InvoiceInstockUrlParams.Search.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceInstocks.FIELD_LAST_MODIFY_TIME;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {

    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }
    if (fieldName.trim().equals(InvoiceInstockUrlParams.Search.PN_BILLNUMBER)) {
      return InvoiceInstocks.ORDER_BY_BILLNUMBER;
    } else if (fieldName.trim().equals(InvoiceInstockUrlParams.Search.FIELD_BIZSTATE)) {
      return InvoiceInstocks.ORDER_BY_BIZSTATE;
    }else if (fieldName.trim().equals(InvoiceInstockUrlParams.Search.FIELD_INVOICE_TYPE)) {
      return InvoiceInstocks.ORDER_BY_INVOICETYPE;
    } else if (fieldName.trim().equals(InvoiceInstockUrlParams.Search.FIELD_ACCOUNT_UNIT)) {
      return InvoiceInstocks.ORDER_BY_STORE;
    } else if (fieldName.trim().equals(InvoiceInstockUrlParams.Search.FIELD_ISRECEIVE)) {
      return InvoiceInstocks.ORDER_BY_ISRECEIVE;
    } else if (fieldName.trim().equals(InvoiceInstockUrlParams.Search.FIELD_INSTOCKOR)) {
      return InvoiceInstocks.ORDER_BY_INSTOCKOR;
    } else if (fieldName.trim().equals(InvoiceInstockUrlParams.Search.FIELD_INSTOCKDATE)) {
      return InvoiceInstocks.ORDER_BY_INSTOCKDATE;
    } else if (fieldName.trim().equals(InvoiceInstockUrlParams.Search.FIELD_REMARK)) {
      return InvoiceInstocks.ORDER_BY_REMARK;
    } else if (fieldName.trim().equals(InvoiceInstockUrlParams.Search.FIELD_LASTMODIFY_INFO_TIME)) {
      return InvoiceInstocks.ORDER_BY_LAST_MODIFY_TIME;
    }
    return null;

  }

}
