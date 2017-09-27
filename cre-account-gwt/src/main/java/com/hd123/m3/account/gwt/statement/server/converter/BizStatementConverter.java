/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BizStatementConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-28 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.statement.server.converter;

import com.hd123.m3.account.gwt.commons.server.BizAccStandardBillConverter;
import com.hd123.m3.account.gwt.commons.server.BizTotalConverter;
import com.hd123.m3.account.gwt.statement.client.biz.BStatement;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementAccRange;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementLine;
import com.hd123.m3.account.service.statement.SettleState;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementAccRange;
import com.hd123.m3.account.service.statement.StatementType;
import com.hd123.m3.commons.biz.date.DateInterval;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.ConverterUtil;

/**
 * 账单转换器
 * <p>
 * BStatement->Statement
 * 
 * @author chenpeisi
 * 
 */
public class BizStatementConverter extends BizAccStandardBillConverter<BStatement, Statement> {

  private static BizStatementConverter instance = null;

  public static BizStatementConverter getInstance() {
    if (instance == null)
      instance = new BizStatementConverter();
    return instance;
  }

  @Override
  public Statement convert(BStatement source) throws ConversionException {
    if (source == null)
      return null;

    Statement target = new Statement();
    super.inject(source, target);
    BizUCNConverter ucnCvt = BizUCNConverter.getInstance();
    BizTotalConverter totalCvt = BizTotalConverter.getInstance();
    target.setAccountUnit(ucnCvt.convert(source.getAccountUnit()));
    target.setCounterpart(ucnCvt.convert(source.getCounterpart()));
    target.setCounterpartType(source.getCounterpart() == null ? null : source.getCounterpart()
        .getCounterpartType());
    target.setContract(BizUCNConverter.getInstance().convert(source.getContract()));
    target.setType(StatementType.valueOf(source.getType()));
    target.setSettleState(SettleState.valueOf(source.getSettleState()));
    target.setReceiptAccDate(source.getReceiptAccDate());
    target.setPayTotal(totalCvt.convert(source.getPayTotal()));
    target.setFreePayTotal(totalCvt.convert(source.getFreePayTotal()));
    target.setIvcPayTotal(totalCvt.convert(source.getIvcPayTotal()));
    target.setReceiptTotal(totalCvt.convert(source.getReceiptTotal()));
    target.setFreeReceiptTotal(totalCvt.convert(source.getFreeReceiptTotal()));
    target.setIvcReceiptTotal(totalCvt.convert(source.getIvcReceiptTotal()));
    target.setAccountTime(source.getAccountTime());
    target.setPlanDate(source.getPlanDate());
    target.setSaleTotal(source.getSaleTotal());
    target.setCoopMode(source.getCoopMode());
    target.setAccountType(source.getAccountType());

    target.setPayAdj(source.getPayAdj());
    target.setPayed(source.getPayed());
    target.setIvcPayAdj(source.getIvcPayAdj());
    target.setIvcPayed(source.getIvcPayed());

    target.setReceiptAdj(source.getReceiptAdj());
    target.setReceipted(source.getReceipted());
    target.setIvcReceiptAdj(source.getIvcReceiptAdj());
    target.setIvcReceipted(source.getIvcReceipted());

    for (BStatementAccRange sr : source.getRanges()) {
      StatementAccRange r = new StatementAccRange();
      r.setSettlementId(sr.getSettlementId());
      r.setCaption(sr.getCaption());
      r.setDateRange(new DateInterval(sr.getDateRange().getBeginDate(), sr.getDateRange().getEndDate()));
      target.getRanges().add(r);
    }
    for (BStatementLine sl : source.getLines()) {
      if (sl.isValid() == false)
        continue;
      target.getLines().add(BizStatementLineConverter.getInstance().convert(sl));
    }
    target.setAttachs(ConverterUtil.convert(source.getAttachs(),
        BizAttachmentConverter.getInstnce()));
    return target;
  }
}
