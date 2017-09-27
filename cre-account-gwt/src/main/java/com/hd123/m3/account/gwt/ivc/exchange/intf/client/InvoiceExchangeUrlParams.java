/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeUrlParams.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月17日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.intf.client;

import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;
import com.hd123.rumba.webframe.gwt.base.client.ModuleUrlParams;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * GWT模块com.hd123.m3.account.gwt.ivc.exchange.InvoiceExchange（发票交换单）的URL参数。
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class InvoiceExchangeUrlParams implements ModuleUrlParams {
  public static final String ENTRY_MODULE = "ivc.exchange.InvoiceExchange";
  public static final String ENTRY_FILE = "InvoiceExchange.html";

  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);

  public static final String PN_UUID = BpmModuleUrlParams.KEY_ENTITY_UUID;
  public static final String PN_BILLNUMBER = "billNumber";
  
  public static interface Flecs {
    /** 搜索条件的分组ID */
    public static final String GROUP_ID = "Ivc.InvoiceExchange";
    
    public static final String FIELD_BILLNUMBER = "billNumber";
    public static final String FIELD_BIZSTATE = "bizState";
    public static final String FIELD_EXCHANGE_TYPE = "exchangeType";
    public static final String FIELD_ACCOUNT_UNIT = "accountUnit";
    public static final String FIELD_EXCHANGER = "exchanger";
    public static final String FIELD_EXCHANGE_DATE = "exchangeDate";
    public static final String FIELD_INVOICE_CODE = "invoiceCode";
    public static final String FIELD_INVOICE_NUNBER = "invoiceNumber";
    public static final String FIELD_REMARK = "remark";

    public static final String FIELD_PERMGROUP = "permGroupId";
    
    public static final String FIELD_CREATE_INFO_OPER_ID = "createInfo.operator.id";
    public static final String FIELD_CREATE_INFO_OPER_NAME = "createInfo.operator.fullName";
    public static final String FIELD_CREATE_INFO_TIME = "createInfo.time";

    public static final String FIELD_LASTMODIFY_INFO_OPER_ID = "lastModifyInfo.operator.id";
    public static final String FIELD_LASTMODIFY_INFO_OPER_NAME = "lastModifyInfo.operator.fullName";
    public static final String FIELD_LASTMODIFY_INFO_TIME = "lastModifyInfo.time";
  }

  public static interface Search extends Flecs {
    /** URL参数名：搜索关键字 */
    public static final String PN_KEYWORD = "keyword";
    public static final String START_NODE = "search";
  }

  public static interface Create {
    public static final String START_NODE = "create";
  }

  public static interface Edit {
    public static final String START_NODE = "edit";
  }

  public static interface View {
    public static final String START_NODE = "view";
  }

  public static interface Log {
    public static final String START_NODE = "log";
  }
}
