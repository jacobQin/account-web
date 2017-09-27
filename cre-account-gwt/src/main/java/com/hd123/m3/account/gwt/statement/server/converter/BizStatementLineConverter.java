/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BizStatementLineConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-28 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.statement.server.converter;

import com.hd123.m3.account.gwt.acc.server.BizAcc1Converter;
import com.hd123.m3.account.gwt.acc.server.BizAcc2Converter;
import com.hd123.m3.account.gwt.commons.server.BizTotalConverter;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementLine;
import com.hd123.m3.account.service.acc.AccountSourceType;
import com.hd123.m3.account.service.statement.StatementLine;
import com.hd123.rumba.commons.gwt.entity.server.EntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * 账单明细行转换器
 * <p>
 * BStatementLine->StatementLine
 * 
 * @author chenpeisi
 * 
 */
public class BizStatementLineConverter extends EntityConverter<BStatementLine, StatementLine> {
  private static BizStatementLineConverter instance = null;

  public static BizStatementLineConverter getInstance() {
    if (instance == null)
      instance = new BizStatementLineConverter();
    return instance;
  }

  @Override
  public StatementLine convert(BStatementLine source) throws ConversionException {
    if (source == null)
      return null;

    StatementLine target = new StatementLine();
    target.inject(source);
    target.setUuid(source.getUuid());
    target.setAccSrcType(AccountSourceType.valueOf(source.getAccSrcType()));
    target.setAcc1(BizAcc1Converter.getInstance().convert(source.getAcc1()));
    target.setAcc2(BizAcc2Converter.getInstance().convert(source.getAcc2()));
    target.setTotal(BizTotalConverter.getInstance().convert(source.getTotal()));
    target.getTotal().setTotal(target.getTotal().getTotal());
    target.setFreeTotal(BizTotalConverter.getInstance().convert(source.getFreeTotal()));
    target.setFromStatement(source.isFromStatement());
    target.setSettlementId(source.getSettlementId());
    target.setRemark(source.getRemark());
    return target;
  }

}
