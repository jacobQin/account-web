/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 *
 * 项目名：M3
 * 文件名：SubjectUrlParams.java
 * 模块说明：
 * 修改历史：
 * 2013-99 - cRazy - 创建。
 */

package com.hd123.m3.account.gwt.subject.intf.client;

import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * @author cRazy
 * 
 */
public class SubjectUrlParams {
  public static final String MODULE_CAPTION = "科目";
  public static final String ENTRY_MODULE = "subject.Subject";
  public static final String ENTRY_FILE = "Subject.html";
  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);

  public static interface Flecs {
    public static final String GROUP_ID = "account.subject.Subject";
    public static final String PN_FLECSCONFIG = "flecsconfig";
    public static final String PN_PAGE = "page";

    public static final String FIELD_CODE = "code";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_STATE = "state";
    public static final String FIELD_USAGE = "usage";
    public static final String FIELD_DIRECTION = "direction";
    public static final String FIELD_CUSTOMTYPE ="customType";
    
    public static final String FIELD_CREATE_INFO_OPER_ID = "createInfo.operator.id";
    public static final String FIELD_CREATE_INFO_OPER_NAME = "createInfo.operator.fullName";
    public static final String FIELD_CREATE_INFO_TIME = "createInfo.time";

    public static final String FIELD_LASTMODIFY_INFO_OPER_ID = "lastModifyInfo.operator.id";
    public static final String FIELD_LASTMODIFY_INFO_OPER_NAME = "lastModifyInfo.operator.fullName";
    public static final String FIELD_LASTMODIFY_INFO_TIME = "lastModifyInfo.time";
  }

  public static interface Base {
    public static final String PN_ENTITY_UUID = "_uuid";
    public static final String PN_ENTITY_CODE = "_code";
  }

  public static interface Search extends Flecs {
    /** URL参数名：搜索关键字 */
    public static final String PN_KEYWORD = "_keyword";

    public static final String START_NODE = "search";
  }

  public static interface Create {
    public static final String START_NODE = "create";
  }

  public static interface View extends Base {
    public static final String START_NODE = "view";
  }

  public static interface Edit extends Base {
    public static final String START_NODE = "edit";
  }

  public static interface Log extends Base {
    public static final String START_NODE = "log";
  }
}