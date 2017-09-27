/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	GRes.java
 * 模块说明：	
 * 修改历史：
 * 2013-9-16 - suizhe - 创建。
 */
package com.hd123.m3.account.gwt.base.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * 通用资源
 * 
 * @author suizhe
 * 
 */
public interface GRes extends ConstantsWithLookup {
  public static GRes R = (GRes) GWT.create(GRes.class);

  public static final String FIELDNAME_COUNTERPART = "counterpart";
  public static final String FIELDNAME_COUNTERPART_TYPE = "counterpartType";
  public static final String FIELDNAME_COUNTERPART_CODE = "counterpartCode";
  public static final String FIELDNAME_COUNTERPART_NAME = "counterpartName";
  public static final String FIELDNAME_BUSINESS = "business";
  public static final String FIELDNAME_BUSINESS_CODE = "businessCode";
  public static final String FIELDNAME_BUSINESS_NAME = "businessName";
  public static final String FIELDNAME_CONTRACT_BILLTYPE = "contract";
  public static final String FIELDNAME_LEASEBACKCONTRACT_BILLTYPE = "leaseBackContract";

  @DefaultStringValue("商户")
  String counterpart();

  @DefaultStringValue("对方单位类型")
  String counterpartType();

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

  /**
   * 跳转配置参数-单号
   * 
   * @return
   */
  @DefaultStringValue("billNumber")
  String dispatch_key();
}
