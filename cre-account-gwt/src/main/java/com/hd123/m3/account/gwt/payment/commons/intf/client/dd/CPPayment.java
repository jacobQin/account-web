package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.i18n.client.Constants;

public interface CPPayment extends Constants {

  public String tableCaption();

  public String accountUnit();

  public String accountUnit_code();

  public String accountUnit_name();

  public String accountUnit_uuid();

  public String billNumber();

  public String bizState();

  public String counterpart();

  public String counterpart_code();

  public String counterpart_name();

  public String counterpart_uuid();

  public String createInfo();

  public String createInfo_operator();

  public String createInfo_operator_fullName();

  public String createInfo_operator_id();

  public String createInfo_operator_namespace();

  public String createInfo_time();

  public String dealer();

  public String defrayalTotal();

  public String defrayalType();

  public String depositSubject();

  public String depositSubject_code();

  public String depositSubject_name();

  public String depositSubject_uuid();

  public String depositTotal();

  public String direction();

  public String lastModifyInfo();

  public String lastModifyInfo_operator();

  public String lastModifyInfo_operator_fullName();

  public String lastModifyInfo_operator_id();

  public String lastModifyInfo_operator_namespace();

  public String lastModifyInfo_time();

  public String latestComment();

  public String overdueTotal();
  
  public String depositRemainTotal();

  public String overdueTotal_tax();

  public String overdueTotal_total();

  public String paymentDate();

  public String remark();

  public String settleNo();
  
  public String coopMode();

  public String state();

  public String total();

  public String total_tax();

  public String total_total();
  
  public String receiptTotal();

  public String receiptTotal_tax();

  public String receiptTotal_total();

  public String unpayedTotal();

  public String unpayedTotal_tax();

  public String unpayedTotal_total();

  public String uuid();

  public String version();

  public String versionTime();
  
  public String incomeDate();
  
}
