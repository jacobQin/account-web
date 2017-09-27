/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	FreezeBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-26 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.freeze.server;

import java.math.BigDecimal;

import com.hd123.m3.account.gwt.acc.server.Acc1BizConverter;
import com.hd123.m3.account.gwt.acc.server.Acc2BizConverter;
import com.hd123.m3.account.gwt.commons.server.AccStandardBillBizConverter;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.freeze.client.biz.BFreeze;
import com.hd123.m3.account.gwt.freeze.client.biz.BFreezeLine;
import com.hd123.m3.account.service.freeze.Freeze;
import com.hd123.m3.account.service.freeze.FreezeLine;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.gwt.entity.server.BOperateInfoConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * 账款冻结单转换器
 * <p>
 * Freeze->BFreeze
 * 
 * @author zhuhairui
 * 
 */
public class FreezeBizConverter extends AccStandardBillBizConverter<Freeze, BFreeze> {

  private static FreezeBizConverter instance;

  public static FreezeBizConverter getInstance() {
    if (instance == null)
      instance = new FreezeBizConverter();
    return instance;
  }

  @Override
  public BFreeze convert(Freeze source) throws ConversionException {
    try {
      if (source == null)
        return null;
      BFreeze target = new BFreeze();
      super.inject(source, target);
      target.setUuid(source.getUuid());
      target.setVersion(source.getVersion());
      target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
      target.setBillNumber(source.getBillNumber());
      target.setState(source.getState() != null ? source.getState().name() : null);
      target.setCounterpart(BCounterpartConverter.getInstance().convert(source.getCounterpart(),
          source.getCounterpartType()));
      target.setFreezePayTotal(source.getPaymentTotal() != null ? source.getPaymentTotal()
          .getTotal() : BigDecimal.ZERO);
      target.setFreezePayTax(source.getPaymentTotal() != null ? source.getPaymentTotal().getTax()
          : BigDecimal.ZERO);
      target.setFreezeRecTotal(source.getReceiptTotal() != null ? source.getReceiptTotal()
          .getTotal() : BigDecimal.ZERO);
      target.setFreezeRecTax(source.getReceiptTotal() != null ? source.getReceiptTotal().getTax()
          : BigDecimal.ZERO);
      target.setFreezeInfo(new BOperateInfoConverter().convert(source.getFreezeInfo()));
      target.setUnfreezeInfo(new BOperateInfoConverter().convert(source.getUnfreezeInfo()));
      target.setFreezeReason(source.getFreezeReason());
      target.setUnfreezeReason(source.getUnfreezeReason());

      for (FreezeLine line : source.getLines()) {
        target.getLines().add(convertFreezeLine(line));
      }

      return target;
    } catch (Exception e) {
      throw new ConversionException(e);
    }
  }

  private BFreezeLine convertFreezeLine(FreezeLine source)  {
    if (source == null)
      return null;

    BFreezeLine target = new BFreezeLine();
    target.inject(source);
    target.setAcc1(Acc1BizConverter.getInstance().convert(source.getAcc1()));
    target.setAcc2(Acc2BizConverter.getInstance().convert(source.getAcc2()));
    target.setTotal(source.getTotal() != null ? source.getTotal().getTotal() : BigDecimal.ZERO);
    target.setTax(source.getTotal() != null ? source.getTotal().getTax() : BigDecimal.ZERO);
    return target;
  }

}
