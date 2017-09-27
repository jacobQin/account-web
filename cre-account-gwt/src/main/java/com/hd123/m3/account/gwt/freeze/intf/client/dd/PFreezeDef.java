package com.hd123.m3.account.gwt.freeze.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

public class PFreezeDef {

  public static CPFreeze constants = (CPFreeze) GWT.create(CPFreeze.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static EmbeddedFieldDef accountUnit = new EmbeddedFieldDef("accountUnit", constants.accountUnit());

  public static StringFieldDef accountUnit_code = new StringFieldDef("accountUnit.code", constants.accountUnit_code(), false, 0, 16);

  public static StringFieldDef accountUnit_name = new StringFieldDef("accountUnit.name", constants.accountUnit_name(), true, 0, 32);

  public static StringFieldDef accountUnit_uuid = new StringFieldDef("accountUnit.uuid", constants.accountUnit_uuid(), false, 0, 19);

  public static StringFieldDef billNumber = new StringFieldDef("billNumber", constants.billNumber(), false, 0, 32);

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

  public static EmbeddedFieldDef freezeInfo = new EmbeddedFieldDef("freezeInfo", constants.freezeInfo());

  public static OtherFieldDef freezeInfo_operator = new OtherFieldDef("freezeInfo.operator", constants.freezeInfo_operator(), true);

  public static StringFieldDef freezeInfo_operator_fullName = new StringFieldDef("freezeInfo.operator.fullName", constants.freezeInfo_operator_fullName(), true, 0, 32);

  public static StringFieldDef freezeInfo_operator_id = new StringFieldDef("freezeInfo.operator.id", constants.freezeInfo_operator_id(), true, 0, 16);

  public static StringFieldDef freezeInfo_operator_namespace = new StringFieldDef("freezeInfo.operator.namespace", constants.freezeInfo_operator_namespace(), true, 0, 16);

  public static DateFieldDef freezeInfo_time = new DateFieldDef("freezeInfo.time", constants.freezeInfo_time(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef freezeReason = new StringFieldDef("freezeReason", constants.freezeReason(), true, 0, 128);

  public static EmbeddedFieldDef lastModifyInfo = new EmbeddedFieldDef("lastModifyInfo", constants.lastModifyInfo());

  public static OtherFieldDef lastModifyInfo_operator = new OtherFieldDef("lastModifyInfo.operator", constants.lastModifyInfo_operator(), true);

  public static StringFieldDef lastModifyInfo_operator_fullName = new StringFieldDef("lastModifyInfo.operator.fullName", constants.lastModifyInfo_operator_fullName(), true, 0, 32);

  public static StringFieldDef lastModifyInfo_operator_id = new StringFieldDef("lastModifyInfo.operator.id", constants.lastModifyInfo_operator_id(), true, 0, 16);

  public static StringFieldDef lastModifyInfo_operator_namespace = new StringFieldDef("lastModifyInfo.operator.namespace", constants.lastModifyInfo_operator_namespace(), true, 0, 16);

  public static DateFieldDef lastModifyInfo_time = new DateFieldDef("lastModifyInfo.time", constants.lastModifyInfo_time(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static EmbeddedFieldDef paymentTotal = new EmbeddedFieldDef("paymentTotal", constants.paymentTotal());

  public static BigDecimalFieldDef paymentTotal_tax = new BigDecimalFieldDef("paymentTotal.tax", constants.paymentTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef paymentTotal_total = new BigDecimalFieldDef("paymentTotal.total", constants.paymentTotal_total(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef receiptTotal = new EmbeddedFieldDef("receiptTotal", constants.receiptTotal());

  public static BigDecimalFieldDef receiptTotal_tax = new BigDecimalFieldDef("receiptTotal.tax", constants.receiptTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef receiptTotal_total = new BigDecimalFieldDef("receiptTotal.total", constants.receiptTotal_total(), false, null, true, null, true, 2);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 128);

  public static StringFieldDef settleNo = new StringFieldDef("settleNo", constants.settleNo(), true, 0, 20);

  public static EnumFieldDef state = new EnumFieldDef("state", constants.state(), false, new String[][] {{"froze", FreezeStateDef.constants.froze()},{"unfroze", FreezeStateDef.constants.unfroze()},});

  public static EmbeddedFieldDef unfreezeInfo = new EmbeddedFieldDef("unfreezeInfo", constants.unfreezeInfo());

  public static OtherFieldDef unfreezeInfo_operator = new OtherFieldDef("unfreezeInfo.operator", constants.unfreezeInfo_operator(), true);

  public static StringFieldDef unfreezeInfo_operator_fullName = new StringFieldDef("unfreezeInfo.operator.fullName", constants.unfreezeInfo_operator_fullName(), true, 0, 32);

  public static StringFieldDef unfreezeInfo_operator_id = new StringFieldDef("unfreezeInfo.operator.id", constants.unfreezeInfo_operator_id(), true, 0, 16);

  public static StringFieldDef unfreezeInfo_operator_namespace = new StringFieldDef("unfreezeInfo.operator.namespace", constants.unfreezeInfo_operator_namespace(), true, 0, 16);

  public static DateFieldDef unfreezeInfo_time = new DateFieldDef("unfreezeInfo.time", constants.unfreezeInfo_time(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef unfreezeReason = new StringFieldDef("unfreezeReason", constants.unfreezeReason(), true, 0, 250);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

  public static OtherFieldDef version = new OtherFieldDef("version", constants.version(), false);

  public static DateFieldDef versionTime = new DateFieldDef("versionTime", constants.versionTime(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

}
