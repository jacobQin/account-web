/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BStatementAdjustLogger.java
 * 模块说明：	
 * 修改历史：
 * 2015-6-18 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.biz;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.hd123.m3.commons.gwt.base.client.biz.HasSummary;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.log.EntityLogger;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author liuguilin
 * 
 */
public class BStatementAdjustLogger extends EntityLogger {

  public static BStatementAdjustLogger instance;

  public static BStatementAdjustLogger getInstance() {
    if (instance == null)
      instance = new BStatementAdjustLogger();
    return instance;
  }

  @Override
  protected JSONValue toJson(HasSummary entity) {
    JSONValue value = super.toJson(entity);
    if (value instanceof JSONObject == false)
      return value;

    JSONObject object = (JSONObject) value;
    if (entity instanceof BStatementAdjust) {
      BStatementAdjust adjust = (BStatementAdjust) entity;
      if (adjust.getAccountUnit() != null)
        object.put("accountUnit", new JSONString(adjust.getAccountUnit().toFriendlyStr()));
      if (adjust.getCounterpart() != null)
        object.put("counterpart", new JSONString(adjust.getCounterpart().toFriendlyStr()));
      if (adjust.getBcontract() != null)
        object.put("contract", new JSONString(adjust.getBcontract().toFriendlyStr()));
      if (adjust.getStatement() != null)
        object.put("statement", new JSONString(adjust.getStatement().id()));
      if (adjust.getBizState() != null)
        object.put("bizState", new JSONString(adjust.getBizState()));
      if (adjust.getReceiptTotal() != null)
        object.put("receiptTotal",
            new JSONString(M3Format.fmt_money.format(adjust.getReceiptTotal())));
      if (adjust.getReceiptTax() != null)
        object.put("receiptTax", new JSONString(M3Format.fmt_money.format(adjust.getReceiptTax())));
      if (adjust.getPaymentTotal() != null)
        object.put("paymentTotal",
            new JSONString(M3Format.fmt_money.format(adjust.getPaymentTotal())));
      if (adjust.getReceiptTax() != null)
        object.put("paymentTax", new JSONString(M3Format.fmt_money.format(adjust.getPaymentTax())));

      JSONArray accountRanges = new JSONArray();
      int index = 0;
      for (int i = 0; i < adjust.getAccountRanges().size(); i++) {
        BDateRange range = adjust.getAccountRanges().get(i);
        JSONObject op = new JSONObject();
        op.put("index", new JSONNumber(i));
        op.put("beginDate", new JSONString(M3Format.fmt_yMd.format(range.getBeginDate())));
        op.put("endDate", new JSONString(M3Format.fmt_yMd.format(range.getEndDate())));
        accountRanges.set(index++, op);
      }
      object.put("accountRanges", accountRanges);

      JSONArray lines = new JSONArray();
      index = 0;
      for (int i = 0; i < adjust.getLines().size(); i++) {
        BStatementAdjustLine line = adjust.getLines().get(i);
        if (line.getSubject() == null || line.getSubject().getUuid() == null) {
          continue;
        }
        JSONObject op = new JSONObject();
        op.put("index", new JSONNumber(i));
        op.put("subject", new JSONString(BUCN.toFriendlyStr(line.getSubject())));
        op.put("direction", new JSONString(String.valueOf(line.getDirection())));
        if (line.getTotal() != null) {
          op.put("total", new JSONString(M3Format.fmt_money.format(line.getTotal().getTotal())));
          op.put("tax", new JSONString(M3Format.fmt_money.format(line.getTotal().getTax())));
        }
        if (line.getTaxRate() != null)
          op.put("taxRate", new JSONString(line.getTaxRate().toString()));
        op.put("invoice", new JSONString(String.valueOf(line.isInvoice())));
        if (line.getBeginDate() != null)
          op.put("beginDate", new JSONString(M3Format.fmt_yMd.format(line.getBeginDate())));
        if (line.getEndDate() != null)
          op.put("endDate", new JSONString(M3Format.fmt_yMd.format(line.getEndDate())));
        if (line.getRemark() != null)
          op.put("remark", new JSONString(line.getRemark()));
        if (line.getSourceAccountId() != null)
          op.put("sourceAccountId", new JSONString(line.getSourceAccountId()));
        if (line.getAccountDate() != null)
          op.put("accountDate", new JSONString(M3Format.fmt_yMd.format(line.getAccountDate())));
        if (line.getLastPayDate() != null)
          op.put("lastPayDate", new JSONString(M3Format.fmt_yMd.format(line.getLastPayDate())));

        lines.set(index++, op);
      }
      object.put("lines", lines);
      return object;
    }
    return object;
  }
}
