/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 * 
 * 项目名：	h5web-account-w
 * 文件名：	FeePermDef.java
 * 模块说明：	
 * 修改历史：
 * 2011-7-6 - xuezhiwen - 创建。
 */
package com.hd123.m3.account.gwt.fee.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * @author xuezhiwen
 * 
 */
public class FeePermDef {
  public static final String RESOURCE_FEE = "account.fee";
  public static final String PRINT_PATH = "account/fee";

  public static final Set<String> RESOURCE_FEE_SET = new HashSet();

  static {
    RESOURCE_FEE_SET.add(RESOURCE_FEE);
  }

  public static final BPermission READ = new BPermission(RESOURCE_FEE, AccountAction.READ.getKey());
  public static final BPermission CREATE = new BPermission(RESOURCE_FEE,
      AccountAction.CREATE.getKey());
  public static final BPermission UPDATE = new BPermission(RESOURCE_FEE,
      AccountAction.UPDATE.getKey());
  public static final BPermission DELETE = new BPermission(RESOURCE_FEE,
      AccountAction.DELETE.getKey());
  public static final BPermission EFFECT = new BPermission(RESOURCE_FEE,
      AccountAction.EFFECT.getKey());
  public static final BPermission ABORT = new BPermission(RESOURCE_FEE,
      AccountAction.ABORT.getKey());
  public static final BPermission IMPORT = new BPermission(RESOURCE_FEE,
      AccountAction.IMPORT.getKey());
  public static final BPermission PRINT = new BPermission(RESOURCE_FEE,
      AccountAction.PRINT.getKey());
}
