/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： FeeUrlParams.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-19 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.intf.client;

import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * @author subinzhu
 * 
 */
public class PaymentNoticeUrlParams {
  public static final String MODULE_CAPTION = "收付款通知单";
  public static final String MODULE_CAPTION_BATCH = "批量生成收付款通知单";

  public static final String ENTRY_MODULE = "paymentnotice.PaymentNotice";
  public static final String ENTRY_FILE = "PaymentNotice.html";
  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);

  public static final String ENTRY_ID_SEARCH = "account.PaymentNotice.search";
  public static final String ENTRY_ID_CREATE = "account.PaymentNotice.create";
  public static final String ENTRY_ID_BATCH = "account.PaymentNotice.batch";

  public static final String ORDER_BY_COUNTERPART_CODE = "counterpartCode";
  public static final String ORDER_BY_ACCOUNTUNIT_CODE = "accountUnitCode";
  public static final String ORDER_BY_CONTRACTBILLNUMBER = "contractBillNumber";

  public static interface Flecs {
    public static final String PN_FLECSCONFIG = "flecsconfig";
    public static final String PN_PAGE = "page";
    public static final String GROUP_ID = "Account.PaymentNotice";

    /** URL参数名：单号起始于 */
    public static final String PN_BILLNUMBER_STARTWITH = "billNumber_startwith";
    /** 单号 */
    public static final String FIELD_BILLNUMBER = "billNumber";
    /** 状态 */
    public static final String FIELD_BIZSTATE = "bizState";
    public static final String FIELD_BPMSTATE = "bpmState";
    /** 授权组 */
    public static final String FIELD_PERMGROUP = "permGroup";
    /**对方单位类型 */
    public static final String FIELD_COUNTERPARTTYPE = "counterpartType";
    /** 商户 */
    public static final String FIELD_COUNTERPART = "counterpart";
    /** 合同编号 */
    public static final String FIELD_CONTRACTBILLNUMBER = "contract.code";
    /** 账单号 */
    public static final String FIELD_STATEMENTBILLNUMBER = "statement.billNumber";
    /** 结转期 */
    public static final String FIELD_SETTLENO = "settleNo";
    /** 应收金额 */
    public static final String FIELD_RECEIPTTOTAL = "receiptTotal.total";
    /** 应收开票金额 */
    public static final String FIELD_RECEIPTIVCTOTAL = "receiptIvcTotal.total";
    /** 应付金额 */
    public static final String FIELD_PAYMENTTOTAL = "paymentTotal.total";
    /** 应付开票金额 */
    public static final String FIELD_PAYMENTIVCTOTAL = "paymentIvcTotal.total";
    /** 项目 */
    public static final String FIELD_BUSINESSUNIT = "businessUnit";
    /** 创建人代码(String) */
    public static final String FIELD_CRETOR_CODE = "createInfo.operator.id";
    /** 创建人名称(String) */
    public static final String FIELD_CRETOR_NAME = "createInfo.operator.fullName";
    /** 创建时间(String) */
    public static final String FIELD_CREATED = "createInfo.time";
    /** 最后修改人代码(String) */
    public static final String FIELD_LASTMODIFIER_CODE = "lastModifyInfo.operator.id";
    /** 最后修改人名称(String) */
    public static final String FIELD_LASTMODIFIER_NAME = "lastModifyInfo.operator.fullName";
    /** 最后修改时间(String) */
    public static final String FIELD_LASTMODIFIED = "lastModifyInfo.time";
  }

  public static interface Search extends Flecs {
    /** URL参数：单号起始于搜索 */
    public static final String PN_KEYWORD = "keyword";
    /** URL参数start取值。 */
    public static final String START_NODE = "search";
    public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_FILE, START_NODE);
  }

  public static interface Base {
    public static final String PN_ENTITY_UUID = "_uuid";
    public static final String PN_ENTITY_BILLNUMBER = "_billNumber";
  }

  public static interface Create extends Base {
    public static final String START_NODE = "create";
    public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_FILE, START_NODE);
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

  /** 批量生成页面 */
  public static interface Batch {
    public static final String START_NODE = "batch";
    public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_FILE, START_NODE);
  }

}
