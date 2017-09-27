package com.hd123.m3.account.gwt.contract.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.mres.client.dd.tax.TaxTypeDef;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.IntFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

public class POverdueTermDef {

  public static CPOverdueTerm constants = (CPOverdueTerm) GWT.create(CPOverdueTerm.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static DateFieldDef beginDate = new DateFieldDef("beginDate", constants.beginDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef caption = new StringFieldDef("caption", constants.caption(), true, 0, 64);

  public static StringFieldDef contractId = new StringFieldDef("contractId", constants.contractId(), false, 0, 32);

  public static IntFieldDef direction = new IntFieldDef("direction", constants.direction(), null, true, null, true);

  public static DateFieldDef endDate = new DateFieldDef("endDate", constants.endDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static DateFieldDef lastUpdateTime = new DateFieldDef("lastUpdateTime", constants.lastUpdateTime(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static EmbeddedFieldDef overdueSubject = new EmbeddedFieldDef("overdueSubject", constants.overdueSubject());

  public static StringFieldDef overdueSubject_code = new StringFieldDef("overdueSubject.code", constants.overdueSubject_code(), false, 0, 16);

  public static StringFieldDef overdueSubject_name = new StringFieldDef("overdueSubject.name", constants.overdueSubject_name(), true, 0, 64);

  public static StringFieldDef overdueSubject_uuid = new StringFieldDef("overdueSubject.uuid", constants.overdueSubject_uuid(), false, 0, 19);

  public static BigDecimalFieldDef rate = new BigDecimalFieldDef("rate", constants.rate(), false, null, true, null, true, 6);

  public static EmbeddedFieldDef taxRate = new EmbeddedFieldDef("taxRate", constants.taxRate());

  public static StringFieldDef taxRate_name = new StringFieldDef("taxRate.name", constants.taxRate_name(), true, 0, 16);

  public static BigDecimalFieldDef taxRate_rate = new BigDecimalFieldDef("taxRate.rate", constants.taxRate_rate(), true, null, true, null, true, 6);

  public static EnumFieldDef taxRate_taxType = new EnumFieldDef("taxRate.taxType", constants.taxRate_taxType(), true, new String[][] {{"excludePrice", TaxTypeDef.constants.excludePrice()},{"includePrice", TaxTypeDef.constants.includePrice()},});

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

  public static OtherFieldDef allSubjects = new OtherFieldDef("allSubjects", constants.allSubjects(), false);

  public static OtherFieldDef contractPeriod = new OtherFieldDef("contractPeriod", constants.contractPeriod(), false);

  public static OtherFieldDef invoice = new OtherFieldDef("invoice", constants.invoice(), false);

}
