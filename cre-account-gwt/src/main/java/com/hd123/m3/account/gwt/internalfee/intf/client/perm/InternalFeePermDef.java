/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InternalFeePermDef.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * @author liuguilin
 * 
 */
public class InternalFeePermDef {
  public static final String RESOURCE_INTERNALFEE = "account.internalFee";
  public static final String PRINT_PATH = "account/internalFee";

  public static final Set<String> RESOURCE_INTERNALFEE_SET = new HashSet();

  static {
    RESOURCE_INTERNALFEE_SET.add(RESOURCE_INTERNALFEE);
  }

  public static final BPermission READ = new BPermission(RESOURCE_INTERNALFEE,
      AccountAction.READ.getKey());
  public static final BPermission CREATE = new BPermission(RESOURCE_INTERNALFEE,
      AccountAction.CREATE.getKey());
  public static final BPermission UPDATE = new BPermission(RESOURCE_INTERNALFEE,
      AccountAction.UPDATE.getKey());
  public static final BPermission DELETE = new BPermission(RESOURCE_INTERNALFEE,
      AccountAction.DELETE.getKey());
  public static final BPermission EFFECT = new BPermission(RESOURCE_INTERNALFEE,
      AccountAction.EFFECT.getKey());
  public static final BPermission ABORT = new BPermission(RESOURCE_INTERNALFEE,
      AccountAction.ABORT.getKey());
  public static final BPermission PRINT = new BPermission(RESOURCE_INTERNALFEE,
      AccountAction.PRINT.getKey());
}
