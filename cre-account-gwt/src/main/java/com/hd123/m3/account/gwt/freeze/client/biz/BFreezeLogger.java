/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BFreezeLogger.java
 * 模块说明：	
 * 修改历史：
 * 2015-6-18 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.freeze.client.biz;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.hd123.m3.commons.gwt.base.client.biz.HasSummary;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.log.EntityLogger;

/**
 * @author liuguilin
 * 
 */
public class BFreezeLogger extends EntityLogger {

  public static BFreezeLogger instance;

  public static BFreezeLogger getInstance() {
    if (instance == null) {
      instance = new BFreezeLogger();
    }
    return instance;
  }

  @Override
  protected JSONValue toJson(HasSummary entity) {
    JSONValue value = super.toJson(entity);
    if (value instanceof JSONObject == false)
      return value;

    JSONObject object = (JSONObject) value;
    if (entity instanceof BFreeze) {
      BFreeze freeze = (BFreeze) entity;
      if (freeze.getAccountUnit() != null)
        object.put("accountUnit", new JSONString(freeze.getAccountUnit().toFriendlyStr()));
      if (freeze.getCounterpart() != null)
        object.put("counterpart", new JSONString(freeze.getCounterpart().toFriendlyStr()));
      if (freeze.getState() != null)
        object.put("state", new JSONString(freeze.getState()));
      if (freeze.getFreezePayTotal() != null)
        object.put("freezePayTotal",
            new JSONString(M3Format.fmt_money.format(freeze.getFreezePayTotal())));
      if (freeze.getFreezePayTax() != null)
        object.put("freezePayTax",
            new JSONString(M3Format.fmt_money.format(freeze.getFreezePayTax())));
      if (freeze.getFreezeRecTotal() != null)
        object.put("freezeRecTotal",
            new JSONString(M3Format.fmt_money.format(freeze.getFreezeRecTotal())));
      if (freeze.getFreezeRecTax() != null)
        object.put("freezeRecTax",
            new JSONString(M3Format.fmt_money.format(freeze.getFreezeRecTax())));
      if (freeze.getFreezeReason() != null)
        object.put("freezeReason", new JSONString(freeze.getFreezeReason()));
      if (freeze.getUnfreezeReason() != null)
        object.put("unfreezeReason", new JSONString(freeze.getUnfreezeReason()));
    }
    return object;
  }
}
