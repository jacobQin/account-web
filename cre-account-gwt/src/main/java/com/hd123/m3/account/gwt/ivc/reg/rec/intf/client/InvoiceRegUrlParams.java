/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRegUrlParams.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.intf.client;

import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * GWT模块com.hd123.m3.account.gwt.ivc.reg.rec.InvoiceReg（发票领用单）的URL参数。
 * 
 * @author chenrizhang
 * @since 1.0
 * 
 */
public class InvoiceRegUrlParams implements BpmModuleUrlParams {
  public static final String ENTRY_MODULE = "ivc.reg.rec.InvoiceRegReceipt";
  public static final String ENTRY_FILE = "InvoiceRegReceipt.html";

  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);

  /** 通过收款单创建发票登记 */
  public static final String KEY_CREATE_BY_PAYMENT = "_byPayment";
  /** 通过通知单创建发票登记 */
  public static final String KEY_CREATE_BY_NOTICE = "_byNotice";
  /** 通过账单创建发票登记 */
  public static final String KEY_CREATE_BY_STATEMENT = "_byStatement";

  public static interface Flecs {
    /** 搜索条件的分组ID */
    public static final String GROUP_ID = "invoice.reg.rec";

    public static final String FIELD_BIZSTATE = "bizState";
    public static final String FIELD_INVOICE_TYPE = "invoiceType";
    public static final String FIELD_ACCOUNT_UNIT = "accountUnit";
    public static final String FIELD_COUNTERPART = "counterpart";
    public static final String FIELD_SOURCEBILL_BILLNUMBER = "sourceBill.billNumber";
    public static final String FIELD_INVOICE_CODE = "invoiceCode";
    public static final String FIELD_INVOICE_NUNBER = "invoiceNumber";
    public static final String FIELD_REGDATE = "regDate";
    public static final String FIELD_REMARK = "remark";

    public static final String FIELD_PERMGROUP = "permGroupId";

    public static final String FIELD_CREATE_INFO_OPER_ID = "createInfo.operator.id";
    public static final String FIELD_CREATE_INFO_OPER_NAME = "createInfo.operator.fullName";
    public static final String FIELD_CREATE_INFO_TIME = "createInfo.time";

    public static final String FIELD_LASTMODIFY_INFO_OPER_ID = "lastModifyInfo.operator.id";
    public static final String FIELD_LASTMODIFY_INFO_OPER_NAME = "lastModifyInfo.operator.fullName";
    public static final String FIELD_LASTMODIFY_INFO_TIME = "lastModifyInfo.time";
  }

}
