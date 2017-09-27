/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	EPPaymentNotice.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-11 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.base.client.AccEPBpmModule;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BProcessDefinition;
import com.hd123.m3.account.gwt.paymentnotice.client.rpc.PaymentNoticeService;
import com.hd123.m3.account.gwt.paymentnotice.client.rpc.PaymentNoticeServiceAsync;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.PaymentNoticeBatchPage;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.PaymentNoticeCreatePage;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.PaymentNoticeEditPage;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.PaymentNoticeLogPage;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.PaymentNoticeSearchPage;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.PaymentNoticeViewPage;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.dd.PPaymentNoticeDef;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.perm.PaymentNoticePermDef;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchBox;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchEvent;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchHandler;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author zhuhairui
 * 
 */
public class EPPaymentNotice extends AccEPBpmModule {

  public static final String OPN_ENTITY = "paymentNotice";

  public static EPPaymentNotice getInstance() {
    return EPPaymentNotice.getInstance(EPPaymentNotice.class);
  }

  public PaymentNoticeServiceAsync getModuleService() {
    return PaymentNoticeService.Locator.getService();
  }

  @Override
  public String getModuleCaption() {
    return PPaymentNoticeDef.TABLE_CAPTION;
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = PaymentNoticeSearchPage.START_NODE;

      if (PaymentNoticeSearchPage.START_NODE.equals(start))
        showContentPage(PaymentNoticeSearchPage.getInstance(), urlParams);
      else if (PaymentNoticeCreatePage.START_NODE.equals(start))
        showContentPage(PaymentNoticeCreatePage.getInstance(), urlParams);
      else if (PaymentNoticeEditPage.START_NODE.equals(start))
        showContentPage(PaymentNoticeEditPage.getInstance(), urlParams);
      else if (PaymentNoticeViewPage.START_NODE.equals(start))
        showContentPage(PaymentNoticeViewPage.getInstance(), urlParams);
      else if (PaymentNoticeLogPage.START_NODE.equals(start))
        showContentPage(PaymentNoticeLogPage.getInstance(), urlParams);
      else if (PaymentNoticeBatchPage.START_NODE.equals(start))
        showContentPage(PaymentNoticeBatchPage.getInstance(), urlParams);
    } catch (Exception e) {
      String msg = PaymentNoticeMessages.M.errorWhenShowPage();
      RMsgBox.showError(msg, e);
    }
  }

  @Override
  protected void onCreate() {
    super.onCreate();

    searchBox = new RSearchBox();
    searchBox.addSearchHandler(new SearchBoxHandler());
    appendSearchBox();
  }

  public void appendSearchBox() {
    getTitleBar().add(new RToolbarSeparator(), RToolbar.ALIGN_RIGHT);

    getTitleBar().add(searchBox, RToolbar.ALIGN_RIGHT);

  }

  /**
   * 返回打印模板路径
   * 
   * @return
   */
  public String getPrintTemplate() {
    return PaymentNoticePermDef.PRINT_PATH;
  }

  private RSearchBox searchBox;

  private class SearchBoxHandler implements RSearchHandler {

    @Override
    public void onSearch(RSearchEvent event) {
      if (event.isBack()) {
        jump(PaymentNoticeSearchPage.getInstance().getLastParams());
        return;
      }

      if (event.isEmpty()) {
        try {
          JumpParameters params = new JumpParameters(PaymentNoticeSearchPage.START_NODE);
          jump(params);
        } catch (Exception e) {
          RMsgBox.showError(e);
        }
        return;
      }

      doSearch(event.getKeyword());

    }
  }

  private void doSearch(final String keyword) {
    RLoadingDialog.show(PaymentNoticeMessages.M.actionDoing(PaymentNoticeMessages.M.search()));
    PaymentNoticeService.Locator.getService().loadByNumber(keyword,
        new RBAsyncCallback2<BPaymentNotice>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            params.setStart(PaymentNoticeSearchPage.START_NODE);
            params.getUrlRef().set(PaymentNoticeSearchPage.PN_KEYWORD, keyword);
            jump(params);
          }

          @Override
          public void onSuccess(BPaymentNotice result) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            if (result == null) {
              params.setStart(PaymentNoticeSearchPage.START_NODE);
              params.getUrlRef().set(PaymentNoticeSearchPage.PN_KEYWORD, keyword);
            } else {
              params.setStart(PaymentNoticeViewPage.START_NODE);
              params.getUrlRef().set(PaymentNoticeViewPage.PN_ENTITY_UUID, result.getUuid());
            }
            jump(params);
          }
        });
  }

  /** 获取默认流程定义，当前用户必须拥有发起权，否则返回空。 */
  public String getDefaultDef() {
    String key = getModuleContext().get(PaymentNoticeService.KEY_DEFAULTBPMKEY);
    if (StringUtil.isNullOrBlank(key)) {
      return null;
    }
    for (BProcessDefinition def : getDefs()) {
      if (key.equals(def.getKey())) {
        return key;
      }
    }
    return null;
  }

  private List<BProcessDefinition> defs;

  /** 获取当前用户有发起权的账单流程定义 */
  public List<BProcessDefinition> getDefs() {
    if (defs == null) {
      GWTUtil.enableSynchronousRPC();
      PaymentNoticeService.Locator.getService().queryProcessDefinition(
          new RBAsyncCallback2<List<BProcessDefinition>>() {

            @Override
            public void onException(Throwable caught) {
              defs = new ArrayList<BProcessDefinition>();
            }

            @Override
            public void onSuccess(List<BProcessDefinition> result) {
              defs = result;

            }
          });
    }
    return defs;
  }

  List<String> coopModes = null;

  /** 获取合作方式列表 */
  public List<String> getCoopModes() {
    if (coopModes == null) {
      coopModes = new ArrayList<String>();
      GWTUtil.enableSynchronousRPC();
      PaymentNoticeService.Locator.getService().getCoopModes(new RBAsyncCallback2<List<String>>() {

        @Override
        public void onException(Throwable caught) {
        }

        @Override
        public void onSuccess(List<String> result) {
          if (result != null && result.isEmpty() == false) {
            coopModes = result;
          }
        }
      });
    }
    return coopModes;
  }

  @Override
  public String getPermResource() {
    return PaymentNoticePermDef.RESOURCE_PAYMENTNOTICE;
  }

}
