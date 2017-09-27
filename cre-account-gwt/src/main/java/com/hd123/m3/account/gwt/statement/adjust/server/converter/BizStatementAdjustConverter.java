/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BizStatementAdjustConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-6 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.server.converter;

import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.server.BizAccStandardBillConverter;
import com.hd123.m3.account.gwt.commons.server.BizBillConverter;
import com.hd123.m3.account.gwt.commons.server.BizTotalConverter;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjustLine;
import com.hd123.m3.account.service.statement.adjust.StatementAdjust;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author zhuhairui
 * 
 */
public class BizStatementAdjustConverter extends
    BizAccStandardBillConverter<BStatementAdjust, StatementAdjust> {

  private static BizStatementAdjustConverter instance;

  public static BizStatementAdjustConverter getInstance() {
    if (instance == null)
      instance = new BizStatementAdjustConverter();
    return instance;
  }

  @Override
  public StatementAdjust convert(BStatementAdjust source) throws ConversionException {
    if (source == null)
      return null;

    StatementAdjust target = new StatementAdjust();
    super.inject(source, target);
    target.setUuid(source.getUuid());
    target.setVersion(source.getVersion());
    target.setBillNumber(source.getBillNumber());
    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    target.setCounterpart(BizUCNConverter.getInstance().convert(source.getCounterpart()));
    target.setCounterpartType(source.getCounterpart() == null ? null : source.getCounterpart()
        .getCounterpartType());
    target.setContract(new UCN(source.getBcontract().getUuid(), source.getBcontract()
        .getBillNumber(), source.getBcontract().getTitle()));
    target.setStatement(BizBillConverter.getInstance().convert(source.getStatement()));
    target.setReceiptTotal(BizTotalConverter.getInstance().convert(
        new BTotal(source.getReceiptTotal(), source.getReceiptTax())));
    target.setPaymentTotal(BizTotalConverter.getInstance().convert(
        new BTotal(source.getPaymentTotal(), source.getPaymentTax())));
    target.setBizState(source.getBizState());
    target.setRemark(source.getRemark());

    target.getLines().clear();

    for (int i = 0; i < source.getLines().size(); i++) {
      BStatementAdjustLine line = source.getLines().get(i);
      if (line.getSubject() == null || line.getSubject().getUuid() == null)
        continue;
      target.getLines().add(BizStatementAdjustLineConverter.getInstance().convert(line));
    }

    return target;
  }
}
