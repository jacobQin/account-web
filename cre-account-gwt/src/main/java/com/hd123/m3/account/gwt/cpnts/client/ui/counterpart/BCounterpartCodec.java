package com.hd123.m3.account.gwt.cpnts.client.ui.counterpart;

import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.hd123.rumba.gwt.flecs.client.codec.DecodeException;
import com.hd123.rumba.gwt.flecs.client.codec.EncodeException;
import com.hd123.rumba.gwt.flecs.client.codec.OperandCodec;

/**
 * 将BCounterpart和JSON对象进行互相转换的工具类
 * 
 * @author zhangyanbo
 * 
 */
public class BCounterpartCodec implements OperandCodec {

  @Override
  public Object decodeOperand(JSONValue json) throws DecodeException {
    if (json instanceof JSONNull)
      return null;
    if (json instanceof JSONObject == false)
      throw new DecodeException();
    JSONObject jsonObject = (JSONObject) json;
    if (jsonObject.containsKey(JSON_KEY) == false)
      throw new DecodeException();

    JSONValue jsonUcn = jsonObject.get(JSON_KEY);
    if (jsonUcn instanceof JSONObject == false)
      throw new DecodeException();
    BCounterpart result = new BCounterpart();
    result.setUuid(decodeString((JSONObject) jsonUcn, JSON_UUID));
    result.setCode(decodeString((JSONObject) jsonUcn, JSON_CODE));
    result.setName(decodeString((JSONObject) jsonUcn, JSON_NAME));
    result.setModule(decodeString((JSONObject) jsonUcn, JSON_MODULE));
    return result;
  }

  @Override
  public JSONValue encodeOperand(Object operand) throws EncodeException {
    if (operand == null)
      return JSONNull.getInstance();
    if (operand instanceof BCounterpart == false)
      throw new EncodeException();

    BCounterpart value = (BCounterpart) operand;
    JSONObject result = new JSONObject();
    JSONObject jsonUcn = new JSONObject();
    jsonUcn.put(JSON_UUID, encodeString(value.getUuid()));
    jsonUcn.put(JSON_CODE, encodeString(value.getCode()));
    jsonUcn.put(JSON_NAME, encodeString(value.getName()));
    jsonUcn.put(JSON_MODULE, encodeString(value.getModule()));
    result.put(JSON_KEY, jsonUcn);
    return result;
  }

  private static JSONValue encodeString(String str) {
    if (str == null)
      return JSONNull.getInstance();
    return new JSONString(str);
  }

  private static String decodeString(JSONObject json, String key) throws DecodeException {
    JSONValue value = json.get(key);
    if (value instanceof JSONNull)
      return null;
    if (value instanceof JSONString == false)
      throw new DecodeException();
    return ((JSONString) value).stringValue();
  }

  private static final String JSON_KEY = "Counterpart";
  private static final String JSON_UUID = "uuid";
  private static final String JSON_CODE = "code";
  private static final String JSON_NAME = "name";
  private static final String JSON_MODULE = "module";
}
