package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPPaymentLineDeposit;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.IntFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;

public class PPaymentLineDepositDef {

  public static CPPaymentLineDeposit constants = (CPPaymentLineDeposit) GWT.create(CPPaymentLineDeposit.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static EmbeddedFieldDef contract = new EmbeddedFieldDef("contract", constants.contract());

  public static StringFieldDef contract_billNumber = new StringFieldDef("contract.billNumber", constants.contract_billNumber(), true, 0, 16);

  public static StringFieldDef contract_billUuid = new StringFieldDef("contract.billUuid", constants.contract_billUuid(), true, 0, 19);

  public static IntFieldDef itemNo = new IntFieldDef("itemNo", constants.itemNo(), null, true, null, true);

  public static StringFieldDef lineUuid = new StringFieldDef("lineUuid", constants.lineUuid(), false, 0, 19);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 128);

  public static EmbeddedFieldDef subject = new EmbeddedFieldDef("subject", constants.deposit_subject());

  public static StringFieldDef subject_code = new StringFieldDef("subject.code", constants.subject_code(), true, 0, 16);

  public static StringFieldDef subject_name = new StringFieldDef("subject.name", constants.subject_name(), true, 0, 64);

  public static StringFieldDef subject_uuid = new StringFieldDef("subject.uuid", constants.subject_uuid(), true, 0, 19);

  public static BigDecimalFieldDef total = new BigDecimalFieldDef("total", constants.total(), false, null, true, null, true, 2);
  
  public static BigDecimalFieldDef gendeposit = new BigDecimalFieldDef("gendeposit", "产生预存款", false, null, true, null, true, 2);
  
  public static BigDecimalFieldDef total_deposit = new BigDecimalFieldDef("deposit.total", constants.total_deposit(), false, null, true, null, true, 2);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
