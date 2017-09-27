/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	PaymentOption.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月8日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.payment.pay;

import java.io.Serializable;

import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 付款单配置项
 * 
 * @author LiBin
 *
 */
public class PaymentOption implements Serializable {

  private static final long serialVersionUID = 194520000426200937L;

  private UCN depositSubject;
  private UCN paymentType;

  /**默认预存款科目*/
  public UCN getDepositSubject() {
    return depositSubject;
  }

  public void setDepositSubject(UCN depositSubject) {
    this.depositSubject = depositSubject;
  }

  /**默认财务付款方式*/
  public UCN getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(UCN paymentType) {
    this.paymentType = paymentType;
  }

}
