/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	PInvoiceRegDef.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * 发票领用单|字段定义
 * 
 * @author chenrizhang
 * @since 1.7
 * 
 */
public class PInvoiceRegDef {
  public static CPInvoiceReg constants = (CPInvoiceReg) GWT.create(CPInvoiceReg.class);

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
              BBizStates.ABORTED, BBizStates.getCaption(BBizStates.ABORTED) } });

  public static EmbeddedFieldDef invoiceType = new EmbeddedFieldDef("invoiceType",
      constants.invoiceType());

  public static EmbeddedFieldDef accountUnit = new EmbeddedFieldDef("accountUnit",
      constants.accountUnit());
  public static EmbeddedFieldDef counterpart = new EmbeddedFieldDef("counterpart",
      constants.counterpart());
  public static StringFieldDef sourceBill_billNumber = new StringFieldDef("sourceBill.billNumber",
      constants.sourceBill_billNumber(), true, 0, 32);

  public static DateFieldDef regDate = new DateFieldDef("regDate", constants.regDate(), true, null,
      true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0,
      128);

  public static StringFieldDef invoice_code = new StringFieldDef("invoiceCode",
      constants.invoiceCode(), false, 0, 32, "^[\\w]+$", "只能是字母或数字");
  public static StringFieldDef invoice_number = new StringFieldDef("invoiceNumber",
      constants.invoiceNumber(), false, 0, 32, "^[\\d]+$", "只能是数字");

  public static BigDecimalFieldDef originTotal = new BigDecimalFieldDef("originTotal",
      constants.originTotal(), false, null, true, null, true, 2);
  public static BigDecimalFieldDef unregTotal = new BigDecimalFieldDef("unregTotal",
      constants.unregTotal(), false, null, true, null, true, 2);
  public static BigDecimalFieldDef total = new BigDecimalFieldDef("total", constants.total(),
      false, BoundaryValue.BIGDECIMAL_MINVALUE_S2, false, BoundaryValue.BIGDECIMAL_MAXVALUE_S2,
      false, 2);
  public static BigDecimalFieldDef taxTotal = new BigDecimalFieldDef("tax",
      constants.tax(), false, null, true, null, true, 2);
}
