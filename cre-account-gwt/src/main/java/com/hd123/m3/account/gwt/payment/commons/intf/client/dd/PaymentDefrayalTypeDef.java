package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.core.client.GWT;

public class PaymentDefrayalTypeDef {

  public static CPaymentDefrayalType constants = (CPaymentDefrayalType) GWT.create(CPaymentDefrayalType.class);

  public static String CLASS_CAPTION = constants.classCaption();

}
