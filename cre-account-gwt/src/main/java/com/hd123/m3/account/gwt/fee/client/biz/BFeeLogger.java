/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BPaymentLogger.java
 * 模块说明：	
 * 修改历史：
 * 2015-6-18 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.biz;

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
public class BFeeLogger extends EntityLogger {

  public static BFeeLogger instance;

  public static BFeeLogger getInstance() {
    if (instance == null) {
      instance = new BFeeLogger();
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
    if (entity instanceof BFee) {
      BFee fee = (BFee) entity;
      if (fee.getAccountUnit() != null)
        object.put("accountUnit", new JSONString(BUCN.toFriendlyStr(fee.getAccountUnit())));
      if (fee.getCounterpart() != null)
        object.put("counterpart", new JSONString(BUCN.toFriendlyStr(fee.getCounterpart())));
      object.put("direction", new JSONString(String.valueOf(fee.getDirection())));
      if (fee.getContract() != null)
        object.put("contract", new JSONString(BUCN.toFriendlyStr(fee.getContract())));
      if (fee.getDealer() != null)
        object.put("dealer", new JSONString(fee.getDealer()));
      if (fee.getAccountDate() != null)
        object.put("accountDate", new JSONString(M3Format.fmt_yMd.format(fee.getAccountDate())));
      if (fee.getDateRange() != null) {
        object.put(
            "dateRange",
            new JSONString((fee.getDateRange().getBeginDate() == null ? "" : M3Format.fmt_yMd
                .format(fee.getDateRange().getBeginDate()))
                + "~"
                + (fee.getDateRange().getEndDate() == null ? "" : M3Format.fmt_yMd.format(fee
                    .getDateRange().getEndDate()))));
      }
      if (fee.getTotal() != null) {
        if (fee.getTotal().getTotal() != null)
          object.put("total.total",
              new JSONString(M3Format.fmt_money.format(fee.getTotal().getTotal())));
        if (fee.getTotal().getTax() != null)
          object.put("total.tax",
              new JSONString(M3Format.fmt_money.format(fee.getTotal().getTax())));
      }
      if (fee.getRemark() != null)
        object.put("remark", new JSONString(fee.getRemark()));
    }
    return object;
  }
}
