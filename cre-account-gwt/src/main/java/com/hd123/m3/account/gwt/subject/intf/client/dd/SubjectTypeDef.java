package com.hd123.m3.account.gwt.subject.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.account.gwt.subject.intf.client.dd.CSubjectType;

public class SubjectTypeDef {

  public static CSubjectType constants = (CSubjectType) GWT.create(CSubjectType.class);

  public static String CLASS_CAPTION = constants.classCaption();

}
