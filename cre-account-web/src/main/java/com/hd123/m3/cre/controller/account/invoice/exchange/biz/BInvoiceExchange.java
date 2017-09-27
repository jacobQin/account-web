/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceExchange.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月28日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.exchange.biz;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.service.ivc.exchange.InvoiceExchange;

/**
 * 发票交换单 | 页面对象
 * 
 * @author wangyibo
 */
public class BInvoiceExchange extends InvoiceExchange {

  private static final long serialVersionUID = 8840823593389521004L;
  
//  private ExchangeType type = ExchangeType.eviToEvi;
//  private InvToInvType invToInvType = InvToInvType.balance;;
  
  private List<BInvoiceExchBalanceLine> bexchBalanceLines = new ArrayList<BInvoiceExchBalanceLine>();
  private List<BInvoiceExchAccountLine> bexchAccountLines = new ArrayList<BInvoiceExchAccountLine>();
  private List<InvoiceExchAccountLine2> exchAccountLines2 = new ArrayList<InvoiceExchAccountLine2>();
  
  public List<BInvoiceExchBalanceLine> getBexchBalanceLines() {
    return bexchBalanceLines;
  }

  public void setBexchBalanceLines(List<BInvoiceExchBalanceLine> bexchBalanceLines) {
    this.bexchBalanceLines = bexchBalanceLines;
  }

  public List<BInvoiceExchAccountLine> getBexchAccountLines() {
    return bexchAccountLines;
  }

  public void setBexchAccountLines(List<BInvoiceExchAccountLine> bexchAccountLines) {
    this.bexchAccountLines = bexchAccountLines;
  }

  public List<InvoiceExchAccountLine2> getExchAccountLines2() {
    return exchAccountLines2;
  }

  public void setExchAccountLines2(List<InvoiceExchAccountLine2> exchAccountLines2) {
    this.exchAccountLines2 = exchAccountLines2;
  }

}
