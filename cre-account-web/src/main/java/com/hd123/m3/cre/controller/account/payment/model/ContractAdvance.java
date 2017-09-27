/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	ContractAdvance.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月23日 - libin - 创建。
 */
package com.hd123.m3.cre.controller.account.payment.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * @author libin
 *
 */
public class ContractAdvance implements Serializable {

  private static final long serialVersionUID = 1268616717640448158L;

  private UCN contract;
  private BigDecimal total;

  /** 合同 */
  public UCN getContract() {
    return contract;
  }

  public void setContract(UCN contract) {
    this.contract = contract;
  }

  /** 余额 */
  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

}
