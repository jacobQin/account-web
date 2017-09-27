/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BPaymentNoticeLogger.java
 * 模块说明：	
 * 修改历史：
 * 2015-6-18 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.biz;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.hd123.m3.commons.gwt.base.client.biz.HasSummary;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.log.EntityLogger;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author liuguilin
 * 
 */
public class BPaymentNoticeLogger extends EntityLogger {

  public static BPaymentNoticeLogger instance;

  public static BPaymentNoticeLogger getInstance() {
    if (instance == null)
      instance = new BPaymentNoticeLogger();
    return instance;
  }

  @Override
  protected JSONValue toJson(HasSummary entity) {
    JSONValue value = super.toJson(entity);
    if (value instanceof JSONObject == false)
      return value;

    JSONObject object = (JSONObject) value;
    if (entity instanceof BPaymentNotice) {
      BPaymentNotice notice = (BPaymentNotice) entity;
      if (notice.getAccountUnit() != null)
        object.put("accountUnit", new JSONString(notice.getAccountUnit().toFriendlyStr()));
      if (notice.getCounterpart() != null)
        object.put("counterpart", new JSONString(notice.getCounterpart().toFriendlyStr()));
      if (notice.getBizState() != null)
        object.put("bizState", new JSONString(notice.getBizState()));
      if (notice.getReceiptTotal() != null)
        object.put("receiptTotal",
            new JSONString(M3Format.fmt_money.format(notice.getReceiptTotal())));
      if (notice.getReceiptTax() != null)
        object.put("receiptTax", new JSONString(M3Format.fmt_money.format(notice.getReceiptTax())));
      if (notice.getReceiptIvcTotal() != null)
        object.put("receiptIvcTotal",
            new JSONString(M3Format.fmt_money.format(notice.getReceiptIvcTotal())));
      if (notice.getReceiptIvcTax() != null)
        object.put("receiptIvcTax",
            new JSONString(M3Format.fmt_money.format(notice.getReceiptIvcTax())));
      if (notice.getPaymentTotal() != null)
        object.put("paymentTotal",
            new JSONString(M3Format.fmt_money.format(notice.getPaymentTotal())));
      if (notice.getPaymentTax() != null)
        object.put("paymentTax", new JSONString(M3Format.fmt_money.format(notice.getPaymentTax())));
      if (notice.getPaymentIvcTotal() != null)
        object.put("paymentIvcTotal",
            new JSONString(M3Format.fmt_money.format(notice.getPaymentIvcTotal())));
      if (notice.getPaymentIvcTax() != null)
        object.put("paymentIvcTax",
            new JSONString(M3Format.fmt_money.format(notice.getPaymentIvcTax())));
      if (notice.getRemark() != null)
        object.put("remark", new JSONString(notice.getRemark()));

      JSONArray lines = new JSONArray();
      int index = 0;
      for (int i = 0; i < notice.getLines().size(); i++) {
        BPaymentNoticeLine line = notice.getLines().get(i);
        JSONObject op = new JSONObject();
        op.put("index", new JSONNumber(i));
        if (line.getStatement() != null)
          op.put("statement", new JSONString(line.getStatement().id()));
        if (line.getContract() != null)
          op.put("contract", new JSONString(BUCN.toFriendlyStr(line.getContract())));
        if (line.getAccountUnit() != null)
          op.put("accountUnit", new JSONString(BUCN.toFriendlyStr(line.getAccountUnit())));
        if (line.getReceiptTotal() != null)
          op.put("receiptTotal", new JSONString(M3Format.fmt_money.format(line.getReceiptTotal())));
        if (line.getReceiptTax() != null)
          op.put("receiptTax", new JSONString(M3Format.fmt_money.format(line.getReceiptTax())));
        if (line.getReceiptIvcTotal() != null)
          op.put("receiptIvcTotal",
              new JSONString(M3Format.fmt_money.format(line.getReceiptIvcTotal())));
        if (line.getReceiptIvcTax() != null)
          op.put("receiptIvcTax",
              new JSONString(M3Format.fmt_money.format(line.getReceiptIvcTax())));
        if (line.getPaymentTotal() != null)
          op.put("paymentTotal", new JSONString(M3Format.fmt_money.format(line.getPaymentTotal())));
        if (line.getPaymentTax() != null)
          op.put("paymentTax", new JSONString(M3Format.fmt_money.format(line.getPaymentTax())));
        if (line.getPaymentIvcTotal() != null)
          op.put("paymentIvcTotal",
              new JSONString(M3Format.fmt_money.format(line.getPaymentIvcTotal())));
        if (line.getPaymentIvcTax() != null)
          op.put("paymentIvcTax",
              new JSONString(M3Format.fmt_money.format(line.getPaymentIvcTax())));
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
