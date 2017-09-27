/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	RecDepositMovePermDef.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.receipt.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * @author zhuhairui
 * 
 */
public class RecDepositMovePermDef {

  public static final String RESOURCE_RECDEPOSITMOVE = "account.recDepositMove";
  public static final String PRINT_PATH = "account/collection/recDepositMove";

  public static final Set<String> RESOURCE_RECDEPOSITMOVE_SET = new HashSet();

  static {
    RESOURCE_RECDEPOSITMOVE_SET.add(RESOURCE_RECDEPOSITMOVE);
  }

  public static final BPermission READ = new BPermission(RESOURCE_RECDEPOSITMOVE,
      AccountAction.READ.getKey());
  public static final BPermission CREATE = new BPermission(RESOURCE_RECDEPOSITMOVE,
      AccountAction.CREATE.getKey());
  public static final BPermission UPDATE = new BPermission(RESOURCE_RECDEPOSITMOVE,
      AccountAction.UPDATE.getKey());
  public static final BPermission DELETE = new BPermission(RESOURCE_RECDEPOSITMOVE,
      AccountAction.DELETE.getKey());
  public static final BPermission EFFECT = new BPermission(RESOURCE_RECDEPOSITMOVE,
      AccountAction.EFFECT.getKey());
  public static final BPermission ABORT = new BPermission(RESOURCE_RECDEPOSITMOVE,
      AccountAction.ABORT.getKey());
  public static final BPermission PRINT = new BPermission(RESOURCE_RECDEPOSITMOVE,
      AccountAction.PRINT.getKey());
}
