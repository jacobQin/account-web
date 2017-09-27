package com.hd123.m3.account.gwt.statement.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.IntFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.JoinFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

public class PStatementSubjectAccountDef {

  public static CPStatementSubjectAccount constants = (CPStatementSubjectAccount) GWT.create(CPStatementSubjectAccount.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static IntFieldDef direction = new IntFieldDef("direction", constants.direction(), null, true, null, true);

  public static EmbeddedFieldDef invoiceAdj = new EmbeddedFieldDef("invoiceAdj", constants.invoiceAdj());

  public static BigDecimalFieldDef invoiceAdj_tax = new BigDecimalFieldDef("invoiceAdj.tax", constants.invoiceAdj_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef invoiceAdj_total = new BigDecimalFieldDef("invoiceAdj.total", constants.invoiceAdj_total(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef invoiced = new EmbeddedFieldDef("invoiced", constants.invoiced());

  public static BigDecimalFieldDef invoiced_tax = new BigDecimalFieldDef("invoiced.tax", constants.invoiced_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef invoiced_total = new BigDecimalFieldDef("invoiced.total", constants.invoiced_total(), false, null, true, null, true, 2);

  public static DateFieldDef lastUpdTime = new DateFieldDef("lastUpdTime", constants.lastUpdTime(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static EmbeddedFieldDef needInvoice = new EmbeddedFieldDef("needInvoice", constants.needInvoice());

  public static BigDecimalFieldDef needInvoice_tax = new BigDecimalFieldDef("needInvoice.tax", constants.needInvoice_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef needInvoice_total = new BigDecimalFieldDef("needInvoice.total", constants.needInvoice_total(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef needSettle = new EmbeddedFieldDef("needSettle", constants.needSettle());

  public static BigDecimalFieldDef needSettle_tax = new BigDecimalFieldDef("needSettle.tax", constants.needSettle_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef needSettle_total = new BigDecimalFieldDef("needSettle.total", constants.needSettle_total(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef settleAdj = new EmbeddedFieldDef("settleAdj", constants.settleAdj());

  public static BigDecimalFieldDef settleAdj_tax = new BigDecimalFieldDef("settleAdj.tax", constants.settleAdj_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef settleAdj_total = new BigDecimalFieldDef("settleAdj.total", constants.settleAdj_total(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef settled = new EmbeddedFieldDef("settled", constants.settled());

  public static BigDecimalFieldDef settled_tax = new BigDecimalFieldDef("settled.tax", constants.settled_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef settled_total = new BigDecimalFieldDef("settled.total", constants.settled_total(), false, null, true, null, true, 2);

  public static JoinFieldDef statement = new JoinFieldDef("statement", constants.statement(), false);

  public static StringFieldDef subjectUuid = new StringFieldDef("subjectUuid", constants.subjectUuid(), false, 0, 19);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
