package com.hd123.m3.account.gwt.ivc.instock.client.biz;

import java.util.Date;
import java.util.List;

import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceLine;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceStandardBill;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * B对象|发票入库单
 * 
 * @author lixiaohong
 * @since 1.7
 *
 */
public class BInvoiceInstock extends BInvoiceStandardBill{
  private static final long serialVersionUID = 8403133007090097175L;
  private BUCN instockor;
  private Date instockDate;
  private boolean receive;
  private List<BInvoiceLine> instockLines;

  /**入库人*/
  public BUCN getInstockor() {
    return instockor;
  }

  public void setInstockor(BUCN instockor) {
    this.instockor = instockor;
  }

  /**入库日期*/
  public Date getInstockDate() {
    return instockDate;
  }

  public void setInstockDate(Date instockDate) {
    this.instockDate = instockDate;
  }
  
  /** 入库并领用 */
  public boolean isReceive() {
    return receive;
  }

  public void setReceive(boolean receive) {
    this.receive = receive;
  }

  /**发票明细*/
  public List<BInvoiceLine> getInstockLines() {
    return instockLines;
  }

  public void setInstockLines(List<BInvoiceLine> instockLines) {
    this.instockLines = instockLines;
  }

}
