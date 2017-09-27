/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AdvanceBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-02-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.commons.server.converter;

import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BAdvance;
import com.hd123.m3.account.service.adv.Advance;
import com.hd123.m3.account.service.adv.Advances;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author zhuhairui
 * 
 */
public class AdvanceBizConverter implements Converter<Advance, BAdvance> {
  private static AdvanceBizConverter instance;

  public static AdvanceBizConverter getInstance() {
    if (instance == null) {
      instance = new AdvanceBizConverter();
    }
    return instance;
  }

  @Override
  public BAdvance convert(Advance source) throws ConversionException {
    if (source == null)
      return null;
    try {
      BAdvance target = new BAdvance();
      if (source.getBill().getUuid().equals(Advances.NONE_BILL_UUID) == false)
        target.setContract(UCNBizConverter.getInstance().convert(source.getBill()));
      target.setSubject(UCNBizConverter.getInstance().convert(source.getSubject()));
      target.setTotal(source.getTotal());

      return target;

    } catch (Exception e) {
      throw new ConversionException(e);
    }
  }
}
