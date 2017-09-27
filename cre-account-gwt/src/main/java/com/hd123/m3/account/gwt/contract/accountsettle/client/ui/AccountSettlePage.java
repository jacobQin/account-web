/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountSettlePage.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-10 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.contract.accountsettle.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HighlightEvent;
import com.google.gwt.event.logical.shared.HighlightHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.contract.accountsettle.client.AccountSettleMessages;
import com.hd123.m3.account.gwt.contract.accountsettle.client.EPAccountSettle;
import com.hd123.m3.account.gwt.contract.accountsettle.client.biz.BAccountSettle;
import com.hd123.m3.account.gwt.contract.accountsettle.client.rpc.AccountSettleFilter;
import com.hd123.m3.account.gwt.contract.accountsettle.client.ui.gadget.AccExecuteResultPanel;
import com.hd123.m3.account.gwt.contract.accountsettle.client.ui.gadget.AccSelectAccSettlePanel;
import com.hd123.m3.account.gwt.contract.accountsettle.client.ui.gadget.AccSelectConfigPanel;
import com.hd123.m3.account.gwt.contract.accountsettle.client.ui.gadget.AccSelectContractPanel;
import com.hd123.m3.account.gwt.contract.accountsettle.intf.client.AccountSettleUrlParams;
import com.hd123.m3.account.gwt.contract.accountsettle.intf.client.perm.AccountSettlePermDef;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
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
 * 出账页面
 * 
 * @author huangjunxian
 * 
 */
public class AccountSettlePage extends BaseContentPage implements
    AccountSettleUrlParams.AccountSettle {

  private static AccountSettlePage instance = null;

  public static AccountSettlePage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new AccountSettlePage();
    return instance;
  }

  public AccountSettlePage() throws ClientBizException {
    super();
    try {
      filter = new AccountSettleFilter();
      drawToolbar();
      drawSelf();
    } catch (Exception e) {
      throw new ClientBizException(CommonsMessages.M.cannotCreatePage("AccountSettlePage"), e);
    }
  }

  private EPAccountSettle ep = EPAccountSettle.getInstance();

  private RAction cancelAction;
  private AccountSettleFilter filter;
  private RRoadmapPanel roadMapPanel;
  private AccSelectContractPanel contractPanel;
  private AccSelectAccSettlePanel accSettlePanel;
  private AccSelectConfigPanel configPanel;
  private AccExecuteResultPanel resultPanel;
  private List<BAccountSettle> records;
  private String statementProcesserKey;
  private String permGroupId;
  private String permGroupTitle;

  private List<HandlerRegistration> roadMapNextButtonHandlerRegs = new ArrayList<HandlerRegistration>();

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

    contractPanel = new AccSelectContractPanel(this);
    roadMapPanel.addStep(AccountSettleMessages.M.select_contract(), RRoadmapStepState.HIGHLIGHTED,
        contractPanel);
    accSettlePanel = new AccSelectAccSettlePanel(this);
    roadMapPanel.addStep(AccountSettleMessages.M.select_accSettle(), accSettlePanel);
    configPanel = new AccSelectConfigPanel(this);
    roadMapPanel.addStep(AccountSettleMessages.M.config(), configPanel);
    resultPanel = new AccExecuteResultPanel(this);
    roadMapPanel.addStep(AccountSettleMessages.M.finish(), resultPanel);

    roadMapPanel.getRoadmap().addHighlightHandler(new HighlightHandler<RRoadmapStep>() {

      @Override
      public void onHighlight(HighlightEvent<RRoadmapStep> event) {
        if (AccountSettleMessages.M.finish().equals(event.getHighlighted().getCaption())) {
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

  private boolean checkIn() {
    if (ep.isPermitted(AccountSettlePermDef.ACCSETTLE) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  private void refresh() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(ep.getModuleCaption());
    ep.getTitleBar().appendAttributeText(AccountSettleMessages.M.select_contract());

    roadMapPanel.reset();
    roadMapPanel.validate();
    if (filter.getAccountUnitUuid() == null && ep.getUserStores().size() == 1) {
      filter.setAccountUnitUuid(ep.getUserStores().get(0).getUuid());
      contractPanel.setAccountUnit(new BUCN(ep.getUserStores().get(0)));
    }
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    contractPanel.clear();
    accSettlePanel.clear();
    resultPanel.clear();
  }

  public AccountSettleFilter getFilter() {
    return filter;
  }

  public RRoadmapPanel getRoadMapPanel() {
    return roadMapPanel;
  }

  public void setRoadMapPanel(RRoadmapPanel roadMapPanel) {
    this.roadMapPanel = roadMapPanel;
  }

  public List<BAccountSettle> getRecords() {
    return records;
  }

  public void setRecords(List<BAccountSettle> records) {
    this.records = records;
  }

  /**
   * 账单流程
   */
  public String getStatementProcesserKey() {
    return statementProcesserKey;
  }

  public void setStatementProcesserKey(String statementProcesserKey) {
    this.statementProcesserKey = statementProcesserKey;
  }

  public void doRefresh() {
    accSettlePanel.refresh();
  }

  /**
   * 授权组
   */
  public String getPermGroupId() {
    return permGroupId;
  }

  public void setPermGroupId(String permGroupId) {
    this.permGroupId = permGroupId;
  }

  public String getPermGroupTitle() {
    return permGroupTitle;
  }

  public void setPermGroupTitle(String permGroupTitle) {
    this.permGroupTitle = permGroupTitle;
  }

  public List<HandlerRegistration> getRoadMapNextButtonHandlerRegs() {
    return roadMapNextButtonHandlerRegs;
  }

  public void setRoadMapNextButtonHandlerRegs(List<HandlerRegistration> roadMapNextButtonHandlerRegs) {
    this.roadMapNextButtonHandlerRegs = roadMapNextButtonHandlerRegs;
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

  public void removeRoadMapNextButtonHandlers() {
    for (int i = roadMapNextButtonHandlerRegs.size() - 1; i >= 0; i--) {
      HandlerRegistration handlerReg = roadMapNextButtonHandlerRegs.get(i);
      handlerReg.removeHandler();
      roadMapNextButtonHandlerRegs.remove(i);
    }
  }

  /**************************************************/
  private class Handler_cancelAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent arg0) {
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
