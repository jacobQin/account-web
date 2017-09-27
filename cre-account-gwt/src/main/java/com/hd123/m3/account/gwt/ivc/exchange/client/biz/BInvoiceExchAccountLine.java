/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceExchAccountLine.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月17日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.biz;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.m3.account.gwt.acc.client.BAcc1;
import com.hd123.m3.account.gwt.acc.client.BAcc2;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridElement;

/**
 * 发票交换账款明细|B对象
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class BInvoiceExchAccountLine implements Serializable, EditGridElement {

  private static final long serialVersionUID = 7857103657990005532L;

  private BAcc1 acc1;
  private BAcc2 acc2;

  private String oldCode;
  private String oldNumber;
  private String newCode;
  private String newNumber;
  private String newInvoiceType;
  private BigDecimal amount = BigDecimal.ZERO;
  private String remark;

  public BAcc1 getAcc1() {
    return acc1;
  }

  public void setAcc1(BAcc1 acc1) {
    this.acc1 = acc1;
  }

  public BAcc2 getAcc2() {
    return acc2;
  }

  public void setAcc2(BAcc2 acc2) {
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

  public String getNewInvoiceType() {
    return newInvoiceType;
  }

  public void setNewInvoiceType(String newInvoiceType) {
    this.newInvoiceType = newInvoiceType;
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
  public String getSubject() {
    if (getAcc1() == null || getAcc1().getSubject() == null) {
      return null;
    }
    return getAcc1().getSubject().toFriendlyStr();
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

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean match(EditGridElement other) {
    return false;
  }

}
