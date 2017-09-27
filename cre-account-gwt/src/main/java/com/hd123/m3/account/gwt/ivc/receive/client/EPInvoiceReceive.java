/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	EPInvoiceReceive.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.receive.client;

import java.util.HashMap;
import java.util.Map;

import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.receive.client.biz.BInvoiceReceive;
import com.hd123.m3.account.gwt.ivc.receive.client.rpc.InvoiceReceiveService;
import com.hd123.m3.account.gwt.ivc.receive.client.rpc.InvoiceReceiveServiceAsync;
import com.hd123.m3.account.gwt.ivc.receive.client.ui.InvoiceReceiveCreatePage;
import com.hd123.m3.account.gwt.ivc.receive.client.ui.InvoiceReceiveEditPage;
import com.hd123.m3.account.gwt.ivc.receive.client.ui.InvoiceReceiveLogPage;
import com.hd123.m3.account.gwt.ivc.receive.client.ui.InvoiceReceiveSearchPage;
import com.hd123.m3.account.gwt.ivc.receive.client.ui.InvoiceReceiveViewPage;
import com.hd123.m3.account.gwt.ivc.receive.intf.client.dd.PInvoiceReceiveDef;
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
 * 发票领用单|入口
 * 
 * @author lixiaohong
 * @since 1.7
 * 
 */
public class EPInvoiceReceive extends EPBpmModule2<BInvoiceReceive> {
  public static final String RESOURCE_KEY = "account.invoice.receive";
  public static final String RESOURCE_PATH = "account/ivc/receive";

  public static EPInvoiceReceive getInstance() {
    return EPInvoiceReceive.getInstance(EPInvoiceReceive.class);
  }

  @Override
  public InvoiceReceiveServiceAsync getModuleService() {
    return InvoiceReceiveService.Locator.getService();
  }

  @Override
  public String getPrintTemplate() {
    return RESOURCE_PATH;
  }

  @Override
  public String getModuleCaption() {
    return PInvoiceReceiveDef.TABLE_CAPTION;
  }

  @Override
  public String getPermResource() {
    return RESOURCE_KEY;
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null) {
        start = InvoiceReceiveSearchPage.START_NODE;
      }
      if (InvoiceReceiveSearchPage.START_NODE.equals(start)) {
        showContentPage(InvoiceReceiveSearchPage.getInstance(), urlParams);
      } else if (InvoiceReceiveCreatePage.START_NODE.equals(start)) {
        showContentPage(InvoiceReceiveCreatePage.getInstance(), urlParams);
      } else if (InvoiceReceiveEditPage.START_NODE.equals(start)) {
        showContentPage(InvoiceReceiveEditPage.getInstance(), urlParams);
      } else if (InvoiceReceiveViewPage.START_NODE.equals(start)) {
        showContentPage(InvoiceReceiveViewPage.getInstance(), urlParams);
      } else if (InvoiceReceiveLogPage.START_NODE.equals(start)) {
        showContentPage(InvoiceReceiveLogPage.getInstance(), urlParams);
      }

    } catch (Exception e) {
      String msg = CommonsMessages.M.errorWhenShowPage();
      RMsgBox.showError(msg, e);
    }
  }

  public Map<String, String> getInvoiceTypes() {
    String invoiceType = getModuleContext().get(InvoiceReceiveService.INVOICE_TYPE);
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
      InvoiceReceiveService.Locator.getService().getCurrentEmployee(getCurrentUser().getId(),
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
