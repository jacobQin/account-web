package com.hd123.m3.account.gwt.commons.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

public class PMonthSettleDef {

  public static CPMonthSettle constants = (CPMonthSettle) GWT.create(CPMonthSettle.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static DateFieldDef beginTime = new DateFieldDef("beginTime", constants.beginTime(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static DateFieldDef createTime = new DateFieldDef("createTime", constants.createTime(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static DateFieldDef endTime = new DateFieldDef("endTime", constants.endTime(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef number = new StringFieldDef("number", constants.number(), false, 0, 32);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
