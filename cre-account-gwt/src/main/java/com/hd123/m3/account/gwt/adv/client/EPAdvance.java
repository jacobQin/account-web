/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	EPAdvance.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.adv.client;

import java.util.Map;

import com.hd123.m3.account.gwt.adv.client.filter.AdvanceFilter;
import com.hd123.m3.account.gwt.adv.client.rpc.AdvanceService;
import com.hd123.m3.account.gwt.adv.client.ui.AdvanceLogPage;
import com.hd123.m3.account.gwt.adv.client.ui.AdvanceSearchPage;
import com.hd123.m3.account.gwt.adv.intf.client.dd.PAdvanceDef;
import com.hd123.m3.account.gwt.base.client.AccEPTitleModule;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchBox;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchEvent;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchHandler;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.ModuleServiceAsync;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author zhuhairui
 * 
 */
public class EPAdvance extends AccEPTitleModule {

  public static EPAdvance getInstance() {
    return EPAdvance.getInstance(EPAdvance.class);
  }

  @Override
  protected ModuleServiceAsync getModuleService() {
    return AdvanceService.Locator.getService();
  }

  @Override
  public String getModuleCaption() {
    return PAdvanceDef.TABLE_CAPTION;
  }

  public static AdvanceFilter getFilter() {
    if (filter == null)
      filter = new AdvanceFilter();
    return filter;
  }

  /** 单据类型 */
  public Map<String, String> getBillType() {
    String billTypeString = getModuleContext().get(AdvanceService.KEY_BILLTYPES);
    return CollectionUtil.toMap(billTypeString);
  }

  @Override
  protected void dispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = AdvanceSearchPage.START_NODE;

      if (AdvanceSearchPage.START_NODE.equals(start))
        showContentPage(AdvanceSearchPage.getInstance(), urlParams);
      else if (AdvanceLogPage.START_NODE.equals(start))
        showContentPage(AdvanceLogPage.getInstance(), urlParams);
    } catch (Exception e) {
      String msg = "显示页面时发生错误。";
      RMsgBox.showError(msg, e);
    }
  }

  @Override
  protected void onCreate() {
    super.onCreate();

    getTitleBar().add(new RToolbarSeparator(), RToolbar.ALIGN_RIGHT);

    searchBox = new RSearchBox();
    searchBox.addSearchHandler(new SearchBoxHandler());
    getTitleBar().add(searchBox, RToolbar.ALIGN_RIGHT);
  }

  private class SearchBoxHandler implements RSearchHandler {
    public void onSearch(RSearchEvent event) {
      if (event.isEmpty()) {
        JumpParameters params = new JumpParameters(AdvanceSearchPage.START_NODE);
        jump(params);
        return;
      }
      doSearch(event.getKeyword());
    }
  }

  private void doSearch(final String keyword) {
    JumpParameters params = new JumpParameters(AdvanceSearchPage.START_NODE);
    params.getUrlRef().set(AdvanceSearchPage.KEY_COUNTERPARTUNIT_LIKE, keyword);
    jump(params);
    return;
  }

  private static AdvanceFilter filter;
  private RSearchBox searchBox;
}
