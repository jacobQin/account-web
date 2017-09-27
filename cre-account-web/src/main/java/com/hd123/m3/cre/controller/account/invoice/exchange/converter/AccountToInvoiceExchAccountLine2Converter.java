/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	AccountToInvoiceExchAccountLine2Converter.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月28日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.exchange.converter;

import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.cre.controller.account.invoice.exchange.biz.InvoiceExchAccountLine2;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * Account -> InvoiceExchAccountLine2
 * 
 * @author wangyibo
 *
 */
public class AccountToInvoiceExchAccountLine2Converter implements Converter<Account,InvoiceExchAccountLine2>{

  private static AccountToInvoiceExchAccountLine2Converter instance = null;
  
  public static AccountToInvoiceExchAccountLine2Converter getInstance() {
    if(instance == null) {
      instance = new AccountToInvoiceExchAccountLine2Converter();
    }
    return instance;
  }
  
  @Override
  public InvoiceExchAccountLine2 convert(Account source) throws ConversionException {
    if(source == null) {
      return null;      
    }
    
    InvoiceExchAccountLine2 line = new InvoiceExchAccountLine2();
    line.setAcc1(source.getAcc1());
    line.setAcc2(source.getAcc2());
    line.setOriginTotal(source.getOriginTotal());
    line.setTotal(source.getTotal());
    line.setOldCode(source.getAcc2().getInvoice().getInvoiceCode());
    line.setOldNumber(source.getAcc2().getInvoice().getInvoiceNumber());
    
    return line;
  }

}
