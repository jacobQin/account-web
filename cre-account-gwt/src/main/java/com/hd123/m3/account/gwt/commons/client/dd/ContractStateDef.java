package com.hd123.m3.account.gwt.commons.client.dd;

import com.google.gwt.core.client.GWT;

public class ContractStateDef {

  public static CContractState constants = (CContractState) GWT.create(CContractState.class);

  public static String CLASS_CAPTION = constants.classCaption();

}
