package com.hd123.m3.account.gwt.statement.intf.client.dd;

import com.google.gwt.i18n.client.Constants;

public interface CStatementType extends Constants {

  public static String normal = "normal";
  public static String patch = "patch";

  public String classCaption();

  public String normal();

  public String patch();
}
