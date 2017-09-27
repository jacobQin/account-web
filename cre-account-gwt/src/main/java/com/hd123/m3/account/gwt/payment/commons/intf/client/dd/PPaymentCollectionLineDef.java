/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	PPaymentCollectionLineDef.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月14日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.core.client.GWT;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.IntFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * 代收明细数据字典
 * 
 * @author LiBin
 *
 */
public class PPaymentCollectionLineDef {

  public static CPPaymentCollectionLine constants = (CPPaymentCollectionLine) GWT.create(CPPaymentCollectionLine.class);
  
  public static String TABLE_CAPTION = constants.tableCaption();
  
  public static IntFieldDef lineNumber = new IntFieldDef("lineNumber", constants.lineNumber(), null, true, null, true);

  public static OtherFieldDef subject = new OtherFieldDef("subject", constants.subject(), false);
  
  public static OtherFieldDef contract = new OtherFieldDef("contract", constants.contract(), false);
  
  public static StringFieldDef contractCode = new StringFieldDef("contractCode", constants.contractCode(), true, 0, 16);
  
  public static StringFieldDef contractName = new StringFieldDef("contractName", constants.contractName(), true, 0, 32);

  public static DateFieldDef beginTime = new DateFieldDef("beginTime", constants.beginTime(), false, null, true, null, true, GWTFormat.fmt_yMd, false);

  public static DateFieldDef endTime = new DateFieldDef("endTime", constants.endTime(), false, null, true, null, true, GWTFormat.fmt_yMd, false);
  
  public static BigDecimalFieldDef receivableTotal = new BigDecimalFieldDef("receivableTotal", constants.receivableTotal(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef realTotal = new BigDecimalFieldDef("realTotal", constants.realTotal(), false, null, true, null, true, 2);

  public static BigDecimalFieldDef unreceivableTotal = new BigDecimalFieldDef("unreceivableTotal", constants.unreceivableTotal(), false, null, true, null, true, 2);

  public static StringFieldDef remark = new StringFieldDef("remark", constants.remark(), true, 0, 128);

  
}
