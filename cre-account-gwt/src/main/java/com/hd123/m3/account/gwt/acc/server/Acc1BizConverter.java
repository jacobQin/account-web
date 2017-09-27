/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	Acc1BizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.acc.server;

import com.hd123.m3.account.gwt.acc.client.BAcc1;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.commons.server.SourceBillBizConverter;
import com.hd123.m3.account.service.acc.Acc1;
import com.hd123.m3.commons.gwt.base.server.tax.TaxRateBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author chenwenfeng
 * 
 */
public class Acc1BizConverter implements Converter<Acc1, BAcc1> {

  private static Acc1BizConverter instance = null;

  public static Acc1BizConverter getInstance() {
    if (instance == null)
      instance = new Acc1BizConverter();
    return instance;
  }

  @Override
  public BAcc1 convert(Acc1 source) throws ConversionException {
    if (source == null)
      return null;

    BAcc1 target = new BAcc1();
    target.setAccountDate(source.getAccountDate());
    target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
    target.setBeginTime(source.getBeginTime());
    target.setContract(UCNBizConverter.getInstance().convert(source.getContract()));
    target.setCounterpart(BCounterpartConverter.getInstance().convert(source.getCounterpart(),
        source.getCounterpartType()));
    target.setDirection(source.getDirection());
    target.setEndTime(source.getEndTime());
    target.setId(source.getId());
    target.setOcrTime(source.getOcrTime());
    target.setRemark(source.getRemark());
    target.setSourceBill(SourceBillBizConverter.getInstance().convert(source.getSourceBill()));
    target.setSubject(UCNBizConverter.getInstance().convert(source.getSubject()));
    target.setTaxRate(TaxRateBizConverter.getInstance().convert(source.getTaxRate()));
    return target;
  }
}
