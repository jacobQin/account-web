/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebateBillUrlParams.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月1日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.rebate.intf.client;

import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * GWT模块com.hd123.m3.account.gwt.rebate.RebateBill（返款但）的URL参数。
 * 
 * @author lixiaohong
 * @since 1.12
 * 
 */
public class RebateBillUrlParams implements BpmModuleUrlParams {
  public static final String ENTRY_MODULE = "rebate.RebateBill";
  public static final String ENTRY_FILE = "RebateBill.html";
  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);

  public static interface Flecs {
    /** 搜索条件的分组ID */
    public static final String GROUP_ID = "rebate.RebateBill";

    public static final String PN_BILLNUMBER = "billNumber";
    public static final String FIELD_BIZSTATE = "bizState";
    public static final String FIELD_CONTRACT = "contract";
    public static final String FIELD_CONTRACT_NAME = "contract.name";
    public static final String FIELD_STORE = "store";
    public static final String FIELD_TENANT = "tenant";
    public static final String FIELD_POSITION = "position";
    /** 铺位编号 */
    public static final String FIELD_POSITIONS = "positions";
    public static final String FIELD_ACCOUNT_DATE = "accountDate";
    /** 起始日期 */
    public static final String FIELD_BEGINDATE = "beginDate";
    /** 截止日期 */
    public static final String FIELD_ENDDATE = "endDate";
    /** 销售返款金额 */
    public static final String FIELD_BACKTOTAL = "backTotal";
    /** 手续费金额 */
    public static final String FIELD_POUNDAGETOTAL = "poundageTotal";
    /** 应返金额 */
    public static final String FIELD_SHOULDBACKTOTAL = "shouldBackTotal";

    public static final String FIELD_PERMGROUP = "permGroupId";
    public static final String FIELD_CREATE_INFO_OPER_ID = "createInfo.operator.id";
    public static final String FIELD_CREATE_INFO_OPER_NAME = "createInfo.operator.fullName";
    public static final String FIELD_CREATE_INFO_TIME = "createInfo.time";
    public static final String FIELD_LASTMODIFY_INFO_OPER_ID = "lastModifyInfo.operator.id";
    public static final String FIELD_LASTMODIFY_INFO_OPER_NAME = "lastModifyInfo.operator.fullName";
    public static final String FIELD_LASTMODIFY_INFO_TIME = "lastModifyInfo.time";
  }
  
  public static interface Base {
    public static final String PN_UUID = "_uuid";
  }
}
