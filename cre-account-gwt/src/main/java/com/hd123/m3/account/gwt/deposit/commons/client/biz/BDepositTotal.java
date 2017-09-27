/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BDepositTotal.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-12 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.deposit.commons.client.biz;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 预存款金额对象
 * 
 * @author huangjunxian
 * 
 */
public class BDepositTotal implements Serializable {
  private static final long serialVersionUID = 291780922908162252L;

  private BigDecimal advanceTotal;
  private BigDecimal contractTotal;

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
