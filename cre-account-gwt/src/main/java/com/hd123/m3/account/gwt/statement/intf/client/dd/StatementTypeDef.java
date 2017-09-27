package com.hd123.m3.account.gwt.statement.intf.client.dd;

import com.google.gwt.core.client.GWT;

public class StatementTypeDef {

  public static CStatementType constants = (CStatementType) GWT.create(CStatementType.class);

  public static String CLASS_CAPTION = constants.classCaption();
}
