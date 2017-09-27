/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceStockUrlParams.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月10日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.stock.intf.client;

import com.hd123.rumba.webframe.gwt.base.client.ModuleUrlParams;

/**
 * GWT模块com.hd123.m3.account.gwt.ivc.instock.InvoiceStock（发票库存单）的URL参数。
 * 
 * @author lixiaohong
 * @since 1.7
 *
 */
public class InvoiceStockUrlParams implements ModuleUrlParams{
  public static interface Base {
    public static final String PN_UUID = "_uuid";
  }
  
  public static interface Flecs extends Base {
    /** 搜索条件的分组ID */
    public static final String GROUP_ID = "Ivc.InvoiceStock";

    public static final String FIELD_INVOICE_TYPE = "invoiceType";
    public static final String FIELD_INVOICE_CODE = "invoiceCode";
    public static final String FIELD_INVOICE_NUNBER = "invoiceNumber";
    public static final String FIELD_STATE = "state";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_ACCOUNT_UNIT = "accountUnit";
    public static final String FIELD_HOLDER = "holder";
    public static final String FIELD_ABORTDATE = "abortDate";
    public static final String FIELD_AMOUNT = "amount";
    
    public static final String FIELD_CREATE_INFO_OPER_ID = "createInfo.operator.id";
    public static final String FIELD_CREATE_INFO_OPER_NAME = "createInfo.operator.fullName";
    public static final String FIELD_CREATE_INFO_TIME = "createInfo.time";

    public static final String FIELD_LASTMODIFY_INFO_OPER_ID = "lastModifyInfo.operator.id";
    public static final String FIELD_LASTMODIFY_INFO_OPER_NAME = "lastModifyInfo.operator.fullName";
    public static final String FIELD_LASTMODIFY_INFO_TIME = "lastModifyInfo.time";
  }
  /** 搜索页面相关参数 */
  public static interface Search extends Flecs{
    public static final String START_NODE = "search";
  }
}
