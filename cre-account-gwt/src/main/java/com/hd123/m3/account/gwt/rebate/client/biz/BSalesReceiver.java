/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	BSaleReceiver.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月16日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.biz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * 收款方
 * @author chenganbang
 */
public enum BSalesReceiver {

  market("商场"), tenant("商户"), contract("合同");

  private String caption;

  private BSalesReceiver(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
//    return ResourceUtil.getString(R.R, this.name(), caption);
    return caption;
  }

  public static interface R extends Messages{
    public static final R R = GWT.create(R.class);

    @DefaultMessage("商场")
    String market();

    @DefaultMessage("商户")
    String tenant();

    @DefaultMessage("合同")
    String contract();
  }
}
