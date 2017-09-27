/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountSettlePermDef.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-10 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.contract.accountsettle.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * 出账权限定义
 * 
 * @author huangjunxian
 * 
 */
public class AccountSettlePermDef {
  public static final String RESOURCE_ACCOUNTSETTLE = "account.accountSettle";

  public static final Set<String> RESOURCE_ACCOUNTSETTLE_SET = new HashSet();

  static {
    RESOURCE_ACCOUNTSETTLE_SET.add(RESOURCE_ACCOUNTSETTLE);
  }

  public static final BPermission ACCSETTLE = new BPermission(RESOURCE_ACCOUNTSETTLE,
      AccountAction.ACCSETTLE.getKey());
}
