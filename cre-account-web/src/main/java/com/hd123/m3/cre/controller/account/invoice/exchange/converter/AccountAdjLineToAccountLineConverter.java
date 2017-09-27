/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	AccountAdjLineToAccountLineConverter.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月28日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.exchange.converter;

import com.hd123.m3.account.service.ivc.exchange.InvoiceExchAccountAdjLine;
import com.hd123.m3.cre.controller.account.invoice.exchange.biz.BInvoiceExchAccountLine;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * 
 * InvoiceExchAccountAdjLine -> BInvoiceExchAccountLine
 * 
 * @author wangyibo
 *
 */
public class AccountAdjLineToAccountLineConverter implements Converter<InvoiceExchAccountAdjLine, BInvoiceExchAccountLine>{

  private static AccountAdjLineToAccountLineConverter instance = null;
  
  public static AccountAdjLineToAccountLineConverter getInstance() {
    if(instance == null) {
      instance = new AccountAdjLineToAccountLineConverter();
    }
    return instance;
  }
  
  @Override
  public BInvoiceExchAccountLine convert(InvoiceExchAccountAdjLine source)
      throws ConversionException {
    if(source == null) {
      return null;
    }
    
    BInvoiceExchAccountLine target = new BInvoiceExchAccountLine();
    
    target.setAcc1(source.getAcc1());
    target.setAcc2(source.getAcc2());
    target.setAmount(source.getTotal().getTotal());
    target.setOldCode(source.getAcc2().getInvoice().getInvoiceCode());
    target.setOldNumber(source.getAcc2().getInvoice().getInvoiceNumber());
    
    return target;
  }

}
