/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BDepositMoveLogger.java
 * 模块说明：	
 * 修改历史：
 * 2015-6-18 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.commons.client.biz;

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
public class BDepositMoveLogger extends EntityLogger {
  public static BDepositMoveLogger instance;

  public static BDepositMoveLogger getInstance() {
    if (instance == null)
      instance = new BDepositMoveLogger();
    return instance;
  }

  @Override
  protected JSONValue toJson(HasSummary entity) {
    JSONValue value = super.toJson(entity);
    if (value instanceof JSONObject == false)
      return value;

    JSONObject object = (JSONObject) value;
    if (entity instanceof BDepositMove) {
      BDepositMove move = (BDepositMove) entity;
      if (move.getAccountUnit() != null)
        object.put("accountUnit", new JSONString(move.getAccountUnit().toFriendlyStr()));
      object.put("direction", new JSONString(String.valueOf(move.getDirection())));
      if (move.getOutCounterpart() != null)
        object.put("outCounterpart", new JSONString(move.getOutCounterpart().toFriendlyStr()));
      if (move.getOutContract() != null)
        object.put("outContract", new JSONString(move.getOutContract().toFriendlyStr()));
      if (move.getOutSubject() != null)
        object.put("outSubject", new JSONString(move.getOutSubject().toFriendlyStr()));
      if (move.getOutBalance() != null)
        object.put("outBalance", new JSONString(M3Format.fmt_money.format(move.getOutBalance())));
      if (move.getInCounterpart() != null)
        object.put("inCounterpart", new JSONString(move.getInCounterpart().toFriendlyStr()));
      if (move.getInContract() != null)
        object.put("inContract", new JSONString(move.getInContract().toFriendlyStr()));
      if (move.getInSubject() != null)
        object.put("inSubject", new JSONString(move.getInSubject().toFriendlyStr()));
      if (move.getInBalance() != null)
        object.put("inBalance", new JSONString(M3Format.fmt_money.format(move.getInBalance())));
      if (move.getAmount() != null)
        object.put("amount", new JSONString(M3Format.fmt_money.format(move.getAmount())));
      if (move.getRemark() != null)
        object.put("remark", new JSONString(move.getRemark()));
    }
    return object;
  }
}
