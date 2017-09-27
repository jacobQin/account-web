/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	AccountLine2ToAccountLineConverter.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月29日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.exchange.converter;

import com.hd123.m3.cre.controller.account.invoice.exchange.biz.BInvoiceExchAccountLine;
import com.hd123.m3.cre.controller.account.invoice.exchange.biz.InvoiceExchAccountLine2;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * InvoiceExchAccountLine2 -> BInvoiceExchAccountLine
 * @author wangyibo
 *
 */
public class AccountLine2ToAccountLineConverter implements Converter<InvoiceExchAccountLine2, BInvoiceExchAccountLine>{

  private static AccountLine2ToAccountLineConverter instance = null;
  
  public static AccountLine2ToAccountLineConverter getInstance() {
    if(instance == null) {
      instance = new AccountLine2ToAccountLineConverter();
    }
    return instance;
  }

  @Override
  public BInvoiceExchAccountLine convert(InvoiceExchAccountLine2 source) throws ConversionException {
    if(source == null) {
      return null;
    }
    
    BInvoiceExchAccountLine target = new BInvoiceExchAccountLine();
    target.setAcc1(source.getAcc1());
    target.setAcc2(source.getAcc2());
    target.setAmount(source.getTotal().getTotal());
    target.setNewCode(source.getNewCode());
    target.setNewNumber(source.getNewNumber());
    target.setNewType(source.getNewType());
    target.setOldCode(source.getOldCode());
    target.setOldNumber(source.getOldNumber());
    target.setRemark(source.getRemark());
    
    return target;
  }
  
}
