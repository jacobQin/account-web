package com.hd123.m3.account.gwt.subject.intf.client.dd;

import com.google.gwt.i18n.client.Constants;

public interface CSubjectType extends Constants {

  public static String credit = "credit";
  public static String predeposit = "predeposit";

  public String classCaption();

  public String credit();

  public String predeposit();

}
