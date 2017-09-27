/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	InvoiceRegServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-30 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegConfig;
import com.hd123.m3.account.gwt.invoice.commons.client.ui.filter.AccountDataFilter;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountNotice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountSourceBill;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleServiceAsync;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * @author chenpeisi
 * 
 */
public interface InvoiceRegServiceAsync extends M3ModuleServiceAsync {

  public void queryAccount(AccountDataFilter filter, AsyncCallback<RPageData<BAccount>> callback);

  public void queryAccountByStatement(AccountDataFilter filter,
      AsyncCallback<RPageData<BAccountStatement>> callback);

  public void queryAccountByBill(AccountDataFilter filter,
      AsyncCallback<RPageData<BAccountSourceBill>> callback);

  public void queryAccountByPaymentNotice(AccountDataFilter filter,
      AsyncCallback<RPageData<BAccountNotice>> callback);

  public void saveConfig(BInvoiceRegConfig config, int direction, AsyncCallback callback);
}
