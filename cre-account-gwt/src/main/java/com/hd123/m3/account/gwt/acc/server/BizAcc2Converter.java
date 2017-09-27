/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BizAcc2Converter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.acc.server;

import com.hd123.m3.account.gwt.acc.client.BAcc2;
import com.hd123.m3.account.gwt.commons.server.BizBillConverter;
import com.hd123.m3.account.gwt.commons.server.BizInvoiceBillConverter;
import com.hd123.m3.account.gwt.commons.server.BizSourceBillConverter;
import com.hd123.m3.account.service.acc.Acc2;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author chenwenfeng
 * 
 */
public class BizAcc2Converter implements Converter<BAcc2, Acc2> {

  private static BizAcc2Converter instance = null;

  public static BizAcc2Converter getInstance() {
    if (instance == null)
      instance = new BizAcc2Converter();
    return instance;
  }

  @Override
  public Acc2 convert(BAcc2 source) throws ConversionException {
    if (source == null) {
      return null;
    }
    Acc2 target = new Acc2();
    target.setGraceDays(source.getGraceDays());
    target.setId(source.getId());
    target.setInvoice(BizInvoiceBillConverter.getInstance().convert(source.getInvoice()));
    target.setLastPayDate(source.getLastPayDate());
    target.setLocker(BizSourceBillConverter.getInstance().convert(source.getLocker()));
    target.setPayment(BizBillConverter.getInstance().convert(source.getPayment()));
    target.setStatement(BizBillConverter.getInstance().convert(source.getStatement()));
    target.setId(target.bizId().toId());
    return target;
  }

}
