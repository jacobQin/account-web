/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	OptionPermDef.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.option.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * @author zhuhairui
 * 
 */
public class OptionPermDef {
  public static final String RESOURCE_OPTION = "account.option";

  public static final Set<String> RESOURCE_OPTION_SET = new HashSet<String>();

  static {
    RESOURCE_OPTION_SET.add(RESOURCE_OPTION);
  }

  /** 查看权 */
  public static final BPermission READ = new BPermission(RESOURCE_OPTION,
      AccountAction.READ.getKey());

  /** 修改权 */
  public static final BPermission UPDATE = new BPermission(RESOURCE_OPTION,
      AccountAction.UPDATE.getKey());

}
