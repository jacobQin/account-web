package com.hd123.m3.account.gwt.commons.client.dd;

import com.google.gwt.i18n.client.Constants;

public interface CContractState extends Constants {

  public static String using = "using";
  public static String finished = "finished";
  public static String canceled = "canceled";

  public String classCaption();

  public String using();

  public String finished();

  public String canceled();

}
