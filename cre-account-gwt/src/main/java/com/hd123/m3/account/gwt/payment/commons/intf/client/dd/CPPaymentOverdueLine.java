package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.i18n.client.Constants;

public interface CPPaymentOverdueLine extends Constants {

  public String tableCaption();

  public String accountId();

  public String accountUnit();

  public String accountUnit_code();

  public String accountUnit_name();

  public String accountUnit_uuid();

  public String businessUnit();

  public String businessUnit_code();

  public String businessUnit_name();

  public String businessUnit_uuid();

  public String contract();

  public String contract_billNumber();

  public String contract_billUuid();
  
  public String contract_name();

  public String counterpart();

  public String counterpart_code();

  public String counterpart_name();

  public String counterpart_uuid();

  public String defrayalTotal();

  public String depositTotal();

  public String lineNumber();

  public String overdueTotal();

  public String overdueTotal_tax();

  public String overdueTotal_total();

  public String payment();

  public String remark();

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

  public String unpayedTotal();

  public String unpayedTotal_tax();

  public String unpayedTotal_total();

  public String uuid();

  public String issueInvoice();

}
