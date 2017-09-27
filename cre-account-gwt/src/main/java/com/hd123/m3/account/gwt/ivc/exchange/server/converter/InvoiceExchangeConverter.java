/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月18日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.server.converter;

import com.hd123.m3.account.gwt.acc.server.BizAcc1Converter;
import com.hd123.m3.account.gwt.acc.server.BizAcc2Converter;
import com.hd123.m3.account.gwt.commons.server.BizTotalConverter;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BExchangeType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvToInvType;
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
import com.hd123.m3.commons.gwt.base.server.convert.BizEntityConverter;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
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
public class InvoiceExchangeConverter extends BizEntityConverter<BInvoiceExchange, InvoiceExchange> {

  private static InvoiceExchangeConverter instance = null;

  public static InvoiceExchangeConverter getInstance() {
    if (instance == null) {
      instance = new InvoiceExchangeConverter();
    }
    return instance;
  }

  @Override
  public InvoiceExchange convert(BInvoiceExchange source) throws ConversionException {
    if (source == null) {
      return null;
    }

    // 发票红冲明细转换器
    Converter<BInvoiceExchBalanceLine, InvoiceExchBalanceLine> balanceLineConverter = ConverterBuilder
        .newBuilder(BInvoiceExchBalanceLine.class, InvoiceExchBalanceLine.class).build();

    // 发票账款明细转换器
    Converter<BInvoiceExchAccountLine2, InvoiceExchAccountLine> accountLineConverter = ConverterBuilder
        .newBuilder(BInvoiceExchAccountLine2.class, InvoiceExchAccountLine.class)
        .map("acc1", BizAcc1Converter.getInstance()).map("acc2", BizAcc2Converter.getInstance())
        .map("originTotal", BizTotalConverter.getInstance())
        .map("total", BizTotalConverter.getInstance()).build();
    
    // 账款调整
    Converter<BInvoiceExchangeAccountAdjLine, InvoiceExchAccountAdjLine> adjLineConverter = ConverterBuilder
        .newBuilder(BInvoiceExchangeAccountAdjLine.class, InvoiceExchAccountAdjLine.class)
        .map("acc1", BizAcc1Converter.getInstance()).map("acc2", BizAcc2Converter.getInstance())
        .map("total", BizTotalConverter.getInstance()).build();


    InvoiceExchange target = new InvoiceExchange();
    inject(source, target);

    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    target.setTenant(BizUCNConverter.getInstance().convert(source.getTenant()));
    target.setType(getExchType(source.getType()));
    target.setInvToInvType(getInvType(source.getInvToInvType()));
    target.setExchanger(BizUCNConverter.getInstance().convert(source.getExchanger()));
    target.setExchangeDate(source.getExchangeDate());
    target.setRemark(source.getRemark());
    
    /**必须在明细转换之前进行调用*/
    AccountLinesPostProcessor.syncModifyInvoiceProerties(source);

    target.setExchBalanceLines(ArrayListConverter.newConverter(balanceLineConverter).convert(
        source.getExchBalanceLines()));
    target.setExchAccountLines(ArrayListConverter.newConverter(accountLineConverter).convert(
        source.getExchAccountLines2()));
    target.setAccountAdjLines(ArrayListConverter.newConverter(adjLineConverter).convert(
        source.getAccountAdjLines()));

    return target;
  }

  private ExchangeType getExchType(BExchangeType source) {
    if (source == null) {
      return null;
    }
    return ExchangeType.valueOf(source.name());
  }

  private InvToInvType getInvType(BInvToInvType source) {
    if (source == null) {
      return null;
    }
    return InvToInvType.valueOf(source.name());
  }
}
