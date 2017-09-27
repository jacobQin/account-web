/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AccountSettleBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014年9月22日 - zhr - 创建。
 */
package com.hd123.m3.account.gwt.statement.server.converter;

import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.statement.client.biz.BAccountSettleLog;
import com.hd123.m3.account.service.contract.AccountSettle;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author zhr
 * 
 */
public class AccountSettleLogBizConverter implements Converter<AccountSettle, BAccountSettleLog> {

  private static AccountSettleLogBizConverter instance;

  public static AccountSettleLogBizConverter getInstance() {
    if (instance == null)
      instance = new AccountSettleLogBizConverter();
    return instance;
  }

  @Override
  public BAccountSettleLog convert(AccountSettle source) throws ConversionException {
    if (source == null)
      return null;

    BAccountSettleLog target = new BAccountSettleLog();
    target.setUuid(source.getUuid());
    target.setAccountUnit(source.getSettlement() == null ? null : source.getSettlement()
        .getAccountUnit() == null ? null : UCNBizConverter.getInstance().convert(
        source.getSettlement().getAccountUnit()));
    target.setCountpart(source.getContract() == null ? null
        : source.getContract().getCounterpart() == null ? null : BCounterpartConverter
            .getInstance().convert(source.getContract().getCounterpart(),
                source.getContract().getCounterpartType()));
    target.setContractTitle(source.getContract() == null ? null : source.getContract().getTitle());
    target.setContractBillNumber(source.getContract() == null ? null : source.getContract()
        .getBillNumber());
    target.setFloor(source.getContract() == null ? null
        : source.getContract().getFloor() == null ? null : UCNBizConverter.getInstance().convert(
            source.getContract().getFloor()));
    target.setCoopMode(source.getContract() == null ? null : source.getContract().getCoopMode());
    target.setSettlementCaption(source.getSettlement() == null ? null : source.getSettlement()
        .getCaption());
    target.setSettlementDateRange(new BDateRange(source.getBeginDate(), source.getEndDate()));
    target.setPlanDate(source.getPlanDate());
    target.setAccountTime(source.getAccountTime());
    target.setBillCalculateType(source.getSettlement() == null ? null : source.getSettlement()
        .getBillCalculateType().name());
    target.setStatementBillNumber(source.getBill() == null ? null : source.getBill()
        .getBillNumber());
    target.setEmpty(Contracts.EMPTY_BILL_UUID.equals(source.getBill().getBillUuid()));
    return target;
  }
}
