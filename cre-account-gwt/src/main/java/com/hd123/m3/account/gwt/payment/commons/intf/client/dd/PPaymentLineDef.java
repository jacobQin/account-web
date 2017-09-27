package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPPaymentLine;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.IntFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.JoinFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;

public class PPaymentLineDef {

  public static CPPaymentLine constants = (CPPaymentLine) GWT.create(CPPaymentLine.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static BigDecimalFieldDef defrayalTotal = new BigDecimalFieldDef("defrayalTotal", constants.defrayalTotal(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef depositTotal = new BigDecimalFieldDef("depositTotal", constants.depositTotal(), false, null, true, null, true, 2);

  public static IntFieldDef lineNumber = new IntFieldDef("lineNumber", constants.lineNumber(), null, true, null, true);

  public static JoinFieldDef payment = new JoinFieldDef("payment", constants.payment(), false);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 128);

  public static EmbeddedFieldDef total = new EmbeddedFieldDef("total", constants.total());

  public static BigDecimalFieldDef total_tax = new BigDecimalFieldDef("total.tax", constants.total_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef total_total = new BigDecimalFieldDef("total.total", constants.total_total(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef unpayedTotal = new EmbeddedFieldDef("unpayedTotal", constants.unpayedTotal());

  public static BigDecimalFieldDef unpayedTotal_tax = new BigDecimalFieldDef("unpayedTotal.tax", constants.unpayedTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef unpayedTotal_total = new BigDecimalFieldDef("unpayedTotal.total", constants.unpayedTotal_total(), false, null, true, null, true, 2);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
