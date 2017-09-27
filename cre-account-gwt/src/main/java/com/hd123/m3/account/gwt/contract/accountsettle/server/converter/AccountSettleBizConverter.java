/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountSettleBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-11 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.contract.accountsettle.server.converter;

import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.contract.accountsettle.client.biz.BAccountSettle;
import com.hd123.m3.account.service.contract.AccountSettle;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * 账单出账界面对象转换器<br>
 * AccountSettle->BAccountSettle
 * 
 * @author huangjunxian
 * 
 */
public class AccountSettleBizConverter implements Converter<AccountSettle, BAccountSettle> {
  private static AccountSettleBizConverter instance = null;

  public static AccountSettleBizConverter getInstance() {
    if (instance == null)
      instance = new AccountSettleBizConverter();
    return instance;
  }

  private AccountSettleBizConverter() {
    super();
  }

  @Override
  public BAccountSettle convert(AccountSettle source) {
    if (source == null)
      return null;
    try {

      BAccountSettle target = new BAccountSettle();
      target.inject(source);
      target.setUuid(source.getUuid());
      if (source.getContract() != null) {
        target.setCounterpart(BCounterpartConverter.getInstance().convert(
            source.getContract().getCounterpart(), source.getContract().getCounterpartType()));
        target.setContractUuid(source.getContract().getUuid());
        target.setContractNumber(source.getContract().getBillNumber());
        target.setContractTitle(source.getContract().getTitle());
        target.setFloor(UCNBizConverter.getInstance().convert(source.getContract().getFloor()));
        target.setCoopMode(source.getContract().getCoopMode());
      }
      if (source.getSettlement() != null) {
        target.setSettlementUuid(source.getSettlement().getUuid());
        target.setSettleName(source.getSettlement().getCaption());
      }
      target.setBeginDate(source.getBeginDate());
      target.setEndDate(source.getEndDate());
      target.setAccountTime(source.getPlanDate());
      if (source.getSettlement() != null && source.getSettlement().getBillCalculateType() != null)
        target.setBillCalcType(source.getSettlement().getBillCalculateType().name());

      return target;
    } catch (Exception e) {
      throw new ConversionException(e);
    }
  }

}
