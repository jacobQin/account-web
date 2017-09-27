/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentNoticeBatchPage.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-28 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HighlightEvent;
import com.google.gwt.event.logical.shared.HighlightHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.paymentnotice.client.EPPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.PaymentNoticeMessages;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.QBatchStatement;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.gadget.batch.PNExecuteResultGadget;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.gadget.batch.PNSelectCounterpartGadget;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.gadget.batch.PNSelectRulesGadget;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.PaymentNoticeUrlParams;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.perm.PaymentNoticePermDef;
import com.hd123.m3.commons.gwt.base.client.biz.BPermedStandardEntity;
import com.hd123.m3.commons.gwt.base.client.biz.HasPerm;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.util.client.history.RHistory;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.button.RButton;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.navigate.roadmap.RRoadmapPanel;
import com.hd123.rumba.gwt.widget2.client.navigate.roadmap.RRoadmapStep;
import com.hd123.rumba.gwt.widget2.client.navigate.roadmap.RRoadmapStepState;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * 收付款通知单批量生成页面
 * 
 * @author huangjunxian
 * 
 */
public class PaymentNoticeBatchPage extends BaseContentPage implements PaymentNoticeUrlParams.Batch {

  private static PaymentNoticeBatchPage instance = null;

  public static PaymentNoticeBatchPage getInstance() {
    if (instance == null)
      instance = new PaymentNoticeBatchPage();
    return instance;
  }

  public PaymentNoticeBatchPage() {
    super();
    drawToolbar();
    drawSelf();
  }

  private EPPaymentNotice ep = EPPaymentNotice.getInstance();

  private List<HandlerRegistration> roadMapNextButtonHandlerRegs = new ArrayList<HandlerRegistration>();
  private List<QBatchStatement> counterparts;
  private boolean splitBill = false;
  private HasPerm perm = new BPermedStandardEntity();
  private String proDefinition;

  private RRoadmapPanel roadMapPanel;
  private PNSelectCounterpartGadget selectCounterpartPanel;
  private PNSelectRulesGadget selectRulesPanel;
  private PNExecuteResultGadget resultPanel;
  private RAction cancelAction;

  private void drawToolbar() {
    cancelAction = new RAction(RActionFacade.CANCEL, new Handler_cancelAction());
    RToolbarButton cancelButton = new RToolbarButton(cancelAction);
    getToolbar().add(cancelButton);
  }

  private void drawSelf() {
    RVerticalPanel root = new RVerticalPanel();
    root.setWidth("100%");
    root.setSpacing(8);
    root.setHeight("300");
    initWidget(root);

    Widget w = drawGadget();
    root.add(w);
  }

  private Widget drawGadget() {
    roadMapPanel = new RRoadmapPanel();
    roadMapPanel.setAnimationEnabled(false);

    selectCounterpartPanel = new PNSelectCounterpartGadget(this);
    roadMapPanel.addStep(
        PaymentNoticeMessages.M.batch_select()
            + ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()),
        RRoadmapStepState.HIGHLIGHTED, selectCounterpartPanel);

    selectRulesPanel = new PNSelectRulesGadget(this);
    roadMapPanel.addStep(PaymentNoticeMessages.M.batch_selectRules(), selectRulesPanel);

    resultPanel = new PNExecuteResultGadget(this);
    roadMapPanel.addStep(PaymentNoticeMessages.M.batch_finish(), resultPanel);

    roadMapPanel.getRoadmap().addHighlightHandler(new HighlightHandler<RRoadmapStep>() {

      @Override
      public void onHighlight(HighlightEvent<RRoadmapStep> event) {
        if (PaymentNoticeMessages.M.batch_finish().equals(event.getHighlighted().getCaption())) {
          resultPanel.disablePreButton();
        }
      }
    });
    return roadMapPanel;
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (checkIn() == false)
      return;

    refresh();
  }

  private void refresh() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(PaymentNoticeMessages.M.batch_caption(ep.getModuleCaption()));
    ep.getTitleBar().appendAttributeText(
        PaymentNoticeMessages.M.batch_select()
            + ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));

    selectCounterpartPanel.clearFilter();
    setSplitBill(false);
    roadMapPanel.reset();
    roadMapPanel.validate();
  }

  private boolean checkIn() {
    if (ep.isPermitted(PaymentNoticePermDef.CREATE) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
  }

  public void removeRoadMapNextButtonHandlers() {
    for (int i = roadMapNextButtonHandlerRegs.size() - 1; i >= 0; i--) {
      HandlerRegistration handlerReg = roadMapNextButtonHandlerRegs.get(i);
      handlerReg.removeHandler();
      roadMapNextButtonHandlerRegs.remove(i);
    }
  }

  public RRoadmapPanel getRoadMapPanel() {
    return roadMapPanel;
  }

  public void setRoadMapPanel(RRoadmapPanel roadMapPanel) {
    this.roadMapPanel = roadMapPanel;
  }

  public List<HandlerRegistration> getRoadMapNextButtonHandlerRegs() {
    return roadMapNextButtonHandlerRegs;
  }

  public void setRoadMapNextButtonHandlerRegs(List<HandlerRegistration> roadMapNextButtonHandlerRegs) {
    this.roadMapNextButtonHandlerRegs = roadMapNextButtonHandlerRegs;
  }

  public List<QBatchStatement> getCounterparts() {
    return counterparts;
  }

  public void setCounterparts(List<QBatchStatement> counterparts) {
    this.counterparts = counterparts;
  }

  public boolean isSplitBill() {
    return splitBill;
  }

  public void setSplitBill(boolean splitBill) {
    this.splitBill = splitBill;
  }

  public HasPerm getPerm() {
    return perm;
  }

  public void setPerm(HasPerm perm) {
    this.perm = perm;
  }

  public String getProDefinition() {
    return proDefinition;
  }

  public void setProDefinition(String proDefinition) {
    this.proDefinition = proDefinition;
  }

  public static RButton getRoadMapPreButton(RRoadmapPanel roadmapPanel) {
    if (roadmapPanel == null)
      return null;

    Iterator<Widget> iter = roadmapPanel.getRoadmap().getToolbar().iterator();
    if (iter.hasNext()) {
      Widget w = iter.next();
      if (w instanceof RButton)
        return (RButton) w;
    }

    return null;
  }

  public static RButton getRoadMapNextButton(RRoadmapPanel roadmapPanel) {
    if (roadmapPanel == null)
      return null;

    Iterator<Widget> iter = roadmapPanel.getRoadmap().getToolbar().iterator();
    if (iter.hasNext()) {
      iter.next();
      if (iter.hasNext()) {
        Widget w = iter.next();
        if (w instanceof RButton)
          return (RButton) w;
      }
    }

    return null;
  }

  /*********************/
  private class Handler_cancelAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RMsgBox.showConfirm(CommonsMessages.M.confirmCancel(""), new RMsgBox.ConfirmCallback() {

        @Override
        public void onClosed(boolean confirmed) {
          if (confirmed) {
            RHistory.back();
          }
        }
      });
    }

  }
}
