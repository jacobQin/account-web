/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-api
 * 文件名：	UsageType.java
 * 模块说明：	
 * 修改历史：
 * 2015-4-21 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.biz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.hd123.m3.commons.gwt.util.client.i18n.GResourceUtil;

/**
 * 用途类型
 * 
 * @author huangjunxian
 * 
 */
public enum BUsageType {
  fixedRent("租金"), saleDeduct("销售提成"), salePayment("销售返款"), tempFee("临时费用"), fixedFee("固定费用"), creditDeposit(
      "预存款"), margin("保证金"),intention("意向金"), other("其它");

  private String caption;

  private BUsageType(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return GResourceUtil.getString(res, this.name(), caption);
  }

  public static R res = GWT.isClient() ? (R) GWT.create(R.class) : null;

  public static interface R extends ConstantsWithLookup {

    @DefaultStringValue("租金")
    String fixedRent();

    @DefaultStringValue("销售提成")
    String saleDeduct();

    @DefaultStringValue("销售返款")
    String salePayment();

    @DefaultStringValue("临时费用")
    String tempFee();

    @DefaultStringValue("固定费用")
    String fixedFee();

    @DefaultStringValue("预存款")
    String creditDeposit();

    @DefaultStringValue("保证金")
    String margin();
    
    @DefaultStringValue("意向金")
    String intention();
    
    @DefaultStringValue("其它")
    String other();
  }
}
