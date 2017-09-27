/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BAccountInvoice.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-11 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.settle.client.biz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;

/**
 * 账款按发票分组。
 * 
 * @author chenwenfeng
 * 
 */
public class BAccountInvoice implements Serializable {
  private static final long serialVersionUID = -5347755787735970591L;

  public static final String ORDER_BY_FIELD_BILLNUMBER = "billNumber";
  public static final String ORDER_BY_FIELD_ACCOUNTUNIT = "accountUnit";
  public static final String ORDER_BY_FIELD_COUNTERPART = "counterpart";
  public static final String ORDER_BY_FIELD_TOTAL = "total";
  public static final String ORDER_BY_FIELD_INVOICECODE = "invoiceCode";
  public static final String ORDER_BY_FIELD_INVOICENUMBER = "invoiceNumber";
  public static final String ORDER_BY_FIELD_REGDATE = "regDate";

  private String uuid;
  private String billNumber;
  private BCounterpart counterpart;
  private BTotal total;
  private String invoiceCode;
  private String invoiceNumber;
  private Date invoiceRegDate;

  private List<BAccount> accounts = new ArrayList<BAccount>();

  /** 登记单标识 */
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /** 登记单号 */
  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }

  /** 对方结算中心 */
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

  /** 发票代码 */
  public String getInvoiceCode() {
    return invoiceCode;
  }

  public void setInvoiceCode(String invoiceCode) {
    this.invoiceCode = invoiceCode;
  }

  /** 发票号码 */
  public String getInvoiceNumber() {
    return invoiceNumber;
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  /** 登记日期 */
  public Date getInvoiceRegDate() {
    return invoiceRegDate;
  }

  public void setInvoiceRegDate(Date invoiceRegDate) {
    this.invoiceRegDate = invoiceRegDate;
  }

  /** 账款列表 */
  public List<BAccount> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<BAccount> accounts) {
    this.accounts = accounts;
  }

  /** 账款按发票登记单分组之后进行金额合计 */
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
