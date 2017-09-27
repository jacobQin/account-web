package com.hd123.m3.account.gwt.internalfee.intf.client.dd;

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

public class PInternalFeeDef {

  public static CPInternalFee constants = (CPInternalFee) GWT.create(CPInternalFee.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static DateFieldDef accountDate = new DateFieldDef("accountDate", constants.accountDate(),
      false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static DateFieldDef beginDate = new DateFieldDef("beginDate", constants.beginDate(), true,
      null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef billNumber = new StringFieldDef("billNumber",
      constants.billNumber(), false, 0, 32);

  public static EnumFieldDef bizState = new EnumFieldDef("bizState", constants.bizState(), false,
      new String[][] {
          {
              BBizStates.INEFFECT, BBizStates.getCaption(BBizStates.INEFFECT) }, {
              BBizStates.EFFECT, BBizStates.getCaption(BBizStates.EFFECT) }, {
              BBizStates.ABORTED, BBizStates.getCaption(BBizStates.ABORTED) }, });

  public static StringFieldDef bpmInstance = new StringFieldDef("bpmInstance",
      constants.bpmInstance(), true, 0, 32);

  public static StringFieldDef bpmMessage = new StringFieldDef("bpmMessage",
      constants.bpmMessage(), true, 0, 128);

  public static StringFieldDef bpmState = new StringFieldDef("bpmState", constants.bpmState(),
      true, 0, 32);

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

  public static IntFieldDef direction = new IntFieldDef("direction", constants.direction(), null,
      true, null, true);

  public static DateFieldDef endDate = new DateFieldDef("endDate", constants.endDate(), true, null,
      true, null, true, GWTFormat.fmt_yMd, false);

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

  public static StringFieldDef permGroupId = new StringFieldDef("permGroupId",
      constants.permGroupId(), true, 0, 32);

  public static StringFieldDef permGroupTitle = new StringFieldDef("permGroupTitle",
      constants.permGroupTitle(), true, 0, 64);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0,
      128);

  public static StringFieldDef settleNo = new StringFieldDef("settleNo", constants.settleNo(),
      true, 0, 20);

  public static EmbeddedFieldDef store = new EmbeddedFieldDef("store", constants.store());

  public static StringFieldDef store_code = new StringFieldDef("store.code",
      constants.store_code(), false, 0, 16);

  public static StringFieldDef store_name = new StringFieldDef("store.name",
      constants.store_name(), true, 0, 64);

  public static StringFieldDef store_uuid = new StringFieldDef("store.uuid",
      constants.store_uuid(), false, 0, 19);

  public static EmbeddedFieldDef total = new EmbeddedFieldDef("total", constants.total());

  public static BigDecimalFieldDef total_tax = new BigDecimalFieldDef("total.tax",
      constants.total_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef total_total = new BigDecimalFieldDef("total.total",
      constants.total_total(), false, null, true, null, true, 2);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

  public static EmbeddedFieldDef vendor = new EmbeddedFieldDef("vendor", constants.vendor());

  public static StringFieldDef vendor_code = new StringFieldDef("vendor.code",
      constants.vendor_code(), false, 0, 16);

  public static StringFieldDef vendor_name = new StringFieldDef("vendor.name",
      constants.vendor_name(), true, 0, 64);

  public static StringFieldDef vendor_uuid = new StringFieldDef("vendor.uuid",
      constants.vendor_uuid(), false, 0, 19);

  public static OtherFieldDef version = new OtherFieldDef("version", constants.version(), false);

  public static DateFieldDef versionTime = new DateFieldDef("versionTime", constants.versionTime(),
      true, null, true, null, true, GWTFormat.fmt_yMd, false);

}
