/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	CommonService.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月14日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.common.client.rpc;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceStock;
import com.hd123.rumba.gwt.base.client.QueryFilter;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.ModuleService;

/**
 * Common模块服务 | 接口。
 * 
 * @author lixiaohong
 * @since 1.7
 *
 */
@RemoteServiceRelativePath("ivc/common")
public interface CommonService extends ModuleService{
  public static class Locator {
    private static CommonServiceAsync service = null;

    public static CommonServiceAsync getService() {
      if (service == null)
        service = GWT.create(CommonService.class);
      return service;
    }
  }
  
  RPageData<BInvoiceStock> queryStocks(QueryFilter filter) throws ClientBizException;

  BInvoiceStock getStockByNumber(String serialNumber, Map<String, Object> filter)
      throws ClientBizException;
  
  Map<String,String> getInvoiceTypes() throws ClientBizException;
}
