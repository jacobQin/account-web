/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BStockState.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月10日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.common.client.biz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.hd123.m3.commons.gwt.util.client.i18n.GResourceUtil;

/**
 * 库存状态|枚举
 * 
 * @author lixiaohong
 * @since 1.7
 * 
 */
public enum BStockState {
  instock("已入库"), received("已领用"), used("已使用"), aborted("已作废 "), usedRecovered("已使用回收"), abortedRecovered(
      "已作废回收 ");

  private String caption;

  private BStockState(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return GResourceUtil.getString(res, this.name(), caption);
  }

  public static R res = GWT.isClient() ? (R) GWT.create(R.class) : null;

  public static interface R extends ConstantsWithLookup {

    @DefaultStringValue("已入库")
    String instock();

    @DefaultStringValue("已领用")
    String received();

    @DefaultStringValue("已使用")
    String used();

    @DefaultStringValue("已作废")
    String aborted();

    @DefaultStringValue("已使用回收")
    String usedRecovered();

    @DefaultStringValue("已作废回收")
    String abortedRecovered();
  }
}
