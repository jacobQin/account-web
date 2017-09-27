/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BDepostRepaymentLogger.java
 * 模块说明：	
 * 修改历史：
 * 2015-6-18 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.commons.client.biz;

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
public class BDepositRepaymentLogger extends EntityLogger {
  public static BDepositRepaymentLogger instance;

  public static BDepositRepaymentLogger getInstance() {
    if (instance == null)
      instance = new BDepositRepaymentLogger();
    return instance;
  }

  @Override
  protected JSONValue toJson(HasSummary entity) {
    JSONValue value = super.toJson(entity);
    if (value instanceof JSONObject == false)
      return value;

    JSONObject object = (JSONObject) value;
    if (entity instanceof BDepositRepayment) {
      BDepositRepayment repayment = (BDepositRepayment) entity;
      if (repayment.getAccountUnit() != null)
        object.put("accountUnit", new JSONString(repayment.getAccountUnit().toFriendlyStr()));
      if (repayment.getCounterpart() != null)
        object.put("counterpart", new JSONString(repayment.getCounterpart().toFriendlyStr()));
      if (repayment.getContract() != null)
        object.put("contract", new JSONString(repayment.getContract().toFriendlyStr()));
      object.put("direction", new JSONString(String.valueOf(repayment.getDirection())));
      if (repayment.getBizState() != null)
        object.put("bizState", new JSONString(repayment.getBizState()));
      if (repayment.getRepaymentTotal() != null)
        object.put("repaymentTotal",
            new JSONString(M3Format.fmt_money.format(repayment.getRepaymentTotal())));
      if (repayment.getBank() != null)
        object.put("bank", new JSONString(repayment.getBank().toFriendlyStr()));
      if (repayment.getPaymentType() != null)
        object.put("paymentType", new JSONString(repayment.getPaymentType().toFriendlyStr()));
      if (repayment.getRepaymentDate() != null)
        object.put("repaymentDate",
            new JSONString(M3Format.fmt_yMd.format(repayment.getRepaymentDate())));
      if (repayment.getDealer() != null)
        object.put("dealer", new JSONString(repayment.getDealer().toFriendlyStr()));
      if (repayment.getCounterContact() != null)
        object.put("counterContact", new JSONString(repayment.getCounterContact()));
      if (repayment.getRemark() != null)
        object.put("remark", new JSONString(repayment.getRemark()));

      JSONArray lines = new JSONArray();
      int index = 0;
      for (int i = 0; i < repayment.getLines().size(); i++) {
        BDepositRepaymentLine line = repayment.getLines().get(i);
        if (line.getSubject() == null || line.getSubject().getUuid() == null)
          continue;

        JSONObject op = new JSONObject();
        op.put("index", new JSONNumber(i));
        op.put("subject", new JSONString(line.getSubject().toFriendlyStr()));
        if (line.getAmount() != null)
          op.put("amount", new JSONString(M3Format.fmt_money.format(line.getAmount())));
        if (line.getRemainAmount() != null)
          op.put("remainAmount", new JSONString(M3Format.fmt_money.format(line.getRemainAmount())));
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
