/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	BDefaultOption.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.commons.client.biz;

import java.io.Serializable;

import com.hd123.m3.commons.gwt.base.client.tax.BTaxRate;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author zhuhairui
 * 
 */
public class BDefaultOption implements Serializable {

  private static final long serialVersionUID = 4909997099994166768L;
  /** 财务付款方式 */
  private BUCN paymentType;
  /** 税率 */
  private BTaxRate taxRate;
  /** 默认预存款科目 */
  private BUCN preReceiveSubject;
  /** 默认预付款科目 */
  private BUCN prePaySubject;

  public BUCN getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(BUCN paymentType) {
    this.paymentType = paymentType;
  }

  public BTaxRate getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(BTaxRate taxRate) {
    this.taxRate = taxRate;
  }

  public BUCN getPreReceiveSubject() {
    return preReceiveSubject;
  }

  public void setPreReceiveSubject(BUCN preReceiveSubject) {
    this.preReceiveSubject = preReceiveSubject;
  }

  public BUCN getPrePaySubject() {
    return prePaySubject;
  }

  public void setPrePaySubject(BUCN prePaySubject) {
    this.prePaySubject = prePaySubject;
  }

}
