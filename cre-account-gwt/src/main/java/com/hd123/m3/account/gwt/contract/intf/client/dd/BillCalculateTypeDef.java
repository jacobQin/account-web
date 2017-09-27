package com.hd123.m3.account.gwt.contract.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.account.gwt.contract.intf.client.dd.CBillCalculateType;

public class BillCalculateTypeDef {

  public static CBillCalculateType constants = (CBillCalculateType) GWT.create(CBillCalculateType.class);

  public static String CLASS_CAPTION = constants.classCaption();

}
