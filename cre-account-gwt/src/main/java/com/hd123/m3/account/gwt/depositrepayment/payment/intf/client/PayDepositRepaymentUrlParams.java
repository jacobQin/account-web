/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PayDepositRepayment.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.payment.intf.client;

import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * 预付款还款单URL参数定义
 * 
 * @author chenpeisi
 * 
 */
public class PayDepositRepaymentUrlParams {
  public static final String MODULE_CAPTION = "预付款还款单";
  public static final String ENTRY_MODULE = "depositrepayment.payment.PayDepositRepayment";
  public static final String ENTRY_FILE = "PayDepositRepayment.html";
  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);

  public static interface Base {
    public static final String PN_ENTITY_UUID = "_uuid";
    public static final String PN_ENTITY_BILLNUMBER = "_billnumber";
  }

  public static interface Flecs {
    /** URL参数名：搜索条件。 */
    public static final String PN_FLECSCONFIG = "flecsconfig";
    /** URL参数名：页码 */
    public static final String PN_PAGE = "page";
    /** URL参数名：搜索关键字 */
    public static final String PN_KEYWORD = "keyword";
    /** 搜索条件的分组ID */
    public static final String GROUP_ID = "depositrepayment.PayDepositRepayment";

    /** 可搜索的字段名 */
    public static final String FIELD_BILLNUMBER = "billNumber";
    public static final String FIELD_COUNTERPARTTYPE = "counterpartType";
    public static final String FIELD_COUNTERPART = "counterpart";
    public static final String FIELD_CONTRACTNUMBER = "contract.code";
    public static final String FIELD_CONTRACTNAME = "contract.name";
    public static final String FIELD_BIZSTATE = "bizState";
    public static final String FIELD_REPAYMENTDATE = "repaymentDate";
    public static final String FIELD_ACCOUNTDATE = "accountDate";
    public static final String FIELD_SUBJECT = "subject";
    public static final String FIELD_SETTLENO = "settleNo";
    public static final String FIELD_BUSINESSUNIT = "businessUnit";
    public static final String FIELD_PAYMENTTYPE = "paymentType";
    public static final String FIELD_DEALER = "dealer";
    public static final String FIELD_SETTLEGROUP = "settleGroup";
    public static final String FIELD_CREATE_INFO_OPER_ID = "createInfo.operator.id";
    public static final String FIELD_CREATE_INFO_OPER_NAME = "createInfo.operator.fullName";
    public static final String FIELD_CREATE_INFO_TIME = "createInfo.time";
    public static final String FIELD_LASTMODIFY_INFO_OPER_ID = "lastModifyInfo.operator.id";
    public static final String FIELD_LASTMODIFY_INFO_OPER_NAME = "lastModifyInfo.operator.fullName";
    public static final String FIELD_LASTMODIFY_INFO_TIME = "lastModifyInfo.time";

    public static final String FIELD_REPAYMENTTOTAL = "repaymentTotal";
    public static final String FIELD_COUNTERCONTACT = "counterContact";

    public static final String FIELD_PERMGROUP = "permGroup";
  }

  public static interface Search extends Flecs {
    public static final String START_NODE = "search";
  }

  public static interface Create {
    public static final String START_NODE = "create";
    public static final String PN_ADVANCE_UUID = "advanceUuid";
  }

  public static interface Edit extends Base {
    public static final String START_NODE = "edit";
  }

  public static interface View extends Base {
    public static final String START_NODE = "view";
  }

  public static interface Log extends Base {
    public static final String START_NODE = "log";
  }
}
