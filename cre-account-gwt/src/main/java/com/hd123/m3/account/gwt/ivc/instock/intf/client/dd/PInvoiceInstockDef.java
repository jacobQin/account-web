/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	PInvoiceInstockDef.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月1日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.instock.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * 发票入库单|字段定义
 * 
 * @author lixiaohong
 * @since 1.7
 *
 */
public class PInvoiceInstockDef {
  public static CPInvoiceInstock constants = (CPInvoiceInstock) GWT.create(CPInvoiceInstock.class);
  public static String TABLE_CAPTION = constants.tableCaption();

  public static StringFieldDef billNumber = new StringFieldDef("billNumber",
      constants.billNumber(), false, 0, 32);

  public static StringFieldDef permGroupId = new StringFieldDef("permGroupId",
      constants.permGroupId(), true, 0, 32);
  
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

  public static EnumFieldDef bizState = new EnumFieldDef("bizState", constants.bizState(), false,
      new String[][] {
          {
              BBizStates.INEFFECT, BBizStates.getCaption(BBizStates.INEFFECT) }, {
              BBizStates.EFFECT, BBizStates.getCaption(BBizStates.EFFECT) }, {
              BBizStates.ABORTED, BBizStates.getCaption(BBizStates.ABORTED) }, });
  
  public static EmbeddedFieldDef invoiceType = new EmbeddedFieldDef("invoiceType", constants.invoiceType());
  
  public static EmbeddedFieldDef accountUnit = new EmbeddedFieldDef("accountUnit", constants.accountUnit());

  public static StringFieldDef accountUnit_code = new StringFieldDef("accountUnit.code",
      constants.accountUnit_code(), false, 0, 16);

  public static StringFieldDef accountUnit_name = new StringFieldDef("accountUnit.name",
      constants.accountUnit_name(), true, 0, 64);

  public static StringFieldDef accountUnit_uuid = new StringFieldDef("accountUnit.uuid",
      constants.accountUnit_uuid(), false, 0, 19);
  
  public static EmbeddedFieldDef instockor = new EmbeddedFieldDef("instockor", constants.instockor());

  public static StringFieldDef instockor_code = new StringFieldDef("instockor.code",
      constants.instockor_code(), false, 0, 16);

  public static StringFieldDef instockor_name = new StringFieldDef("instockor.name",
      constants.instockor_name(), true, 0, 64);

  public static StringFieldDef instockor_uuid = new StringFieldDef("instockor.uuid",
      constants.instockor_uuid(), false, 0, 19);
  
  public static DateFieldDef instockDate = new DateFieldDef("instockDate", constants.instockDate(),
      true, null, true, null, true, GWTFormat.fmt_yMd, false);
  
  public static StringFieldDef remark = new StringFieldDef("remark",
      constants.remark(), true, 0, 128);
  
  public static StringFieldDef invoice_code = new StringFieldDef("invoiceCode",
      constants.invoiceCode(), true, 0, 32);
  
  public static StringFieldDef invoice_number = new StringFieldDef("invoiceNumber",
      constants.invoiceNumber(), true, 0, 32);
  
  public static EnumFieldDef isReceive = new EnumFieldDef("receive", constants.isReceive(), false,
      new String[][] {
          {
              "true", "是" }, {
              "false", "否" } });
}
