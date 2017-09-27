/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	ReceiptQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-5 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.server;

import com.hd123.m3.account.gwt.payment.rec.intf.client.ReceiptUrlParams;
import com.hd123.m3.account.service.payment.Payments;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * @author subinzhu
 * 
 */
public class ReceiptQueryBuilder extends FlecsConditionDecoderImpl {

  private static ReceiptQueryBuilder instance;

  public static ReceiptQueryBuilder getInstance() {
    if (instance == null)
      instance = new ReceiptQueryBuilder();
    return instance;
  }

  public ReceiptQueryBuilder() {
    super();
  }

  @Override
  protected String getDefaultOrderField() {
    return Payments.FIELD_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_BILLNUMBER))
      return Payments.FIELD_BILLNUMBER;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_BIZSTATE))
      return Payments.FIELD_BIZSTATE;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_BPMSTATE))
      return Payments.FIELD_BPMSTATE;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_CONTRACTBILLNUMBER))
      return Payments.FIELD_CONTRACTBILLNUMBER;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_CONTRACT_NAME))
      return Payments.FIELD_CONTRACTNAME;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_COUNTERPARTTYPE))
      return Payments.FIELD_COUNTERPART_TYPE;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_COUNTERPART))
      return Payments.FIELD_COUNTERPARTUNIT;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_RECEIPTDATE))
      return Payments.FIELD_PAYMENTDATE;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_SETTLENO))
      return Payments.FIELD_SETTLENO;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_COOPMODE))
      return Payments.FIELD_COOPMODE;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_PERMGROUP))
      return Payments.FIELD_PERMGROUP;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_ACCOUNTUNIT))
      return Payments.FIELD_BUSINESSUNIT;
    else if (ReceiptUrlParams.Search.FIELD_CREATE_INFO_OPER_ID.equals(fieldName.trim()))
      return Payments.FIELD_CREATOR_CODE;
    else if (ReceiptUrlParams.Search.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName.trim()))
      return Payments.FIELD_CREATOR_NAME;
    else if (ReceiptUrlParams.Search.FIELD_CREATE_INFO_TIME.equals(fieldName.trim()))
      return Payments.FIELD_CREATED;
    else if (ReceiptUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_ID.equals(fieldName.trim()))
      return Payments.FIELD_LAST_MODIFIER_CODE;
    else if (ReceiptUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName.trim()))
      return Payments.FIELD_LAST_MODIFIER_NAME;
    else if (ReceiptUrlParams.Search.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName.trim()))
      return Payments.FIELD_LAST_MODIFY_TIME;
    else
      return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_BILLNUMBER))
      return Payments.ORDER_BY_BILLNUMBER;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_BIZSTATE))
      return Payments.ORDER_BY_BIZSTATE;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_COUNTERPART))
      return Payments.ORDER_BY_COUNTERPARTCENTERCODE;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_RECEIPTDATE))
      return Payments.ORDER_BY_PAYMENTDATE;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_UNPAYEDTOTAL))
      return Payments.ORDER_BY_UNPAYEDTOTAL;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_TOTAL))
      return Payments.ORDER_BY_TOTAL;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_DEFRAYALTOTAL))
      return Payments.ORDER_BY_DEFRAYALTOTAL;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_SETTLENO))
      return Payments.ORDER_BY_SETTLENO;
    else if (fieldName.trim().equals(ReceiptUrlParams.Search.FIELD_REMARK))
      return Payments.ORDER_BY_REMARK;
    else
      return null;
  }

}
