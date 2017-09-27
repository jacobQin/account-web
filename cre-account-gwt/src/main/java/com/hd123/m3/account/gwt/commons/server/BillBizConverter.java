/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BBillConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.commons.server;

import com.hd123.m3.account.commons.Bill;
import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author chenwenfeng
 * 
 */
public class BillBizConverter implements Converter<Bill, BBill> {
  private static BillBizConverter instance = null;

  public static BillBizConverter getInstance() {
    if (instance == null)
      instance = new BillBizConverter();
    return instance;
  }

  public BillBizConverter() {
  }

  @Override
  public BBill convert(Bill source) throws ConversionException {
    if (source == null)
      return null;

    BBill target = new BBill();
    target.setBillUuid(source.getBillUuid());
    target.setBillNumber(source.getBillNumber());
    return target;
  }

}
