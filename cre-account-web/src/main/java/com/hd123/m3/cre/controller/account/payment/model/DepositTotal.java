/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	DepositTotal.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月23日 - libin - 创建。
 */
package com.hd123.m3.cre.controller.account.payment.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 预存款余额
 * 
 * @author libin
 *
 */
/**
 * @author libin
 *
 */
public class DepositTotal implements Serializable {

  private static final long serialVersionUID = -5140201413370292783L;

  private BigDecimal total = BigDecimal.ZERO;

  private List<ContractAdvance> advances = new ArrayList<ContractAdvance>();

  /** 余额合计 */
  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  /** 合同余额 */
  public List<ContractAdvance> getAdvances() {
    return advances;
  }

  public void setAdvances(List<ContractAdvance> advances) {
    this.advances = advances;
  }

}
