/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	PayDepositRepaymentEditPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-20 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.payment.client.ui;

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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.actionname.DepositRepaymentActionName;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepaymentLogger;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepaymentValidator;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.DepositRepaymentMessage;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.dd.PDepositRepaymentDef;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.EPPayDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.rpc.PayDepositRepaymentLoader;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.rpc.PayDepositRepaymentService;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.rpc.PayDepositRepaymentServiceAgent;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.ui.gadget.PayDepositRepaymentGeneralEditGadget;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.ui.gadget.PayDepositRepaymentLineEditGadget;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.ui.gadget.PayDepositRepaymentLineEditGrid;
import com.hd123.m3.account.gwt.depositrepayment.payment.intf.client.PayDepositRepaymentUrlParams.Edit;
import com.hd123.m3.account.gwt.depositrepayment.payment.intf.client.perm.PayDepositRepaymentPermDef;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmEditPage;
import com.hd123.m3.commons.gwt.util.client.layout.BillLayoutManager;
import com.hd123.m3.commons.gwt.util.client.layout.LayoutButtonGroup;
import com.hd123.m3.commons.gwt.util.client.validation.LineLocator;
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
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
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
 * @author zhuhairui
 * 
 */
public class PayDepositRepaymentEditPage extends BaseBpmEditPage implements Edit, RValidatable,
    RActionHandler, HasFocusables, HasRActionHandlers, SelectionHandler<Message> {
  private static PayDepositRepaymentEditPage instance = null;

  public static PayDepositRepaymentEditPage getInstance() {
    if (instance == null)
      instance = new PayDepositRepaymentEditPage();
    return instance;
  }

  public PayDepositRepaymentEditPage() {
    super();
    drawToolbar();
    drawSelf();
    afterDraw();
  }

  private BDepositRepayment entity;
  private BDepositRepaymentValidator validator;

  private RAction saveAction;
  private RAction cancelAction;

  private RAction addAction;
  private RAction insertAction;
  private RPopupMenu addMenu;
  private RAction deleteAction;
  private RToolbarSplitButton addButton;
  private Label totalNumber;

  private BillLayoutManager layoutMgr;
  private PayDepositRepaymentGeneralEditGadget generalGadget;
  private PayDepositRepaymentLineEditGadget lineEditGadget;
  private PayDepositRepaymentLineEditGrid lineGridGadget;
  private RTextArea remarkField;

  private void drawToolbar() {
    saveAction = new RAction(RActionFacade.SAVE, new Handler_saveAction());
    RToolbarButton saveButton = new RToolbarButton(saveAction);
    getToolbar().add(saveButton);

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

    generalGadget = new PayDepositRepaymentGeneralEditGadget();
    panel.add(generalGadget);

    panel.add(drawMiddle());
    panel.add(drawRemark());
  }

  private Widget drawMiddle() {
    layoutMgr = new BillLayoutManager();

    lineGridGadget = new PayDepositRepaymentLineEditGrid();
    layoutMgr.setMainWidget(lineGridGadget);

    lineEditGadget = new PayDepositRepaymentLineEditGadget();
    layoutMgr.setSecondaryWidget(lineEditGadget);

    RCaptionBox box = new RCaptionBox();
    box.getCaptionBar().setShowMaximize(true);
    box.getCaptionBar().setShowCollapse(true);
    box.setContentSpacing(0);
    box.setCaption(DepositRepaymentMessage.M.repaymentLines());
    box.setContent(layoutMgr);
    box.setWidth("100%");
    box.setEditing(true);

    totalNumber = new Label(DepositRepaymentMessage.M.resultTotal(0));
    box.getCaptionBar().addButton(totalNumber);

    addMenu = new RPopupMenu();
    addAction = new RAction(RActionFacade.CREATE, new Handler_addAction());
    addAction.setCaption(DepositRepaymentMessage.M.addLine());
    addAction.setHotKey(new RHotKey(false, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(addAction));
    insertAction = new RAction(DepositRepaymentMessage.M.insertLine(), new Handler_insertAction());
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
    remarkField = new RTextArea(PDepositRepaymentDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        entity.setRemark(remarkField.getValue());
      }
    });

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PDepositRepaymentDef.constants.remark());
    box.setWidth("100%");
    box.setEditing(true);
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
    lineGridGadget.addActionHandler(lineEditGadget);
    lineEditGadget.addActionHandler(lineGridGadget);

    // 通知总金额改变
    lineEditGadget.addActionHandler(generalGadget);
    lineGridGadget.addActionHandler(generalGadget);

  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (checkIn() == false)
      return;

    PayDepositRepaymentLoader.decodeParams(params, new Command() {
      @Override
      public void execute() {
        entity = PayDepositRepaymentLoader.getEntity();
        refresh(entity);
        getEP().appendSearchBox();
      }
    });
  }

  private boolean checkIn() {
    if (getEP().isProcessMode())
      return true;
    if (getEP().isPermitted(PayDepositRepaymentPermDef.UPDATE) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
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
    generalGadget.clearQueryConditions();
    lineEditGadget.clearQueryConditions();
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == DepositRepaymentActionName.REFRESH) {
      totalNumber.setText(DepositRepaymentMessage.M.resultTotal(entity.getLines().size()));
    }
  }

  @Override
  public void clearValidResults() {
    generalGadget.clearValidResults();
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
    clearValidResults();
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
        RActionEvent.fire(PayDepositRepaymentEditPage.this,
            DepositRepaymentActionName.ONSELECT_LINE, new Integer(o.getLineNumber()));
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
    RLoadingDialog.show(DepositRepaymentMessage.M.actionDoing(DepositRepaymentMessage.M.save()));
    PayDepositRepaymentService.Locator.getService().save(entity, getEP().getProcessCtx(),
        new RBAsyncCallback2<BDepositRepayment>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositRepaymentMessage.M.actionFailed(DepositRepaymentMessage.M.save(),
                getEP().getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(BDepositRepayment result) {
            RLoadingDialog.hide();
            BDepositRepaymentLogger.getInstance().log(DepositRepaymentMessage.M.modify(), result);

            String msg = DepositRepaymentMessage.M.onSuccess(DepositRepaymentMessage.M.save(),
                getEP().getModuleCaption(), entity.toFriendlyStr());
            getEP().jumpToViewPage(entity.getUuid(), Message.info(msg));
          }
        });
  }

  private class Handler_cancelAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RMsgBox.showConfirm(
          DepositRepaymentMessage.M.confirmCancel(DepositRepaymentMessage.M.edit()),
          new RMsgBox.ConfirmCallback() {
            public void onClosed(boolean confirmed) {
              if (confirmed)
                RHistory.back();
            }
          });
    }
  }

  private class Handler_addAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RActionEvent.fire(PayDepositRepaymentEditPage.this, DepositRepaymentActionName.ADD_LINE);
    }
  }

  private class Handler_insertAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RActionEvent.fire(PayDepositRepaymentEditPage.this, DepositRepaymentActionName.INSERT_LINE);
    }
  }

  private class Handler_deleteAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RActionEvent.fire(PayDepositRepaymentEditPage.this,
          DepositRepaymentActionName.BATCH_DELETE_LINE);
    }
  }

  @Override
  public void refreshToolbar(RToolbar toolbar) {
    saveAction.setVisible(getEP().isPermitted(PayDepositRepaymentPermDef.UPDATE));
  }

  @Override
  protected EPPayDepositRepayment getEP() {
    return EPPayDepositRepayment.getInstance();
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
      PayDepositRepaymentServiceAgent.executeTask(processCtx, operation, entity, true, this);
    }
  }

  @Override
  protected void refreshEntity() {
    assert entity != null;

    validator = new BDepositRepaymentValidator(entity, lineEditGadget);

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
        setFocusFirstField();
      }
    });
  }

  private void setFocusFirstField() {
    Timer t = new Timer() {

      @Override
      public void run() {
        generalGadget.setFocusOnFirstField();
      }
    };
    t.schedule(500);
  }
}
