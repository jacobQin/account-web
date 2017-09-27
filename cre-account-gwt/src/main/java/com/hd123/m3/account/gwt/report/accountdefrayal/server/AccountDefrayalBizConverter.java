/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountDefrayalBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-12-10 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.report.accountdefrayal.server;

import java.math.BigDecimal;

import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.commons.server.BillBizConverter;
import com.hd123.m3.account.gwt.commons.server.SourceBillBizConverter;
import com.hd123.m3.account.gwt.report.accountdefrayal.client.biz.BAccountDefrayal;
import com.hd123.m3.account.service.report.accountdefrayal.AccountDefrayal;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.gwt.entity.server.BEntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * 科目收付情况转换器
 * <p>
 * AccountDefrayal->BAccountDefrayal
 * 
 * @author zhuhairui
 * 
 */
public class AccountDefrayalBizConverter extends
    BEntityConverter<AccountDefrayal, BAccountDefrayal> {

  private static AccountDefrayalBizConverter instance;

  public static AccountDefrayalBizConverter getInstance() {
    if (instance == null)
      instance = new AccountDefrayalBizConverter();
    return instance;
  }

  @Override
  public BAccountDefrayal convert(AccountDefrayal source) throws ConversionException {
    if (source == null)
      return null;

    try {
      BAccountDefrayal target = new BAccountDefrayal();
      target.inject(source);
      target.setSourceBill(SourceBillBizConverter.getInstance().convert(
          source.getAcc1().getSourceBill()));
      target.setSubject(UCNBizConverter.getInstance().convert(source.getAcc1().getSubject()));
      target.setDirection(source.getAcc1().getDirection());
      target.setNeedSettle(source.getNeedSettle() != null
          && source.getNeedSettle().getTotal() != null ? source.getNeedSettle().getTotal()
          : BigDecimal.ZERO);
      target
          .setSettleAdj(source.getSettleAdj() != null && source.getSettleAdj().getTotal() != null ? source
              .getSettleAdj().getTotal() : BigDecimal.ZERO);
      target
          .setSettled(source.getSettled() != null && source.getSettled().getTotal() != null ? source
              .getSettled().getTotal() : BigDecimal.ZERO);
      target.setNeedInvoice(source.getNeedInvoice() != null
          && source.getNeedInvoice().getTotal() != null ? source.getNeedInvoice().getTotal()
          : BigDecimal.ZERO);
      target.setInvoiceAdj(source.getInvoiceAdj() != null
          && source.getInvoiceAdj().getTotal() != null ? source.getInvoiceAdj().getTotal()
          : BigDecimal.ZERO);
      target
          .setInvoiced(source.getInvoiced() != null && source.getInvoiced().getTotal() != null ? source
              .getInvoiced().getTotal() : BigDecimal.ZERO);
      target.setStatement(BillBizConverter.getInstance().convert(source.getStatement()));
      target.setBeginDate(source.getAcc1().getBeginTime());
      target.setEndDate(source.getAcc1().getEndTime());
      target.setLastReceiptDate(source.getLastPayDate());
      target
          .setContract(source.getAcc1() != null && source.getAcc1().getContract() != null ? UCNBizConverter
              .getInstance().convert(source.getAcc1().getContract()) : null);
      target
          .setCounterpart(source.getAcc1() != null && source.getAcc1().getCounterpart() != null ? BCounterpartConverter
              .getInstance().convert(source.getAcc1().getCounterpart(),
                  source.getAcc1().getCounterpartType()) : null);
      return target;
    } catch (Exception e) {
      throw new ConversionException(e);
    }
  }

}
