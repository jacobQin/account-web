/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceExchBalanceLine.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月17日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.biz;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridElement;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;

/**
 * 发票交换单红冲明细|B对象
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class BInvoiceExchBalanceLine implements Serializable, EditGridElement {

  private static final long serialVersionUID = 3556679789073427238L;

  private String oldCode;
  private String oldNumber;
  private String balanceCode;
  private String balanceNumber;
  private BigDecimal amount;
  private String remark;

  public String getOldCode() {
    return oldCode;
  }

  public void setOldCode(String oldCode) {
    this.oldCode = oldCode;
  }

  public String getOldNumber() {
    return oldNumber;
  }

  public void setOldNumber(String oldNumber) {
    this.oldNumber = oldNumber;
  }

  public String getBalanceCode() {
    return balanceCode;
  }

  public void setBalanceCode(String balanceCode) {
    this.balanceCode = balanceCode;
  }

  public String getBalanceNumber() {
    return balanceNumber;
  }

  public void setBalanceNumber(String balanceNumber) {
    this.balanceNumber = balanceNumber;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Override
  public boolean isEmpty() {
    return StringUtil.isNullOrBlank(oldNumber);
  }

  @Override
  public boolean match(EditGridElement other) {
    return false;
  }

}
