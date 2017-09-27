/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-common
 * 文件名： BMonthSettleConverter.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-18 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.server.converter;

import com.hd123.m3.account.gwt.cpnts.client.biz.BMonthSettle;
import com.hd123.m3.account.service.monthsettle.MonthSettle;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author subinzhu
 * 
 */
public class MonthSettleBizConverter implements Converter<MonthSettle, BMonthSettle> {
  private static MonthSettleBizConverter instance;

  public static MonthSettleBizConverter getInstance() {
    if (instance == null)
      instance = new MonthSettleBizConverter();
    return instance;
  }

  public BMonthSettle convert(MonthSettle source) {
    if (source == null)
      return null;
    BMonthSettle target = new BMonthSettle();
    target.inject(source);

    target.setNumber(source.getNumber());
    target.setBeginTime(source.getBeginTime());
    target.setEndTime(source.getEndTime());
    target.setCreateTime(source.getCreateTime());

    return target;
  }

}
