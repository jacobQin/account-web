/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	EPInvoiceAbort.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月15日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.abort.client;

import java.util.HashMap;
import java.util.Map;

import com.hd123.m3.account.gwt.ivc.abort.client.biz.BInvoiceAbort;
import com.hd123.m3.account.gwt.ivc.abort.client.rpc.InvoiceAbortService;
import com.hd123.m3.account.gwt.ivc.abort.client.rpc.InvoiceAbortServiceAsync;
import com.hd123.m3.account.gwt.ivc.abort.client.ui.InvoiceAbortCreatePage;
import com.hd123.m3.account.gwt.ivc.abort.client.ui.InvoiceAbortEditPage;
import com.hd123.m3.account.gwt.ivc.abort.client.ui.InvoiceAbortLogPage;
import com.hd123.m3.account.gwt.ivc.abort.client.ui.InvoiceAbortSearchPage;
import com.hd123.m3.account.gwt.ivc.abort.client.ui.InvoiceAbortViewPage;
import com.hd123.m3.account.gwt.ivc.abort.intf.client.dd.PInvoiceAbortDef;
import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
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
 * 发票作废单|入口
 * 
 * @author lixiaohong
 * @since 1.7
 * 
 */
public class EPInvoiceAbort extends EPBpmModule2<BInvoiceAbort> {
  public static final String RESOURCE_KEY = "account.invoice.abort";
  public static final String RESOURCE_PATH = "account/ivc/abort";

  public static EPInvoiceAbort getInstance() {
    return EPInvoiceAbort.getInstance(EPInvoiceAbort.class);
  }

  @Override
  public InvoiceAbortServiceAsync getModuleService() {
    return InvoiceAbortService.Locator.getService();
  }

  @Override
  public String getPrintTemplate() {
    return RESOURCE_PATH;
  }

  @Override
  public String getModuleCaption() {
    return PInvoiceAbortDef.TABLE_CAPTION;
  }

  @Override
  public String getPermResource() {
    return RESOURCE_KEY;
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null) {
        start = InvoiceAbortSearchPage.START_NODE;
      }
      if (InvoiceAbortSearchPage.START_NODE.equals(start)) {
        showContentPage(InvoiceAbortSearchPage.getInstance(), urlParams);
      } else if (InvoiceAbortCreatePage.START_NODE.equals(start)) {
        showContentPage(InvoiceAbortCreatePage.getInstance(), urlParams);
      } else if (InvoiceAbortEditPage.START_NODE.equals(start)) {
        showContentPage(InvoiceAbortEditPage.getInstance(), urlParams);
      } else if (InvoiceAbortViewPage.START_NODE.equals(start)) {
        showContentPage(InvoiceAbortViewPage.getInstance(), urlParams);
      } else if (InvoiceAbortLogPage.START_NODE.equals(start)) {
        showContentPage(InvoiceAbortLogPage.getInstance(), urlParams);
      }

    } catch (Exception e) {
      String msg = CommonsMessages.M.errorWhenShowPage();
      RMsgBox.showError(msg, e);
    }
  }

  public Map<String, String> getInvoiceTypes() {
    String invoiceType = getModuleContext().get(InvoiceAbortService.INVOICE_TYPE);
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
      InvoiceAbortService.Locator.getService().getCurrentEmployee(getCurrentUser().getId(),
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
