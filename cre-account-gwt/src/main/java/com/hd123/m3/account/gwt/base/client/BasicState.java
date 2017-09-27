/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-common
 * 文件名：	BasicState.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-25 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.base.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * 基本资料状态取值
 * 
 * @author subinzhu
 * 
 */
public enum BasicState {
  using(), deleted();

  private BasicState() {
  }

  public String getCaption() {
    if (this.name().equals("using"))
      return R.R.using();
    else
      return R.R.deleted();
  }

  public interface R extends ConstantsWithLookup {
    public static final R R = GWT.create(R.class);

    @DefaultStringValue("使用中")
    String using();

    @DefaultStringValue("已删除")
    String deleted();
  }
}
