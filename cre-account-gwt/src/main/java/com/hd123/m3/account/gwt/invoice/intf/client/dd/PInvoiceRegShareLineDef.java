package com.hd123.m3.account.gwt.invoice.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.mres.client.dd.tax.TaxTypeDef;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.IntFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.JoinFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

public class PInvoiceRegShareLineDef {

  public static CPInvoiceRegShareLine constants = (CPInvoiceRegShareLine) GWT.create(CPInvoiceRegShareLine.class);

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

  public static StringFieldDef acc1_contract_billNumber = new StringFieldDef("acc1.contract.billNumber", constants.acc1_contract_billNumber(), false, 0, 16);

  public static StringFieldDef acc1_contract_billUuid = new StringFieldDef("acc1.contract.billUuid", constants.acc1_contract_billUuid(), false, 0, 32);

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

  public static StringFieldDef acc1_sourceBill_billNumber = new StringFieldDef("acc1.sourceBill.billNumber", constants.acc1_sourceBill_billNumber(), false, 0, 16);

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

  public static EmbeddedFieldDef acc2 = new EmbeddedFieldDef("acc2", constants.acc2());

  public static IntFieldDef acc2_graceDays = new IntFieldDef("acc2.graceDays", constants.acc2_graceDays(), null, true, null, true);

  public static StringFieldDef acc2_id = new StringFieldDef("acc2.id", constants.acc2_id(), false, 0, 64);

  public static OtherFieldDef acc2_invoice = new OtherFieldDef("acc2.invoice", constants.acc2_invoice(), true);

  public static StringFieldDef acc2_invoice_billNumber = new StringFieldDef("acc2.invoice.billNumber", constants.acc2_invoice_billNumber(), false, 0, 16);

  public static StringFieldDef acc2_invoice_billUuid = new StringFieldDef("acc2.invoice.billUuid", constants.acc2_invoice_billUuid(), false, 0, 19);

  public static StringFieldDef acc2_invoice_invoiceCode = new StringFieldDef("acc2.invoice.invoiceCode", constants.acc2_invoice_invoiceCode(), false, 0, 16);

  public static StringFieldDef acc2_invoice_invoiceNumber = new StringFieldDef("acc2.invoice.invoiceNumber", constants.acc2_invoice_invoiceNumber(), false, 0, 16);

  public static DateFieldDef acc2_lastPayDate = new DateFieldDef("acc2.lastPayDate", constants.acc2_lastPayDate(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static OtherFieldDef acc2_locker = new OtherFieldDef("acc2.locker", constants.acc2_locker(), true);

  public static StringFieldDef acc2_locker_billNumber = new StringFieldDef("acc2.locker.billNumber", constants.acc2_locker_billNumber(), false, 0, 16);

  public static StringFieldDef acc2_locker_billType = new StringFieldDef("acc2.locker.billType", constants.acc2_locker_billType(), false, 0, 64);

  public static StringFieldDef acc2_locker_billUuid = new StringFieldDef("acc2.locker.billUuid", constants.acc2_locker_billUuid(), false, 0, 19);

  public static OtherFieldDef acc2_payment = new OtherFieldDef("acc2.payment", constants.acc2_payment(), true);

  public static StringFieldDef acc2_payment_billNumber = new StringFieldDef("acc2.payment.billNumber", constants.acc2_payment_billNumber(), false, 0, 16);

  public static StringFieldDef acc2_payment_billUuid = new StringFieldDef("acc2.payment.billUuid", constants.acc2_payment_billUuid(), false, 0, 19);

  public static OtherFieldDef acc2_statement = new OtherFieldDef("acc2.statement", constants.acc2_statement(), true);

  public static StringFieldDef acc2_statement_billNumber = new StringFieldDef("acc2.statement.billNumber", constants.acc2_statement_billNumber(), false, 0, 16);

  public static StringFieldDef acc2_statement_billUuid = new StringFieldDef("acc2.statement.billUuid", constants.acc2_statement_billUuid(), false, 0, 19);

  public static JoinFieldDef invoiceReg = new JoinFieldDef("invoiceReg", constants.invoiceReg(), false);

  public static EmbeddedFieldDef total = new EmbeddedFieldDef("total", constants.total());

  public static BigDecimalFieldDef total_tax = new BigDecimalFieldDef("total.tax", constants.total_tax(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef total_total = new BigDecimalFieldDef("total.total", constants.total_total(), false, null, true, null, true, 2);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
