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

import com.hd123.m3.account.gwt.commons.server.BizAccStandardBillConverter;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.service.depositrepayment.DepositRepayment;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.ConverterUtil;

/**
 * @author chenpeisi
 * 
 */
public class BizDepositRepaymentConverter extends
    BizAccStandardBillConverter<BDepositRepayment, DepositRepayment> {

  public static BizDepositRepaymentConverter instance = null;

  public static BizDepositRepaymentConverter getInstance() {
    if (instance == null)
      instance = new BizDepositRepaymentConverter();
    return instance;
  }

  @Override
  public DepositRepayment convert(BDepositRepayment source) throws ConversionException {
    if (source == null)
      return null;

    try {
      DepositRepayment target = new DepositRepayment();
      inject(source, target);

      target.setUuid(source.getUuid());
      target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
      if (source.getBank() != null) {
        target.setBankAccount(source.getBank().getBankAccount());
        target.setBankCode(source.getBank().getCode());
        target.setBankName(source.getBank().getName());
      } else {
        target.setBankAccount(null);
        target.setBankCode(null);
        target.setBankName(null);
      }
      target.setDealer(source.getDealer() == null ? null : source.getDealer().getUuid());

      target.setBillNumber(source.getBillNumber());
      target.setContract(BizUCNConverter.getInstance().convert(source.getContract()));
      target.setCounterContact(source.getCounterContact());
      UCN counterpart = new UCN();
      if (source.getCounterpart() != null) {
        counterpart.setCode(source.getCounterpart().getCode());
        counterpart.setName(source.getCounterpart().getName());
        counterpart.setUuid(source.getCounterpart().getUuid());
      }
      target.setCounterpart(counterpart);
      target.setCounterpartType(source.getCounterpart() == null ? null : source.getCounterpart()
          .getCounterpartType());
      target.setDirection(source.getDirection());
      target.setPaymentType(BizUCNConverter.getInstance().convert(source.getPaymentType()));
      target.setRemark(source.getRemark());
      target.setRepaymentDate(source.getRepaymentDate());
      target.setAccountDate(source.getAccountDate());
      target.setSettleNo(source.getSettleNo());
      target.setRepaymentTotal(source.getRepaymentTotal());
      target.setVersion(source.getVersion());

      target.setLines(ConverterUtil.convert(source.getLines(),
          BizDepositRepaymentLineConverter.getInstnce()));

      return target;
    } catch (Exception e) {
      throw new ConversionException(e.getMessage());
    }
  }
}
