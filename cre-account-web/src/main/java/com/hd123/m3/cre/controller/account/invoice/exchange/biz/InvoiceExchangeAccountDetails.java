/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceExchangeAccountDetails.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月28日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.exchange.biz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.service.ivc.exchange.InvoiceExchAccountAdjLine;

/**
 * 发票交换单账款相关明细
 * 
 * @author wangyibo
 *
 */
public class InvoiceExchangeAccountDetails implements Serializable{

  private static final long serialVersionUID = -1037819721841085728L;
  
  private List<BInvoiceExchAccountLine> bexchAccountLines = new ArrayList<BInvoiceExchAccountLine>();
  private List<InvoiceExchAccountLine2> exchAccountLines2 = new ArrayList<InvoiceExchAccountLine2>();
  private List<InvoiceExchAccountAdjLine> accountAdjLines = new ArrayList<InvoiceExchAccountAdjLine>();
  
  private List<Illegality> illegalities = new ArrayList<Illegality>();
  
  /** 账款明细①*/
  public List<BInvoiceExchAccountLine> getBexchAccountLines() {
    return bexchAccountLines;
  }

  public void setExchAccountLines(List<BInvoiceExchAccountLine> bexchAccountLines) {
    this.bexchAccountLines = bexchAccountLines;
  }

  /**账款明细②*/
  public List<InvoiceExchAccountLine2> getExchAccountLines2() {
    return exchAccountLines2;
  }

  public void setExchAccountLines2(List<InvoiceExchAccountLine2> exchAccountLines2) {
    this.exchAccountLines2 = exchAccountLines2;
  }

  /**账款调整*/
  public List<InvoiceExchAccountAdjLine> getAccountAdjLines() {
    return accountAdjLines;
  }

  public void setAccountAdjLines(List<InvoiceExchAccountAdjLine> accountAdjLines) {
    this.accountAdjLines = accountAdjLines;
  }

  /**非法*/
  public List<Illegality> getIllegalities() {
    return illegalities;
  }

  public void setIllegalities(List<Illegality> illegalities) {
    this.illegalities = illegalities;
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
