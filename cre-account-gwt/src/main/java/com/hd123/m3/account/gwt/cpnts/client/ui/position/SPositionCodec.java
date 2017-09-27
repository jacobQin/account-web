package com.hd123.m3.account.gwt.cpnts.client.ui.position;

import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.flecs.client.codec.DecodeException;
import com.hd123.rumba.gwt.flecs.client.codec.EncodeException;
import com.hd123.rumba.gwt.flecs.client.codec.OperandCodec;

/**
 * 将SPosition和JSON对象进行互相转换的工具类
 * 
 * @author zhangyanbo
 * 
 */
public class SPositionCodec implements OperandCodec {

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
    SPosition result = new SPosition();
    result.setUuid(decodeString((JSONObject) jsonUcn, JSON_UUID));
    result.setCode(decodeString((JSONObject) jsonUcn, JSON_CODE));
    result.setName(decodeString((JSONObject) jsonUcn, JSON_NAME));
    result.setStore(decodeUCN((JSONObject) jsonUcn, JSON_STORE));
    result.setPositionType(decodeString((JSONObject) jsonUcn, JSON_POSITION_TYPE));
    result.setPositionSubType(decodeString((JSONObject) jsonUcn, JSON_POSITION_SUBTYPE));
    result.setSearchType(decodeSearchType((JSONObject) jsonUcn, JSON_SEARCH_TYPE));
    return result;
  }

  @Override
  public JSONValue encodeOperand(Object operand) throws EncodeException {
    if (operand == null)
      return JSONNull.getInstance();
    if (operand instanceof SPosition == false)
      throw new EncodeException();

    SPosition value = (SPosition) operand;
    JSONObject result = new JSONObject();
    JSONObject jsonUcn = new JSONObject();
    jsonUcn.put(JSON_UUID, encodeString(value.getUuid()));
    jsonUcn.put(JSON_CODE, encodeString(value.getCode()));
    jsonUcn.put(JSON_NAME, encodeString(value.getName()));
    jsonUcn.put(JSON_STORE, encodeUCN(value.getStore()));
    jsonUcn.put(JSON_POSITION_TYPE, encodeString(value.getPositionType()));
    jsonUcn.put(JSON_POSITION_SUBTYPE, encodeString(value.getPositionSubType()));
    jsonUcn.put(JSON_SEARCH_TYPE, encodeSearchType(value.getSearchType()));
    result.put(JSON_KEY, jsonUcn);
    return result;
  }

  private static JSONValue encodeString(String str) {
    if (str == null)
      return JSONNull.getInstance();
    return new JSONString(str);
  }

  private static JSONValue encodeUCN(BUCN value) {
    if (value == null)
      return JSONNull.getInstance();
    JSONObject json = new JSONObject();
    json.put(JSON_UUID, encodeString(value.getUuid()));
    json.put(JSON_CODE, encodeString(value.getCode()));
    json.put(JSON_NAME, encodeString(value.getName()));
    return json;
  }

  private static JSONValue encodeSearchType(PositionSubType value) {
    if (value == null)
      return JSONNull.getInstance();
    JSONObject json = new JSONObject();
    json.put(JSON_POSITION_TYPE, encodeString(value.getPositionType()));
    json.put(JSON_POSITION_SUBTYPE, encodeString(value.getPositionSubType()));
    return json;
  }

  private static String decodeString(JSONObject json, String key) throws DecodeException {
    JSONValue value = json.get(key);
    if (value instanceof JSONNull)
      return null;
    if (value instanceof JSONString == false)
      throw new DecodeException();
    return ((JSONString) value).stringValue();
  }

  private static BUCN decodeUCN(JSONObject json, String key) throws DecodeException {
    JSONValue value = json.get(key);
    if (value instanceof JSONNull)
      return null;
    if (value instanceof JSONObject == false)
      throw new DecodeException();
    BUCN result = new BUCN();
    result.setUuid(decodeString((JSONObject) value, JSON_UUID));
    result.setCode(decodeString((JSONObject) value, JSON_CODE));
    result.setName(decodeString((JSONObject) value, JSON_NAME));
    return result;
  }

  private static PositionSubType decodeSearchType(JSONObject json, String key)
      throws DecodeException {
    JSONValue value = json.get(key);
    if (value instanceof JSONNull)
      return null;
    if (value instanceof JSONObject == false)
      throw new DecodeException();
    PositionSubType result = new PositionSubType();
    result.setPositionType(decodeString((JSONObject) value, JSON_POSITION_TYPE));
    result.setPositionSubType(decodeString((JSONObject) value, JSON_POSITION_SUBTYPE));
    return result;
  }

  private static final String JSON_KEY = "TypePosition";
  private static final String JSON_UUID = "uuid";
  private static final String JSON_CODE = "code";
  private static final String JSON_NAME = "name";
  private static final String JSON_STORE = "owner";
  private static final String JSON_POSITION_TYPE = "positionType";
  private static final String JSON_POSITION_SUBTYPE = "positionSubType";
  private static final String JSON_SEARCH_TYPE = "searchType";
}
