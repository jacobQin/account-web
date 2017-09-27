/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月18日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.server;

import com.hd123.m3.account.gwt.ivc.exchange.intf.client.InvoiceExchangeUrlParams;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchanges;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * 发票交换单查询字段、排序字段转换器
 * 
 * @author LiBin
 * @since 1.7
 */
public class InvoiceExchangeQueryBuilder extends FlecsConditionDecoderImpl{

  private static InvoiceExchangeQueryBuilder instance = null;
  
  public static InvoiceExchangeQueryBuilder getInstance(){
    if(instance == null){
      instance = new InvoiceExchangeQueryBuilder();
    }
    return instance;
  }
  @Override
  protected String getDefaultOrderField() {
    return InvoiceExchanges.ORDER_BY_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }

    if (InvoiceExchangeUrlParams.PN_BILLNUMBER.equals(fieldName.trim())) {
      return InvoiceExchanges.FIELD_BILLNUMBER;
    } else if (InvoiceExchangeUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return InvoiceExchanges.FIELD_BIZSTATE;
    } else if (InvoiceExchangeUrlParams.Search.FIELD_ACCOUNT_UNIT.equals(fieldName.trim())) {
      return InvoiceExchanges.FIELD_STORE;
    } else if (InvoiceExchangeUrlParams.Search.FIELD_EXCHANGER.equals(fieldName.trim())) {
      return InvoiceExchanges.FIELD_EXCHANGER;
    } else if (InvoiceExchangeUrlParams.Search.FIELD_INVOICE_CODE.equals(fieldName.trim())) {
      return InvoiceExchanges.FIELD_INVOICECODE;
    } else if (InvoiceExchangeUrlParams.Search.FIELD_INVOICE_NUNBER.equals(fieldName.trim())) {
      return InvoiceExchanges.FIELD_INVOICENUMBER;
    } else if (InvoiceExchangeUrlParams.Search.FIELD_EXCHANGE_DATE.equals(fieldName.trim())) {
      return InvoiceExchanges.FIELD_EXCHANGEDATE;
    } else if (InvoiceExchangeUrlParams.Search.FIELD_EXCHANGE_TYPE.equals(fieldName.trim())) {
      return InvoiceExchanges.FIELD_EXCHANGETYPE;
    }else if (InvoiceExchangeUrlParams.Search.FIELD_PERMGROUP.equals(fieldName.trim())) {
      return InvoiceExchanges.FIELD_PERMGROUP;
    } else if (InvoiceExchangeUrlParams.Search.FIELD_CREATE_INFO_OPER_ID.equals(fieldName.trim())) {
      return InvoiceExchanges.FIELD_CREATOR_CODE;
    } else if (InvoiceExchangeUrlParams.Search.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName.trim())) {
      return InvoiceExchanges.FIELD_CREATOR_NAME;
    } else if (InvoiceExchangeUrlParams.Search.FIELD_CREATE_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceExchanges.FIELD_CREATED;
    } else if (InvoiceExchangeUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_ID
        .equals(fieldName.trim())) {
      return InvoiceExchanges.FIELD_LAST_MODIFIER_CODE;
    } else if (InvoiceExchangeUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName
        .trim())) {
      return InvoiceExchanges.FIELD_LAST_MODIFIER_NAME;
    } else if (InvoiceExchangeUrlParams.Search.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceExchanges.FIELD_LAST_MODIFY_TIME;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {

    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }
    if (fieldName.trim().equals(InvoiceExchangeUrlParams.PN_BILLNUMBER)) {
      return InvoiceExchanges.ORDER_BY_BILLNUMBER;
    } else if (fieldName.trim().equals(InvoiceExchangeUrlParams.Search.FIELD_BIZSTATE)) {
      return InvoiceExchanges.ORDER_BY_BIZSTATE;
    }else if (fieldName.trim().equals(InvoiceExchangeUrlParams.Search.FIELD_EXCHANGE_TYPE)) {
      return InvoiceExchanges.ORDER_BY_EXCHANGETYPE;
    } else if (fieldName.trim().equals(InvoiceExchangeUrlParams.Search.FIELD_ACCOUNT_UNIT)) {
      return InvoiceExchanges.ORDER_BY_STORE;
    } else if (fieldName.trim().equals(InvoiceExchangeUrlParams.Search.FIELD_EXCHANGER)) {
      return InvoiceExchanges.ORDER_BY_EXCHANGER;
    } else if (fieldName.trim().equals(InvoiceExchangeUrlParams.Search.FIELD_EXCHANGE_DATE)) {
      return InvoiceExchanges.ORDER_BY_EXCHANGEDATE;
    } else if (fieldName.trim().equals(InvoiceExchangeUrlParams.Search.FIELD_REMARK)) {
      return InvoiceExchanges.ORDER_BY_REMARK;
    } 
    return null;

  }

}