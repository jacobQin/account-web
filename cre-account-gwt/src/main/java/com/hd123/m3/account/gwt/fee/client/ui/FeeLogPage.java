/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： FeeLogPage.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-16 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.ia.widget.audit.client.AuditQueryParams;
import com.hd123.ia.widget.audit.client.BKeyword;
import com.hd123.m3.account.gwt.base.client.LoggerConstant;
import com.hd123.m3.account.gwt.fee.client.EPFee;
import com.hd123.m3.account.gwt.fee.client.FeeMessages;
import com.hd123.m3.account.gwt.fee.client.biz.BFee;
import com.hd123.m3.account.gwt.fee.client.rpc.FeeLoader;
import com.hd123.m3.account.gwt.fee.intf.client.FeeUrlParams.Log;
import com.hd123.m3.account.gwt.fee.intf.client.perm.FeePermDef;
import com.hd123.m3.commons.gwt.base.client.log.M3AuditEventGrid;
import com.hd123.rumba.gwt.flecs.client.DefaultOperator;
import com.hd123.rumba.gwt.flecs.client.FlecsQueryDef.Condition;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.panel.RCardPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author subinzhu
 * 
 */
public class FeeLogPage extends BaseContentPage implements Log {
  private static FeeLogPage instance = null;
  private EPFee ep = EPFee.getInstance();

  public static FeeLogPage getInstance() {
    if (instance == null)
      instance = new FeeLogPage();
    return instance;
  }

  private FeeLoader entityLoader;
  private BFee entity;
  private RAction backAction;
  private HTML html;
  private M3AuditEventGrid logGadget;

  public FeeLogPage() {
    super();
    entityLoader = new FeeLoader();
    drawToolbar();
    drawSelf();
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    // Do Nothing
  }

  private void drawToolbar() {
    backAction = new RAction(RActionFacade.BACK_TO_VIEW, new Handler_backAction());
    RToolbarButton backButton = new RToolbarButton(backAction);
    getToolbar().add(backButton);
  }

  private void drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    initWidget(panel);

    Widget w = drawGeneral();
    panel.add(w);

    logGadget = new M3AuditEventGrid();
    panel.add(logGadget);
  }

  private Widget drawGeneral() {
    html = new HTML();
    html.setStyleName(RTextStyles.STYLE_OVERVIEW_TITLE);

    RCardPanel cardPanel = new RCardPanel(html);
    cardPanel.setContentSpacing(10);
    cardPanel.setWidth("100%");

    return cardPanel;
  }

  public void onShow(final JumpParameters params) {
    super.onShow(params);
    if (checkIn() == false)
      return;

    entityLoader.decodeParams(params, new Command() {
      public void execute() {
        entity = entityLoader.getEntity();

        refreshTitle();
        refresh();
      }
    });
  }

  private boolean checkIn() {
    if (ep.isProcessMode())
      return true;
    if (ep.isPermitted(FeePermDef.READ) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(ep.getModuleCaption());
    if (entity != null) {
      ep.getTitleBar().appendAttributeText(entity.getBillNumber());
      ep.getTitleBar().appendAttributeText(FeeMessages.M.log(), "-");
    }
  }

  private void refresh() {
    html.setText(entity.getBillNumber());

    logGadget.setParams(getConditions());
    logGadget.getPagingGrid().refresh();
  }

  private List<Condition> getConditions() {
    List<Condition> conditions = new ArrayList<Condition>();
    Condition condition = new Condition();
    condition.setFieldName(AuditQueryParams.FIELD_KEY);
    condition.setOperator(DefaultOperator.EQUALS);
    ArrayList<BKeyword> keywords = new ArrayList<BKeyword>();

    if (entity != null) {
      BKeyword keyword = new BKeyword(LoggerConstant.INDEX_ZORE, LoggerConstant.KEY_UUID,
          entity.getUuid());
      keywords.add(keyword);
    }

    condition.setOperand(keywords);
    conditions.add(condition);

    return conditions;
  }

  /** ************************ 以下是私有类定义 *************************** */
  private class Handler_backAction implements ClickHandler {
    public void onClick(ClickEvent event) {
      ep.jumpToViewPage(entity.getUuid());
    }
  }
}
