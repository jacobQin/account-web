/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	BSalePaymentTypes.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月16日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.biz;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 付款方式，用于保存。
 * 
 * @author chenganbang
 */
public class BSalesPayment implements Serializable {
  private static final long serialVersionUID = 1791948255883465549L;

  private BUCN payment;
  private BUCN bank;
  private BigDecimal total;

  private BSalesReceiver receiver;

  /**
   * 付款方式
   * @return
   */
  public BUCN getPayment() {
    return payment;
  }

  public void setPayment(BUCN payment) {
    this.payment = payment;
  }

  /**
   * 银行
   * @return
   */
  public BUCN getBank() {
    return bank;
  }

  public void setBank(BUCN bank) {
    this.bank = bank;
  }

  /**
   * 总额
   * @return
   */
  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  /**
   * 收款方
   * @return
   */
  public BSalesReceiver getReceiver() {
    return receiver;
  }

  public void setReceiver(BSalesReceiver receiver) {
    this.receiver = receiver;
  }

}
