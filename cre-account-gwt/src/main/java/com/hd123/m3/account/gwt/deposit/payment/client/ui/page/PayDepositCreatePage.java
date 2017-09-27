/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： PayCreatePage.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.payment.client.ui.page;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.deposit.commons.client.actionname.DepositActionName;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositLogger;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositValidator;
import com.hd123.m3.account.gwt.deposit.intf.client.DepositMessage;
import com.hd123.m3.account.gwt.deposit.intf.client.dd.PDepositDef;
import com.hd123.m3.account.gwt.deposit.payment.client.EPPayDeposit;
import com.hd123.m3.account.gwt.deposit.payment.client.rpc.PayDepositService;
import com.hd123.m3.account.gwt.deposit.payment.client.rpc.PayDepositServiceAgent;
import com.hd123.m3.account.gwt.deposit.payment.client.ui.gadget.PayDepositGeneralCreateGadget;
import com.hd123.m3.account.gwt.deposit.payment.client.ui.gadget.PayDepositLineEditGadget;
import com.hd123.m3.account.gwt.deposit.payment.client.ui.gadget.PayDepositLineEditGrid;
import com.hd123.m3.account.gwt.deposit.payment.intf.client.PayDepositUrlParams;
import com.hd123.m3.account.gwt.deposit.payment.intf.client.PayDepositUrlParams.Create;
import com.hd123.m3.account.gwt.deposit.payment.intf.client.perm.PayDepositPermDef;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmCreatePage;
import com.hd123.m3.commons.gwt.util.client.layout.BillLayoutManager;
import com.hd123.m3.commons.gwt.util.client.layout.LayoutButtonGroup;
import com.hd123.m3.commons.gwt.util.client.validation.LineLocator;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.history.RHistory;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.hotkey.RHotKey;
import com.hd123.rumba.gwt.widget2.client.hotkey.RKey;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSplitButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author chenpeisi
 * 
 */
