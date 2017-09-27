/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegPermDef.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-24 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.receipt.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * @author chenpeisi
 * 
 */
public class RecInvoiceRegPermDef {
  public static final String RESOURCE_RECINVOICEREG = "account.recInvoiceReg";
  public static final String PRINT_PATH = "account/collection/recInvoiceReg";

  public static final Set<String> RESOURCE_RECINVOICEREG_SET = new HashSet();

  static {
    RESOURCE_RECINVOICEREG_SET.add(RESOURCE_RECINVOICEREG);
  }

  public static final BPermission READ = new BPermission(RESOURCE_RECINVOICEREG,
      AccountAction.READ.getKey());
  public static final BPermission CREATE = new BPermission(RESOURCE_RECINVOICEREG,
      AccountAction.CREATE.getKey());
  public static final BPermission UPDATE = new BPermission(RESOURCE_RECINVOICEREG,
      AccountAction.UPDATE.getKey());
  public static final BPermission DELETE = new BPermission(RESOURCE_RECINVOICEREG,
      AccountAction.DELETE.getKey());
  public static final BPermission EFFECT = new BPermission(RESOURCE_RECINVOICEREG,
      AccountAction.EFFECT.getKey());
  public static final BPermission ABORT = new BPermission(RESOURCE_RECINVOICEREG,
      AccountAction.ABORT.getKey());
  public static final BPermission CONFIG = new BPermission(RESOURCE_RECINVOICEREG,
      AccountAction.CONFIG.getKey());
  public static final BPermission PRINT = new BPermission(RESOURCE_RECINVOICEREG,
      AccountAction.PRINT.getKey());
  public static final BPermission ADDFROMSTATEMENT = new BPermission(RESOURCE_RECINVOICEREG,
      AccountAction.ADDFROMSTATEMENT.getKey());
  public static final BPermission ADDFROMPAYNOTICE = new BPermission(RESOURCE_RECINVOICEREG,
      AccountAction.ADDFROMPAYNOTICE.getKey());
  public static final BPermission ADDFROMSRCBILL = new BPermission(RESOURCE_RECINVOICEREG,
      AccountAction.ADDFROMSRCBILL.getKey());
  public static final BPermission ADDFROMACCOUNT = new BPermission(RESOURCE_RECINVOICEREG,
      AccountAction.ADDFROMACCOUNT.getKey());
}
