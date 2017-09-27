/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceExchAccountLine2.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月28日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.exchange.biz;

import java.io.Serializable;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.service.acc.Acc1;
import com.hd123.m3.account.service.acc.Acc2;

/**
 * 发票交换账款明细|B对象.改对象用于与接口数据对象进行数据转换.
 * <p>
 * 设计该对象的缘由:对于账款明细界面显示时需要进行合并（合并规则：acc1.id与acc2.invoice.invoiceNumber相同），
 * 而服务端仍然需要分开显示。换句话说，该对象用于保持服务端数据的状态，在界面交互的过程中不需要修改。
 * 
 * @author wangyibo
 *
 */
public class InvoiceExchAccountLine2 implements Serializable {
  
  private static final long serialVersionUID = -4359221911402215712L;
  private Acc1 acc1;
  private Acc2 acc2;

  private Total originTotal;
  private Total total;

  private String oldCode;
  private String oldNumber;
  private String newCode;
  private String newNumber;
  private String newType;
  private String remark;

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

  public Total getOriginTotal() {
    return originTotal;
  }

  public void setOriginTotal(Total originTotal) {
    this.originTotal = originTotal;
  }

  public Total getTotal() {
    return total;
  }

  public void setTotal(Total total) {
    this.total = total;
  }

  public String getOldCode() {
    return oldCode;
  }

  public void setOldCode(String oldCode) {
    this.oldCode = oldCode;
  }

  public String getOldNumber() {
    return oldNumber;
  }

  public void setOldNumber(String oldNumber) {
    this.oldNumber = oldNumber;
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
  
  

}
