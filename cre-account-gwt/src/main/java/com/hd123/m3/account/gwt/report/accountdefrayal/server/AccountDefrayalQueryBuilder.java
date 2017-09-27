/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountDefrayalQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2013-12-10 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.report.accountdefrayal.server;

import com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.AccountDefrayalUrlParams;
import com.hd123.m3.account.service.report.accountdefrayal.AccountDefrayals;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * 科目收付情况flecs查询构造器
 * 
 * @author zhuhairui
 * 
 */
public class AccountDefrayalQueryBuilder extends FlecsConditionDecoderImpl {

  private static AccountDefrayalQueryBuilder instance;

  public static AccountDefrayalQueryBuilder getInstance() {
    if (instance == null)
      instance = new AccountDefrayalQueryBuilder();
    return instance;
  }

  @Override
  protected String getDefaultOrderField() {
    return AccountDefrayals.FIELD_SOURCEBILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    String field = fieldName.trim();
    if (AccountDefrayalUrlParams.Search.FIELD_SOURCEBILLTYPE.equals(field))
      return AccountDefrayals.FIELD_SOURCEBILLTYPE;
    else if (AccountDefrayalUrlParams.Search.FIELD_COUNTERPARTTYPE.equals(field))
      return AccountDefrayals.FIELD_COUNTERPART_TYPE;
    else if (AccountDefrayalUrlParams.Search.FIELD_SOURCEBILLNUMBER.equals(field))
      return AccountDefrayals.FIELD_SOURCEBILLNUMBER;
    else if (AccountDefrayalUrlParams.Search.FIELD_SUBJECT.equals(field))
      return AccountDefrayals.FIELD_SUBJECT;
    else if (AccountDefrayalUrlParams.Search.FIELD_DIRECTION.equals(field))
      return AccountDefrayals.FIELD_DIRECTION;
    else if (AccountDefrayalUrlParams.Search.FIELD_COUNTERPART.equals(field))
      return AccountDefrayals.FIELD_COUNTERPART;
    else if (AccountDefrayalUrlParams.Search.FIELD_ACCOUNTUNIT.equals(field))
      return AccountDefrayals.FIELD_ACCOUNTUNIT;
    else if (AccountDefrayalUrlParams.Search.FIELD_CONTRACT_BILLNUMBER.equals(field))
      return AccountDefrayals.FIELD_CONTRACTNUMBER;
    else if (AccountDefrayalUrlParams.Search.FIELD_CONTRACT_NAME.equals(field))
      return AccountDefrayals.FIELD_CONTRACTNAME;
    else if (AccountDefrayalUrlParams.Search.FIELD_STATEMENTNUM.equals(field))
      return AccountDefrayals.FIELD_STATEMENTNUMBER;
    else if (AccountDefrayalUrlParams.Search.FIELD_ACCOUNTTIME.equals(field))
      return AccountDefrayals.FIELD_ACCOUNTTIME;
    else if (AccountDefrayalUrlParams.Search.FIELD_STATEMENTSETTLENO.equals(field))
      return AccountDefrayals.FIELD_STATEMENTSETTLENO;
    else if (AccountDefrayalUrlParams.Search.FIELD_SETTLED.equals(field))
      return AccountDefrayals.FIELD_SETTLED;
    else if (AccountDefrayalUrlParams.Search.FIELD_INVOICED.equals(field))
      return AccountDefrayals.FIELD_INVOICED;
    else
      return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    String field = fieldName.trim();

    if (AccountDefrayalUrlParams.Search.FIELD_SOURCEBILLNUMBER.equals(field))
      return AccountDefrayals.ORDER_BY_SOURCEBILLNUMBER;
    else if (AccountDefrayalUrlParams.Search.FIELD_SOURCEBILLTYPE.equals(field))
      return AccountDefrayals.ORDER_BY_SOURCEBILLTYPE;
    else if (AccountDefrayalUrlParams.Search.FIELD_SUBJECT.equals(field))
      return AccountDefrayals.ORDER_BY_SUBJECT;
    else if (AccountDefrayalUrlParams.Search.FIELD_DIRECTION.equals(field))
      return AccountDefrayals.ORDER_BY_DIRECTION;
    else if (AccountDefrayalUrlParams.Search.FIELD_NEEDSETTLE.equals(field))
      return AccountDefrayals.ORDER_BY_NEEDSETTLE;
    else if (AccountDefrayalUrlParams.Search.FIELD_SETTLEADJ.equals(field))
      return AccountDefrayals.ORDER_BY_SETTLEADJ;
    else if (AccountDefrayalUrlParams.Search.FIELD_SETTLETOTAL.equals(field))
      return AccountDefrayals.ORDER_BY_SETTLED;
    else if (AccountDefrayalUrlParams.Search.FIELD_NEEDINVOICE.equals(field))
      return AccountDefrayals.ORDER_BY_NEEDINVOICE;
    else if (AccountDefrayalUrlParams.Search.FIELD_INVOICEADJ.equals(field))
      return AccountDefrayals.ORDER_BY_INVOICEADJ;
    else if (AccountDefrayalUrlParams.Search.FIELD_INVOICETOTAL.equals(field))
      return AccountDefrayals.ORDER_BY_INVOICED;
    else if (AccountDefrayalUrlParams.Search.FIELD_STATEMENTNUM.equals(field))
      return AccountDefrayals.ORDER_BY_STATEMENT;
    else if (AccountDefrayalUrlParams.Search.FIELD_DATERANGE.equals(field))
      return AccountDefrayals.ORDER_BY_DATERANGE;
    else if (AccountDefrayalUrlParams.Search.FIELD_LASTRECEIPTDATE.equals(field))
      return AccountDefrayals.ORDER_BY_LASTPAYDATE;
    else if (AccountDefrayalUrlParams.Search.FIELD_CONTRACT_BILLNUMBER.equals(field))
      return AccountDefrayals.ORDER_BY_CONTRACTNUMBER;
    else if (AccountDefrayalUrlParams.Search.FIELD_CONTRACT_NAME.equals(field))
      return AccountDefrayals.ORDER_BY_CONTRACTNAME;
    else if (AccountDefrayalUrlParams.Search.FIELD_COUNTERPART.equals(field))
      return AccountDefrayals.ORDER_BY_COUNTERPART;
    else if (AccountDefrayalUrlParams.Search.FIELD_ACCOUNTUNIT.equals(field))
      return AccountDefrayals.ORDER_BY_ACCOUNTUNIT;
    else
      return null;
  }
}
