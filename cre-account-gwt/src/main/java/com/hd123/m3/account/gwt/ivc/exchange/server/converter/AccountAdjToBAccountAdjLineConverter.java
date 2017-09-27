/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	AccountAdjToBInvoiceExchangeAccountAdjLine.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月24日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.server.converter;

import com.hd123.m3.account.gwt.acc.server.Acc1BizConverter;
import com.hd123.m3.account.gwt.acc.server.Acc2BizConverter;
import com.hd123.m3.account.gwt.commons.server.TotalBizConverter;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchangeAccountAdjLine;
import com.hd123.m3.account.service.acc.AccountAdj;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * AccountAdj -> BInvoiceExchangeAccountAdjLine
 * 
 * @author LiBin
 * @since 1.7
 *
 */
public class AccountAdjToBAccountAdjLineConverter implements Converter<AccountAdj,BInvoiceExchangeAccountAdjLine>{
  
  private static AccountAdjToBAccountAdjLineConverter instance = null;
  
  public static AccountAdjToBAccountAdjLineConverter getInstance(){
    if(instance == null){
      instance = new AccountAdjToBAccountAdjLineConverter();
    }
    return instance;
  }

  @Override
  public BInvoiceExchangeAccountAdjLine convert(AccountAdj source) throws ConversionException {
    if(source == null){
      return null;
    }
    
    BInvoiceExchangeAccountAdjLine target = new BInvoiceExchangeAccountAdjLine();
    target.setAcc1(Acc1BizConverter.getInstance().convert(source.getAcc1()));
    target.setAcc2(Acc2BizConverter.getInstance().convert(source.getAcc2()));
    target.setTotal(TotalBizConverter.getInstance().convert(source.getTotal()));
    
    target.setAdjId(source.getAdjId());
    target.setAdjRemark(source.getAdjRemark());
    target.setAdjTime(source.getAdjTime());
    
    return target;
  }

}
