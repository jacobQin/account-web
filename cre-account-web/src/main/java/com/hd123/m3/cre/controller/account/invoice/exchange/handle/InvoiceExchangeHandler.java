/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceExchangeHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月30日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.exchange.handle;

import com.hd123.m3.account.service.ivc.exchange.InvoiceExchAccountLine;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchBalanceLine;
import com.hd123.m3.cre.controller.account.invoice.exchange.biz.BInvoiceExchBalanceLine;
import com.hd123.m3.cre.controller.account.invoice.exchange.biz.BInvoiceExchange;
import com.hd123.m3.cre.controller.account.invoice.exchange.biz.InvoiceExchAccountLine2;
import com.hd123.m3.cre.controller.account.invoice.exchange.converter.AccountLinesPostProcessor;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * BInvoiceExchange -> BInvoiceExchange(用于保存)
 * 
 * @author wangyibo
 *
 */
public class InvoiceExchangeHandler {

  public static InvoiceExchangeHandler instance = null;

  public static InvoiceExchangeHandler getInstance() {
    if (instance == null) {
      instance = new InvoiceExchangeHandler();
    }
    return instance;
  }

  public BInvoiceExchange hander(BInvoiceExchange source) {
    if (source == null) {
      return null;
    }
    
    // 发票账款明细转换器
    Converter<InvoiceExchAccountLine2, InvoiceExchAccountLine> accountLineConverter = ConverterBuilder
        .newBuilder(InvoiceExchAccountLine2.class, InvoiceExchAccountLine.class).build();

    /** 必须在明细转换之前进行调用 */
    AccountLinesPostProcessor.syncModifyInvoiceProerties(source);
    
    source.setExchAccountLines(ArrayListConverter.newConverter(accountLineConverter).convert(
        source.getExchAccountLines2()));
    
    return source;
  }
  
  public BInvoiceExchange handerBalance(BInvoiceExchange source) {
    if (source == null) {
      return null;
    }
    
    Converter<BInvoiceExchBalanceLine, InvoiceExchBalanceLine> balaceLineConverter = ConverterBuilder.newBuilder(
        BInvoiceExchBalanceLine.class, InvoiceExchBalanceLine.class).build();

    source.setExchBalanceLines(ArrayListConverter.newConverter(balaceLineConverter).convert(
        source.getBexchBalanceLines()));
    
    return source;
  }

}
