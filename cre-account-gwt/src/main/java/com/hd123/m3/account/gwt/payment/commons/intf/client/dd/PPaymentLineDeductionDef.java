package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPPaymentLineDeduction;
import com.hd123.rumba.gwt.util.client.fielddef.IntFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;

public class PPaymentLineDeductionDef {

  public static CPPaymentLineDeduction constants = (CPPaymentLineDeduction) GWT.create(CPPaymentLineDeduction.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static StringFieldDef accountId = new StringFieldDef("accountId", constants.accountId(), true, 0, 32);

  public static StringFieldDef bizId = new StringFieldDef("bizId", constants.bizId(), true, 0, 100);

  public static IntFieldDef itemNo = new IntFieldDef("itemNo", constants.itemNo(), null, true, null, true);

  public static StringFieldDef lineUuid = new StringFieldDef("lineUuid", constants.lineUuid(), false, 0, 19);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 128);

  public static BigDecimalFieldDef total = new BigDecimalFieldDef("total", constants.total(), false, null, true, null, true, 2);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
