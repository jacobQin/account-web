/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebateBillBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月1日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.rebate.server.converter;

import com.hd123.m3.account.gwt.rebate.client.biz.BContract;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateLine;
import com.hd123.m3.account.gwt.rebate.client.biz.BSalesPayment;
import com.hd123.m3.account.gwt.rebate.client.biz.BSalesReceiver;
import com.hd123.m3.account.service.rebate.RebateBill;
import com.hd123.m3.account.service.rebate.RebateLine;
import com.hd123.m3.account.service.rebate.SalesPayment;
import com.hd123.m3.commons.gwt.base.server.convert.EntityBizConverter;
import com.hd123.m3.commons.gwt.base.server.tax.TaxRateBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * 销售额返款单|转换器。
 * <p>
 * RebateBill-->BRebateBill.
 * 
 * @author lixiaohong
 * @since 1.12
 *
 */
public class RebateBillBizConverter extends EntityBizConverter<RebateBill, BRebateBill> {
  private static RebateBillBizConverter instance = null;
  // 付款方式转换器
  Converter<SalesPayment, BSalesPayment> paymentConverter = new SalePaymentBizConverter();
  // 返款明细转换器
  Converter<RebateLine, BRebateLine> lineConverter = ConverterBuilder
      .newBuilder(RebateLine.class, BRebateLine.class)
      .map("payments", ArrayListConverter.newConverter(paymentConverter))
      .map("srcBill", UCNBizConverter.getInstance()).build();

  private RebateBillBizConverter() {
    super();
  }

  public static RebateBillBizConverter getInstance() {
    if (instance == null)
      instance = new RebateBillBizConverter();
    return instance;
  }

  @Override
  public BRebateBill convert(RebateBill source) throws ConversionException {
    if (source == null) {
      return null;
    }
    BRebateBill target = new BRebateBill();
    super.inject(source, target);

    target.setAccountDate(source.getAccountDate());
    target.setBackTotal(source.getBackTotal());
    target.setBeginDate(source.getBeginDate());
    target.setStore(new UCNBizConverter().convert(source.getStore()));

    BContract contract = new BContract();
    contract.setUuid(source.getContract().getUuid());
    contract.setCode(source.getContract().getCode());
    contract.setName(source.getContract().getName());
    target.setContract(contract);

    target.setTenant(new UCNBizConverter().convert(source.getTenant()));
    target.setEndDate(source.getEndDate());
    target.setPositions(source.getPositions());
    target.setPoundageTotal(source.getPoundageTotal());
    target.setRemark(source.getRemark());
    target.setShouldBackTotal(source.getShouldBackTotal());

    target.setRebateInvoice(source.isRebateInvoice());
    target.setPoundageInvoice(source.isPoundageInvoice());

    target.setRebateSubject(new UCNBizConverter().convert(source.getRebateSubject()));
    target.setPoundageSubject(new UCNBizConverter().convert(source.getPoundageSubject()));
    target.setPoundageTaxRate(TaxRateBizConverter.getInstance()
        .convert(source.getPoundageTaxRate()));
    target.setBackTaxRate(TaxRateBizConverter.getInstance().convert(source.getBackTaxRate()));

    for (RebateLine line : source.getLines()) {
      target.getLines().add(convert(line));
    }

    return target;
  }

  private BRebateLine convert(RebateLine source) {
    if (source == null) {
      return null;
    }

    BRebateLine target = new BRebateLine();
    target.setLineNumber(source.getLineNumber());
    target.setAmount(source.getAmount());
    target.setSrcBill(new UCNBizConverter().convert(source.getSrcBill()));
    target.setPoundageAmount(source.getPoundageAmount());
    target.setSalesDate(source.getSalesDate());
    target.setRebateAmount(source.getRebateAmount());

    target.setAccountDate(source.getAccountDate());
    target.setRptUuid(source.getRptUuid());

    for (SalesPayment payment : source.getPayments()) {
      target.getPayments().add(convert(payment));
    }
    return target;
  }

  private BSalesPayment convert(SalesPayment source) {
    if (source == null) {
      return null;
    }

    BSalesPayment target = new BSalesPayment();
    target.setPayment(new UCNBizConverter().convert(source.getPayment()));
    target.setBank(new UCNBizConverter().convert(source.getBank()));
    target.setTotal(source.getTotal());
    target.setReceiver(source.getReceiver() == null ? null : BSalesReceiver.valueOf(source
        .getReceiver().name()));
    return target;
  }

}
