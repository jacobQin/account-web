package com.hd123.m3.account.gwt.paymentnotice.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

public class PPaymentNoticeDef {

  public static CPPaymentNotice constants = (CPPaymentNotice) GWT.create(CPPaymentNotice.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static EmbeddedFieldDef accountUnit = new EmbeddedFieldDef("accountUnit", constants.accountUnit());

  public static StringFieldDef accountUnit_code = new StringFieldDef("accountUnit.code", constants.accountUnit_code(), false, 0, 16);

  public static StringFieldDef accountUnit_name = new StringFieldDef("accountUnit.name", constants.accountUnit_name(), true, 0, 32);

  public static StringFieldDef accountUnit_uuid = new StringFieldDef("accountUnit.uuid", constants.accountUnit_uuid(), false, 0, 19);

  public static StringFieldDef billNumber = new StringFieldDef("billNumber", constants.billNumber(), false, 0, 32);

  public static EnumFieldDef bizState = new EnumFieldDef("bizState", constants.bizState(), false, new String[][] {{BBizStates.INEFFECT, BBizStates.getCaption(BBizStates.INEFFECT)},{BBizStates.EFFECT, BBizStates.getCaption(BBizStates.EFFECT)},{BBizStates.ABORTED, BBizStates.getCaption(BBizStates.ABORTED)},});

  public static EmbeddedFieldDef counterpart = new EmbeddedFieldDef("counterpart", constants.counterpart());

  public static StringFieldDef counterpart_code = new StringFieldDef("counterpart.code", constants.counterpart_code(), false, 0, 16);

  public static StringFieldDef counterpart_name = new StringFieldDef("counterpart.name", constants.counterpart_name(), true, 0, 32);

  public static StringFieldDef counterpart_uuid = new StringFieldDef("counterpart.uuid", constants.counterpart_uuid(), false, 0, 19);

  public static EmbeddedFieldDef createInfo = new EmbeddedFieldDef("createInfo", constants.createInfo());

  public static OtherFieldDef createInfo_operator = new OtherFieldDef("createInfo.operator", constants.createInfo_operator(), true);

  public static StringFieldDef createInfo_operator_fullName = new StringFieldDef("createInfo.operator.fullName", constants.createInfo_operator_fullName(), true, 0, 32);

  public static StringFieldDef createInfo_operator_id = new StringFieldDef("createInfo.operator.id", constants.createInfo_operator_id(), true, 0, 16);

  public static StringFieldDef createInfo_operator_namespace = new StringFieldDef("createInfo.operator.namespace", constants.createInfo_operator_namespace(), true, 0, 16);

  public static DateFieldDef createInfo_time = new DateFieldDef("createInfo.time", constants.createInfo_time(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static EmbeddedFieldDef lastModifyInfo = new EmbeddedFieldDef("lastModifyInfo", constants.lastModifyInfo());

  public static OtherFieldDef lastModifyInfo_operator = new OtherFieldDef("lastModifyInfo.operator", constants.lastModifyInfo_operator(), true);

  public static StringFieldDef lastModifyInfo_operator_fullName = new StringFieldDef("lastModifyInfo.operator.fullName", constants.lastModifyInfo_operator_fullName(), true, 0, 32);

  public static StringFieldDef lastModifyInfo_operator_id = new StringFieldDef("lastModifyInfo.operator.id", constants.lastModifyInfo_operator_id(), true, 0, 16);

  public static StringFieldDef lastModifyInfo_operator_namespace = new StringFieldDef("lastModifyInfo.operator.namespace", constants.lastModifyInfo_operator_namespace(), true, 0, 16);

  public static DateFieldDef lastModifyInfo_time = new DateFieldDef("lastModifyInfo.time", constants.lastModifyInfo_time(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef latestComment = new StringFieldDef("latestComment", constants.latestComment(), true, 0, 128);

  public static EmbeddedFieldDef paymentIvcTotal = new EmbeddedFieldDef("paymentIvcTotal", constants.paymentIvcTotal());

  public static BigDecimalFieldDef paymentIvcTotal_tax = new BigDecimalFieldDef("paymentIvcTotal.tax", constants.paymentIvcTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef paymentIvcTotal_total = new BigDecimalFieldDef("paymentIvcTotal.total", constants.paymentIvcTotal_total(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef paymentTotal = new EmbeddedFieldDef("paymentTotal", constants.paymentTotal());

  public static BigDecimalFieldDef paymentTotal_tax = new BigDecimalFieldDef("paymentTotal.tax", constants.paymentTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef paymentTotal_total = new BigDecimalFieldDef("paymentTotal.total", constants.paymentTotal_total(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef receiptIvcTotal = new EmbeddedFieldDef("receiptIvcTotal", constants.receiptIvcTotal());

  public static BigDecimalFieldDef receiptIvcTotal_tax = new BigDecimalFieldDef("receiptIvcTotal.tax", constants.receiptIvcTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef receiptIvcTotal_total = new BigDecimalFieldDef("receiptIvcTotal.total", constants.receiptIvcTotal_total(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef receiptTotal = new EmbeddedFieldDef("receiptTotal", constants.receiptTotal());

  public static BigDecimalFieldDef receiptTotal_tax = new BigDecimalFieldDef("receiptTotal.tax", constants.receiptTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef receiptTotal_total = new BigDecimalFieldDef("receiptTotal.total", constants.receiptTotal_total(), false, null, true, null, true, 2);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 128);

  public static StringFieldDef settleNo = new StringFieldDef("settleNo", constants.settleNo(), true, 0, 20);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

  public static OtherFieldDef version = new OtherFieldDef("version", constants.version(), false);

  public static DateFieldDef versionTime = new DateFieldDef("versionTime", constants.versionTime(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

}
