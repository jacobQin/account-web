/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceRecycleLine.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.client.biz;

import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceLine;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridElement;

/**
 * B对象|发票回收明细
 * 
 * @author lixiaohong
 * @since 1.9
 * 
 */
public class BInvoiceRecycleLine extends BInvoiceLine {
  private static final long serialVersionUID = 2716941242188701537L;

  private int realQty;
  private int balanceQty;

  /** 实际回收张数 */
  public int getRealQty() {
    return realQty;
  }

  public void setRealQty(int realQty) {
    this.realQty = realQty;
  }

  /** 差额 */
  public int getBalanceQty() {
    return balanceQty;
  }

  public void setBalanceQty(int balanceQty) {
    this.balanceQty = balanceQty;
  }

  @Override
  public boolean isEmpty() {
    if (getInvoiceCode() != null || getStartNumber() != null || getQuantity() != 0
        || getEndNumber() != null || getRemark() != null || realQty != 0) {
      return false;
    } else {
      return true;
    }
  }

  @Override
  public boolean match(EditGridElement other) {
    if (other instanceof BInvoiceRecycleLine == false)
      return false;
    BInvoiceRecycleLine detail = (BInvoiceRecycleLine) other;
    if (getInvoiceCode() != null && getStartNumber() != null && realQty != 0
        && detail.getInvoiceCode() != null && detail.getStartNumber() != null
        && detail.getRealQty() != 0 && getInvoiceCode().equals(detail.getInvoiceCode())
        && getStartNumber().equals(detail.getStartNumber())
        && realQty!=detail.getRealQty()) {
      return true;
    }
    return false;
  }
}
