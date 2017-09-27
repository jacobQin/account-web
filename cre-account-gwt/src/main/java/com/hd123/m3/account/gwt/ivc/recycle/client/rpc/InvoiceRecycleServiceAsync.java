/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRecycleServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.ivc.recycle.client.biz.BInvoiceRecycle;
import com.hd123.m3.commons.gwt.bpm.client.rpc2.M3BpmModuleService2Async;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 发票回收单|异步接口
 * 
 * @author lixiaohong
 * @since 1.9
 *
 */
public interface InvoiceRecycleServiceAsync extends M3BpmModuleService2Async<BInvoiceRecycle>{
  public void getCurrentEmployee(String id, AsyncCallback<BUCN> callback);
}
