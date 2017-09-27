/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentPermDef.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-4 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * @author subinzhu
 * 
 */
public class PaymentPermDef {
  public static final String RESOURCE_PAYMENT = "account.payment.pay";
  public static final String PRINT_PATH = "account/payment/payment";

  public static final Set<String> RESOURCE_PAYMENT_SET = new HashSet();

  static {
    RESOURCE_PAYMENT_SET.add(RESOURCE_PAYMENT);
  }

  public static final BPermission READ = new BPermission(RESOURCE_PAYMENT,
      AccountAction.READ.getKey());
  public static final BPermission CREATE = new BPermission(RESOURCE_PAYMENT,
      AccountAction.CREATE.getKey());
  public static final BPermission UPDATE = new BPermission(RESOURCE_PAYMENT,
      AccountAction.UPDATE.getKey());
  public static final BPermission DELETE = new BPermission(RESOURCE_PAYMENT,
      AccountAction.DELETE.getKey());
  public static final BPermission EFFECT = new BPermission(RESOURCE_PAYMENT,
      AccountAction.EFFECT.getKey());
  public static final BPermission ABORT = new BPermission(RESOURCE_PAYMENT,
      AccountAction.ABORT.getKey());
  public static final BPermission PRINT = new BPermission(RESOURCE_PAYMENT,
      AccountAction.PRINT.getKey());
  public static final BPermission CONFIG = new BPermission(RESOURCE_PAYMENT,
      AccountAction.CONFIG.getKey());
  public static final BPermission ADDFROMSTATEMENT = new BPermission(RESOURCE_PAYMENT,
      AccountAction.ADDFROMSTATEMENT.getKey());
  public static final BPermission ADDFROMINVOICE = new BPermission(RESOURCE_PAYMENT,
      AccountAction.ADDFROMINVOICE.getKey());
  public static final BPermission ADDFROMPAYNOTICE = new BPermission(RESOURCE_PAYMENT,
      AccountAction.ADDFROMPAYNOTICE.getKey());
  public static final BPermission ADDFROMSRCBILL = new BPermission(RESOURCE_PAYMENT,
      AccountAction.ADDFROMSRCBILL.getKey());
  public static final BPermission ADDFROMACCOUNT = new BPermission(RESOURCE_PAYMENT,
      AccountAction.ADDFROMACCOUNT.getKey());
  public static final BPermission IVCREGISTE = new BPermission(RESOURCE_PAYMENT,
      AccountAction.IVCREGISTE.getKey());
}
