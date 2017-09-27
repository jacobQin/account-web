/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BStatementLogger.java
 * 模块说明：	
 * 修改历史：
 * 2015-6-18 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.statement.client.biz;

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
public class BStatementLogger extends EntityLogger {

  public static BStatementLogger instance;

  public static BStatementLogger getInstance() {
    if (instance == null)
      instance = new BStatementLogger();
    return instance;
  }

  @Override
  protected JSONValue toJson(HasSummary entity) {
    JSONValue value = super.toJson(entity);
    if (value instanceof JSONObject == false)
      return value;

    JSONObject object = (JSONObject) value;
    if (entity instanceof BStatement) {
      BStatement statement = (BStatement) entity;
      if (statement != null)
        object.put("accountUnit", new JSONString(statement.getAccountUnit().toFriendlyStr()));
      if (statement.getCounterpart() != null)
        object.put("counterpart", new JSONString(statement.getCounterpart().toFriendlyStr()));
      if (statement.getContract() != null)
        object.put("contract", new JSONString(statement.getContract().toFriendlyStr()));
      if (statement.getType() != null)
        object.put("type", new JSONString(statement.getType()));
      if (statement.getSettleState() != null)
        object.put("settleState", new JSONString(statement.getSettleState()));
      if (statement.getReceiptAccDate() != null)
        object.put("receiptAccDate",
            new JSONString(M3Format.fmt_yMd.format(statement.getReceiptAccDate())));
      if (statement.getPayTotal() != null) {
        if (statement.getPayTotal().getTotal() != null)
          object.put("payTotal.total",
              new JSONString(M3Format.fmt_money.format(statement.getPayTotal().getTotal())));
        if (statement.getPayTotal().getTax() != null)
          object.put("payTotal.tax",
              new JSONString(M3Format.fmt_money.format(statement.getPayTotal().getTax())));
      }
      if (statement.getFreePayTotal() != null) {
        if (statement.getFreePayTotal().getTotal() != null)
          object.put("freePayTotal.total",
              new JSONString(M3Format.fmt_money.format(statement.getFreePayTotal().getTotal())));
        if (statement.getPayTotal().getTax() != null)
          object.put("freePayTotal.tax",
              new JSONString(M3Format.fmt_money.format(statement.getFreePayTotal().getTax())));
      }
      if (statement.getIvcPayTotal() != null) {
        if (statement.getIvcPayTotal().getTotal() != null)
          object.put("ivcPayTotal.total",
              new JSONString(M3Format.fmt_money.format(statement.getIvcPayTotal().getTotal())));
        if (statement.getPayTotal().getTax() != null)
          object.put("ivcPayTotal.tax",
              new JSONString(M3Format.fmt_money.format(statement.getIvcPayTotal().getTax())));
      }
      if (statement.getReceiptTotal() != null) {
        if (statement.getReceiptTotal().getTotal() != null)
          object.put("receiptTotal.total",
              new JSONString(M3Format.fmt_money.format(statement.getReceiptTotal().getTotal())));
        if (statement.getPayTotal().getTax() != null)
          object.put("receiptTotal.tax",
              new JSONString(M3Format.fmt_money.format(statement.getReceiptTotal().getTax())));
      }
      if (statement.getFreeReceiptTotal() != null) {
        if (statement.getFreeReceiptTotal().getTotal() != null)
          object
              .put(
                  "freeReceiptTotal.total",
                  new JSONString(M3Format.fmt_money.format(statement.getFreeReceiptTotal()
                      .getTotal())));
        if (statement.getPayTotal().getTax() != null)
          object.put("freeReceiptTotal.tax",
              new JSONString(M3Format.fmt_money.format(statement.getFreeReceiptTotal().getTax())));
      }
      if (statement.getIvcReceiptTotal() != null) {
        if (statement.getIvcReceiptTotal().getTotal() != null)
          object.put("ivcReceiptTotal.total",
              new JSONString(M3Format.fmt_money.format(statement.getIvcReceiptTotal().getTotal())));
        if (statement.getPayTotal().getTax() != null)
          object.put("ivcReceiptTotal.tax",
              new JSONString(M3Format.fmt_money.format(statement.getIvcReceiptTotal().getTax())));
      }
      if (statement.getAccountTime() != null)
        object.put("accountTime",
            new JSONString(M3Format.fmt_yMd.format(statement.getAccountTime())));
      if (statement.getPlanDate() != null)
        object.put("planDate", new JSONString(M3Format.fmt_yMd.format(statement.getPlanDate())));
      if (statement.getSaleTotal() != null)
        object
            .put("saleTotal", new JSONString(M3Format.fmt_money.format(statement.getSaleTotal())));
      if (statement.getCoopMode() != null)
        object.put("coopMode", new JSONString(statement.getCoopMode()));
      if (statement.getAccountType() != null)
        object.put("accountType", new JSONString(statement.getAccountType()));
      if (statement.getPayAdj() != null)
        object.put("payAdj", new JSONString(M3Format.fmt_money.format(statement.getPayAdj())));
      if (statement.getPayed() != null)
        object.put("payed", new JSONString(M3Format.fmt_money.format(statement.getPayed())));
      if (statement.getIvcPayAdj() != null)
        object
            .put("ivcPayAdj", new JSONString(M3Format.fmt_money.format(statement.getIvcPayAdj())));
      if (statement.getIvcPayed() != null)
        object.put("ivcPayed", new JSONString(M3Format.fmt_money.format(statement.getIvcPayed())));
      if (statement.getReceiptAdj() != null)
        object.put("receiptAdj",
            new JSONString(M3Format.fmt_money.format(statement.getReceiptAdj())));
      if (statement.getReceipted() != null)
        object
            .put("receipted", new JSONString(M3Format.fmt_money.format(statement.getReceipted())));
      if (statement.getIvcReceiptAdj() != null)
        object.put("ivcReceiptAdj",
            new JSONString(M3Format.fmt_money.format(statement.getIvcReceiptAdj())));
      if (statement.getIvcReceipted() != null)
        object.put("ivcReceipted",
            new JSONString(M3Format.fmt_money.format(statement.getIvcReceipted())));
      if (statement.getRemark() != null)
        object.put("remark", new JSONString(statement.getRemark()));
    }
    return object;
  }
}
