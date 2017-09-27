/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-basic
 * 文件名：	PaymentDefrayalType.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月30日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.option.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.hd123.m3.commons.gwt.util.client.i18n.GResourceUtil;

/**
 * 收款收款方式枚举。
 * 
 * @author LiBin
 * 
 */
public enum PaymentDefrayalType {

  bill("按总额收款"), lineSingle("按科目收款");

  private String caption;

  private PaymentDefrayalType(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return GResourceUtil.getString(res, name(), caption);
  }

  public static R res = GWT.isClient() ? (R) GWT.create(R.class) : null;

  public static interface R extends ConstantsWithLookup {

    @DefaultStringValue("按总额收款")
    String bill();

    @DefaultStringValue("按科目收款")
    String lineSingle();

    @DefaultStringValue("按科目多收款")
    String line();
  }
}
