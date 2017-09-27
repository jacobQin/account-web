/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InternalFeeQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.server;

import com.hd123.m3.account.gwt.internalfee.intf.client.InternalFeeUrlParams;
import com.hd123.m3.account.service.internalfee.InternalFees;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * @author liuguilin
 * 
 */
public class InternalFeeQueryBuilder extends FlecsConditionDecoderImpl {

  private static InternalFeeQueryBuilder instance;

  public static InternalFeeQueryBuilder getInstance() {
    if (instance == null)
      instance = new InternalFeeQueryBuilder();
    return instance;
  }

  public InternalFeeQueryBuilder() {
    super();
  }

  @Override
  protected String getDefaultOrderField() {
    return InternalFees.FIELD_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (fieldName.trim().equals(InternalFeeUrlParams.Search.FIELD_BILLNUMBER))
      return InternalFees.FIELD_BILLNUMBER;
    else if (fieldName.trim().equals(InternalFeeUrlParams.Search.FIELD_BIZSTATE))
      return InternalFees.FIELD_BIZSTATE;
    else if (fieldName.trim().equals(InternalFeeUrlParams.Search.FIELD_BPMSTATE))
      return InternalFees.FIELD_BPMSTATE;
    else if (fieldName.trim().equals(InternalFeeUrlParams.Search.FIELD_ACCOUNTDATE))
      return InternalFees.FIELD_ACCOUNTDATE;
    else if (fieldName.trim().equals(InternalFeeUrlParams.Search.FIELD_SUBJECT))
      return InternalFees.FIELD_SUBJECT;
    else if (fieldName.trim().equals(InternalFeeUrlParams.Search.FIELD_SETTLENO))
      return InternalFees.FIELD_SETTLENO;
    else if (fieldName.trim().equals(InternalFeeUrlParams.Search.FIELD_PERMGROUP))
      return InternalFees.FIELD_PERMGROUP;
    else if (fieldName.trim().equals(InternalFeeUrlParams.Search.FIELD_STORE))
      return InternalFees.FIELD_STORE;
    else if (fieldName.trim().equals(InternalFeeUrlParams.Search.FIELD_VENDOR))
      return InternalFees.FIELD_VENDOR;
    else if (InternalFeeUrlParams.Flecs.FIELD_CREATE_INFO_OPER_ID.equals(fieldName))
      return InternalFees.FIELD_CREATOR_CODE;
    else if (InternalFeeUrlParams.Flecs.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName))
      return InternalFees.FIELD_CREATOR_NAME;
    else if (InternalFeeUrlParams.Flecs.FIELD_CREATE_INFO_TIME.equals(fieldName))
      return InternalFees.FIELD_CREATED;
    else if (InternalFeeUrlParams.Flecs.FIELD_LASTMODIFY_INFO_OPER_ID.equals(fieldName))
      return InternalFees.FIELD_LAST_MODIFIER_CODE;
    else if (InternalFeeUrlParams.Flecs.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName))
      return InternalFees.FIELD_LAST_MODIFIER_NAME;
    else if (InternalFeeUrlParams.Flecs.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName))
      return InternalFees.FIELD_LAST_MODIFY_TIME;
    else
      return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (fieldName.trim().equals(InternalFeeUrlParams.Search.FIELD_BILLNUMBER))
      return InternalFees.ORDER_BY_BILLNUMBER;
    else if (fieldName.trim().equals(InternalFeeUrlParams.Search.FIELD_BIZSTATE))
      return InternalFees.ORDER_BY_BIZSTATE;
    else if (fieldName.trim().equals(InternalFeeUrlParams.Search.FIELD_STORE))
      return InternalFees.ORDER_BY_STORE;
    else if (fieldName.trim().equals(InternalFeeUrlParams.Search.FIELD_VENDOR))
      return InternalFees.ORDER_BY_VENDOR;
    else if (fieldName.trim().equals(InternalFeeUrlParams.Search.FIELD_DIRECTION))
      return InternalFees.ORDER_BY_DIRECTION;
    else if (fieldName.trim().equals(InternalFeeUrlParams.Search.FIELD_ACCOUNTDATE))
      return InternalFees.ORDER_BY_ACCOUNTDATE;
    else if (fieldName.trim().equals(InternalFeeUrlParams.Search.FIELD_TOTAL))
      return InternalFees.ORDER_BY_TOTAL;
    else if (fieldName.trim().equals(InternalFeeUrlParams.Search.FIELD_REMARK))
      return InternalFees.ORDER_BY_REMARK;
    else
      return null;
  }
}
