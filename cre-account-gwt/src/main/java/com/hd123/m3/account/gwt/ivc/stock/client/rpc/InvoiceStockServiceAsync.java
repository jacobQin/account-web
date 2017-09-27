/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceStockServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月10日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.stock.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.ivc.stock.client.biz.BInvoiceStock;
import com.hd123.m3.account.gwt.ivc.stock.client.biz.BInvoiceStockRegLine;
import com.hd123.m3.commons.gwt.bpm.client.rpc2.M3BpmModuleService2Async;

/**
 * 发票库存|异步接口
 * 
 * @author lixiaohong
 * @since 1.7
 * 
 */
public interface InvoiceStockServiceAsync extends M3BpmModuleService2Async<BInvoiceStock> {

  void getRegLinesByNumber(String invoiceNumber, AsyncCallback<List<BInvoiceStockRegLine>> callback);

}
