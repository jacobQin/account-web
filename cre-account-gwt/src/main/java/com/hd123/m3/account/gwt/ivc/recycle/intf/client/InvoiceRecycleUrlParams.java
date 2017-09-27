/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRecycleUrlParams.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月8日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.intf.client;

import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * GWT模块com.hd123.m3.account.gwt.ivc.recycle.InvoiceRecycle（发票回收单）的URL参数。
 * 
 * @author lixiaohong
 * @since 1.9
 *
 */
public class InvoiceRecycleUrlParams implements BpmModuleUrlParams{

  public static final String ENTRY_MODULE = "ivc.recycle.InvoiceRecycle";
  public static final String ENTRY_FILE = "InvoiceRecycle.html";

  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);

  public static interface Flecs {
    /** 搜索条件的分组ID */
    public static final String GROUP_ID = "Ivc.InvoiceRecycle";
    
    public static final String PN_BILLNUMBER = "billNumber";
    public static final String FIELD_BIZSTATE = "bizState";
    public static final String FIELD_INVOICE_TYPE = "invoiceType";
    public static final String FIELD_ACCOUNT_UNIT = "accountUnit";
    public static final String FIELD_RETURNOR = "returnor";
    public static final String FIELD_RECEIVER = "receiver";
    public static final String FIELD_INVOICE_CODE = "invoiceCode";
    public static final String FIELD_INVOICE_NUNBER = "invoiceNumber";
    public static final String FIELD_RECYCLEDATE = "recycleDate";

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
