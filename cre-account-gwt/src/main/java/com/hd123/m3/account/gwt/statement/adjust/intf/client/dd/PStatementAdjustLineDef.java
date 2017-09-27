package com.hd123.m3.account.gwt.statement.adjust.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.mres.client.dd.tax.TaxTypeDef;
import com.hd123.rumba.gwt.util.client.fielddef.BooleanFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.IntFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.JoinFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

public class PStatementAdjustLineDef {

  public static CPStatementAdjustLine constants = (CPStatementAdjustLine) GWT.create(CPStatementAdjustLine.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static StringFieldDef accountId = new StringFieldDef("accountId", constants.accountId(), true, 0, 32);

  public static JoinFieldDef adjust = new JoinFieldDef("adjust", constants.adjust(), false);

  public static EmbeddedFieldDef amount = new EmbeddedFieldDef("amount", constants.amount());

  public static BigDecimalFieldDef amount_tax = new BigDecimalFieldDef("amount.tax", constants.amount_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef amount_total = new BigDecimalFieldDef("amount.total", constants.amount_total(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef dateRange = new EmbeddedFieldDef("dateRange", constants.dateRange());

  public static DateFieldDef dateRange_beginDate = new DateFieldDef("dateRange.beginDate", constants.dateRange_beginDate(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static DateFieldDef dateRange_endDate = new DateFieldDef("dateRange.endDate", constants.dateRange_endDate(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static IntFieldDef direction = new IntFieldDef("direction", constants.direction(), null, true, null, true);

  public static IntFieldDef lineNumber = new IntFieldDef("lineNumber", constants.lineNumber(), null, true, null, true);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 128);

  public static StringFieldDef sourceAccountId = new StringFieldDef("sourceAccountId", constants.sourceAccountId(), true, 0, 32);

  public static EmbeddedFieldDef subject = new EmbeddedFieldDef("subject", constants.subject());

  public static StringFieldDef subject_code = new StringFieldDef("subject.code", constants.subject_code(), false, 0, 16);

  public static StringFieldDef subject_name = new StringFieldDef("subject.name", constants.subject_name(), true, 0, 32);

  public static StringFieldDef subject_uuid = new StringFieldDef("subject.uuid", constants.subject_uuid(), false, 0, 19);

  public static EmbeddedFieldDef taxRate = new EmbeddedFieldDef("taxRate", constants.taxRate());

  public static StringFieldDef taxRate_name = new StringFieldDef("taxRate.name", constants.taxRate_name(), true, 0, 16);

  public static BigDecimalFieldDef taxRate_rate = new BigDecimalFieldDef("taxRate.rate", constants.taxRate_rate(), true, null, true, null, true, 6);

  public static EnumFieldDef taxRate_taxType = new EnumFieldDef("taxRate.taxType", constants.taxRate_taxType(), false, new String[][] {{"excludePrice", TaxTypeDef.constants.excludePrice()},{"includePrice", TaxTypeDef.constants.includePrice()},});

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

  public static BooleanFieldDef invoice = new BooleanFieldDef("invoice", constants.invoice(), false);
  
  public static DateFieldDef accountDate = new DateFieldDef("accountDate", constants.accountDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);
  
  public static DateFieldDef lastPayDate = new DateFieldDef("lastPayDate", constants.lastPayDate(), true, null, true, null, true, GWTFormat.fmt_yMd, false);
  
  
}
