/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	Acc2BizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.acc.server;

import com.hd123.m3.account.gwt.acc.client.BAcc2;
import com.hd123.m3.account.gwt.commons.server.BillBizConverter;
import com.hd123.m3.account.gwt.commons.server.InvoiceBillBizConverter;
import com.hd123.m3.account.gwt.commons.server.SourceBillBizConverter;
import com.hd123.m3.account.service.acc.Acc2;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author chenwenfeng
 * 
 */
public class Acc2BizConverter implements Converter<Acc2, BAcc2> {

  private static Acc2BizConverter instance = null;

  public static Acc2BizConverter getInstance() {
    if (instance == null)
      instance = new Acc2BizConverter();
    return instance;
  }

  @Override
  public BAcc2 convert(Acc2 source) throws ConversionException {
    if (source == null) {
      return null;
    }
    BAcc2 target = new BAcc2();
    target.setGraceDays(source.getGraceDays());
    target.setId(source.getId());
    target.setInvoice(InvoiceBillBizConverter.getInstance().convert(source.getInvoice()));
    target.setLastPayDate(source.getLastPayDate());
    target.setLocker(SourceBillBizConverter.getInstance().convert(source.getLocker()));
    target.setPayment(BillBizConverter.getInstance().convert(source.getPayment()));
    target.setStatement(BillBizConverter.getInstance().convert(source.getStatement()));
    return target;
  }

}
