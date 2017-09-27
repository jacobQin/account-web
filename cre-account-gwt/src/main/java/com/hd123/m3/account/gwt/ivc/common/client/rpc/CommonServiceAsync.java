/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	CommonServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月14日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.common.client.rpc;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceStock;
import com.hd123.rumba.gwt.base.client.QueryFilter;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.ModuleServiceAsync;

/**
 * Common模块服务｜异步接口
 * 
 * @author lixiaohong
 * @since 1.7
 * 
 */
public interface CommonServiceAsync extends ModuleServiceAsync {
  void queryStocks(QueryFilter filter, AsyncCallback<RPageData<BInvoiceStock>> callback);

  void getStockByNumber(String invoiceNumber, Map<String, Object> filter,
      AsyncCallback<BInvoiceStock> callback);

  void getInvoiceTypes(AsyncCallback<Map<String, String>> callback);
}
