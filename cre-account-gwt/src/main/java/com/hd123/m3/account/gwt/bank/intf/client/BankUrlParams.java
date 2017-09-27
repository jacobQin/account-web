/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 *
 * 项目名：M3
 * 文件名：BankUrlParams.java
 * 模块说明：
 * 修改历史：
 * 2013-817 - chenrizhang - 创建。
 */

package com.hd123.m3.account.gwt.bank.intf.client;


/**
 * @author chenrizhang
 * 
 */
public class BankUrlParams {
  public static final String MODULE_CAPTION = "银行资料";
  public static final String ENTRY_FILE = "Bank.html";

  public static interface Base {
    public static final String PN_ENTITY_UUID = "_uuid";
    public static final String PN_ENTITY_CODE = "_code";
  }

  public static interface Search {
    public static final String PN_IS_RECYCLE = "isRecycle";

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