/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BInvoiceRegConfig.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-6 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.client.biz;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发票登记单选项配置
 * 
 * @author chenpeisi
 * 
 */
public class BInvoiceRegConfig implements Serializable {
  private static final long serialVersionUID = 5787050293433139699L;

  private BigDecimal totalDiffHi;
  private BigDecimal taxDiffHi;
  private BigDecimal totalDiffLo;
  private BigDecimal taxDiffLo;
  private Boolean regTotalWritable = Boolean.FALSE;

  public BigDecimal getTotalDiffHi() {
    return totalDiffHi;
  }

  public void setTotalDiffHi(BigDecimal totalDiffHi) {
    this.totalDiffHi = totalDiffHi;
  }

  public BigDecimal getTaxDiffHi() {
    return taxDiffHi;
  }

  public void setTaxDiffHi(BigDecimal taxDiffHi) {
    this.taxDiffHi = taxDiffHi;
  }

  public BigDecimal getTotalDiffLo() {
    return totalDiffLo;
  }

  public void setTotalDiffLo(BigDecimal totalDiffLo) {
    this.totalDiffLo = totalDiffLo;
  }

  public BigDecimal getTaxDiffLo() {
    return taxDiffLo;
  }

  public void setTaxDiffLo(BigDecimal taxDiffLo) {
    this.taxDiffLo = taxDiffLo;
  }

  public Boolean getRegTotalWritable() {
    return regTotalWritable;
  }

  public void setRegTotalWritable(Boolean regTotalWritable) {
    this.regTotalWritable = regTotalWritable;
  }

}
