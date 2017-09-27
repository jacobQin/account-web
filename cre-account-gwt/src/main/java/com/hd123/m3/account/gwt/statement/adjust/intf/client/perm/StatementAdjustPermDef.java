/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StatementAdjustPermDef.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * @author zhuhairui
 * 
 */
public class StatementAdjustPermDef {

  public static final String RESOURCE_STATEMENTADJUST = "account.statementAdjust";
  public static final String PRINT_PATH = "account/receivable/statementadjust";

  public static final Set<String> RESOURCE_STATEMENTADJUST_SET = new HashSet();

  static {
    RESOURCE_STATEMENTADJUST_SET.add(RESOURCE_STATEMENTADJUST);
  }

  public static final BPermission READ = new BPermission(RESOURCE_STATEMENTADJUST,
      AccountAction.READ.getKey());
  public static final BPermission CREATE = new BPermission(RESOURCE_STATEMENTADJUST,
      AccountAction.CREATE.getKey());
  public static final BPermission UPDATE = new BPermission(RESOURCE_STATEMENTADJUST,
      AccountAction.UPDATE.getKey());
  public static final BPermission DELETE = new BPermission(RESOURCE_STATEMENTADJUST,
      AccountAction.DELETE.getKey());
  public static final BPermission EFFECT = new BPermission(RESOURCE_STATEMENTADJUST,
      AccountAction.EFFECT.getKey());
  public static final BPermission ABORT = new BPermission(RESOURCE_STATEMENTADJUST,
      AccountAction.ABORT.getKey());
  public static final BPermission PRINT = new BPermission(RESOURCE_STATEMENTADJUST,
      AccountAction.PRINT.getKey());

}
