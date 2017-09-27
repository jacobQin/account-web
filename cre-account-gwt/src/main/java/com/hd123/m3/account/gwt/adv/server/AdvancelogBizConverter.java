/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AdvancelogBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.adv.server;

import com.hd123.m3.account.gwt.adv.client.biz.BAdvanceLog;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.commons.server.SourceBillBizConverter;
import com.hd123.m3.account.service.adv.AdvanceLog;
import com.hd123.m3.commons.gwt.base.server.convert.EntityBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * 预存款账户日志转换器
 * <p>
 * AdvanceLog->BAdvanceLog
 * 
 * @author zhuhairui
 * 
 */
public class AdvancelogBizConverter extends EntityBizConverter<AdvanceLog, BAdvanceLog> {

  public static AdvancelogBizConverter getInstance() {
    if (instance == null)
      instance = new AdvancelogBizConverter();
    return instance;
  }

  public AdvancelogBizConverter() {
  }

  @Override
  public BAdvanceLog convert(AdvanceLog source) throws ConversionException {
    if (source == null)
      return null;

    try {
      BAdvanceLog target = new BAdvanceLog();
      target.setTime(source.getTime());
      target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
      target.setCounterpart(BCounterpartConverter.getInstance().convert(source.getCounterpart(),
          source.getCounterpartType()));
      target.setContractBillNum(source.getBill() == null ? null : source.getBill().getCode());
      target.setSourceBill(SourceBillBizConverter.getInstance().convert(source.getSourceBill()));
      target.setSubject(UCNBizConverter.getInstance().convert(source.getSubject()));
      target.setBeforeTotal(source.getBeforeTotal());
      target.setTotal(source.getTotal());
      target.setAfterTotal(source.getAfterTotal());

      return target;
    } catch (Exception e) {
      throw new ConversionException(e.getMessage());
    }
  }

  private static AdvancelogBizConverter instance;
}
