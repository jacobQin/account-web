/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	OptionUrlParams.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.option.intf.client;

import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;

/**
 * @author zhuhairui
 * 
 */
public class OptionUrlParams {

  public static final String MODULE_CAPTION = "结算配置";
  public static final String ENTRY_MODULE = "option.Option";
  public static final String ENTRY_FILE = "Option.html";
  public static final GwtUrl ENTRY_URL = new GwtUrl(ENTRY_MODULE, ENTRY_FILE);

  public static interface View {
    public static final String START_NODE = "View";
  }

  public static interface EditConfig {
    public static final String START_NODE = "EditConfig";
  }

  public static interface EditDefault {
    public static final String START_NODE = "EditDefault";
  }

}
