/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	PositionBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年1月29日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.server.converter;

import com.hd123.m3.account.gwt.cpnts.client.biz.TypeBUCN;
import com.hd123.m3.investment.service.res.position.Position;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * Position -> SPosition
 * 
 * @author LiBin
 * 
 */
public class PositionBizConverter implements Converter<Position, TypeBUCN> {

  @Override
  public TypeBUCN convert(Position source) throws ConversionException {
    if (source == null) {
      return null;
    }
    
    TypeBUCN target = new TypeBUCN();
    target.setUuid(source.getUuid());
    target.setCode(source.getCode());
    target.setName(source.getName());

    return target;
  }

}
