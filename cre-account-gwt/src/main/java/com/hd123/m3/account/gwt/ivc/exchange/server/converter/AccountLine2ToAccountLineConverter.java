/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceExchAccountLine2ToBInvoiceExchAccountLineConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月24日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.server.converter;

import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchAccountLine;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchAccountLine2;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * BInvoiceExchAccountLine2 -> BInvoiceExchAccountLine
 * 
 * @author LiBin
 * @since  1.7
 *
 */
public class AccountLine2ToAccountLineConverter implements Converter<BInvoiceExchAccountLine2, BInvoiceExchAccountLine> {

  private static AccountLine2ToAccountLineConverter instance=null;
  
  public static AccountLine2ToAccountLineConverter getInstance(){
    if(instance == null){
      instance = new AccountLine2ToAccountLineConverter();
    }
    return instance;
  }
  @Override
  public BInvoiceExchAccountLine convert(BInvoiceExchAccountLine2 source)
      throws ConversionException {
    if(source == null){
      return null;
    }
    
    BInvoiceExchAccountLine target = new BInvoiceExchAccountLine();
    target.setAcc1(source.getAcc1());
    target.setAcc2(source.getAcc2());
    target.setAmount(source.getTotal().getTotal());
    target.setNewCode(source.getNewCode());
    target.setNewNumber(source.getNewNumber());
    target.setNewInvoiceType(source.getNewType());
    target.setOldCode(source.getOldCode());
    target.setOldNumber(source.getOldNumber());
    target.setRemark(source.getRemark());
    
    return target;
  }

}
