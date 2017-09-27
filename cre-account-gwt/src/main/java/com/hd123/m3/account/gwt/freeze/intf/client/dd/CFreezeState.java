package com.hd123.m3.account.gwt.freeze.intf.client.dd;

import com.google.gwt.i18n.client.Constants;

public interface CFreezeState extends Constants {
  public static String froze = "froze";
  public static String unfroze = "unfroze";

  public String classCaption();

  public String froze();

  public String unfroze();
}
