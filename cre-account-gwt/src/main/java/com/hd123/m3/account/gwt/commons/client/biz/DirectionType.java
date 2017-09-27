/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	DirectionType.java
 * 模块说明：	
 * 修改历史：
 * 2013-7-3 - viva - 创建。
 */
package com.hd123.m3.account.gwt.commons.client.biz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.hd123.m3.commons.gwt.util.client.i18n.GResourceUtil;

/**
 * @author viva
 * 
 */
public enum DirectionType {
  receipt(1, "收"), payment(-1, "付");

  private int value;
  private String caption;

  private DirectionType(int value, String caption) {
    this.value = value;
    this.caption = caption;
  };

  public int getDirectionValue() {
    return value;
  }

  public String getCaption() {
    return caption;
  }

  public static String getCaptionByValue(int value) {
    return receipt.value == value ? GResourceUtil.getString(res, "receipt", receipt.caption)
        : payment.value == value ? GResourceUtil.getString(res, "payment", payment.caption) : null;
  }

  public static R res = GWT.isClient() ? (R) GWT.create(R.class) : null;

  public static interface R extends ConstantsWithLookup {
    @DefaultStringValue("收")
    String receipt();

    @DefaultStringValue("付")
    String payment();
  }
}
