/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	EPInvoiceReturn.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.returns.client;

import java.util.HashMap;
import java.util.Map;

import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.returns.client.biz.BInvoiceReturn;
import com.hd123.m3.account.gwt.ivc.returns.client.rpc.InvoiceReturnService;
import com.hd123.m3.account.gwt.ivc.returns.client.rpc.InvoiceReturnServiceAsync;
import com.hd123.m3.account.gwt.ivc.returns.client.ui.InvoiceReturnCreatePage;
import com.hd123.m3.account.gwt.ivc.returns.client.ui.InvoiceReturnEditPage;
import com.hd123.m3.account.gwt.ivc.returns.client.ui.InvoiceReturnLogPage;
import com.hd123.m3.account.gwt.ivc.returns.client.ui.InvoiceReturnSearchPage;
import com.hd123.m3.account.gwt.ivc.returns.client.ui.InvoiceReturnViewPage;
import com.hd123.m3.account.gwt.ivc.returns.intf.client.dd.PInvoiceReturnDef;
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
 * 发票领退单|入口
 * 
 * @author lixiaohong
 * @since 1.7
 * 
 */
public class EPInvoiceReturn extends EPBpmModule2<BInvoiceReturn> {
  public static final String RESOURCE_KEY = "account.invoice.return";
  public static final String RESOURCE_PATH = "account/ivc/return";

  public static EPInvoiceReturn getInstance() {
    return EPInvoiceReturn.getInstance(EPInvoiceReturn.class);
  }

  @Override
  public InvoiceReturnServiceAsync getModuleService() {
    return InvoiceReturnService.Locator.getService();
  }

  @Override
  public String getPrintTemplate() {
    return RESOURCE_PATH;
  }

  @Override
  public String getModuleCaption() {
    return PInvoiceReturnDef.TABLE_CAPTION;
  }

  @Override
  public String getPermResource() {
    return RESOURCE_KEY;
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null) {
        start = InvoiceReturnSearchPage.START_NODE;
      }
      if (InvoiceReturnSearchPage.START_NODE.equals(start)) {
        showContentPage(InvoiceReturnSearchPage.getInstance(), urlParams);
      } else if (InvoiceReturnCreatePage.START_NODE.equals(start)) {
        showContentPage(InvoiceReturnCreatePage.getInstance(), urlParams);
      } else if (InvoiceReturnEditPage.START_NODE.equals(start)) {
        showContentPage(InvoiceReturnEditPage.getInstance(), urlParams);
      } else if (InvoiceReturnViewPage.START_NODE.equals(start)) {
        showContentPage(InvoiceReturnViewPage.getInstance(), urlParams);
      } else if (InvoiceReturnLogPage.START_NODE.equals(start)) {
        showContentPage(InvoiceReturnLogPage.getInstance(), urlParams);
      }

    } catch (Exception e) {
      String msg = CommonsMessages.M.errorWhenShowPage();
      RMsgBox.showError(msg, e);
    }
  }

  public Map<String, String> getInvoiceTypes() {
    String invoiceType = getModuleContext().get(InvoiceReturnService.INVOICE_TYPE);
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
      InvoiceReturnService.Locator.getService().getCurrentEmployee(getCurrentUser().getId(),
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
