/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： FeeSearchPage.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-16 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.server;

import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.commons.server.AccStandardBillBizConverter;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.fee.client.biz.BFee;
import com.hd123.m3.account.service.fee.Fee;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author subinzhu
 * 
 */
public class FeeBizConverter extends AccStandardBillBizConverter<Fee, BFee> {

  private static FeeBizConverter instance = null;

  public static FeeBizConverter getInstance() {
    if (instance == null)
      instance = new FeeBizConverter();
    return instance;
  }

  private boolean convertLine = false;

  public FeeBizConverter() {
    super();
  }

  public FeeBizConverter(boolean convertLine) {
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
  public BFee convert(Fee source) throws ConversionException {
    if (source == null)
      return null;

    BFee target = new BFee();
    super.inject(source, target);

    BContract contract = new BContract();
    if (source.getContract() != null) {
      contract.setUuid(source.getContract().getUuid());
      contract.setBillNumber(source.getContract().getCode());
      contract.setTitle(source.getContract().getName());
    }
    target.setContract(contract);
    if (source.getDateRange() != null)
      target.setDateRange(new BDateRange(source.getDateRange().getBeginDate() == null ? null
          : source.getDateRange().getBeginDate(), source.getDateRange().getEndDate() == null ? null
          : source.getDateRange().getEndDate()));
    else
      target.setDateRange(new BDateRange());
    target.setAccountUnit(convertUCN(source.getAccountUnit()));
    target.setCounterpart(BCounterpartConverter.getInstance().convert(source.getCounterpart(),
        source.getCounterpartType()));
    target.setAccountDate(source.getAccountDate());
    target.setDirection(source.getDirection() == 0 ? DirectionType.receipt.getDirectionValue()
        : source.getDirection());
    target.setGenerateStatement(source.isGenerateStatement());

    if (source.getTotal() != null) {
      BTotal total = new BTotal();
      total.setTotal(source.getTotal().getTotal());
      total.setTax(source.getTotal().getTax());
      target.setTotal(total);
    } else {
      target.setTotal(new BTotal());
    }

    target.setDealer(source.getDealer());

    if (convertLine) {
      target.setLines(ConverterUtil.convert(source.getLines(), FeeLineBizConverter.getInstance()));
    }

    return target;
  }
}
