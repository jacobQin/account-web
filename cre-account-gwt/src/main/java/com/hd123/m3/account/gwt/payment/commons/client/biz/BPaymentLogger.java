/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BPaymentLogger.java
 * 模块说明：	
 * 修改历史：
 * 2015-6-18 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.biz;

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
public class BPaymentLogger extends EntityLogger {

  public static BPaymentLogger instance;

  public static BPaymentLogger getInstance() {
    if (instance == null) {
      instance = new BPaymentLogger();
    }
    return instance;
  }

  @Override
  protected JSONValue toJson(HasSummary entity) {
    JSONValue value = super.toJson(entity);
    if (value instanceof JSONObject == false) {
      return value;
    }

    JSONObject object = (JSONObject) value;
    if (entity instanceof BPayment) {
      BPayment payment = (BPayment) entity;
      if (payment.getAccountUnit() != null)
        object.put("accountUnit", new JSONString(BUCN.toFriendlyStr(payment.getAccountUnit())));
      if (payment.getCounterpart() != null)
        object.put("counterpart", new JSONString(BUCN.toFriendlyStr(payment.getCounterpart())));
      object.put("direction", new JSONString(String.valueOf(payment.getDirection())));
      if (payment.getDefrayalType() != null)
        object.put("defrayalType", new JSONString(payment.getDefrayalType()));
      if (payment.getDealer() != null)
        object.put("dealer", new JSONString(BUCN.toFriendlyStr(payment.getDealer())));
      if (payment.getPaymentDate() != null)
        object
            .put("paymentDate", new JSONString(M3Format.fmt_yMd.format(payment.getPaymentDate())));
      if (payment.getReceiptTotal() != null) {
        if (payment.getReceiptTotal().getTotal() != null)
          object.put("receiptTotal.total",
              new JSONString(M3Format.fmt_money.format(payment.getReceiptTotal().getTotal())));
        if (payment.getReceiptTotal().getTax() != null)
          object.put("receiptTotal.tax",
              new JSONString(M3Format.fmt_money.format(payment.getReceiptTotal().getTax())));
      }
      if (payment.getUnpayedTotal() != null) {
        if (payment.getUnpayedTotal().getTotal() != null)
          object.put("unpayedTotal.total",
              new JSONString(M3Format.fmt_money.format(payment.getUnpayedTotal().getTotal())));
        if (payment.getUnpayedTotal().getTax() != null)
          object.put("unpayedTotal.tax",
              new JSONString(M3Format.fmt_money.format(payment.getUnpayedTotal().getTax())));
      }
      if (payment.getTotal() != null) {
        if (payment.getTotal().getTotal() != null)
          object.put("total.total",
              new JSONString(M3Format.fmt_money.format(payment.getTotal().getTotal())));
        if (payment.getTotal().getTax() != null)
          object.put("total.tax",
              new JSONString(M3Format.fmt_money.format(payment.getTotal().getTax())));
      }
      if (payment.getOverdueTotal() != null) {
        if (payment.getOverdueTotal().getTotal() != null)
          object.put("overdueTotal.total",
              new JSONString(M3Format.fmt_money.format(payment.getOverdueTotal().getTotal())));
        if (payment.getOverdueTotal().getTax() != null)
          object.put("overdueTotal.tax",
              new JSONString(M3Format.fmt_money.format(payment.getOverdueTotal().getTax())));
      }
      if (payment.getDefrayalTotal() != null)
        object.put("defrayalTotal",
            new JSONString(M3Format.fmt_money.format(payment.getDefrayalTotal())));
      if (payment.getDepositTotal() != null)
        object.put("depositTotal",
            new JSONString(M3Format.fmt_money.format(payment.getDepositTotal())));
      if (payment.getDepositSubject() != null)
        object.put("depositSubject",
            new JSONString(BUCN.toFriendlyStr(payment.getDepositSubject())));
      if (payment.getIncomeDate() != null)
        object.put("incomeDate", new JSONString(M3Format.fmt_yMd.format(payment.getIncomeDate())));
      if(payment.getRemark() != null)
        object.put("remark", new JSONString(payment.getRemark()));
    }
    return object;
  }
}
