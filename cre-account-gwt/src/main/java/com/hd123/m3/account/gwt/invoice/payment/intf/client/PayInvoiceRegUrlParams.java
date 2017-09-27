/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PayInvoiceRegUrlParams.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-24 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.payment.intf.client;

import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * 付款发票登记单URL参数定义
 * 
 * @author chenpeisi
 * 
 */
public class PayInvoiceRegUrlParams {
  public static final String MODULE_CAPTION = "付款发票登记单";
  public static final String ENTRY_MODULE = "invoice.payment.PayInvoiceReg";
  public static final String ENTRY_FILE = "PayInvoiceReg.html";
  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);

  public static interface Flecs {
    /** URL参数名：搜索条件。 */
    public static final String PN_FLECSCONFIG = "flecsconfig";
    /** URL参数名：页码 */
    public static final String PN_PAGE = "page";
    /** URL参数名：登录名起始于 */
    public static final String PN_KEYWORD = "keyword";
    /** 搜索条件的分组ID */
    public static final String GROUP_ID = "invoice.PayInvoiceRegSearch";
    /** 授权组 */
    public static final String FIELD_PERMGROUP = "permGroup";
    /** 单号 */
    public static final String FIELD_BILLNUMBER = "billNumber";
    /** 业务状态 */
    public static final String FIELD_BIZSTATE = "bizState";
    /** 流程状态 */
    public static final String FIELD_BPMSTATE = "bpmState";
    /** 结算单位 */
    public static final String FIELD_ACCOUNTUNIT = "accountUnit";
    /** 对方单位 类型*/
    public static final String FIELD_COUNTERPARTTYPE = "counterpartType";
    /** 对方单位 */
    public static final String FIELD_COUNTERPART = "counterpart";
    /** 发票号码 */
    public static final String FIELD_INVOICENUMBER = "invoiceNumber";
    /** 来源单据号 */
    public static final String FIELD_SOURCEBILLNUMBER = "acc1.sourceBill.billNumber";
    /** 结转期 */
    public static final String FIELD_SETTLENO = "settleNo";
    /** 登记日期 */
    public static final String FIELD_REGDATE = "regDate";
    /** 结算组 */
    public static final String FIELD_SETTLEGROUP = "settleGroup";
    /** 创建人代码 */
    public static final String FIELD_CREATE_INFO_OPER_ID = "createInfo.operator.id";
    /** 创建人名称 */
    public static final String FIELD_CREATE_INFO_OPER_NAME = "createInfo.operator.fullName";
    /** 创建时间 */
    public static final String FIELD_CREATE_INFO_TIME = "createInfo.time";
    /** 最后修改人代码 */
    public static final String FIELD_LASTMODIFY_INFO_OPER_ID = "lastModifyInfo.operator.id";
    /** 最后修改人名称 */
    public static final String FIELD_LASTMODIFY_INFO_OPER_NAME = "lastModifyInfo.operator.fullName";
    /** 最后修改时间 */
    public static final String FIELD_LASTMODIFY_INFO_TIME = "lastModifyInfo.time";

    public static final String FIELD_INVOICETOTAL_TOTAL = "invoiceTotal.total";
    public static final String FIELD_INVOICETOTAL_TAX = "invoiceTotal.tax";

    public static final String FIELD_ACCOUNTTOTAL_TOTAL = "accountTotal.total";
    public static final String FIELD_ACCOUNTTOTAL_TAX = "accountTotal.tax";

    public static final String FIELD_TOTALDIFF = "totalDiff";
    public static final String FIELD_TAXDIFF = "taxDiff";
  }

  public static interface Base {
    public static final String PN_UUID = "_uuid";
    public static final String PN_BILLNUMBER = "billNumber";
  }

  public static interface Search extends Flecs {
    /** URL参数start取值。 */
    public static final String START_NODE = "search";
  }

  public static interface Create {
    /** URL参数start取值。 */
    public static final String START_NODE = "create";

    public static final String PN_STATEMENT_UUID = "statement_uuid";
    public static final String PN_PAYMENT_UUID = "payment_uuid";
  }

  public static interface Edit extends Base {
    /** URL参数start取值。 */
    public static final String START_NODE = "edit";
  }

  public static interface View extends Base {
    /** URL参数start取值。 */
    public static final String START_NODE = "view";
  }

  public static interface Log extends Base {
    /** URL参数start取值。 */
    public static final String START_NODE = "log";
  }
}
