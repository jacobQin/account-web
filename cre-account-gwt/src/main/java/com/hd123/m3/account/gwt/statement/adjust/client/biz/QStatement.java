/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BStatement.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-10 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.biz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author zhuhairui
 * 
 */
public class QStatement implements Serializable {
  private static final long serialVersionUID = -1183953293844367099L;

  public static final String FIELD_BILLNUMBER = "billNumber";
  public static final String FIELD_SETTLENO = "settleNo";
  public static final String FIELD_COUNTERPART = "counterpart";
  public static final String FIELD_CONTRACTNUMBER = "contractNumber";

  private BBill statement;
  private String settleNo;
  private BCounterpart counterpart;
  private BUCN contract;
  private BUCN accountUnit;
  private List<BDateRange> accountRanges = new ArrayList<BDateRange>();

  private List<QStatementLine> lines = new ArrayList<QStatementLine>();

  /** 账单标识 */
  public BBill getStatement() {
    return statement;
  }

  public void setStatement(BBill statement) {
    this.statement = statement;
  }

  /** 结转期 */
  public String getSettleNo() {
    return settleNo;
  }

  public void setSettleNo(String settleNo) {
    this.settleNo = settleNo;
  }

  /** 商户 */
  public BCounterpart getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BCounterpart counterpart) {
    this.counterpart = counterpart;
  }

  /** 合同 */
  public BUCN getContract() {
    return contract;
  }

  /** 业务单位 */
  public void setContract(BUCN contract) {
    this.contract = contract;
  }

  /** 项目 */
  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  public List<BDateRange> getAccountRanges() {
    return accountRanges;
  }

  public void setAccountRanges(List<BDateRange> accountRanges) {
    this.accountRanges = accountRanges;
  }

  /** 账单明细 */
  public List<QStatementLine> getLines() {
    return lines;
  }

  public void setLines(List<QStatementLine> lines) {
    this.lines = lines;
  }

  public BContract getBContract() {
    BContract bContract = new BContract();
    if (contract != null) {
      bContract.setUuid(contract.getUuid());
      bContract.setBillNumber(contract.getCode());
      bContract.setTitle(contract.getName());
      // bContract.setBusinessUnit(businessUnit);
      return bContract;
    } else {
      return null;
    }
  }

}
