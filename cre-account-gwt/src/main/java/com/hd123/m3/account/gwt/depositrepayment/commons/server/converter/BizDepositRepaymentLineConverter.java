/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	DepositRepaymentLineBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.commons.server.converter;

import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepaymentLine;
import com.hd123.m3.account.service.depositrepayment.DepositRepaymentLine;
import com.hd123.m3.commons.gwt.base.server.convert.BizEntityConverter;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author chenpeisi
 * 
 */
public class BizDepositRepaymentLineConverter extends
    BizEntityConverter<BDepositRepaymentLine, DepositRepaymentLine> {

  private static BizDepositRepaymentLineConverter instance = null;

  public static BizDepositRepaymentLineConverter getInstnce() {
    if (instance == null)
      instance = new BizDepositRepaymentLineConverter();
    return instance;
  }

  @Override
  public DepositRepaymentLine convert(BDepositRepaymentLine source) throws ConversionException {
    if (source == null)
      return null;

    try {
      DepositRepaymentLine target = new DepositRepaymentLine();
      target.inject(source);

      target.setUuid(source.getUuid());
      target.setAmount(source.getAmount());
      target.setLineNumber(source.getLineNumber());
      target.setRemark(source.getRemark());
      target.setSubject(BizUCNConverter.getInstance().convert(source.getSubject()));

      return target;
    } catch (Exception e) {
      throw new ConversionException(e.getMessage());
    }
  }
}
