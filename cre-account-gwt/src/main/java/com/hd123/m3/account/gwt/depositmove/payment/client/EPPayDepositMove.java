/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	EPPayDepositMove.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-10 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.payment.client;

import com.hd123.m3.account.gwt.base.client.AccEPBpmModule;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BDepositMove;
import com.hd123.m3.account.gwt.depositmove.intf.client.DepositMoveMessage;
import com.hd123.m3.account.gwt.depositmove.payment.client.rpc.PayDepositMoveService;
import com.hd123.m3.account.gwt.depositmove.payment.client.rpc.PayDepositMoveServiceAsync;
import com.hd123.m3.account.gwt.depositmove.payment.client.ui.PayDepositMoveCreatePage;
import com.hd123.m3.account.gwt.depositmove.payment.client.ui.PayDepositMoveEditPage;
import com.hd123.m3.account.gwt.depositmove.payment.client.ui.PayDepositMoveLogViewPage;
import com.hd123.m3.account.gwt.depositmove.payment.client.ui.PayDepositMoveSearchPage;
import com.hd123.m3.account.gwt.depositmove.payment.client.ui.PayDepositMoveViewPage;
import com.hd123.m3.account.gwt.depositmove.payment.intf.client.PayDepositMoveUrlParams;
import com.hd123.m3.account.gwt.depositmove.payment.intf.client.perm.PayDepositMovePermDef;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchBox;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchEvent;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchHandler;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
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
public class EPPayDepositMove extends AccEPBpmModule {
  public static final String OPN_ENTITY = "payDepositMove";

  public static EPPayDepositMove getInstance() {
    return EPPayDepositMove.getInstance(EPPayDepositMove.class);
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = PayDepositMoveSearchPage.START_NODE;

      if (PayDepositMoveSearchPage.START_NODE.equals(start)) {
        showContentPage(PayDepositMoveSearchPage.getInstance(), urlParams);
      } else if (PayDepositMoveCreatePage.START_NODE.equals(start)) {
        showContentPage(PayDepositMoveCreatePage.getInstance(), urlParams);
      } else if (PayDepositMoveEditPage.START_NODE.equals(start)) {
        showContentPage(PayDepositMoveEditPage.getInstance(), urlParams);
      } else if (PayDepositMoveViewPage.START_NODE.equals(start)) {
        showContentPage(PayDepositMoveViewPage.getInstance(), urlParams);
      } else if (PayDepositMoveLogViewPage.START_NODE.equals(start)) {
        showContentPage(PayDepositMoveLogViewPage.getInstance(), urlParams);
      }
    } catch (Exception e) {
      String msg = DepositMoveMessage.M.errorWhenShowPage();
      RMsgBox.showError(msg, e);
    }
  }

  @Override
  public String getModuleCaption() {
    return PayDepositMoveUrlParams.MODULE_CAPTION;
  }

  @Override
  public PayDepositMoveServiceAsync getModuleService() {
    return PayDepositMoveService.Locator.getService();
  }

  /**
   * 返回打印模板路径
   * 
   * @return
   */
  public String getPrintTemplate() {
    return PayDepositMovePermDef.PRINT_PATH;
  }

  private RSearchBox searchBox;

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

  private class SearchBoxHandler implements RSearchHandler {
    @Override
    public void onSearch(RSearchEvent event) {
      if (event.isBack()) {
        JumpParameters params = PayDepositMoveSearchPage.getInstance().getLastParams();
        jump(params);
        return;
      }
      if (event.isEmpty()) {
        JumpParameters params = new JumpParameters(PayDepositMoveSearchPage.START_NODE);
        jump(params);
        return;
      }
      doSearch(event.getKeyword());
    }
  }

  private void doSearch(final String keyword) {
    RLoadingDialog.show(DepositMoveMessage.M.actionDoing(DepositMoveMessage.M.search()));
    PayDepositMoveService.Locator.getService().loadByNumber(keyword,
        new RBAsyncCallback2<BDepositMove>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            params.setStart(PayDepositMoveSearchPage.START_NODE);
            params.getUrlRef().set(PayDepositMoveSearchPage.PN_KEYWORD, keyword);
            jump(params);
          }

          public void onSuccess(BDepositMove result) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            if (result == null) {
              params.setStart(PayDepositMoveSearchPage.START_NODE);
              params.getUrlRef().set(PayDepositMoveSearchPage.PN_KEYWORD, keyword);
            } else {
              params.setStart(PayDepositMoveViewPage.START_NODE);
              params.getUrlRef().set(PayDepositMoveViewPage.PN_ENTITY_UUID, result.getUuid());
            }
            jump(params);
          }
        });
  }

  @Override
  public String getPermResource() {
    return PayDepositMovePermDef.RESOURCE_PAYDEPOSITMOVE;
  }
}
