/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementUrlParams.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-24 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.intf.client;

import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * 账单的URL参数定义。
 * 
 * @author huangjunxian
 * 
 */
public class StatementUrlParams {
  public static final String MODULE_CAPTION = "账单";
  public static final String ENTRY_MODULE = "statement.Statement";
  public static final String ENTRY_FILE = "Statement.html";
  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);

  /** 合同类型 */
  public static final String KEY_CONTRACT_CATEGORY = "investment/contract/category";

  public static interface Flecs {
    public static final String PN_FLECSCONFIG = "flecsconfig";
    public static final String PN_PAGE = "page";
    public static final String PN_LOGINSTART = "loginStart";
    public static final String GROUP_ID = "Account.lease.Statement";

    public static final String FIELD_PERMGROUP = "permGroup";
    public static final String FIELD_ACCOUNTUNIT = "accountUnit";
    public static final String FIELD_BILLNUMBER = "billNumber";
    public static final String FIELD_COUNTERPARTTYPE = "counterpartType";
    public static final String FIELD_COUNTERPART = "counterpart";
    public static final String FIELD_CONTRACTNUMBER = "contract.code";
    public static final String FIELD_POSITION = "typePosition";
    public static final String FIELD_BUILDING = "typeBuilding";
    public static final String FIELD_CONTRACTNAME = "contract.name";
    public static final String FIELD_SETTLENO = "settleNo";
    public static final String FIELD_ACCOUNTTIME = "accountTime";
    public static final String FIELD_LASTRECEIPTDATE = "lastReceiptDate";
    public static final String FIELD_PLANPAYDATE = "planPayDate";
    public static final String FIELD_ACCOUNTRANGE = "accountRange";
    public static final String FIELD_SUBJECT = "acc1.subject";
    public static final String FIELD_BIZSTATE = "bizState";
    public static final String FIELD_SOURCEBILLTYPE = "acc1.sourceBill.billType";
    public static final String FIELD_SOURCENUMBER = "acc1.sourceBill.billNumber";
    public static final String FIELD_STATEMENTSETTLE = "statementSettle";
    public static final String FIELD_INVOICEREG = "invoiceReg";
    public static final String FIELD_SETTLESTATE = "settleState";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_COOPMODE = "coopMode";
    public static final String FIELD_CONTRACT_CATEGORY = "contractCategory";

    public static final String FIELD_CREATE_INFO_OPER_ID = "createInfo.operator.id";
    public static final String FIELD_CREATE_INFO_OPER_NAME = "createInfo.operator.fullName";
    public static final String FIELD_CREATE_INFO_TIME = "createInfo.time";
    public static final String FIELD_LASTMODIFY_INFO_OPER_ID = "lastModifyInfo.operator.id";
    public static final String FIELD_LASTMODIFY_INFO_OPER_NAME = "lastModifyInfo.operator.fullName";
    public static final String FIELD_LASTMODIFY_INFO_TIME = "lastModifyInfo.time";

    public static final String FIELD_RECEIPTTOTOAL = "receiptTotal";
    public static final String FIELD_RECEIPTED = "receipted";
    public static final String FIELD_PAYTOTAL = "payTotal";
    public static final String FIELD_PAYED = "payed";
    
    public static final String FIELD_RECEIPTTOTOAL_TOTAL = "receiptTotal.total";
    public static final String FIELD_PAYTOTAL_TOTAL = "payTotal.total";
  }

  public static interface AccountFilter {
    public static final String FIELD_SUBJECT = "subject";
    public static final String FIELD_DIRECTION = "direction";
    public static final String FIELD_TOTAL = "total";
    public static final String FIELD_TAX = "tax";
    public static final String FIELD_DATERANGE = "dateRange";
    public static final String FIELD_SOURCEBILLTYPE = "sourceBillType";
    public static final String FIELD_SOURCEBILLNUMBER = "sourceBillNumber";
  }

  public static interface Base {
    /** URL参数：指定查看账单的Uuid。 */
    public static final String PN_UUID = "_uuid";
    /** URL参数：指定查看账单的业务主键（名称）。 */
    public static final String PN_BILLNUMBER = "billNumber";
  }

  public static interface Search extends Flecs {
    /** URL参数：单号起始于搜索 */
    public static final String PN_KEYWORD = "keyword";
    /** URL参数start取值。 */
    public static final String START_NODE = "search";
  }

  /** 补录单新建界面 */
  public static interface Create {
    /** URL参数start取值。 */
    public static final String START_NODE = "create";
  }

  public static interface View extends Base {
    /** URL参数start取值。 */
    public static final String START_NODE = "view";
  }

  public static interface Edit extends Base {
    /** URL参数start取值。 */
    public static final String START_NODE = "edit";
  }

  /** 补录单编辑界面 */
  public static interface EditPatch extends Base {
    /** URL参数start取值。 */
    public static final String START_NODE = "editPatch";
  }

  public static interface AccView extends Base {
    /** URL参数start取值。 */
    public static final String START_NODE = "accView";

    public static final String KEY_SUBJECT_UUID = "subject_uuid";
    public static final String KEY_SUBJECT_CODE = "subject_code";
    public static final String KEY_SUBJECT_NAME = "subject_name";
  }

  public static interface AccEdit extends Base {
    /** URL参数start取值。 */
    public static final String START_NODE = "accEdit";
  }

  public static interface AccAdd extends Base {
    /** URL参数start取值。 */
    public static final String START_NODE = "accAdd";
  }

  public static interface RemarkEdit extends Base {
    /** URL参数start取值。 */
    public static final String START_NODE = "remark_edit";
  }

  public static interface Log extends Base {
    /** URL参数start取值。 */
    public static final String START_NODE = "log";
  }

  public static interface AccSettleLog extends Flecs {
    /** URL参数start取值。 */
    public static final String START_NODE = "accSettleLog";

    // 账单出账日志
    public static final String FIELD_CONTRACTTITLE = "contract.title";
    public static final String FIELD_FLOOR = "floor";
    public static final String FIELD_COOPMODE = "contract.coopMode";
    public static final String FIELD_SETTLEMENTCAPTION = "settlementCaption";
    public static final String FIELD_SETTLEMENTRANGEDATE = "settlementRangeDate";
    public static final String FIELD_PLANDATE = "planDate";
    public static final String FIELD_BILLCALCULATETYPE = " billCalculateType";

    // flecs
    public static final String FIELD_FLOOR_CODE = "floor.code";
    public static final String FIELD_FLOOR_NAME = "floor.name";
    public static final String FIELD_POSITION = " position";
    public static final String FIELD_POSITION_CODE = " position.code";
    public static final String FIELD_POSITION_NAME = " position.name";
    public static final String FIELD_SETTLEMENTBEGINDATE = "settlementBeginDate";
    public static final String FIELD_SETTLEMENTENDDATE = "settlementEndDate";
    public static final String FIELD_ISACCOUNTSETTLE = "isAccountSettle";

    public static final String GROUP_ID = "Account.lease.AccountSettleLog";
  }

}
