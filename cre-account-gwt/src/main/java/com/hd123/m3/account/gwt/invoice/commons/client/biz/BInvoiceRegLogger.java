/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceRegLogger.java
 * 模块说明：	
 * 修改历史：
 * 2015-6-18 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.client.biz;

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
public class BInvoiceRegLogger extends EntityLogger {

  public static BInvoiceRegLogger instance;

  public static BInvoiceRegLogger getInstance() {
    if (instance == null) {
      instance = new BInvoiceRegLogger();
    }
    return instance;
  }

  @Override
  protected JSONValue toJson(HasSummary entity) {
    JSONValue value = super.toJson(entity);
    if (value instanceof JSONObject == false)
      return value;

    JSONObject object = (JSONObject) value;
    if (entity instanceof BInvoiceReg) {
      BInvoiceReg reg = (BInvoiceReg) entity;
      if (reg.getAccountUnit() != null)
        object.put("accountUnit", new JSONString(reg.getAccountUnit().toFriendlyStr()));
      if (reg.getCounterpart() != null)
        object.put("counterpart", new JSONString(reg.getCounterpart().toFriendlyStr()));
      object.put("direction", new JSONString(String.valueOf(reg.getDirection())));
      if (reg.getRegDate() != null)
        object.put("regDate", new JSONString(M3Format.fmt_yMd.format(reg.getRegDate())));
      if (reg.getInvoiceCode() != null)
        object.put("invoiceCode", new JSONString(reg.getInvoiceCode()));
      if (reg.getInvoiceTotal() != null) {
        if (reg.getInvoiceTotal().getTotal() != null)
          object.put("invoiceTotal.total",
              new JSONString(M3Format.fmt_money.format(reg.getInvoiceTotal().getTotal())));
        if (reg.getInvoiceTotal().getTax() != null)
          object.put("invoiceTotal.tax",
              new JSONString(M3Format.fmt_money.format(reg.getInvoiceTotal().getTax())));
      }
      if (reg.getAccountTotal() != null) {
        if (reg.getAccountTotal().getTotal() != null)
          object.put("accountTotal.total",
              new JSONString(M3Format.fmt_money.format(reg.getAccountTotal().getTotal())));
        if (reg.getAccountTotal().getTax() != null)
          object.put("accountTotal.tax",
              new JSONString(M3Format.fmt_money.format(reg.getAccountTotal().getTax())));
      }
      if (reg.getTotalDiff() != null)
        object.put("totalDiff", new JSONString(M3Format.fmt_money.format(reg.getTotalDiff())));
      if (reg.getTaxDiff() != null)
        object.put("taxDiff", new JSONString(M3Format.fmt_money.format(reg.getTaxDiff())));
      if (reg.getRemark() != null)
        object.put("remark", new JSONString(reg.getRemark()));
    }
    return object;
  }

}
