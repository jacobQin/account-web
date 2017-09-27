/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceAbort.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月15日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.transport.client.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceStandardBill;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * B对象|发票作废单
 * 
 * @author lixiaohong
 * @since 1.9
 * 
 */
public class BInvoiceTransport extends BInvoiceStandardBill {

  private static final long serialVersionUID = -716242286649558047L;
  private BUCN transportor;
  private Date transportDate;

  private List<BInvoiceTransportLine> transportLines = new ArrayList<BInvoiceTransportLine>();

  /** 调拨人 */
  public BUCN getTransportor() {
    return transportor;
  }

  public void setTransportor(BUCN transportor) {
    this.transportor = transportor;
  }

  /** 调拨日期 */
  public Date getTransportDate() {
    return transportDate;
  }

  public void setTransportDate(Date transportDate) {
    this.transportDate = transportDate;
  }

  /** 调拨明细 */
  public List<BInvoiceTransportLine> getTransportLines() {
    return transportLines;
  }

  public void setTransportLines(List<BInvoiceTransportLine> transportLines) {
    this.transportLines = transportLines;
  }

}
