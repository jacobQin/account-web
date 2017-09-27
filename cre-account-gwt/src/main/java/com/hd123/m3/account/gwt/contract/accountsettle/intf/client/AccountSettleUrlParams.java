/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountSettleUrlParams.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-9 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.contract.accountsettle.intf.client;

import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * @author huangjunxian
 * 
 */
public class AccountSettleUrlParams {
  public static final String MODULE_CAPTION = "出账";

  public static final String ENTRY_MODULE = "contract.accountsettle.AccountSettle";
  public static final String ENTRY_FILE = "AccountSettle.html";
  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);

  /** 合同类型 */
  public static final String KEY_CONTRACT_CATEGORY = "investment/contract/category";

  public static interface Filter {
    /** 项目等于。String */
    public static final String KEY_ACCOUNTUNIT = "accountUnit.uuid";
    /** 对方单位等于。String */
    public static final String KEY_COUNTERPART = "counterpart";
    /** 对方单位类型等于。String */
    public static final String KEY_COUNTERPARTTYPE = "counterpartType";
    /** 合同编号等于。String */
    public static final String KEY_CONTRACT_BILLNUMBER = "contract_billNumber";
    /** 合同标题类似于。String */
    public static final String KEY_CONTRACT_TITLE = "contract_title";
    /** 业态等于。String */
    public static final String KEY_CATEGORY = "category";
    /** 楼层等于。String */
    public static final String KEY_FLOOR = "floor";
    /** 楼宇类型等于。String */
    public static final String KEY_BUILDING_TYPE = "building_type = ";
    /** 楼宇等于。String */
    public static final String KEY_BUILDING = "position = ";
    /** 位置类型等于。String */
    public static final String KEY_POSITION_TYPE = "position_type";
    /** 位置子类型等于。String */
    public static final String KEY_POSITION_SUBTYPE = "position_subType";
    /** 位置等于。String */
    public static final String KEY_POSITION = "position";
    /** 合作方式等于。String */
    public static final String KEY_COOPMODE = "coopmode";
    /** 合同类型等于。String */
    public static final String KEY_CONTRACT_CATEGORY = "contractCategory";
    /** 结算周期名称类似于。String */
    public static final String KEY_SETTLEMENT_NAME = "settlementName";
    /** 出账方式等于。String */
    public static final String KEY_BILLCALCULATETYPE = "billCalculateType";
  }

  public static interface AccountSettle {
    public static final String START_NODE = "accountSettle";
  }
}
