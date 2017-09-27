/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementAdjustConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-18 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.server.converter;

import java.math.BigDecimal;

import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.server.AccStandardBillBizConverter;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.commons.server.BillBizConverter;
import com.hd123.m3.account.gwt.commons.server.TotalBizConverter;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjustLine;
import com.hd123.m3.account.service.statement.adjust.StatementAdjust;
import com.hd123.m3.account.service.statement.adjust.StatementAdjustLine;
import com.hd123.m3.commons.gwt.base.server.tax.TaxRateBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;

/**
 * 账单调整单转换器
 * <p>
 * StatementAdjust->BStatementAdjust
 * 
 * @author zhuhairui
 * 
 */
public class StatementAdjustBizConverter extends
    AccStandardBillBizConverter<StatementAdjust, BStatementAdjust> {

  private static StatementAdjustBizConverter instance;

  public static StatementAdjustBizConverter getInstance() {
    if (instance == null)
      instance = new StatementAdjustBizConverter();
    return instance;
  }

  @Override
  public BStatementAdjust convert(StatementAdjust source) {
    if (source == null)
      return null;

    BStatementAdjust target = new BStatementAdjust();
    inject(source, target);
    target.inject(source);
    target.setUuid(source.getUuid());
    target.setVersion(source.getVersion());
    target.setBillNumber(source.getBillNumber());
    target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
    target.setCounterpart(BCounterpartConverter.getInstance().convert(source.getCounterpart(),
        source.getCounterpartType()));
    BContract contract = new BContract();
    if (source.getContract() != null) {
      contract.setUuid(source.getContract().getUuid());
      contract.setBillNumber(source.getContract().getCode());
      contract.setName(source.getContract().getName());
    }
    target.setBcontract(contract);
    target.setStatement(BillBizConverter.getInstance().convert(source.getStatement()));
    target.setBizState(source.getBizState());

    target.setReceiptTotal(source.getReceiptTotal() != null ? source.getReceiptTotal().getTotal()
        : BigDecimal.ZERO);
    target.setReceiptTax(source.getReceiptTotal() != null ? source.getReceiptTotal().getTax()
        : BigDecimal.ZERO);

    target.setPaymentTotal(source.getPaymentTotal() != null ? source.getPaymentTotal().getTotal()
        : BigDecimal.ZERO);
    target.setPaymentTax(source.getPaymentTotal() != null ? source.getPaymentTotal().getTax()
        : BigDecimal.ZERO);
    target.setRemark(source.getRemark());

    for (StatementAdjustLine line : source.getLines()) {
      target.getLines().add(convertStatementAdjustLine(line));
    }
    return target;
  }

  private BStatementAdjustLine convertStatementAdjustLine(StatementAdjustLine source) {
    if (source == null)
      return null;

    BStatementAdjustLine target = new BStatementAdjustLine();
    target.setLineNumber(source.getLineNumber());
    target.setSubject(UCNBizConverter.getInstance().convert(source.getSubject()));
    target.setDirection(source.getDirection());
    target.setSourceAccountId(source.getSourceAccountId());
    target.setAccountDate(source.getAccountDate());
    target.setLastPayDate(source.getLastPayDate());

    if (source.getAmount() != null) {
      target.setTotal(TotalBizConverter.getInstance().convert(source.getAmount()));
    } else {
      target.setTotal(new BTotal());
    }

    target.setTaxRate(TaxRateBizConverter.getInstance().convert(source.getTaxRate()));
    target.setInvoice(source.isInvoice());
    target
        .setBeginDate(source.getDateRange() == null ? null : source.getDateRange().getBeginDate());
    target.setEndDate(source.getDateRange() == null ? null : source.getDateRange().getEndDate());
    target.setRemark(source.getRemark());
    return target;
  }

}
