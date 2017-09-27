/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeCreatePage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月17日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.ui.page;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.common.client.ActionName;
import com.hd123.m3.account.gwt.ivc.exchange.client.EPInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BExchangeType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvToInvType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget.InvoiceExchangeEviToEviAccountEditWidget;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget.InvoiceExchangeEviToEviEditWidget;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget.InvoiceExchangeEviToInvAccountEditWidget;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget.InvoiceExchangeGeneralEditWidget;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget.InvoiceExchangeInvToInvAccountEditWidget;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget.InvoiceExchangeInvToInvEditWidget;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.InvoiceExchangeUrlParams.Create;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.dd.PInvoiceExchangeDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.bpm.client.rpc2.M3BpmModuleService2Agent.Callback;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpm2Message;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmCreatePage2;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstFieldUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.RDateUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 发票交换单|新建页面
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class InvoiceExchangeCreatePage extends BaseBpmCreatePage2<BInvoiceExchange> implements
    Create, RActionHandler {

  private static InvoiceExchangeCreatePage instance = null;

  private RAction saveAndCreateAction;

  private ActionHandler actionHandler;
  private FieldValueChangeHandler changeHandler;
  private InvoiceExchangeGeneralEditWidget generalWidget;
  private RTextArea remarkField;
  
  private RMultiVerticalPanel mainPanel;

  // 票据明细组件
  private InvoiceExchangeEviToEviEditWidget eviToEviEditWidget;
  private InvoiceExchangeInvToInvEditWidget invToInvEditWidget;

  // 账款明细组件
  private InvoiceExchangeEviToEviAccountEditWidget eviToEviAccountEditWidget;
  private InvoiceExchangeEviToInvAccountEditWidget eviToInvAccountEditWidget;
  private InvoiceExchangeInvToInvAccountEditWidget invToInvAccountEditWidget;

  public static InvoiceExchangeCreatePage getInstance() {
    if (instance == null) {
      instance = new InvoiceExchangeCreatePage();
    }
    return instance;
  }

  private void refreshToolBar() {
    saveAndCreateAction.setVisible((getEP().getProcessCtx().getUrlParams().getDefinitionkey()==null)&&(!getEP().getProcessCtx().isProcessMode()));
    saveAndCreateAction.setEnabled(getEP().isPermitted(BAction.CREATE.getKey()));
  }

  @Override
  public void onHide() {
    generalWidget.clearConditions();
    eviToEviAccountEditWidget.clearConditions();
    eviToInvAccountEditWidget.clearConditions();
    invToInvAccountEditWidget.clearConditions();
  }

  @Override
  public void onAction(RActionEvent event) {
    if (ActionName.CHANGE_EXCHANGE_TYPE_ACTION.equals(event.getActionName())) {
      refreshShow();
    } else if (ActionName.CHANGE_STORE.equals(event.getActionName())) {
      clearEntityDetails();
      resetWidgetEntity();
    } else if (ActionName.CHANGE_INVTOINVTYPE_ACTION.equals(event.getActionName())) {
      refreshShowWhenInvToInvTypeChanged();
    }
  }
  
  private void refreshShowWhenInvToInvTypeChanged(){
    entity.getExchAccountLines().clear();
    mainPanel.remove(invToInvAccountEditWidget);
    if (BInvToInvType.exchange.equals(entity.getInvToInvType())) {
      mainPanel.add(0, invToInvAccountEditWidget);
      invToInvAccountEditWidget.setValue(entity);
    }
  }
  
  private void refreshShow(){
    removeMainPanelWidgets();
    clearEntityDetails();
    if (BExchangeType.invToInv.equals(entity.getType())) {
      mainPanel.add(0, invToInvEditWidget);
      invToInvEditWidget.setValue(entity);
      if (BInvToInvType.exchange.equals(entity.getInvToInvType())) {
        mainPanel.add(0, invToInvAccountEditWidget);
        invToInvAccountEditWidget.setValue(entity);
      }
    } else {
      mainPanel.add(0, eviToEviEditWidget);
      eviToEviEditWidget.setValue(entity);
      if (BExchangeType.eviToInv.equals(entity.getType())) {
        mainPanel.add(0, eviToInvAccountEditWidget);
        eviToInvAccountEditWidget.setValue(entity);
      } else {
        mainPanel.add(0, eviToEviAccountEditWidget);
        eviToEviAccountEditWidget.setValue(entity);
      }
    }
  }
  
  private void clearEntityDetails(){
    entity.getExchBalanceLines().clear();
    entity.getExchAccountLines().clear();
  }

  private void removeMainPanelWidgets() {
    mainPanel.remove(eviToEviEditWidget);
    mainPanel.remove(invToInvEditWidget);
    mainPanel.remove(eviToEviAccountEditWidget);
    mainPanel.remove(eviToInvAccountEditWidget);
    mainPanel.remove(invToInvAccountEditWidget);
  }

  protected void drawToolbar() {
    actionHandler = new ActionHandler();

    saveAction = new RAction(RActionFacade.SAVE, actionHandler);
    saveButton = new RToolbarButton(saveAction);
    getToolbar().add(saveButton);

    saveAndCreateAction = new RAction(RActionFacade.SAVE_AND_CREATE, actionHandler);
    RToolbarButton saveAndCreateButton = new RToolbarButton(saveAndCreateAction);
    getToolbar().add(saveAndCreateButton);

    // BPM出口按钮
    injectBpmActions();

    getToolbar().addSeparator();
    cancelAction = new RAction(RActionFacade.CANCEL, actionHandler);
    RToolbarButton cancelButton = new RToolbarButton(cancelAction);
    getToolbar().add(cancelButton);
    getToolbar().minimizeSeparators();
  }

  @Override
  protected void drawSelf(VerticalPanel root) {
    changeHandler = new FieldValueChangeHandler();

    generalWidget = new InvoiceExchangeGeneralEditWidget();
    root.add(generalWidget);

    mainPanel = new RMultiVerticalPanel(1);
    root.add(mainPanel);

    eviToEviEditWidget = new InvoiceExchangeEviToEviEditWidget();
    invToInvEditWidget = new InvoiceExchangeInvToInvEditWidget();
    
    eviToEviAccountEditWidget = new InvoiceExchangeEviToEviAccountEditWidget();
    eviToInvAccountEditWidget = new InvoiceExchangeEviToInvAccountEditWidget();
    invToInvAccountEditWidget = new InvoiceExchangeInvToInvAccountEditWidget();

    addActionListener();
    
    root.add(drawRemarkBox());
  }
  
  private void addActionListener(){
    invToInvEditWidget.addActionHandler(invToInvAccountEditWidget);
    eviToEviEditWidget.addActionHandler(eviToEviAccountEditWidget);
    eviToEviEditWidget.addActionHandler(eviToInvAccountEditWidget);
    
    generalWidget.addActionHandler(invToInvAccountEditWidget);
    generalWidget.addActionHandler(this);
  }

  private Widget drawRemarkBox() {
    remarkField = new RTextArea();
    remarkField.setFieldDef(PInvoiceExchangeDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.addChangeHandler(changeHandler);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceExchangeDef.constants.remark());
    box.setWidth("100%");
    box.setEditing(true);
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }

  @Override
  protected EPInvoiceExchange getEP() {
    return EPInvoiceExchange.getInstance();
  }

  @Override
  protected void newEntity(JumpParameters params, AsyncCallback<BInvoiceExchange> callback) {
    BInvoiceExchange target = buildEntity();
    callback.onSuccess(target);
  }

  private BInvoiceExchange buildEntity() {
    BInvoiceExchange target = new BInvoiceExchange();
    target.setExchanger(getEP().getCurrentUserUCN());
    target.setBizState(BBizStates.INEFFECT);
    target.setExchangeDate(RDateUtil.today());
    return target;
  }

  @Override
  protected void refreshEntity() {
    refreshToolBar();
    if (entity.getAccountUnit() == null && getEP().getUserStores().size() == 1) {
      entity.setAccountUnit(new BUCN(getEP().getUserStores().get(0)));
    }
    
    generalWidget.setValue(entity, false);
    remarkField.setValue(entity.getRemark());

    refreshShow();

    clearValidResults();
    FocusOnFirstFieldUtil.focusOnFirstField(generalWidget);
  }
  
  private void resetWidgetEntity(){
    eviToEviEditWidget.setValue(entity);
    invToInvEditWidget.setValue(entity);
    eviToEviAccountEditWidget.setValue(entity);
    eviToInvAccountEditWidget.setValue(entity);
    invToInvAccountEditWidget.setValue(entity);
  }
      

  /**
   * 保存并新建
   */
  protected void doSaveAndCreate() {
    GWTUtil.blurActiveElement();
    if (validate() == false) {
      return;
    }

    beforeSave();
    getEP().getServiceAgent().save(entity, new Callback<BInvoiceExchange>() {
      @Override
      public void execute(BInvoiceExchange result) {
        entity = buildEntity();
        refreshEntity();
        Message infoMsg = Message.info(BaseBpm2Message.M.saveSuccessfully(result.toFriendlyStr()));
        getMessagePanel().putMessage(infoMsg);
      }
    });
  }

  private class ActionHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == saveAction) {
        doSave();
      } else if (event.getSource() == saveAndCreateAction) {
        doSaveAndCreate();
      } else if (event.getSource() == cancelAction) {
        doCancel();
      }
    }
  }

  private class FieldValueChangeHandler implements ChangeHandler {
    @Override
    public void onChange(ChangeEvent event) {
      if (remarkField == event.getSource()) {
        entity.setRemark(remarkField.getValue());
      }
    }
  }

}
