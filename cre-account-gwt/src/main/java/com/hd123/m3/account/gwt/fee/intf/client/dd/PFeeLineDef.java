package com.hd123.m3.account.gwt.fee.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.mres.client.dd.tax.TaxTypeDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.IntFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.JoinFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;

public class PFeeLineDef {

  public static CPFeeLine constants = (CPFeeLine) GWT.create(CPFeeLine.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static StringFieldDef accountId = new StringFieldDef("accountId", constants.accountId(), true, 0, 32);

  public static EmbeddedFieldDef dateRange = new EmbeddedFieldDef("dateRange", constants.dateRange());

  public static JoinFieldDef fee = new JoinFieldDef("fee", constants.fee(), false);

  public static IntFieldDef lineNumber = new IntFieldDef("lineNumber", constants.lineNumber(), null, true, null, true);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 128);

  public static EmbeddedFieldDef subject = new EmbeddedFieldDef("subject", constants.subject());

  public static StringFieldDef subject_code = new StringFieldDef("subject.code", constants.subject_code(), false, 0, 16);

  public static StringFieldDef subject_name = new StringFieldDef("subject.name", constants.subject_name(), true, 0, 32);

  public static StringFieldDef subject_uuid = new StringFieldDef("subject.uuid", constants.subject_uuid(), false, 0, 19);

  public static EmbeddedFieldDef taxRate = new EmbeddedFieldDef("taxRate", constants.taxRate());

  public static StringFieldDef taxRate_name = new StringFieldDef("taxRate.name", constants.taxRate_name(), true, 0, 16);

  public static BigDecimalFieldDef taxRate_rate = new BigDecimalFieldDef("taxRate.rate", constants.taxRate_rate(), true, null, true, null, true, 6);

  public static EnumFieldDef taxRate_taxType = new EnumFieldDef("taxRate.taxType", constants.taxRate_taxType(), false, new String[][] {{"excludePrice", TaxTypeDef.constants.excludePrice()},{"includePrice", TaxTypeDef.constants.includePrice()},});

  public static EmbeddedFieldDef total = new EmbeddedFieldDef("total", constants.total());

  public static BigDecimalFieldDef total_tax = new BigDecimalFieldDef("total.tax", constants.total_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef total_total = new BigDecimalFieldDef("total.total", constants.total_total(), false, null, true, null, true, 2);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

  public static OtherFieldDef issueInvoice = new OtherFieldDef("issueInvoice", constants.issueInvoice(), false);

}
