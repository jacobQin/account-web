package com.hd123.m3.account.gwt.freeze.intf.client.dd;

import com.google.gwt.core.client.GWT;

public class FreezeStateDef {
  public static CFreezeState constants = (CFreezeState) GWT.create(CFreezeState.class);

  public static String CLASS_CAPTION = constants.classCaption();
}
