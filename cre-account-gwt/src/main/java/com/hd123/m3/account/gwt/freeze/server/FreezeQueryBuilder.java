/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	FreezeQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-26 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.freeze.server;

import com.hd123.m3.account.gwt.freeze.intf.client.FreezeUrlParams;
import com.hd123.m3.account.service.freeze.Freezes;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * 账款冻结单flecs查询构造器
 * 
 * @author zhuhairui
 * 
 */
public class FreezeQueryBuilder extends FlecsConditionDecoderImpl {

  private static FreezeQueryBuilder instance;

  public static FreezeQueryBuilder getInstance() {
    if (instance == null)
      instance = new FreezeQueryBuilder();
    return instance;
  }

  @Override
  protected String getDefaultOrderField() {
    return Freezes.FIELD_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    String field = fieldName.trim();
    if (FreezeUrlParams.Search.FIELD_BILLNUMBER.equals(field))
      return Freezes.FIELD_BILLNUMBER;
    else if (FreezeUrlParams.Search.FIELD_STATE.equals(field))
      return Freezes.FIELD_STATE;
    else if (FreezeUrlParams.Search.FIELD_COUNTERPARTTYPE.equals(field))
      return Freezes.FIELD_COUNTERPART_TYPE;
    else if (FreezeUrlParams.Search.FIELD_COUNTERPART.equals(field))
      return Freezes.FIELD_COUNTERPART;
    else if (FreezeUrlParams.Search.FIELD_CONTRACT_BILLNUMBER.equals(field))
      return Freezes.FIELD_CONTRACT_BILLNUMBER;
    else if (FreezeUrlParams.Search.FIELD_FREEZE_DATE.equals(field))
      return Freezes.FIELD_FREEZE_DATE;
    else if (FreezeUrlParams.Search.FIELD_UNFREEZE_DATE.equals(field))
      return Freezes.FIELD_UNFREEZE_DATE;
    else if (FreezeUrlParams.Search.FIELD_BUSINESSUNIT.equals(field))
      return Freezes.FIELD_BUSINESSUNIT;
    else if (FreezeUrlParams.Search.FIELD_CRETOR_CODE.equals(field))
      return Freezes.FIELD_CREATOR_CODE;
    else if (FreezeUrlParams.Search.FIELD_CRETOR_NAME.equals(field))
      return Freezes.FIELD_CREATOR_NAME;
    else if (FreezeUrlParams.Search.FIELD_CREATED.equals(field))
      return Freezes.FIELD_CREATED;
    else if (FreezeUrlParams.Search.FIELD_LASTMODIFIER_CODE.equals(field))
      return Freezes.FIELD_LAST_MODIFIER_CODE;
    else if (FreezeUrlParams.Search.FIELD_LASTMODIFIER_NAME.equals(field))
      return Freezes.FIELD_LAST_MODIFIER_NAME;
    else if (FreezeUrlParams.Search.FIELD_LASTMODIFIED.equals(field))
      return Freezes.FIELD_LAST_MODIFY_TIME;
    else if (FreezeUrlParams.Search.FIELD_PERMGROUP.equals(field))
      return Freezes.FIELD_PERMGROUP;
    else
      return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    String field = fieldName.trim();
    if (FreezeUrlParams.Search.FIELD_BILLNUMBER.equals(field))
      return Freezes.ORDER_BY_BILLNUMBER;
    else if (FreezeUrlParams.Search.FIELD_STATE.equals(field))
      return Freezes.ORDER_BY_STATE;
    else if (FreezeUrlParams.Search.FIELD_COUNTERPART.equals(field))
      return Freezes.FIELD_COUNTERPART;
    else if (FreezeUrlParams.Search.FIELD_CONTRACT_PAYMENTTOTAL.equals(field)) {
      return Freezes.ORDER_BY_PAYMENTTOTAL;
    } else if (FreezeUrlParams.Search.FIELD_CONTRACT_RECEIPTTOTAL.equals(field)) {
      return Freezes.ORDER_BY_RECEIPTTOTAL;
    } else if (FreezeUrlParams.Search.FIELD_FREEZE_DATE.equals(field)) {
      return Freezes.FIELD_FREEZE_DATE;
    } else if (FreezeUrlParams.Search.FIELD_UNFREEZE_DATE.equals(field)) {
      return Freezes.FIELD_UNFREEZE_DATE;
    }
    return null;
  }

}