public class PayDepositCreatePage extends BaseBpmCreatePage implements Create, RValidatable,
    RActionHandler, HasRActionHandlers, SelectionHandler<Message>, HasFocusables {
  private static PayDepositCreatePage instance = null;

  public static PayDepositCreatePage getInstance() {
    if (instance == null)
      instance = new PayDepositCreatePage();
    return instance;
  }

  public PayDepositCreatePage() {
    super();
    drawToolbar();
    drawSelf();
    afterDraw();
  }

  private BDeposit entity;
  private BDepositValidator validator;

  private RAction saveAction;
  private RAction cancelAction;

  private RAction importFromContractAction;
  private RAction addAction;
  private RAction insertAction;
  private RPopupMenu addMenu;
  private RAction deleteAction;
  private RToolbarSplitButton addButton;
  private Label totalNumber;

  private BillLayoutManager layoutMgr;
  private PayDepositGeneralCreateGadget generalGadget;
  private PayDepositLineEditGrid lineGridGadget;
  private PayDepositLineEditGadget lineEditGadget;
  private RTextArea remarkField;

  private void drawToolbar() {
    saveAction = new RAction(RActionFacade.SAVE, new Handler_saveAction());
    RToolbarButton saveButton = new RToolbarButton(saveAction);
    getToolbar().add(saveButton);

    // BPM出口按钮
    injectBpmActions();

    cancelAction = new RAction(RActionFacade.CANCEL, new Handler_cancelAction());
    RToolbarButton cancelButton = new RToolbarButton(cancelAction);
    getToolbar().add(cancelButton);
  }

  private void drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    initWidget(panel);

    generalGadget = new PayDepositGeneralCreateGadget();
    panel.add(generalGadget);

    Widget w = drawMiddle();
    panel.add(w);

    w = drawRemark();
    panel.add(w);
  }

  private Widget drawMiddle() {
    layoutMgr = new BillLayoutManager();

    lineGridGadget = new PayDepositLineEditGrid();
    layoutMgr.setMainWidget(lineGridGadget);

    lineEditGadget = new PayDepositLineEditGadget();
    layoutMgr.setSecondaryWidget(lineEditGadget);

    generalGadget.addActionHandler(lineEditGadget);
    generalGadget.addActionHandler(lineGridGadget);

    RCaptionBox box = new RCaptionBox();
    box.setEditing(true);
    box.getCaptionBar().setShowMaximize(true);
    box.getCaptionBar().setShowCollapse(true);
    box.setContentSpacing(0);
    box.setCaption(DepositMessage.M.paymentLine());
    box.setContent(layoutMgr);
    box.setWidth("100%");

    totalNumber = new Label(DepositMessage.M.resultTotal(0));
    box.getCaptionBar().addButton(totalNumber);

    addMenu = new RPopupMenu();
    importFromContractAction = new RAction(DepositMessage.M.importFromContract(),
        new Handler_importFromContractAction());
    importFromContractAction.setHotKey(null);
    addMenu.addItem(new RMenuItem(importFromContractAction));
    addAction = new RAction(RActionFacade.CREATE, new Handler_addAction());
    addAction.setCaption(DepositMessage.M.addLine());
    addAction.setHotKey(new RHotKey(false, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(addAction));
    insertAction = new RAction(DepositMessage.M.insertLine(), new Handler_insertAction());
    insertAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(insertAction));

    addButton = new RToolbarSplitButton(addAction);
    addButton.setMenu(addMenu);
    addButton.setShowText(false);
    box.getCaptionBar().addButton(addButton);

    deleteAction = new RAction(RActionFacade.DELETE, new Handler_deleteAction());
    deleteAction.setHotKey(null);
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    box.getCaptionBar().addButton(deleteButton);

    box.getCaptionBar().addButton(new RToolbarSeparator());

    LayoutButtonGroup btnGroup = new LayoutButtonGroup(layoutMgr);
    box.getCaptionBar().addButton(btnGroup);

    return box;
  }

  private Widget drawRemark() {
    remarkField = new RTextArea(PDepositDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        entity.setRemark(remarkField.getValue());
      }
    });

    RCaptionBox box = new RCaptionBox();
    box.setEditing(true);
    box.setWidth("100%");
    box.setCaption(PDepositDef.constants.remark());
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }

  private void afterDraw() {
    // 监听错误消息。
    getMessagePanel().addSelectionHandler(this);
    // 定位错误消息行。
    this.addActionHandler(lineGridGadget);
    // 改变标题。
    lineGridGadget.addActionHandler(this);

    generalGadget.addActionHandler(lineEditGadget);
    generalGadget.addActionHandler(lineGridGadget);

    lineEditGadget.addActionHandler(lineGridGadget);
    lineGridGadget.addActionHandler(lineEditGadget);
    // 通知总金额改变
    lineEditGadget.addActionHandler(generalGadget);
    lineGridGadget.addActionHandler(generalGadget);
  }

  public void onShow(final JumpParameters params) {
    super.onShow(params);

    if (checkIn() == false)
      return;

    doCreate(params, new Command() {

      @Override
      public void execute() {
        refresh(entity);
        getEP().appendSearchBox();
      }
    });
  }

  private boolean checkIn() {
    if (getEP().isBpmMode())
      return true;
    if (getEP().isPermitted(PayDepositPermDef.CREATE) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void doCreate(JumpParameters params, final Command callback) {
    String advanceUuid = params.getUrlRef().get(PayDepositUrlParams.Create.PN_ADVANCE_UUID);

    if (StringUtil.isNullOrBlank(advanceUuid) == false)
      doCreateByAdvance(advanceUuid, callback);
    else
      doCreate(callback);
  }

  private void doCreate(final Command callback) {
    RLoadingDialog.show(DepositMessage.M.actionDoing(DepositMessage.M.create()));
    PayDepositService.Locator.getService().create(new RBAsyncCallback2<BDeposit>() {
      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = DepositMessage.M.actionFailed(DepositMessage.M.create(), getEP()
            .getModuleCaption());
        RMsgBox.showErrorAndBack(msg, caught);
      }

      public void onSuccess(BDeposit result) {
        RLoadingDialog.hide();
        entity = result;
        callback.execute();
      }
    });
  }

  private void doCreateByAdvance(String advanceUuid, final Command callback) {
    RLoadingDialog.show(DepositMessage.M.actionDoing(DepositMessage.M.create()));
    PayDepositService.Locator.getService().createByAdvance(advanceUuid,
        new RBAsyncCallback2<BDeposit>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMessage.M.actionFailed(DepositMessage.M.create(), getEP()
                .getModuleCaption());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BDeposit result) {
            RLoadingDialog.hide();
            entity = result;
            callback.execute();
          }
        });
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    generalGadget.clearQueryConditions();
    lineEditGadget.clearQueryConditions();
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == DepositActionName.REFRESH) {
      totalNumber.setText(DepositMessage.M.resultTotal(entity.getLines().size()));
    }
  }

  @Override
  public void clearValidResults() {
    generalGadget.clearValidResults();
    lineEditGadget.clearValidResults();
    remarkField.clearValidResults();
    validator.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return generalGadget.isValid() && remarkField.isValid() && validator.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> list = new ArrayList<Message>();
    list.addAll(generalGadget.getInvalidMessages());
    list.addAll(remarkField.getInvalidMessages());
    list.addAll(validator.getInvalidMessages());
    return list;
  }

  @Override
  public boolean validate() {
    getMessagePanel().clearMessages();
    boolean valid = generalGadget.validate();
    valid &= remarkField.validate();
    valid &= validator.validate();
    if (valid == false)
      getMessagePanel().putMessages(getInvalidMessages());
    return valid;
  }

  @Override
  public Focusable getFocusable(String field) {
    return lineEditGadget.getFocusable(field);
  }

  @Override
  public void onSelection(SelectionEvent<Message> event) {
    Object obj = event.getSelectedItem().getSource();
    if (obj instanceof LineLocator) {
      LineLocator o = (LineLocator) event.getSelectedItem().getSource();
      if (o != null)
        RActionEvent.fire(PayDepositCreatePage.this, DepositActionName.ONSELECT_LINE,
            new Integer(o.getLineNumber()));
    }
  }

  private class Handler_saveAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      doSave();
    }
  }

  private void doSave() {
    GWTUtil.blurActiveElement();
    if (validate() == false)
      return;
    RLoadingDialog.show(DepositMessage.M.actionDoing(DepositMessage.M.save()));
    PayDepositService.Locator.getService().save(entity.getFilterData(), getEP().getProcessCtx(),
        new RBAsyncCallback2<BDeposit>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMessage.M.actionFailed(DepositMessage.M.save(), getEP()
                .getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(BDeposit result) {
            RLoadingDialog.hide();
            BDepositLogger.getInstance().log(DepositMessage.M.create(),result);
            
            String msg = DepositMessage.M.onSuccess(DepositMessage.M.save(), getEP()
                .getModuleCaption(), result.toFriendlyStr());
            getEP().jumpToViewPage(result.getUuid(), Message.info(msg));
          }
        });
  }

  private class Handler_cancelAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RMsgBox.showConfirm(DepositMessage.M.confirmCancel(DepositMessage.M.create()),
          new RMsgBox.ConfirmCallback() {
            public void onClosed(boolean confirmed) {
              if (confirmed)
                RHistory.back();
            }
          });
    }
  }

  private class Handler_importFromContractAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RActionEvent.fire(PayDepositCreatePage.this, DepositActionName.IMPORT_FROM_CONTRACT);
    }
  }

  private class Handler_addAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RActionEvent.fire(PayDepositCreatePage.this, DepositActionName.ADD_LINE);
    }
  }

  private class Handler_insertAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RActionEvent.fire(PayDepositCreatePage.this, DepositActionName.INSERT_LINE);
    }
  }

  private class Handler_deleteAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RActionEvent.fire(PayDepositCreatePage.this, DepositActionName.BATCH_DELETE_LINE);
    }
  }

  @Override
  protected EPPayDeposit getEP() {
    return EPPayDeposit.getInstance();
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  private class ProcessDialog extends M3BaseOutgoingDialog {

    @Override
    protected boolean doBeforeShowDialog() {
      if (BBizActions.DELETE.equals(outgoingDefinition.getBusinessAction())) {
        return true;
      } else {
        GWTUtil.blurActiveElement();
        return validate();
      }
    }

    @Override
    protected void doExcute(BOperation operation, AsyncCallback<Object> callback) {
      final BProcessContext processCtx = getEP().getFormBuilder().getProcessCtx();
      PayDepositServiceAgent.executeTask(processCtx, operation, entity, true, this);
    }
  }

  @Override
  protected void refreshEntity() {
    assert entity != null;
    if (entity.getAccountUnit() == null && getEP().getUserStores().size() == 1) {
      entity.setAccountUnit(new BUCN(getEP().getUserStores().get(0)));
    }

    validator = new BDepositValidator(entity, PayDepositCreatePage.this);

    generalGadget.refresh(entity);

    lineEditGadget.setEntity(entity);
    lineGridGadget.setEntity(entity);
    lineGridGadget.refresh(false);
    remarkField.setValue(entity.getRemark());

    clearValidResults();
    
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        layoutMgr.restoreHeight();
        setFoucsOnFirstField();
      }
    });
  }

  private void setFoucsOnFirstField() {
    generalGadget.focusOnFirstField();
  }
}
