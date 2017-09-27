/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementAdjustQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-22 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.server;

import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustUrlParams;
import com.hd123.m3.account.service.statement.adjust.StatementAdjusts;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * 账单调整单flecs查询构造器
 * 
 * @author zhuhairui
 * 
 */
public class StatementAdjustQueryBuilder extends FlecsConditionDecoderImpl {

  private static StatementAdjustQueryBuilder instance = null;

  public static StatementAdjustQueryBuilder getInstance() {
    if (instance == null)
      instance = new StatementAdjustQueryBuilder();
    return instance;
  }

  @Override
  protected String getDefaultOrderField() {
    return StatementAdjusts.FIELD_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    String field = fieldName.trim();
    if (StatementAdjustUrlParams.Search.FIELD_BILLNUMBER.equals(field))
      return StatementAdjusts.FIELD_BILLNUMBER;
    else if (StatementAdjustUrlParams.Search.FIELD_BIZSTATE.equals(field))
      return StatementAdjusts.FIELD_BIZSTATE;
    else if (StatementAdjustUrlParams.Search.FIELD_SETTLENO.equals(field))
      return StatementAdjusts.FIELD_SETTLENO;
    else if (StatementAdjustUrlParams.Search.FIELD_COUNTERPARTTYPE.equals(field))
      return StatementAdjusts.FIELD_COUNTERPART_TYPE;
    else if (StatementAdjustUrlParams.Search.FIELD_COUNTERPART.equals(field))
      return StatementAdjusts.FIELD_COUNTERPART;
    else if (StatementAdjustUrlParams.Search.FIELD_ACCOUNTUNIT.equals(field))
      return StatementAdjusts.FIELD_ACCOUNTUNIT;
    else if (StatementAdjustUrlParams.Search.FIELD_CONTRACTBILLNUMBER.equals(field))
      return StatementAdjusts.FIELD_CONTRACTNUMBER;
    else if (StatementAdjustUrlParams.Search.FIELD_CONTRACTNAME.equals(field))
      return StatementAdjusts.FIELD_CONTRACTNAME;
    else if (StatementAdjustUrlParams.Search.FIELD_STATEMENTBILLNUMBER.equals(field))
      return StatementAdjusts.FIELD_STATEMENTNUMBER;
    else if (StatementAdjustUrlParams.Search.FIELD_SUBJECT.equals(field))
      return StatementAdjusts.FIELD_SUBJECT;
    else if (StatementAdjustUrlParams.Search.FIELD_CRETOR_CODE.equals(field))
      return StatementAdjusts.FIELD_CRETOR_CODE;
    else if (StatementAdjustUrlParams.Search.FIELD_CRETOR_NAME.equals(field))
      return StatementAdjusts.FIELD_CRETOR_NAME;
    else if (StatementAdjustUrlParams.Search.FIELD_CREATED.equals(field))
      return StatementAdjusts.FIELD_CREATED;
    else if (StatementAdjustUrlParams.Search.FIELD_LASTMODIFIER_CODE.equals(field))
      return StatementAdjusts.FIELD_LASTMODIFIER_CODE;
    else if (StatementAdjustUrlParams.Search.FIELD_LASTMODIFIER_NAME.equals(field))
      return StatementAdjusts.FIELD_LASTMODIFIER_NAME;
    else if (StatementAdjustUrlParams.Search.FIELD_LASTMODIFIED.equals(field))
      return StatementAdjusts.FIELD_LASTMODIFIED;
    else if (StatementAdjustUrlParams.Search.FIELD_PERMGROUP.equals(field))
      return StatementAdjusts.FIELD_PERMGROUP;
    else
      return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    String field = fieldName.trim();

    if (StatementAdjustUrlParams.Search.FIELD_BILLNUMBER.equals(field))
      return StatementAdjusts.ORDER_BY_BILLNUMBER;
    else if (StatementAdjustUrlParams.Search.FIELD_BIZSTATE.equals(field))
      return StatementAdjusts.ORDER_BY_BIZSTATE;
    else if (StatementAdjustUrlParams.Search.FIELD_COUNTERPART.equals(field))
      return StatementAdjusts.ORDER_BY_COUNTERPARTCODE;
    else if (StatementAdjustUrlParams.Search.FIELD_CONTRACTBILLNUMBER.equals(field))
      return StatementAdjusts.ORDER_BY_CONTRACTNUMBER;
    else if (StatementAdjustUrlParams.Search.FIELD_CONTRACTNAME.equals(field))
      return StatementAdjusts.ORDER_BY_CONTRACTNAME;
    else if (StatementAdjustUrlParams.Search.FIELD_STATEMENTBILLNUMBER.equals(field))
      return StatementAdjusts.ORDER_BY_STATEMENTNUMBER;
    else if (StatementAdjustUrlParams.Search.FIELD_SETTLENO.equals(field))
      return StatementAdjusts.ORDER_BY_SETTLENO;
    else
      return null;
  }

}
