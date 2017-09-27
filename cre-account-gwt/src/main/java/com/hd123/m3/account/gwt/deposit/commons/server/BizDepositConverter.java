/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BizDepositConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-20 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.commons.server;

import com.hd123.m3.account.gwt.commons.server.BizAccStandardBillConverter;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.service.deposit.Deposit;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.ConverterUtil;

/**
 * @author chenpeisi
 * 
 */
public class BizDepositConverter extends BizAccStandardBillConverter<BDeposit, Deposit> {
  private static BizDepositConverter instance = null;

  public static BizDepositConverter getInstance() {
    if (instance == null)
      instance = new BizDepositConverter();
    return instance;
  }

  @Override
  public Deposit convert(BDeposit source) throws ConversionException {
    assert source != null;

    Deposit target = new Deposit();
    super.inject(source, target);

    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    target.setRemark(source.getRemark());
    target.setVersion(source.getVersion());
    target.setDirection(source.getDirection());
    target.setCounterpart(BizUCNConverter.getInstance().convert(source.getCounterpart()));
    target.setCounterpartType(source.getCounterpart() == null ? null : source.getCounterpart()
        .getCounterpartType());
    target.setDealer(source.getDealer() == null ? null : source.getDealer().getUuid());
    target.setCounterContact(source.getCounterContact());
    target.setDepositDate(source.getDepositDate());
    target.setDepositTotal(source.getDepositTotal());
    target.setAccountDate(source.getAccountDate());
    target.setPaymentType(BizUCNConverter.getInstance().convert(source.getPaymentType()));
    target.setContract(BizUCNConverter.getInstance().convert(source.getContract()));
    if (source.getBank() != null) {
      target.setBankAccount(source.getBank().getBankAccount());
      target.setBankCode(source.getBank().getCode());
      target.setBankName(source.getBank().getName());
    }
    target
        .setLines(ConverterUtil.convert(source.getLines(), BizDepositLineConverter.getInstance()));

    return target;
  }
}
