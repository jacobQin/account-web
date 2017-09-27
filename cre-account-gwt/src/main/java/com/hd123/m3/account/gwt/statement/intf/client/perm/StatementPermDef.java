/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementPermDef.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-24 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * 账单权限定义
 * 
 * @author huangjunxian
 * 
 */
public class StatementPermDef {
  public static final String RESOURCE_STATEMENT = "account.statement";
  public static final String PRINT_PATH = "account/receivable/statement";

  public static final Set<String> RESOURCE_STATEMENT_SET = new HashSet();

  static {
    RESOURCE_STATEMENT_SET.add(RESOURCE_STATEMENT);
  }

  public static final BPermission READ = new BPermission(RESOURCE_STATEMENT,
      AccountAction.READ.getKey());
  public static final BPermission UPDATE = new BPermission(RESOURCE_STATEMENT,
      AccountAction.UPDATE.getKey());
  public static final BPermission DELETE = new BPermission(RESOURCE_STATEMENT,
      AccountAction.DELETE.getKey());
  public static final BPermission EFFECT = new BPermission(RESOURCE_STATEMENT,
      AccountAction.EFFECT.getKey());
  public static final BPermission ABORT = new BPermission(RESOURCE_STATEMENT,
      AccountAction.ABORT.getKey());
  public static final BPermission PRINT = new BPermission(RESOURCE_STATEMENT,
      AccountAction.PRINT.getKey());
  public static final BPermission MODIFYACCOUNT = new BPermission(RESOURCE_STATEMENT,
      AccountAction.MODIFYACCOUNT.getKey());
  public static final BPermission CREATE = new BPermission(RESOURCE_STATEMENT,
      AccountAction.CREATE.getKey());
  /** 附件编辑权 */
  public static final BPermission ATTACHMENTEDIT = new BPermission(RESOURCE_STATEMENT,
      AccountAction.ATTACHMENTEDIT.getKey());
}
