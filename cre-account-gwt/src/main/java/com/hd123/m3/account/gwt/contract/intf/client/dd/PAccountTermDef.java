package com.hd123.m3.account.gwt.contract.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

public class PAccountTermDef {

  public static CPAccountTerm constants = (CPAccountTerm) GWT.create(CPAccountTerm.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static DateFieldDef beginDate = new DateFieldDef("beginDate", constants.beginDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef caption = new StringFieldDef("caption", constants.caption(), true, 0, 64);

  public static StringFieldDef contractId = new StringFieldDef("contractId", constants.contractId(), false, 0, 32);

  public static DateFieldDef endDate = new DateFieldDef("endDate", constants.endDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef formula = new StringFieldDef("formula", constants.formula(), true, 0, 2147483647);

  public static DateFieldDef lastUpdateTime = new DateFieldDef("lastUpdateTime", constants.lastUpdateTime(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

  public static OtherFieldDef contractPeriod = new OtherFieldDef("contractPeriod", constants.contractPeriod(), false);

  public static OtherFieldDef saleReturn = new OtherFieldDef("saleReturn", constants.saleReturn(), false);

}
