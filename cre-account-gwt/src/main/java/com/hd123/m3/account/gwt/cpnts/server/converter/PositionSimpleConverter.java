/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	PositionSimpleConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-1-26 - chenrizhang- 创建。
 */
package com.hd123.m3.account.gwt.cpnts.server.converter;

import com.hd123.m3.account.gwt.cpnts.client.ui.position.SPosition;
import com.hd123.m3.commons.gwt.base.server.convert.EntityBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.m3.investment.service.res.position.Position;

/**
 * @author chenrizhang
 * 
 */
public class PositionSimpleConverter extends EntityBizConverter<Position, SPosition> {
  private static PositionSimpleConverter instance = null;

  public static PositionSimpleConverter getInstance() {
    if (instance == null)
      instance = new PositionSimpleConverter();
    return instance;
  }

  private PositionSimpleConverter() {
    super();
  }

  @Override
  public SPosition convert(Position source) {
    if (source == null)
      return null;
    SPosition target = new SPosition();

    super.inject(source, target);

    target.setCode(source.getCode());
    target.setName(source.getName());
    target.setStore(UCNBizConverter.getInstance().convert(source.getStore()));
    target.setPositionType(source.getPositionType().name());
    target.setPositionSubType(source.getSubType());

    target.setBuilding(UCNBizConverter.getInstance().convert(source.getBuilding()));
    target.setFloor(UCNBizConverter.getInstance().convert(source.getFloor()));
    target.setRentArea(source.getRentArea());

    return target;
  }

}
