package com.hd123.m3.account.gwt.subject.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.mres.client.dd.tax.TaxTypeDef;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

public class PSubjectDef {

  public static CPSubject constants = (CPSubject) GWT.create(CPSubject.class);

  public static String TABLE_CAPTION = constants.tableCaption();

  public static StringFieldDef code = new StringFieldDef("code", constants.code(), false, 0, 32, "^[\\w\\.\\-\\+]+$", "只能包含字母、数字、\"_\"、\".\"、\"-\"、\"+\"。");

  public static EmbeddedFieldDef createInfo = new EmbeddedFieldDef("createInfo", constants.createInfo());

  public static OtherFieldDef createInfo_operator = new OtherFieldDef("createInfo.operator", constants.createInfo_operator(), true);

  public static StringFieldDef createInfo_operator_fullName = new StringFieldDef("createInfo.operator.fullName", constants.createInfo_operator_fullName(), true, 0, 32);

  public static StringFieldDef createInfo_operator_id = new StringFieldDef("createInfo.operator.id", constants.createInfo_operator_id(), true, 0, 16);

  public static StringFieldDef createInfo_operator_namespace = new StringFieldDef("createInfo.operator.namespace", constants.createInfo_operator_namespace(), true, 0, 16);

  public static DateFieldDef createInfo_time = new DateFieldDef("createInfo.time", constants.createInfo_time(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef direction = new StringFieldDef("direction", constants.direction());

  public static EmbeddedFieldDef lastModifyInfo = new EmbeddedFieldDef("lastModifyInfo", constants.lastModifyInfo());

  public static OtherFieldDef lastModifyInfo_operator = new OtherFieldDef("lastModifyInfo.operator", constants.lastModifyInfo_operator(), true);

  public static StringFieldDef lastModifyInfo_operator_fullName = new StringFieldDef("lastModifyInfo.operator.fullName", constants.lastModifyInfo_operator_fullName(), true, 0, 32);

  public static StringFieldDef lastModifyInfo_operator_id = new StringFieldDef("lastModifyInfo.operator.id", constants.lastModifyInfo_operator_id(), true, 0, 16);

  public static StringFieldDef lastModifyInfo_operator_namespace = new StringFieldDef("lastModifyInfo.operator.namespace", constants.lastModifyInfo_operator_namespace(), true, 0, 16);

  public static DateFieldDef lastModifyInfo_time = new DateFieldDef("lastModifyInfo.time", constants.lastModifyInfo_time(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static StringFieldDef name = new StringFieldDef("name", constants.name(), false, 0, 64);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 128);

  public static EmbeddedFieldDef taxRate = new EmbeddedFieldDef("taxRate", constants.taxRate());

  public static EmbeddedFieldDef customType = new EmbeddedFieldDef("customType", constants.customType());
  
  public static StringFieldDef taxRate_name = new StringFieldDef("taxRate.name", constants.taxRate_name(), true, 0, 16);

  public static BigDecimalFieldDef taxRate_rate = new BigDecimalFieldDef("taxRate.rate", constants.taxRate_rate(), true, null, true, null, true, 6);

  public static EnumFieldDef taxRate_taxType = new EnumFieldDef("taxRate.taxType", constants.taxRate_taxType(), true, new String[][] {{"excludePrice", TaxTypeDef.constants.excludePrice()},{"includePrice", TaxTypeDef.constants.includePrice()},});

  public static EnumFieldDef type = new EnumFieldDef("type", constants.type(), false, new String[][] {{"credit", SubjectTypeDef.constants.credit()},{"predeposit", SubjectTypeDef.constants.predeposit()},});

  public static StringFieldDef uuid = new StringFieldDef("uuid", constants.uuid(), false, 0, 19);

  public static StringFieldDef state = new StringFieldDef("state", constants.state());

  public static OtherFieldDef version = new OtherFieldDef("version", constants.version(), false);

  public static DateFieldDef versionTime = new DateFieldDef("versionTime", constants.versionTime(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static OtherFieldDef store = new OtherFieldDef("store", constants.store(),false);
  
  public static StringFieldDef ordinal = new StringFieldDef("state", constants.ordinal());
}
