/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-web
 * 文件名：	BAccountSettleLogConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月9日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.convert;

import com.hd123.m3.account.service.contract.AccountSettle;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.m3.cre.controller.account.statement.model.BAccountSettleLog;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.gwt.base.client.BUCN;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenganbang
 *
 */
public class BAccountSettleLogConverter {
  private static BAccountSettleLogConverter instance = null;

  private BAccountSettleLogConverter() {
  }

  public static BAccountSettleLogConverter getInstance() {
    if (instance == null) {
      instance = new BAccountSettleLogConverter();
    }
    return instance;
  }

  public QueryResult<BAccountSettleLog> convert(QueryResult<AccountSettle> source) {
    QueryResult<BAccountSettleLog> result = new QueryResult<BAccountSettleLog>();
    if (source != null) {
      List<AccountSettle> settles = source.getRecords();
      List<BAccountSettleLog> logs = new ArrayList<BAccountSettleLog>();
      for (AccountSettle settle : settles) {
        BAccountSettleLog log = new BAccountSettleLog();

        log.setUuid(settle.getUuid());
        log.setAccountUnit(settle.getSettlement() == null ? null : settle.getSettlement()
            .getAccountUnit() == null ? null : UCNBizConverter.getInstance().convert(
            settle.getSettlement().getAccountUnit()));
        log.setCounterpart(settle.getContract() == null ? null : settle.getContract()
            .getCounterpart() == null ? null : UCNBizConverter.getInstance().convert(
            settle.getContract().getCounterpart()));
        log.setCounterpartType(settle.getContract().getCounterpartType());
        BUCN contract = new BUCN(settle.getContract().getUuid(), settle.getContract()
            .getBillNumber(), settle.getContract().getTitle());
        log.setContract(contract);
        log.setFloor(settle.getContract() == null ? null
            : settle.getContract().getFloor() == null ? null : UCNBizConverter.getInstance()
                .convert(settle.getContract().getFloor()));
        log.setCoopMode(settle.getContract() == null ? null : settle.getContract().getCoopMode());
        log.setSettlementCaption(settle.getSettlement() == null ? null : settle.getSettlement()
            .getCaption());
        log.setSettlementDateRange(new BDateRange(settle.getBeginDate(), settle.getEndDate()));
        log.setPlanDate(settle.getPlanDate());
        log.setAccountTime(settle.getAccountTime());
        log.setBillCalculateType(settle.getSettlement() == null ? null : settle.getSettlement()
            .getBillCalculateType().name());
        log.setStatementBillNumber(settle.getBill() == null ? null : settle.getBill()
            .getBillNumber());
        log.setStatementBillUuid(settle.getBill() == null ? null : settle.getBill().getBillUuid());
        log.setEmpty(Contracts.EMPTY_BILL_UUID.equals(settle.getBill().getBillUuid()));

        logs.add(log);
      }

      result.setPage(source.getPage());
      result.setPageCount(source.getPageCount());
      result.setRecordCount(source.getRecordCount());
      result.setPageSize(source.getPageSize());
      result.setRecords(logs);
    }
    return result;
  }
}
