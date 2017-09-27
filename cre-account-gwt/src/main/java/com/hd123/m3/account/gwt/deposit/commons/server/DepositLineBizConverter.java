/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BCollectionLineConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.commons.server;

import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositLine;
import com.hd123.m3.account.service.deposit.DepositLine;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.gwt.entity.server.BEntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author chenpeisi
 * 
 */
public class DepositLineBizConverter extends BEntityConverter<DepositLine, BDepositLine> {

  private static DepositLineBizConverter instance = null;

  public static DepositLineBizConverter getInstance() {
    if (instance == null)
      instance = new DepositLineBizConverter();
    return instance;
  }

  @Override
  public BDepositLine convert(DepositLine source) throws ConversionException {
    if (source == null)
      return null;

    try {
      BDepositLine target = new BDepositLine();
      super.inject(source, target);

      target.setUuid(source.getUuid());
      target.setLineNumber(source.getLineNumber());
      target.setSubject(UCNBizConverter.getInstance().convert(source.getSubject()));
      target.setTotal(source.getAmount());
      target.setRemainTotal(source.getRemainTotal());
      target.setRemark(source.getRemark());
      target.setContractTotal(source.getContractTotal());

      return target;
    } catch (Exception e) {
      throw new ConversionException(e);
    }
  }
}
