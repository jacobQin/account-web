/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BillTypeBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-4 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.commons.server;

import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.service.billtype.BillType;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author chenpeisi
 * 
 */
public class BillTypeBizConverter implements Converter<BillType, BBillType> {

  private static BillTypeBizConverter instance = null;

  public static BillTypeBizConverter getInstance() {
    if (instance == null)
      instance = new BillTypeBizConverter();
    return instance;
  }

  @Override
  public BBillType convert(BillType source) throws ConversionException {
    if (source == null)
      return null;
    try {
      BBillType target = new BBillType();
      target.setCaption(source.getCaption());
      target.setClassName(source.getClassName());
      target.setDirection(source.getDirection());
      target.setName(source.getName());
      return target;
    } catch (Exception e) {
      throw new ConversionException(e);
    }
  }
}
