/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentNoticeBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-14 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.server;

import java.math.BigDecimal;

import com.hd123.m3.account.gwt.commons.server.AccStandardBillBizConverter;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.commons.server.BillBizConverter;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNoticeLine;
import com.hd123.m3.account.service.paymentnotice.PaymentNotice;
import com.hd123.m3.account.service.paymentnotice.PaymentNoticeLine;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * 收付款通知单转换器
 * <p>
 * PaymentNotice->BPaymentNotice
 * 
 * @author zhuhairui
 * 
 */
public class PaymentNoticeBizConverter extends
    AccStandardBillBizConverter<PaymentNotice, BPaymentNotice> {

  private static PaymentNoticeBizConverter instance;

  public static PaymentNoticeBizConverter getInstance() {
    if (instance == null)
      instance = new PaymentNoticeBizConverter();
    return instance;
  }

  @Override
  public BPaymentNotice convert(PaymentNotice source) throws ConversionException {

    try {
      if (source == null)
        return null;
      BPaymentNotice target = new BPaymentNotice();
      super.inject(source, target);
      target.setBillNumber(source.getBillNumber());
      target.setUuid(source.getUuid());
      target.setVersion(source.getVersion());

      target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
      target.setBizState(source.getBizState());
      target.setCounterpart(BCounterpartConverter.getInstance().convert(source.getCounterpart(),
          source.getCounterpartType()));
      target.setReceiptTotal(source.getReceiptTotal() != null ? source.getReceiptTotal().getTotal()
          : BigDecimal.ZERO);
      target.setReceiptTax(source.getReceiptTotal() != null ? source.getReceiptTotal().getTax()
          : BigDecimal.ZERO);
      target.setReceiptIvcTotal(source.getReceiptIvcTotal() != null ? source.getReceiptIvcTotal()
          .getTotal() : BigDecimal.ZERO);
      target.setReceiptIvcTax(source.getReceiptIvcTotal() != null ? source.getReceiptIvcTotal()
          .getTax() : BigDecimal.ZERO);

      target.setPaymentTotal(source.getPaymentTotal() != null ? source.getPaymentTotal().getTotal()
          : BigDecimal.ZERO);
      target.setPaymentTax(source.getPaymentTotal() != null ? source.getPaymentTotal().getTax()
          : BigDecimal.ZERO);
      target.setPaymentIvcTotal(source.getPaymentIvcTotal() != null ? source.getPaymentIvcTotal()
          .getTotal() : BigDecimal.ZERO);
      target.setPaymentIvcTax(source.getPaymentIvcTotal() != null ? source.getPaymentIvcTotal()
          .getTax() : BigDecimal.ZERO);
      target.setBpmMessage(source.getBpmMessage());

      for (PaymentNoticeLine line : source.getLines()) {
        target.getLines().add(convertPaymentNoticeLine(line));
      }
      return target;
    } catch (Exception e) {
      throw new ConversionException(e);
    }
  }

  private BPaymentNoticeLine convertPaymentNoticeLine(PaymentNoticeLine source)
       {

    if (source == null)
      return null;

    BPaymentNoticeLine target = new BPaymentNoticeLine();
    target.setLineNumber(source.getLineNumber());
    target.setStatement(BillBizConverter.getInstance().convert(source.getStatement()));
    target.setContract(UCNBizConverter.getInstance().convert(source.getContract()));
    target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
    target.setReceiptTotal(source.getReceiptTotal() != null ? source.getReceiptTotal().getTotal()
        : BigDecimal.ZERO);
    target.setReceiptTax(source.getReceiptTotal() != null ? source.getReceiptTotal().getTax()
        : BigDecimal.ZERO);
    target.setReceiptIvcTotal(source.getReceiptIvcTotal() != null ? source.getReceiptIvcTotal()
        .getTotal() : BigDecimal.ZERO);
    target.setReceiptIvcTax(source.getReceiptIvcTotal() != null ? source.getReceiptIvcTotal()
        .getTax() : BigDecimal.ZERO);
    target.setPaymentTotal(source.getPaymentTotal() != null ? source.getPaymentTotal().getTotal()
        : BigDecimal.ZERO);
    target.setPaymentTax(source.getPaymentTotal() != null ? source.getPaymentTotal().getTax()
        : BigDecimal.ZERO);
    target.setPaymentIvcTotal(source.getPaymentIvcTotal() != null ? source.getPaymentIvcTotal()
        .getTotal() : BigDecimal.ZERO);
    target.setPaymentIvcTax(source.getPaymentIvcTotal() != null ? source.getPaymentIvcTotal()
        .getTax() : BigDecimal.ZERO);
    target.setRemark(source.getRemark());
    return target;
  }
}
