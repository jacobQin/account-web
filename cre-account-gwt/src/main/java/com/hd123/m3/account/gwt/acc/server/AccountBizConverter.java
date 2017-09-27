/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BizAccountConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-8 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.acc.server;

import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.commons.server.TotalBizConverter;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.rumba.commons.gwt.entity.server.BEntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author chenwenfeng
 * 
 */
public class AccountBizConverter extends BEntityConverter<Account, BAccount> {

  private static AccountBizConverter instance = null;

  public static AccountBizConverter getInstance() {
    if (instance == null)
      instance = new AccountBizConverter();
    return instance;
  }

  @Override
  public BAccount convert(Account source) throws ConversionException {
    if (source == null)
      return null;

    BAccount target = new BAccount();
    inject(source, target);
    target.setAcc1(Acc1BizConverter.getInstance().convert(source.getAcc1()));
    target.setAcc2(Acc2BizConverter.getInstance().convert(source.getAcc2()));
    target.setOriginTotal(TotalBizConverter.getInstance().convert(source.getOriginTotal()));
    target.setTotal(TotalBizConverter.getInstance().convert(source.getTotal()));
    return target;
  }
}
