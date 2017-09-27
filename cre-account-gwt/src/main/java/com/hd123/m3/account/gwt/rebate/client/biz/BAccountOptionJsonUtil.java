/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	Calculator.java
 * 模块说明：	
 * 修改历史：
 * 2015年7月9日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.biz;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;

/**
 * 计算器
 * 
 * @author chenrizhang
 *
 */
public class BAccountOptionJsonUtil {

  public static Map<String, BAccountOption> mapFromJson(String json) {
    List<BAccountOption> list = fromJson(json);

    Map<String, BAccountOption> result = new HashMap<String, BAccountOption>();
    if (list != null) {
      for (BAccountOption option : list) {
        if (StringUtil.isNullOrBlank(option.getStoreUuid())) {
          result.put(BAccountOption.DEFAULT_OPTION, option);
        } else {
          result.put(option.getStoreUuid(), option);
        }
      }
    }
    if (result.containsKey(BAccountOption.DEFAULT_OPTION) == false) {// 保证包含一个默认的结算配置
      result.put(BAccountOption.DEFAULT_OPTION, new BAccountOption());
    }
    // 保证这个默认的结算配置值不是空的
    BAccountOption defaultOption = result.get(BAccountOption.DEFAULT_OPTION);
    if (defaultOption.getScale() == null) {
      defaultOption.setScale(BAccountOption.DEFAULT_SCALE);
    }
    if (defaultOption.getMonthDays() == null) {
      defaultOption.setMonthDays(BAccountOption.DEFAULT_MONTH_DAYS);
    }
    if (defaultOption.getRoundingMode() == null) {
      defaultOption.setRoundingMode(BAccountOption.DEFAULT_ROUNDING_MODE);
    }
    if (defaultOption.getIncompleteMonthByRealDays() == null) {
      defaultOption
          .setIncompleteMonthByRealDays(BAccountOption.DEFAULT_INCOMPLETE_MONTH_BY_REAL_DAYS);
    }
    if (defaultOption.getRebateByBill() == null) {
      defaultOption.setRebateByBill(BAccountOption.DEFAULT_REBATE_BY_BILL);
    }

    // 若有项目的结算配置，如果有空的，取默认的结算配置。
    for (BAccountOption option : result.values()) {
      if (option.getScale() == null) {
        option.setScale(defaultOption.getScale());
      }
      if (option.getMonthDays() == null) {
        option.setMonthDays(defaultOption.getMonthDays());
      }
      if (option.getRoundingMode() == null) {
        option.setRoundingMode(defaultOption.getRoundingMode());
      }
      if (option.getIncompleteMonthByRealDays() == null) {
        option.setIncompleteMonthByRealDays(defaultOption.getIncompleteMonthByRealDays());
      }
      if (option.getRebateByBill() == null) {
        option.setRebateByBill(defaultOption.getRebateByBill());
      }
    }

    return result;
  }

  public static List<BAccountOption> fromJson(String json) {
    List<BAccountOption> ucns = new ArrayList<BAccountOption>();

    if (json == null || "".equals(json.trim())) {
      return ucns;
    }

    JSONValue value = JSONParser.parseLenient(json);
    JSONArray list = value.isArray();
    if (list == null) {
      JSONObject obj = value.isObject();
      if (obj != null) {
        BAccountOption ucn = fromJson(obj);
        if (ucn != null) {
          ucns.add(ucn);
        }
      }
    } else {
      for (int i = 0; i < list.size(); i++) {
        JSONObject obj = list.get(i).isObject();
        BAccountOption ucn = fromJson(obj);
        if (ucn != null) {
          ucns.add(ucn);
        }
      }
    }

    return ucns;
  }

  private static BAccountOption fromJson(JSONObject json) {
    if (json == null) {
      return null;
    }

    BAccountOption option = new BAccountOption();
    option.setStoreUuid(getStringValue(json, "storeUuid"));
    option.setScale(getIntValue(json, "scale"));
    option.setMonthDays(getBigDecimalValue(json, "monthDays"));
    option.setRoundingMode(getStringValue(json, "roundingMode") == null ? null : RoundingMode
        .valueOf(getStringValue(json, "roundingMode")));
    option.setIncompleteMonthByRealDays(getBooleanValue(json, "incompleteMonthByRealDays"));
    option.setRebateByBill(getBooleanValue(json, "rebateByBill"));
    return option;
  }

  private static String getStringValue(JSONObject obj, String name) {
    if (obj == null) {
      return null;
    } else {
      JSONValue value = obj.get(name);
      if (value == null)
        return null;
      JSONString str = value.isString();
      if (str == null) {
        return null;
      } else {
        return str.stringValue();
      }
    }
  }

  private static Boolean getBooleanValue(JSONObject obj, String name) {
    if (obj == null) {
      return null;
    } else {
      JSONValue value = obj.get(name);
      if (value == null)
        return null;
      JSONBoolean b = value.isBoolean();
      if (b == null) {
        return null;
      } else {
        return b.booleanValue();
      }
    }
  }

  private static Integer getIntValue(JSONObject obj, String name) {
    if (obj == null) {
      return null;
    } else {
      JSONValue value = obj.get(name);
      if (value == null)
        return null;
      JSONNumber number = value.isNumber();
      if (number == null) {
        return null;
      } else {
        return Double.valueOf(number.doubleValue()).intValue();
      }
    }
  }

  private static BigDecimal getBigDecimalValue(JSONObject obj, String name) {
    if (obj == null) {
      return null;
    } else {
      JSONValue value = obj.get(name);
      if (value == null)
        return null;
      JSONNumber number = value.isNumber();
      if (number == null) {
        return null;
      } else {
        return BigDecimal.valueOf(number.doubleValue());
      }
    }
  }
}
