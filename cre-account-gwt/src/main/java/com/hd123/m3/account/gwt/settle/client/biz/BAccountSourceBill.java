/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BAccountSourceBill.java
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
import com.hd123.m3.account.gwt.commons.client.biz.BSourceBill;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 账款按来源单据分组。
 * 
 * @author chenwenfeng
 * 
 */
public class BAccountSourceBill implements Serializable {
  private static final long serialVersionUID = -120366455991496447L;

  public static final String ORDER_BY_FIELD_SOURCEBILLTYPE = "sourceBillType";
  public static final String ORDER_BY_FIELD_SOURCEBILLNUMBER = "sourceBillNumber";
  public static final String ORDER_BY_FIELD_ACCOUNTUNIT = "accountUnit";
  public static final String ORDER_BY_FIELD_COUNTERPART = "counterpart";
  public static final String ORDER_BY_FIELD_CONTRACT = "contract";
  public static final String ORDER_BY_FIELD_CONTRACTTITLE = "contractTitle";
  public static final String ORDER_BY_FIELD_TOTAL = "total";

  private BSourceBill sourceBill;
  private BCounterpart counterpart;
  private BTotal total;
  private BUCN contract;

  private List<BAccount> accounts = new ArrayList<BAccount>();

  /** 来源单据 */
  public BSourceBill getSourceBill() {
    return sourceBill;
  }

  public void setSourceBill(BSourceBill sourceBill) {
    this.sourceBill = sourceBill;
  }

  /** 对方单位 */
  public BCounterpart getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BCounterpart counterpart) {
    this.counterpart = counterpart;
  }

  /** 金额 */
  public BTotal getTotal() {
    return total;
  }

  public void setTotal(BTotal total) {
    this.total = total;
  }

  /** 合同 */
  public BUCN getContract() {
    return contract;
  }

  public void setContract(BUCN contract) {
    this.contract = contract;
  }

  /** 账款列表 */
  public List<BAccount> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<BAccount> accounts) {
    this.accounts = accounts;
  }

  /** 账款按来源单据分组之后进行金额合计 */
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
