/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BizPayDepositRepaymentLineConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-21 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.payment.server.converter;

import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepaymentLine;
import com.hd123.m3.account.service.depositrepayment.DepositRepaymentLine;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.gwt.entity.server.EntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author zhuhairui
 * 
 */
public class BizPayDepositRepaymentLineConverter extends
    EntityConverter<BDepositRepaymentLine, DepositRepaymentLine> {

  private static BizPayDepositRepaymentLineConverter instance;

  public static BizPayDepositRepaymentLineConverter getInstance() {
    if (instance == null)
      instance = new BizPayDepositRepaymentLineConverter();
    return instance;
  }

  @Override
  public DepositRepaymentLine convert(BDepositRepaymentLine source) throws ConversionException {
    if (source == null)
      return null;

    DepositRepaymentLine target = new DepositRepaymentLine();
    target.setUuid(source.getUuid());
    target.setLineNumber(source.getLineNumber());
    target.setRemark(source.getRemark());
    target.setSubject(BizUCNConverter.getInstance().convert(source.getSubject()));
    target.setAmount(source.getAmount());

    return target;
  }

}
