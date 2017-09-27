/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BizDepositLineConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-20 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.server.converter;

import com.hd123.m3.account.gwt.acc.server.BizAcc1Converter;
import com.hd123.m3.account.gwt.acc.server.BizAcc2Converter;
import com.hd123.m3.account.gwt.commons.server.BizTotalConverter;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegLine;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.invoice.InvoiceRegLine;
import com.hd123.rumba.commons.gwt.entity.server.EntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author chenpeisi
 * 
 */
public class BizInvoiceRegLineConverter extends EntityConverter<BInvoiceRegLine, InvoiceRegLine> {
  private static BizInvoiceRegLineConverter instance;

  public static BizInvoiceRegLineConverter getInstance() {
    if (instance == null)
      instance = new BizInvoiceRegLineConverter();
    return instance;
  }

  @Override
  public InvoiceRegLine convert(BInvoiceRegLine source) throws ConversionException {
    if (source == null)
      return null;

    InvoiceRegLine target = new InvoiceRegLine();
    super.inject(source, target);

    target.setLineNumber(source.getLineNumber());
    target.setAcc1(BizAcc1Converter.getInstance().convert(source.getAcc1()));
    target.setAcc2(BizAcc2Converter.getInstance().convert(source.getAcc2()));
    target.getAcc2().setLocker(Accounts.NONE_LOCKER);
    target.getAcc2().setId(target.getAcc2().bizId().toId());

    target.setRemark(source.getRemark());
    target.setTotal(BizTotalConverter.getInstance().convert(source.getRegTotal()));
    target.setUnregTotal(BizTotalConverter.getInstance().convert(source.getUnregTotal()));

    return target;
  }
}
