/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeUtil.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月23日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client;

import java.math.BigDecimal;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.hd123.m3.account.gwt.acc.client.BAcc1;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;

/**
 * 发票交换单工具类
 * 
 * @author LiBin
 * @since 1.7
 *
 */
public class InvoiceExchangeUtil {

  public static final String SEPARATOR = "~";
  public static final String YYYYMMDD = "yyyy-MM-dd";
  public static final DateTimeFormat FMT_YYYYMMDD = DateTimeFormat.getFormat(YYYYMMDD);

  public static String getBeginEndDate(BAcc1 acc1) {
    if (acc1==null ||(acc1.getBeginTime() == null && acc1.getEndTime() == null)) {
      return null;
    }

  if (acc1.getBeginTime() == null) {
      return SEPARATOR + FMT_YYYYMMDD.format(acc1.getEndTime());
    }

    if (acc1.getEndTime() == null) {
      return FMT_YYYYMMDD.format(acc1.getBeginTime()) + SEPARATOR;
    }

    return FMT_YYYYMMDD.format(acc1.getBeginTime()) + SEPARATOR
        + FMT_YYYYMMDD.format(acc1.getEndTime());
  }
  
  public static String formatAmount(BigDecimal amount){
    if(amount == null){
      return null;
    }
    return M3Format.fmt_money.format(amount);
  }

}
