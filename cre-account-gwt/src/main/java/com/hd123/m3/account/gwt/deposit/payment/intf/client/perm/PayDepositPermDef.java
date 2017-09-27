/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： PayActionName.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.payment.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * 预付款单权限定义。
 * 
 * @author chenpeisi
 * 
 */
public class PayDepositPermDef {
  public static final String RESOURCE_PAYDEPOSIT = "account.payDeposit";
  public static final String PRINT_PATH = "account/payment/payDeposit";

  public static final Set<String> RESOURCE_PAYDEPOSIT_SET = new HashSet();

  static {
    RESOURCE_PAYDEPOSIT_SET.add(RESOURCE_PAYDEPOSIT);
  }

  public static final BPermission READ = new BPermission(RESOURCE_PAYDEPOSIT,
      AccountAction.READ.getKey());
  public static final BPermission CREATE = new BPermission(RESOURCE_PAYDEPOSIT,
      AccountAction.CREATE.getKey());
  public static final BPermission UPDATE = new BPermission(RESOURCE_PAYDEPOSIT,
      AccountAction.UPDATE.getKey());
  public static final BPermission DELETE = new BPermission(RESOURCE_PAYDEPOSIT,
      AccountAction.DELETE.getKey());
  public static final BPermission EFFECT = new BPermission(RESOURCE_PAYDEPOSIT,
      AccountAction.EFFECT.getKey());
  public static final BPermission ABORT = new BPermission(RESOURCE_PAYDEPOSIT,
      AccountAction.ABORT.getKey());
  public static final BPermission PRINT = new BPermission(RESOURCE_PAYDEPOSIT,
      AccountAction.PRINT.getKey());

}
