/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	BizRebateBillConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月1日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.rebate.server.converter;

import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateLine;
import com.hd123.m3.account.gwt.rebate.client.biz.BSalesPayment;
import com.hd123.m3.account.service.rebate.RebateBill;
import com.hd123.m3.account.service.rebate.RebateLine;
import com.hd123.m3.account.service.rebate.SalesPayment;
import com.hd123.m3.account.service.report.product.SalesReceiver;
import com.hd123.m3.commons.gwt.base.server.convert.BizEntityConverter;
import com.hd123.m3.commons.gwt.base.server.tax.BizTaxRateConverter;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * 销售额返款单|转换器。
 * <p>
 * BRebateBill-->RebateBill.
 * 
 * @author lixiaohong
 * @since 1.12
 *
 */
public class BizRebateBillConverter extends BizEntityConverter<BRebateBill, RebateBill> {

  @Override
  public RebateBill convert(BRebateBill source) throws ConversionException {
    if (source == null) {
      return null;
    }

    RebateBill target = new RebateBill();
    super.inject(source, target);

    target.setAccountDate(source.getAccountDate());
    target.setBackTotal(source.getBackTotal());
    target.setBeginDate(source.getBeginDate());
    target.setStore(new BizUCNConverter().convert(source.getStore()));
    target.setContract(new BizUCNConverter().convert(source.getContract()));
    target.setTenant(new BizUCNConverter().convert(source.getTenant()));
    target.setEndDate(source.getEndDate());
    target.setPositions(source.getPositions());
    target.setPoundageTotal(source.getPoundageTotal());
    target.setRemark(source.getRemark());
    target.setSingle(source.isSingle());
    target.setShouldBackTotal(source.getShouldBackTotal());
    target.setRebateInvoice(source.isRebateInvoice());
    target.setPoundageInvoice(source.isPoundageInvoice());

    target.setRebateSubject(new BizUCNConverter().convert(source.getRebateSubject()));
    target.setPoundageSubject(new BizUCNConverter().convert(source.getPoundageSubject()));
    target.setPoundageTax(BizTaxRateConverter.getInstance().convert(source.getPoundageTaxRate()));
    target.setBackTaxRate(BizTaxRateConverter.getInstance().convert(source.getBackTaxRate()));

    for (BRebateLine line : source.getLines()) {
      target.getLines().add(convert(line));
    }

    return target;
  }

  private RebateLine convert(BRebateLine source) {
    if (source == null) {
      return null;
    }

    RebateLine target = new RebateLine();
    target.setLineNumber(source.getLineNumber());
    target.setAmount(source.getAmount());
    target.setSrcBill(new BizUCNConverter().convert(source.getSrcBill()));
    target.setPoundageAmount(source.getPoundageAmount());
    target.setSalesDate(source.getSalesDate());
    target.setRebateAmount(source.getRebateAmount());

    target.setAccountDate(source.getAccountDate());
    target.setRptUuid(source.getRptUuid());

    for (BSalesPayment payment : source.getPayments()) {
      target.getPayments().add(convert(payment));
    }
    return target;
  }

  private SalesPayment convert(BSalesPayment source) {
    if (source == null) {
      return null;
    }

    SalesPayment target = new SalesPayment();
    target.setPayment(new BizUCNConverter().convert(source.getPayment()));
    target.setBank(new BizUCNConverter().convert(source.getBank()));
    target.setTotal(source.getTotal());
    target.setReceiver(source.getReceiver() == null ? null : SalesReceiver.valueOf(source
        .getReceiver().name()));
    return target;
  }

}
