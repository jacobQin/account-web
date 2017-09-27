package com.hd123.m3.account.gwt.statement.intf.client.dd;

import com.google.gwt.i18n.client.Constants;

public interface CPStatementSubjectAccount extends Constants {

  public String tableCaption();

  public String direction();

  public String invoiceAdj();

  public String invoiceAdj_tax();

  public String invoiceAdj_total();

  public String invoiced();

  public String invoiced_tax();

  public String invoiced_total();

  public String lastUpdTime();

  public String needInvoice();

  public String needInvoice_tax();

  public String needInvoice_total();

  public String needSettle();

  public String needSettle_tax();

  public String needSettle_total();

  public String settleAdj();

  public String settleAdj_tax();

  public String settleAdj_total();

  public String settled();

  public String settled_tax();

  public String settled_total();

  public String statement();

  public String subjectUuid();

  public String uuid();

}
