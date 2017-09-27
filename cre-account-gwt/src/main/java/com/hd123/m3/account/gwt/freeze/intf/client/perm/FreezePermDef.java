/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	FreezePermDef.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-4 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.freeze.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * @author zhuhairui
 * 
 */
public class FreezePermDef {

  public static final String RESOURCE_FREEZE = "account.freeze";
  public static final String PRINT_PATH = "account/freeze";

  public static final Set<String> RESOURCE_FREEZE_SET = new HashSet();

  static {
    RESOURCE_FREEZE_SET.add(RESOURCE_FREEZE);
  }

  public static final BPermission READ = new BPermission(RESOURCE_FREEZE,
      AccountAction.READ.getKey());
  public static final BPermission CREATE = new BPermission(RESOURCE_FREEZE,
      AccountAction.CREATE.getKey());
  public static final BPermission FREEZE = new BPermission(RESOURCE_FREEZE,
      AccountAction.FREEZE.getKey());
  public static final BPermission UNFREEZE = new BPermission(RESOURCE_FREEZE,
      AccountAction.UNFREEZE.getKey());
  public static final BPermission PRINT = new BPermission(RESOURCE_FREEZE,
      AccountAction.PRINT.getKey());
}
