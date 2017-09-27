/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BAccountStatement.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-11 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.settle.client.biz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 账款按账单分组。
 * 
 * @author chenwenfeng
 * 
 */
public class BAccountPayment implements Serializable {
  private static final long serialVersionUID = 5102691907253880185L;

  public static final String ORDER_BY_FIELD_BILLNUMBER = "billNumber";
  public static final String ORDER_BY_FIELD_CONTRACT = "contract";
  public static final String ORDER_BY_FIELD_CONTRACTTITLE = "contractTitle";
  public static final String ORDER_BY_FIELD_ACCOUNTUNIT = "accountUnit";
  public static final String ORDER_BY_FIELD_COUNTERPART = "counterpart";
  public static final String ORDER_BY_FIELD_TOTAL = "total";

  private String uuid;
  private String billNumber;
  private BUCN accountUnit;
  private BCounterpart counterpart;
  private BUCN contract;

  private BTotal total;
  private List<BAccount> accounts = new ArrayList<BAccount>();

  /** 账单标识 */
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /** 账单号 */
  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }

  /** 项目 */
  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  /** 对方单位 */
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

  public void setContract(BUCN contract) {
    this.contract = contract;
  }

  /** 金额 */
  public BTotal getTotal() {
    return total;
  }

  public void setTotal(BTotal total) {
    this.total = total;
  }

  /** 账款列表 */
  public List<BAccount> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<BAccount> accounts) {
    this.accounts = accounts;
  }

  /** 账款按账单分组之后进行金额合计 */
  public BTotal aggregateTotal() {
    if (accounts == null || accounts.isEmpty())
      return BTotal.zero();

    BTotal t = BTotal.zero();
    for (BAccount account : accounts) {
      t = t.add(account.getTotal());
    }
    return t;
  }
}
