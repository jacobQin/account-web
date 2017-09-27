package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.core.client.GWT;

public class PaymentStateDef {

  public static CPaymentState constants = (CPaymentState) GWT.create(CPaymentState.class);

  public static String CLASS_CAPTION = constants.classCaption();

}
