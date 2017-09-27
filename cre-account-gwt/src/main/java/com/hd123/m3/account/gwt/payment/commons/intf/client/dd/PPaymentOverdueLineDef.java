package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.mres.client.dd.tax.TaxTypeDef;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.IntFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.JoinFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;

public class PPaymentOverdueLineDef {

  public static CPPaymentOverdueLine constants = (CPPaymentOverdueLine) GWT.create(CPPaymentOverdueLine.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static StringFieldDef accountId = new StringFieldDef("accountId", constants.accountId(), true, 0, 32);

  public static EmbeddedFieldDef accountUnit = new EmbeddedFieldDef("accountUnit", constants.accountUnit());

  public static StringFieldDef accountUnit_code = new StringFieldDef("accountUnit.code", constants.accountUnit_code(), false, 0, 16);

  public static StringFieldDef accountUnit_name = new StringFieldDef("accountUnit.name", constants.accountUnit_name(), true, 0, 64);

  public static StringFieldDef accountUnit_uuid = new StringFieldDef("accountUnit.uuid", constants.accountUnit_uuid(), false, 0, 19);

  public static EmbeddedFieldDef businessUnit = new EmbeddedFieldDef("businessUnit", constants.businessUnit());

  public static StringFieldDef businessUnit_code = new StringFieldDef("businessUnit.code", constants.businessUnit_code(), true, 0, 16);

  public static StringFieldDef businessUnit_name = new StringFieldDef("businessUnit.name", constants.businessUnit_name(), true, 0, 64);

  public static StringFieldDef businessUnit_uuid = new StringFieldDef("businessUnit.uuid", constants.businessUnit_uuid(), true, 0, 19);

  public static EmbeddedFieldDef contract = new EmbeddedFieldDef("contract", constants.contract());

  public static StringFieldDef contract_billNumber = new StringFieldDef("contract.billNumber", constants.contract_billNumber(), false, 0, 32);

  public static StringFieldDef contract_billUuid = new StringFieldDef("contract.billUuid", constants.contract_billUuid(), false, 0, 32);

  public static StringFieldDef contract_name = new StringFieldDef("contract.name",constants.contract_name(), false, 0, 64);
  
  public static EmbeddedFieldDef counterpart = new EmbeddedFieldDef("counterpart", constants.counterpart());

  public static StringFieldDef counterpart_code = new StringFieldDef("counterpart.code", constants.counterpart_code(), false, 0, 16);

  public static StringFieldDef counterpart_name = new StringFieldDef("counterpart.name", constants.counterpart_name(), true, 0, 64);

  public static StringFieldDef counterpart_uuid = new StringFieldDef("counterpart.uuid", constants.counterpart_uuid(), false, 0, 19);

  public static BigDecimalFieldDef defrayalTotal = new BigDecimalFieldDef("defrayalTotal", constants.defrayalTotal(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef depositTotal = new BigDecimalFieldDef("depositTotal", constants.depositTotal(), false, null, true, null, true, 2);

  public static IntFieldDef lineNumber = new IntFieldDef("lineNumber", constants.lineNumber(), null, true, null, true);

  public static EmbeddedFieldDef overdueTotal = new EmbeddedFieldDef("overdueTotal", constants.overdueTotal());

  public static BigDecimalFieldDef overdueTotal_tax = new BigDecimalFieldDef("overdueTotal.tax", constants.overdueTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef overdueTotal_total = new BigDecimalFieldDef("overdueTotal.total", constants.overdueTotal_total(), false, null, true, null, true, 2);

  public static JoinFieldDef payment = new JoinFieldDef("payment", constants.payment(), false);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 128);

  public static EmbeddedFieldDef subject = new EmbeddedFieldDef("subject", constants.subject());

  public static StringFieldDef subject_code = new StringFieldDef("subject.code", constants.subject_code(), false, 0, 16);

  public static StringFieldDef subject_name = new StringFieldDef("subject.name", constants.subject_name(), true, 0, 64);

  public static StringFieldDef subject_uuid = new StringFieldDef("subject.uuid", constants.subject_uuid(), false, 0, 19);

  public static EmbeddedFieldDef taxRate = new EmbeddedFieldDef("taxRate", constants.taxRate());

  public static StringFieldDef taxRate_name = new StringFieldDef("taxRate.name", constants.taxRate_name(), true, 0, 16);

  public static BigDecimalFieldDef taxRate_rate = new BigDecimalFieldDef("taxRate.rate", constants.taxRate_rate(), true, null, true, null, true, 6);

  public static EnumFieldDef taxRate_taxType = new EnumFieldDef("taxRate.taxType", constants.taxRate_taxType(), false, new String[][] {{"excludePrice", TaxTypeDef.constants.excludePrice()},{"includePrice", TaxTypeDef.constants.includePrice()},});

  public static EmbeddedFieldDef total = new EmbeddedFieldDef("total", constants.total());

  public static BigDecimalFieldDef total_tax = new BigDecimalFieldDef("total.tax", constants.total_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef total_total = new BigDecimalFieldDef("total.total", constants.total_total(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef unpayedTotal = new EmbeddedFieldDef("unpayedTotal", constants.unpayedTotal());

  public static BigDecimalFieldDef unpayedTotal_tax = new BigDecimalFieldDef("unpayedTotal.tax", constants.unpayedTotal_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef unpayedTotal_total = new BigDecimalFieldDef("unpayedTotal.total", constants.unpayedTotal_total(), false, null, true, null, true, 2);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

  public static OtherFieldDef issueInvoice = new OtherFieldDef("issueInvoice", constants.issueInvoice(), false);

}
