/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月17日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchangeAccountDetails;
import com.hd123.m3.commons.gwt.bpm.client.rpc2.M3BpmModuleService2Async;

/**
 * 发票交换单GWT服务|异步接口
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public interface InvoiceExchangeServiceAsync extends M3BpmModuleService2Async<BInvoiceExchange> {

  /** @see #getAccountLinesByInvoceNumbers(List,String, AsyncCallback) */
  void getAccountLinesByInvoceNumbers(List<String> invoiceNumbers, String billUuid,
      AsyncCallback<BInvoiceExchangeAccountDetails> callback);

  /** @see #getAllTypes(AsyncCallback) */
  void getAllTypes(AsyncCallback<List<BBillType>> callback);
}
