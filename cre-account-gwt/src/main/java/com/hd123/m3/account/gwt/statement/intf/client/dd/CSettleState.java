package com.hd123.m3.account.gwt.statement.intf.client.dd;

import com.google.gwt.i18n.client.Constants;

public interface CSettleState extends Constants {

  public static String initial = "initial";
  public static String partialSettled = "partialSettled";
  public static String settled = "settled";

  public String classCaption();

  public String initial();

  public String partialSettled();

  public String settled();
}
