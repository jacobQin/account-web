/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	DepositRepaymentBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.commons.server.converter;

import com.hd123.m3.account.gwt.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.commons.server.AccStandardBillBizConverter;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.service.depositrepayment.DepositRepayment;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author chenpeisi
 * 
 */
public class DepositRepaymentBizConverter extends
    AccStandardBillBizConverter<DepositRepayment, BDepositRepayment> {

  public static DepositRepaymentBizConverter instance = null;

  public static DepositRepaymentBizConverter getInstance() {
    if (instance == null)
      instance = new DepositRepaymentBizConverter();
    return instance;
  }

  @Override
  public BDepositRepayment convert(DepositRepayment source) throws ConversionException {
    if (source == null)
      return null;

    try {
      BDepositRepayment target = new BDepositRepayment();
      inject(source, target);

      target.setUuid(source.getUuid());
      target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
      if (source.getBankCode() != null && source.getBankName() != null) {
        target.setBank(new BBank());
        target.getBank().setCode(source.getBankCode());
        target.getBank().setName(source.getBankName());
        target.getBank().setBankAccount(source.getBankAccount());
      }
      target.setDealer(new BUCN(source.getDealer(), null, null));

      target.setBillNumber(source.getBillNumber());
      target.setContract(UCNBizConverter.getInstance().convert(source.getContract()));
      target.setCounterContact(source.getCounterContact());
      target.setCounterpart(BCounterpartConverter.getInstance().convert(source.getCounterpart(),
          source.getCounterpartType()));
      target.setDirection(source.getDirection());
      target.setPaymentType(UCNBizConverter.getInstance().convert(source.getPaymentType()));
      target.setRemark(source.getRemark());
      target.setRepaymentDate(source.getRepaymentDate());
      target.setAccountDate(source.getAccountDate());
      target.setSettleNo(source.getSettleNo());
      target.setRepaymentTotal(source.getRepaymentTotal());
      target.setVersion(source.getVersion());
      
      target.setDeposit(UCNBizConverter.getInstance().convert(source.getDeposit()));

      target.setLines(ConverterUtil.convert(source.getLines(),
          DepositRepaymentLineBizConverter.getInstnce()));

      return target;
    } catch (Exception e) {
      throw new ConversionException(e.getMessage());
    }
  }
}
