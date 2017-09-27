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

import com.hd123.m3.account.gwt.acc.client.BAcc1;
import com.hd123.m3.account.gwt.acc.client.BAcc2;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;

/**
 * 发票交换账款明细|B对象.改对象用于与接口数据对象进行数据转换.
 * <p>
 * 设计该对象的缘由:对于账款明细界面显示时需要进行合并（合并规则：acc1.id与acc2.invoice.invoiceNumber相同），
 * 而服务端仍然需要分开显示。换句话说，该对象用于保持服务端数据的状态，在界面交互的过程中不需要修改。
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class BInvoiceExchAccountLine2 implements Serializable {

  private static final long serialVersionUID = 7857103657990005532L;

  private BAcc1 acc1;
  private BAcc2 acc2;

  private BTotal originTotal;
  private BTotal total;

  private String oldCode;
  private String oldNumber;
  private String newCode;
  private String newNumber;
  private String newType;
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
  
  public BTotal getOriginTotal() {
    return originTotal;
  }

  public void setOriginTotal(BTotal originTotal) {
    this.originTotal = originTotal;
  }

  public BTotal getTotal() {
    return total;
  }

  public void setTotal(BTotal total) {
    this.total = total;
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

}
