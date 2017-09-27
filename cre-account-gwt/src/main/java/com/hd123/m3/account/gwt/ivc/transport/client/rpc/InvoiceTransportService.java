/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceTransportService.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月14日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.transport.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.ivc.transport.client.biz.BInvoiceTransport;
import com.hd123.m3.commons.gwt.bpm.client.rpc2.M3BpmModuleService2;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;

/**
 * 发票调拨单|同步接口。
 * 
 * @author lixiaohong
 * @since 1.9
 *
 */
@RemoteServiceRelativePath("invoice/transport")
public interface InvoiceTransportService extends M3BpmModuleService2<BInvoiceTransport>{
  public static final String INVOICE_TYPE="invoiceType";
  
  public static class Locator {
    private static InvoiceTransportServiceAsync service = null;

    public static InvoiceTransportServiceAsync getService() {
      if (service == null)
        service = GWT.create(InvoiceTransportService.class);
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
