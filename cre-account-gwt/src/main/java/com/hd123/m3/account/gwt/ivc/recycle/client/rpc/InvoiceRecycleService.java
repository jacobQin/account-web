/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRecycleService.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.ivc.recycle.client.biz.BInvoiceRecycle;
import com.hd123.m3.commons.gwt.bpm.client.rpc2.M3BpmModuleService2;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;

/**
 * 发票回收单|同步接口。
 * 
 * @author lixiaohong
 * @since 1.9
 *
 */
@RemoteServiceRelativePath("invoice/recycle")
public interface InvoiceRecycleService extends M3BpmModuleService2<BInvoiceRecycle>{
public static final String INVOICE_TYPE="invoiceType";
  
  public static class Locator {
    private static InvoiceRecycleServiceAsync service = null;

    public static InvoiceRecycleServiceAsync getService() {
      if (service == null)
        service = GWT.create(InvoiceRecycleService.class);
      return service;
    }
  }
  
  /**
   * 取得当前登录员工。
   * 
   * @param id
   *          用户id not null。
   * @return 登录员工
   * @throws ClientBizException
   */
  public BUCN getCurrentEmployee(String id) throws ClientBizException;
}
