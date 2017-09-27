/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountDefrayal.java
 * 模块说明：	
 * 修改历史：
 * 2013-12-9 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.report.accountdefrayal.intf.client;

import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * @author zhuhairui
 * 
 */
public class AccountDefrayalUrlParams {
  public static final String MODULE_CAPTION = "科目收付情况";
  public static final String ENTRY_MODULE = "report.accountdefrayal.AccountDefrayal";
  public static final String ENTRY_FILE = "AccountDefrayal.html";
  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);

  public static interface Search extends Flecs {
    public static final String START_NODE = "search";

    /** 来源单据单号 */
    public static final String KEY_SOURCEBILLNUMBER = "sourceBillNumber";
    /** 来源单据类型 */
    public static final String KEY_SOURCEBILLTYPE = "sourceBillType";
    /** 账单号 */
    public static final String KEY_STATEMENTNUMBER = "statementNumber";
  }

  public static interface Flecs {
    public static final String PN_FLECSCONFIG = "flecsconfig";
    public static final String PN_PAGE = "page";
    public static final String GROUP_ID = "Account.AccountDefrayal";

    /** URL参数名：单号起始于 */
    public static final String PN_SOURCEBILLNUMBER_STARTWITH = "billNumber_startwith";
    /** 单号 */
    public static final String FIELD_SOURCEBILLNUMBER = "acc1.sourceBill.billNumber";
    /** 单据类型 */
    public static final String FIELD_SOURCEBILLTYPE = "acc1.sourceBill.billType";
    /** 科目 */
    public static final String FIELD_SUBJECT = "acc1.subject";
    /**对方单位类型*/
    public static final String FIELD_COUNTERPARTTYPE = "counterpartType";
    /** 收付方向 */
    public static final String FIELD_DIRECTION = "acc1.direction";
    /** 对账组 */
    public static final String FIELD_ACCOUNTGROUP = "accountGroup";
    /** 应收付款金额 */
    public static final String FIELD_NEEDSETTLE = "needSettle.total";
    /** 调整金额 */
    public static final String FIELD_SETTLEADJ = "settleAdj";
    /** 实收付金额 */
    public static final String FIELD_SETTLETOTAL = "settleTotal";
    /** 应收付发票登记 */
    public static final String FIELD_NEEDINVOICE = "needInvoice";
    /** 发票登记调整 */
    public static final String FIELD_INVOICEADJ = "invoiceAdj";
    /** 实收付发票登记 */
    public static final String FIELD_INVOICETOTAL = "invoiceTotal";
    /** 账单号 */
    public static final String FIELD_STATEMENTNUM = "statement.billNumber";
    /** 出账时间 */
    public static final String FIELD_ACCOUNTTIME = "accountTime";
    /** 账单结转期 */
    public static final String FIELD_STATEMENTSETTLENO = "statementSettleNo";
    /** 已结清 */
    public static final String FIELD_SETTLED = "settled";
    /** 已开票 */
    public static final String FIELD_INVOICED = "invoiced";
    /** 起止日期 */
    public static final String FIELD_DATERANGE = "dateRange";
    /** 最后缴款日期 */
    public static final String FIELD_LASTRECEIPTDATE = "lastReceiptDate";
    /** 合同编号 */
    public static final String FIELD_CONTRACT_BILLNUMBER = "acc1.contract.code";
    /** 店招 */
    public static final String FIELD_CONTRACT_NAME = "acc1.contract.name";
    /** 商户 */
    public static final String FIELD_COUNTERPART = "counterpart";
    /** 项目 */
    public static final String FIELD_ACCOUNTUNIT = "accountUnit";
  }

}
