/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	PInvoiceStockDef.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月10日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.stock.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BStockState;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * 发票库存|字段定义
 * 
 * @author lixiaohong
 * @since 1.0
 * 
 */
public class PInvoiceStockDef {
  public static CPInvoiceStock constants = (CPInvoiceStock) GWT.create(CPInvoiceStock.class);
  public static String TABLE_CAPTION = constants.tableCaption();

  public static StringFieldDef sourceBillNumber = new StringFieldDef("sourceBillNumber",
      constants.sourceBillNumber(), false, 0, 32);

  public static EmbeddedFieldDef accountUnit = new EmbeddedFieldDef("accountUnit",
      constants.accountUnit());

  public static StringFieldDef invoiceCode = new StringFieldDef("invoiceCode",
      constants.invoiceCode(), false, 0, 32);

  public static StringFieldDef invoiceNumber = new StringFieldDef("invoiceNumber",
      constants.invoiceNumber(), false, 0, 32);

  public static DateFieldDef abortDate = new DateFieldDef("abortDate", constants.abortDate(), true,
      null, true, null, true, GWTFormat.fmt_yMd, false);

  public static EnumFieldDef state = new EnumFieldDef("state", constants.state(), false,
      new String[][] {
          {
              BStockState.instock.name(), BStockState.instock.getCaption() }, {
              BStockState.received.name(), BStockState.received.getCaption() }, {
              BStockState.used.name(), BStockState.used.getCaption() }, {
              BStockState.aborted.name(), BStockState.aborted.getCaption() }, {
              BStockState.usedRecovered.name(), BStockState.usedRecovered.getCaption() }, {
              BStockState.abortedRecovered.name(), BStockState.abortedRecovered.getCaption() } });

  public static EmbeddedFieldDef invoiceType = new EmbeddedFieldDef("invoiceType",
      constants.invoiceType());

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), false, 0,
      128);

  public static EmbeddedFieldDef holder = new EmbeddedFieldDef("holder", constants.holder());

  public static BigDecimalFieldDef amount = new BigDecimalFieldDef("amount", constants.amount(),
      true, null, false, null, false, 3);

  public static EmbeddedFieldDef createInfo = new EmbeddedFieldDef("createInfo",
      constants.createInfo());

  public static OtherFieldDef createInfo_operator = new OtherFieldDef("createInfo.operator",
      constants.createInfo_operator(), true);

  public static StringFieldDef createInfo_operator_fullName = new StringFieldDef(
      "createInfo.operator.fullName", constants.createInfo_operator_fullName(), true, 0, 32);

  public static StringFieldDef createInfo_operator_id = new StringFieldDef(
      "createInfo.operator.id", constants.createInfo_operator_id(), true, 0, 16);

  public static StringFieldDef createInfo_operator_namespace = new StringFieldDef(
      "createInfo.operator.namespace", constants.createInfo_operator_namespace(), true, 0, 16);

  public static DateFieldDef createInfo_time = new DateFieldDef("createInfo.time",
      constants.createInfo_time(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static EmbeddedFieldDef lastModifyInfo = new EmbeddedFieldDef("lastModifyInfo",
      constants.lastModifyInfo());

  public static OtherFieldDef lastModifyInfo_operator = new OtherFieldDef(
      "lastModifyInfo.operator", constants.lastModifyInfo_operator(), true);

  public static StringFieldDef lastModifyInfo_operator_fullName = new StringFieldDef(
      "lastModifyInfo.operator.fullName", constants.lastModifyInfo_operator_fullName(), true, 0, 32);

  public static StringFieldDef lastModifyInfo_operator_id = new StringFieldDef(
      "lastModifyInfo.operator.id", constants.lastModifyInfo_operator_id(), true, 0, 16);

  public static StringFieldDef lastModifyInfo_operator_namespace = new StringFieldDef(
      "lastModifyInfo.operator.namespace", constants.lastModifyInfo_operator_namespace(), true, 0,
      16);

  public static DateFieldDef lastModifyInfo_time = new DateFieldDef("lastModifyInfo.time",
      constants.lastModifyInfo_time(), true, null, true, null, true, GWTFormat.fmt_yMd, false);
}
