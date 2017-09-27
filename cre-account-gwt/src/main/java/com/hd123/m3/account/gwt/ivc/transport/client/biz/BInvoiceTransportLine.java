/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceTransportLine.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月14日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.transport.client.biz;

import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceLine;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 发票调拨明细
 * 
 * @author lixiaohong
 * @since 1.9
 * 
 */
public class BInvoiceTransportLine extends BInvoiceLine {

  private static final long serialVersionUID = 7765122438212418452L;
  private BUCN inAccountUnit;
  private BUCN receiver;

  /** 调入项目 */
  public BUCN getInAccountUnit() {
    return inAccountUnit;
  }

  public void setInAccountUnit(BUCN inAccountUnit) {
    this.inAccountUnit = inAccountUnit;
  }

  /** 领用人 */
  public BUCN getReceiver() {
    return receiver;
  }

  public void setReceiver(BUCN receiver) {
    this.receiver = receiver;
  }

  @Override
  public boolean isEmpty() {
    if (this.inAccountUnit != null || this.receiver != null || getInvoiceCode() != null
        || getStartNumber() != null || getQuantity() != 0 || getEndNumber() != null
        || getRemark() != null) {
      return false;
    } else {
      return true;
    }
  }
}
