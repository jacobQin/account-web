/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BExchangeType.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月17日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.biz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.hd123.m3.commons.gwt.util.client.i18n.GResourceUtil;

/**
 * 票据交换类型|枚举
 * 
 * @author LiBin
 * @since 1.7
 *
 */
public enum BExchangeType {
  eviToInv("收据换发票"),invToInv("发票换发票"), eviToEvi("收据换收据");

  private String caption;

  private BExchangeType(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return GResourceUtil.getString(res, this.name(), caption);
  }

  public static R res = GWT.isClient() ? (R) GWT.create(R.class) : null;

  public static interface R extends ConstantsWithLookup {

    @DefaultStringValue("收据换发票")
    String eviToInv();

    @DefaultStringValue("发票换发票")
    String invToInv();

    @DefaultStringValue("收据换收据")
    String eviToEvi();
  }
  
}
