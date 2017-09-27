package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPPaymentDefrayal;
import com.hd123.rumba.gwt.util.client.fielddef.IntFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.JoinFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;

public class PPaymentDefrayalDef {

  public static CPPaymentDefrayal constants = (CPPaymentDefrayal) GWT.create(CPPaymentDefrayal.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static IntFieldDef lineNumber = new IntFieldDef("lineNumber", constants.lineNumber(), null, true, null, true);

  public static JoinFieldDef payment = new JoinFieldDef("payment", constants.payment(), false);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 128);

  public static BigDecimalFieldDef total = new BigDecimalFieldDef("total", constants.total(), false, null, true, null, true, 2);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
