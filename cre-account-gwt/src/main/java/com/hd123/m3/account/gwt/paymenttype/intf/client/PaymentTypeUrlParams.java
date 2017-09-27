/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	PaymentTypeUrlParams.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymenttype.intf.client;

import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * @author zhuhairui
 * 
 */
public class PaymentTypeUrlParams {
  public static final String MODULE_CAPTION = "付款方式";
  public static final String ENTRY_MODULE = "paymenttype.PaymentType";
  public static final String ENTRY_FILE = "PaymentType.html";
  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);

  public static interface View {
    public static final String START_NODE = "View";
  }

}
