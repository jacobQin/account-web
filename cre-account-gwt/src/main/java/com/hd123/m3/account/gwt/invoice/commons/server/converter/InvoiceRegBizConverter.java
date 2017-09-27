/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-27 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.server.converter;

import com.hd123.m3.account.gwt.commons.server.AccStandardBillBizConverter;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.commons.server.TotalBizConverter;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.service.invoice.InvoiceReg;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.ConverterUtil;

/**
 * @author chenpeisi
 * 
 */
public class InvoiceRegBizConverter extends AccStandardBillBizConverter<InvoiceReg, BInvoiceReg> {

  public static InvoiceRegBizConverter instance = null;

  public static InvoiceRegBizConverter getInstance() {
    if (instance == null)
      instance = new InvoiceRegBizConverter();
    return instance;
  }

  public InvoiceRegBizConverter() {
    super();
  }

  @Override
  public BInvoiceReg convert(InvoiceReg source) throws ConversionException {
    if (source == null)
      return null;
    try {
      BInvoiceReg target = new BInvoiceReg();
      inject(source, target);

      target.setUuid(source.getUuid());
      target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
      target.setAccountTotal(TotalBizConverter.getInstance().convert(source.getAccountTotal()));
      target.setBillNumber(source.getBillNumber());
      target.setCounterpart(BCounterpartConverter.getInstance().convert(source.getCounterpart(),
          source.getCounterpartType()));
      target.setDirection(source.getDirection());
      target.setInvoiceCode(source.getInvoiceCode());
      target.setInvoiceTotal(TotalBizConverter.getInstance().convert(source.getInvoiceTotal()));
      target.setRegDate(source.getRegDate());
      target.setRemark(source.getRemark());
      target.setTaxDiff(source.getTaxDiff());
      target.setTotalDiff(source.getTotalDiff());
      target.setSettleNo(source.getSettleNo());

      target.setLines(ConverterUtil.convert(source.getLines(),
          InvoiceRegLineBizConverter.getInstnce()));

      target.setInvoices(ConverterUtil.convert(source.getInvoices(),
          InvoiceRegInvoiceBizConverter.getInstnce()));

      return target;
    } catch (Exception e) {
      throw new ConversionException(e);
    }
  }
}
