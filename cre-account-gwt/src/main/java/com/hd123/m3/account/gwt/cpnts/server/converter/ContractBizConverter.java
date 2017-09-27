/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	ContractBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-17 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.server.converter;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.service.contract.Contract;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author subinzhu
 * 
 */
public class ContractBizConverter implements Converter<Contract, BContract> {
  private static ContractBizConverter instance;

  public static ContractBizConverter getInstance() {
    if (instance == null) {
      instance = new ContractBizConverter();
    }
    return instance;
  }

  private ContractBizConverter() {
  }

  @Override
  public BContract convert(Contract source) throws ConversionException {
    if (source == null)
      return null;
    try {
      BContract target = new BContract();
      target.setUuid(source.getUuid());
      target.setBillNumber(source.getBillNumber());
      target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getBusinessUnit()));
      target.setCounterpart(BCounterpartConverter.getInstance().convert(source.getCounterpart(),
          source.getCounterpartType()));
      target.setTitle(source.getTitle());
      target.setFloor(UCNBizConverter.getInstance().convert(source.getFloor()));
      target.setCoopMode(source.getCoopMode());
      target.setContractCategory(source.getContractCategory());
      target.setBeginDate(source.getBeginDate());
      target.setEndDate(source.getEndDate());

      List<BUCN> positions = new ArrayList<BUCN>();
      for (UCN ucn : source.getPositions()) {
        BUCN position = UCNBizConverter.getInstance().convert(ucn);
        positions.add(position);
      }
      target.setPositions(positions);
      return target;
    } catch (Exception e) {
      throw new ConversionException(e);
    }
  }
}