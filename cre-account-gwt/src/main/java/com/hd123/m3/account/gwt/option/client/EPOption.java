/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	EPOption.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.option.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.m3.account.gwt.commons.client.biz.BProcessDefinition;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.rpc.InvoiceRegService;
import com.hd123.m3.account.gwt.option.client.rpc.OptionService;
import com.hd123.m3.account.gwt.option.client.ui.OptionConfigEditPage;
import com.hd123.m3.account.gwt.option.client.ui.OptionDefaultEditPage;
import com.hd123.m3.account.gwt.option.client.ui.OptionViewPage;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.ModuleServiceAsync;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.TitleEntryPoint;

/**
 * @author zhuhairui
 * 
 */
public class EPOption extends TitleEntryPoint {

  private BProcessDefinition statementDefaultProcess;
  private BProcessDefinition paymentNoticeDefaultProcess;
  private List<BUCN> paymentTypes;

  public static EPOption getInstance() {
    return EPOption.getInstance(EPOption.class);
  }

  /** 获取账单默认流程 */
  public BProcessDefinition getStatementDefaultBPMProcess() {
    String key = getModuleContext().get(OptionService.KEY_STATEMENTDEFAULTBPMKEY);
    if (StringUtil.isNullOrBlank(key))
      return null;

    if (statementDefaultProcess == null) {
      GWTUtil.enableSynchronousRPC();
      OptionService.Locator.getService().getProcessDefinition(key,
          new RBAsyncCallback2<BProcessDefinition>() {

            @Override
            public void onException(Throwable caught) {
              // Do nothing
            }

            @Override
            public void onSuccess(BProcessDefinition result) {
              statementDefaultProcess = result;
            }
          });
    }
    return statementDefaultProcess;
  }
  
  public Map<String, String> getInvoiceTypes() {
    String invoiceType = getModuleContext().get(InvoiceRegService.KEY_INVOICE_TYPE);
    if (invoiceType == null) {
      return new HashMap<String, String>();
    } else {
      return CollectionUtil.toMap(invoiceType);
    }
  }

  /** 获取账单默认流程 */
  public BProcessDefinition getPaymentNoticeDefaultBPMProcess() {
    String key = getModuleContext().get(OptionService.KEY_PAYMENTNOTICEDEFAULTBPMKEY);
    if (StringUtil.isNullOrBlank(key))
      return null;

    if (paymentNoticeDefaultProcess == null) {
      GWTUtil.enableSynchronousRPC();
      OptionService.Locator.getService().getProcessDefinition(key,
          new RBAsyncCallback2<BProcessDefinition>() {

            @Override
            public void onException(Throwable caught) {
              // Do nothing
            }

            @Override
            public void onSuccess(BProcessDefinition result) {
              paymentNoticeDefaultProcess = result;
            }
          });
    }
    return paymentNoticeDefaultProcess;
  }

  private List<BProcessDefinition> statementDefs;

  public List<BProcessDefinition> getStatementDefs() {
    if (statementDefs == null) {
      GWTUtil.enableSynchronousRPC();
      OptionService.Locator.getService().queryProcessDefinition(
          getModuleContext().get(OptionService.KEY_STATEMENTBPMKEYPREFIX),
          new RBAsyncCallback2<List<BProcessDefinition>>() {

            @Override
            public void onException(Throwable caught) {
              statementDefs = new ArrayList<BProcessDefinition>();
            }

            @Override
            public void onSuccess(List<BProcessDefinition> result) {
              statementDefs = result;

            }
          });
    }
    return statementDefs;
  }

  private List<BProcessDefinition> paymentNoticeDefs;

  public List<BProcessDefinition> getPaymentNoticeDefs() {
    if (paymentNoticeDefs == null) {
      GWTUtil.enableSynchronousRPC();
      OptionService.Locator.getService().queryProcessDefinition(
          getModuleContext().get(OptionService.KEY_PAYMENTNOTICEBPMKEYPREFIX),
          new RBAsyncCallback2<List<BProcessDefinition>>() {

            @Override
            public void onException(Throwable caught) {
              paymentNoticeDefs = new ArrayList<BProcessDefinition>();
            }

            @Override
            public void onSuccess(List<BProcessDefinition> result) {
              paymentNoticeDefs = result;

            }
          });
    }
    return paymentNoticeDefs;
  }

  @Override
  protected void dispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = OptionViewPage.START_NODE;
      if (OptionViewPage.START_NODE.equals(start))
        showContentPage(OptionViewPage.getInstance(), urlParams);
      else if (OptionConfigEditPage.START_NODE.equals(start))
        showContentPage(OptionConfigEditPage.getInstance(), urlParams);
      else if (OptionDefaultEditPage.START_NODE.equals(start))
        showContentPage(OptionDefaultEditPage.getInstance(), urlParams);
    } catch (Exception e) {
      RMsgBox.showError(OptionMessages.M.showPageError(getModuleCaption()), e);
    }
  }

  /** 获取所有可用的付款方式 */
  public List<BUCN> getPaymentTypes() {
    if (paymentTypes == null) {
      GWTUtil.enableSynchronousRPC();
      RLoadingDialog.show(OptionMessages.M.actionDoing(OptionMessages.M.load()
          + OptionMessages.M.paymentType()));
      OptionService.Locator.getService().getPaymentTypes(
          new RBAsyncCallback2<List<BUCN>>() {
            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = OptionMessages.M.actionFailed(OptionMessages.M.load(),
                  OptionMessages.M.paymentType());
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(List<BUCN> result) {
              RLoadingDialog.hide();
              paymentTypes = result;
            }
          });
    }
    return paymentTypes;
  }
  
  @Override
  protected String getModuleCaption() {
    return OptionMessages.M.moduleCaption();
  }

  @Override
  protected ModuleServiceAsync getModuleService() {
    return OptionService.Locator.getService();
  }

}
