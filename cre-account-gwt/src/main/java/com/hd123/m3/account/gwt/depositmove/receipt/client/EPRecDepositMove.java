/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	EPRecDepositMove.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.receipt.client;

import com.hd123.m3.account.gwt.base.client.AccEPBpmModule;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BDepositMove;
import com.hd123.m3.account.gwt.depositmove.intf.client.DepositMoveMessage;
import com.hd123.m3.account.gwt.depositmove.receipt.client.rpc.RecDepositMoveService;
import com.hd123.m3.account.gwt.depositmove.receipt.client.rpc.RecDepositMoveServiceAsync;
import com.hd123.m3.account.gwt.depositmove.receipt.client.ui.RecDepositMoveCreatePage;
import com.hd123.m3.account.gwt.depositmove.receipt.client.ui.RecDepositMoveEditPage;
import com.hd123.m3.account.gwt.depositmove.receipt.client.ui.RecDepositMoveLogViewPage;
import com.hd123.m3.account.gwt.depositmove.receipt.client.ui.RecDepositMoveSearchPage;
import com.hd123.m3.account.gwt.depositmove.receipt.client.ui.RecDepositMoveViewPage;
import com.hd123.m3.account.gwt.depositmove.receipt.intf.client.RecDepositMoveUrlParams;
import com.hd123.m3.account.gwt.depositmove.receipt.intf.client.perm.RecDepositMovePermDef;
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
public class EPRecDepositMove extends AccEPBpmModule {
  public static final String OPN_ENTITY = "recDepositMove";

  public static EPRecDepositMove getInstance() {
    return EPRecDepositMove.getInstance(EPRecDepositMove.class);
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = RecDepositMoveSearchPage.START_NODE;

      if (RecDepositMoveSearchPage.START_NODE.equals(start)) {
        showContentPage(RecDepositMoveSearchPage.getInstance(), urlParams);
      } else if (RecDepositMoveCreatePage.START_NODE.equals(start)) {
        showContentPage(RecDepositMoveCreatePage.getInstance(), urlParams);
      } else if (RecDepositMoveEditPage.START_NODE.equals(start)) {
        showContentPage(RecDepositMoveEditPage.getInstance(), urlParams);
      } else if (RecDepositMoveViewPage.START_NODE.equals(start)) {
        showContentPage(RecDepositMoveViewPage.getInstance(), urlParams);
      } else if (RecDepositMoveLogViewPage.START_NODE.equals(start)) {
        showContentPage(RecDepositMoveLogViewPage.getInstance(), urlParams);
      }
    } catch (Exception e) {
      String msg = DepositMoveMessage.M.processError(DepositMoveMessage.M.show());
      RMsgBox.showError(msg, e);
    }
  }

  @Override
  public String getModuleCaption() {
    return RecDepositMoveUrlParams.MODULE_CAPTION;
  }

  @Override
  public RecDepositMoveServiceAsync getModuleService() {
    return RecDepositMoveService.Locator.getService();
  }

  /**
   * 返回打印模板路径
   * 
   * @return
   */
  public String getPrintTemplate() {
    return RecDepositMovePermDef.PRINT_PATH;
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
        JumpParameters params = RecDepositMoveSearchPage.getInstance().getLastParams();
        jump(params);
        return;
      }
      if (event.isEmpty()) {
        JumpParameters params = new JumpParameters(RecDepositMoveSearchPage.START_NODE);
        jump(params);
        return;
      }
      doSearch(event.getKeyword());
    }
  }

  private void doSearch(final String keyword) {
    RLoadingDialog.show(DepositMoveMessage.M.actionDoing(DepositMoveMessage.M.search()));
    RecDepositMoveService.Locator.getService().loadByNumber(keyword,
        new RBAsyncCallback2<BDepositMove>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            params.setStart(RecDepositMoveSearchPage.START_NODE);
            params.getUrlRef().set(RecDepositMoveSearchPage.PN_KEYWORD, keyword);
            jump(params);
          }

          public void onSuccess(BDepositMove result) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            if (result == null) {
              params.setStart(RecDepositMoveSearchPage.START_NODE);
              params.getUrlRef().set(RecDepositMoveSearchPage.PN_KEYWORD, keyword);
            } else {
              params.setStart(RecDepositMoveViewPage.START_NODE);
              params.getUrlRef().set(RecDepositMoveViewPage.PN_ENTITY_UUID, result.getUuid());
            }
            jump(params);
          }
        });
  }

  @Override
  public String getPermResource() {
    return RecDepositMovePermDef.RESOURCE_RECDEPOSITMOVE;
  }
}
