package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.hd123.m3.account.gwt.cpnts.client.biz.TypeBUCN;
import com.hd123.rumba.gwt.flecs.client.codec.DecodeException;
import com.hd123.rumba.gwt.flecs.client.codec.EncodeException;
import com.hd123.rumba.gwt.flecs.client.codec.OperandCodec;

/**
 * 将TypeBUCN和JSON对象进行互相转换的工具类
 * 
 * @author zhangyanbo
 * 
 */
public class TypeUCNCodec implements OperandCodec {

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
    TypeBUCN result = new TypeBUCN();
    result.setUuid(decodeString((JSONObject) jsonUcn, JSON_UUID));
    result.setCode(decodeString((JSONObject) jsonUcn, JSON_CODE));
    result.setName(decodeString((JSONObject) jsonUcn, JSON_NAME));
    result.setType(decodeString((JSONObject) jsonUcn, JSON_TYPE));
    return result;
  }

  @Override
  public JSONValue encodeOperand(Object operand) throws EncodeException {
    if (operand == null)
      return JSONNull.getInstance();
    if (operand instanceof TypeBUCN == false)
      throw new EncodeException();

    TypeBUCN value = (TypeBUCN) operand;
    JSONObject result = new JSONObject();
    JSONObject jsonUcn = new JSONObject();
    jsonUcn.put(JSON_UUID, encodeString(value.getUuid()));
    jsonUcn.put(JSON_CODE, encodeString(value.getCode()));
    jsonUcn.put(JSON_NAME, encodeString(value.getName()));
    jsonUcn.put(JSON_TYPE, encodeString(value.getType()));
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

  private static final String JSON_KEY = "typeUCN";
  private static final String JSON_UUID = "uuid";
  private static final String JSON_CODE = "code";
  private static final String JSON_NAME = "name";
  private static final String JSON_TYPE = "type";
}
