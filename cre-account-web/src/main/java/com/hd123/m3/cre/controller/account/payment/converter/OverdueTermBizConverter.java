package com.hd123.m3.cre.controller.account.payment.converter;

import com.hd123.m3.account.service.payment.PaymentOverdueTerm;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentOverdueTerm;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author LiBin
 * 
 */
public class OverdueTermBizConverter implements Converter<PaymentOverdueTerm, BPaymentOverdueTerm> {

  private static OverdueTermBizConverter instance = null;

  public static OverdueTermBizConverter getInstance() {
    if (instance == null)
      instance = new OverdueTermBizConverter();
    return instance;
  }

  @Override
  public BPaymentOverdueTerm convert(PaymentOverdueTerm source) throws ConversionException {
    if (source == null)
      return null;

    BPaymentOverdueTerm target = new BPaymentOverdueTerm();
    try {
      target.setContract(source.getContract());
      target.setSubject(source.getSubject());
      target.setDirection(source.getDirection());
      target.setRate(source.getRate());
      target.setTaxRate(source.getTaxRate());
      target.setInvoice(source.isInvoice());
      target.setAllSubjects(source.isAllSubjects());
      target.setSubjects(source.getSubjects());
      return target;
    } catch (Exception e) {
      throw new ConversionException(e.getMessage());
    }
  }

}
