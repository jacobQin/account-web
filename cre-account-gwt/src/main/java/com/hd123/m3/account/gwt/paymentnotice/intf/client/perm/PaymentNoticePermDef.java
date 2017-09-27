/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentNoticePermDef.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-11 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * @author zhuhairui
 * 
 */
public class PaymentNoticePermDef {
  public static final String RESOURCE_PAYMENTNOTICE = "account.paymentNotice";
  public static final String PRINT_PATH = "account/receivable/paymentNotice";

  public static final Set<String> RESOURCE_PAYMENTNOTICE_SET = new HashSet();

  static {
    RESOURCE_PAYMENTNOTICE_SET.add(RESOURCE_PAYMENTNOTICE);
  }

  public static final BPermission READ = new BPermission(RESOURCE_PAYMENTNOTICE,
      AccountAction.READ.getKey());
  public static final BPermission CREATE = new BPermission(RESOURCE_PAYMENTNOTICE,
      AccountAction.CREATE.getKey());
  public static final BPermission UPDATE = new BPermission(RESOURCE_PAYMENTNOTICE,
      AccountAction.UPDATE.getKey());
  public static final BPermission DELETE = new BPermission(RESOURCE_PAYMENTNOTICE,
      AccountAction.DELETE.getKey());
  public static final BPermission EFFECT = new BPermission(RESOURCE_PAYMENTNOTICE,
      AccountAction.EFFECT.getKey());
  public static final BPermission ABORT = new BPermission(RESOURCE_PAYMENTNOTICE,
      AccountAction.ABORT.getKey());
  public static final BPermission PRINT = new BPermission(RESOURCE_PAYMENTNOTICE,
      AccountAction.PRINT.getKey());
}
