package com.hd123.m3.account.gwt.contract.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.account.gwt.contract.intf.client.dd.CContractState;

public class ContractStateDef {

  public static CContractState constants = (CContractState) GWT.create(CContractState.class);

  public static String CLASS_CAPTION = constants.classCaption();

}
