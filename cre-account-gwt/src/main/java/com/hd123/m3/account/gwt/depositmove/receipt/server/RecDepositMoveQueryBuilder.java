/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	RecDepositMoveQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-28 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.receipt.server;

import com.hd123.m3.account.gwt.depositmove.receipt.intf.client.RecDepositMoveUrlParams;
import com.hd123.m3.account.service.depositmove.DepositMoves;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * @author zhuhairui
 * 
 */
public class RecDepositMoveQueryBuilder extends FlecsConditionDecoderImpl {
  private static RecDepositMoveQueryBuilder instance = null;

  public static RecDepositMoveQueryBuilder getInstance() {
    if (instance == null)
      instance = new RecDepositMoveQueryBuilder();
    return instance;
  }

  public RecDepositMoveQueryBuilder() {
    super();
  }

  @Override
  protected String getDefaultOrderField() {
    return DepositMoves.FIELD_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (RecDepositMoveUrlParams.Search.FIELD_BILLNUMBER.equals(fieldName.trim())) {
      return DepositMoves.FIELD_BILLNUMBER;
    } else if (RecDepositMoveUrlParams.Search.FIELD_OUTCOUNTERPARTTYPE.equals(fieldName.trim())) {
      return DepositMoves.FIELD_OUTCOUNTERPART_TYPE;
    } else if (RecDepositMoveUrlParams.Search.FIELD_OUTCOUNTERPART.equals(fieldName.trim())) {
      return DepositMoves.FIELD_OUTCOUNTERPART;
    } else if (RecDepositMoveUrlParams.Search.FIELD_INCOUNTERPART.equals(fieldName.trim())) {
      return DepositMoves.FIELD_INCOUNTERPART;
    } else if (RecDepositMoveUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return DepositMoves.FIELD_BIZSTATE;
    } else if (RecDepositMoveUrlParams.Search.FIELD_OUTCONTRACTNUMBER.equals(fieldName.trim())) {
      return DepositMoves.FIELD_OUTCONTRACTNUMBER;
    } else if (RecDepositMoveUrlParams.Search.FIELD_OUTCONTRACTNAME.equals(fieldName.trim())) {
      return DepositMoves.FIELD_OUTCONTRACTNAME;
    } else if (RecDepositMoveUrlParams.Search.FIELD_INCOUNTERPARTTYPE.equals(fieldName.trim())) {
      return DepositMoves.FIELD_INCOUNTERPART_TYPE;
    } else if (RecDepositMoveUrlParams.Search.FIELD_INCONTRACTNUMBER.equals(fieldName.trim())) {
      return DepositMoves.FIELD_INCONTRACTNUMBER;
    } else if (RecDepositMoveUrlParams.Search.FIELD_INCONTRACTNAME.equals(fieldName.trim())) {
      return DepositMoves.FIELD_INCONTRACTNAME;
    } else if (RecDepositMoveUrlParams.Search.FIELD_OUTSUBJECT.equals(fieldName.trim())) {
      return DepositMoves.FIELD_OUTSUBJECT;
    } else if (RecDepositMoveUrlParams.Search.FIELD_ACCOUNTDATE.equals(fieldName.trim())) {
      return DepositMoves.FIELD_ACCOUNTDATE;
    } else if (RecDepositMoveUrlParams.Search.FIELD_INSUBJECT.equals(fieldName.trim())) {
      return DepositMoves.FIELD_INSUBJECT;
    } else if (RecDepositMoveUrlParams.Search.FIELD_SETTLENO.equals(fieldName.trim())) {
      return DepositMoves.FIELD_SETTLENO;
    } else if (RecDepositMoveUrlParams.Search.FIELD_PERMGROUP.equals(fieldName.trim())) {
      return DepositMoves.FIELD_PERMGROUP;
    } else if (RecDepositMoveUrlParams.Search.FIELD_CREATOR_CODE.equals(fieldName.trim())) {
      return DepositMoves.FIELD_CREATOR_CODE;
    } else if (RecDepositMoveUrlParams.Search.FIELD_CREATOR_NAME.equals(fieldName.trim())) {
      return DepositMoves.FIELD_CREATOR_NAME;
    } else if (RecDepositMoveUrlParams.Search.FIELD_CREATED.equals(fieldName.trim())) {
      return DepositMoves.FIELD_CREATED;
    } else if (RecDepositMoveUrlParams.Search.FIELD_LAST_MODIFIER_CODE.equals(fieldName.trim())) {
      return DepositMoves.FIELD_LAST_MODIFIER_CODE;
    } else if (RecDepositMoveUrlParams.Search.FIELD_LAST_MODIFIER_NAME.equals(fieldName.trim())) {
      return DepositMoves.FIELD_LAST_MODIFIER_NAME;
    } else if (RecDepositMoveUrlParams.Search.FIELD_LAST_MODIFY_TIME.equals(fieldName.trim())) {
      return DepositMoves.FIELD_LAST_MODIFY_TIME;
    } else if (RecDepositMoveUrlParams.Search.FIELD_BUSINESSUNIT.equals(fieldName.trim())) {
      return DepositMoves.FIELD_BUSINESS;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (RecDepositMoveUrlParams.Search.FIELD_BILLNUMBER.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_BILLNUMBER;
    } else if (RecDepositMoveUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_BIZSTATE;
    } else if (RecDepositMoveUrlParams.Search.FIELD_OUTCOUNTERPART.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_OUTCOUNTERPART;
    } else if (RecDepositMoveUrlParams.Search.FIELD_OUTCONTRACTNUMBER.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_OUTCONTRACTNUMBER;
    } else if (RecDepositMoveUrlParams.Search.FIELD_OUTCONTRACTNAME.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_OUTCONTRACTNAME;
    } else if (RecDepositMoveUrlParams.Search.FIELD_OUTSUBJECT.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_OUTSUBJECT;
    }else if (RecDepositMoveUrlParams.Search.FIELD_ACCOUNTDATE.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_ACCOUNTDATE;
    } else if (RecDepositMoveUrlParams.Search.FIELD_AMOUNT.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_MOVETOTAL;
    } else if (RecDepositMoveUrlParams.Search.FIELD_INCOUNTERPART.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_INCOUNTERPART;
    } else if (RecDepositMoveUrlParams.Search.FIELD_INSUBJECT.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_INSUBJECT;
    } else if (RecDepositMoveUrlParams.Search.FIELD_INCONTRACTNUMBER.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_INCONTRACTNUMBER;
    } else if (RecDepositMoveUrlParams.Search.FIELD_INCONTRACTNAME.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_INCONTRACTNAME;
    } else if (RecDepositMoveUrlParams.Search.FIELD_REMARK.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_REMARK;
    } else if (RecDepositMoveUrlParams.Search.FIELD_SETTLENO.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_SETTLENO;
    }
    return null;
  }
}
