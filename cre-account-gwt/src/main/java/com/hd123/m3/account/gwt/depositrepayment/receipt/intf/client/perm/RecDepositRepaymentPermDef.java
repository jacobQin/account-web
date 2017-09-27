/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecDepositRepaymentPermDef.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-22 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.receipt.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * 预存款还款单权限定义
 * 
 * @author subinzhu
 * 
 */
public class RecDepositRepaymentPermDef {

  public static final String RESOURCE_RECDEPOSITREPAYMENT = "account.recDepositRepayment";
  public static final String PRINT_PATH = "account/collection/recDepositRepayment";

  public static final Set<String> RESOURCE_RECDEPOSITREPAYMENT_SET = new HashSet();

  static {
    RESOURCE_RECDEPOSITREPAYMENT_SET.add(RESOURCE_RECDEPOSITREPAYMENT);
  }

  public static final BPermission READ = new BPermission(RESOURCE_RECDEPOSITREPAYMENT,
      AccountAction.READ.getKey());
  public static final BPermission CREATE = new BPermission(RESOURCE_RECDEPOSITREPAYMENT,
      AccountAction.CREATE.getKey());
  public static final BPermission UPDATE = new BPermission(RESOURCE_RECDEPOSITREPAYMENT,
      AccountAction.UPDATE.getKey());
  public static final BPermission DELETE = new BPermission(RESOURCE_RECDEPOSITREPAYMENT,
      AccountAction.DELETE.getKey());
  public static final BPermission EFFECT = new BPermission(RESOURCE_RECDEPOSITREPAYMENT,
      AccountAction.EFFECT.getKey());
  public static final BPermission ABORT = new BPermission(RESOURCE_RECDEPOSITREPAYMENT,
      AccountAction.ABORT.getKey());
  public static final BPermission PRINT = new BPermission(RESOURCE_RECDEPOSITREPAYMENT,
      AccountAction.PRINT.getKey());

}
