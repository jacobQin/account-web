package com.hd123.m3.account.gwt.fee.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.account.gwt.fee.intf.client.dd.CFeeState;

public class FeeStateDef {

  public static CFeeState constants = (CFeeState) GWT.create(CFeeState.class);

  public static String CLASS_CAPTION = constants.classCaption();

}
