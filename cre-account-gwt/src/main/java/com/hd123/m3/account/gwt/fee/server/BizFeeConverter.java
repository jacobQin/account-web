/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BizFeeConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-17 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.fee.server;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.gwt.commons.server.BizAccStandardBillConverter;
import com.hd123.m3.account.gwt.fee.client.biz.BFee;
import com.hd123.m3.account.service.fee.Fee;
import com.hd123.m3.commons.biz.date.DateInterval;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.ConverterUtil;

/**
 * @author huangjunxian
 * 
 */
public class BizFeeConverter extends BizAccStandardBillConverter<BFee, Fee> {

  private static BizFeeConverter instance = null;

  public static BizFeeConverter getInstance() {
    if (instance == null)
      instance = new BizFeeConverter();
    return instance;
  }

  @Override
  public Fee convert(BFee source) throws ConversionException {
    if (source == null)
      return null;

    Fee target = new Fee();
    super.inject(source, target);
    target.setContract(new UCN(source.getContract().getUuid(),
        source.getContract().getBillNumber(), source.getContract().getTitle()));
    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    target.setCounterpart(BizUCNConverter.getInstance().convert(source.getCounterpart()));
    target.setCounterpartType(source.getCounterpart() == null ? null : source.getCounterpart()
        .getCounterpartType());
    if (source.getDateRange() != null)
      target.setDateRange(new DateInterval(source.getDateRange().getBeginDate(), source.getDateRange()
          .getEndDate()));
    else
      target.setDateRange(null);
    target.setAccountDate(source.getAccountDate());
    target.setDirection(source.getDirection());
    target.setGenerateStatement(source.isGenerateStatement());
    Total total = new Total();
    total.setTotal(source.getTotal().getTotal());
    total.setTax(source.getTotal().getTax());
    target.setTotal(total);
    target.setDealer(source.getDealer());
    target.setLines(ConverterUtil.convert(source.getLines(), BizFeeLineConverter.getInstance()));
    return target;
  }

}
