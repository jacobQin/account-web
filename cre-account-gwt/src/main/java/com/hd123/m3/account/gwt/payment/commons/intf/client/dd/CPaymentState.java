package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.i18n.client.Constants;

public interface CPaymentState extends Constants {

  public static String initial = "initial";
  public static String submitted = "submitted";
  public static String audited = "audited";
  public static String rejected = "rejected";
  public static String rechecked = "rechecked";
  public static String aborted = "aborted";

  public String classCaption();

  public String initial();

  public String submitted();

  public String audited();

  public String rejected();

  public String rechecked();

  public String aborted();

}
