/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	EPInvoiceTransport.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月15日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.transport.client;

import java.util.HashMap;
import java.util.Map;

import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.transport.client.biz.BInvoiceTransport;
import com.hd123.m3.account.gwt.ivc.transport.client.rpc.InvoiceTransportService;
import com.hd123.m3.account.gwt.ivc.transport.client.rpc.InvoiceTransportServiceAsync;
import com.hd123.m3.account.gwt.ivc.transport.client.ui.InvoiceTransportCreatePage;
import com.hd123.m3.account.gwt.ivc.transport.client.ui.InvoiceTransportEditPage;
import com.hd123.m3.account.gwt.ivc.transport.client.ui.InvoiceTransportLogPage;
import com.hd123.m3.account.gwt.ivc.transport.client.ui.InvoiceTransportSearchPage;
import com.hd123.m3.account.gwt.ivc.transport.client.ui.InvoiceTransportViewPage;
import com.hd123.m3.account.gwt.ivc.transport.intf.client.InvoiceTransportUrlParams;
import com.hd123.m3.account.gwt.ivc.transport.intf.client.dd.PInvoiceTransportDef;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.bpm.client.EPBpmModule2;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * 发票调拨单|入口
 * 
 * @author lixiaohong
 * @since 1.9
 * 
 */
public class EPInvoiceTransport extends EPBpmModule2<BInvoiceTransport> {

  public static final String RESOURCE_KEY = "account.invoice.transport";
  public static final String RESOURCE_PATH = "account/ivc/transport";

  public static EPInvoiceTransport getInstance() {
    return EPInvoiceTransport.getInstance(EPInvoiceTransport.class);
  }

  @Override
  public InvoiceTransportServiceAsync getModuleService() {
    return InvoiceTransportService.Locator.getService();
  }

  @Override
  public String getPrintTemplate() {
    return RESOURCE_PATH;
  }

  @Override
  public String getModuleCaption() {
    return PInvoiceTransportDef.TABLE_CAPTION;
  }

  @Override
  public String getPermResource() {
    return RESOURCE_KEY;
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null) {
        start = InvoiceTransportUrlParams.START_NODE_SEARCH;
      }
      if (InvoiceTransportUrlParams.START_NODE_SEARCH.equals(start)) {
        showContentPage(InvoiceTransportSearchPage.getInstance(), urlParams);
      } else if (InvoiceTransportUrlParams.START_NODE_CREATE.equals(start)) {
        showContentPage(InvoiceTransportCreatePage.getInstance(), urlParams);
      }
       else if (InvoiceTransportUrlParams.START_NODE_EDIT.equals(start)) {
       showContentPage(InvoiceTransportEditPage.getInstance(), urlParams);
       }
      else if (InvoiceTransportUrlParams.START_NODE_VIEW.equals(start)) {
        showContentPage(InvoiceTransportViewPage.getInstance(), urlParams);
      } else if (InvoiceTransportUrlParams.START_NODE_LOG.equals(start)) {
        showContentPage(InvoiceTransportLogPage.getInstance(), urlParams);
      }

    } catch (Exception e) {
      String msg = CommonsMessages.M.errorWhenShowPage();
      RMsgBox.showError(msg, e);
    }
  }

  public Map<String, String> getInvoiceTypes() {
    String invoiceType = getModuleContext().get(InvoiceTransportService.INVOICE_TYPE);
    if (invoiceType == null) {
      return new HashMap<String, String>();
    } else {
      return CollectionUtil.toMap(invoiceType);
    }
  }

  /** 当前员工 */
  private BUCN currentEmployee;

  public BUCN getCurrentEmployee() {
    if (currentEmployee == null) {
      GWTUtil.enableSynchronousRPC();
      RLoadingDialog.show(InvoiceCommonMessages.M.actionDoing(InvoiceCommonMessages.M.load()
          + InvoiceCommonMessages.M.currentEmployee()));
      InvoiceTransportService.Locator.getService().getCurrentEmployee(getCurrentUser().getId(),
          new RBAsyncCallback2<BUCN>() {
            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = InvoiceCommonMessages.M.actionFailed(InvoiceCommonMessages.M.load(),
                  InvoiceCommonMessages.M.currentEmployee());
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(BUCN result) {
              RLoadingDialog.hide();
              currentEmployee = result;
            }
          });
    }
    return currentEmployee;
  }

}
