/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BDepositLogger.java
 * 模块说明：	
 * 修改历史：
 * 2015-6-18 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.deposit.commons.client.biz;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
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
public class BDepositLogger extends EntityLogger {
  public static BDepositLogger instance;

  public static BDepositLogger getInstance() {
    if (instance == null)
      instance = new BDepositLogger();
    return instance;
  }

  @Override
  protected JSONValue toJson(HasSummary entity) {
    JSONValue value = super.toJson(entity);
    if (value instanceof JSONObject == false)
      return value;

    JSONObject object = (JSONObject) value;
    if (entity instanceof BDeposit) {
      BDeposit deposit = (BDeposit) entity;
      if (deposit.getAccountUnit() != null)
        object.put("accountUnit", new JSONString(deposit.getAccountUnit().toFriendlyStr()));
      if (deposit.getCounterpart() != null)
        object.put("counterpart", new JSONString(deposit.getCounterpart().toFriendlyStr()));
      if (deposit.getContract() != null)
        object.put("contract", new JSONString(deposit.getContract().toFriendlyStr()));
      if (deposit.getPaymentType() != null)
        object.put("paymentType", new JSONString(deposit.getPaymentType().toFriendlyStr()));
      if (deposit.getDepositDate() != null)
        object
            .put("depositDate", new JSONString(M3Format.fmt_yMd.format(deposit.getDepositDate())));
      if (deposit.getDealer() != null)
        object.put("dealer", new JSONString(deposit.getDealer().toFriendlyStr()));
      if (deposit.getBank() != null)
        object.put("bank", new JSONString(deposit.getBank().toFriendlyStr()));
      if (deposit.getCounterContact() != null)
        object.put("counterContact", new JSONString(deposit.getCounterContact()));
      if (deposit.getDepositTotal() != null)
        object.put("depositTotal",
            new JSONString(M3Format.fmt_money.format(deposit.getDepositTotal())));
      object.put("direction", new JSONString(String.valueOf(deposit.getDirection())));
      if (deposit.getRemark() != null)
        object.put("remark", new JSONString(deposit.getRemark()));

      JSONArray lines = new JSONArray();
      int index = 0;
      for (int i = 0; i < deposit.getLines().size(); i++) {
        BDepositLine line = deposit.getLines().get(i);
        if (line.getSubject() == null || line.getSubject().getUuid() == null)
          continue;

        JSONObject op = new JSONObject();
        op.put("index", new JSONNumber(i));
        op.put("subject", new JSONString(line.getSubject().toFriendlyStr()));
        if (line.getTotal() != null)
          op.put("total", new JSONString(M3Format.fmt_money.format(line.getTotal())));
        if (line.getRemainTotal() != null)
          op.put("remainTotal", new JSONString(M3Format.fmt_money.format(line.getRemainTotal())));
        if (line.getContractTotal() != null)
          op.put("contractTotal",
              new JSONString(M3Format.fmt_money.format(line.getContractTotal())));
        if (line.getRemark() != null)
          op.put("remark", new JSONString(line.getRemark()));

        lines.set(index++, op);
      }
      object.put("lines", lines);
      return object;
    }
    return object;
  }
}
