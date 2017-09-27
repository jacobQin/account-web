package com.hd123.m3.account.gwt.commons.client.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;

/**
 * json -> List<BBillType>
 * 
 * @author chenrizhang
 * 
 */
public class BBillTypeUtils {

  public static List<BBillType> fromJson(String json) {
    List<BBillType> ucns = new ArrayList<BBillType>();

    if (json == null || "".equals(json.trim())) {
      return ucns;
    }

    JSONValue value = JSONParser.parseLenient(json);
    JSONArray list = value.isArray();
    if (list == null) {
      JSONObject obj = value.isObject();
      if (obj != null) {
        BBillType ucn = fromJson(obj);
        if (ucn != null) {
          ucns.add(ucn);
        }
      }
    } else {
      for (int i = 0; i < list.size(); i++) {
        JSONObject obj = list.get(i).isObject();
        BBillType ucn = fromJson(obj);
        if (ucn != null) {
          ucns.add(ucn);
        }
      }
    }

    return ucns;
  }

  private static BBillType fromJson(JSONObject json) {
    if (json == null) {
      return null;
    }

    BBillType type = new BBillType();
    type.setName(getStringValue(json, "name"));
    type.setCaption(getStringValue(json, "caption"));
    type.setClassName(getStringValue(json, "className"));
    type.setDirection(getIntValue(json, "direction"));
    return type;
  }

  private static String getStringValue(JSONObject obj, String name) {
    if (obj == null) {
      return null;
    } else {
      JSONValue value = obj.get(name);
      JSONString str = value.isString();
      if (str == null) {
        return null;
      } else {
        return str.stringValue();
      }
    }
  }

  private static int getIntValue(JSONObject obj, String name) {
    if (obj == null) {
      return 0;
    } else {
      JSONValue value = obj.get(name);
      JSONNumber str = value.isNumber();
      if (str == null) {
        return 0;
      } else {
        return Double.valueOf(str.doubleValue()).intValue();
      }
    }
  }
}
