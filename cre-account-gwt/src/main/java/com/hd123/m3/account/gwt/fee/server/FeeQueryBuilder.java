/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	FeeQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-19 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.server;

import com.hd123.m3.account.gwt.fee.intf.client.FeeUrlParams;
import com.hd123.m3.account.service.fee.Fees;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * @author subinzhu
 * 
 */
public class FeeQueryBuilder extends FlecsConditionDecoderImpl {

  private static FeeQueryBuilder instance;

  public static FeeQueryBuilder getInstance() {
    if (instance == null)
      instance = new FeeQueryBuilder();
    return instance;
  }

  public FeeQueryBuilder() {
    super();
  }

  @Override
  protected String getDefaultOrderField() {
    return Fees.FIELD_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_BILLNUMBER))
      return Fees.FIELD_BILLNUMBER;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_BIZSTATE))
      return Fees.FIELD_BIZSTATE;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_BPMSTATE))
      return Fees.FIELD_BPMSTATE;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_COUNTERPARTTYPE))
      return Fees.FIELD_COUNTERPART_TYPE;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_COUNTERPART))
      return Fees.FIELD_COUNTERPART;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_CONTRACT_CODE))
      return Fees.FIELD_CONTRACTNUMBER;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_CONTRACT_NAME))
      return Fees.FIELD_CONTRACTNAME;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_ACCOUNTDATE))
      return Fees.FIELD_ACCOUNTDATE;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_SUBJECT))
      return Fees.FIELD_SUBJECT;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_SETTLENO))
      return Fees.FIELD_SETTLENO;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_COOPMODE))
      return Fees.FIELD_COOPMODE;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_PERMGROUP))
      return Fees.FIELD_PERMGROUP;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_DIRECTION))
      return Fees.FIELD_DIRECTION;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_ACCOUNTUNIT))
      return Fees.FIELD_ACCOUNTUNIT;
    else if (FeeUrlParams.Flecs.FIELD_CREATE_INFO_OPER_ID.equals(fieldName))
      return Fees.FIELD_CREATOR_CODE;
    else if (FeeUrlParams.Flecs.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName))
      return Fees.FIELD_CREATOR_NAME;
    else if (FeeUrlParams.Flecs.FIELD_CREATE_INFO_TIME.equals(fieldName))
      return Fees.FIELD_CREATED;
    else if (FeeUrlParams.Flecs.FIELD_LASTMODIFY_INFO_OPER_ID.equals(fieldName))
      return Fees.FIELD_LAST_MODIFIER_CODE;
    else if (FeeUrlParams.Flecs.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName))
      return Fees.FIELD_LAST_MODIFIER_NAME;
    else if (FeeUrlParams.Flecs.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName))
      return Fees.FIELD_LAST_MODIFY_TIME;
    else
      return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_BILLNUMBER))
      return Fees.ORDER_BY_BILLNUMBER;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_BIZSTATE))
      return Fees.ORDER_BY_BIZSTATE;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_BPMSTATE))
      return Fees.ORDER_BY_BPMSTATE;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_COUNTERPART))
      return Fees.ORDER_BY_COUNTERPART;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_CONTRACT_CODE))
      return Fees.ORDER_BY_CONTRACT_NUMBER;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_CONTRACT_NAME))
      return Fees.ORDER_BY_CONTRACT_NAME;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_DIRECTION))
      return Fees.ORDER_BY_DIRECTION;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_ACCOUNTDATE))
      return Fees.ORDER_BY_ACCOUNTDATE;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_TOTAL))
      return Fees.ORDER_BY_TOTAL;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_REMARK))
      return Fees.ORDER_BY_REMARK;
    else if (fieldName.trim().equals(FeeUrlParams.Search.FIELD_ACCOUNTUNIT))
      return Fees.ORDER_BY_ACCOUNTUNIT;
    else
      return null;
  }

}
