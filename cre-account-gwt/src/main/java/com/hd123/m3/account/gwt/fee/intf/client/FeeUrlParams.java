/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： FeeUrlParams.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-19 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.intf.client;

import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * @author subinzhu
 * 
 */
public class FeeUrlParams {
  public static final String MODULE_CAPTION = "费用单";

  public static final String ENTRY_MODULE = "fee.Fee";
  public static final String ENTRY_FILE = "Fee.html";
  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);

  public static interface Flecs {
    public static final String PN_FLECSCONFIG = "flecsconfig";
    public static final String PN_PAGE = "page";
    public static final String GROUP_ID = "Account.Fee";

    public static final String FIELD_BILLNUMBER = "billNumber";
    public static final String FIELD_BIZSTATE = "bizState";
    public static final String FIELD_BPMSTATE = "bpmState";
    public static final String FIELD_COUNTERPARTTYPE = "counterpartType";
    public static final String FIELD_COUNTERPART = "counterpart.code";
    public static final String FIELD_CONTRACT_CODE = "contract.code";
    public static final String FIELD_CONTRACT_NAME = "contract.name";
    public static final String FIELD_DIRECTION = "direction";
    public static final String FIELD_ACCOUNTDATE = "accountDate";
    public static final String FIELD_ACCOUNTUNIT = "accountUnit.uuid";
    public static final String FIELD_SUBJECT = "subject.uuid";
    public static final String FIELD_SETTLENO = "settleNo";
    public static final String FIELD_COOPMODE = "coopMode";
    public static final String FIELD_PERMGROUP = "permGroup";
    public static final String FIELD_TOTAL = "total.total";
    public static final String FIELD_REMARK = "remark";

    public static final String FIELD_CREATE_INFO_OPER_ID = "createInfo.operator.id";
    public static final String FIELD_CREATE_INFO_OPER_NAME = "createInfo.operator.fullName";
    public static final String FIELD_CREATE_INFO_TIME = "createInfo.time";
    public static final String FIELD_LASTMODIFY_INFO_OPER_ID = "lastModifyInfo.operator.id";
    public static final String FIELD_LASTMODIFY_INFO_OPER_NAME = "lastModifyInfo.operator.fullName";
    public static final String FIELD_LASTMODIFY_INFO_TIME = "lastModifyInfo.time";
  }

  public static interface Search extends Flecs {
    /** URL参数：单号起始于搜索 */
    public static final String PN_KEYWORD = "keyword";
    /** URL参数start取值。 */
    public static final String START_NODE = "search";
  }

  public static interface Base {
    public static final String PN_UUID = "_uuid";
    public static final String PN_NUMBER = "_billNumber";
  }

  public static interface Create extends Base {
    public static final String START_NODE = "create";
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

}
