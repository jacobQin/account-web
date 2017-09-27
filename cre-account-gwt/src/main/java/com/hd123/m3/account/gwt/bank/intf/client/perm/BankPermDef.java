/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-basic
 * 文件名：	BankPermDef.java
 * 模块说明：	
 * 修改历史：
 * 2013-9-17 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.bank.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * @author chenrizhang
 * 
 */
public class BankPermDef {

  public static final String RESOURCE_BANK = "account.bank";

  public static final Set<String> RESOURCE_BANK_SET = new HashSet();

  static {
    RESOURCE_BANK_SET.add(RESOURCE_BANK);
  }
  public static final BPermission READ = new BPermission(RESOURCE_BANK, AccountAction.READ.getKey());
  public static final BPermission CREATE = new BPermission(RESOURCE_BANK,
      AccountAction.CREATE.getKey());
  public static final BPermission UPDATE = new BPermission(RESOURCE_BANK,
      AccountAction.UPDATE.getKey());
  public static final BPermission DELETE = new BPermission(RESOURCE_BANK,
      AccountAction.DELETE.getKey());
}
