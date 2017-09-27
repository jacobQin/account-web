/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BizInternalFeeLineConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.server;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFeeLine;
import com.hd123.m3.account.service.internalfee.InternalFeeLine;
import com.hd123.m3.commons.gwt.base.server.tax.BizTaxRateConverter;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.gwt.entity.server.EntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author liuguilin
 * 
 */
public class BizInternalFeeLineConverter extends EntityConverter<BInternalFeeLine, InternalFeeLine> {

  private static BizInternalFeeLineConverter instance;

  public static BizInternalFeeLineConverter getInstance() {
    if (instance == null)
      instance = new BizInternalFeeLineConverter();
    return instance;
  }

  @Override
  public InternalFeeLine convert(BInternalFeeLine source) throws ConversionException {
    if (source == null)
      return null;

    InternalFeeLine target = new InternalFeeLine();
    super.inject(source, target);

    if (source.getSubject() != null) {
      target.setSubject(new UCN(source.getSubject().getUuid(), source.getSubject().getCode(),
          source.getSubject().getName()));
    } else {
      target.setSubject(null);
    }

    if (source.getTotal() != null) {
      Total total = new Total();
      total.setTotal(source.getTotal().getTotal());
      total.setTax(source.getTotal().getTax());
      target.setTotal(total);
    } else {
      target.setTotal(new Total());
    }

    target.setTaxRate(BizTaxRateConverter.getInstance().convert(source.getTaxRate()));

    target.setIssueInvoice(source.isIssueInvoice());
    target.setRemark(source.getRemark());

    return target;
  }

}
