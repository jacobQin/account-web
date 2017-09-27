/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BAccountDefrayalConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月30日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.accountdefrayal.convert;

import java.math.BigDecimal;

import com.hd123.m3.account.service.report.accountdefrayal.AccountDefrayal;
import com.hd123.m3.cre.controller.account.accountdefrayal.model.BAccountDefrayal;

/**
 * @author chenganbang
 *
 */
public class BAccountDefrayalConverter {
  private static BAccountDefrayalConverter instance = null;

  private BAccountDefrayalConverter() {
  }

  public static BAccountDefrayalConverter getInstance() {
    if (instance == null) {
      instance = new BAccountDefrayalConverter();
    }
    return instance;
  }

  public BAccountDefrayal convert(AccountDefrayal source) {
    BAccountDefrayal result = new BAccountDefrayal();

    result.setUuid(source.getAcc1() != null ? source.getAcc1().getId() : null);
    result.setContract(source.getAcc1() != null && source.getAcc1().getContract() != null ? source
        .getAcc1().getContract() : null);
    result.setAccountUnit(source.getAcc1().getAccountUnit());
    result
        .setCounterpart(source.getAcc1() != null && source.getAcc1().getCounterpart() != null ? source
            .getAcc1().getCounterpart() : null);
    result.setCounterpartType(source.getAcc1() != null
        && source.getAcc1().getCounterpartType() != null ? source.getAcc1().getCounterpartType()
        : null);
    result.setSubject(source.getAcc1() != null && source.getAcc1().getSubject() != null ? source
        .getAcc1().getSubject() : null);
    result.setDirection(source.getAcc1() != null ? source.getAcc1().getDirection() : 0);
    result.setNeedSettle(source.getNeedSettle() != null
        && source.getNeedSettle().getTotal() != null ? source.getNeedSettle().getTotal()
        : BigDecimal.ZERO);
    result
        .setSettled(source.getSettled() != null && source.getSettled().getTotal() != null ? source
            .getSettled().getTotal() : BigDecimal.ZERO);
    result.setOwedAmount(result.getNeedSettle().subtract(result.getSettled()));
    result
        .setInvoiced(source.getInvoiced() != null && source.getInvoiced().getTotal() != null ? source
            .getInvoiced().getTotal() : BigDecimal.ZERO);
    result.setLastPayDate(source.getLastPayDate());
    result.setSrcBill(source.getAcc1() != null && source.getAcc1().getSourceBill() != null ? source
        .getAcc1().getSourceBill() : null);
    result.setStatement(source.getStatement());
    result.setSettlement(source.getAcc1().getBeginTime(), source.getAcc1().getEndTime());
    result.setAccountDate(source.getAcc1().getAccountDate());

    return result;
  }
}
