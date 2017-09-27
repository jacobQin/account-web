/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BInvoiceExchangeHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月30日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.exchange.handle;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.service.billtype.BillType;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchAccountLine;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchBalanceLine;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchange;
import com.hd123.m3.cre.controller.account.invoice.exchange.biz.BInvoiceExchAccountLine;
import com.hd123.m3.cre.controller.account.invoice.exchange.biz.BInvoiceExchBalanceLine;
import com.hd123.m3.cre.controller.account.invoice.exchange.biz.BInvoiceExchange;
import com.hd123.m3.cre.controller.account.invoice.exchange.biz.InvoiceExchAccountLine2;
import com.hd123.m3.cre.controller.account.invoice.exchange.converter.AccountLinesPostProcessor;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * 对业务对象进行处理 ，对其对应页面对象的部分中的属性传值
 * InvoiceExchange -> BInvoiceExchange
 * @author wangyibo
 *
 */
public class BInvoiceExchangeHandler {

  private static final Converter<InvoiceExchange, BInvoiceExchange> CONVETER = ConverterBuilder
      .newBuilder(InvoiceExchange.class, BInvoiceExchange.class).build();

  public static BInvoiceExchange newInstance(InvoiceExchange source) {
    return CONVETER.convert(source);
  }

  public static BInvoiceExchangeHandler instance = null;
  private List<BillType> billTypes = new ArrayList<BillType>();

  public static BInvoiceExchangeHandler getInstance(List<BillType> billTypes) {
    if (instance == null) {
      instance = new BInvoiceExchangeHandler();
    }
    
    instance.billTypes = billTypes;
    
    return instance;
  }

  public BInvoiceExchange handler(InvoiceExchange source) {

    BInvoiceExchange target = newInstance(source);
    
    

    Converter<InvoiceExchBalanceLine, BInvoiceExchBalanceLine> balaceLineConverter = ConverterBuilder.newBuilder(
        InvoiceExchBalanceLine.class, BInvoiceExchBalanceLine.class).build();
    
    // 特殊明细处理,发票账款明细
    Converter<InvoiceExchAccountLine, InvoiceExchAccountLine2> accountLineConverter = ConverterBuilder
        .newBuilder(InvoiceExchAccountLine.class, InvoiceExchAccountLine2.class).build();

    target.setExchAccountLines2(ArrayListConverter.newConverter(accountLineConverter).convert(
        source.getExchAccountLines()));
    
    target.setBexchBalanceLines(ArrayListConverter.newConverter(balaceLineConverter).convert(source.getExchBalanceLines()));

    List<BInvoiceExchAccountLine> bexchAccountLines = AccountLinesPostProcessor
        .postProcessAccountLines(target.getExchAccountLines2(), target.getAccountAdjLines());
    
    for (BInvoiceExchAccountLine bInvoiceExchAccountLine : bexchAccountLines) {
      bInvoiceExchAccountLine.setSourceBillCaption(getBillCaption(bInvoiceExchAccountLine));
    }
    
    target.setBexchAccountLines(bexchAccountLines);
    //排序
    handleSort(target);
    
    return target;
  }
  
  /**处理顺序*/
  private BInvoiceExchange handleSort(BInvoiceExchange exchange){
    List<BInvoiceExchAccountLine> accounts = new ArrayList<BInvoiceExchAccountLine>();
    for (InvoiceExchBalanceLine line : exchange.getExchBalanceLines()) {
      for(BInvoiceExchAccountLine account : exchange.getBexchAccountLines()) {
        if(line.getOldNumber().equals(account.getOldNumber())) {
          accounts.add(account);
        }
      }
    }
    
    exchange.getBexchAccountLines().clear();
    exchange.getBexchAccountLines().addAll(accounts);
    
    return exchange;
  }
  
  private String getBillCaption(BInvoiceExchAccountLine source) {
    if ( source.getAcc1() == null || source.getAcc1().getSourceBill() == null) {
      return null;
    }

    if (billTypes == null) {
      return source.getAcc1().getSourceBill().getBillType();
    }

    for (BillType type : billTypes) {
      if (source.getAcc1().getSourceBill().getBillType().equals(type.getName())) {
        return type.getCaption();
      }
    }

    return source.getAcc1().getSourceBill().getBillType();
  }

}
