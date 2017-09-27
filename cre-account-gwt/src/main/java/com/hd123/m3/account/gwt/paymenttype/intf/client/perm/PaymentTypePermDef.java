/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	PaymentTypePermDef.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymenttype.intf.client.perm;

import java.util.HashSet;
import java.util.Set;

import com.hd123.m3.account.gwt.base.client.AccountAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * @author zhuhairui
 * 
 */
public class PaymentTypePermDef {
  public static final String RESOURCE_PAYMENTTYPE = "account.paymentType";

  public static final Set<String> RESOURCE_PAYMENTTYPE_SET = new HashSet();

  static {
    RESOURCE_PAYMENTTYPE_SET.add(RESOURCE_PAYMENTTYPE);
  }

  /** 查看权 */
  public static final BPermission READ = new BPermission(RESOURCE_PAYMENTTYPE,
      AccountAction.READ.getKey());

  /** 新建权 */
  public static final BPermission CREATE = new BPermission(RESOURCE_PAYMENTTYPE,
      AccountAction.CREATE.getKey());

  /** 修改权 */
  public static final BPermission UPDATE = new BPermission(RESOURCE_PAYMENTTYPE,
      AccountAction.UPDATE.getKey());

  /** 启用权 */
  public static final BPermission ENABLE = new BPermission(RESOURCE_PAYMENTTYPE,
      AccountAction.ENABLE.getKey());

  /** 停用权 */
  public static final BPermission DISABLE = new BPermission(RESOURCE_PAYMENTTYPE,
      AccountAction.DISABLE.getKey());
}
