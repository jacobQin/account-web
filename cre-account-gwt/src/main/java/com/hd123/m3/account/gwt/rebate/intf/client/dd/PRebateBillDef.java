/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	PRebateBillDef.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月1日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.rebate.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * 销售额返款单|字段定义
 * 
 * @author lixiaohong
 * @since 1.12
 *
 */
public class PRebateBillDef {
  public static CPRebateBill constants = (CPRebateBill) GWT.create(CPRebateBill.class);
  public static String TABLE_CAPTION = constants.tableCaption();

  public static StringFieldDef billNumber = new StringFieldDef("billNumber",
      constants.billNumber(), false, 0, 32);

  public static StringFieldDef permGroupId = new StringFieldDef("permGroupId",
      constants.permGroupId(), true, 0, 32);

  public static EmbeddedFieldDef createInfo = new EmbeddedFieldDef("createInfo",
      constants.createInfo());

  public static OtherFieldDef createInfo_operator = new OtherFieldDef("createInfo.operator",
      constants.createInfo_operator(), true);

  public static StringFieldDef createInfo_operator_fullName = new StringFieldDef(
      "createInfo.operator.fullName", constants.createInfo_operator_fullName(), true, 0, 32);

  public static StringFieldDef createInfo_operator_id = new StringFieldDef(
      "createInfo.operator.id", constants.createInfo_operator_id(), true, 0, 16);

  public static StringFieldDef createInfo_operator_namespace = new StringFieldDef(
      "createInfo.operator.namespace", constants.createInfo_operator_namespace(), true, 0, 16);

  public static DateFieldDef createInfo_time = new DateFieldDef("createInfo.time",
      constants.createInfo_time(), true, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static EmbeddedFieldDef lastModifyInfo = new EmbeddedFieldDef("lastModifyInfo",
      constants.lastModifyInfo());

  public static OtherFieldDef lastModifyInfo_operator = new OtherFieldDef(
      "lastModifyInfo.operator", constants.lastModifyInfo_operator(), true);

  public static StringFieldDef lastModifyInfo_operator_fullName = new StringFieldDef(
      "lastModifyInfo.operator.fullName", constants.lastModifyInfo_operator_fullName(), true, 0, 32);

  public static StringFieldDef lastModifyInfo_operator_id = new StringFieldDef(
      "lastModifyInfo.operator.id", constants.lastModifyInfo_operator_id(), true, 0, 16);

  public static StringFieldDef lastModifyInfo_operator_namespace = new StringFieldDef(
      "lastModifyInfo.operator.namespace", constants.lastModifyInfo_operator_namespace(), true, 0,
      16);

  public static DateFieldDef lastModifyInfo_time = new DateFieldDef("lastModifyInfo.time",
      constants.lastModifyInfo_time(), true, null, true, null, true, GWTFormat.fmt_yMd, false);
  
  public static EnumFieldDef bizState = new EnumFieldDef("bizState", constants.bizState(), false,
      new String[][] {
          {
              BBizStates.INEFFECT, BBizStates.getCaption(BBizStates.INEFFECT) }, {
              BBizStates.EFFECT, BBizStates.getCaption(BBizStates.EFFECT) }, {
              BBizStates.ABORTED, BBizStates.getCaption(BBizStates.ABORTED) } });

  public static EmbeddedFieldDef store = new EmbeddedFieldDef("store", constants.store());
  
  public static EmbeddedFieldDef tenant = new EmbeddedFieldDef("tenant", constants.tenant());

  public static EmbeddedFieldDef contract = new EmbeddedFieldDef("contract", constants.contract());

  public static StringFieldDef contract_name = new StringFieldDef("contract.name",
      constants.contract_name(), false, 0, 64);

  public static StringFieldDef positions = new StringFieldDef("positions", constants.positions(),
      false, 0, 100);
  
  public static StringFieldDef position = new StringFieldDef("position", constants.position(),
      false, 0, 100);

  public static DateFieldDef accountDate = new DateFieldDef("accountDate", constants.accountDate(),
      false, null, true, null, true, GWTFormat.fmt_yMd, false);
  
  public static DateFieldDef beginDate = new DateFieldDef("beginDate", constants.beginDate(),
      false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static DateFieldDef endDate = new DateFieldDef("endDate", constants.endDate(), false,
      null, true, null, true, GWTFormat.fmt_yMd, false);

  public static BigDecimalFieldDef backTotal = new BigDecimalFieldDef("backTotal",
      constants.backTotal(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef poundageTotal = new BigDecimalFieldDef("poundageTotal",
      constants.poundageTotal(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef shouldBackTotal = new BigDecimalFieldDef("shouldBackTotal",
      constants.shouldBackTotal(), false, null, true, null, true, 2);
  
  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 256);
}
