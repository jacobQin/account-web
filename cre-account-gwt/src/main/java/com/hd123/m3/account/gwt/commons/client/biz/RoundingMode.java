/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	RoundingMode.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-14 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.commons.client.biz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.hd123.m3.commons.gwt.util.client.i18n.GResourceUtil;

/**
 * @author subinzhu
 * 
 */
public enum RoundingMode {
  HALF_UP("四舍五入"), DOWN("截断"), UP("向上取整");

  private String caption;

  private RoundingMode(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return GResourceUtil.getString(res, name(), caption);
  }

  public static R res = GWT.isClient() ? (R) GWT.create(R.class) : null;

  public static interface R extends ConstantsWithLookup {

    @DefaultStringValue("四舍五入")
    String HALF_UP();

    @DefaultStringValue("截断")
    String DOWN();

    @DefaultStringValue("向上取整")
    String UP();
  }
}
