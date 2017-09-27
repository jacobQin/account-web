/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	EPFreeze.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-3 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.freeze.client;

import java.util.Map;

import com.hd123.m3.account.gwt.base.client.AccEPBpmModule;
import com.hd123.m3.account.gwt.freeze.client.biz.BFreeze;
import com.hd123.m3.account.gwt.freeze.client.rpc.FreezeService;
import com.hd123.m3.account.gwt.freeze.client.rpc.FreezeServiceAsync;
import com.hd123.m3.account.gwt.freeze.client.ui.FreezeCreatePage;
import com.hd123.m3.account.gwt.freeze.client.ui.FreezeLogPage;
import com.hd123.m3.account.gwt.freeze.client.ui.FreezeSearchPage;
import com.hd123.m3.account.gwt.freeze.client.ui.FreezeViewPage;
import com.hd123.m3.account.gwt.freeze.intf.client.FreezeMessages;
import com.hd123.m3.account.gwt.freeze.intf.client.dd.PFreezeDef;
import com.hd123.m3.account.gwt.freeze.intf.client.perm.FreezePermDef;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchBox;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchEvent;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchHandler;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 账款冻结单入口。
 * 
 * @author zhuhairui
 * 
 */
public class EPFreeze extends AccEPBpmModule {
  public static final String OPN_ENTITY = "freeze";

  public static EPFreeze getInstance() {
    return EPFreeze.getInstance(EPFreeze.class);
  }

  @Override
  public String getModuleCaption() {
    return PFreezeDef.TABLE_CAPTION;
  }

  @Override
  public FreezeServiceAsync getModuleService() {
    return FreezeService.Locator.getService();
  }

  /** 单据类型 */
  public Map<String, String> getBillType() {
    String billTypeString = getModuleContext().get(FreezeService.KEY_BILLTYPES);
    return CollectionUtil.toMap(billTypeString);
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = FreezeSearchPage.START_NODE;

      if (FreezeSearchPage.START_NODE.equals(start))
        showContentPage(FreezeSearchPage.getInstance(), urlParams);
      else if (FreezeCreatePage.START_NODE.equals(start))
        showContentPage(FreezeCreatePage.getInstance(), urlParams);
      else if (FreezeViewPage.START_NODE.equals(start))
        showContentPage(FreezeViewPage.getInstance(), urlParams);
      else if (FreezeLogPage.START_NODE.equals(start))
        showContentPage(FreezeLogPage.getInstance(), urlParams);
    } catch (Exception e) {
      String msg = FreezeMessages.M.errorWhenShowPage();
      RMsgBox.showError(msg, e);
    }
  }

  /**
   * 返回打印模板路径
   * 
   * @return
   */
  public String getPrintTemplate() {
    return FreezePermDef.PRINT_PATH;
  }

  private RSearchBox searchBox;

  @Override
  protected void onCreate() {
    super.onCreate();
    getTitleBar().add(new RToolbarSeparator(), RToolbar.ALIGN_RIGHT);

    searchBox = new RSearchBox();
    searchBox.addSearchHandler(new SearchBoxHandler());
    getTitleBar().add(searchBox, RToolbar.ALIGN_RIGHT);
  }

  public void showSearchBox(boolean show) {
    if (show == false && searchBox != null) {
      getTitleBar().remove(searchBox);
    } else if (show && searchBox != null) {
      getTitleBar().add(searchBox, RToolbar.ALIGN_RIGHT);
    }
  }

  private class SearchBoxHandler implements RSearchHandler {
    @Override
    public void onSearch(RSearchEvent event) {
      if (event.isBack()) {
        try {
          jump(FreezeSearchPage.getInstance().getLastJumpParams());
        } catch (ClientBizException e) {
          RMsgBox.showError(e);
        }
        return;
      }

      if (event.isEmpty()) {
        try {
          jump(FreezeSearchPage.START_NODE);
        } catch (Exception e) {
          RMsgBox.showError(e);
        }
        return;
      }

      doSearch(event.getKeyword());
    }
  }

  private void doSearch(final String keyword) {
    RLoadingDialog.show(FreezeMessages.M.actionDoing(FreezeMessages.M.search()));
    FreezeService.Locator.getService().loadByNumber(keyword, new RBAsyncCallback2<BFreeze>() {
      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        JumpParameters params = new JumpParameters();
        params.setStart(FreezeSearchPage.START_NODE);
        params.getUrlRef().set(FreezeSearchPage.PN_KEYWORD, keyword);
        jump(params);
      }

      @Override
      public void onSuccess(BFreeze result) {
        RLoadingDialog.hide();
        JumpParameters params = new JumpParameters();
        if (result == null) {
          params.setStart(FreezeSearchPage.START_NODE);
          params.getUrlRef().set(FreezeSearchPage.PN_KEYWORD, keyword);
        } else {
          params.setStart(FreezeViewPage.START_NODE);
          params.getUrlRef().set(FreezeViewPage.PN_ENTITY_UUID, result.getUuid());
        }
        jump(params);
      }
    });
  }

  @Override
  public String getPermResource() {
    return FreezePermDef.RESOURCE_FREEZE;
  }

}
