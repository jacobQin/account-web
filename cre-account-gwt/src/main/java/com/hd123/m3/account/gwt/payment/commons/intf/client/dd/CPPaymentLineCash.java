package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.i18n.client.Constants;

public interface CPPaymentLineCash extends Constants {

  public String tableCaption();

  public String bankAccount();

  public String bankCode();

  public String bankName();

  public String itemNo();

  public String lineUuid();

  public String paymentType();

  public String paymentType_code();

  public String paymentType_name();

  public String paymentType_uuid();

  public String remark();

  public String total();
  
  public String cash_total();

  public String uuid();

}
