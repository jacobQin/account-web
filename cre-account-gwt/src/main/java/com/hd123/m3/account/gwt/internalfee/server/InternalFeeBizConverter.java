/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InternalFeeBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.server;

import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.commons.server.AccStandardBillBizConverter;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFee;
import com.hd123.m3.account.service.internalfee.InternalFee;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author liuguilin
 * 
 */
public class InternalFeeBizConverter extends AccStandardBillBizConverter<InternalFee, BInternalFee> {

  private static InternalFeeBizConverter instance = null;

  public static InternalFeeBizConverter getInstance() {
    if (instance == null)
      instance = new InternalFeeBizConverter();
    return instance;
  }

  private boolean convertLine = false;

  public InternalFeeBizConverter() {
    super();
  }

  public InternalFeeBizConverter(boolean convertLine) {
    super();
    this.convertLine = convertLine;
  }

  public void setConvertLine(boolean convertLine) {
    this.convertLine = convertLine;
  }

  private BUCN convertUCN(UCN source) throws ConversionException {
    if (source == null)
      return null;

    BUCN target = new BUCN();
    target.setUuid(source.getUuid());
    target.setCode(source.getCode());
    target.setName(source.getName());

    return target;
  }

  @Override
  public BInternalFee convert(InternalFee source) throws ConversionException {
    if (source == null)
      return null;

    BInternalFee target = new BInternalFee();
    super.inject(source, target);
    target.setAccountDate(source.getAccountDate());
    target.setBeginDate(source.getBeginDate());
    target.setEndDate(source.getEndDate());
    target.setDirection(source.getDirection() == 0 ? DirectionType.receipt.getDirectionValue()
        : source.getDirection());
    target.setVendor(convertUCN(source.getVendor()));
    target.setStore(convertUCN(source.getStore()));

    if (source.getTotal() != null) {
      BTotal total = new BTotal();
      total.setTotal(source.getTotal().getTotal());
      total.setTax(source.getTotal().getTax());
      target.setTotal(total);
    } else {
      target.setTotal(new BTotal());
    }

    if (convertLine) {
      target.setLines(ConverterUtil.convert(source.getLines(),
          InternalFeeLineBizConverter.getInstance()));
    }

    return target;
  }

}
