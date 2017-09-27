package com.hd123.m3.account.gwt.ivc.instock.client;

import java.util.HashMap;
import java.util.Map;

import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.instock.client.biz.BInvoiceInstock;
import com.hd123.m3.account.gwt.ivc.instock.client.rpc.InvoiceInstockService;
import com.hd123.m3.account.gwt.ivc.instock.client.rpc.InvoiceInstockServiceAsync;
import com.hd123.m3.account.gwt.ivc.instock.client.ui.InvoiceInstockCreatePage;
import com.hd123.m3.account.gwt.ivc.instock.client.ui.InvoiceInstockEditPage;
import com.hd123.m3.account.gwt.ivc.instock.client.ui.InvoiceInstockLogPage;
import com.hd123.m3.account.gwt.ivc.instock.client.ui.InvoiceInstockSearchPage;
import com.hd123.m3.account.gwt.ivc.instock.client.ui.InvoiceInstockViewPage;
import com.hd123.m3.account.gwt.ivc.instock.intf.client.dd.PInvoiceInstockDef;
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
 * 发票入库单|入口
 * 
 * @author lixiaohong
 * @since 1.7
 *
 */
public class EPInvoiceInstock extends EPBpmModule2<BInvoiceInstock> {
  public static final String RESOURCE_KEY = "account.invoice.instock";
  public static final String RESOURCE_PATH = "account/ivc/instock";

  public static EPInvoiceInstock getInstance() {
    return EPInvoiceInstock.getInstance(EPInvoiceInstock.class);
  }

  @Override
  public InvoiceInstockServiceAsync getModuleService() {
    return InvoiceInstockService.Locator.getService();
  }

  @Override
  public String getPrintTemplate() {
    return RESOURCE_PATH;
  }

  @Override
  public String getModuleCaption() {
    return PInvoiceInstockDef.TABLE_CAPTION;
  }

  @Override
  public String getPermResource() {
    return RESOURCE_KEY;
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null) {
        start = InvoiceInstockSearchPage.START_NODE;
      }

      if (InvoiceInstockSearchPage.START_NODE.equals(start)) {
        showContentPage(InvoiceInstockSearchPage.getInstance(), urlParams);
      } else if (InvoiceInstockCreatePage.START_NODE.equals(start)) {
        showContentPage(InvoiceInstockCreatePage.getInstance(), urlParams);
      } else if (InvoiceInstockEditPage.START_NODE.equals(start)) {
        showContentPage(InvoiceInstockEditPage.getInstance(), urlParams);
      } else if (InvoiceInstockViewPage.START_NODE.equals(start)) {
        showContentPage(InvoiceInstockViewPage.getInstance(), urlParams);
      } else if (InvoiceInstockLogPage.START_NODE.equals(start)) {
        showContentPage(InvoiceInstockLogPage.getInstance(), urlParams);
      }

    } catch (Exception e) {
      String msg = CommonsMessages.M.errorWhenShowPage();
      RMsgBox.showError(msg, e);
    }
  }

  public Map<String, String> getInvoiceTypes() {
    String invoiceType = getModuleContext().get(InvoiceInstockService.INVOICE_TYPE);
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
      InvoiceInstockService.Locator.getService().getCurrentEmployee(getCurrentUser().getId(),
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
