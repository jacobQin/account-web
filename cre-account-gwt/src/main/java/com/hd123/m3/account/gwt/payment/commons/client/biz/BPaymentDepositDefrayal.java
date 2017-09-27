/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BPaymentDepositDefrayal.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-29 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.biz;

import java.math.BigDecimal;

import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 收付款单按总额收款时的预存款收付明细
 * 
 * @author subinzhu
 * 
 */
public class BPaymentDepositDefrayal extends BPaymentDefrayal {
  private static final long serialVersionUID = -877216038868057337L;

  public static final String FN_DEPOSITDEFRAYALTOTAL = "depositDefrayalTotal";

  private BUCN subject;
  private BUCN contract;
  private BigDecimal remainTotal = BigDecimal.ZERO;

  public BUCN getSubject() {
    return subject;
  }

  public void setSubject(BUCN subject) {
    this.subject = subject;
  }

  public BUCN getContract() {
    return contract;
  }

  public void setContract(BUCN contract) {
    this.contract = contract;
  }

  public BigDecimal getRemainTotal() {
    return remainTotal;
  }

  public void setRemainTotal(BigDecimal remainTotal) {
    this.remainTotal = remainTotal;
  }

}
