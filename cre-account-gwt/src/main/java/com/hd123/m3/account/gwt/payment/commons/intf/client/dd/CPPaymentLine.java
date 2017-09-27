package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.i18n.client.Constants;

public interface CPPaymentLine extends Constants {

  public String tableCaption();

  public String defrayalTotal();

  public String depositTotal();

  public String lineNumber();

  public String payment();

  public String remark();

  public String total();

  public String total_tax();

  public String total_total();

  public String unpayedTotal();

  public String unpayedTotal_tax();

  public String unpayedTotal_total();

  public String uuid();

}
