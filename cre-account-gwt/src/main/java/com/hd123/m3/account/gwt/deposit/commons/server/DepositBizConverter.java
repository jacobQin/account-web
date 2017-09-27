/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： BCollectionConverter.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-23 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.commons.server;

import com.hd123.m3.account.gwt.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.commons.server.AccStandardBillBizConverter;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.service.deposit.Deposit;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.BUCN;

public class DepositBizConverter extends AccStandardBillBizConverter<Deposit, BDeposit> {

  private static DepositBizConverter instance = null;

  public static DepositBizConverter getInstance() {
    if (instance == null)
      instance = new DepositBizConverter();
    return instance;
  }

  @Override
  public BDeposit convert(Deposit source) throws ConversionException {
    if (source == null)
      return null;
    try {
      BDeposit target = new BDeposit();
      inject(source, target);

      target.setUuid(source.getUuid());
      target.setDealer(new BUCN(source.getDealer(), null, null));
      target.setRemark(source.getRemark());
      target.setBillNumber(source.getBillNumber());
      target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
      target.setCounterpart(BCounterpartConverter.getInstance().convert(source.getCounterpart(),
          source.getCounterpartType()));
      target.setContract(UCNBizConverter.getInstance().convert(source.getContract()));
      target.setPaymentType(UCNBizConverter.getInstance().convert(source.getPaymentType()));

      if (source.getBankCode() != null && source.getBankName() != null) {
        target.setBank(new BBank());
        target.getBank().setCode(source.getBankCode());
        target.getBank().setName(source.getBankName());
        target.getBank().setBankAccount(source.getBankAccount());
      }
      target.setCounterContact(source.getCounterContact());
      target.setSettleNo(source.getSettleNo());

      target.setDirection(source.getDirection());
      target.setDepositTotal(source.getDepositTotal());
      target.setDepositDate(source.getDepositDate());
      target.setAccountDate(source.getAccountDate());
      target.setLines(ConverterUtil.convert(source.getLines(),
          DepositLineBizConverter.getInstance()));

      return target;
    } catch (Exception e) {
      throw new ConversionException(e);
    }
  }
}
