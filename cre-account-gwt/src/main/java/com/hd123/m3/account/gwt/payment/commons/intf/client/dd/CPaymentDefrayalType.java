package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.i18n.client.Constants;

public interface CPaymentDefrayalType extends Constants {

  public static String bill = "bill";
  public static String line = "line";   //多科目收款
  public static String lineSingle = "lineSingle";

  public String classCaption();

  public String bill();

  public String line();
  public String lineSingle();

}
