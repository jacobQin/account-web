/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	FreezeUrlParams.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-25 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.freeze.intf.client;

import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * @author zhuhairui
 * 
 */
public class FreezeUrlParams {
  public static final String MODULE_CAPTION = "账款冻结单";
  public static final String ENTRY_MODULE = "freeze.Freeze";
  public static final String ENTRY_FILE = "Freeze.html";
  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_FILE, ENTRY_FILE);

  public static interface Search extends Flecs {
    /** URL参数：单号起始于搜索 */
    public static final String PN_KEYWORD = "keyword";
    public static final String START_NODE = "search";
  }

  public static interface Flecs {
    public static final String PN_FLECSCONFIG = "flecsconfig";
    public static final String PN_PAGE = "page";
    public static final String GROUP_ID = "Account.Freeze";

    /** URL参数名：单号起始于 */
    public static final String PN_BILLNUMBER_STARTWITH = "billNumber_startwith";
    /** 单号 */
    public static final String FIELD_BILLNUMBER = "billNumber";
    /** 状态 */
    public static final String FIELD_STATE = "state";
    /** 对方单位类型 */
    public static final String FIELD_COUNTERPARTTYPE = "counterpartType";
    /** 商户 */
    public static final String FIELD_COUNTERPART = "counterpart";
    /** 合同编号 */
    public static final String FIELD_CONTRACT_BILLNUMBER = "contract.billNumber";
    /** 冻结付款金额 */
    public static final String FIELD_CONTRACT_PAYMENTTOTAL = "paymentTotal";
    /** 冻结收款金额 */
    public static final String FIELD_CONTRACT_RECEIPTTOTAL = "receiptTotal";
    /** 冻结日期 */
    public static final String FIELD_FREEZE_DATE = "freezeInfo.time";
    /** 解冻日期 */
    public static final String FIELD_UNFREEZE_DATE = "unfreezeInfo.time";
    /** 项目 */
    public static final String FIELD_BUSINESSUNIT = "businessUnit";
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
    /** 授权组 */
    public static final String FIELD_PERMGROUP = "permGroup";
  }

  public static interface Base {
    public static final String PN_ENTITY_UUID = "_uuid";
    public static final String PN_ENTITY_BILLNUMBER = "_billNumber";
  }

  public static interface View extends Base {
    public static final String START_NODE = "view";
  }

  public static interface Create extends Base {
    public static final String START_NODE = "create";
  }

  public static interface Log extends Base {
    public static final String START_NODE = "log";
  }
}
