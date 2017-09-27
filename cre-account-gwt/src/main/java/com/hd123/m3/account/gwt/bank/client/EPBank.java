/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 *
 * 项目名： M3
 * 文件名： EPBank.java
 * 模块说明：
 * 修改历史：
 * 2013-817 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.bank.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.hd123.m3.account.gwt.bank.client.biz.BBank;
import com.hd123.m3.account.gwt.bank.client.rpc.BankService;
import com.hd123.m3.account.gwt.bank.client.ui.BankCreatePage;
import com.hd123.m3.account.gwt.bank.client.ui.BankEditPage;
import com.hd123.m3.account.gwt.bank.client.ui.BankLogPage;
import com.hd123.m3.account.gwt.bank.client.ui.BankSearchPage;
import com.hd123.m3.account.gwt.bank.client.ui.BankViewPage;
import com.hd123.m3.account.gwt.bank.intf.client.BankUrlParams;
import com.hd123.m3.account.gwt.base.client.LoggerConstant;
import com.hd123.m3.commons.gwt.base.client.M3Sessions;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.base.client.util.BUCNUtils;
import com.hd123.rumba.commons.gwt.entity.client.HasUCN;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.audit.client.BAuditLogger;
import com.hd123.rumba.webframe.gwt.audit.client.BKeywords;
import com.hd123.rumba.webframe.gwt.base.client.ModuleServiceAsync;
import com.hd123.rumba.webframe.gwt.entrypoint.client.TitleEntryPoint;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author chenrizhang
 * 
 */
public class EPBank extends TitleEntryPoint {
  private List<HasUCN> userStores;

  public static final String OPN_ENTITY = "open_entity";

  public static EPBank getInstance() {
    return EPBank.getInstance(EPBank.class);
  }

  @Override
  protected void dispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = BankSearchPage.START_NODE;
      if (BankSearchPage.START_NODE.equals(start))
        showContentPage(BankSearchPage.getInstance(), urlParams);
      else if (BankCreatePage.START_NODE.equals(start))
        showContentPage(BankCreatePage.getInstance(), urlParams);
      else if (BankEditPage.START_NODE.equals(start))
        showContentPage(BankEditPage.getInstance(), urlParams);
      else if (BankViewPage.START_NODE.equals(start))
        showContentPage(BankViewPage.getInstance(), urlParams);
      else if (BankLogPage.START_NODE.equals(start))
        showContentPage(BankLogPage.getInstance(), urlParams);
    } catch (ClientBizException e) {
      String msg = CommonsMessages.M.processError(CommonsMessages.M.show());
      RMsgBox.showError(msg, e);
    }
  }

  @Override
  public String getModuleCaption() {
    return BankUrlParams.MODULE_CAPTION;
  }

  @Override
  public ModuleServiceAsync getModuleService() {
    return BankService.Locator.getService();
  }

  private RAction searchAction;

  @Override
  protected void onCreate() {
    super.onCreate();
    auditLogger = BAuditLogger.getModuleLogger();
    getTitleBar().add(new RToolbarSeparator(), RToolbar.ALIGN_RIGHT);

    searchAction = new RAction(RActionFacade.SEARCH);
    searchAction.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        JumpParameters params = new JumpParameters(BankSearchPage.START_NODE);
        jump(params);
      }
    });
    getTitleBar().add(new RToolbarButton(searchAction), RToolbar.ALIGN_RIGHT);
  }

  private BAuditLogger auditLogger;

  public void log(String action) {
    auditLogger.log(action, new BKeywords());
  }

  public void log(String action, BBank entity) {
    BKeywords keywords = new BKeywords();
    if (entity != null) {
      keywords.put(LoggerConstant.INDEX_ZORE, LoggerConstant.KEY_UUID, entity.getUuid());
      keywords.put(LoggerConstant.INDEX_ONE, LoggerConstant.KEY_CODE, entity.getCode());
    }
    auditLogger.log(action, keywords);
  }

  /** 当前用户的项目 */
  public List<HasUCN> getUserStores() {
    if (userStores != null) {
      return userStores;
    }
    String str = getModuleContext().get(M3Sessions.KEY_PERM_STORES);
    if (str == null) {
      userStores = new ArrayList<HasUCN>();
    } else {
      userStores = BUCNUtils.fromJson(str);
    }

    return userStores;
  }
}
