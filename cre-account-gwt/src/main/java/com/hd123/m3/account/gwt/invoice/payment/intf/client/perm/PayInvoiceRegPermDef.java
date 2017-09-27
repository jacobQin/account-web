/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegPermDef.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-24 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.payment.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * @author chenpeisi
 * 
 */
public class PayInvoiceRegPermDef {
  public static final String RESOURCE_PAYINVOICEREG = "account.payInvoiceReg";
  public static final String PRINT_PATH = "account/payment/payInvoiceReg";

  public static final Set<String> RESOURCE_PAYINVOICEREG_SET = new HashSet();

  static {
    RESOURCE_PAYINVOICEREG_SET.add(RESOURCE_PAYINVOICEREG);
  }

  public static final BPermission READ = new BPermission(RESOURCE_PAYINVOICEREG,
      AccountAction.READ.getKey());
  public static final BPermission CREATE = new BPermission(RESOURCE_PAYINVOICEREG,
      AccountAction.CREATE.getKey());
  public static final BPermission UPDATE = new BPermission(RESOURCE_PAYINVOICEREG,
      AccountAction.UPDATE.getKey());
  public static final BPermission DELETE = new BPermission(RESOURCE_PAYINVOICEREG,
      AccountAction.DELETE.getKey());
  public static final BPermission EFFECT = new BPermission(RESOURCE_PAYINVOICEREG,
      AccountAction.EFFECT.getKey());
  public static final BPermission ABORT = new BPermission(RESOURCE_PAYINVOICEREG,
      AccountAction.ABORT.getKey());
  public static final BPermission CONFIG = new BPermission(RESOURCE_PAYINVOICEREG,
      AccountAction.CONFIG.getKey());
  public static final BPermission PRINT = new BPermission(RESOURCE_PAYINVOICEREG,
      AccountAction.PRINT.getKey());
  public static final BPermission ADDFROMSTATEMENT = new BPermission(RESOURCE_PAYINVOICEREG,
      AccountAction.ADDFROMSTATEMENT.getKey());
  public static final BPermission ADDFROMPAYNOTICE = new BPermission(RESOURCE_PAYINVOICEREG,
      AccountAction.ADDFROMPAYNOTICE.getKey());
  public static final BPermission ADDFROMSRCBILL = new BPermission(RESOURCE_PAYINVOICEREG,
      AccountAction.ADDFROMSRCBILL.getKey());
  public static final BPermission ADDFROMACCOUNT = new BPermission(RESOURCE_PAYINVOICEREG,
      AccountAction.ADDFROMACCOUNT.getKey());
}
