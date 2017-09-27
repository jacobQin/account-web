/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	CounterpartBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-17 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.server.converter;

import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpart;
import com.hd123.m3.investment.service.counterpart.Counterpart;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author subinzhu
 * 
 */
public class CounterpartBizConverter implements Converter<Counterpart, BCounterpart> {

  @Override
  public BCounterpart convert(Counterpart source) {
    if (source == null)
      return null;
    BCounterpart target = new BCounterpart();
    target.setUuid(source.getUuid());
    target.setCode(source.getCode());
    target.setName(source.getName());
    target.setModule(source.getModule());
    target.setModuleCaption(source.getModuleCaption());
    return target;
  }
}