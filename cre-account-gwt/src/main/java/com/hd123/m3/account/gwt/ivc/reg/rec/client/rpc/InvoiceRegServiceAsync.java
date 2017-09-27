/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRegServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceRegOptions;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountNotice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountPayment;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.commons.gwt.bpm.client.rpc2.M3BpmModuleService2Async;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * 发票登记单|异步接口
 * 
 * @author chenrizhang
 * @since 1.7
 * 
 */
public interface InvoiceRegServiceAsync extends M3BpmModuleService2Async<BInvoiceReg> {

  void createByPayment(String paymentUuid, AsyncCallback<BInvoiceReg> callback);

  void createByStatement(String statementUuid, AsyncCallback<BInvoiceReg> callback);

  void createByNotice(String noticeUuid, AsyncCallback<BInvoiceReg> callback);

  void queryAccounts(IvcRegAccountFilter filter, AsyncCallback<RPageData<BAccount>> callback);

  void queryAccStatements(IvcRegAccountFilter filter,
      AsyncCallback<RPageData<BAccountStatement>> callback);

  void queryAccPayments(IvcRegAccountFilter filter,
      AsyncCallback<RPageData<BAccountPayment>> callback);

  void queryAccNotices(IvcRegAccountFilter filter, AsyncCallback<RPageData<BAccountNotice>> callback);

  void saveOptions(BInvoiceRegOptions config, AsyncCallback<Void> callback);

  void print(String uuid, long version, AsyncCallback<Void> callback);
}
