/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	BConstractState.java
 * 模块说明：	
 * 修改历史：
 * 2015-6-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.biz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * 合同状态
 * 
 * @author liuguilin
 * 
 */
public class BContractState {

  public static final String INEFFECT = "ineffect";
  public static final String EFFECTED = "effected";
  public static final String FINISHED = "finished";

  public static String getCaption(String name) {
    if (INEFFECT.equals(name))
      return R.R.ineffect();
    if (EFFECTED.equals(name))
      return R.R.effected();
    if (FINISHED.equals(name))
      return R.R.finished();
    return "";
  }

  public static interface R extends ConstantsWithLookup {

    public static final R R = GWT.create(R.class);

    @DefaultStringValue("已审核未生效")
    String ineffect();

    @DefaultStringValue("已生效")
    String effected();

    @DefaultStringValue("已终止")
    String finished();
  }
}
