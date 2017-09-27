/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	DepositTotal.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月16日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.deposit.receipt;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 预存款金额
 * 
 * @author LiBin
 *
 */
public class DepositTotal implements Serializable {

  private static final long serialVersionUID = 8095370259005757082L;

  private BigDecimal advanceTotal = BigDecimal.ZERO;
  private BigDecimal contractTotal = BigDecimal.ZERO;

  /** 预存款余额 */
  public BigDecimal getAdvanceTotal() {
    return advanceTotal;
  }

  public void setAdvanceTotal(BigDecimal advanceTotal) {
    this.advanceTotal = advanceTotal;
  }

  /** 合同金额 */
  public BigDecimal getContractTotal() {
    return contractTotal;
  }

  public void setContractTotal(BigDecimal contractTotal) {
    this.contractTotal = contractTotal;
  }

}
