/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	AccountTo.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月24日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.server.converter;

import com.hd123.m3.account.gwt.acc.server.Acc1BizConverter;
import com.hd123.m3.account.gwt.acc.server.Acc2BizConverter;
import com.hd123.m3.account.gwt.commons.server.TotalBizConverter;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchAccountLine2;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * Account -> BInvoiceExchAccountLine2
 * 
 * @author LiBin
 * @since  1.7
 *
 */
public class AccountToBInvoiceExchAccountLine2Converter implements Converter<Account,BInvoiceExchAccountLine2> {

  private static AccountToBInvoiceExchAccountLine2Converter instance=null;
  
  public static AccountToBInvoiceExchAccountLine2Converter getInstance(){
    if(instance == null){
      instance = new AccountToBInvoiceExchAccountLine2Converter();
    }
    return instance;
  }
  
  @Override
  public BInvoiceExchAccountLine2 convert(Account source) throws ConversionException {
    if(source == null){
      return null;
    }
    
    BInvoiceExchAccountLine2 line = new BInvoiceExchAccountLine2();
    line.setAcc1(Acc1BizConverter.getInstance().convert(source.getAcc1()));
    line.setAcc2(Acc2BizConverter.getInstance().convert(source.getAcc2()));
    line.setOriginTotal(TotalBizConverter.getInstance().convert(source.getOriginTotal()));
    line.setTotal(TotalBizConverter.getInstance().convert(source.getTotal()));
    line.setOldCode(source.getAcc2().getInvoice().getInvoiceCode());
    line.setOldNumber(source.getAcc2().getInvoice().getInvoiceNumber());
    
    return line;
  }

}
