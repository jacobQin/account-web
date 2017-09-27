/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	PayDepositMovePermDef.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-10 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.payment.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * 预付款还款单权限定义
 * 
 * @author zhuhairui
 * 
 */
public class PayDepositMovePermDef {

  public static final String RESOURCE_PAYDEPOSITMOVE = "account.payDepositMove";
  public static final String PRINT_PATH = "account/payment/payDepositMove";

  public static final Set<String> RESOURCE_PAYDEPOSITMOVE_SET = new HashSet();

  static {
    RESOURCE_PAYDEPOSITMOVE_SET.add(RESOURCE_PAYDEPOSITMOVE);
  }

  public static final BPermission READ = new BPermission(RESOURCE_PAYDEPOSITMOVE,
      AccountAction.READ.getKey());
  public static final BPermission CREATE = new BPermission(RESOURCE_PAYDEPOSITMOVE,
      AccountAction.CREATE.getKey());
  public static final BPermission UPDATE = new BPermission(RESOURCE_PAYDEPOSITMOVE,
      AccountAction.UPDATE.getKey());
  public static final BPermission DELETE = new BPermission(RESOURCE_PAYDEPOSITMOVE,
      AccountAction.DELETE.getKey());
  public static final BPermission EFFECT = new BPermission(RESOURCE_PAYDEPOSITMOVE,
      AccountAction.EFFECT.getKey());
  public static final BPermission ABORT = new BPermission(RESOURCE_PAYDEPOSITMOVE,
      AccountAction.ABORT.getKey());
  public static final BPermission PRINT = new BPermission(RESOURCE_PAYDEPOSITMOVE,
      AccountAction.PRINT.getKey());
}
