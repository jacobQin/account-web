/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInternalFeeLogger.java
 * 模块说明：	
 * 修改历史：
 * 2015-6-17 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.client.biz;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
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
public class BInternalFeeLogger extends EntityLogger {

  public static BInternalFeeLogger instance;

  public static BInternalFeeLogger getInstance() {
    if (instance == null)
      instance = new BInternalFeeLogger();
    return instance;
  }

  @Override
  protected JSONValue toJson(HasSummary entity) {
    JSONValue value = super.toJson(entity);
    if (value instanceof JSONObject == false)
      return value;

    JSONObject object = (JSONObject) value;

    if (entity instanceof BInternalFee) {
      BInternalFee fee = (BInternalFee) entity;
      if (fee.getStore() != null)
        object.put("store", new JSONString(fee.getStore().toFriendlyStr()));
      if (fee.getVendor() != null)
        object.put("vendor", new JSONString(fee.getVendor().toFriendlyStr()));
      if (fee.getBeginDate() != null)
        object.put("beginDate", new JSONString(M3Format.fmt_yMd.format(fee.getBeginDate())));
      if (fee.getEndDate() != null)
        object.put("endDate", new JSONString(M3Format.fmt_yMd.format(fee.getEndDate())));
      object.put("direction", new JSONString(String.valueOf(fee.getDirection())));
      if (fee.getTotal() != null) {
        if (fee.getTotal().getTotal() != null)
          object.put("total", new JSONString(M3Format.fmt_money.format(fee.getTotal().getTotal())));
        if (fee.getTotal().getTax() != null)
          object.put("tax", new JSONString(M3Format.fmt_money.format(fee.getTotal().getTax())));
      }
      if (fee.getRemark() != null)
        object.put("remark", new JSONString(fee.getRemark()));

      JSONArray lines = new JSONArray();
      int index = 0;
      for (int i = 0; i < fee.getLines().size(); i++) {
        BInternalFeeLine l = fee.getLines().get(i);
        if (l.getSubject() == null || l.getSubject().getUuid() == null) {
          continue;
        }
        JSONObject op = new JSONObject();
        op.put("index", new JSONNumber(i));
        op.put("subject", new JSONString(BUCN.toFriendlyStr(l.getSubject())));
        if (l.getTotal() != null) {
          if (l.getTotal().getTotal() != null)
            op.put("total", new JSONString(M3Format.fmt_money.format(l.getTotal().getTotal())));
          if (l.getTotal().getTax() != null)
            op.put("tax", new JSONString(M3Format.fmt_money.format(l.getTotal().getTax())));
        }
        if (l.getTaxRate() != null)
          op.put("taxRate", new JSONString(l.getTaxRate().toString()));
        if (l.getRemark() != null)
          op.put("remark", new JSONString(l.getRemark()));
        op.put("issueInvoice", JSONBoolean.getInstance(l.isIssueInvoice()));

        lines.set(index++, op);
      }
      object.put("lines", lines);
      return object;
    }
    return object;
  }

}
