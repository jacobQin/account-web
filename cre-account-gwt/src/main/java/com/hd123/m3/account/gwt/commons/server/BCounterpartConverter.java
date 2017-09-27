/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	BCounterpartConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015-11-2 - xiongzhimin - 创建。
 */
package com.hd123.m3.account.gwt.commons.server;

import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.investment.service.counterpart.Counterpart;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author xiongzhimin
 * 
 */
public class BCounterpartConverter implements Converter<Counterpart, BCounterpart> {
  
  private static BCounterpartConverter instance;

  public static BCounterpartConverter getInstance() {
    if (instance == null)
      instance = new BCounterpartConverter();
    return instance;
  }


  @Override
  public BCounterpart convert(Counterpart source) throws ConversionException {
    if (source == null)
      return null;
    BCounterpart target = new BCounterpart();
    target.setCode(source.getCode());
    target.setCounterpartType(source.getModule());
    target.setName(source.getName());
    target.setUuid(source.getUuid());
    return target;
  }

  public BCounterpart convert(UCN source,String counterpartType){
    if (source == null)
      return null;
    BCounterpart target = new BCounterpart();
    target.setCode(source.getCode());
    target.setCounterpartType(counterpartType);
    target.setName(source.getName());
    target.setUuid(source.getUuid());
    return target;
  }
  
  
}
