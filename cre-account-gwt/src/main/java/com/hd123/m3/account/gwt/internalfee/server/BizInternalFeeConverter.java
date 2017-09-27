/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BizInternalFeeConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.server;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.gwt.commons.server.BizAccStandardBillConverter;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFee;
import com.hd123.m3.account.service.internalfee.InternalFee;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.ConverterUtil;

/**
 * @author liuguilin
 * 
 */
public class BizInternalFeeConverter extends BizAccStandardBillConverter<BInternalFee, InternalFee> {

  private static BizInternalFeeConverter instance;

  public static BizInternalFeeConverter getInstance() {
    if (instance == null)
      instance = new BizInternalFeeConverter();
    return instance;
  }

  @Override
  public InternalFee convert(BInternalFee source) throws ConversionException {
    if (source == null)
      return null;

    InternalFee target = new InternalFee();
    super.inject(source, target);

    target.setVendor(BizUCNConverter.getInstance().convert(source.getVendor()));
    target.setStore(BizUCNConverter.getInstance().convert(source.getStore()));
    target.setAccountDate(source.getAccountDate());
    target.setBeginDate(source.getBeginDate());
    target.setEndDate(source.getEndDate());
    target.setDirection(source.getDirection());
    Total total = new Total();
    total.setTotal(source.getTotal().getTotal());
    total.setTax(source.getTotal().getTax());
    target.setTotal(total);
    target.setLines(ConverterUtil.convert(source.getLines(),
        BizInternalFeeLineConverter.getInstance()));

    return target;
  }

}
