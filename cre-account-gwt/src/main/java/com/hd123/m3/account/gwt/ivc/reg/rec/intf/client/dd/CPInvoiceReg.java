/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	CPInvoiceReg.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.dd;

import com.hd123.m3.account.gwt.ivc.intf.client.dd.CPInvoiceStandardBill;

/**
 * @author chenrizhang
 *
 */
public interface CPInvoiceReg extends CPInvoiceStandardBill {

  @DefaultStringValue("对方单位")
  public String counterpart();

  @DefaultStringValue("来源单号")
  public String sourceBill_billNumber();

  @DefaultStringValue("开票日期")
  public String regDate();

  @DefaultStringValue("应开票金额")
  public String originTotal();

  @DefaultStringValue("本次应开票金额")
  public String unregTotal();

  @DefaultStringValue("开票金额")
  public String total();
  
  @DefaultStringValue("开票税额")
  public String tax();
}
