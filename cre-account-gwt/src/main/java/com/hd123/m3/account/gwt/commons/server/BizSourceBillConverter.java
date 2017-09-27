/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BizSourceBillConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.commons.server;

import com.hd123.m3.account.commons.SourceBill;
import com.hd123.m3.account.gwt.commons.client.biz.BSourceBill;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author chenwenfeng
 * 
 */
public class BizSourceBillConverter implements Converter<BSourceBill, SourceBill> {
  private static BizSourceBillConverter instance = null;

  public static BizSourceBillConverter getInstance() {
    if (instance == null)
      instance = new BizSourceBillConverter();
    return instance;
  }

  @Override
  public SourceBill convert(BSourceBill source) throws ConversionException {
    if (source == null)
      return null;
    SourceBill target = new SourceBill();
    target.setBillNumber(source.getBillNumber());
    target.setBillType(source.getBillType());
    target.setBillUuid(source.getBillUuid());
    return target;
  }

}
