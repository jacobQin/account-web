package com.hd123.m3.account.gwt.paymentnotice.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.IntFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.JoinFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

public class PPaymentNoticeLineDef {

  public static CPPaymentNoticeLine constants = (CPPaymentNoticeLine) GWT.create(CPPaymentNoticeLine.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static EmbeddedFieldDef businessUnit = new EmbeddedFieldDef("businessUnit", constants.businessUnit());

  public static StringFieldDef businessUnit_code = new StringFieldDef("businessUnit.code", constants.businessUnit_code(), true, 0, 16);

  public static StringFieldDef businessUnit_name = new StringFieldDef("businessUnit.name", constants.businessUnit_name(), true, 0, 32);

  public static StringFieldDef businessUnit_uuid = new StringFieldDef("businessUnit.uuid", constants.businessUnit_uuid(), true, 0, 19);

  public static EmbeddedFieldDef contract = new EmbeddedFieldDef("contract", constants.contract());

  public static StringFieldDef contract_code = new StringFieldDef("contract.code", constants.contract_code(), false, 0, 32);

  public static StringFieldDef contract_name = new StringFieldDef("contract.name", constants.contract_name(), true, 0, 32);
  
  public static StringFieldDef contract_uuid = new StringFieldDef("contract.uuid", constants.contract_uuid(), false, 0, 19);

  public static DateFieldDef lastReceiptDate = new DateFieldDef("lastReceiptDate", constants.lastReceiptDate(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static IntFieldDef lineNumber = new IntFieldDef("lineNumber", constants.lineNumber(), null, true, null, true);

  public static JoinFieldDef notice = new JoinFieldDef("notice", constants.notice(), false);

  public static EmbeddedFieldDef paymentIvcTotal = new EmbeddedFieldDef("paymentIvcTotal", constants.paymentIvcTotal());

  public static BigDecimalFieldDef paymentIvcTotal_tax = new BigDecimalFieldDef("paymentIvcTotal.tax", constants.paymentIvcTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef paymentIvcTotal_total = new BigDecimalFieldDef("paymentIvcTotal.total", constants.paymentIvcTotal_total(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef paymentTotal = new EmbeddedFieldDef("paymentTotal", constants.paymentTotal());

  public static BigDecimalFieldDef paymentTotal_tax = new BigDecimalFieldDef("paymentTotal.tax", constants.paymentTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef paymentTotal_total = new BigDecimalFieldDef("paymentTotal.total", constants.paymentTotal_total(), false, null, true, null, true, 2);

  public static DateFieldDef planPayDate = new DateFieldDef("planPayDate", constants.planPayDate(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static EmbeddedFieldDef receiptIvcTotal = new EmbeddedFieldDef("receiptIvcTotal", constants.receiptIvcTotal());

  public static BigDecimalFieldDef receiptIvcTotal_tax = new BigDecimalFieldDef("receiptIvcTotal.tax", constants.receiptIvcTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef receiptIvcTotal_total = new BigDecimalFieldDef("receiptIvcTotal.total", constants.receiptIvcTotal_total(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef receiptTotal = new EmbeddedFieldDef("receiptTotal", constants.receiptTotal());

  public static BigDecimalFieldDef receiptTotal_tax = new BigDecimalFieldDef("receiptTotal.tax", constants.receiptTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef receiptTotal_total = new BigDecimalFieldDef("receiptTotal.total", constants.receiptTotal_total(), false, null, true, null, true, 2);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 64);

  public static EmbeddedFieldDef statement = new EmbeddedFieldDef("statement", constants.statement());

  public static StringFieldDef statement_billNumber = new StringFieldDef("statement.billNumber", constants.statement_billNumber(), false, 0, 32);

  public static StringFieldDef statement_billUuid = new StringFieldDef("statement.billUuid", constants.statement_billUuid(), false, 0, 19);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
