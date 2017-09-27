/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PayQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-16 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.payment.server;

import com.hd123.m3.account.gwt.deposit.payment.intf.client.PayDepositUrlParams;
import com.hd123.m3.account.service.deposit.Deposits;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * @author chenpeisi
 * 
 */
public class PayDepositQueryBuilder extends FlecsConditionDecoderImpl {

  private static PayDepositQueryBuilder instance = null;

  public static PayDepositQueryBuilder getInstance() {
    if (instance == null)
      instance = new PayDepositQueryBuilder();
    return instance;
  }

  public PayDepositQueryBuilder() {
    super();
  }

  @Override
  protected String getDefaultOrderField() {
    return Deposits.FIELD_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (PayDepositUrlParams.Search.FIELD_BILLNUMBER.equals(fieldName.trim())) {
      return Deposits.FIELD_BILLNUMBER;
    } else if (PayDepositUrlParams.Search.FIELD_ACCOUNTUNIT.equals(fieldName.trim())) {
      return Deposits.FIELD_ACCOUNTUNIT;
    } else if (PayDepositUrlParams.Search.FIELD_COUNTERPART.equals(fieldName.trim())) {
      return Deposits.FIELD_COUNTERPART;
    } else if (PayDepositUrlParams.Search.FIELD_COUNTERPARTTYPE.equals(fieldName.trim())) {
      return Deposits.FIELD_COUNTERPART_TYPE;
    } else if (PayDepositUrlParams.Search.FIELD_SERIALNUMBER.equals(fieldName.trim())) {
      return Deposits.FIELD_CONTRACTNUMBER;
    } else if (PayDepositUrlParams.Search.FIELD_SERIALNAME.equals(fieldName.trim())) {
      return Deposits.FIELD_CONTRACTNAME;
    } else if (PayDepositUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return Deposits.FIELD_BIZSTATE;
    } else if (PayDepositUrlParams.Search.FIELD_CREATE_INFO_OPER_ID.equals(fieldName.trim())) {
      return Deposits.FIELD_CREATOR_CODE;
    } else if (PayDepositUrlParams.Search.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName.trim())) {
      return Deposits.FIELD_CREATOR_NAME;
    } else if (PayDepositUrlParams.Search.FIELD_DEPOSITDATE.equals(fieldName.trim())) {
      return Deposits.FIELD_DEPOSITDATE;
    } else if (PayDepositUrlParams.Search.FIELD_ACCOUNTDATE.equals(fieldName.trim())) {
      return Deposits.FIELD_ACCOUNTDATE;
    } else if (PayDepositUrlParams.Search.FIELD_SUBJECT.equals(fieldName.trim())) {
      return Deposits.FIELD_SUBJECT;
    } else if (PayDepositUrlParams.Search.FIELD_SETTLENO.equals(fieldName.trim())) {
      return Deposits.FIELD_SETTLENO;
    } else if (PayDepositUrlParams.Search.FIELD_PAYMENTTYPE.equals(fieldName.trim())) {
      return Deposits.FIELD_PAYMENTTYPE;
    } else if (PayDepositUrlParams.Search.FIELD_DEALER.equals(fieldName.trim())) {
      return Deposits.FIELD_DEALER;
    } else if (PayDepositUrlParams.Search.FIELD_CREATE_INFO_TIME.equals(fieldName.trim())) {
      return Deposits.FIELD_CREATED;
    } else if (PayDepositUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_ID.equals(fieldName.trim())) {
      return Deposits.FIELD_LAST_MODIFIER_CODE;
    } else if (PayDepositUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName.trim())) {
      return Deposits.FIELD_LAST_MODIFIER_NAME;
    } else if (PayDepositUrlParams.Search.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName.trim())) {
      return Deposits.FIELD_LAST_MODIFY_TIME;
    } else if (PayDepositUrlParams.Search.FIELD_PERMGROUP.equals(fieldName.trim())) {
      return Deposits.FIELD_PERMGROUP;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (PayDepositUrlParams.Search.FIELD_BILLNUMBER.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_BILLNUMBER;
    }else if (PayDepositUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_BIZSTATE;
    } else if (PayDepositUrlParams.Search.FIELD_BPMSTATE.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_BPMSTATE;
    } else if (PayDepositUrlParams.Search.FIELD_ACCOUNTUNIT.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_ACCOUNTUNIT;
    } else if (PayDepositUrlParams.Search.FIELD_COUNTERPART.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_COUNTERPART;
    } else if (PayDepositUrlParams.Search.FIELD_SERIALNUMBER.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_CONTRACTNUMBER;
    } else if (PayDepositUrlParams.Search.FIELD_SERIALNAME.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_CONTRACTNAME;
    } else if (PayDepositUrlParams.Search.FIELD_PAYTOTAL.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_DEPOSITTOTAL;
    } else if (PayDepositUrlParams.Search.FIELD_PAYMENTTYPE.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_PAYMENTTYPE;
    } else if (PayDepositUrlParams.Search.FIELD_DEPOSITDATE.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_DEPOSITDATE;
    } else if (PayDepositUrlParams.Search.FIELD_ACCOUNTDATE.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_ACCOUNTDATE;
    } else if (PayDepositUrlParams.Search.FIELD_DEALER.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_DEALER;
    } else if (PayDepositUrlParams.Search.FIELD_COUNTERCONTACT.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_COUNTERCONTACT;
    } else if (PayDepositUrlParams.Search.FIELD_SETTLENO.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_SETTLENO;
    }
    return null;
  }

}
