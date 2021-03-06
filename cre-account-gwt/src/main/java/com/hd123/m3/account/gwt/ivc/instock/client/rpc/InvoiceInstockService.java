/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceInstockService.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月1日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.instock.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.ivc.instock.client.biz.BInvoiceInstock;
import com.hd123.m3.commons.gwt.bpm.client.rpc2.M3BpmModuleService2;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;

/**
 * 发票入库单|同步接口。
 * 
 * @author lixiaohong
 * @since 1.7
 *
 */
@RemoteServiceRelativePath("invoice/instock")
public interface InvoiceInstockService extends M3BpmModuleService2<BInvoiceInstock>{
  public static final String INVOICE_TYPE="invoiceType";
  
  public static class Locator {
    private static InvoiceInstockServiceAsync service = null;

    public static InvoiceInstockServiceAsync getService() {
      if (service == null)
        service = GWT.create(InvoiceInstockService.class);
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
