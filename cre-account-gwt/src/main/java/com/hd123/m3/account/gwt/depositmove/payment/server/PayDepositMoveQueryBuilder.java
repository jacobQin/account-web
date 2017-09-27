/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PayDepositMoveQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-21 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.payment.server;

import com.hd123.m3.account.gwt.deposit.payment.intf.client.PayDepositUrlParams;
import com.hd123.m3.account.gwt.depositmove.payment.intf.client.PayDepositMoveUrlParams;
import com.hd123.m3.account.service.depositmove.DepositMoves;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * @author chenpeisi
 * 
 */
public class PayDepositMoveQueryBuilder extends FlecsConditionDecoderImpl {

  private static PayDepositMoveQueryBuilder instance = null;

  public static PayDepositMoveQueryBuilder getInstance() {
    if (instance == null)
      instance = new PayDepositMoveQueryBuilder();
    return instance;
  }

  public PayDepositMoveQueryBuilder() {
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

    if (PayDepositMoveUrlParams.Search.FIELD_BILLNUMBER.equals(fieldName.trim())) {
      return DepositMoves.FIELD_BILLNUMBER;
    }else if (PayDepositMoveUrlParams.Search.FIELD_OUTCOUNTERPARTTYPE.equals(fieldName.trim())) {
      return DepositMoves.FIELD_OUTCOUNTERPART_TYPE;
    } else if (PayDepositMoveUrlParams.Search.FIELD_OUTCOUNTERPART.equals(fieldName.trim())) {
      return DepositMoves.FIELD_OUTCOUNTERPART;
    } else if (PayDepositMoveUrlParams.Search.FIELD_INCOUNTERPART.equals(fieldName.trim())) {
      return DepositMoves.FIELD_INCOUNTERPART;
    } else if (PayDepositMoveUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return DepositMoves.FIELD_BIZSTATE;
    } else if (PayDepositMoveUrlParams.Search.FIELD_OUTCONTRACTNUMBER.equals(fieldName.trim())) {
      return DepositMoves.FIELD_OUTCONTRACTNUMBER;
    } else if (PayDepositMoveUrlParams.Search.FIELD_OUTCONTRACTNAME.equals(fieldName.trim())) {
      return DepositMoves.FIELD_OUTCONTRACTNAME;
    }else if (PayDepositMoveUrlParams.Search.FIELD_INCOUNTERPARTTYPE.equals(fieldName.trim())) {
      return DepositMoves.FIELD_INCOUNTERPART_TYPE;
    } else if (PayDepositMoveUrlParams.Search.FIELD_INCONTRACTNUMBER.equals(fieldName.trim())) {
      return DepositMoves.FIELD_INCONTRACTNUMBER;
    } else if (PayDepositMoveUrlParams.Search.FIELD_INCONTRACTNAME.equals(fieldName.trim())) {
      return DepositMoves.FIELD_INCONTRACTNAME;
    } else if (PayDepositMoveUrlParams.Search.FIELD_OUTSUBJECT.equals(fieldName.trim())) {
      return DepositMoves.FIELD_OUTSUBJECT;
    }else if (PayDepositMoveUrlParams.Search.FIELD_ACCOUNTDATE.equals(fieldName.trim())) {
      return DepositMoves.FIELD_ACCOUNTDATE;
    }  else if (PayDepositMoveUrlParams.Search.FIELD_INSUBJECT.equals(fieldName.trim())) {
      return DepositMoves.FIELD_INSUBJECT;
    } else if (PayDepositMoveUrlParams.Search.FIELD_SETTLENO.equals(fieldName.trim())) {
      return DepositMoves.FIELD_SETTLENO;
    } else if (PayDepositMoveUrlParams.Search.FIELD_CREATOR_CODE.equals(fieldName.trim())) {
      return DepositMoves.FIELD_CREATOR_CODE;
    } else if (PayDepositMoveUrlParams.Search.FIELD_CREATOR_NAME.equals(fieldName.trim())) {
      return DepositMoves.FIELD_CREATOR_NAME;
    } else if (PayDepositMoveUrlParams.Search.FIELD_CREATED.equals(fieldName.trim())) {
      return DepositMoves.FIELD_CREATED;
    } else if (PayDepositMoveUrlParams.Search.FIELD_LAST_MODIFIER_CODE.equals(fieldName.trim())) {
      return DepositMoves.FIELD_LAST_MODIFIER_CODE;
    } else if (PayDepositMoveUrlParams.Search.FIELD_LAST_MODIFIER_NAME.equals(fieldName.trim())) {
      return DepositMoves.FIELD_LAST_MODIFIER_NAME;
    } else if (PayDepositMoveUrlParams.Search.FIELD_LAST_MODIFY_TIME.equals(fieldName.trim())) {
      return DepositMoves.FIELD_LAST_MODIFY_TIME;
    } else if (PayDepositMoveUrlParams.Search.FIELD_BUSINESSUNIT.equals(fieldName.trim())) {
      return DepositMoves.FIELD_BUSINESS;
    } else if (PayDepositMoveUrlParams.Search.FIELD_PERMGROUP.equals(fieldName.trim())) {
      return DepositMoves.FIELD_PERMGROUP;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (PayDepositMoveUrlParams.Search.FIELD_BILLNUMBER.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_BILLNUMBER;
    } else if (PayDepositMoveUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_BIZSTATE;
    } else if (PayDepositMoveUrlParams.Search.FIELD_OUTCOUNTERPART.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_OUTCOUNTERPART;
    } else if (PayDepositMoveUrlParams.Search.FIELD_OUTCONTRACTNUMBER.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_OUTCONTRACTNUMBER;
    } else if (PayDepositMoveUrlParams.Search.FIELD_OUTCONTRACTNAME.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_OUTCONTRACTNAME;
    } else if (PayDepositMoveUrlParams.Search.FIELD_OUTSUBJECT.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_OUTSUBJECT;
    } else if (PayDepositMoveUrlParams.Search.FIELD_ACCOUNTDATE.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_ACCOUNTDATE;
    } else if (PayDepositMoveUrlParams.Search.FIELD_AMOUNT.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_MOVETOTAL;
    } else if (PayDepositMoveUrlParams.Search.FIELD_INCOUNTERPART.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_INCOUNTERPART;
    } else if (PayDepositMoveUrlParams.Search.FIELD_INSUBJECT.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_INSUBJECT;
    } else if (PayDepositMoveUrlParams.Search.FIELD_INCONTRACTNUMBER.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_INCONTRACTNUMBER;
    } else if (PayDepositMoveUrlParams.Search.FIELD_INCONTRACTNAME.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_INCONTRACTNAME;
    } else if (PayDepositMoveUrlParams.Search.FIELD_REMARK.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_REMARK;
    } else if (PayDepositUrlParams.Search.FIELD_SETTLENO.equals(fieldName.trim())) {
      return DepositMoves.ORDER_BY_SETTLENO;
    }
    return null;
  }
}
