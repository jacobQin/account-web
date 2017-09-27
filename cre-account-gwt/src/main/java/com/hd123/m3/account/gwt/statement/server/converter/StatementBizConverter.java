/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-24 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.server.converter;

import com.hd123.m3.account.gwt.commons.server.AccStandardBillBizConverter;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.commons.server.TotalBizConverter;
import com.hd123.m3.account.gwt.statement.client.biz.BStatement;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementAccRange;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementAccRange;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.ConverterUtil;

/**
 * 账单转换器
 * <p>
 * Statement->BStatement
 * 
 * @author huangjunxian
 * 
 */
public class StatementBizConverter extends AccStandardBillBizConverter<Statement, BStatement> {

  private static StatementBizConverter instance = null;

  public static StatementBizConverter getInstance() {
    if (instance == null)
      instance = new StatementBizConverter();
    return instance;
  }

  @Override
  public BStatement convert(Statement source) throws ConversionException {
    if (source == null)
      return null;

    try {

      BStatement target = new BStatement();
      StatementBizConverter.getInstance().inject(source, target);
      UCNBizConverter ucnCvt = UCNBizConverter.getInstance();
      target.setAccountUnit(ucnCvt.convert(source.getAccountUnit()));
      target.setCounterpart(BCounterpartConverter.getInstance().convert(source.getCounterpart(),
          source.getCounterpartType()));
      target.setContract(UCNBizConverter.getInstance().convert(source.getContract()));
      target.setType(source.getType().name());
      target
          .setSettleState(source.getSettleState() == null ? null : source.getSettleState().name());
      target.setReceiptAccDate(source.getReceiptAccDate());
      target.setPayTotal(TotalBizConverter.getInstance().convert(source.getPayTotal()));
      target.setFreePayTotal(TotalBizConverter.getInstance().convert(source.getFreePayTotal()));
      target.setIvcPayTotal(TotalBizConverter.getInstance().convert(source.getIvcPayTotal()));
      target.setReceiptTotal(TotalBizConverter.getInstance().convert(source.getReceiptTotal()));
      target.setFreeReceiptTotal(TotalBizConverter.getInstance().convert(
          source.getFreeReceiptTotal()));
      target.setIvcReceiptTotal(TotalBizConverter.getInstance()
          .convert(source.getIvcReceiptTotal()));
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

      for (StatementAccRange sr : source.getRanges()) {
        BStatementAccRange br = new BStatementAccRange();
        br.setCaption(sr.getCaption());
        br.setSettlementId(sr.getSettlementId());
        br.setDateRange(new BDateRange(sr.getDateRange().getBeginDate(), sr.getDateRange()
            .getEndDate()));
        target.getRanges().add(br);
      }
      target.setLines(ConverterUtil.convert(source.getLines(),
          StatementLineBizConverter.getInstnce()));
      target.setShowDesLineInfo(source.getLines().isEmpty()
          && source.getDesActiveLines().isEmpty() == false);
      target.setAttachs(ConverterUtil.convert(source.getAttachs(),
          AttachmentBizConverter.getInstnce()));

      return target;
    } catch (Exception e) {
      throw new ConversionException(e.getMessage());
    }
  }
}
