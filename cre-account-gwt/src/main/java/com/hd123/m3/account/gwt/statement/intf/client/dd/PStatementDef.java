package com.hd123.m3.account.gwt.statement.intf.client.dd;

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

public class PStatementDef {

  public static CPStatement constants = (CPStatement) GWT.create(CPStatement.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static EmbeddedFieldDef accountRange = new EmbeddedFieldDef("accountRange",
      constants.accountRange());

  public static DateFieldDef accountRange_beginDate = new DateFieldDef("accountRange.beginDate",
      constants.accountRange_beginDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static DateFieldDef accountRange_endDate = new DateFieldDef("accountRange.endDate",
      constants.accountRange_endDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static DateFieldDef accountTime = new DateFieldDef("accountTime", constants.accountTime(),
      false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static EmbeddedFieldDef accountUnit = new EmbeddedFieldDef("accountUnit",
      constants.accountUnit());

  public static StringFieldDef accountUnit_code = new StringFieldDef("accountUnit.code",
      constants.accountUnit_code(), false, 0, 16);

  public static StringFieldDef accountUnit_name = new StringFieldDef("accountUnit.name",
      constants.accountUnit_name(), true, 0, 32);

  public static StringFieldDef accountUnit_uuid = new StringFieldDef("accountUnit.uuid",
      constants.accountUnit_uuid(), false, 0, 19);

  public static StringFieldDef contractCategory = new StringFieldDef("contractCategory",
      constants.contractCategory(), true, 0, 128);

  //应付金额
  public static BigDecimalFieldDef payTotalTotal = new BigDecimalFieldDef("payTotal.total",
      constants.payTotal(), true, null, true, null, true, 2);
  //应收金额
  public static BigDecimalFieldDef receiptTotalTotal = new BigDecimalFieldDef(
      "receiptTotal.total", constants.receiptTotal(), true, null, true, null, true, 2);

  public static StringFieldDef coopMode = new StringFieldDef("coopMode", constants.coopMode(),
      true, 0, 64);

  public static StringFieldDef billNumber = new StringFieldDef("billNumber",
      constants.billNumber(), false, 0, 32);

  public static EnumFieldDef bizState = new EnumFieldDef("bizState", constants.bizState(), false,
      new String[][] {
          {
              BBizStates.INEFFECT, BBizStates.getCaption(BBizStates.INEFFECT) }, {
              BBizStates.EFFECT, BBizStates.getCaption(BBizStates.EFFECT) }, {
              BBizStates.ABORTED, BBizStates.getCaption(BBizStates.ABORTED) }, });

  public static EmbeddedFieldDef contract = new EmbeddedFieldDef("contract", constants.contract());

  public static StringFieldDef contract_code = new StringFieldDef("contract.code",
      constants.contract_code(), false, 0, 32);

  public static StringFieldDef contract_name = new StringFieldDef("contract.name",
      constants.contract_name(), false, 0, 64);

  public static StringFieldDef contract_uuid = new StringFieldDef("contract.uuid",
      constants.contract_uuid(), false, 0, 19);

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

  public static EmbeddedFieldDef freePayTotal = new EmbeddedFieldDef("freePayTotal",
      constants.freePayTotal());

  public static BigDecimalFieldDef freePayTotal_tax = new BigDecimalFieldDef("freePayTotal.tax",
      constants.freePayTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef freePayTotal_total = new BigDecimalFieldDef(
      "freePayTotal.total", constants.freePayTotal_total(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef freeReceiptTotal = new EmbeddedFieldDef("freeReceiptTotal",
      constants.freeReceiptTotal());

  public static BigDecimalFieldDef freeReceiptTotal_tax = new BigDecimalFieldDef(
      "freeReceiptTotal.tax", constants.freeReceiptTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef freeReceiptTotal_total = new BigDecimalFieldDef(
      "freeReceiptTotal.total", constants.freeReceiptTotal_total(), false, null, true, null, true,
      2);

  public static BigDecimalFieldDef ivcPayAdj = new BigDecimalFieldDef("ivcPayAdj",
      constants.ivcPayAdj(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef ivcPayed = new BigDecimalFieldDef("ivcPayed",
      constants.ivcPayed(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef ivcPayTotal = new EmbeddedFieldDef("ivcPayTotal",
      constants.ivcPayTotal());

  public static BigDecimalFieldDef ivcPayTotal_tax = new BigDecimalFieldDef("ivcPayTotal.tax",
      constants.ivcPayTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef ivcPayTotal_total = new BigDecimalFieldDef("ivcPayTotal.total",
      constants.ivcPayTotal_total(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef ivcReceiptAdj = new BigDecimalFieldDef("ivcReceiptAdj",
      constants.ivcReceiptAdj(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef ivcReceipted = new BigDecimalFieldDef("ivcReceipted",
      constants.ivcReceipted(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef ivcReceiptTotal = new EmbeddedFieldDef("ivcReceiptTotal",
      constants.ivcReceiptTotal());

  public static BigDecimalFieldDef ivcReceiptTotal_tax = new BigDecimalFieldDef(
      "ivcReceiptTotal.tax", constants.ivcReceiptTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef ivcReceiptTotal_total = new BigDecimalFieldDef(
      "ivcReceiptTotal.total", constants.ivcReceiptTotal_total(), false, null, true, null, true, 2);

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

  public static DateFieldDef lastReceiptDate = new DateFieldDef("lastReceiptDate",
      constants.lastReceiptDate(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef latestComment = new StringFieldDef("latestComment",
      constants.latestComment(), true, 0, 128);

  public static BigDecimalFieldDef payAdj = new BigDecimalFieldDef("payAdj", constants.payAdj(),
      false, null, true, null, true, 2);

  public static BigDecimalFieldDef payed = new BigDecimalFieldDef("payed", constants.payed(),
      false, null, true, null, true, 2);

  public static IntFieldDef payGrace = new IntFieldDef("payGrace", constants.payGrace(), null,
      true, null, true);

  public static EmbeddedFieldDef payTotal = new EmbeddedFieldDef("payTotal", constants.payTotal());

  public static BigDecimalFieldDef payTotal_tax = new BigDecimalFieldDef("payTotal.tax",
      constants.payTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef payTotal_total = new BigDecimalFieldDef("payTotal.total",
      constants.payTotal_total(), false, null, true, null, true, 2);

  public static DateFieldDef planPayDate = new DateFieldDef("planPayDate", constants.planPayDate(),
      true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static BigDecimalFieldDef receiptAdj = new BigDecimalFieldDef("receiptAdj",
      constants.receiptAdj(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef receipted = new BigDecimalFieldDef("receipted",
      constants.receipted(), false, null, true, null, true, 2);

  public static IntFieldDef receiptGrace = new IntFieldDef("receiptGrace",
      constants.receiptGrace(), null, true, null, true);

  public static EmbeddedFieldDef receiptTotal = new EmbeddedFieldDef("receiptTotal",
      constants.receiptTotal());

  public static BigDecimalFieldDef receiptTotal_tax = new BigDecimalFieldDef("receiptTotal.tax",
      constants.receiptTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef receiptTotal_total = new BigDecimalFieldDef(
      "receiptTotal.total", constants.receiptTotal_total(), false, null, true, null, true, 2);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0,
      128);

  public static StringFieldDef settlementId = new StringFieldDef("settlementId",
      constants.settlementId(), true, 0, 19);

  public static StringFieldDef settleNo = new StringFieldDef("settleNo", constants.settleNo(),
      false, 0, 20);

  public static StringFieldDef settlement = new StringFieldDef("settlement",
      constants.settlement(), false, 0, 20);

  public static EnumFieldDef settleState = new EnumFieldDef("settleState",
      SettleStateDef.constants.classCaption(), false, new String[][] {
          {
              "initial", SettleStateDef.constants.initial() }, {
              "partialSettled", SettleStateDef.constants.partialSettled() }, {
              "settled", SettleStateDef.constants.settled() } });

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

  public static OtherFieldDef version = new OtherFieldDef("version", constants.version(), false);

  public static DateFieldDef versionTime = new DateFieldDef("versionTime", constants.versionTime(),
      true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static EnumFieldDef type = new EnumFieldDef("type",
      StatementTypeDef.constants.classCaption(), false, new String[][] {
          {
              "normal", StatementTypeDef.constants.normal() }, {
              "patch", StatementTypeDef.constants.patch() } });

  public static DateFieldDef receiptAccDate = new DateFieldDef("receiptAccDate",
      constants.receiptAccDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

}
