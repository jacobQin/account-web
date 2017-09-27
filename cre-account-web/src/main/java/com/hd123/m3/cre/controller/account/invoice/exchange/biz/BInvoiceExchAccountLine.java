/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BInvoiceExchAccountLine.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月29日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.exchange.biz;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.m3.account.service.acc.Acc1;
import com.hd123.m3.account.service.acc.Acc2;
import com.hd123.m3.account.service.ivc.stock.InvoiceStock;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 发票交换账款明细 | 页面对象
 * 
 * @author wangyibo
 *
 */
public class BInvoiceExchAccountLine implements Serializable {

  private static final long serialVersionUID = 7368152504290553987L;

  private Acc1 acc1;
  private Acc2 acc2;

  private String oldCode;
  private String oldNumber;
  private String newCode;
  private String newNumber;
  private String newType;
  private BigDecimal amount = BigDecimal.ZERO;
  private String remark;

  private String sourceBillCaption;
  
  private InvoiceStock newInvocie;

  public String getSourceBillCaption() {
    return sourceBillCaption;
  }

  public void setSourceBillCaption(String sourceBillCaption) {
    this.sourceBillCaption = sourceBillCaption;
  }

  public Acc1 getAcc1() {
    return acc1;
  }

  public void setAcc1(Acc1 acc1) {
    this.acc1 = acc1;
  }

  public Acc2 getAcc2() {
    return acc2;
  }

  public void setAcc2(Acc2 acc2) {
    this.acc2 = acc2;
  }

  public String getNewCode() {
    return newCode;
  }

  public void setNewCode(String newCode) {
    this.newCode = newCode;
  }

  public String getNewNumber() {
    return newNumber;
  }

  public void setNewNumber(String newNumber) {
    this.newNumber = newNumber;
  }

  public String getNewType() {
    return newType;
  }

  public void setNewType(String newType) {
    this.newType = newType;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  /** 取得原发票代码 */
  public String getOldCode() {
    if (oldCode != null) {
      return oldCode;
    }
    if (getAcc2() == null || getAcc2().getInvoice() == null) {
      return null;
    }

    setOldCode(getAcc2().getInvoice().getInvoiceCode());

    return getAcc2().getInvoice().getInvoiceCode();
  }

  public void setOldCode(String oldCode) {
    this.oldCode = oldCode;
  }

  /** 取得原发票号码 */
  public String getOldNumber() {
    if (oldNumber != null) {
      return oldNumber;
    }
    if (getAcc2() == null || getAcc2().getInvoice() == null) {
      return null;
    }

    setOldNumber(getAcc2().getInvoice().getInvoiceNumber());
    return oldNumber;
  }

  public void setOldNumber(String oldNumber) {
    this.oldNumber = oldNumber;
  }

  /** 取得登记金额 */
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  /** 取得账款科目 */
  public UCN getSubject() {
    if (getAcc1() == null || getAcc1().getSubject() == null) {
      return null;
    }
    return getAcc1().getSubject();
  }

  /** 取得来源单号 */
  public String getSourceBill() {
    if (getAcc1() == null || getAcc1().getSourceBill() == null) {
      return null;
    }
    return getAcc1().getSourceBill().getBillNumber();
  }

  /** 取得来源单据类型 */
  public String getSourceBillType() {
    if (getAcc1() == null || getAcc1().getSourceBill() == null) {
      return null;
    }
    return getAcc1().getSourceBill().getBillType();
  }

  public InvoiceStock getNewInvocie() {
    return newInvocie;
  }

  public void setNewInvocie(InvoiceStock newInvocie) {
    this.newInvocie = newInvocie;
  }
  
  

}
