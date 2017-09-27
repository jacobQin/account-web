/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	PayDepositRepaymentQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-20 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.payment.server;

import com.hd123.m3.account.gwt.depositrepayment.payment.intf.client.PayDepositRepaymentUrlParams;
import com.hd123.m3.account.service.depositrepayment.DepositRepayments;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * @author zhuhairui
 * 
 */
public class PayDepositRepaymentQueryBuilder extends FlecsConditionDecoderImpl {
  private static PayDepositRepaymentQueryBuilder instance = null;

  public static PayDepositRepaymentQueryBuilder getInstance() {
    if (instance == null)
      instance = new PayDepositRepaymentQueryBuilder();
    return instance;
  }

  public PayDepositRepaymentQueryBuilder() {
    super();
  }

  @Override
  protected String getDefaultOrderField() {
    return DepositRepayments.FIELD_BILLNUMBER;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (PayDepositRepaymentUrlParams.Search.FIELD_BILLNUMBER.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_BILLNUMBER;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_COUNTERPARTTYPE.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_COUNTERPART_TYPE;
    }  else if (PayDepositRepaymentUrlParams.Search.FIELD_COUNTERPART.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_COUNTERPART;
    }else if (PayDepositRepaymentUrlParams.Search.FIELD_CONTRACTNUMBER.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_CONTRACTNUMBER;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_CONTRACTNAME.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_CONTRACTNAME;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_BIZSTATE;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_CREATE_INFO_OPER_ID.equals(fieldName
        .trim())) {
      return DepositRepayments.FIELD_CREATOR_CODE;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName
        .trim())) {
      return DepositRepayments.FIELD_CREATOR_NAME;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_REPAYMENTDATE.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_REPAYMENTDATE;
    }else if (PayDepositRepaymentUrlParams.Search.FIELD_ACCOUNTDATE.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_ACCOUNTDATE;
    }  else if (PayDepositRepaymentUrlParams.Search.FIELD_SUBJECT.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_SUBJECT;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_SETTLENO.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_SETTLENO;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_PAYMENTTYPE.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_PAYMENTTYPE;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_DEALER.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_DEALER;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_PERMGROUP.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_PERMGROUP;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_CREATE_INFO_TIME.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_CREATED;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_ID.equals(fieldName
        .trim())) {
      return DepositRepayments.FIELD_LAST_MODIFIER_CODE;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName
        .trim())) {
      return DepositRepayments.FIELD_LAST_MODIFIER_NAME;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName
        .trim())) {
      return DepositRepayments.FIELD_LAST_MODIFY_TIME;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_BUSINESSUNIT.equals(fieldName.trim())) {
      return DepositRepayments.FIELD_BUSINESS;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (PayDepositRepaymentUrlParams.Search.FIELD_BILLNUMBER.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_BILLNUMBER;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_BIZSTATE.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_BIZSTATE;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_COUNTERPART.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_COUNTERPART;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_CONTRACTNUMBER.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_CONTRACTNUMBER;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_CONTRACTNAME.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_CONTRACTNAME;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_REPAYMENTTOTAL.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_REPAYMENTTOTAL;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_PAYMENTTYPE.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_PAYMENTTYPE;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_REPAYMENTDATE.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_REPAYMENTDATE;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_ACCOUNTDATE.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_ACCOUNTDATE;
    }  else if (PayDepositRepaymentUrlParams.Search.FIELD_DEALER.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_DEALER;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_COUNTERCONTACT.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_COUNTERCONTACT;
    } else if (PayDepositRepaymentUrlParams.Search.FIELD_SETTLENO.equals(fieldName.trim())) {
      return DepositRepayments.ORDER_BY_SETTLENO;
    }
    return null;
  }

}
