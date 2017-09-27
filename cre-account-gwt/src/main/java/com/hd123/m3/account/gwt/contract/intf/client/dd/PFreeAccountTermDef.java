package com.hd123.m3.account.gwt.contract.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.IntFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

public class PFreeAccountTermDef {

  public static CPFreeAccountTerm constants = (CPFreeAccountTerm) GWT.create(CPFreeAccountTerm.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static DateFieldDef beginDate = new DateFieldDef("beginDate", constants.beginDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef contractId = new StringFieldDef("contractId", constants.contractId(), false, 0, 32);

  public static IntFieldDef days = new IntFieldDef("days", constants.days(), null, true, null, true);

  public static DateFieldDef endDate = new DateFieldDef("endDate", constants.endDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static DateFieldDef lastUpdateTime = new DateFieldDef("lastUpdateTime", constants.lastUpdateTime(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static IntFieldDef months = new IntFieldDef("months", constants.months(), null, true, null, true);

  public static EmbeddedFieldDef subject = new EmbeddedFieldDef("subject", constants.subject());

  public static StringFieldDef subject_code = new StringFieldDef("subject.code", constants.subject_code(), false, 0, 16);

  public static StringFieldDef subject_name = new StringFieldDef("subject.name", constants.subject_name(), true, 0, 64);

  public static StringFieldDef subject_uuid = new StringFieldDef("subject.uuid", constants.subject_uuid(), false, 0, 19);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
