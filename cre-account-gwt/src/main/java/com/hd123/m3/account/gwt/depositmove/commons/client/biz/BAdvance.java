/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BAdvance.java
 * 模块说明：	
 * 修改历史：
 * 2014-02-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.commons.client.biz;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author zhuhairui
 * 
 */
public class BAdvance implements Serializable {
  private static final long serialVersionUID = -8208247364228615772L;

  private BUCN contract;
  private BUCN subject;
  private BigDecimal total;

  public BUCN getContract() {
    return contract;
  }

  public void setContract(BUCN contract) {
    this.contract = contract;
  }

  public BUCN getSubject() {
    return subject;
  }

  public void setSubject(BUCN subject) {
    this.subject = subject;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

}
