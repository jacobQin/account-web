/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecDepositRepaymentQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2014-02-25 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.receipt.server;

import com.hd123.m3.account.gwt.depositrepayment.receipt.intf.client.RecDepositRepaymentUrlParams;
import com.hd123.m3.account.service.depositrepayment.DepositRepayments;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * @author zhuhairui
 * 
 */
public class RecDepositRepaymentQueryBuilder extends FlecsConditionDecoderImpl {

  private static RecDepositRepaymentQueryBuilder instance = null;

  public static RecDepositRepaymentQueryBuilder getInstance() {
    if (instance == null)
      instance = new RecDepositRepaymentQueryBuilder();
    return instance;
  }

  public RecDepositRepaymentQueryBuilder() {
    super();
  }

  @Override
  protected String getDefaultOrderField() {
    return DepositRepayments.FIELD_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (RecDepositRepaymentUrlParams.Search.FIELD_BILLNUMBER.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_BILLNUMBER;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_ACCOUNTUNIT.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_ACCOUNTUNIT;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_COUNTERPARTTYPE.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_COUNTERPART_TYPE;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_COUNTERPART.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_COUNTERPART;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_CONTRACTNUMBER.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_CONTRACTNUMBER;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_CONTRACTNAME.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_CONTRACTNAME;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_BIZSTATE;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_CREATE_INFO_OPER_ID.equals(fieldName
        .trim())) {
      return DepositRepayments.FIELD_CREATOR_CODE;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName
        .trim())) {
      return DepositRepayments.FIELD_CREATOR_NAME;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_REPAYMENTDATE.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_REPAYMENTDATE;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_ACCOUNTDATE.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_ACCOUNTDATE;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_SUBJECT.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_SUBJECT;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_SETTLENO.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_SETTLENO;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_PAYMENTTYPE.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_PAYMENTTYPE;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_DEALER.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_DEALER;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_PERMGROUP.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_PERMGROUP;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_CREATE_INFO_TIME.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_CREATED;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_ID.equals(fieldName
        .trim())) {
      return DepositRepayments.FIELD_LAST_MODIFIER_CODE;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName
        .trim())) {
      return DepositRepayments.FIELD_LAST_MODIFIER_NAME;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName
        .trim())) {
      return DepositRepayments.FIELD_LAST_MODIFY_TIME;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_BUSINESSUNIT.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_BUSINESS;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (RecDepositRepaymentUrlParams.Search.FIELD_BILLNUMBER.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_BILLNUMBER;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_BIZSTATE;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_COUNTERPART.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_COUNTERPART;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_CONTRACTNUMBER.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_CONTRACTNUMBER;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_CONTRACTNAME.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_CONTRACTNAME;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_REPAYMENTTOTAL.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_REPAYMENTTOTAL;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_PAYMENTTYPE.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_PAYMENTTYPE;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_REPAYMENTDATE.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_REPAYMENTDATE;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_ACCOUNTDATE.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_ACCOUNTDATE;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_DEALER.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_DEALER;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_COUNTERCONTACT.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_COUNTERCONTACT;
    } else if (RecDepositRepaymentUrlParams.Search.FIELD_SETTLENO.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_SETTLENO;
    }
    return null;
  }

}
