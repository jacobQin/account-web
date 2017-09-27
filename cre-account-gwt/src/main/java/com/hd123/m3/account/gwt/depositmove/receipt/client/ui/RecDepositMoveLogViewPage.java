/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	RecDepositMoveLogViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.receipt.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.ia.widget.audit.client.AuditQueryParams;
import com.hd123.ia.widget.audit.client.BKeyword;
import com.hd123.m3.account.gwt.base.client.LoggerConstant;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BDepositMove;
import com.hd123.m3.account.gwt.depositmove.intf.client.DepositMoveMessage;
import com.hd123.m3.account.gwt.depositmove.receipt.client.EPRecDepositMove;
import com.hd123.m3.account.gwt.depositmove.receipt.client.rpc.RecDepositMoveLoader;
import com.hd123.m3.account.gwt.depositmove.receipt.intf.client.RecDepositMoveUrlParams;
import com.hd123.m3.account.gwt.depositmove.receipt.intf.client.RecDepositMoveUrlParams.Log;
import com.hd123.m3.account.gwt.depositmove.receipt.intf.client.perm.RecDepositMovePermDef;
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
 * @author zhuhairui
 * 
 */
public class RecDepositMoveLogViewPage extends BaseContentPage implements Log {
  private static EPRecDepositMove ep = EPRecDepositMove.getInstance();
  private static RecDepositMoveLogViewPage instance = null;

  public static RecDepositMoveLogViewPage getInstance() {
    if (instance == null)
      instance = new RecDepositMoveLogViewPage();
    return instance;
  }

  public RecDepositMoveLogViewPage() {
    super();
    drawToolbar();
    drawSelf();
  }

  private BDepositMove entity;
  private Label cardPanellabel;

  private RAction backAction;
  private M3AuditEventGrid logGadget;

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

    Widget w = drawCardPanel();
    panel.add(w);

    logGadget = new M3AuditEventGrid();
    panel.add(logGadget);
  }

  private Widget drawCardPanel() {
    cardPanellabel = new Label("");
    cardPanellabel.setStyleName(RTextStyles.STYLE_OVERVIEW_TITLE);

    RCardPanel cardPanel = new RCardPanel(cardPanellabel);
    cardPanel.setContentSpacing(10);
    cardPanel.setWidth("100%");
    return cardPanel;
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    RecDepositMoveLoader.decodeParams(params, new Command() {
      public void execute() {
        entity = RecDepositMoveLoader.getEntity();
        if (checkIn() == false)
          return;

        refreshTitle();
        refreshEntity();
      }
    });
  }

  private boolean checkIn() {
    if (ep.isProcessMode())
      return true;
    if (ep.isPermitted(RecDepositMovePermDef.READ) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(RecDepositMoveUrlParams.MODULE_CAPTION);
    if (entity != null) {
      ep.getTitleBar().appendAttributeText(entity.getBillNumber());
      ep.getTitleBar().appendAttributeText(DepositMoveMessage.M.log(), "-");
    }
  }

  private void refreshEntity() {
    cardPanellabel.setText(entity.getBillNumber());

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

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
  }

  private class Handler_backAction implements ClickHandler {
    public void onClick(ClickEvent event) {
      ep.jumpToViewPage(entity.getUuid());
    }
  }
}
