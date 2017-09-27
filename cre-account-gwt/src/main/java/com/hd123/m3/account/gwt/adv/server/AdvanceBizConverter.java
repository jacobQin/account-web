/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AdvanceBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.adv.server;

import com.hd123.m3.account.gwt.adv.client.biz.BAdvance;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.service.adv.Advance;
import com.hd123.m3.commons.gwt.base.server.convert.EntityBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * 预存款账户转换器
 * <p>
 * Advance->BAdvance
 * 
 * @author zhuhairui
 * 
 */
public class AdvanceBizConverter extends EntityBizConverter<Advance, BAdvance> {

  public static AdvanceBizConverter getInstance() {
    if (instance == null)
      instance = new AdvanceBizConverter();
    return instance;
  }

  public AdvanceBizConverter() {
  }

  @Override
  public BAdvance convert(Advance source) throws ConversionException {
    if (source == null)
      return null;
    BAdvance target = new BAdvance();
    target.setUuid(source.getUuid());
    target.setSubject(UCNBizConverter.getInstance().convert(source.getSubject()));
    target.setContract(UCNBizConverter.getInstance().convert(source.getBill()));
    target.setTotal(source.getTotal());
    target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
    target.setCounterpart(BCounterpartConverter.getInstance().convert(source.getCounterpart(),
        source.getCounterpartType()));
    return target;
  }

  private static AdvanceBizConverter instance;
}
