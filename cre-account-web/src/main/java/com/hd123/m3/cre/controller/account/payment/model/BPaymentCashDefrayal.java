package com.hd123.m3.cre.controller.account.payment.model;

import java.math.BigDecimal;

import com.hd123.m3.account.service.bank.Bank;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 收付款单按总额收款时的现金收付明细
 * 
 * @author LiBin
 * 
 */
public class BPaymentCashDefrayal extends BPaymentDefrayal {
  private static final long serialVersionUID = 8360051350213242089L;

  public static final String FN_CASHDEFRAYALTOTAL = "cashDefrayalTotal";
  public static final String FN_BANK = "bank";

  private UCN paymentType;
  private BigDecimal poundage;
  private Bank bank;

  public UCN getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(UCN paymentType) {
    this.paymentType = paymentType;
  }
  
  /** 手续费 */
  public BigDecimal getPoundage() {
    return poundage;
  }

  public void setPoundage(BigDecimal poundage) {
    this.poundage = poundage;
  }


  public Bank getBank() {
    return bank;
  }

  public void setBank(Bank bank) {
    this.bank = bank;
  }

}
