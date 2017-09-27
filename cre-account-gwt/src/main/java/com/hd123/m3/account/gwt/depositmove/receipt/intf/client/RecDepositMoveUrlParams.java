/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	RecDepositMoveUrlParams.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.receipt.intf.client;

import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * 预存款转移单URL参数定义
 * 
 * @author zhuhairui
 * 
 */
public class RecDepositMoveUrlParams {
  public static final String MODULE_CAPTION = "预存款转移单";
  public static final String ENTRY_MODULE = "depositmove.receipt.RecDepositMove";
  public static final String ENTRY_FILE = "RecDepositMove.html";
  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);

  public static interface Base {
    public static final String PN_ENTITY_UUID = "_uuid";
    public static final String PN_ENTITY_BILLNUMBER = "_billnumber";
  }

  public static interface Flecs {
    /** URL参数名：搜索条件。 */
    public static final String PN_FLECSCONFIG = "flecsconfig";
    /** URL参数名：页码 */
    public static final String PN_PAGE = "page";
    /** URL参数名：搜索关键字 */
    public static final String PN_KEYWORD = "keyword";
    /** 搜索条件的分组ID */
    public static final String GROUP_ID = "depositmove.RecDepositMove";

    /** 单号(String) */
    public static final String FIELD_BILLNUMBER = "billNumber";
    /** 转出对方结算中心(String) */
    public static final String FIELD_OUTCOUNTERPART = "outCounterpart";
    /** 转入对方结算中心(String) */
    public static final String FIELD_INCOUNTERPART = "inCounterpart";
    /** 转出对方单位类型(String) */
    public static final String FIELD_OUTCOUNTERPARTTYPE = "outCounterpartType";
    /** 转入对方单位类型(String) */
    public static final String FIELD_INCOUNTERPARTTYPE = "inCounterpartType";
    /** 状态(String) */
    public static final String FIELD_BIZSTATE = "bizState";
    /** 转出合同编号(String) */
    public static final String FIELD_OUTCONTRACTNUMBER = "outContract.code";
    /** 转出店招(String) */
    public static final String FIELD_OUTCONTRACTNAME = "outContract.name";
    /** 转入合同编号(String) */
    public static final String FIELD_INCONTRACTNUMBER = "inContract.code";
    /** 转入店招(String) */
    public static final String FIELD_INCONTRACTNAME = "inContract.name";
    /** 转出科目(String) */
    public static final String FIELD_OUTSUBJECT = "outSubject";
    /** 记账日期(String) */
    public static final String FIELD_ACCOUNTDATE = "accountDate";
    /** 转入科目(String) */
    public static final String FIELD_INSUBJECT = "inSubject";
    /** 项目 */
    public static final String FIELD_BUSINESSUNIT = "businessUnit";
    /** 结转期(String) */
    public static final String FIELD_SETTLENO = "settleNo";
    /** 结算组 */
    public static final String FIELD_SETTLEGROUP = "settleGroup";
    /** 创建人代码(String) */
    public static final String FIELD_CREATOR_CODE = "createInfo.operator.id";
    /** 创建人名称(String) */
    public static final String FIELD_CREATOR_NAME = "createInfo.operator.fullName";
    /** 创建时间(Date) */
    public static final String FIELD_CREATED = "createInfo.time";
    /** 修改人代码(String) */
    public static final String FIELD_LAST_MODIFIER_CODE = "lastModifyInfo.operator.id";
    /** 修改人名称(String) */
    public static final String FIELD_LAST_MODIFIER_NAME = "lastModifyInfo.operator.fullName";
    /** 修改时间(Date) */
    public static final String FIELD_LAST_MODIFY_TIME = "lastModifyInfo.time";

    /** 转移金额(BigDecimal) */
    public static final String FIELD_AMOUNT = "amount";
    /** 说明(String) */
    public static final String FIELD_REMARK = "remark";
    /** 授权组 */
    public static final String FIELD_PERMGROUP = "permGroup";
  }

  public static interface Search extends Flecs {
    public static final String START_NODE = "search";
  }

  public static interface Create {
    public static final String START_NODE = "create";
    public static final String PN_ADVANCE_UUID = "advanceUuid";
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
