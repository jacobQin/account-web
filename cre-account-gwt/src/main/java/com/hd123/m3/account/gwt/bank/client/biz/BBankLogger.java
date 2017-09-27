/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-basic
 * 文件名：	BBankLogger.java
 * 模块说明：	
 * 修改历史：
 * 2015-6-17 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.bank.client.biz;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.hd123.m3.commons.gwt.base.client.biz.HasSummary;
import com.hd123.m3.commons.gwt.base.client.log.EntityLogger;

/**
 * @author huangjunxian
 * 
 */
public class BBankLogger extends EntityLogger {
  public static BBankLogger instance;

  public static BBankLogger getInstance() {
    if (instance == null)
      instance = new BBankLogger();
    return instance;
  }

  @Override
  protected JSONValue toJson(HasSummary entity) {
    JSONValue value = super.toJson(entity);
    if (value instanceof JSONObject == false)
      return value;

    JSONObject object = (JSONObject) value;

    if (entity instanceof BBank) {
      if (((BBank) entity).getCode() != null)
        object.put("code", new JSONString(((BBank) entity).getCode()));
      if (((BBank) entity).getName() != null)
        object.put("name", new JSONString(((BBank) entity).getName()));
      if (((BBank) entity).getBank() != null)
        object.put("bank", new JSONString(((BBank) entity).getBank()));
      if (((BBank) entity).getAccount() != null)
        object.put("account", new JSONString(((BBank) entity).getAccount()));
      if (((BBank) entity).getAddress() != null)
        object.put("address", new JSONString(((BBank) entity).getAddress()));
      if (((BBank) entity).getBank() != null)
        object.put("bank", new JSONString(((BBank) entity).getBank()));
      if (((BBank) entity).getRemark() != null)
        object.put("remark", new JSONString(((BBank) entity).getRemark()));
      return object;

    }
    return object;
  }
}
