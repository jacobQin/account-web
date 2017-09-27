/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRegPermDef.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月22日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.intf.client;

import com.hd123.rumba.webframe.gwt.base.client.permission.BAction;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;

/**
 * @author chenrizhang
 *
 */
public interface InvoiceRegPermDef {
  public static final String RESOURCE_KEY = "account.invoice.reg.rec";
  public static final String RESOURCE_PATH = "account/ivc/reg.rec";

  public static final String ACTION_ADD_FROM_STATEMENT = "addFromStatement";
  public static final String ACTION_ADD_FROM_PAYMENT = "addFromPayment";
  public static final String ACTION_ADD_FROM_PAYNOTICE = "addFromPayNotice";
  public static final String ACTION_ADD_FROM_ACCOUNT = "addFromAccount";

  public static final BPermission CREATE = new BPermission(RESOURCE_KEY,
      BAction.CREATE.getKey());
  
}
