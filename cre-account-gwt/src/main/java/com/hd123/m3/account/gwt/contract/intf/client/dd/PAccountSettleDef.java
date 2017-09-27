package com.hd123.m3.account.gwt.contract.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.JoinFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

public class PAccountSettleDef {

  public static CPAccountSettle constants = (CPAccountSettle) GWT.create(CPAccountSettle.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static DateFieldDef accountTime = new DateFieldDef("accountTime", constants.accountTime(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static DateFieldDef beginDate = new DateFieldDef("beginDate", constants.beginDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static EmbeddedFieldDef bill = new EmbeddedFieldDef("bill", constants.bill());

  public static StringFieldDef bill_billNumber = new StringFieldDef("bill.billNumber", constants.bill_billNumber(), false, 0, 16);

  public static StringFieldDef bill_billUuid = new StringFieldDef("bill.billUuid", constants.bill_billUuid(), false, 0, 32);

  public static JoinFieldDef contract = new JoinFieldDef("contract", constants.contract(), false);

  public static DateFieldDef endDate = new DateFieldDef("endDate", constants.endDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static DateFieldDef planDate = new DateFieldDef("planDate", constants.planDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static JoinFieldDef settlement = new JoinFieldDef("settlement", constants.settlement(), false);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
