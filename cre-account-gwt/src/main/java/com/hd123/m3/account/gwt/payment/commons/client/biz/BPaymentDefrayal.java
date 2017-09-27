/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BPaymentDefrayal.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.biz;

import java.math.BigDecimal;

import com.hd123.rumba.commons.gwt.entity.client.BEntity;

/**
 * 收付款单按总额收款时的收付明细
 * 
 * @author subinzhu
 * 
 */
public class BPaymentDefrayal extends BEntity {
  private static final long serialVersionUID = 7917895069906474631L;

  private int lineNumber;
  private BPayment receive;
  private BigDecimal total = BigDecimal.ZERO;
  private String remark;

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public BPayment getReceive() {
    return receive;
  }

  public void setReceive(BPayment receive) {
    this.receive = receive;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

}
