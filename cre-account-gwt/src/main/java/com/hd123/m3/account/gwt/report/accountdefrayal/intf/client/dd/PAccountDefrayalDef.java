package com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.dd;

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

public class PAccountDefrayalDef {

  public static CPAccountDefrayal constants = (CPAccountDefrayal) GWT.create(CPAccountDefrayal.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static EmbeddedFieldDef acc1 = new EmbeddedFieldDef("acc1", constants.acc1());

  public static DateFieldDef acc1_accountDate = new DateFieldDef("acc1.accountDate", constants.acc1_accountDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static OtherFieldDef acc1_accountUnit = new OtherFieldDef("acc1.accountUnit", constants.acc1_accountUnit(), true);

  public static StringFieldDef acc1_accountUnit_code = new StringFieldDef("acc1.accountUnit.code", constants.acc1_accountUnit_code(), false, 0, 16);

  public static StringFieldDef acc1_accountUnit_name = new StringFieldDef("acc1.accountUnit.name", constants.acc1_accountUnit_name(), true, 0, 64);

  public static StringFieldDef acc1_accountUnit_uuid = new StringFieldDef("acc1.accountUnit.uuid", constants.acc1_accountUnit_uuid(), false, 0, 19);

  public static DateFieldDef acc1_beginTime = new DateFieldDef("acc1.beginTime", constants.acc1_beginTime(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static OtherFieldDef acc1_business = new OtherFieldDef("acc1.business", constants.acc1_business(), true);

  public static StringFieldDef acc1_business_code = new StringFieldDef("acc1.business.code", constants.acc1_business_code(), true, 0, 16);

  public static StringFieldDef acc1_business_name = new StringFieldDef("acc1.business.name", constants.acc1_business_name(), true, 0, 64);

  public static StringFieldDef acc1_business_uuid = new StringFieldDef("acc1.business.uuid", constants.acc1_business_uuid(), true, 0, 19);

  public static OtherFieldDef acc1_contract = new OtherFieldDef("acc1.contract", constants.acc1_contract(), true);

  public static StringFieldDef acc1_contract_code = new StringFieldDef("acc1.contract.code", constants.acc1_contract_code(), false, 0, 32);

  public static StringFieldDef acc1_contract_name = new StringFieldDef("acc1.contract.name", constants.acc1_contract_name(), false, 0, 64);
  
  public static StringFieldDef acc1_contract_uuid = new StringFieldDef("acc1.contract.uuid", constants.acc1_contract_uuid(), false, 0, 32);

  public static OtherFieldDef acc1_counterpart = new OtherFieldDef("acc1.counterpart", constants.acc1_counterpart(), true);

  public static StringFieldDef acc1_counterpart_code = new StringFieldDef("acc1.counterpart.code", constants.acc1_counterpart_code(), false, 0, 16);

  public static StringFieldDef acc1_counterpart_name = new StringFieldDef("acc1.counterpart.name", constants.acc1_counterpart_name(), true, 0, 64);

  public static StringFieldDef acc1_counterpart_uuid = new StringFieldDef("acc1.counterpart.uuid", constants.acc1_counterpart_uuid(), false, 0, 19);

  public static IntFieldDef acc1_direction = new IntFieldDef("acc1.direction", constants.acc1_direction(), null, true, null, true);

  public static DateFieldDef acc1_endTime = new DateFieldDef("acc1.endTime", constants.acc1_endTime(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef acc1_id = new StringFieldDef("acc1.id", constants.acc1_id(), false, 0, 32);

  public static DateFieldDef acc1_ocrTime = new DateFieldDef("acc1.ocrTime", constants.acc1_ocrTime(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef acc1_remark = new StringFieldDef("acc1.remark", constants.acc1_remark(), true, 0, 128);

  public static OtherFieldDef acc1_sourceBill = new OtherFieldDef("acc1.sourceBill", constants.acc1_sourceBill(), true);

  public static StringFieldDef acc1_sourceBill_billNumber = new StringFieldDef("acc1.sourceBill.billNumber", constants.acc1_sourceBill_billNumber(), false, 0, 32);

  public static StringFieldDef acc1_sourceBill_billType = new StringFieldDef("acc1.sourceBill.billType", constants.acc1_sourceBill_billType(), false, 0, 64);

  public static StringFieldDef acc1_sourceBill_billUuid = new StringFieldDef("acc1.sourceBill.billUuid", constants.acc1_sourceBill_billUuid(), false, 0, 19);

  public static OtherFieldDef acc1_subject = new OtherFieldDef("acc1.subject", constants.acc1_subject(), true);

  public static StringFieldDef acc1_subject_code = new StringFieldDef("acc1.subject.code", constants.acc1_subject_code(), false, 0, 16);

  public static StringFieldDef acc1_subject_name = new StringFieldDef("acc1.subject.name", constants.acc1_subject_name(), true, 0, 64);

  public static StringFieldDef acc1_subject_uuid = new StringFieldDef("acc1.subject.uuid", constants.acc1_subject_uuid(), false, 0, 19);

  public static OtherFieldDef acc1_taxRate = new OtherFieldDef("acc1.taxRate", constants.acc1_taxRate(), true);

  public static StringFieldDef acc1_taxRate_name = new StringFieldDef("acc1.taxRate.name", constants.acc1_taxRate_name(), true, 0, 16);

  public static BigDecimalFieldDef acc1_taxRate_rate = new BigDecimalFieldDef("acc1.taxRate.rate", constants.acc1_taxRate_rate(), true, null, true, null, true, 6);

  public static EnumFieldDef acc1_taxRate_taxType = new EnumFieldDef("acc1.taxRate.taxType", constants.acc1_taxRate_taxType(), true, new String[][] {{"excludePrice", TaxTypeDef.constants.excludePrice()},{"includePrice", TaxTypeDef.constants.includePrice()},});

  public static EmbeddedFieldDef invoiceAdj = new EmbeddedFieldDef("invoiceAdj", constants.invoiceAdj());

  public static BigDecimalFieldDef invoiceAdj_tax = new BigDecimalFieldDef("invoiceAdj.tax", constants.invoiceAdj_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef invoiceAdj_total = new BigDecimalFieldDef("invoiceAdj.total", constants.invoiceAdj_total(), false, null, true, null, true, 2);

  public static EmbeddedFieldDef invoiced = new EmbeddedFieldDef("invoiced", constants.invoiced());

  public static BigDecimalFieldDef invoiced_tax = new BigDecimalFieldDef("invoiced.tax", constants.invoiced_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef invoiced_total = new BigDecimalFieldDef("invoiced.total", constants.invoiced_total(), false, null, true, null, true, 2);

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

  public static EmbeddedFieldDef statement = new EmbeddedFieldDef("statement", constants.statement());

  public static StringFieldDef statement_billNumber = new StringFieldDef("statement.billNumber", constants.statement_billNumber(), false, 0, 32);

  public static StringFieldDef statement_billUuid = new StringFieldDef("statement.billUuid", constants.statement_billUuid(), false, 0, 19);

  public static EmbeddedFieldDef total = new EmbeddedFieldDef("total", constants.total());

  public static BigDecimalFieldDef total_tax = new BigDecimalFieldDef("total.tax", constants.total_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef total_total = new BigDecimalFieldDef("total.total", constants.total_total(), false, null, true, null, true, 2);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
