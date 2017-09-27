/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	EPInvoiceRecycle.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.client;

import java.util.HashMap;
import java.util.Map;

import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.recycle.client.biz.BInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.client.rpc.InvoiceRecycleService;
import com.hd123.m3.account.gwt.ivc.recycle.client.rpc.InvoiceRecycleServiceAsync;
import com.hd123.m3.account.gwt.ivc.recycle.client.ui.InvoiceRecycleCreatePage;
import com.hd123.m3.account.gwt.ivc.recycle.client.ui.InvoiceRecycleEditPage;
import com.hd123.m3.account.gwt.ivc.recycle.client.ui.InvoiceRecycleLogPage;
import com.hd123.m3.account.gwt.ivc.recycle.client.ui.InvoiceRecycleSearchPage;
import com.hd123.m3.account.gwt.ivc.recycle.client.ui.InvoiceRecycleViewPage;
import com.hd123.m3.account.gwt.ivc.recycle.intf.client.InvoiceRecycleUrlParams;
import com.hd123.m3.account.gwt.ivc.recycle.intf.client.dd.PInvoiceRecycleDef;
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
 * 发票回收单|入口
 * 
 * @author lixiaohong
 * @since 1.9
 *
 */
public class EPInvoiceRecycle extends EPBpmModule2<BInvoiceRecycle>{

  public static final String RESOURCE_KEY = "account.invoice.recycle";
  public static final String RESOURCE_PATH = "account/ivc/recycle";

  public static EPInvoiceRecycle getInstance() {
    return EPInvoiceRecycle.getInstance(EPInvoiceRecycle.class);
  }

  @Override
  public InvoiceRecycleServiceAsync getModuleService() {
    return InvoiceRecycleService.Locator.getService();
  }

  @Override
  public String getPrintTemplate() {
    return RESOURCE_PATH;
  }

  @Override
  public String getModuleCaption() {
    return PInvoiceRecycleDef.TABLE_CAPTION;
  }

  @Override
  public String getPermResource() {
    return RESOURCE_KEY;
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null) {
        start = InvoiceRecycleUrlParams.START_NODE_SEARCH;
      }
      if (InvoiceRecycleUrlParams.START_NODE_SEARCH.equals(start)) {
        showContentPage(InvoiceRecycleSearchPage.getInstance(), urlParams);
      } else if (InvoiceRecycleUrlParams.START_NODE_CREATE.equals(start)) {
        showContentPage(InvoiceRecycleCreatePage.getInstance(), urlParams);
      }
      else if (InvoiceRecycleUrlParams.START_NODE_EDIT.equals(start)) {
       showContentPage(InvoiceRecycleEditPage.getInstance(), urlParams);
       }
      else if (InvoiceRecycleUrlParams.START_NODE_VIEW.equals(start)) {
        showContentPage(InvoiceRecycleViewPage.getInstance(), urlParams);
      } else if (InvoiceRecycleUrlParams.START_NODE_LOG.equals(start)) {
        showContentPage(InvoiceRecycleLogPage.getInstance(), urlParams);
      }

    } catch (Exception e) {
      String msg = CommonsMessages.M.errorWhenShowPage();
      RMsgBox.showError(msg, e);
    }
  }

  public Map<String, String> getInvoiceTypes() {
    String invoiceType = getModuleContext().get(InvoiceRecycleService.INVOICE_TYPE);
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
      InvoiceRecycleService.Locator.getService().getCurrentEmployee(getCurrentUser().getId(),
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
