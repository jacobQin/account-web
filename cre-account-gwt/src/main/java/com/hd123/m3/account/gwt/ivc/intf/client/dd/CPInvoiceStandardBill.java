/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	CPInvoiceStandardBill.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.intf.client.dd;

import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * @author lixiaohong
 *
 */
public interface CPInvoiceStandardBill extends ConstantsWithLookup {

  @DefaultStringValue("单号")
  public String billNumber();

  @DefaultStringValue("单号")
  public String bizState();

  @DefaultStringValue("bpmMessage")
  public String bpmMessage();

  @DefaultStringValue("bpmState")
  public String bpmState();

  @DefaultStringValue("uuid")
  public String uuid();

  @DefaultStringValue("版本")
  public String version();

  @DefaultStringValue("版本时间")
  public String versionTime();

  @DefaultStringValue("授权组")
  public String permGroupId();

  @DefaultStringValue("授权组标题")
  public String permGroupTitle();

  @DefaultStringValue("创建信息")
  public String createInfo();

  @DefaultStringValue("创建人")
  public String createInfo_operator();

  @DefaultStringValue("创建人名称")
  public String createInfo_operator_fullName();

  @DefaultStringValue("创建人代码")
  public String createInfo_operator_id();

  @DefaultStringValue("创建人组织")
  public String createInfo_operator_namespace();

  @DefaultStringValue("创建时间")
  public String createInfo_time();

  @DefaultStringValue("最后修改信息")
  public String lastModifyInfo();

  @DefaultStringValue("最后修改人")
  public String lastModifyInfo_operator();

  @DefaultStringValue("最后修改人名称")
  public String lastModifyInfo_operator_fullName();

  @DefaultStringValue("最后修改人代码")
  public String lastModifyInfo_operator_id();

  @DefaultStringValue("最后修改人组织")
  public String lastModifyInfo_operator_namespace();

  @DefaultStringValue("最后修改时间")
  public String lastModifyInfo_time();

  @DefaultStringValue("发票类型")
  public String invoiceType();

  @DefaultStringValue("项目")
  public String accountUnit();

  @DefaultStringValue("项目代码")
  public String accountUnit_code();

  @DefaultStringValue("项目名称")
  public String accountUnit_name();

  @DefaultStringValue("项目uuid")
  public String accountUnit_uuid();

  @DefaultStringValue("发票代码")
  public String invoiceCode();

  @DefaultStringValue("发票号码")
  public String invoiceNumber();

  @DefaultStringValue("说明")
  public String remark();
}
