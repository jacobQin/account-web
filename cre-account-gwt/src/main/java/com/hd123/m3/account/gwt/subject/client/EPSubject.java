/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 *
 * 项目名： M3
 * 文件名： EPSubject.java
 * 模块说明：
 * 修改历史：
 * 2013-99 - cRazy - 创建。
 */
package com.hd123.m3.account.gwt.subject.client;

import java.util.List;

import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectUsage;
import com.hd123.m3.account.gwt.subject.client.biz.BSubject;
import com.hd123.m3.account.gwt.subject.client.rpc.SubjectService;
import com.hd123.m3.account.gwt.subject.client.ui.SubjectCreatePage;
import com.hd123.m3.account.gwt.subject.client.ui.SubjectEditPage;
import com.hd123.m3.account.gwt.subject.client.ui.SubjectLogPage;
import com.hd123.m3.account.gwt.subject.client.ui.SubjectSearchPage;
import com.hd123.m3.account.gwt.subject.client.ui.SubjectViewPage;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchBox;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchEvent;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchHandler;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.ModuleServiceAsync;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.TitleEntryPoint;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author cRazy
 * 
 */
public class EPSubject extends TitleEntryPoint {

  public static final String OPN_ENTITY = "opn_entity";
  public static final String KEY_CUSTOM_TYPE="account/subject/customType";

  public static EPSubject getInstance() {
    return EPSubject.getInstance(EPSubject.class);
  }

  @Override
  protected void dispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = SubjectSearchPage.START_NODE;
      if (SubjectSearchPage.START_NODE.equals(start))
        showContentPage(SubjectSearchPage.getInstance(), urlParams);
      else if (SubjectCreatePage.START_NODE.equals(start))
        showContentPage(SubjectCreatePage.getInstance(), urlParams);
      else if (SubjectEditPage.START_NODE.equals(start))
        showContentPage(SubjectEditPage.getInstance(), urlParams);
      else if (SubjectViewPage.START_NODE.equals(start))
        showContentPage(SubjectViewPage.getInstance(), urlParams);
      else if (SubjectLogPage.START_NODE.equals(start))
        showContentPage(SubjectLogPage.getInstance(), urlParams);
    } catch (ClientBizException e) {
      String msg = CommonsMessages.M.processError(CommonsMessages.M.show());
      RMsgBox.showError(msg, e);
    }
  }

  @Override
  public String getModuleCaption() {
    return SubjectUrlParams.MODULE_CAPTION;
  }

  protected ModuleServiceAsync getModuleService() {
    return SubjectService.Locator.getService();
  }

  private List<BSubjectUsage> usages = null;

  /** 科目用途 */
  public List<BSubjectUsage> getUsages() {
    GWTUtil.enableSynchronousRPC();
    if (usages == null) {
      SubjectService.Locator.getService().getUsages(new RBAsyncCallback2<List<BSubjectUsage>>() {

        @Override
        public void onException(Throwable caught) {
          RLoadingDialog.hide();
          String msg = SubjectMessages.M.actionFailed(SubjectMessages.M.getUsages(),
              getModuleCaption());
          RMsgBox.showError(msg, caught);
        }

        @Override
        public void onSuccess(List<BSubjectUsage> result) {
          usages = result;
        }
      });
    }
    return usages;
  }

  public BSubjectUsage getUsage(String usageCode) {
    if (usageCode == null)
      return null;
    List<BSubjectUsage> list = getUsages();
    for (BSubjectUsage u : list) {
      if (usageCode.equals(u.getCode()))
        return u;
    }

    return null;
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    getTitleBar().add(new RToolbarSeparator(), RToolbar.ALIGN_RIGHT);

    searchBox = new RSearchBox();
    searchBox.addSearchHandler(new SearchBoxHandler());
    getTitleBar().add(searchBox, RToolbar.ALIGN_RIGHT);
  }

  private RSearchBox searchBox;

  private class SearchBoxHandler implements RSearchHandler {

    @Override
    public void onSearch(RSearchEvent event) {
      if (event.isBack()) {
        try {
          JumpParameters params = SubjectSearchPage.getInstance().getParams();
          params.getMessages().clear();
          jump(params);
        } catch (ClientBizException e) {
          RMsgBox.showError(e);
        }
        return;
      }
      if (event.isEmpty()) {
        JumpParameters params = new JumpParameters(SubjectSearchPage.START_NODE);
        jump(params);
      } else {
        doSearch(event.getKeyword());
      }
    }
  }

  private void doSearch(final String keyword) {
    RLoadingDialog.show(SubjectMessages.M.actionDoing(SubjectMessages.M.search()));

    SubjectService.Locator.getService().loadByCode(keyword, new RBAsyncCallback2<BSubject>() {

      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = SubjectMessages.M.actionFailed(SubjectMessages.M.search(), getModuleCaption());
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(BSubject result) {
        RLoadingDialog.hide();
        JumpParameters params = new JumpParameters();
        if (result == null) {
          params.setStart(SubjectSearchPage.START_NODE);
          params.getUrlRef().set(SubjectSearchPage.PN_KEYWORD, keyword);
        } else {
          params.setStart(SubjectViewPage.START_NODE);
          params.getUrlRef().set(SubjectViewPage.PN_ENTITY_UUID, result.getUuid());
          params.getExtend().put(OPN_ENTITY, result);
        }
        jump(params);
      }
    });
  }
}
