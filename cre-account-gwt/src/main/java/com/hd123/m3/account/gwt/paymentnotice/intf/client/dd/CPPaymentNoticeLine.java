package com.hd123.m3.account.gwt.paymentnotice.intf.client.dd;

import com.google.gwt.i18n.client.Constants;

public interface CPPaymentNoticeLine extends Constants {

  public String tableCaption();

  public String businessUnit();

  public String businessUnit_code();

  public String businessUnit_name();

  public String businessUnit_uuid();

  public String contract();

  public String contract_code();

  public String contract_name();
  
  public String contract_uuid();

  public String lastReceiptDate();

  public String lineNumber();

  public String notice();

  public String paymentIvcTotal();

  public String paymentIvcTotal_tax();

  public String paymentIvcTotal_total();

  public String paymentTotal();

  public String paymentTotal_tax();

  public String paymentTotal_total();

  public String planPayDate();

  public String receiptIvcTotal();

  public String receiptIvcTotal_tax();

  public String receiptIvcTotal_total();

  public String receiptTotal();

  public String receiptTotal_tax();

  public String receiptTotal_total();

  public String remark();

  public String statement();

  public String statement_billNumber();

  public String statement_billUuid();

  public String uuid();

}
