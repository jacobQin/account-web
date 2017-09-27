package com.hd123.m3.account.gwt.subject.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.account.gwt.subject.intf.client.dd.CPSubjectUsage;
import com.hd123.rumba.gwt.util.client.fielddef.JoinFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;

public class PSubjectUsageDef {

  public static CPSubjectUsage constants = (CPSubjectUsage) GWT.create(CPSubjectUsage.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static JoinFieldDef subject = new JoinFieldDef("subject", constants.subject(), false);

  public static StringFieldDef usage = new StringFieldDef("usage", constants.usage(), false, 0, 10);

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

}
