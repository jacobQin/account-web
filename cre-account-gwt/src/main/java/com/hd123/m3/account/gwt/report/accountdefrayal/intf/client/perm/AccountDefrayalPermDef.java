/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AccountDefrayalPermDef.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-14 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.rumba.webframe.gwt.base.client.permission.BAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * @author zhuhairui
 * 
 */
public class AccountDefrayalPermDef {
  public static final String RESOURCE_ACCOUNTDEFRAYAL = "account.report.accountdefrayal";

  public static final Set<String> RESOURCE_ACCOUNTDEFRAYAL_SET = new HashSet<String>();

  static {
    RESOURCE_ACCOUNTDEFRAYAL_SET.add(RESOURCE_ACCOUNTDEFRAYAL);
  }

  /** 查看权 */
  public static final BPermission READ = new BPermission(RESOURCE_ACCOUNTDEFRAYAL,
      BAction.READ.getKey());
}
