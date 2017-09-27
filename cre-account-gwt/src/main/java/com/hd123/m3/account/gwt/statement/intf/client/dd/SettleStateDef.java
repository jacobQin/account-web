package com.hd123.m3.account.gwt.statement.intf.client.dd;

import com.google.gwt.core.client.GWT;

public class SettleStateDef {

  public static CSettleState constants = (CSettleState) GWT.create(CSettleState.class);

  public static String CLASS_CAPTION = constants.classCaption();

}
