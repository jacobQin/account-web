/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	BizTypeBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月29日 - lizongyi - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.server.converter;

import com.hd123.m3.investment.service.biztype.BizType;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author lizongyi
 *
 */
public class BizTypeBizConverter implements Converter<BizType, BUCN> {

  @Override
  public BUCN convert(BizType source) throws ConversionException {
    if (source == null) {
      return null;
    }

    BUCN target = new BUCN();
    target.setUuid(source.getUuid());
    target.setCode(source.getCode());
    target.setName(source.getName());

    return target;
  }
}
