/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-common
 * 文件名：	AccRes.java
 * 模块说明：	
 * 修改历史：
 * 2014-5-22 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.base.server;

import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;

/**
 * @author zhuhairui
 * 
 */
public class AccRes {
  public static final String FIELDNAME_COUNTERPART = "counterpart";
  public static final String FIELDNAME_COUNTERPART_CODE = "counterpartCode";
  public static final String FIELDNAME_COUNTERPART_NAME = "counterpartName";
  public static final String FIELDNAME_BUSINESS = "business";
  public static final String FIELDNAME_BUSINESS_CODE = "businessCode";
  public static final String FIELDNAME_BUSINESS_NAME = "businessName";
  public static final String FIELDNAME_CONTRACT_BILLTYPE = "contract";

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("商户")
    String counterpart();

    @DefaultStringValue("商户代码")
    String counterpartCode();

    @DefaultStringValue("商户名称")
    String counterpartName();

    @DefaultStringValue("项目")
    String business();

    @DefaultStringValue("项目代码")
    String businessCode();

    @DefaultStringValue("项目名称")
    String businessName();
  }
}
