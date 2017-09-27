package com.hd123.m3.account.gwt.depositmove.intf.client.dd;

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

public class PDepositMoveDef {

  public static CPDepositMove constants = (CPDepositMove) GWT.create(CPDepositMove.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static EmbeddedFieldDef accountUnit = new EmbeddedFieldDef("accountUnit", constants.accountUnit());

  public static StringFieldDef accountUnit_code = new StringFieldDef("accountUnit.code", constants.accountUnit_code(), false, 0, 16);

  public static StringFieldDef accountUnit_name = new StringFieldDef("accountUnit.name", constants.accountUnit_name(), true, 0, 32);

  public static StringFieldDef accountUnit_uuid = new StringFieldDef("accountUnit.uuid", constants.accountUnit_uuid(), false, 0, 19);

  public static BigDecimalFieldDef amount = new BigDecimalFieldDef("amount", constants.amount(), false, null, true, null, true, 2);

  public static StringFieldDef billNumber = new StringFieldDef("billNumber", constants.billNumber(), false, 0, 32);

  public static EnumFieldDef bizState = new EnumFieldDef("bizState", constants.bizState(), false, new String[][] {{BBizStates.INEFFECT, BBizStates.getCaption(BBizStates.INEFFECT)},{BBizStates.EFFECT, BBizStates.getCaption(BBizStates.EFFECT)},{BBizStates.ABORTED, BBizStates.getCaption(BBizStates.ABORTED)}});

  public static EmbeddedFieldDef createInfo = new EmbeddedFieldDef("createInfo", constants.createInfo());

  public static OtherFieldDef createInfo_operator = new OtherFieldDef("createInfo.operator", constants.createInfo_operator(), true);

  public static StringFieldDef createInfo_operator_fullName = new StringFieldDef("createInfo.operator.fullName", constants.createInfo_operator_fullName(), true, 0, 32);

  public static StringFieldDef createInfo_operator_id = new StringFieldDef("createInfo.operator.id", constants.createInfo_operator_id(), true, 0, 16);

  public static StringFieldDef createInfo_operator_namespace = new StringFieldDef("createInfo.operator.namespace", constants.createInfo_operator_namespace(), true, 0, 16);

  public static DateFieldDef createInfo_time = new DateFieldDef("createInfo.time", constants.createInfo_time(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static IntFieldDef direction = new IntFieldDef("direction", constants.direction(), null, true, null, true);

  public static EmbeddedFieldDef inContract = new EmbeddedFieldDef("inContract", constants.inContract());

  public static StringFieldDef inContract_code = new StringFieldDef("inContract.code", constants.inContract_code(), true, 0, 32);

  public static StringFieldDef inContract_name = new StringFieldDef("inContract.name", constants.inContract_name(), true, 0, 64);
  
  public static StringFieldDef inContract_uuid = new StringFieldDef("inContract.uuid", constants.inContract_uuid(), true, 0, 19);

  public static EmbeddedFieldDef inCounterpart = new EmbeddedFieldDef("inCounterpart", constants.inCounterpart());

  public static StringFieldDef inCounterpart_code = new StringFieldDef("inCounterpart.code", constants.inCounterpart_code(), false, 0, 16);

  public static StringFieldDef inCounterpart_name = new StringFieldDef("inCounterpart.name", constants.inCounterpart_name(), true, 0, 32);

  public static StringFieldDef inCounterpart_uuid = new StringFieldDef("inCounterpart.uuid", constants.inCounterpart_uuid(), false, 0, 19);

  public static EmbeddedFieldDef inSubject = new EmbeddedFieldDef("inSubject", constants.inSubject());

  public static StringFieldDef inSubject_code = new StringFieldDef("inSubject.code", constants.inSubject_code(), false, 0, 16);

  public static StringFieldDef inSubject_name = new StringFieldDef("inSubject.name", constants.inSubject_name(), true, 0, 32);

  public static StringFieldDef inSubject_uuid = new StringFieldDef("inSubject.uuid", constants.inSubject_uuid(), false, 0, 19);

  public static EmbeddedFieldDef lastModifyInfo = new EmbeddedFieldDef("lastModifyInfo", constants.lastModifyInfo());

  public static OtherFieldDef lastModifyInfo_operator = new OtherFieldDef("lastModifyInfo.operator", constants.lastModifyInfo_operator(), true);

  public static StringFieldDef lastModifyInfo_operator_fullName = new StringFieldDef("lastModifyInfo.operator.fullName", constants.lastModifyInfo_operator_fullName(), true, 0, 32);

  public static StringFieldDef lastModifyInfo_operator_id = new StringFieldDef("lastModifyInfo.operator.id", constants.lastModifyInfo_operator_id(), true, 0, 16);

  public static StringFieldDef lastModifyInfo_operator_namespace = new StringFieldDef("lastModifyInfo.operator.namespace", constants.lastModifyInfo_operator_namespace(), true, 0, 16);

  public static DateFieldDef lastModifyInfo_time = new DateFieldDef("lastModifyInfo.time", constants.lastModifyInfo_time(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef latestComment = new StringFieldDef("latestComment", constants.latestComment(), true, 0, 128);

  public static EmbeddedFieldDef outContract = new EmbeddedFieldDef("outContract", constants.outContract());

  public static StringFieldDef outContract_code = new StringFieldDef("outContract.code", constants.outContract_code(), true, 0, 32);

  public static StringFieldDef outContract_name = new StringFieldDef("outContract.name", constants.outContract_name(), true, 0, 64);
  
  public static StringFieldDef outContract_uuid = new StringFieldDef("outContract.uuid", constants.outContract_uuid(), true, 0, 19);

  public static EmbeddedFieldDef outCounterpart = new EmbeddedFieldDef("outCounterpart", constants.outCounterpart());

  public static StringFieldDef outCounterpart_code = new StringFieldDef("outCounterpart.code", constants.outCounterpart_code(), false, 0, 16);

  public static StringFieldDef outCounterpart_name = new StringFieldDef("outCounterpart.name", constants.outCounterpart_name(), true, 0, 32);

  public static StringFieldDef outCounterpart_uuid = new StringFieldDef("outCounterpart.uuid", constants.outCounterpart_uuid(), false, 0, 19);

  public static EmbeddedFieldDef outSubject = new EmbeddedFieldDef("outSubject", constants.outSubject());

  public static StringFieldDef outSubject_code = new StringFieldDef("outSubject.code", constants.outSubject_code(), false, 0, 16);

  public static StringFieldDef outSubject_name = new StringFieldDef("outSubject.name", constants.outSubject_name(), true, 0, 32);

  public static StringFieldDef outSubject_uuid = new StringFieldDef("outSubject.uuid", constants.outSubject_uuid(), false, 0, 19);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 128);

  public static StringFieldDef settleNo = new StringFieldDef("settleNo", constants.settleNo(), true, 0, 20);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

  public static OtherFieldDef version = new OtherFieldDef("version", constants.version(), false);

  public static DateFieldDef versionTime = new DateFieldDef("versionTime", constants.versionTime(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static DateFieldDef accountDate = new DateFieldDef("accountDate", constants.accountDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);
}
