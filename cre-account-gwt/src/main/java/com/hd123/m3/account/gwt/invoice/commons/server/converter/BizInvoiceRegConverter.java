/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BizDepositConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-20 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.server.converter;

import com.hd123.m3.account.gwt.commons.server.BizAccStandardBillConverter;
import com.hd123.m3.account.gwt.commons.server.BizTotalConverter;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.service.invoice.InvoiceReg;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.ConverterUtil;

/**
 * @author chenpeisi
 * 
 */
public class BizInvoiceRegConverter extends BizAccStandardBillConverter<BInvoiceReg, InvoiceReg> {
  private static BizInvoiceRegConverter instance = null;

  public static BizInvoiceRegConverter getInstance() {
    if (instance == null)
      instance = new BizInvoiceRegConverter();
    return instance;
  }

  @Override
  public InvoiceReg convert(BInvoiceReg source) throws ConversionException {
    assert source != null;

    InvoiceReg target = new InvoiceReg();
    super.inject(source, target);

    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    target.setAccountTotal(BizTotalConverter.getInstance().convert(source.getAccountTotal()));
    target.setCounterpart(BizUCNConverter.getInstance().convert(source.getCounterpart()));
    target.setCounterpartType(source.getCounterpart() == null ? null : source.getCounterpart()
        .getCounterpartType());
    target.setDirection(source.getDirection());
    target.setInvoiceCode(source.getInvoiceCode());
    target.setInvoiceTotal(BizTotalConverter.getInstance().convert(source.getInvoiceTotal()));
    target.setRegDate(source.getRegDate());
    target.setRemark(source.getRemark());
    target.setSettleNo(source.getSettleNo());
    target.setTaxDiff(source.getTaxDiff());
    target.setTotalDiff(source.getTotalDiff());
    target.setVersion(source.getVersion());

    target.setLines(ConverterUtil.convert(source.getLines(),
        BizInvoiceRegLineConverter.getInstance()));

    target.setInvoices(ConverterUtil.convert(source.getInvoices(),
        BizInvoiceRegInvoiceConverter.getInstance()));

    return target;
  }
}
