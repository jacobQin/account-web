/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-5 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.server.converter;

import java.util.List;

import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.server.AccStandardBillBizConverter;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.commons.server.TotalBizConverter;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCashDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCollectionLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDepositDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueLine;
import com.hd123.m3.account.service.payment.Payment;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.ConverterUtil;

/**
 * @author subinzhu
 * 
 */
public class PaymentBizConverter extends AccStandardBillBizConverter<Payment, BPayment> {

  private boolean convertLine = false;

  public PaymentBizConverter() {
    super();
  }

  public PaymentBizConverter(boolean convertLine) {
    super();
    this.convertLine = convertLine;
  }

  public void setConvertLine(boolean convertLine) {
    this.convertLine = convertLine;
  }

  @Override
  public BPayment convert(Payment source) throws ConversionException {
    if (source == null)
      return null;

    BPayment target = new BPayment();
    super.inject(source, target);
    target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
    target.setCounterpart(BCounterpartConverter.getInstance().convert(source.getCounterpart(),
        source.getCounterpartType()));
    target.setDirection(source.getDirection());
    target.setBizState(source.getBizState());
    target.setDefrayalType(source.getDefrayalType().toString());
    target.setDealer(UCNBizConverter.getInstance().convert(source.getDealer()));
    target.setPaymentDate(source.getPaymentDate());
    target.setReceiptTotal(BTotal.zero());
    target.setUnpayedTotal(TotalBizConverter.getInstance().convert(source.getUnpayedTotal()));
    target.setTotal(TotalBizConverter.getInstance().convert(source.getTotal()));
    target.setOverdueTotal(TotalBizConverter.getInstance().convert(source.getOverdueTotal()));
    target.setDefrayalTotal(source.getDefrayalTotal());
    target.setDepositTotal(source.getDepositTotal());
    target.setDepositSubject(UCNBizConverter.getInstance().convert(source.getDepositSubject()));
    target.setIncomeDate(source.getIncomeDate());
    target.setIvcCode(source.getPivcCode());
    target.setIvcDate(source.getPivcDate());
    target.setIvcNumber(source.getPivcNumber());
    target.setIvcType(source.getPivcType());
    target.setIvcRemark(source.getPivcRemark());

    if (convertLine) {
      List<BPaymentLine> lines = ConverterUtil.convert(source.getLines(),
          PaymentLineBizConverter.getInstance());
      for (BPaymentLine line : lines) {
        if (line instanceof BPaymentAccountLine) {
          target.getAccountLines().add((BPaymentAccountLine) line);
        } else if (line instanceof BPaymentCollectionLine) {
          target.getCollectionLines().add((BPaymentCollectionLine) line);
        } else {
          target.getOverdueLines().add((BPaymentOverdueLine) line);
        }
      }

      List<BPaymentDefrayal> defrayals = ConverterUtil.convert(source.getDefrayals(),
          PaymentDefrayalBizConverter.getInstance());
      for (BPaymentDefrayal d : defrayals) {
        if (d instanceof BPaymentCashDefrayal)
          target.getCashs().add((BPaymentCashDefrayal) d);
        else
          target.getDeposits().add((BPaymentDepositDefrayal) d);
      }
    }

    return target;
  }
}
