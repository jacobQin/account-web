/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	DepositMoveBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-02-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.commons.server.converter;

import com.hd123.m3.account.gwt.commons.server.AccStandardBillBizConverter;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BDepositMove;
import com.hd123.m3.account.service.depositmove.DepositMove;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author zhuhairui
 * 
 */
public class DepositMoveBizConverter extends AccStandardBillBizConverter<DepositMove, BDepositMove> {
  public static DepositMoveBizConverter instance = null;

  public static DepositMoveBizConverter getInstance() {
    if (instance == null)
      instance = new DepositMoveBizConverter();
    return instance;
  }

  @Override
  public BDepositMove convert(DepositMove source) throws ConversionException {
    if (source == null)
      return null;
    try {
      BDepositMove target = new BDepositMove();
      inject(source, target);

      target.setAmount(source.getAmount());
      target.setBillNumber(source.getBillNumber());
      target.setDirection(source.getDirection());
      target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
      target.setInContract(UCNBizConverter.getInstance().convert(source.getInContract()));
      target.setInCounterpart(BCounterpartConverter.getInstance().convert(
          source.getInCounterpart(), source.getInCounterpartType()));
      target.setInSubject(UCNBizConverter.getInstance().convert(source.getInSubject()));
      target.setOutContract(UCNBizConverter.getInstance().convert(source.getOutContract()));
      target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
      target.setOutCounterpart(BCounterpartConverter.getInstance().convert(
          source.getOutCounterpart(), source.getOutCounterpartType()));
      target.setOutSubject(UCNBizConverter.getInstance().convert(source.getOutSubject()));
      target.setRemark(source.getRemark());
      target.setSettleNo(source.getSettleNo());
      target.setBizState(source.getBizState());
      target.setUuid(source.getUuid());
      target.setVersion(source.getVersion());
      target.setAccountDate(source.getAccountDate());

      return target;

    } catch (Exception e) {
      throw new ConversionException(e);
    }
  }
}
