/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： EPFee.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-16 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.client;

import java.math.RoundingMode;
import java.util.List;

import com.hd123.m3.account.gwt.base.client.AccEPBpmModule;
import com.hd123.m3.account.gwt.fee.client.biz.BFee;
import com.hd123.m3.account.gwt.fee.client.rpc.FeeService;
import com.hd123.m3.account.gwt.fee.client.rpc.FeeServiceAsync;
import com.hd123.m3.account.gwt.fee.client.ui.FeeCreatePage;
import com.hd123.m3.account.gwt.fee.client.ui.FeeEditPage;
import com.hd123.m3.account.gwt.fee.client.ui.FeeLogPage;
import com.hd123.m3.account.gwt.fee.client.ui.FeeSearchPage;
import com.hd123.m3.account.gwt.fee.client.ui.FeeViewPage;
import com.hd123.m3.account.gwt.fee.intf.client.dd.PFeeDef;
import com.hd123.m3.account.gwt.fee.intf.client.perm.FeePermDef;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchBox;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchEvent;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchHandler;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 费用单 | 模块入口
 * 
 * @author subinzhu
 * 
 */
public class EPFee extends AccEPBpmModule {
  public static final String OPN_ENTITY = "enitity";
  private List<String> coopModes;
  
  public static EPFee getInstance() {
    return EPFee.getInstance(EPFee.class);
  }

  @Override
  public String getModuleCaption() {
    return PFeeDef.TABLE_CAPTION;
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = FeeSearchPage.START_NODE;

      if (FeeSearchPage.START_NODE.equals(start))
        showContentPage(FeeSearchPage.getInstance(), urlParams);
      else if (FeeCreatePage.START_NODE.equals(start))
        showContentPage(FeeCreatePage.getInstance(), urlParams);
      else if (FeeViewPage.START_NODE.equals(start))
        showContentPage(FeeViewPage.getInstance(), urlParams);
      else if (FeeEditPage.START_NODE.equals(start))
        showContentPage(FeeEditPage.getInstance(), urlParams);
      else if (FeeLogPage.START_NODE.equals(start))
        showContentPage(FeeLogPage.getInstance(), urlParams);
    } catch (Exception e) {
      String msg = CommonsMessages.M.processError(CommonsMessages.M.show());
      RMsgBox.showError(msg, e);
    }
  }

  public FeeServiceAsync getModuleService() {
    return FeeService.Locator.getService();
  }

  /**
   * 返回打印模板路径
   * 
   * @return
   */
  public String getPrintTemplate() {
    return FeePermDef.PRINT_PATH;
  }

  private RSearchBox searchBox;

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
    public void onSearch(RSearchEvent event) {
      if (event.isBack()) {
        JumpParameters params = FeeSearchPage.getInstance().getLastParams();
        jump(params);
        return;
      }
      if (event.isEmpty()) {
        JumpParameters params = new JumpParameters(FeeSearchPage.START_NODE);
        jump(params);
        return;
      }
      doSearch(event.getKeyword());
    }
  }

  private void doSearch(final String keyword) {
    RLoadingDialog.show("正在搜索...");

    FeeService.Locator.getService().loadByNumber(keyword, new RBAsyncCallback2<BFee>() {
      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        JumpParameters params = new JumpParameters();
        params.setStart(FeeSearchPage.START_NODE);
        params.getUrlRef().set(FeeSearchPage.PN_KEYWORD, keyword);
        jump(params);
      }

      @Override
      public void onSuccess(BFee result) {
        RLoadingDialog.hide();
        JumpParameters params = new JumpParameters();
        if (result == null) {
          params.setStart(FeeSearchPage.START_NODE);
          params.getUrlRef().set(FeeSearchPage.PN_KEYWORD, keyword);
        } else {
          params.setStart(FeeViewPage.START_NODE);
          params.getUrlRef().set(FeeViewPage.PN_UUID, result.getUuid());
        }
        jump(params);
      }
    });
  }
  
  public List<String> getCoopModes() {
    if (coopModes == null) {
      coopModes = CollectionUtil.toList(getModuleContext().get(
          FeeService.KEY_COOPMODES));
    }
    return coopModes;
  }

  @Override
  public String getPermResource() {
    return FeePermDef.RESOURCE_FEE;
  }

  /** 获取选项配置小数精度 */
  public int getScale() {
    return Integer.valueOf(getModuleContext().get(FeeService.SCALE));
  }

  /** 获取选项配置舍入算法 */
  public RoundingMode getRoundingMode() {
    return RoundingMode.valueOf(getModuleContext().get(FeeService.ROUNDING_MODE));
  }
}
