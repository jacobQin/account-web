/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	BReceiptOverdueDefault.java
 * 模块说明：	
 * 修改历史：
 * 2016年2月19日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.commons.client.biz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.hd123.m3.commons.gwt.util.client.i18n.GResourceUtil;

/**
 * 收款单中的滞纳金默认值
 * 
 * @author lixiaohong
 * 
 */
public enum BReceiptOverdueDefault {
  zero("zero", "默认为0"), calcValue("calcValue", "默认为计算金额");
  private String value;
  private String caption;

  private BReceiptOverdueDefault(String value, String caption) {
    this.value = value;
    this.caption = caption;
  };

  public String getDirectionValue() {
    return value;
  }

  public String getCaption() {
    return caption;
  }

  public static String getCaptionByValue(int value) {
    return zero.value.equals(value) ? GResourceUtil.getString(res, "zero", zero.caption)
        : calcValue.value.equals(value) ? GResourceUtil.getString(res, "calcValue",
            calcValue.caption) : null;
  }

  public static R res = GWT.isClient() ? (R) GWT.create(R.class) : null;

  public static interface R extends ConstantsWithLookup {
    @DefaultStringValue("默认为0")
    String zero();

    @DefaultStringValue("默认为计算金额")
    String calcValue();
  }
}
