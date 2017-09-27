/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	EPInvoiceStock.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月10日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.stock.client;

import java.util.HashMap;
import java.util.Map;

import com.hd123.m3.account.gwt.ivc.stock.client.biz.BInvoiceStock;
import com.hd123.m3.account.gwt.ivc.stock.client.rpc.InvoiceStockService;
import com.hd123.m3.account.gwt.ivc.stock.client.ui.InvoiceStockSearchPage;
import com.hd123.m3.account.gwt.ivc.stock.intf.client.InvoiceStockUrlParams;
import com.hd123.m3.account.gwt.ivc.stock.intf.client.dd.PInvoiceStockDef;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;
import com.hd123.m3.commons.gwt.bpm.client.EPBpmModule2;
import com.hd123.m3.commons.gwt.bpm.client.rpc2.M3BpmModuleService2Async;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.commons.gwt.mini.client.http.Url;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 发票库存|入口
 * 
 * @author lixiaohong
 * @since 1.7
 * 
 */
public class EPInvoiceStock extends EPBpmModule2<BInvoiceStock>{
  public static final String RESOURCE_KEY = "account.invoice.stock";
  
  public static EPInvoiceStock getInstance() {
    return EPInvoiceStock.getInstance(EPInvoiceStock.class);
  }

  @Override
  public M3BpmModuleService2Async getModuleService() {
    return InvoiceStockService.Locator.getService();
  }

  @Override
  public String getPrintTemplate() {
    return null;
  }

  @Override
  public String getModuleCaption() {
    return PInvoiceStockDef.TABLE_CAPTION;
  }

  @Override
  public String getPermResource() {
    return RESOURCE_KEY;
  }
  
  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    if (StringUtil.isNullOrBlank(start)) {
      start = InvoiceStockSearchPage.START_NODE;
    }
    try{
    if (start.equals(InvoiceStockSearchPage.START_NODE)) {
      showContentPage(InvoiceStockSearchPage.getInstance(), urlParams);
    }
    }catch (Exception e) {
      String msg = CommonsMessages.M.errorWhenShowPage();
      RMsgBox.showError(msg, e);
    }
    
  }

  public Map<String, String> getInvoiceTypes() {
    String invoiceType = getModuleContext().get(InvoiceStockService.INVOICE_TYPE);
    if (invoiceType == null) {
      return new HashMap<String, String>();
    } else {
      return CollectionUtil.toMap(invoiceType);
    }
  }
  
  @Override
  public void jumpToSearchPage(String keyword, Message msg) {
    if (StringUtil.isNullOrBlank(keyword)) {
      jumpToSearchPage(msg);
    } else if (isBpmMode()) {
      // 模块间跳转
      Url url = getCurrentNoBpmUrl(BpmModuleUrlParams.START_NODE_SEARCH);
      url.getUnion().set(InvoiceStockUrlParams.Flecs.FIELD_INVOICE_NUNBER, keyword);
      RWindow.navigate(url.getUrl());
    } else {
      JumpParameters params = new JumpParameters();
      params.setStart(BpmModuleUrlParams.START_NODE_SEARCH);
      params.getUrlRef().set(InvoiceStockUrlParams.Flecs.FIELD_INVOICE_NUNBER, keyword);
      if (msg != null) {
        params.getMessages().add(msg);
      }
      jump(params);
    }
  }
}
