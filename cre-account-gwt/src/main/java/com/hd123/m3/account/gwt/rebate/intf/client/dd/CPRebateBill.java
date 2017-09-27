/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	CPRebateBill.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月1日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.rebate.intf.client.dd;

import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * @author lixiaohong
 *
 */
public interface CPRebateBill extends ConstantsWithLookup {
  @DefaultStringValue("销售额返款单")
  public String tableCaption();

  @DefaultStringValue("单号")
  public String billNumber();
  
  @DefaultStringValue("授权组")
  public String permGroupId();
  
  @DefaultStringValue("授权")
  public String permGroup();

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

  @DefaultStringValue("状态")
  public String bizState();

  @DefaultStringValue("项目")
  public String store();
  
  @DefaultStringValue("商户")
  public String tenant();

  @DefaultStringValue("合同")
  public String contract();
  
  @DefaultStringValue("合同编号")
  public String contractCode();
  
  @DefaultStringValue("店招")
  public String contract_name();

  @DefaultStringValue("铺位编号")
  public String positions();
  
  @DefaultStringValue("铺位")
  public String position();

  @DefaultStringValue("记账日期")
  public String accountDate();
  
  @DefaultStringValue("起始日期")
  public String beginDate();

  @DefaultStringValue("截止日期")
  public String endDate();

  @DefaultStringValue("销售返款金额")
  public String backTotal();

  @DefaultStringValue("手续费金额")
  public String poundageTotal();

  @DefaultStringValue("应返金额")
  public String shouldBackTotal();
  
  @DefaultStringValue("说明")
  public String remark();
  
  @DefaultStringValue("返款合计")
  public String rebateTotal();
  
  @DefaultStringValue("返款信息")
  public String rebateInfo();
  
  @DefaultStringValue("返款销售额")
  public String amount();
  
  @DefaultStringValue("销售额返款")
  public String backTotalInfo();

  @DefaultStringValue("手续费")
  public String poundageTotalInfo();
  
  @DefaultStringValue("销售付款明细")
  public String rebateLine();
  
  @DefaultStringValue("合计")
  public String shouldBackTotalInfo();
}
