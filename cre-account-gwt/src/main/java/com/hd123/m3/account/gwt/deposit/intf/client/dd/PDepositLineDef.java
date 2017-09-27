package com.hd123.m3.account.gwt.deposit.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.IntFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.JoinFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;

public class PDepositLineDef {

  public static CPDepositLine constants = (CPDepositLine) GWT.create(CPDepositLine.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static BigDecimalFieldDef amount = new BigDecimalFieldDef("amount", constants.amount(), false, null, true, null, true, 2);

  public static JoinFieldDef deposit = new JoinFieldDef("deposit", constants.deposit(), false);

  public static IntFieldDef lineNumber = new IntFieldDef("lineNumber", constants.lineNumber(), null, true, null, true);

  public static BigDecimalFieldDef remainTotal = new BigDecimalFieldDef("remainTotal", constants.remainTotal(), true, null, true, null, true, 2);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 64);

  public static EmbeddedFieldDef subject = new EmbeddedFieldDef("subject", constants.subject());

  public static StringFieldDef subject_code = new StringFieldDef("subject.code", constants.subject_code(), false, 0, 16);

  public static StringFieldDef subject_name = new StringFieldDef("subject.name", constants.subject_name(), true, 0, 32);

  public static StringFieldDef subject_uuid = new StringFieldDef("subject.uuid", constants.subject_uuid(), false, 0, 19);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
