/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-basic
 * 文件名：	SubjectPermDef.java
 * 模块说明：	
 * 修改历史：
 * 2013-9-17 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.subject.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * @author chenrizhang
 * 
 */
public class SubjectPermDef {
  public static final String RESOURCE_SUBJECT = "account.subject";

  public static final Set<String> RESOURCE_SUBJECT_SET = new HashSet();

  static {
    RESOURCE_SUBJECT_SET.add(RESOURCE_SUBJECT);
  }
  public static final BPermission READ = new BPermission(RESOURCE_SUBJECT,
      AccountAction.READ.getKey());
  public static final BPermission CREATE = new BPermission(RESOURCE_SUBJECT,
      AccountAction.CREATE.getKey());
  public static final BPermission UPDATE = new BPermission(RESOURCE_SUBJECT,
      AccountAction.UPDATE.getKey());
  public static final BPermission ENABLE = new BPermission(RESOURCE_SUBJECT,
      AccountAction.ENABLE.getKey());
  public static final BPermission DISABLE = new BPermission(RESOURCE_SUBJECT,
      AccountAction.DISABLE.getKey());
}