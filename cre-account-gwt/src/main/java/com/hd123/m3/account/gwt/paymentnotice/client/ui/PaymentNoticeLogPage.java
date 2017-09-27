/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentNoticeLogPage.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-19 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.ui;

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
import com.hd123.m3.account.gwt.paymentnotice.client.EPPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.PaymentNoticeMessages;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.rpc.PaymentNoticeLoader;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.PaymentNoticeUrlParams.Log;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.dd.PPaymentNoticeDef;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.perm.PaymentNoticePermDef;
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
public class PaymentNoticeLogPage extends BaseContentPage implements Log {

  public static PaymentNoticeLogPage getInstance() {
    if (instance == null)
      instance = new PaymentNoticeLogPage();
    return instance;
  }

  public PaymentNoticeLogPage() {
    super();
    loader = new PaymentNoticeLoader();
    drawToolbar();
    drawSelf();
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    // do nothing
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);
    if (!checkIn())
      return;

    loader.decoderParams(params, new Command() {

      @Override
      public void execute() {
        entity = loader.getEntity();

        refreshTitle();
        refreshEntity();
      }
    });
  }

  private EPPaymentNotice ep = EPPaymentNotice.getInstance();
  private static PaymentNoticeLogPage instance;
  private PaymentNoticeLoader loader;
  private BPaymentNotice entity;

  private Label cardPanellabel;

  private RAction backAction;
  private M3AuditEventGrid logGadget;

  private void drawToolbar() {
    backAction = new RAction(RActionFacade.BACK_TO_VIEW, new Handler_backAction());
    getToolbar().add(new RToolbarButton(backAction));
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

  private boolean checkIn() {
    if (ep.isProcessMode())
      return true;
    if (ep.isPermitted(PaymentNoticePermDef.READ) == false) {
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
      ep.getTitleBar().appendAttributeText(PaymentNoticeMessages.M.log(), " -");
    }
  }

  private void refreshEntity() {
    cardPanellabel.setText(entity.getBillNumber() + ","
        + PPaymentNoticeDef.bizState.getEnumCaption(entity.getBizState()));

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

  private class Handler_backAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      JumpParameters parameters = new JumpParameters(PaymentNoticeViewPage.START_NODE);
      parameters.getUrlRef().set(PaymentNoticeViewPage.PN_ENTITY_UUID, entity.getUuid());
      ep.jump(parameters);
    }
  }
}
