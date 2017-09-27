/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-basic
 * 文件名：	BPaymentTypeLogger.java
 * 模块说明：	
 * 修改历史：
 * 2015-6-17 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.paymenttype.client.biz;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.hd123.m3.commons.gwt.base.client.biz.HasSummary;
import com.hd123.m3.commons.gwt.base.client.log.EntityLogger;

/**
 * @author liuguilin
 * 
 */
public class BPaymentTypeLogger extends EntityLogger {

  public static BPaymentTypeLogger instance;

  public static BPaymentTypeLogger getInstance() {
    if (instance == null) {
      instance = new BPaymentTypeLogger();
    }
    return instance;
  }

  @Override
  protected JSONValue toJson(HasSummary entity) {
    JSONValue value = super.toJson(entity);
    if (value instanceof JSONObject == false)
      return value;

    JSONObject object = (JSONObject) value;

    if (entity instanceof BPaymentType) {
      BPaymentType paymentType = (BPaymentType) entity;
      if (paymentType.getCode() != null)
        object.put("code", new JSONString(paymentType.getCode()));
      if (paymentType.getName() != null)
        object.put("name", new JSONString(paymentType.getName()));
      object.put("enabled", new JSONString(String.valueOf(paymentType.isEnabled())));
      if (paymentType.getRemark() != null)
        object.put("remark", new JSONString(paymentType.getRemark()));
      return object;
    }
    return object;
  }
}
