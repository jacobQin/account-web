/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceExchangeConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月18日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.server.converter;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.acc.server.Acc1BizConverter;
import com.hd123.m3.account.gwt.acc.server.Acc2BizConverter;
import com.hd123.m3.account.gwt.commons.server.TotalBizConverter;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BExchangeType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvToInvType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchAccountLine;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchAccountLine2;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchBalanceLine;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchangeAccountAdjLine;
import com.hd123.m3.account.service.ivc.exchange.ExchangeType;
import com.hd123.m3.account.service.ivc.exchange.InvToInvType;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchAccountAdjLine;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchAccountLine;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchBalanceLine;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchange;
import com.hd123.m3.commons.gwt.base.server.convert.EntityBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * {@link InvoiceExchange} -> {@link BInvoiceExchange}
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class BInvoiceExchangeConverter extends
    EntityBizConverter<InvoiceExchange, BInvoiceExchange> {

  private static BInvoiceExchangeConverter instance = null;

  public static BInvoiceExchangeConverter getInstance() {
    if (instance == null) {
      instance = new BInvoiceExchangeConverter();
    }
    return instance;
  }

  @Override
  public BInvoiceExchange convert(InvoiceExchange source) throws ConversionException {
    if (source == null) {
      return null;
    }

    // 发票红冲明细转换器
    Converter<InvoiceExchBalanceLine, BInvoiceExchBalanceLine> balanceLineConverter = ConverterBuilder
        .newBuilder(InvoiceExchBalanceLine.class, BInvoiceExchBalanceLine.class).build();

    // 发票账款明细转换器
    Converter<InvoiceExchAccountLine, BInvoiceExchAccountLine2> accountLineConverter = ConverterBuilder
        .newBuilder(InvoiceExchAccountLine.class, BInvoiceExchAccountLine2.class)
        .map("acc1", Acc1BizConverter.getInstance()).map("acc2", Acc2BizConverter.getInstance())
        .map("originTotal", TotalBizConverter.getInstance())
        .map("total", TotalBizConverter.getInstance()).build();

    // 账款调整
    Converter<InvoiceExchAccountAdjLine, BInvoiceExchangeAccountAdjLine> adjLineConverter = ConverterBuilder
        .newBuilder(InvoiceExchAccountAdjLine.class, BInvoiceExchangeAccountAdjLine.class)
        .map("acc1", Acc1BizConverter.getInstance()).map("acc2", Acc2BizConverter.getInstance())
        .map("total", TotalBizConverter.getInstance()).build();

    BInvoiceExchange target = new BInvoiceExchange();
    inject(source, target);

    target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
    target.setTenant(UCNBizConverter.getInstance().convert(source.getTenant()));
    target.setType(getExchType(source.getType()));
    target.setInvToInvType(getInvType(source.getInvToInvType()));
    target.setExchanger(UCNBizConverter.getInstance().convert(source.getExchanger()));
    target.setExchangeDate(source.getExchangeDate());
    target.setRemark(source.getRemark());

    target.setExchBalanceLines(ArrayListConverter.newConverter(balanceLineConverter).convert(
        source.getExchBalanceLines()));
    
    target.setExchAccountLines2(ArrayListConverter.newConverter(accountLineConverter).convert(
        source.getExchAccountLines()));
    target.setAccountAdjLines(ArrayListConverter.newConverter(adjLineConverter).convert(
        source.getAccountAdjLines()));

    List<BInvoiceExchAccountLine> exchAccountLines = AccountLinesPostProcessor
        .postProcessAccountLines(target.getExchAccountLines2(), target.getAccountAdjLines());
    target.setExchAccountLines(exchAccountLines);
    
    handleSort(target);
    
    return target;
  }
  
  /**处理顺序*/
  private BInvoiceExchange handleSort(BInvoiceExchange exchange){
    List<BInvoiceExchAccountLine> accounts = new ArrayList<BInvoiceExchAccountLine>();
    for(BInvoiceExchBalanceLine line:exchange.getExchBalanceLines()){
      for(BInvoiceExchAccountLine account:exchange.getExchAccountLines()){
        if(line.getOldNumber().equals(account.getOldNumber())){
          accounts.add(account);
        }
      }
    }
    
    exchange.getExchAccountLines().clear();
    exchange.getExchAccountLines().addAll(accounts);
    
    return exchange;
  }

  private BExchangeType getExchType(ExchangeType source) {
    if (source == null) {
      return null;
    }
    return BExchangeType.valueOf(source.name());
  }

  private BInvToInvType getInvType(InvToInvType source) {
    if (source == null) {
      return null;
    }
    return BInvToInvType.valueOf(source.name());
  }

}
