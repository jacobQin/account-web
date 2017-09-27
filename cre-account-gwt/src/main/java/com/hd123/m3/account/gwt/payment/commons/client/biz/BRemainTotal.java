/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BRemainTotal.java
 * 模块说明：	
 * 修改历史：
 * 2013-12-5 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.biz;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 预存款余额B对象。 KEY = subject + contract
 * 
 * @author subinzhu
 * 
 */
public class BRemainTotal implements Serializable {
  private static final long serialVersionUID = 735028925861850548L;

  private String subject;
  private String contract;
  private BigDecimal remainTotal = BigDecimal.ZERO;

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getContract() {
    return contract;
  }

  public void setContract(String contract) {
    this.contract = contract;
  }

  public BigDecimal getRemainTotal() {
    return remainTotal;
  }

  public void setRemainTotal(BigDecimal remainTotal) {
    this.remainTotal = remainTotal;
  }

}
