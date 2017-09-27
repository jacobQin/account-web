/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-basic
 * 文件名：	BSubjectLogger.java
 * 模块说明：	
 * 修改历史：
 * 2015-6-17 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.subject.client.biz;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.hd123.m3.commons.gwt.base.client.biz.HasSummary;
import com.hd123.m3.commons.gwt.base.client.log.EntityLogger;

/**
 * @author liuguilin
 * 
 */
public class BSubjectLogger extends EntityLogger {

  public static BSubjectLogger instance;

  public static BSubjectLogger getInstatnce() {
    if (instance == null) {
      instance = new BSubjectLogger();
    }
    return instance;
  }

  @Override
  protected JSONValue toJson(HasSummary entity) {
    JSONValue value = super.toJson(entity);
    if (value instanceof JSONObject == false)
      return value;

    JSONObject object = (JSONObject) value;

    if (entity instanceof BSubject) {
      BSubject subject = (BSubject) entity;
      if (subject.getCode() != null)
        object.put("code", new JSONString(subject.getCode()));
      if (subject.getName() != null)
        object.put("name", new JSONString(subject.getName()));
      object.put("direction", new JSONString(String.valueOf(subject.getDirection())));
      object.put("enable", new JSONString(String.valueOf(subject.isEnabled())));
      if (subject.getType() != null)
        object.put("type", new JSONString(subject.getType()));
      if (subject.getCustomType() != null)
        object.put("customType", new JSONString(subject.getCustomType()));
      if (subject.getTaxRate() != null) {
        object.put("taxRate", new JSONString(subject.getTaxRate().toString()));
      }
      if (subject.getRemark() != null)
        object.put("remark", new JSONString(subject.getRemark()));

      JSONArray usages = new JSONArray();
      int index = 0;
      for (int i = 0; i < subject.getUsages().size(); i++) {
        String usage = subject.getUsages().get(i);
        JSONObject op = new JSONObject();
        op.put("index", new JSONNumber(i));
        op.put("usage", new JSONString(usage));
        usages.set(index++, op);
      }
      object.put("usages", usages);
      return object;
    }

    return object;
  }
}
