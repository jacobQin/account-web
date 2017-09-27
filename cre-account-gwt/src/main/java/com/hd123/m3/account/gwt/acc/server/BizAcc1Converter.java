/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BizAcc1Converter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.acc.server;

import com.hd123.m3.account.gwt.acc.client.BAcc1;
import com.hd123.m3.account.gwt.commons.server.BizSourceBillConverter;
import com.hd123.m3.account.service.acc.Acc1;
import com.hd123.m3.commons.gwt.base.server.tax.BizTaxRateConverter;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author chenwenfeng
 * 
 */
public class BizAcc1Converter implements Converter<BAcc1, Acc1> {

  private static BizAcc1Converter instance = null;

  public static BizAcc1Converter getInstance() {
    if (instance == null)
      instance = new BizAcc1Converter();
    return instance;
  }

  @Override
  public Acc1 convert(BAcc1 source) throws ConversionException {
    if (source == null)
      return null;
    Acc1 target = new Acc1();
    target.setAccountDate(source.getAccountDate());
    target.setAccountUnit(new UCN(source.getAccountUnit().getUuid(), source.getAccountUnit()
        .getCode(), source.getAccountUnit().getName()));
    target.setBeginTime(source.getBeginTime());
    target.setContract(new UCN(source.getContract().getUuid(), source.getContract().getCode(),
        source.getContract().getName()));
    target.setCounterpart(new UCN(source.getCounterpart().getUuid(), source.getCounterpart()
        .getCode(), source.getCounterpart().getName()));
    target.setCounterpartType(source.getCounterpart().getCounterpartType());
    target.setDirection(source.getDirection());
    target.setEndTime(source.getEndTime());
    target.setId(source.getId());
    target.setOcrTime(source.getOcrTime());
    target.setRemark(source.getRemark());
    target.setSourceBill(BizSourceBillConverter.getInstance().convert(source.getSourceBill()));
    target.setSubject(new UCN(source.getSubject().getUuid(), source.getSubject().getCode(), source
        .getSubject().getName()));
    target.setTaxRate(BizTaxRateConverter.getInstance().convert(source.getTaxRate()));
    return target;
  }
}
