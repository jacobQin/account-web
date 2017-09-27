/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BizDepositLineConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-20 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.commons.server;

import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositLine;
import com.hd123.m3.account.service.deposit.DepositLine;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.gwt.entity.server.EntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author chenpeisi
 * 
 */
public class BizDepositLineConverter extends EntityConverter<BDepositLine, DepositLine> {
  private static BizDepositLineConverter instance;

  public static BizDepositLineConverter getInstance() {
    if (instance == null)
      instance = new BizDepositLineConverter();
    return instance;
  }

  @Override
  public DepositLine convert(BDepositLine source) throws ConversionException {
    if (source == null)
      return null;

    DepositLine target = new DepositLine();

    super.inject(source, target);

    target.setUuid(source.getUuid());
    target.setLineNumber(source.getLineNumber());
    target.setRemark(source.getRemark());
    target.setSubject(BizUCNConverter.getInstance().convert(source.getSubject()));
    target.setAmount(source.getTotal());
    target.setRemainTotal(source.getRemainTotal());
    target.setContractTotal(source.getContractTotal());

    return target;
  }
}
