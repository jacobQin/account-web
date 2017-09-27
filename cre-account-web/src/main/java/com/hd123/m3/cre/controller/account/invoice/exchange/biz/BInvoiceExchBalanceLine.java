/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BInvoiceExchBalanceLine.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月6日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.exchange.biz;

import com.hd123.m3.account.service.ivc.exchange.InvoiceExchBalanceLine;
import com.hd123.m3.account.service.ivc.stock.InvoiceStock;

/**
 * @author wangyibo
 *
 */
public class BInvoiceExchBalanceLine extends InvoiceExchBalanceLine{

  private static final long serialVersionUID = -2998972731069316600L;
  
  private InvoiceStock oldInvoice;
  private InvoiceStock balanceInvocie;

  public InvoiceStock getOldInvoice() {
    return oldInvoice;
  }

  public void setOldInvoice(InvoiceStock oldInvoice) {
    this.oldInvoice = oldInvoice;
  }

  public InvoiceStock getBalanceInvocie() {
    return balanceInvocie;
  }

  public void setBalanceInvocie(InvoiceStock balanceInvocie) {
    this.balanceInvocie = balanceInvocie;
  }
  
  

}
