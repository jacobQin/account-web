package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.i18n.client.Constants;

public interface CPPaymentAccOverdue extends Constants {

  public String tableCaption();

  public String contract();

  public String contract_billNumber();

  public String contract_billUuid();

  public String direction();

  public String line();

  public String subject();

  public String subject_code();

  public String subject_name();

  public String subject_uuid();

  public String taxRate();

  public String taxRate_name();

  public String taxRate_rate();

  public String taxRate_taxType();

  public String total();

  public String total_tax();

  public String total_total();

  public String uuid();

  public String issueInvoice();

}
