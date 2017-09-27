package com.hd123.m3.account.gwt.fee.intf.client.dd;

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

public class PFeeDef {

  public static CPFee constants = (CPFee) GWT.create(CPFee.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static DateFieldDef accountDate = new DateFieldDef("accountDate", constants.accountDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static EmbeddedFieldDef accountUnit = new EmbeddedFieldDef("accountUnit", constants.accountUnit());

  public static StringFieldDef accountUnit_code = new StringFieldDef("accountUnit.code", constants.accountUnit_code(), false, 0, 16);

  public static StringFieldDef accountUnit_name = new StringFieldDef("accountUnit.name", constants.accountUnit_name(), true, 0, 64);

  public static StringFieldDef accountUnit_uuid = new StringFieldDef("accountUnit.uuid", constants.accountUnit_uuid(), false, 0, 19);

  public static StringFieldDef billNumber = new StringFieldDef("billNumber", constants.billNumber(), false, 0, 32);

  public static EnumFieldDef bizState = new EnumFieldDef("bizState", constants.bizState(), false, new String[][] {{BBizStates.INEFFECT, BBizStates.getCaption(BBizStates.INEFFECT)},{BBizStates.EFFECT, BBizStates.getCaption(BBizStates.EFFECT)},{BBizStates.ABORTED, BBizStates.getCaption(BBizStates.ABORTED)},});

  public static EmbeddedFieldDef businessUnit = new EmbeddedFieldDef("businessUnit", constants.businessUnit());

  public static StringFieldDef businessUnit_code = new StringFieldDef("businessUnit.code", constants.businessUnit_code(), false, 0, 16);

  public static StringFieldDef businessUnit_name = new StringFieldDef("businessUnit.name", constants.businessUnit_name(), true, 0, 64);

  public static StringFieldDef businessUnit_uuid = new StringFieldDef("businessUnit.uuid", constants.businessUnit_uuid(), false, 0, 19);

  public static EmbeddedFieldDef contract = new EmbeddedFieldDef("contract", constants.contract());

  public static StringFieldDef contract_code = new StringFieldDef("contract.code", constants.contract_code(), false, 0, 32);

  public static StringFieldDef contract_name = new StringFieldDef("contract.name", constants.contract_name(), false, 0, 64);
  
  public static StringFieldDef contract_uuid = new StringFieldDef("contract.uuid", constants.contract_uuid(), false, 0, 19);

  public static EmbeddedFieldDef counterpart = new EmbeddedFieldDef("counterpart", constants.counterpart());

  public static StringFieldDef counterpart_code = new StringFieldDef("counterpart.code", constants.counterpart_code(), false, 0, 16);

  public static StringFieldDef counterpart_name = new StringFieldDef("counterpart.name", constants.counterpart_name(), true, 0, 64);

  public static StringFieldDef counterpart_uuid = new StringFieldDef("counterpart.uuid", constants.counterpart_uuid(), false, 0, 19);

  public static EmbeddedFieldDef createInfo = new EmbeddedFieldDef("createInfo", constants.createInfo());

  public static OtherFieldDef createInfo_operator = new OtherFieldDef("createInfo.operator", constants.createInfo_operator(), true);

  public static StringFieldDef createInfo_operator_fullName = new StringFieldDef("createInfo.operator.fullName", constants.createInfo_operator_fullName(), true, 0, 32);

  public static StringFieldDef createInfo_operator_id = new StringFieldDef("createInfo.operator.id", constants.createInfo_operator_id(), true, 0, 16);

  public static StringFieldDef createInfo_operator_namespace = new StringFieldDef("createInfo.operator.namespace", constants.createInfo_operator_namespace(), true, 0, 16);

  public static DateFieldDef createInfo_time = new DateFieldDef("createInfo.time", constants.createInfo_time(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef dealer = new StringFieldDef("dealer", constants.dealer(), true, 0, 32);

  public static EmbeddedFieldDef dateRange = new EmbeddedFieldDef("dateRange", constants.dateRange());
  
  public static DateFieldDef dateRange_beginDate = new DateFieldDef("dateRange.beginDate", constants.dateRange_beginDate(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static DateFieldDef dateRange_endDate = new DateFieldDef("dateRange.endDate", constants.dateRange_endDate(), true, null, true, null, true, GWTFormat.fmt_yMd, false);
  
  public static IntFieldDef direction = new IntFieldDef("direction", constants.direction(), null, true, null, true);

  public static EmbeddedFieldDef lastModifyInfo = new EmbeddedFieldDef("lastModifyInfo", constants.lastModifyInfo());

  public static OtherFieldDef lastModifyInfo_operator = new OtherFieldDef("lastModifyInfo.operator", constants.lastModifyInfo_operator(), true);

  public static StringFieldDef lastModifyInfo_operator_fullName = new StringFieldDef("lastModifyInfo.operator.fullName", constants.lastModifyInfo_operator_fullName(), true, 0, 32);

  public static StringFieldDef lastModifyInfo_operator_id = new StringFieldDef("lastModifyInfo.operator.id", constants.lastModifyInfo_operator_id(), true, 0, 16);

  public static StringFieldDef lastModifyInfo_operator_namespace = new StringFieldDef("lastModifyInfo.operator.namespace", constants.lastModifyInfo_operator_namespace(), true, 0, 16);

  public static DateFieldDef lastModifyInfo_time = new DateFieldDef("lastModifyInfo.time", constants.lastModifyInfo_time(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef latestComment = new StringFieldDef("latestComment", constants.latestComment(), true, 0, 128);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 128);

  public static StringFieldDef settleNo = new StringFieldDef("settleNo", constants.settleNo(), true, 0, 20);
  
  public static StringFieldDef coopMode = new StringFieldDef("coopMode", constants.coopMode(),
      true, 0, 64);

  public static EnumFieldDef state = new EnumFieldDef("state", constants.state(), false, new String[][] {{"initial", FeeStateDef.constants.initial()},{"submitted", FeeStateDef.constants.submitted()},{"audited", FeeStateDef.constants.audited()},{"rejected", FeeStateDef.constants.rejected()},{"aborted", FeeStateDef.constants.aborted()},});

  public static EmbeddedFieldDef total = new EmbeddedFieldDef("total", constants.total());

  public static BigDecimalFieldDef total_tax = new BigDecimalFieldDef("total.tax", constants.total_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef total_total = new BigDecimalFieldDef("total.total", constants.total_total(), false, null, true, null, true, 2);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

  public static OtherFieldDef version = new OtherFieldDef("version", constants.version(), false);

  public static DateFieldDef versionTime = new DateFieldDef("versionTime", constants.versionTime(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static EnumFieldDef generateStatement = new EnumFieldDef("generateStatement", constants.bizState(), false, new String[][] {{Boolean.TRUE.toString(), "是"},{Boolean.FALSE.toString(), "否"}});
}
