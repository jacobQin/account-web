package com.hd123.m3.cre.controller.account.payment.model;

import java.math.BigDecimal;

import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 收付款单按科目收款时的预存款收付明细
 * 
 * @author LiBin
 * 
 */
public class BPaymentLineDeposit extends BPaymentLineDefrayal {
  private static final long serialVersionUID = -1354199597949686815L;

  public static final String FN_LINEDEPOSITTOTAL = "lineDepositTotal";
  public static final String FN_LINEDEPOSITSUBJECT = "lineDepositSubject";

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
