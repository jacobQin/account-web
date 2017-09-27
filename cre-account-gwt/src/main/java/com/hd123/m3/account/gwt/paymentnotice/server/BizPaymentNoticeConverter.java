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

import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.server.BizAccStandardBillConverter;
import com.hd123.m3.account.gwt.commons.server.BizBillConverter;
import com.hd123.m3.account.gwt.commons.server.BizTotalConverter;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNoticeLine;
import com.hd123.m3.account.service.paymentnotice.PaymentNotice;
import com.hd123.m3.account.service.paymentnotice.PaymentNoticeLine;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * 收付款通知单转换器
 * <p>
 * PaymentNotice->BPaymentNotice
 * 
 * @author zhuhairui
 * 
 */
public class BizPaymentNoticeConverter extends
    BizAccStandardBillConverter<BPaymentNotice, PaymentNotice> {

  private static BizPaymentNoticeConverter instance;

  public static BizPaymentNoticeConverter getInstance() {
    if (instance == null)
      instance = new BizPaymentNoticeConverter();
    return instance;
  }

  @Override
  public PaymentNotice convert(BPaymentNotice source) throws ConversionException {

    try {
      if (source == null)
        return null;
      PaymentNotice target = new PaymentNotice();
      super.inject(source, target);

      target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
      target.setBizState(source.getBizState());
      target.setCounterpart(BizUCNConverter.getInstance().convert(source.getCounterpart()));
      target.setCounterpartType(source.getCounterpart() == null ? null : source.getCounterpart()
          .getCounterpartType());
      target.setReceiptTotal(BizTotalConverter.getInstance().convert(
          new BTotal(source.getReceiptTotal(), source.getReceiptTax())));
      target.setReceiptIvcTotal(BizTotalConverter.getInstance().convert(
          new BTotal(source.getReceiptIvcTotal(), source.getReceiptIvcTax())));
      target.setPaymentTotal(BizTotalConverter.getInstance().convert(
          new BTotal(source.getPaymentTotal(), source.getPaymentTax())));
      target.setPaymentIvcTotal(BizTotalConverter.getInstance().convert(
          new BTotal(source.getPaymentIvcTotal(), source.getPaymentIvcTax())));
      target.setRemark(source.getRemark());
      target.setBpmMessage(source.getBpmMessage());

      target.getLines().clear();
      for (int i = 0; i < source.getLines().size(); i++) {
        BPaymentNoticeLine bPaymentNoticeLine = source.getLines().get(i);
        target.getLines().add(convertLine(bPaymentNoticeLine));
      }

      return target;
    } catch (Exception e) {
      throw new ConversionException(e);
    }
  }

  private PaymentNoticeLine convertLine(BPaymentNoticeLine source) {
    assert source != null;
    PaymentNoticeLine target = new PaymentNoticeLine();
    target.setLineNumber(source.getLineNumber());
    target.setStatement(BizBillConverter.getInstance().convert(source.getStatement()));
    target.setContract(BizUCNConverter.getInstance().convert(source.getContract()));
    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    target.setReceiptTotal(BizTotalConverter.getInstance().convert(
        new BTotal(source.getReceiptTotal(), source.getReceiptTax())));
    target.setReceiptIvcTotal(BizTotalConverter.getInstance().convert(
        new BTotal(source.getReceiptIvcTotal(), source.getReceiptIvcTax())));
    target.setPaymentTotal(BizTotalConverter.getInstance().convert(
        new BTotal(source.getPaymentTotal(), source.getPaymentTax())));
    target.setPaymentIvcTotal(BizTotalConverter.getInstance().convert(
        new BTotal(source.getPaymentIvcTotal(), source.getPaymentIvcTax())));
    target.setRemark(source.getRemark());

    return target;
  }
}
