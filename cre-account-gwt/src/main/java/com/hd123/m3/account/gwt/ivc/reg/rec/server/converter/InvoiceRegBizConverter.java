/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRegBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.server.converter;

import com.hd123.m3.account.gwt.acc.server.Acc1BizConverter;
import com.hd123.m3.account.gwt.acc.server.Acc2BizConverter;
import com.hd123.m3.account.gwt.commons.server.TotalBizConverter;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BIvcRegLine;
import com.hd123.m3.account.service.ivc.reg.InvoiceReg;
import com.hd123.m3.account.service.ivc.reg.IvcRegLine;
import com.hd123.m3.commons.gwt.base.server.convert.EntityBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * InvoiceReg-->BInvoiceReg.
 * 
 * @author chenrizhang
 * @since 1.0
 * 
 */
public class InvoiceRegBizConverter extends EntityBizConverter<InvoiceReg, BInvoiceReg> {

  @Override
  public BInvoiceReg convert(InvoiceReg source) throws ConversionException {
    if (source == null) {
      return null;
    }
    BInvoiceReg target = new BInvoiceReg();
    inject(source, target);
    target.setAccountUnit(new UCNBizConverter().convert(source.getAccountUnit()));
    target.setCounterpart(new UCNBizConverter().convert(source.getCounterpart()));
    target.setCounterpartType(source.getCounterpartType());
    target.setDirection(source.getDirection());
    target.setRegDate(source.getRegDate());
    target.setAllowSplitReg(source.isAllowSplitReg());
    target.setUseInvoiceStock(source.isUseInvoiceStock());
    target.setInvoiceType(source.getInvoiceType());
    target.setInvoiceTotal(TotalBizConverter.getInstance().convert(source.getInvoiceTotal()));
    target.setAccountTotal(TotalBizConverter.getInstance().convert(source.getAccountTotal()));
    target.setTotalDiff(source.getTotalDiff());
    target.setTaxDiff(source.getTaxDiff());
    target.setRemark(source.getRemark());
    target.setPrinted(source.isPrinted());
    for (IvcRegLine s : source.getRegLines()) {
      BIvcRegLine t = new BIvcRegLine();
      t.setUuid(s.getUuid());
      t.setAcc1(Acc1BizConverter.getInstance().convert(s.getAcc1()));
      t.setAcc2(Acc2BizConverter.getInstance().convert(s.getAcc2()));
      t.setInvoiceCode(s.getInvoiceCode());
      t.setInvoiceNumber(s.getInvoiceNumber());
      t.setInvoiceType(s.getInvoiceType());
      t.setOriginTotal(TotalBizConverter.getInstance().convert(s.getOriginTotal()));
      t.setTotal(TotalBizConverter.getInstance().convert(s.getTotal()));
      t.setUnregTotal(TotalBizConverter.getInstance().convert(s.getUnregTotal()));
      t.setTotalDiff(s.getTotalDiff());
      t.setTaxDiff(s.getTaxDiff());
      t.setRemark(s.getRemark());
      target.getRegLines().add(t);
    }
    return target;
  }

}
