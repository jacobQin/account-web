package com.hd123.m3.account.gwt.adv.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.IntFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

public class PAdvanceLogDef {

  public static CPAdvanceLog constants = (CPAdvanceLog) GWT.create(CPAdvanceLog.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static EmbeddedFieldDef accountUnit = new EmbeddedFieldDef("accountUnit", constants.accountUnit());

  public static StringFieldDef accountUnit_code = new StringFieldDef("accountUnit.code", constants.accountUnit_code(), false, 0, 16);

  public static StringFieldDef accountUnit_name = new StringFieldDef("accountUnit.name", constants.accountUnit_name(), true, 0, 64);

  public static StringFieldDef accountUnit_uuid = new StringFieldDef("accountUnit.uuid", constants.accountUnit_uuid(), false, 0, 19);

  public static BigDecimalFieldDef afterLockTotal = new BigDecimalFieldDef("afterLockTotal", constants.afterLockTotal(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef afterTotal = new BigDecimalFieldDef("afterTotal", constants.afterTotal(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef beforeLockTotal = new BigDecimalFieldDef("beforeLockTotal", constants.beforeLockTotal(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef beforeTotal = new BigDecimalFieldDef("beforeTotal", constants.beforeTotal(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef bill = new EmbeddedFieldDef("bill", constants.bill());

  public static StringFieldDef bill_billNumber = new StringFieldDef("bill.billNumber", constants.bill_billNumber(), false, 0, 16);

  public static StringFieldDef bill_billUuid = new StringFieldDef("bill.billUuid", constants.bill_billUuid(), false, 0, 32);

  public static EmbeddedFieldDef counterpart = new EmbeddedFieldDef("counterpart", constants.counterpart());

  public static StringFieldDef counterpart_code = new StringFieldDef("counterpart.code", constants.counterpart_code(), false, 0, 16);

  public static StringFieldDef counterpart_name = new StringFieldDef("counterpart.name", constants.counterpart_name(), true, 0, 64);

  public static StringFieldDef counterpart_uuid = new StringFieldDef("counterpart.uuid", constants.counterpart_uuid(), false, 0, 19);

  public static IntFieldDef direction = new IntFieldDef("direction", constants.direction(), null, true, null, true);

  public static BigDecimalFieldDef lockTotal = new BigDecimalFieldDef("lockTotal", constants.lockTotal(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef operateInfo = new EmbeddedFieldDef("operateInfo", constants.operateInfo());

  public static OtherFieldDef operateInfo_operator = new OtherFieldDef("operateInfo.operator", constants.operateInfo_operator(), true);

  public static StringFieldDef operateInfo_operator_fullName = new StringFieldDef("operateInfo.operator.fullName", constants.operateInfo_operator_fullName(), true, 0, 32);

  public static StringFieldDef operateInfo_operator_id = new StringFieldDef("operateInfo.operator.id", constants.operateInfo_operator_id(), true, 0, 16);

  public static StringFieldDef operateInfo_operator_namespace = new StringFieldDef("operateInfo.operator.namespace", constants.operateInfo_operator_namespace(), true, 0, 16);

  public static DateFieldDef operateInfo_time = new DateFieldDef("operateInfo.time", constants.operateInfo_time(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 128);

  public static EmbeddedFieldDef sourceBill = new EmbeddedFieldDef("sourceBill", constants.sourceBill());

  public static StringFieldDef sourceBill_billNumber = new StringFieldDef("sourceBill.billNumber", constants.sourceBill_billNumber(), true, 0, 16);

  public static StringFieldDef sourceBill_billType = new StringFieldDef("sourceBill.billType", constants.sourceBill_billType(), true, 0, 64);

  public static StringFieldDef sourceBill_billUuid = new StringFieldDef("sourceBill.billUuid", constants.sourceBill_billUuid(), true, 0, 32);

  public static EmbeddedFieldDef subject = new EmbeddedFieldDef("subject", constants.subject());

  public static StringFieldDef subject_code = new StringFieldDef("subject.code", constants.subject_code(), false, 0, 16);

  public static StringFieldDef subject_name = new StringFieldDef("subject.name", constants.subject_name(), true, 0, 64);

  public static StringFieldDef subject_uuid = new StringFieldDef("subject.uuid", constants.subject_uuid(), false, 0, 19);

  public static DateFieldDef time = new DateFieldDef("time", constants.time(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static BigDecimalFieldDef total = new BigDecimalFieldDef("total", constants.total(), false, null, true, null, true, 2);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
