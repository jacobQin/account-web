package com.hd123.m3.cre.controller.account.payment.model;

import java.math.BigDecimal;

import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 收付款单按总额收款时的预存款收付明细
 * 
 * @author LiBin
 * 
 */
public class BPaymentDepositDefrayal extends BPaymentDefrayal {
  private static final long serialVersionUID = -877216038868057337L;

  public static final String FN_DEPOSITDEFRAYALTOTAL = "depositDefrayalTotal";

  private UCN subject;
  private UCN contract;
  private BigDecimal remainTotal = BigDecimal.ZERO;

  public UCN getSubject() {
    return subject;
  }

  public void setSubject(UCN subject) {
    this.subject = subject;
  }

  public UCN getContract() {
    return contract;
  }

  public void setContract(UCN contract) {
    this.contract = contract;
  }

  public BigDecimal getRemainTotal() {
    return remainTotal;
  }

  public void setRemainTotal(BigDecimal remainTotal) {
    this.remainTotal = remainTotal;
  }

}
