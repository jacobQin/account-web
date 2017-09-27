/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AdvanceUrlParams.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-30 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.adv.intf.client;

import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * @author zhuhairui
 * 
 */
public class AdvanceUrlParams {
  public static final String MODULE_CAPTION = "预存款账户";
  public static final String ENTRY_MODULE = "adv.Advance";
  public static final String ENTRY_FILE = "Advance.html";
  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);

  public static interface Base {
    public static final String DEPOSITPAY = "预付款";
    public static final String DEPOSITREC = "预存款";
  }

  public static interface Search extends CounterpartUnitFilter {
    public static final String START_NODE = "search";
  }

  public static interface CounterpartUnitFilter extends Base {
    /** 对方单位 类似于 */
    public static final String KEY_COUNTERPARTUNIT_LIKE = "counterpartUnitLike";
    /** 对方单位类型等于。String */
    public static final String KEY_COUNTERPARTTYPE = "counterpartType";

    public static final String ORDER_BY_CODE = "code";
  }

  public static interface Log extends Base {
    /** URL参数start取值。 */
    public static final String START_NODE = "log";
    /** URL参数: 是否预存款。 */
    public static final String KEY_IS_REC = "isRec";

    /* 排序条件 */
    /** 发生时间 */
    public static final String ORDER_BY_TIME = "time";
  }

  public static interface LogFilter {
    /** 结算单位 等于 */
    public static final String KEY_ACCOUNTUNIT = "accountUnit";
    /** 对方结算单位类型 等于 */
    public static final String KEY_COUNTERPARTTYPE = "counterpartType";
    /** 对方结算单位 等于 */
    public static final String KEY_COUNTERPARTUNIT = "counterpartUnit";
    /** 预存款科目 等于 */
    public static final String KEY_SUBJECT = "subject";
    /** 预存款类型 等于 */
    public static final String KEY_ADVANCETYPE = "advanceType";
    /** 发生日期 起始于 */
    public static final String KEY_LOGTIME_BEGIN = "logTimeBegin";
    /** 发生日期 终止于 */
    public static final String KEY_LOGTIME_END = "logTimeEnd";
  }

}
