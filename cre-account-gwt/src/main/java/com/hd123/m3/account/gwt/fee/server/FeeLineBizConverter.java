/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： BFeeLineConverter.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-16 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.server;

import com.hd123.m3.account.gwt.fee.client.biz.BFeeLine;
import com.hd123.m3.account.service.fee.FeeLine;
import com.hd123.m3.commons.gwt.base.server.tax.TaxRateBizConverter;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.gwt.entity.server.BEntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author subinzhu
 * 
 */
public class FeeLineBizConverter extends BEntityConverter<FeeLine, BFeeLine> {

  private static FeeLineBizConverter instance;

  public static FeeLineBizConverter getInstance() {
    if (instance == null)
      instance = new FeeLineBizConverter();
    return instance;
  }

  @Override
  public BFeeLine convert(FeeLine source) throws ConversionException {
    if (source == null)
      return null;

    BFeeLine target = new BFeeLine();
    super.inject(source, target);

    try {
      target.setLineNumber(source.getLineNumber());
      target.setSubject(UCNBizConverter.getInstance().convert(source.getSubject()));
      target.getTotal().setValue(source.getTotal() == null ? null : source.getTotal().getTotal());
      target.setTaxRate(TaxRateBizConverter.getInstance().convert(source.getTaxRate()));
      target.setTax(source.getTotal() == null ? null : source.getTotal().getTax());
      target.setIssueInvoice(source.isIssueInvoice());
      target.setAccountId(source.getAccountId());
      target.setRemark(source.getRemark());
      target.setDateRange(new BDateRange(source.getBeginDate(), source.getEndDate()));
      return target;
    } catch (Exception e) {
      throw new ConversionException(e);
    }
  }
}