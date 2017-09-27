/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BPaymentNoticeState.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-19 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.biz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.hd123.m3.commons.gwt.util.client.i18n.GResourceUtil;

/**
 * 收付款通知单状态
 * 
 * @author zhuhairui
 * 
 */
public enum BPaymentNoticeState {
  initial("未提交"), submitted("已提交"), audited("已审核"), rejected("审批不通过"), aborted("已作废");

  private String caption;

  private BPaymentNoticeState(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return GResourceUtil.getString(res, this.name(), caption);
  }

  public static R res = GWT.isClient() ? (R) GWT.create(R.class) : null;

  public static interface R extends ConstantsWithLookup {

    @DefaultStringValue("未提交")
    String initial();

    @DefaultStringValue("已提交")
    String submitted();

    @DefaultStringValue("已审核")
    String audited();

    @DefaultStringValue("审批不通过")
    String rejected();

    @DefaultStringValue("已作废")
    String aborted();
  }
}
