/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BizDepositMoveConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015-2-27 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.commons.server.converter;

import com.hd123.m3.account.gwt.commons.server.BizAccStandardBillConverter;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BDepositMove;
import com.hd123.m3.account.service.depositmove.DepositMove;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author chenpeisi
 * 
 * 
 */
public class BizDepositMoveConverter extends BizAccStandardBillConverter<BDepositMove, DepositMove> {
  public static BizDepositMoveConverter instance = null;

  public static BizDepositMoveConverter getInstance() {
    if (instance == null)
      instance = new BizDepositMoveConverter();
    return instance;
  }

  @Override
  public DepositMove convert(BDepositMove source) throws ConversionException {
    if (source == null)
      return null;
    try {
      DepositMove target = new DepositMove();
      inject(source, target);

      target.setAmount(source.getAmount());
      target.setBillNumber(source.getBillNumber());
      target.setDirection(source.getDirection());
      target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
      target.setInContract(BizUCNConverter.getInstance().convert(source.getInContract()));
      target.setInCounterpart(BizUCNConverter.getInstance().convert(source.getInCounterpart()));
      target.setInCounterpartType(source.getInCounterpart() == null ? null : source
          .getInCounterpart().getCounterpartType());
      target.setInSubject(BizUCNConverter.getInstance().convert(source.getInSubject()));
      target.setOutContract(BizUCNConverter.getInstance().convert(source.getOutContract()));
      target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
      target.setOutCounterpart(BizUCNConverter.getInstance().convert(source.getOutCounterpart()));
      target.setOutCounterpartType(source.getOutCounterpart() == null ? null : source
          .getOutCounterpart().getCounterpartType());
      target.setOutSubject(BizUCNConverter.getInstance().convert(source.getOutSubject()));
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
