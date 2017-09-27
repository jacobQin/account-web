/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	PayDepositRepaymentPermDef.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-19 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.payment.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * @author zhuhairui
 * 
 */
public class PayDepositRepaymentPermDef {
  public static final String RESOURCE_PAYDEPOSITREPAYMENT = "account.payDepositRepayment";
  public static final String PRINT_PATH = "account/payment/payDepositRepayment";

  public static final Set<String> RESOURCE_DEPOSITREPAYMENT_SET = new HashSet();

  static {
    RESOURCE_DEPOSITREPAYMENT_SET.add(RESOURCE_PAYDEPOSITREPAYMENT);
  }

  public static final BPermission READ = new BPermission(RESOURCE_PAYDEPOSITREPAYMENT,
      AccountAction.READ.getKey());
  public static final BPermission CREATE = new BPermission(RESOURCE_PAYDEPOSITREPAYMENT,
      AccountAction.CREATE.getKey());
  public static final BPermission UPDATE = new BPermission(RESOURCE_PAYDEPOSITREPAYMENT,
      AccountAction.UPDATE.getKey());
  public static final BPermission DELETE = new BPermission(RESOURCE_PAYDEPOSITREPAYMENT,
      AccountAction.DELETE.getKey());
  public static final BPermission EFFECT = new BPermission(RESOURCE_PAYDEPOSITREPAYMENT,
      AccountAction.EFFECT.getKey());
  public static final BPermission ABORT = new BPermission(RESOURCE_PAYDEPOSITREPAYMENT,
      AccountAction.ABORT.getKey());
  public static final BPermission PRINT = new BPermission(RESOURCE_PAYDEPOSITREPAYMENT,
      AccountAction.PRINT.getKey());
}
