package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.IntFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

public class PPaymentDef {

  public static CPPayment constants = (CPPayment) GWT.create(CPPayment.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static EmbeddedFieldDef accountUnit = new EmbeddedFieldDef("accountUnit",
      constants.accountUnit());

  public static StringFieldDef accountUnit_code = new StringFieldDef("accountUnit.code",
      constants.accountUnit_code(), false, 0, 16);

  public static StringFieldDef accountUnit_name = new StringFieldDef("accountUnit.name",
      constants.accountUnit_name(), true, 0, 32);

  public static StringFieldDef accountUnit_uuid = new StringFieldDef("accountUnit.uuid",
      constants.accountUnit_uuid(), false, 0, 19);

  public static StringFieldDef billNumber = new StringFieldDef("billNumber",
      constants.billNumber(), false, 0, 32);

  public static EnumFieldDef finishBizState = new EnumFieldDef("bizState", constants.bizState(),
      false, new String[][] {
          {
              BBizStates.INEFFECT, BBizStates.getCaption(BBizStates.INEFFECT) }, {
              BBizStates.EFFECT, BBizStates.getCaption(BBizStates.EFFECT) }, {
              BBizStates.FINISHED, "已登记" }, {
              BBizStates.ABORTED, BBizStates.getCaption(BBizStates.ABORTED) } });

  public static EnumFieldDef bizState = new EnumFieldDef("bizState", constants.bizState(),
      false, new String[][] {
          {
              BBizStates.INEFFECT, BBizStates.getCaption(BBizStates.INEFFECT) }, {
              BBizStates.EFFECT, BBizStates.getCaption(BBizStates.EFFECT) }, {
              BBizStates.ABORTED, BBizStates.getCaption(BBizStates.ABORTED) } });

  public static EmbeddedFieldDef counterpart = new EmbeddedFieldDef("counterpart",
      constants.counterpart());

  public static StringFieldDef counterpart_code = new StringFieldDef("counterpart.code",
      constants.counterpart_code(), false, 0, 16);

  public static StringFieldDef counterpart_name = new StringFieldDef("counterpart.name",
      constants.counterpart_name(), true, 0, 32);

  public static StringFieldDef counterpart_uuid = new StringFieldDef("counterpart.uuid",
      constants.counterpart_uuid(), false, 0, 19);

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

  public static StringFieldDef dealer = new StringFieldDef("dealer", constants.dealer(), true, 0,
      32);

  public static BigDecimalFieldDef defrayalTotal = new BigDecimalFieldDef("defrayalTotal",
      constants.defrayalTotal(), false, null, true, null, true, 2);

  public static EnumFieldDef defrayalType = new EnumFieldDef("defrayalType",
      constants.defrayalType(), false, new String[][] {
          {
              "bill", PaymentDefrayalTypeDef.constants.bill() }, {
              "lineSingle", PaymentDefrayalTypeDef.constants.lineSingle() }, {
              "line", PaymentDefrayalTypeDef.constants.line() } });

  public static EmbeddedFieldDef depositSubject = new EmbeddedFieldDef("depositSubject",
      constants.depositSubject());

  public static StringFieldDef depositSubject_code = new StringFieldDef("depositSubject.code",
      constants.depositSubject_code(), true, 0, 16);

  public static StringFieldDef depositSubject_name = new StringFieldDef("depositSubject.name",
      constants.depositSubject_name(), true, 0, 32);

  public static StringFieldDef depositSubject_uuid = new StringFieldDef("depositSubject.uuid",
      constants.depositSubject_uuid(), true, 0, 19);

  public static BigDecimalFieldDef depositTotal = new BigDecimalFieldDef("depositTotal",
      constants.depositTotal(), false, null, true, null, true, 2);

  public static IntFieldDef direction = new IntFieldDef("direction", constants.direction(), null,
      true, null, true);

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

  public static StringFieldDef latestComment = new StringFieldDef("latestComment",
      constants.latestComment(), true, 0, 64);

  public static EmbeddedFieldDef overdueTotal = new EmbeddedFieldDef("overdueTotal",
      constants.overdueTotal());

  public static BigDecimalFieldDef overdueTotal_tax = new BigDecimalFieldDef("overdueTotal.tax",
      constants.overdueTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef overdueTotal_total = new BigDecimalFieldDef(
      "overdueTotal.total", constants.overdueTotal_total(), false, null, true, null, true, 2);

  public static DateFieldDef paymentDate = new DateFieldDef("paymentDate", constants.paymentDate(),
      false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0,
      128);

  public static StringFieldDef settleNo = new StringFieldDef("settleNo", constants.settleNo(),
      true, 0, 20);

  public static StringFieldDef coopMode = new StringFieldDef("coopMode", constants.coopMode(),
      true, 0, 64);

  public static EmbeddedFieldDef total = new EmbeddedFieldDef("total", constants.total());

  public static BigDecimalFieldDef total_tax = new BigDecimalFieldDef("total.tax",
      constants.total_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef total_total = new BigDecimalFieldDef("total.total",
      constants.total_total(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef receiptTotal = new EmbeddedFieldDef("receiptTotal",
      constants.receiptTotal());

  public static BigDecimalFieldDef receiptTotal_tax = new BigDecimalFieldDef("receiptTotal.tax",
      constants.receiptTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef receiptTotal_total = new BigDecimalFieldDef(
      "receiptTotal.total", constants.receiptTotal_total(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef unpayedTotal = new EmbeddedFieldDef("unpayedTotal",
      constants.unpayedTotal());

  public static BigDecimalFieldDef unpayedTotal_tax = new BigDecimalFieldDef("unpayedTotal.tax",
      constants.unpayedTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef unpayedTotal_total = new BigDecimalFieldDef(
      "unpayedTotal.total", constants.unpayedTotal_total(), false, null, true, null, true, 2);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

  public static OtherFieldDef version = new OtherFieldDef("version", constants.version(), false);

  public static DateFieldDef versionTime = new DateFieldDef("versionTime", constants.versionTime(),
      true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static DateFieldDef incomeDate = new DateFieldDef("incomeDate", constants.incomeDate(),
      true, null, true, null, true, GWTFormat.fmt_yMd, false);

}
