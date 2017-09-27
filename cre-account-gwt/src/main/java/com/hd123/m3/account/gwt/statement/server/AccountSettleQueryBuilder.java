/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AccountSettleQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2014年9月19日 - zhr - 创建。
 */
package com.hd123.m3.account.gwt.statement.server;

import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.m3.account.service.contract.AccountSettles;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * @author zhr
 * 
 */
public class AccountSettleQueryBuilder extends FlecsConditionDecoderImpl {
  private static AccountSettleQueryBuilder instance;

  public static AccountSettleQueryBuilder getInstance() {
    if (instance == null)
      instance = new AccountSettleQueryBuilder();
    return instance;
  }

  @Override
  protected String getDefaultOrderField() {
    return AccountSettles.FIELD_ACCOUNTUNIT;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (StatementUrlParams.AccSettleLog.FIELD_ACCOUNTUNIT.equals(fieldName))
      return AccountSettles.FIELD_ACCOUNTUNIT;
    else if (StatementUrlParams.AccSettleLog.FIELD_COUNTERPARTTYPE.equals(fieldName))
      return AccountSettles.FIELD_COUNTERPART_TYPE;
    else if (StatementUrlParams.AccSettleLog.FIELD_COUNTERPART.equals(fieldName))
      return AccountSettles.FIELD_COUNTERPART;
    else if (StatementUrlParams.AccSettleLog.FIELD_CONTRACTNUMBER.equals(fieldName))
      return AccountSettles.FIELD_CONTRACTNUMBER;
    else if (StatementUrlParams.AccSettleLog.FIELD_CONTRACTTITLE.equals(fieldName))
      return AccountSettles.FIELD_CONTRACTTITLE;
    else if (StatementUrlParams.AccSettleLog.FIELD_FLOOR_CODE.equals(fieldName))
      return AccountSettles.FIELD_FLOOR_CODE;
    else if (StatementUrlParams.AccSettleLog.FIELD_FLOOR_NAME.equals(fieldName))
      return AccountSettles.FIELD_FLOOR_NAME;
    else if (StatementUrlParams.AccSettleLog.FIELD_POSITION_CODE.equals(fieldName))
      return AccountSettles.FIELD_POSITION_CODE;
    else if (StatementUrlParams.AccSettleLog.FIELD_POSITION_NAME.equals(fieldName))
      return AccountSettles.FIELD_POSITION_NAME;
    else if (StatementUrlParams.AccSettleLog.FIELD_COOPMODE.equals(fieldName))
      return AccountSettles.FIELD_COOPMODE;
    else if (StatementUrlParams.AccSettleLog.FIELD_SETTLEMENTCAPTION.equals(fieldName))
      return AccountSettles.FIELD_SETTLE_CAPTION;
    else if (StatementUrlParams.AccSettleLog.FIELD_SETTLEMENTBEGINDATE.equals(fieldName))
      return AccountSettles.FIELD_BEGINDATE;
    else if (StatementUrlParams.AccSettleLog.FIELD_SETTLEMENTENDDATE.equals(fieldName))
      return AccountSettles.FIELD_ENDDATE;
    else if (StatementUrlParams.AccSettleLog.FIELD_PLANDATE.equals(fieldName))
      return AccountSettles.FIELD_PLANDATE;
    else if (StatementUrlParams.AccSettleLog.FIELD_ACCOUNTTIME.equals(fieldName))
      return AccountSettles.FIELD_ACCOUNTTIME;
    else if (StatementUrlParams.AccSettleLog.FIELD_BILLCALCULATETYPE.equals(fieldName))
      return AccountSettles.FIELD_BILLCALCULATETYPE;
    else if (StatementUrlParams.AccSettleLog.FIELD_ISACCOUNTSETTLE.equals(fieldName))
      return AccountSettles.FIELD_ISACCOUNTSETTLE;
    else if (StatementUrlParams.AccSettleLog.FIELD_BILLNUMBER.equals(fieldName))
      return AccountSettles.FIELD_STATEMENTNUM;
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (StatementUrlParams.AccSettleLog.FIELD_ACCOUNTUNIT.equals(fieldName))
      return AccountSettles.ORDER_BY_ACCOUNT;
    else if (StatementUrlParams.AccSettleLog.FIELD_COUNTERPART.equals(fieldName))
      return AccountSettles.ORDER_BY_COUNTERPART;
    else if (StatementUrlParams.AccSettleLog.FIELD_CONTRACTTITLE.equals(fieldName))
      return AccountSettles.ORDER_BY_CONTRACTTITLE;
    else if (StatementUrlParams.AccSettleLog.FIELD_CONTRACTNUMBER.equals(fieldName))
      return AccountSettles.ORDER_BY_CONTRACTNUMBER;
    else if (StatementUrlParams.AccSettleLog.FIELD_FLOOR.equals(fieldName))
      return AccountSettles.ORDER_BY_FLOOR;
    else if (StatementUrlParams.AccSettleLog.FIELD_COOPMODE.equals(fieldName))
      return AccountSettles.ORDER_BY_COOPMODE;
    else if (StatementUrlParams.AccSettleLog.FIELD_SETTLEMENTCAPTION.equals(fieldName))
      return AccountSettles.ORDER_BY_SETTLE_CAPTION;
    else if (StatementUrlParams.AccSettleLog.FIELD_SETTLEMENTRANGEDATE.equals(fieldName))
      return AccountSettles.ORDER_BY_BEGINEDATE;
    else if (StatementUrlParams.AccSettleLog.FIELD_PLANDATE.equals(fieldName))
      return AccountSettles.ORDER_BY_PLANDATE;
    else if (StatementUrlParams.AccSettleLog.FIELD_ACCOUNTTIME.equals(fieldName))
      return AccountSettles.ORDER_BY_ACCOUNTTIME;
    else if (StatementUrlParams.AccSettleLog.FIELD_BILLCALCULATETYPE.equals(fieldName))
      return AccountSettles.ORDER_BY_BILLCALCULATETYPE;
    else if (StatementUrlParams.AccSettleLog.FIELD_BILLNUMBER.equals(fieldName))
      return AccountSettles.ORDER_BY_STATEMENTNUM;
    return null;
  }
}
