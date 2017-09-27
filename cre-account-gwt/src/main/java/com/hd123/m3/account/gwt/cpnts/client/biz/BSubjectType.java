/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	BSubjectType.java
 * 模块说明：	
 * 修改历史：
 * 2013-8-15 - suizhe - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.biz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.hd123.m3.commons.gwt.util.client.i18n.GResourceUtil;

/**
 * @author suizhe
 * 
 */
public enum BSubjectType {
  credit("账款"), predeposit("预存款");

  private String caption;

  private BSubjectType(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return GResourceUtil.getString(res, this.name(), caption);
  }

  public static R res = GWT.isClient() ? (R) GWT.create(R.class) : null;

  public static interface R extends ConstantsWithLookup {

    @DefaultStringValue("账款")
    String credit();

    @DefaultStringValue("预存款")
    String predeposit();
  }
}
