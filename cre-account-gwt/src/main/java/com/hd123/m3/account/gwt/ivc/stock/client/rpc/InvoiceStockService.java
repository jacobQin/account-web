/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceStockService.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月10日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.stock.client.rpc;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.ivc.stock.client.biz.BInvoiceStock;
import com.hd123.m3.account.gwt.ivc.stock.client.biz.BInvoiceStockRegLine;
import com.hd123.m3.commons.gwt.bpm.client.rpc2.M3BpmModuleService2;

/**
 * 发票库存|同步接口。
 * 
 * @author lixiaohong
 * @since 1.7
 *
 */
@RemoteServiceRelativePath("invoice/stock")
public interface InvoiceStockService extends M3BpmModuleService2<BInvoiceStock>{
  public static final String INVOICE_TYPE="invoiceType";
  public static class Locator {
    private static InvoiceStockServiceAsync service = null;

    public static InvoiceStockServiceAsync getService() {
      if (service == null)
        service = GWT.create(InvoiceStockService.class);
      return service;
    }
  }
  
  /**
   * 根据发票号码查询该发票号码对应的登记明细
   * @param invoiceNumber
   *        传入null或者空字符串将导致直接返回空列表。
   *        
   * @return 返回指定发票号码的登记明细，至少返回一个空列表。
   * @throws Exception
   */
  public List<BInvoiceStockRegLine> getRegLinesByNumber(String invoiceNumber) throws Exception;
  
}
