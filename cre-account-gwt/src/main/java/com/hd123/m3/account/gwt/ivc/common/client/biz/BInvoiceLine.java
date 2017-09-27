/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceInstockLine.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月1日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.common.client.biz;

import java.io.Serializable;

import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridElement;

/**
 * B对象|发票入库明细
 * 
 * @author lixiaohong
 * @since 1.0
 * 
 */
public class BInvoiceLine implements Serializable, EditGridElement {
  private static final long serialVersionUID = -6925878353220212426L;

  private String invoiceCode;
  private String startNumber;
  private int quantity;
  private String endNumber;
  private String remark;

  /** 发票代码 */
  public String getInvoiceCode() {
    return invoiceCode;
  }

  public void setInvoiceCode(String invoiceCode) {
    this.invoiceCode = invoiceCode;
  }

  /** 起始发票号码 */
  public String getStartNumber() {
    return startNumber;
  }

  public void setStartNumber(String startNumber) {
    this.startNumber = startNumber;
  }

  /** 入库张数 */
  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /** 结束发票号码 */
  public String getEndNumber() {
    return endNumber;
  }

  public void setEndNumber(String endNumber) {
    this.endNumber = endNumber;
  }

  /** 说明 */
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
  
  @Override
  public boolean isEmpty() {
    if (invoiceCode != null || startNumber != null || quantity != 0 || endNumber != null
        || remark != null) {
      return false;
    } else {
      return true;
    }
  }

  @Override
  public boolean match(EditGridElement other) {
    if (other instanceof BInvoiceLine == false)
      return false;
    BInvoiceLine detail = (BInvoiceLine) other;
    if (invoiceCode != null && startNumber != null && detail.getInvoiceCode() != null
        && detail.getStartNumber() != null && invoiceCode.equals(detail.getInvoiceCode())
        && startNumber.equals(detail.getStartNumber())) {
      return true;
    }
    return false;
  }
}
