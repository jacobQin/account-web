package com.hd123.m3.account.gwt.contract.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.account.gwt.contract.intf.client.dd.CPBizContract;
import com.hd123.rumba.gwt.util.client.fielddef.JoinFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;

public class PBizContractDef {

  public static CPBizContract constants = (CPBizContract) GWT.create(CPBizContract.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static StringFieldDef accountGroup = new StringFieldDef("accountGroup", constants.accountGroup(), false, 0, 19);

  public static JoinFieldDef contract = new JoinFieldDef("contract", constants.contract(), true);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
