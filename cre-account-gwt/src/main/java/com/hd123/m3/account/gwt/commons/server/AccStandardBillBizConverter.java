/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BStandardBillConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.commons.server;

import com.hd123.m3.account.commons.AccStandardBill;
import com.hd123.m3.account.gwt.commons.client.biz.BAccStandardBill;
import com.hd123.m3.commons.gwt.base.server.convert.EntityBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author chenwenfeng
 * 
 */
public abstract class AccStandardBillBizConverter<S extends AccStandardBill, T extends BAccStandardBill>
    extends EntityBizConverter<S, T> {

  @Override
  public void inject(S source, T target) throws IllegalArgumentException, ConversionException {
    super.inject(source, target);
    target.setSettleNo(source.getSettleNo());
    target.setRemark(source.getRemark());
  }

}
