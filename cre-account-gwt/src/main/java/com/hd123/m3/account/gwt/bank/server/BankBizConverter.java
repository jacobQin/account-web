/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2010，所有权利保留。
 * 
 * 项目名：	M3
 * 文件名：	BBankConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-817 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.bank.server;

import com.hd123.m3.account.gwt.bank.client.biz.BBank;
import com.hd123.m3.account.service.bank.Bank;
import com.hd123.rumba.commons.gwt.entity.server.BStandardEntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author chenrizhang
 * 
 */
public class BankBizConverter extends BStandardEntityConverter<Bank, BBank> {

  private static BankBizConverter instance = null;

  public static BankBizConverter getInstance() {
    if (instance == null)
      instance = new BankBizConverter();
    return instance;
  }

  private BankBizConverter() {
    super();
  }

  @Override
  public BBank convert(Bank source) throws ConversionException {
    if (source == null)
      return null;

    BBank target = new BBank();
    inject(source, target);
    target.setStore(source.getStore() == null ? null : new BUCN(source.getStore().getUuid(), source
        .getStore().getCode(), source.getStore().getName()));
    target.setCode(source.getCode());
    target.setName(source.getName());
    target.setBank(source.getBank());
    target.setAccount(source.getAccount());
    target.setAddress(source.getAddress());
    target.setRemark(source.getRemark());

    return target;
  }
}