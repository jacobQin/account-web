/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BizInvoiceRegConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.server.converter;

import com.hd123.m3.account.gwt.acc.server.BizAcc1Converter;
import com.hd123.m3.account.gwt.acc.server.BizAcc2Converter;
import com.hd123.m3.account.gwt.commons.server.BizTotalConverter;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BIvcRegLine;
import com.hd123.m3.account.service.ivc.reg.InvoiceReg;
import com.hd123.m3.account.service.ivc.reg.IvcRegLine;
import com.hd123.m3.commons.gwt.base.server.convert.BizEntityConverter;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * BInvoiceReg-->InvoiceReg.
 * 
 * @author chenrizhang
 * @since 1.0
 * 
 */
public class BizInvoiceRegConverter extends BizEntityConverter<BInvoiceReg, InvoiceReg> {

  @Override
  public InvoiceReg convert(BInvoiceReg source) throws ConversionException {
    if (source == null) {
      return null;
    }
    InvoiceReg target = new InvoiceReg();
    inject(source, target);
    target.setAccountUnit(new BizUCNConverter().convert(source.getAccountUnit()));
    target.setCounterpart(new BizUCNConverter().convert(source.getCounterpart()));
    target.setCounterpartType(source.getCounterpartType());
    target.setDirection(source.getDirection());
    target.setRegDate(source.getRegDate());
    target.setAllowSplitReg(source.isAllowSplitReg());
    target.setUseInvoiceStock(source.isUseInvoiceStock());
    target.setInvoiceType(source.getInvoiceType());
    target.setInvoiceTotal(BizTotalConverter.getInstance().convert(source.getInvoiceTotal()));
    target.setAccountTotal(BizTotalConverter.getInstance().convert(source.getAccountTotal()));
    target.setTotalDiff(source.getTotalDiff());
    target.setTaxDiff(source.getTaxDiff());
    target.setRemark(source.getRemark());
    target.setPrinted(source.isPrinted());
    for (BIvcRegLine s : source.getRegLines()) {
      IvcRegLine t = new IvcRegLine();
      t.setUuid(s.getUuid());
      t.setAcc1(BizAcc1Converter.getInstance().convert(s.getAcc1()));
      t.setAcc2(BizAcc2Converter.getInstance().convert(s.getAcc2()));
      t.setInvoiceCode(s.getInvoiceCode());
      t.setInvoiceNumber(s.getInvoiceNumber());
      t.setInvoiceType(s.getInvoiceType());
      t.setOriginTotal(BizTotalConverter.getInstance().convert(s.getOriginTotal()));
      t.setTotal(BizTotalConverter.getInstance().convert(s.getTotal()));
      t.setUnregTotal(BizTotalConverter.getInstance().convert(s.getUnregTotal()));
      t.setTotalDiff(s.getTotalDiff());
      t.setTaxDiff(s.getTaxDiff());
      t.setRemark(s.getRemark());
      target.getRegLines().add(t);
    }
    return target;
  }
}
