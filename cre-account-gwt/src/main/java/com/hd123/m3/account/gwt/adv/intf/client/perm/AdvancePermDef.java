/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AdvancePerm.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.adv.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * @author zhuhairui
 * 
 */
public class AdvancePermDef {
  public static final String RESOURCE_ADVANCE = "account.advance";

  public static final Set<String> RESOURCE_ADVANCE_SET = new HashSet();

  static {
    RESOURCE_ADVANCE_SET.add(RESOURCE_ADVANCE);
  }

  public static final BPermission READ = new BPermission(RESOURCE_ADVANCE,
      AccountAction.READ.getKey());

}
