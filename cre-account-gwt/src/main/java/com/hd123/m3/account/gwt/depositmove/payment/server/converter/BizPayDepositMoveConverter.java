/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BizPayDepositMoveConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-10 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.payment.server.converter;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.gwt.commons.server.BizAccStandardBillConverter;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BDepositMove;
import com.hd123.m3.account.service.depositmove.DepositMove;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author zhuhairui
 * 
 */
public class BizPayDepositMoveConverter extends
    BizAccStandardBillConverter<BDepositMove, DepositMove> {
  private static BizPayDepositMoveConverter instance;

  public static BizPayDepositMoveConverter getInstance() {
    if (instance == null)
      instance = new BizPayDepositMoveConverter();
    return instance;
  }

  @Override
  public DepositMove convert(BDepositMove source) throws ConversionException {
    if (source == null)
      return null;

    DepositMove target = new DepositMove();
    super.inject(source, target);
    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    target.setAmount(source.getAmount());
    target.setDirection(Direction.PAYMENT);
    target.setInContract(BizUCNConverter.getInstance().convert(source.getInContract()));
    target.setInCounterpart(BizUCNConverter.getInstance().convert(source.getInCounterpart()));
    target.setInCounterpartType(source.getInCounterpart() == null ? null : source
        .getInCounterpart().getCounterpartType());
    target.setInSubject(BizUCNConverter.getInstance().convert(source.getInSubject()));
    target.setOutContract(BizUCNConverter.getInstance().convert(source.getOutContract()));
    target.setOutCounterpart(BizUCNConverter.getInstance().convert(source.getOutCounterpart()));
    target.setOutCounterpartType(source.getOutCounterpart() == null ? null : source
        .getOutCounterpart().getCounterpartType());
    target.setOutSubject(BizUCNConverter.getInstance().convert(source.getOutSubject()));
    target.setRemark(source.getRemark());
    target.setVersion(source.getVersion());
    return target;
  }

}
