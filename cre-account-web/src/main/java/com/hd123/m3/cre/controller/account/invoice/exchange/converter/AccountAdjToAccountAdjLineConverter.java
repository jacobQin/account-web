/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	AccountAdjToAccountAdjLineConverter.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月29日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.exchange.converter;

import com.hd123.m3.account.service.acc.AccountAdj;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchAccountAdjLine;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * AccountAdj -> InvoiceExchAccountAdjLine
 * 
 * @author wangyibo
 *
 */
public class AccountAdjToAccountAdjLineConverter implements Converter<AccountAdj, InvoiceExchAccountAdjLine>{
  
  private static AccountAdjToAccountAdjLineConverter instance = null;
  
  public static AccountAdjToAccountAdjLineConverter getInstance() {
    if(instance == null) {
      instance = new AccountAdjToAccountAdjLineConverter();
    }
    return instance;
  }
  
  @Override
  public InvoiceExchAccountAdjLine convert(AccountAdj source) throws ConversionException {
    if(source == null) {
      return null;      
    }
    
    InvoiceExchAccountAdjLine target = new InvoiceExchAccountAdjLine();
    target.setAcc1(source.getAcc1());
    target.setAcc2(source.getAcc2());
    target.setTotal(source.getTotal());
    
    target.setAdjId(source.getAdjId());
    target.setAdjRemark(source.getAdjRemark());
    target.setAdjTime(source.getAdjTime());
    
    return target;
  }

}
