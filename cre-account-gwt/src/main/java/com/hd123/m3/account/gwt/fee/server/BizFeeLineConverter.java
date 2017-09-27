/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BizFeeLineConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-17 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.fee.server;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.gwt.fee.client.biz.BFeeLine;
import com.hd123.m3.account.service.fee.FeeLine;
import com.hd123.m3.commons.gwt.base.server.tax.BizTaxRateConverter;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.gwt.entity.server.EntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author huangjunxian
 * 
 */
public class BizFeeLineConverter extends EntityConverter<BFeeLine, FeeLine> {

  private static BizFeeLineConverter instance;

  public static BizFeeLineConverter getInstance() {
    if (instance == null)
      instance = new BizFeeLineConverter();
    return instance;
  }

  @Override
  public FeeLine convert(BFeeLine source) throws ConversionException {
    if (source == null)
      return null;

    FeeLine target = new FeeLine();
    super.inject(source, target);

    try {
      target.setLineNumber(source.getLineNumber());
      if (source.getSubject() != null) {
        target.setSubject(new UCN(source.getSubject().getUuid(), source.getSubject().getCode(),
            source.getSubject().getName()));
      } else {
        target.setSubject(null);
      }

      if (source.getTotal() != null) {
        Total total = new Total();
        total.setTotal(source.getTotal().getValue());
        total.setTax(source.getTax());
        target.setTotal(total);
      } else {
        target.setTotal(new Total());
      }

      target.setTaxRate(BizTaxRateConverter.getInstance().convert(source.getTaxRate()));

      target.setIssueInvoice(source.isIssueInvoice());
      target.setAccountId(source.getAccountId());
      target.setRemark(source.getRemark());
      if (source.getDateRange() != null) {
        target.setBeginDate(source.getDateRange().getBeginDate());
        target.setEndDate(source.getDateRange().getEndDate());
      }

      return target;
    } catch (Exception e) {
      throw new ConversionException(e);
    }
  }

}
