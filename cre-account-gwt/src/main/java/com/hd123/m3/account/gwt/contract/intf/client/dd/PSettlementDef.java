package com.hd123.m3.account.gwt.contract.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

public class PSettlementDef {

  public static CPSettlement constants = (CPSettlement) GWT.create(CPSettlement.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static EmbeddedFieldDef accountUnit = new EmbeddedFieldDef("accountUnit", constants.accountUnit());

  public static StringFieldDef accountUnit_code = new StringFieldDef("accountUnit.code", constants.accountUnit_code(), false, 0, 16);

  public static StringFieldDef accountUnit_name = new StringFieldDef("accountUnit.name", constants.accountUnit_name(), true, 0, 64);

  public static StringFieldDef accountUnit_uuid = new StringFieldDef("accountUnit.uuid", constants.accountUnit_uuid(), false, 0, 19);

  public static DateFieldDef beginDate = new DateFieldDef("beginDate", constants.beginDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static EnumFieldDef billCalculateType = new EnumFieldDef("billCalculateType", constants.billCalculateType(), false, new String[][] {{"auto", BillCalculateTypeDef.constants.auto()},{"manul", BillCalculateTypeDef.constants.manul()},});

  public static StringFieldDef contractId = new StringFieldDef("contractId", constants.contractId(), false, 0, 32);

  public static StringFieldDef def = new StringFieldDef("def", constants.def(), true, 0, 2147483647);

  public static DateFieldDef endDate = new DateFieldDef("endDate", constants.endDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static DateFieldDef lastUpdateTime = new DateFieldDef("lastUpdateTime", constants.lastUpdateTime(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
