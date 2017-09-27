/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	PInvoiceLineDef.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月8日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;

/**
 * 发票明细行字段定义
 * 
 * @author lixiaohong
 * @since 1.0
 *
 */
public class PInvoiceLineDef {
  public static CPInvoiceLine constants = (CPInvoiceLine) GWT.create(CPInvoiceLine.class);
  
  public static StringFieldDef invoiceCode = new StringFieldDef("invoiceCode",
      constants.invoiceCode(), true, 0, 32);
  
  public static StringFieldDef startNumber = new StringFieldDef("startNumber",
      constants.startNumber(), true, 0, 16);
  
  public static BigDecimalFieldDef quantity = new BigDecimalFieldDef("quantity",
      constants.quantity(), true, null, true, null, true, 0);
  
  public static BigDecimalFieldDef receive_quantity = new BigDecimalFieldDef("quantity",
      constants.receive_quantity(), true, null, true, null, true, 0);
  
  public static BigDecimalFieldDef abort_quantity = new BigDecimalFieldDef("quantity",
      constants.abort_quantity(), true, null, true, null, true, 0);
  
  public static BigDecimalFieldDef return_quantity = new BigDecimalFieldDef("quantity",
      constants.return_quantity(), true, null, true, null, true, 0);
      
  public static BigDecimalFieldDef transport_quantity = new BigDecimalFieldDef("quantity",
      constants.transport_quantity(), true, null, true, null, true, 0);
  
  public static BigDecimalFieldDef recycle_quantity = new BigDecimalFieldDef("quantity",
      constants.recycle_quantity(), true, null, true, null, true, 0);

  public static BigDecimalFieldDef realQty = new BigDecimalFieldDef("realQty",
      constants.realQty(), true, null, true, null, true, 0);
  
  public static BigDecimalFieldDef balanceQty = new BigDecimalFieldDef("balanceQty",
      constants.balanceQty(), true, null, true, null, true, 0);
  
  public static StringFieldDef endNumber = new StringFieldDef("endNumber",
      constants.endNumber(), true, 0, 16);
  
  public static StringFieldDef remark = new StringFieldDef("remark",
      constants.remark(), true, 0, 128);
}
