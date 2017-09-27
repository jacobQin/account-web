/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	EPInternalFee.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.client;

import java.math.RoundingMode;

import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFee;
import com.hd123.m3.account.gwt.internalfee.client.rpc.InternalFeeService;
import com.hd123.m3.account.gwt.internalfee.client.rpc.InternalFeeServiceAsync;
import com.hd123.m3.account.gwt.internalfee.client.ui.InternalFeeCreatePage;
import com.hd123.m3.account.gwt.internalfee.client.ui.InternalFeeEditPage;
import com.hd123.m3.account.gwt.internalfee.client.ui.InternalFeeLogPage;
import com.hd123.m3.account.gwt.internalfee.client.ui.InternalFeeSearchPage;
import com.hd123.m3.account.gwt.internalfee.client.ui.InternalFeeViewPage;
import com.hd123.m3.account.gwt.internalfee.intf.client.dd.PInternalFeeDef;
import com.hd123.m3.account.gwt.internalfee.intf.client.perm.InternalFeePermDef;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.bpm.client.EPBpmModule;
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
 * 内部费用单 | 模块入口
 * 
 * @author liuguilin
 * 
 */

public class EPInternalFee extends EPBpmModule {

  public static final String OPN_ENTITY = "enitity";
  public static final String KEY_NAVI_COOKIENAME = InternalFeeViewPage.class.getName();

  public static EPInternalFee getInstance() {
    return EPInternalFee.getInstance(EPInternalFee.class);
  }

  @Override
  public String getModuleCaption() {
    return PInternalFeeDef.TABLE_CAPTION;
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = InternalFeeSearchPage.START_NODE;

      if (InternalFeeSearchPage.START_NODE.equals(start))
        showContentPage(InternalFeeSearchPage.getInstance(), urlParams);
      else if (InternalFeeCreatePage.START_NODE.equals(start))
        showContentPage(InternalFeeCreatePage.getInstance(), urlParams);
      else if (InternalFeeViewPage.START_NODE.equals(start))
        showContentPage(InternalFeeViewPage.getInstance(), urlParams);
      else if (InternalFeeEditPage.START_NODE.equals(start))
        showContentPage(InternalFeeEditPage.getInstance(), urlParams);
      else if (InternalFeeLogPage.START_NODE.equals(start))
        showContentPage(InternalFeeLogPage.getInstance(), urlParams);
    } catch (Exception e) {
      String msg = CommonsMessages.M.processError(CommonsMessages.M.show());
      RMsgBox.showError(msg, e);
    }
  }

  @Override
  public InternalFeeServiceAsync getModuleService() {
    return InternalFeeService.Locator.getService();
  }

  @Override
  public String getPermResource() {
    return InternalFeePermDef.RESOURCE_INTERNALFEE;
  }

  /**
   * 返回打印模板路径
   * 
   * @return
   */
  public String getPrintTemplate() {
    return InternalFeePermDef.PRINT_PATH;
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
    public void onSearch(RSearchEvent event) {
      if (event.isBack()) {
        JumpParameters params = InternalFeeSearchPage.getInstance().getLastParams();
        jump(params);
        return;
      }
      if (event.isEmpty()) {
        JumpParameters params = new JumpParameters(InternalFeeSearchPage.START_NODE);
        jump(params);
        return;
      }
      doSearch(event.getKeyword());
    }
  }

  private void doSearch(final String keyword) {
    RLoadingDialog.show("正在搜索...");

    InternalFeeService.Locator.getService().loadByNumber(keyword,
        new RBAsyncCallback2<BInternalFee>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            params.setStart(InternalFeeSearchPage.START_NODE);
            params.getUrlRef().set(InternalFeeSearchPage.PN_KEYWORD, keyword);
            jump(params);
          }

          @Override
          public void onSuccess(BInternalFee result) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            if (result == null) {
              params.setStart(InternalFeeSearchPage.START_NODE);
              params.getUrlRef().set(InternalFeeSearchPage.PN_KEYWORD, keyword);
            } else {
              params.setStart(InternalFeeViewPage.START_NODE);
              params.getUrlRef().set(InternalFeeViewPage.PN_UUID, result.getUuid());
            }
            jump(params);
          }
        });
  }

  /** 获取选项配置小数精度 */
  public int getScale() {
    return Integer.valueOf(getModuleContext().get(InternalFeeService.SCALE));
  }

  /** 获取选项配置舍入算法 */
  public RoundingMode getRoundingMode() {
    return RoundingMode.valueOf(getModuleContext().get(InternalFeeService.ROUNDING_MODE));
  }
}
