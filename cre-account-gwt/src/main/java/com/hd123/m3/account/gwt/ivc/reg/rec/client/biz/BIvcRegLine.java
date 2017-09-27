/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-api
 * 文件名：	IvcRegLine.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月2日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.biz;

import java.math.BigDecimal;

import com.hd123.m3.account.gwt.acc.client.BAcc1;
import com.hd123.m3.account.gwt.acc.client.BAcc2;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridElement;
import com.hd123.rumba.commons.gwt.entity.client.BEntity;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;

/**
 * 收款发票登记单明细
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class BIvcRegLine extends BEntity implements EditGridElement {
  private static final long serialVersionUID = -5496520623585580281L;

  private BAcc1 acc1;
  private BAcc2 acc2;
  private BTotal originTotal = new BTotal(BigDecimal.ZERO, BigDecimal.ZERO);
  private BTotal unregTotal = new BTotal(BigDecimal.ZERO, BigDecimal.ZERO);
  private BTotal total = new BTotal(BigDecimal.ZERO, BigDecimal.ZERO);
  private String invoiceCode;
  private String invoiceNumber;
  private String invoiceType;
  private String remark;
  private BigDecimal totalDiff;
  private BigDecimal taxDiff;

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean match(EditGridElement other) {
    if (other instanceof BIvcRegLine == false)
      return false;
    if (acc1 == null || acc1.getId() == null)
      return false;
    if (((BIvcRegLine) other).getAcc1() == null || ((BIvcRegLine) other).getAcc1().getId() == null)
      return false;
    if (acc1.getId().equals(((BIvcRegLine) other).getAcc1().getId()) == false)
      return false;
    return ObjectUtil.equals(acc2.getPayment(), ((BIvcRegLine) other).getAcc2().getPayment());
  }

  /** 一级账款 */
  public BAcc1 getAcc1() {
    return acc1;
  }

  public void setAcc1(BAcc1 acc1) {
    this.acc1 = acc1;
  }

  /** 二级账款 */
  public BAcc2 getAcc2() {
    return acc2;
  }

  public void setAcc2(BAcc2 acc2) {
    this.acc2 = acc2;
  }

  /** 原始金额 */
  public BTotal getOriginTotal() {
    return originTotal;
  }

  public void setOriginTotal(BTotal originTotal) {
    this.originTotal = originTotal;
  }

  /** 本次应开票金额 */
  public BTotal getUnregTotal() {
    return unregTotal;
  }

  public void setUnregTotal(BTotal unregTotal) {
    this.unregTotal = unregTotal;
  }

  /** 开票金额 */
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

  /** 发票类型 */
  public String getInvoiceType() {
    return invoiceType;
  }

  public void setInvoiceType(String invoiceType) {
    this.invoiceType = invoiceType;
  }

  /** 说明 */
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  /** 金额差异 */
  public BigDecimal getTotalDiff() {
    return totalDiff;
  }

  public void setTotalDiff(BigDecimal totalDiff) {
    this.totalDiff = totalDiff;
  }

  /** 税额差异 */
  public BigDecimal getTaxDiff() {
    return taxDiff;
  }

  public void setTaxDiff(BigDecimal taxDiff) {
    this.taxDiff = taxDiff;
  }

}
