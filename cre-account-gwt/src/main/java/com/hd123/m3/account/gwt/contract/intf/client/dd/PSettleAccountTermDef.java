package com.hd123.m3.account.gwt.contract.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.account.gwt.contract.intf.client.dd.CPSettleAccountTerm;
import com.hd123.rumba.gwt.util.client.fielddef.JoinFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;

public class PSettleAccountTermDef {

  public static CPSettleAccountTerm constants = (CPSettleAccountTerm) GWT.create(CPSettleAccountTerm.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static JoinFieldDef accountTerm = new JoinFieldDef("accountTerm", constants.accountTerm(), false);

  public static JoinFieldDef settlement = new JoinFieldDef("settlement", constants.settlement(), false);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
