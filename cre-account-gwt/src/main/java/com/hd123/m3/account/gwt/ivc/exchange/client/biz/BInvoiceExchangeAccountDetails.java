/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceExchangeAccountDetails.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月24日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.biz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 发票交换单账款相关明细
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class BInvoiceExchangeAccountDetails implements Serializable {

  private static final long serialVersionUID = 5896028530832433761L;

  private List<BInvoiceExchAccountLine> exchAccountLines = new ArrayList<BInvoiceExchAccountLine>();
  private List<BInvoiceExchAccountLine2> exchAccountLines2 = new ArrayList<BInvoiceExchAccountLine2>();
  private List<BInvoiceExchangeAccountAdjLine> accountAdjLines = new ArrayList<BInvoiceExchangeAccountAdjLine>();

  private List<Illegality> Illegalities = new ArrayList<Illegality>();

  /** 账款明细 */
  public List<BInvoiceExchAccountLine> getExchAccountLines() {
    return exchAccountLines;
  }

  public void setExchAccountLines(List<BInvoiceExchAccountLine> exchAccountLines) {
    this.exchAccountLines = exchAccountLines;
  }

  /** 账款明细2 */
  public List<BInvoiceExchAccountLine2> getExchAccountLines2() {
    return exchAccountLines2;
  }

  public void setExchAccountLines2(List<BInvoiceExchAccountLine2> exchAccountLines2) {
    this.exchAccountLines2 = exchAccountLines2;
  }

  /** 账款调整 */
  public List<BInvoiceExchangeAccountAdjLine> getAccountAdjLines() {
    return accountAdjLines;
  }

  public void setAccountAdjLines(List<BInvoiceExchangeAccountAdjLine> accountAdjLines) {
    this.accountAdjLines = accountAdjLines;
  }

  /**  */
  public List<Illegality> getIllegalities() {
    return Illegalities;
  }

  public void setIllegalities(List<Illegality> illegalities) {
    Illegalities = illegalities;
  }

  /** 不非法类型枚举 */
  public enum IllegalityType {
    /** 被单据锁定 */
    locked,
    /** 不存在账款 */
    noAccount
  }

  public class Illegality {

    private String invoiceNumber;
    private String lockedBill;
    private IllegalityType type;

    /** 发票号码 */
    public String getInvoiceNumber() {
      return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
      this.invoiceNumber = invoiceNumber;
    }

    /** 锁定单号 */
    public String getLockedBill() {
      return lockedBill;
    }

    public void setLockedBill(String lockedBill) {
      this.lockedBill = lockedBill;
    }

    /** 不合法类型，默认为发票被锁定 */
    public IllegalityType getType() {
      return type;
    }

    public void setType(IllegalityType type) {
      this.type = type;
    }

  }

}
