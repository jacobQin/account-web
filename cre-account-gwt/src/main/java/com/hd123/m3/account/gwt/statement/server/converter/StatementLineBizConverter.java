/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementLineBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-1 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.server.converter;

import com.hd123.m3.account.gwt.acc.server.Acc1BizConverter;
import com.hd123.m3.account.gwt.acc.server.Acc2BizConverter;
import com.hd123.m3.account.gwt.commons.server.TotalBizConverter;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementLine;
import com.hd123.m3.account.service.statement.StatementLine;
import com.hd123.rumba.commons.gwt.entity.server.BEntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * 账单明细行转换器
 * <p>
 * StatementLine->BStatementLine
 * 
 * @author huangjunxian
 * 
 */
public class StatementLineBizConverter extends BEntityConverter<StatementLine, BStatementLine> {

  private static StatementLineBizConverter instance = null;

  public static StatementLineBizConverter getInstnce() {
    if (instance == null)
      instance = new StatementLineBizConverter();
    return instance;
  }

  @Override
  public BStatementLine convert(StatementLine source) throws ConversionException {
    if (source == null)
      return null;

    BStatementLine target = new BStatementLine();
    target.inject(source);
    target.setUuid(source.getUuid());
    target.setAccSrcType(source.getAccSrcType() == null ? null : source.getAccSrcType().name());
    target.setAcc1(Acc1BizConverter.getInstance().convert(source.getAcc1()));
    target.setAcc2(Acc2BizConverter.getInstance().convert(source.getAcc2()));
    target.setTotal(TotalBizConverter.getInstance().convert(source.getTotal()));
    target.setFreeTotal(TotalBizConverter.getInstance().convert(source.getFreeTotal()));
    target.setFromStatement(source.isFromStatement());
    target.setRemark(source.getRemark());
    return target;
  }

}
