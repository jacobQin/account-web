/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BPaymentCashDefrayal.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-29 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.biz;

import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 收付款单按总额收款时的现金收付明细
 * 
 * @author subinzhu
 * 
 */
public class BPaymentCashDefrayal extends BPaymentDefrayal {
  private static final long serialVersionUID = 8360051350213242089L;

  public static final String FN_CASHDEFRAYALTOTAL = "cashDefrayalTotal";
  public static final String FN_BANK = "bank";

  private BUCN paymentType;
  private BBank bank;

  public BUCN getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(BUCN paymentType) {
    this.paymentType = paymentType;
  }

  public BBank getBank() {
    return bank;
  }

  public void setBank(BBank bank) {
    this.bank = bank;
  }

}
