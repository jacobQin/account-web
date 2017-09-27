/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	ReceiptUrlParams.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-30 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.intf.client;

import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * @author subinzhu
 * 
 */
public class ReceiptUrlParams {

  public static final String ENTRY_MODULE = "payment.rec.Receipt";
  public static final String ENTRY_FILE = "Receipt.html";
  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);
  
  public static interface Base {
    public static final String PN_UUID = "_uuid";
    public static final String PN_BILLNUMBER = "billnumber";
  }

  public static interface Flecs {

    /** URL参数名：搜索条件。 */
    public static final String PN_FLECSCONFIG = "flecsconfig";
    /** URL参数名：页码 */
    public static final String PN_PAGE = "page";
    /** 搜索条件的分组ID */
    public static final String GROUP_ID = "Account.Receipt";

    /** 可搜索的字段名 */
    public static final String FIELD_BILLNUMBER = "billNumber";
    public static final String FIELD_BIZSTATE = "bizState";
    public static final String FIELD_BPMSTATE = "bpmState";
    public static final String FIELD_CONTRACTBILLNUMBER = "contract.billNumber";
    public static final String FIELD_COUNTERPARTTYPE = "counterpartType";
    public static final String FIELD_COUNTERPART = "counterpart";
    public static final String FIELD_RECEIPTDATE = "receiptDate";
    public static final String FIELD_SETTLENO = "settleNo";
    public static final String FIELD_COOPMODE = "coopMode";
    public static final String FIELD_ACCOUNTUNIT = "accountUnit";
    public static final String FIELD_PERMGROUP = "permGroup";
    public static final String FIELD_CONTRACT_NAME = "contract.name";

    public static final String FIELD_CREATE_INFO_OPER_ID = "createInfo.operator.id";
    public static final String FIELD_CREATE_INFO_OPER_NAME = "createInfo.operator.fullName";
    public static final String FIELD_CREATE_INFO_TIME = "createInfo.time";

    public static final String FIELD_LASTMODIFY_INFO_OPER_ID = "lastModifyInfo.operator.id";
    public static final String FIELD_LASTMODIFY_INFO_OPER_NAME = "lastModifyInfo.operator.fullName";
    public static final String FIELD_LASTMODIFY_INFO_TIME = "lastModifyInfo.time";
  }

  public static interface Search extends Flecs {
    /** URL参数名：搜索关键字 */
    public static final String PN_KEYWORD = "_keyword";
    public static final String START_NODE = "search";

    /** 可排序字段名 */
    public static final String FIELD_UNPAYEDTOTAL = "unpayedTotal.total";
    public static final String FIELD_TOTAL = "total.total";
    public static final String FIELD_DEFRAYALTOTAL = "defrayalTotal";
    public static final String FIELD_REMARK = "remark";
  }

  public static interface Create {
    public static final String START_NODE = "create";
    public static final String PN_COUNTERPARTCODE = "counterpartCode";
    public static final String PN_CREATEBYSTATEMENT = "createByStatement";
    public static final String PN_CREATEBYINVOICEREG = "createByInvoiceReg";
    public static final String PN_CREATEBYPAYMENTNOTICE = "createByPaymentNotice";
    public static final String PN_CREATEBYSOURCEBILL = "createBySourceBill";
    //根据账款uuid创建
    public static final String PN_CREATEBYACCOUNT = "createByAccount";
    //根据账款accId创建
    public static final String PN_CREATEBYACCOUNTIDS = "createByAccountIds";
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

  public static interface IvcReg extends Base {
    public static final String START_NODE = "ivcReg";
  }
  
  public static interface IvcRegView extends Base {
    public static final String START_NODE = "ivcRegView";
  }

}
