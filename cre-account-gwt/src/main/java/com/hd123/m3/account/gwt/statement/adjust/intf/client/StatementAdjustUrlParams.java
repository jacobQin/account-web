/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StatementAdjustUrlParams.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.intf.client;

import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * @author zhuhairui
 * 
 */
public class StatementAdjustUrlParams {
  public static final String MIDDLECODE = "账单调整单";
  public static final String ENTRY_MODULE = "statement.adjust.StatementAdjust";
  public static final String ENTRY_FILE = "StatementAdjust.html";
  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);

  // UCNBox中用
  public static final String SUBJECT_CODE = "code";
  public static final String SUBJECT_NAME = "name";

  public static interface Base {
    public static final String PN_ENTITY_UUID = "_uuid";
    public static final String PN_ENTITY_BILLNUMBER = "_billNumber";
    public static final String SCENCE = "scence";
  }

  public static interface Search extends Flecs {
    /** URL参数：单号起始于搜索 */
    public static final String PN_KEYWORD = "keyword";
    public static final String START_NODE = "search";
  }

  public static interface Create {
    public static final String START_NODE = "create";
    public static final String PN_STATEMENT_UUID = "statementUuid";
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

  public static interface Flecs {
    public static final String GROUP_ID = "statement.adjust.StatementAdjust";
    public static final String PN_FLECSCONFIG = "flecsconfig";
    public static final String PN_PAGE = "page";

    /** URL参数名：登录名起始于 */
    public static final String PN_BILLNUMBER = "billNumber";

    /** 单号 */
    public static final String FIELD_BILLNUMBER = "billNumber";
    /** 状态 */
    public static final String FIELD_BIZSTATE = "bizState";
    /** 结转期 */
    public static final String FIELD_SETTLENO = "settleNo";
    /** 项目 */
    public static final String FIELD_ACCOUNTUNIT = "acountUnit";
    /** 对方单位类型 */
    public static final String FIELD_COUNTERPARTTYPE = "counterpartType";
    /** 商户 */
    public static final String FIELD_COUNTERPART = "counterpart";
    /** 合同编号 */
    public static final String FIELD_CONTRACTBILLNUMBER = "contract.code";
    /** 店招 */
    public static final String FIELD_CONTRACTNAME = "contract.name";
    /** 账单号 */
    public static final String FIELD_STATEMENTBILLNUMBER = "statement.billNumber";
    /** 科目 */
    public static final String FIELD_SUBJECT = "subject";
    /** 授权组 */
    public static final String FIELD_PERMGROUP = "permGroup";
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

}
