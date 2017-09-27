package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPPaymentCashDefrayal;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.IntFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.JoinFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;

public class PPaymentCashDefrayalDef {

  public static CPPaymentCashDefrayal constants = (CPPaymentCashDefrayal) GWT.create(CPPaymentCashDefrayal.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static StringFieldDef bankAccount = new StringFieldDef("bankAccount", constants.bankAccount(), true, 0, 50);

  public static StringFieldDef bankCode = new StringFieldDef("bankCode", constants.bankCode(), true, 0, 16);

  public static StringFieldDef bankName = new StringFieldDef("bankName", constants.bankName(), true, 0, 64);

  public static IntFieldDef lineNumber = new IntFieldDef("lineNumber", constants.lineNumber(), null, true, null, true);

  public static JoinFieldDef payment = new JoinFieldDef("payment", constants.payment(), false);

  public static EmbeddedFieldDef paymentType = new EmbeddedFieldDef("paymentType", constants.paymentType());

  public static StringFieldDef paymentType_code = new StringFieldDef("paymentType.code", constants.paymentType_code(), true, 0, 16);

  public static StringFieldDef paymentType_name = new StringFieldDef("paymentType.name", constants.paymentType_name(), true, 0, 64);

  public static StringFieldDef paymentType_uuid = new StringFieldDef("paymentType.uuid", constants.paymentType_uuid(), true, 0, 19);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 128);

  public static BigDecimalFieldDef total = new BigDecimalFieldDef("total", constants.total(), false, null, true, null, true, 2);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
