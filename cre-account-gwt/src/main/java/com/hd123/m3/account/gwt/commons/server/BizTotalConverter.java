/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BizTotalConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.commons.server;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author chenwenfeng
 * 
 */
public class BizTotalConverter implements Converter<BTotal, Total> {
  private static BizTotalConverter instance = null;

  public static BizTotalConverter getInstance() {
    if (instance == null)
      instance = new BizTotalConverter();
    return instance;
  }

  @Override
  public Total convert(BTotal source) throws ConversionException {
    if (source == null)
      return null;
    Total target = new Total();
    target.setTax(source.getTax());
    target.setTotal(source.getTotal());
    return target;
  }

}
