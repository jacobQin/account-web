/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BizStatementAdjustLineConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-6 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.server.converter;

import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.server.BizTotalConverter;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjustLine;
import com.hd123.m3.account.service.statement.adjust.StatementAdjustLine;
import com.hd123.m3.commons.biz.date.DateInterval;
import com.hd123.m3.commons.gwt.base.server.tax.BizTaxRateConverter;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.gwt.entity.server.EntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author zhuhairui
 * 
 */
public class BizStatementAdjustLineConverter extends
    EntityConverter<BStatementAdjustLine, StatementAdjustLine> {

  private static BizStatementAdjustLineConverter instance;

  public static BizStatementAdjustLineConverter getInstance() {
    if (instance == null)
      instance = new BizStatementAdjustLineConverter();
    return instance;
  }

  @Override
  public StatementAdjustLine convert(BStatementAdjustLine source) throws ConversionException {
    if (source == null)
      return null;

    StatementAdjustLine target = new StatementAdjustLine();

    target.setSubject(BizUCNConverter.getInstance().convert(source.getSubject()));
    target.setAmount(BizTotalConverter.getInstance().convert(
        new BTotal(source.getTotal().getTotal(), source.getTotal().getTax())));
    target.setTaxRate(BizTaxRateConverter.getInstance().convert(source.getTaxRate()));
    target.setDirection(source.getDirection());
    target.setInvoice(source.isInvoice());
    target.setDateRange(new DateInterval(source.getBeginDate(), source.getEndDate()));
    target.setRemark(source.getRemark());
    target.setSourceAccountId(source.getSourceAccountId());
    target.setAccountDate(source.getAccountDate());
    target.setLastPayDate(source.getLastPayDate());

    return target;
  }

}
