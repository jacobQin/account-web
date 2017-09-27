package com.hd123.m3.cre.controller.account.payment.converter;

import java.util.List;

import com.hd123.m3.account.service.payment.Payment;
import com.hd123.m3.cre.controller.account.payment.model.BPayment;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentAccountLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentCashDefrayal;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentCollectionLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentDefrayal;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentDepositDefrayal;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentOverdueLine;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;
import com.hd123.rumba.commons.util.converter.ConverterUtil;

/**
 * @author LiBin
 * 
 */
public class PaymentBizConverter implements Converter<Payment, BPayment> {

  private static Converter<Payment, BPayment> paymentConverter = ConverterBuilder.newBuilder(
      Payment.class, BPayment.class).build();

  private static PaymentBizConverter instance = null;

  public static PaymentBizConverter getInstance() {
    if (instance == null) {
      instance = new PaymentBizConverter();
    }
    return instance;
  }

  private PaymentBizConverter() {
    // Do Nothing
  }

  @Override
  public BPayment convert(Payment source) throws ConversionException {
    if (source == null) {
      return null;
    }

    BPayment target = paymentConverter.convert(source);

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
        target.getCashes().add((BPaymentCashDefrayal) d);
      else
        target.getDeposits().add((BPaymentDepositDefrayal) d);
    }

    return target;
  }
}
