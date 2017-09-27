/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PayQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-16 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.receipt.server;

import com.hd123.m3.account.gwt.deposit.receipt.intf.client.RecDepositUrlParams;
import com.hd123.m3.account.service.deposit.Deposits;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * @author chenpeisi
 * 
 */
public class RecDepositQueryBuilder extends FlecsConditionDecoderImpl {

  private static RecDepositQueryBuilder instance = null;

  public static RecDepositQueryBuilder getInstance() {
    if (instance == null)
      instance = new RecDepositQueryBuilder();
    return instance;
  }

  public RecDepositQueryBuilder() {
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

    if (RecDepositUrlParams.Search.FIELD_BILLNUMBER.equals(fieldName.trim())) {
      return Deposits.FIELD_BILLNUMBER;
    } else if (RecDepositUrlParams.Search.FIELD_ACCOUNTUNIT.equals(fieldName.trim())) {
      return Deposits.FIELD_ACCOUNTUNIT;
    } else if (RecDepositUrlParams.Search.FIELD_COUNTERPART.equals(fieldName.trim())) {
      return Deposits.FIELD_COUNTERPART;
    } else if (RecDepositUrlParams.Search.FIELD_COUNTERPARTTYPE.equals(fieldName.trim())) {
      return Deposits.FIELD_COUNTERPART_TYPE;
    } else if (RecDepositUrlParams.Search.FIELD_SERIALNUMBER.equals(fieldName.trim())) {
      return Deposits.FIELD_CONTRACTNUMBER;
    } else if (RecDepositUrlParams.Search.FIELD_SERIALNAME.equals(fieldName.trim())) {
      return Deposits.FIELD_CONTRACTNAME;
    } else if (RecDepositUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return Deposits.FIELD_BIZSTATE;
    } else if (RecDepositUrlParams.Search.FIELD_CREATE_INFO_OPER_ID.equals(fieldName.trim())) {
      return Deposits.FIELD_CREATOR_CODE;
    } else if (RecDepositUrlParams.Search.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName.trim())) {
      return Deposits.FIELD_CREATOR_NAME;
    } else if (RecDepositUrlParams.Search.FIELD_DEPOSITDATE.equals(fieldName.trim())) {
      return Deposits.FIELD_DEPOSITDATE;
    }  else if (RecDepositUrlParams.Search.FIELD_ACCOUNTDATE.equals(fieldName.trim())) {
      return Deposits.FIELD_ACCOUNTDATE;
    } else if (RecDepositUrlParams.Search.FIELD_SUBJECT.equals(fieldName.trim())) {
      return Deposits.FIELD_SUBJECT;
    } else if (RecDepositUrlParams.Search.FIELD_SETTLENO.equals(fieldName.trim())) {
      return Deposits.FIELD_SETTLENO;
    } else if (RecDepositUrlParams.Search.FIELD_PAYMENTTYPE.equals(fieldName.trim())) {
      return Deposits.FIELD_PAYMENTTYPE;
    } else if (RecDepositUrlParams.Search.FIELD_DEALER.equals(fieldName.trim())) {
      return Deposits.FIELD_DEALER;
    } else if (RecDepositUrlParams.Search.FIELD_CREATE_INFO_TIME.equals(fieldName.trim())) {
      return Deposits.FIELD_CREATED;
    } else if (RecDepositUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_ID.equals(fieldName.trim())) {
      return Deposits.FIELD_LAST_MODIFIER_CODE;
    } else if (RecDepositUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName.trim())) {
      return Deposits.FIELD_LAST_MODIFIER_NAME;
    } else if (RecDepositUrlParams.Search.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName.trim())) {
      return Deposits.FIELD_LAST_MODIFY_TIME;
    } else if (RecDepositUrlParams.Search.FIELD_PERMGROUP.equals(fieldName.trim())) {
      return Deposits.FIELD_PERMGROUP;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (RecDepositUrlParams.Search.FIELD_BILLNUMBER.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_BILLNUMBER;
    } else if (RecDepositUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_BIZSTATE;
    } else if (RecDepositUrlParams.Search.FIELD_BPMSTATE.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_BPMSTATE;
    } else if (RecDepositUrlParams.Search.FIELD_ACCOUNTUNIT.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_ACCOUNTUNIT;
    } else if (RecDepositUrlParams.Search.FIELD_COUNTERPART.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_COUNTERPART;
    } else if (RecDepositUrlParams.Search.FIELD_SERIALNUMBER.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_CONTRACTNUMBER;
    } else if (RecDepositUrlParams.Search.FIELD_SERIALNAME.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_CONTRACTNAME;
    } else if (RecDepositUrlParams.Search.FIELD_PAYTOTAL.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_DEPOSITTOTAL;
    } else if (RecDepositUrlParams.Search.FIELD_PAYMENTTYPE.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_PAYMENTTYPE;
    } else if (RecDepositUrlParams.Search.FIELD_DEPOSITDATE.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_DEPOSITDATE;
    }  else if (RecDepositUrlParams.Search.FIELD_ACCOUNTDATE.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_ACCOUNTDATE;
    } else if (RecDepositUrlParams.Search.FIELD_DEALER.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_DEALER;
    } else if (RecDepositUrlParams.Search.FIELD_COUNTERCONTACT.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_COUNTERCONTACT;
    } else if (RecDepositUrlParams.Search.FIELD_SETTLENO.equals(fieldName.trim())) {
      return Deposits.ORDER_BY_SETTLENO;
    }
    return null;
  }

}
