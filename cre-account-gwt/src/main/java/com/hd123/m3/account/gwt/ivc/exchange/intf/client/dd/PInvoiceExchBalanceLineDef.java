/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	PInvoiceExchBalanceLine.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月20日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;

/**
 * 票据明细字段定义
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class PInvoiceExchBalanceLineDef {

  public static CPInvoiceExchBalanceLine constants = GWT.create(CPInvoiceExchBalanceLine.class);

  public static StringFieldDef oldReceiptCode = new StringFieldDef("oldReceiptCode",
      constants.oldReceiptCode(), true, 0, 32);

  public static StringFieldDef oldReceiptNumber = new StringFieldDef("oldReceiptNumber",
      constants.oldReceiptNumber(), true, 0, 16);

  public static StringFieldDef subject = new StringFieldDef("subject",
      constants.subject(), true, 0, 64);
  
  public static StringFieldDef beginEndDate = new StringFieldDef("beginEndDate",
      constants.beginEndDate(), true, 0, 64);
  
  public static StringFieldDef sourceBillType = new StringFieldDef("sourceBillType",
      constants.sourceBillType(), true, 0, 64);
  
  public static StringFieldDef sourceBill = new StringFieldDef("sourceBill",
      constants.sourceBill(), true, 0, 64);
  
  public static StringFieldDef oldInvoiceCode = new StringFieldDef("oldInvoiceCode",
      constants.oldInvoiceCode(), true, 0, 32);

  public static StringFieldDef oldInvoiceNumber = new StringFieldDef("oldInvoiceNumber",
      constants.oldInvoiceNumber(), true, 0, 16);
  
  
  public static StringFieldDef newReceiptCode = new StringFieldDef("newReceiptCode",
      constants.newReceiptCode(), true, 0, 32);

  public static StringFieldDef newReceiptNumber = new StringFieldDef("newReceiptNumber",
      constants.newReceiptNumber(), true, 0, 16);
  
  public static StringFieldDef newInvoiceCode = new StringFieldDef("newInvoiceCode",
      constants.newInvoiceCode(), true, 0, 32);

  public static StringFieldDef newInvoiceNumber = new StringFieldDef("newInvoiceNumber",
      constants.newInvoiceNumber(), true, 0, 16);

  public static StringFieldDef balanceInvoiceCode = new StringFieldDef("balanceInvoiceCode",
      constants.balanceInvoiceCode(), true, 0, 32);

  public static StringFieldDef balanceInvoiceNumber = new StringFieldDef("balanceInvoiceNumber",
      constants.balanceInvoiceNumber(), true, 0, 16);

  public static BigDecimalFieldDef amount = new BigDecimalFieldDef("amount", constants.amount(),
      true, null, true, null, true, 2);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0,
      128);
}
