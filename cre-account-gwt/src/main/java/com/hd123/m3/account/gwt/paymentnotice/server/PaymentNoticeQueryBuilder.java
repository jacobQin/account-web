/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentNoticeQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-14 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.server;

import com.hd123.m3.account.gwt.paymentnotice.intf.client.PaymentNoticeUrlParams;
import com.hd123.m3.account.service.paymentnotice.PaymentNotices;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * 收付款通知单flecs查询构造器
 * 
 * @author zhuhairui
 * 
 */
public class PaymentNoticeQueryBuilder extends FlecsConditionDecoderImpl {

  private static PaymentNoticeQueryBuilder instance;

  public static PaymentNoticeQueryBuilder getInstance() {
    if (instance == null)
      instance = new PaymentNoticeQueryBuilder();
    return instance;
  }

  @Override
  protected String getDefaultOrderField() {
    return PaymentNotices.FIELD_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    String field = fieldName.trim();
    if (PaymentNoticeUrlParams.Search.FIELD_BILLNUMBER.equals(field))
      return PaymentNotices.FIELD_BILLNUMBER;
    else if (PaymentNoticeUrlParams.Search.FIELD_BIZSTATE.equals(field))
      return PaymentNotices.FIELD_BIZSTATE;
    else if (PaymentNoticeUrlParams.Search.FIELD_BPMSTATE.equals(field))
      return PaymentNotices.FIELD_BPMSTATE;
    else if (PaymentNoticeUrlParams.Search.FIELD_PERMGROUP.equals(field))
      return PaymentNotices.FIELD_PERMGROUP;
    else if (PaymentNoticeUrlParams.Search.FIELD_COUNTERPARTTYPE.equals(field))
      return PaymentNotices.FIELD_COUNTERPART_TYPE;
    else if (PaymentNoticeUrlParams.Search.FIELD_COUNTERPART.equals(field))
      return PaymentNotices.FIELD_COUNTERPART;
    else if (PaymentNoticeUrlParams.Search.FIELD_CONTRACTBILLNUMBER.equals(field))
      return PaymentNotices.FIELD_CONTRACTNUMBER;
    else if (PaymentNoticeUrlParams.Search.FIELD_STATEMENTBILLNUMBER.equals(field))
      return PaymentNotices.FIELD_STATEMENTNUMBER;
    else if (PaymentNoticeUrlParams.Search.FIELD_SETTLENO.equals(field))
      return PaymentNotices.FIELD_SETTLENO;
    else if (PaymentNoticeUrlParams.Search.FIELD_BUSINESSUNIT.equals(field))
      return PaymentNotices.FIELD_BUSINESSUNIT;
    else if (PaymentNoticeUrlParams.Search.FIELD_CRETOR_CODE.equals(field))
      return PaymentNotices.FIELD_CRETOR_CODE;
    else if (PaymentNoticeUrlParams.Search.FIELD_CRETOR_NAME.equals(field))
      return PaymentNotices.FIELD_CRETOR_NAME;
    else if (PaymentNoticeUrlParams.Search.FIELD_CREATED.equals(field))
      return PaymentNotices.FIELD_CREATED;
    else if (PaymentNoticeUrlParams.Search.FIELD_LASTMODIFIER_CODE.equals(field))
      return PaymentNotices.FIELD_LASTMODIFIER_CODE;
    else if (PaymentNoticeUrlParams.Search.FIELD_LASTMODIFIER_NAME.equals(field))
      return PaymentNotices.FIELD_LASTMODIFIER_NAME;
    else if (PaymentNoticeUrlParams.Search.FIELD_LASTMODIFIED.equals(field))
      return PaymentNotices.FIELD_LASTMODIFIED;
    else
      return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    String field = fieldName.trim();

    if (PaymentNoticeUrlParams.Search.FIELD_BILLNUMBER.equals(field))
      return PaymentNotices.ORDER_BY_BILLNUMBER;
    else if (PaymentNoticeUrlParams.Search.FIELD_BIZSTATE.equals(field))
      return PaymentNotices.ORDER_BY_BIZSTATE;
    else if (PaymentNoticeUrlParams.Search.FIELD_BPMSTATE.equals(field))
      return PaymentNotices.ORDER_BY_BPMSTATE;
    else if (PaymentNoticeUrlParams.Search.FIELD_COUNTERPART.equals(field))
      return PaymentNotices.ORDER_BY_COUNTERPART;
    else if (PaymentNoticeUrlParams.Search.FIELD_SETTLENO.equals(field))
      return PaymentNotices.FIELD_SETTLENO;
    else if (PaymentNoticeUrlParams.Search.FIELD_RECEIPTTOTAL.equals(field))
      return PaymentNotices.ORDER_BY_RECEIPTTOTAL;
    else if (PaymentNoticeUrlParams.Search.FIELD_RECEIPTIVCTOTAL.equals(field))
      return PaymentNotices.ORDER_BY_RECEIPTIVCTOTAL;
    else if (PaymentNoticeUrlParams.Search.FIELD_PAYMENTTOTAL.equals(field))
      return PaymentNotices.ORDER_BY_PAYMENTTOTAL;
    else if (PaymentNoticeUrlParams.Search.FIELD_PAYMENTIVCTOTAL.equals(field))
      return PaymentNotices.ORDER_BY_PAYMENTIVCTOTAL;
    else
      return null;
  }

}
