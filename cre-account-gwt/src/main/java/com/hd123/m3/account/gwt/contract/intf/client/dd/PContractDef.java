package com.hd123.m3.account.gwt.contract.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

public class PContractDef {

  public static CPContract constants = (CPContract) GWT.create(CPContract.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static BigDecimalFieldDef area = new BigDecimalFieldDef("area", constants.area(), false, null, true, null, true, 2);

  public static DateFieldDef beginDate = new DateFieldDef("beginDate", constants.beginDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef billNumber = new StringFieldDef("billNumber", constants.billNumber(), false, 0, 16);

  public static EmbeddedFieldDef businessUnit = new EmbeddedFieldDef("businessUnit", constants.businessUnit());

  public static StringFieldDef businessUnit_code = new StringFieldDef("businessUnit.code", constants.businessUnit_code(), true, 0, 16);

  public static StringFieldDef businessUnit_name = new StringFieldDef("businessUnit.name", constants.businessUnit_name(), true, 0, 64);

  public static StringFieldDef businessUnit_uuid = new StringFieldDef("businessUnit.uuid", constants.businessUnit_uuid(), true, 0, 19);

  public static EmbeddedFieldDef counterpart = new EmbeddedFieldDef("counterpart", constants.counterpart());

  public static StringFieldDef counterpart_code = new StringFieldDef("counterpart.code", constants.counterpart_code(), false, 0, 16);

  public static StringFieldDef counterpart_name = new StringFieldDef("counterpart.name", constants.counterpart_name(), true, 0, 64);

  public static StringFieldDef counterpart_uuid = new StringFieldDef("counterpart.uuid", constants.counterpart_uuid(), false, 0, 19);

  public static DateFieldDef endDate = new DateFieldDef("endDate", constants.endDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static DateFieldDef lastUpdateTime = new DateFieldDef("lastUpdateTime", constants.lastUpdateTime(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static EmbeddedFieldDef signUnit = new EmbeddedFieldDef("signUnit", constants.signUnit());

  public static StringFieldDef signUnit_code = new StringFieldDef("signUnit.code", constants.signUnit_code(), false, 0, 16);

  public static StringFieldDef signUnit_name = new StringFieldDef("signUnit.name", constants.signUnit_name(), true, 0, 64);

  public static StringFieldDef signUnit_uuid = new StringFieldDef("signUnit.uuid", constants.signUnit_uuid(), false, 0, 19);

  public static EnumFieldDef state = new EnumFieldDef("state", constants.state(), false, new String[][] {{"using", ContractStateDef.constants.using()},{"finished", ContractStateDef.constants.finished()},{"canceled", ContractStateDef.constants.canceled()},});

  public static StringFieldDef title = new StringFieldDef("title", constants.title(), true, 0, 64);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

  public static OtherFieldDef version = new OtherFieldDef("version", constants.version(), false);

}
