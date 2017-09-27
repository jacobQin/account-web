package com.hd123.m3.account.gwt.statement.adjust.intf.client.dd;

import com.google.gwt.i18n.client.Constants;

public interface CPStatementAdjustLine extends Constants {

  public String tableCaption();

  public String accountId();

  public String adjust();

  public String amount();

  public String amount_tax();

  public String amount_total();

  public String dateRange();

  public String dateRange_beginDate();

  public String dateRange_endDate();

  public String direction();

  public String lineNumber();

  public String remark();

  public String sourceAccountId();

  public String subject();

  public String subject_code();

  public String subject_name();

  public String subject_uuid();

  public String taxRate();

  public String taxRate_name();

  public String taxRate_rate();

  public String taxRate_taxType();

  public String uuid();

  public String invoice();

  public String accountDate();
  
  public String lastPayDate();

}
