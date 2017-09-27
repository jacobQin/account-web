/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegCreatePage.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-27 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.receipt.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.invoice.commons.client.actionname.InvoiceRegActionName;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegConfig;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegInvoiceValidator;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegLine;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegLineValidator;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegLogger;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.InvoiceRegLocator;
import com.hd123.m3.account.gwt.invoice.commons.client.ui.dialog.AccBrowserDialog;
import com.hd123.m3.account.gwt.invoice.commons.client.ui.dialog.AccByBillBrowserDialog;
import com.hd123.m3.account.gwt.invoice.commons.client.ui.dialog.AccByPaymentNoticeBrowserDialog;
import com.hd123.m3.account.gwt.invoice.commons.client.ui.dialog.AccByStatementBrowserDialog;
import com.hd123.m3.account.gwt.invoice.intf.client.InvoiceRegMessage;
import com.hd123.m3.account.gwt.invoice.intf.client.dd.PInvoiceRegDef;
import com.hd123.m3.account.gwt.invoice.intf.client.dd.PInvoiceRegLineDef;
import com.hd123.m3.account.gwt.invoice.receipt.client.EPRecInvoiceReg;
import com.hd123.m3.account.gwt.invoice.receipt.client.rpc.RecInvoiceRegService;
import com.hd123.m3.account.gwt.invoice.receipt.client.rpc.RecInvoiceRegServiceAgent;
import com.hd123.m3.account.gwt.invoice.receipt.client.ui.gadget.RecInvoiceRegGeneralCreateGadget;
import com.hd123.m3.account.gwt.invoice.receipt.client.ui.gadget.RecInvoiceRegInvoiceEditGadget;
import com.hd123.m3.account.gwt.invoice.receipt.client.ui.gadget.RecInvoiceRegInvoiceEditGrid;
import com.hd123.m3.account.gwt.invoice.receipt.client.ui.gadget.RecInvoiceRegLineEditGadget;
import com.hd123.m3.account.gwt.invoice.receipt.client.ui.gadget.RecInvoiceRegLineEditGrid;
import com.hd123.m3.account.gwt.invoice.receipt.intf.client.RecInvoiceRegUrlParams;
import com.hd123.m3.account.gwt.invoice.receipt.intf.client.RecInvoiceRegUrlParams.Create;
import com.hd123.m3.account.gwt.invoice.receipt.intf.client.perm.RecInvoiceRegPermDef;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountNotice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountSourceBill;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmCreatePage;
import com.hd123.m3.commons.gwt.util.client.layout.BillLayoutManager;
import com.hd123.m3.commons.gwt.util.client.layout.LayoutButtonGroup;
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
public class RecInvoiceRegCreatePage extends BaseBpmCreatePage implements Create, RValidatable,
    RActionHandler, HasRActionHandlers, SelectionHandler<Message> {
  private static RecInvoiceRegCreatePage instance = null;

  public static RecInvoiceRegCreatePage getInstance() {
    if (instance == null) {
      instance = new RecInvoiceRegCreatePage();
    }
    return instance;
  }

  public RecInvoiceRegCreatePage() {
    super();
    drawToolbar();
    drawSelf();
    afterDraw();
  }

  private BInvoiceReg entity;
  private BInvoiceRegLineValidator lineValidator;
  private BInvoiceRegInvoiceValidator invoiceValidator;

  private RAction saveAction;
  private RAction cancelAction;

  private RecInvoiceRegGeneralCreateGadget generalGadget;

  private Label invoiceTotal;
  private BillLayoutManager invoiceLayoutMgr;
  private RecInvoiceRegInvoiceEditGrid invoiceGrid;
  private RecInvoiceRegInvoiceEditGadget invoiceGadget;
  private RAction addInvoiceAction;
  private RAction insertInvoiceAction;
  private RPopupMenu addInvoiceMenu;
  private RAction deleteInvoiceAction;
  private RToolbarSplitButton addInvoiceButton;

  private Label lineTotal;
  private BillLayoutManager lineLayoutMgr;
  private RecInvoiceRegLineEditGrid lineGrid;
  private RecInvoiceRegLineEditGadget lineGadget;
  /** 从账单导入 */
  private RAction addStatementAction;
  private AccByStatementBrowserDialog statementDialog;
  /** 从收付款通知单导入 */
  private RAction addPaymentNoticeAction;
  private AccByPaymentNoticeBrowserDialog paymentNoticeDialog;
  /** 从其他单据导入 */
  private RAction addAccBillAction;
  private AccByBillBrowserDialog billDialog;
  /** 从账款导入 */
  private RAction addAccountAction;
  private AccBrowserDialog dialog;

  private RPopupMenu addLineMenu;
  private RAction deleteLineAction;
  private RToolbarSplitButton addLineButton;

  private RTextArea remarkField;

  private Handler_clickAction clickHandler = new Handler_clickAction();

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

    generalGadget = new RecInvoiceRegGeneralCreateGadget();
    panel.add(generalGadget);

    panel.add(drawLinePanel());

    panel.add(drawInvoicePanel());

    panel.add(drawRemark());
  }

  private Widget drawLinePanel() {
    lineLayoutMgr = new BillLayoutManager();

    lineGrid = new RecInvoiceRegLineEditGrid();
    lineLayoutMgr.setMainWidget(lineGrid);

    lineGadget = new RecInvoiceRegLineEditGadget();
    lineLayoutMgr.setSecondaryWidget(lineGadget);

    RCaptionBox box = new RCaptionBox();
    box.getCaptionBar().setShowMaximize(true);
    box.setContentSpacing(0);
    box.setCaption(InvoiceRegMessage.M.lineInfo());
    box.setContent(lineLayoutMgr);
    box.setWidth("100%");

    lineTotal = new HTML(InvoiceRegMessage.M.resultTotal(0));
    box.getCaptionBar().addButton(lineTotal);

    addLineMenu = new RPopupMenu();

    addStatementAction = new RAction(RActionFacade.CREATE, clickHandler);
    addStatementAction.setCaption(InvoiceRegMessage.M.addFromStatement());
    addLineMenu.addItem(new RMenuItem(addStatementAction));

    addPaymentNoticeAction = new RAction(InvoiceRegMessage.M.addFromPaymentNotice(), clickHandler);
    addLineMenu.addItem(new RMenuItem(addPaymentNoticeAction));

    addAccBillAction = new RAction(InvoiceRegMessage.M.addFromBill(), clickHandler);
    addLineMenu.addItem(new RMenuItem(addAccBillAction));

    addAccountAction = new RAction(InvoiceRegMessage.M.addFromAccount(), clickHandler);
    addLineMenu.addItem(new RMenuItem(addAccountAction));

    addLineButton = new RToolbarSplitButton(addStatementAction);
    addLineButton.setMenu(addLineMenu);
    addLineButton.setShowText(false);
    box.getCaptionBar().addButton(addLineButton);

    deleteLineAction = new RAction(RActionFacade.DELETE, clickHandler);
    deleteLineAction.clearHotKey();
    RToolbarButton deleteButton = new RToolbarButton(deleteLineAction);
    deleteButton.setShowText(false);
    box.getCaptionBar().addButton(deleteButton);

    box.getCaptionBar().addButton(new RToolbarSeparator());

    LayoutButtonGroup btnGroup = new LayoutButtonGroup(lineLayoutMgr);
    box.getCaptionBar().addButton(btnGroup);

    box.setEditing(true);
    return box;
  }

  private Widget drawInvoicePanel() {
    invoiceLayoutMgr = new BillLayoutManager();

    invoiceGrid = new RecInvoiceRegInvoiceEditGrid();
    invoiceLayoutMgr.setMainWidget(invoiceGrid);

    invoiceGadget = new RecInvoiceRegInvoiceEditGadget();
    invoiceLayoutMgr.setSecondaryWidget(invoiceGadget);

    RCaptionBox box = new RCaptionBox();
    box.getCaptionBar().setShowMaximize(true);
    box.setContentSpacing(0);
    box.setCaption(InvoiceRegMessage.M.invoiceInfo());
    box.setContent(invoiceLayoutMgr);
    box.setWidth("100%");

    invoiceTotal = new HTML(InvoiceRegMessage.M.resultTotal(0));
    box.getCaptionBar().addButton(invoiceTotal);

    addInvoiceMenu = new RPopupMenu();
    addInvoiceAction = new RAction(RActionFacade.CREATE, clickHandler);
    addInvoiceAction.setCaption(InvoiceRegMessage.M.addLine());
    addInvoiceAction.setHotKey(new RHotKey(false, false, false, RKey.KEY_CODE_ADD));
    addInvoiceMenu.addItem(new RMenuItem(addInvoiceAction));
    insertInvoiceAction = new RAction(InvoiceRegMessage.M.insertLine(), clickHandler);
    insertInvoiceAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_INSERT));
    addInvoiceMenu.addItem(new RMenuItem(insertInvoiceAction));

    addInvoiceButton = new RToolbarSplitButton(addInvoiceAction);
    addInvoiceButton.setMenu(addInvoiceMenu);
    addInvoiceButton.setShowText(false);
    box.getCaptionBar().addButton(addInvoiceButton);

    deleteInvoiceAction = new RAction(RActionFacade.DELETE, clickHandler);
    deleteInvoiceAction.clearHotKey();
    RToolbarButton deleteButton = new RToolbarButton(deleteInvoiceAction);
    deleteButton.setShowText(false);
    box.getCaptionBar().addButton(deleteButton);

    box.getCaptionBar().addButton(new RToolbarSeparator());

    LayoutButtonGroup btnGroup = new LayoutButtonGroup(invoiceLayoutMgr);
    box.getCaptionBar().addButton(btnGroup);

    box.setEditing(true);
    return box;
  }

  private Widget drawRemark() {
    remarkField = new RTextArea(PInvoiceRegDef.remark);
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
    box.setCaption(PInvoiceRegDef.constants.remark());
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }

  private void afterDraw() {
    this.addActionHandler(lineGadget);
    lineGadget.addActionHandler(this);
    this.addActionHandler(lineGrid);
    lineGrid.addActionHandler(this);

    this.addActionHandler(invoiceGadget);
    invoiceGadget.addActionHandler(this);
    this.addActionHandler(invoiceGrid);
    invoiceGrid.addActionHandler(this);

    generalGadget.addActionHandler(lineGadget);
    generalGadget.addActionHandler(lineGrid);
    generalGadget.addActionHandler(invoiceGadget);
    generalGadget.addActionHandler(invoiceGrid);

    lineGadget.addActionHandler(generalGadget);
    lineGrid.addActionHandler(generalGadget);
    invoiceGadget.addActionHandler(generalGadget);
    invoiceGrid.addActionHandler(generalGadget);

    invoiceGadget.addActionHandler(invoiceGrid);
    invoiceGrid.addActionHandler(invoiceGadget);
    lineGadget.addActionHandler(lineGrid);
    lineGrid.addActionHandler(lineGadget);

    getMessagePanel().addSelectionHandler(this);

  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    generalGadget.clearQueryConditions();
    clearQueryConditions();
  }

  public void clearQueryConditions() {
    if (statementDialog != null)
      statementDialog.clearConditions();
    if (paymentNoticeDialog != null)
      paymentNoticeDialog.clearConditions();
    if (billDialog != null)
      billDialog.clearConditions();
    if (dialog != null)
      dialog.clearConditions();
  }

  @Override
  public void onShow(JumpParameters params) {
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
    if (getEP().isPermitted(RecInvoiceRegPermDef.CREATE) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void doCreate(JumpParameters params, final Command callback) {
    String statementUuid = params.getUrlRef().get(RecInvoiceRegUrlParams.Create.PN_STATEMENT_UUID);
    String paymentUuid = params.getUrlRef().get(RecInvoiceRegUrlParams.Create.PN_PAYMENT_UUID);

    RLoadingDialog.show(InvoiceRegMessage.M.actionDoing(InvoiceRegMessage.M.create()));

    if (StringUtil.isNullOrBlank(statementUuid) && StringUtil.isNullOrBlank(paymentUuid))
      doCreate(callback);
    else if (StringUtil.isNullOrBlank(statementUuid) == false
        && StringUtil.isNullOrBlank(paymentUuid))
      doCreateByStatement(statementUuid, callback);
    else if (StringUtil.isNullOrBlank(statementUuid)
        && StringUtil.isNullOrBlank(paymentUuid) == false)
      doCreateByPayment(paymentUuid, callback);

  }

  private void doCreate(final Command callback) {
    RecInvoiceRegService.Locator.getService().create(new RBAsyncCallback2<BInvoiceReg>() {
      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = InvoiceRegMessage.M.actionFailed(InvoiceRegMessage.M.create(), getEP()
            .getModuleCaption());
        RMsgBox.showErrorAndBack(msg, caught);
      }

      public void onSuccess(BInvoiceReg result) {
        RLoadingDialog.hide();
        entity = result;
        callback.execute();
      }
    });
  }

  private void doCreateByStatement(String statementUuid, final Command callback) {
    RecInvoiceRegService.Locator.getService().createByStatement(statementUuid,
        DirectionType.receipt.getDirectionValue(), new RBAsyncCallback2<BInvoiceReg>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InvoiceRegMessage.M.actionFailed(InvoiceRegMessage.M.create(), getEP()
                .getModuleCaption());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          public void onSuccess(BInvoiceReg result) {
            RLoadingDialog.hide();
            entity = result;
            callback.execute();
          }
        });
  }

  private void doCreateByPayment(String paymentUuid, final Command callback) {
    RecInvoiceRegService.Locator.getService().createByPayment(paymentUuid,
        new RBAsyncCallback2<BInvoiceReg>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InvoiceRegMessage.M.actionFailed(InvoiceRegMessage.M.create(), getEP()
                .getModuleCaption());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          public void onSuccess(BInvoiceReg result) {
            RLoadingDialog.hide();
            entity = result;
            callback.execute();
          }
        });
  }

  @Override
  public void onSelection(SelectionEvent<Message> event) {
    if (event.getSelectedItem().getSource() instanceof InvoiceRegLocator) {
      InvoiceRegLocator locator = (InvoiceRegLocator) event.getSelectedItem().getSource();
      if (locator == null)
        return;
      if (locator.getLocatorType().equals(InvoiceRegLocator.lineType)) {
        RActionEvent.fire(RecInvoiceRegCreatePage.this, InvoiceRegActionName.ONSELECT_LINE,
            Integer.valueOf(locator.getLineNumber()));
      } else if (locator.getLocatorType().equals(InvoiceRegLocator.invoiceType)) {
        RActionEvent.fire(RecInvoiceRegCreatePage.this, InvoiceRegActionName.ONSELECT_INVOICE,
            Integer.valueOf(locator.getLineNumber()));
      }
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == InvoiceRegActionName.REFRESH_INVOICE) {
      invoiceTotal.setText(InvoiceRegMessage.M.resultTotal(entity.getInvoices().size()));
    }
    if (event.getActionName() == InvoiceRegActionName.REFRESH_LINE) {
      lineTotal.setText(InvoiceRegMessage.M.resultTotal(entity.getLines().size()));
    }
  }

  @Override
  public void clearValidResults() {
    generalGadget.clearValidResults();
    remarkField.clearValidResults();
    lineValidator.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return generalGadget.isValid() && remarkField.isValid() && lineValidator.isValid()
        && invoiceValidator.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> list = new ArrayList<Message>();
    list.addAll(generalGadget.getInvalidMessages());
    list.addAll(remarkField.getInvalidMessages());
    list.addAll(lineValidator.getInvalidMessages());
    list.addAll(invoiceValidator.getInvalidMessages());
    return list;
  }

  @Override
  public boolean validate() {
    getMessagePanel().clearMessages();
    boolean valid = generalGadget.validate();
    valid &= remarkField.validate();
    valid &= lineValidator.validate();
    valid &= invoiceValidator.validate();
    if (valid == false)
      getMessagePanel().putMessages(getInvalidMessages());
    return valid;
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

    if (validateConfig() == false)
      return;

    RLoadingDialog.show(InvoiceRegMessage.M.actionDoing(InvoiceRegMessage.M.save()));
    RecInvoiceRegService.Locator.getService().save(entity, getEP().getProcessCtx(),
        new RBAsyncCallback2<BInvoiceReg>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InvoiceRegMessage.M.actionFailed(InvoiceRegMessage.M.save(), getEP()
                .getModuleCaption());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BInvoiceReg result) {
            RLoadingDialog.hide();
            BInvoiceRegLogger.getInstance().log(InvoiceRegMessage.M.create(), result);

            Message msg = Message.info(InvoiceRegMessage.M.onSuccess(InvoiceRegMessage.M.save(),
                getEP().getModuleCaption(), result.toFriendlyStr()));
            getEP().jumpToViewPage(result.getUuid(), msg);
          }
        });
  }

  private boolean validateConfig() {
    BInvoiceRegConfig config = getEP().getConfig();
    if (config.getRegTotalWritable() == false)
      for (BInvoiceRegLine l : entity.getLines()) {
        if (l.getUnregTotal().equals(l.getRegTotal()) == false) {
          String msg = InvoiceRegMessage.M.notEquals(new Integer(l.getLineNumber()),
              PInvoiceRegLineDef.constants.total(), PInvoiceRegLineDef.constants.unregTotal());
          RMsgBox.showError(msg);
          return false;
        }
      }
    if (entity.getTotalDiff().compareTo(config.getTotalDiffHi()) > 0
        || entity.getTotalDiff().compareTo(config.getTotalDiffLo()) < 0) {
      String msg = InvoiceRegMessage.M.totalDiffError(entity.getTotalDiff(),
          config.getTotalDiffHi(), config.getTotalDiffLo());
      RMsgBox.showError(msg);
      return false;
    }
    if (entity.getTaxDiff().compareTo(config.getTaxDiffHi()) > 0
        || entity.getTaxDiff().compareTo(config.getTaxDiffLo()) < 0) {
      String msg = InvoiceRegMessage.M.taxDiffError(entity.getTaxDiff(), config.getTaxDiffHi(),
          config.getTaxDiffLo());
      RMsgBox.showError(msg);
      return false;
    }
    return true;
  }

  private class Handler_cancelAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RMsgBox.showConfirm(InvoiceRegMessage.M.confirmCancel(InvoiceRegMessage.M.create()),
          new RMsgBox.ConfirmCallback() {
            public void onClosed(boolean confirmed) {
              if (confirmed)
                RHistory.back();
            }
          });
    }
  }

  private class Handler_clickAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == addInvoiceAction) {
        GWTUtil.blurActiveElement();
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
          @Override
          public void execute() {
            RActionEvent.fire(RecInvoiceRegCreatePage.this, InvoiceRegActionName.ADD_INVOICE);
          }
        });
      } else if (event.getSource() == insertInvoiceAction) {
        RActionEvent.fire(RecInvoiceRegCreatePage.this, InvoiceRegActionName.INSERT_INVOICE);
      } else if (event.getSource() == deleteInvoiceAction) {
        RActionEvent.fire(RecInvoiceRegCreatePage.this, InvoiceRegActionName.BATCH_DELETE_INVOICE);
      } else if (event.getSource() == deleteLineAction) {
        RActionEvent.fire(RecInvoiceRegCreatePage.this, InvoiceRegActionName.BATCH_DELETE_LINE);
      } else if (event.getSource() == addStatementAction) {
        GWTUtil.blurActiveElement();
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
          @Override
          public void execute() {
            if (confirm())
              addAccountFromStatement();
          }
        });
      } else if (event.getSource() == addPaymentNoticeAction) {
        if (confirm())
          addAccountFromPaymentNotice();
      } else if (event.getSource() == addAccBillAction) {
        if (confirm())
          addAccountFromBill();
      } else if (event.getSource() == addAccountAction) {
        if (confirm())
          addAccount();
      }
    }
  }

  private boolean confirm() {
    if (entity.getAccountUnit() == null || entity.getAccountUnit().getUuid() == null) {
      String msg = InvoiceRegMessage.M.onSelectCounterpartFirst(getEP().getFieldCaption(
          GRes.FIELDNAME_BUSINESS, GRes.R.business()));
      RMsgBox.showError(msg);
      return false;
    } else if (entity.getCounterpart() == null || entity.getCounterpart().getUuid() == null) {
      String msg = InvoiceRegMessage.M.onSelectCounterpartFirst(getEP().getFieldCaption(
          GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
      RMsgBox.showError(msg);
      return false;
    } else
      return true;
  }

  private void addAccountFromStatement() {
    if (statementDialog == null) {
      statementDialog = new AccByStatementBrowserDialog(DirectionType.receipt.getDirectionValue(),
          new AccByStatementBrowserDialog.Callback() {

            @Override
            public void execute(List<BAccountStatement> results) {
              for (BAccountStatement result : results) {
                onRefreshValue(result.getAccounts());
              }
              lineGrid.refresh();
            }

          });
    }
    statementDialog.setAccountUnit(entity.getAccountUnit().getUuid());
    statementDialog.setCounterpart(entity.getCounterpart().getUuid());
    statementDialog.center(entity.getAccountIds());
  }

  private void addAccountFromPaymentNotice() {
    if (paymentNoticeDialog == null)
      paymentNoticeDialog = new AccByPaymentNoticeBrowserDialog(
          DirectionType.receipt.getDirectionValue(),
          new AccByPaymentNoticeBrowserDialog.Callback() {

            @Override
            public void execute(List<BAccountNotice> results) {
              for (BAccountNotice result : results) {
                onRefreshValue(result.getAccounts());
              }
              lineGrid.refresh();
            }
          });
    paymentNoticeDialog.setAccountUnit(entity.getAccountUnit().getUuid());
    paymentNoticeDialog.setCounterpart(entity.getCounterpart().getUuid());
    paymentNoticeDialog.center(entity.getAccountIds());
  }

  public void addAccountFromBill() {
    if (billDialog == null) {
      billDialog = new AccByBillBrowserDialog(DirectionType.receipt.getDirectionValue(),
          new AccByBillBrowserDialog.Callback() {

            @Override
            public void execute(List<BAccountSourceBill> results) {
              for (BAccountSourceBill reulst : results) {
                onRefreshValue(reulst.getAccounts());
              }
              lineGrid.refresh();
            }
          });
    }
    billDialog.setAccountUnit(entity.getAccountUnit().getUuid());
    billDialog.setCounterpart(entity.getCounterpart().getUuid());
    billDialog.setBillTypeMap(getEP().getBillTypeMap());
    billDialog.center(entity.getAccountIds());
  }

  public void addAccount() {
    if (dialog == null) {
      dialog = new AccBrowserDialog(DirectionType.receipt.getDirectionValue(),
          new AccBrowserDialog.Callback() {

            @Override
            public void execute(List<BAccount> results) {
              onRefreshValue(results);
              lineGrid.refresh();
            }
          });
    }
    dialog.setAccountUnit(entity.getAccountUnit().getUuid());
    dialog.setCounterpart(entity.getCounterpart().getUuid());
    dialog.setBillTypeMap(getEP().getBillTypeMap());
    dialog.center(entity.getAccountIds());
  }

  private void onRefreshValue(List<BAccount> accounts) {
    if (accounts == null || accounts.isEmpty())
      return;
    Iterator<BInvoiceRegLine> iterator = entity.getLines().iterator();
    while (iterator.hasNext()) {
      BInvoiceRegLine line = iterator.next();
      if (line.getAcc1().getSourceBill() == null
          || line.getAcc1().getSourceBill().getBillNumber() == null
          || "".equals(line.getAcc1().getSourceBill().getBillNumber().trim())) {
        iterator.remove();
      }
    }
    for (BAccount account : accounts) {
      BInvoiceRegLine line = new BInvoiceRegLine();
      line.setAcc1(account.getAcc1());
      line.setAcc2(account.getAcc2());
      line.setUnregTotal(account.getTotal().clone());
      line.setRegTotal(account.getTotal());

      entity.getLines().add(line);
    }
  }

  @Override
  protected EPRecInvoiceReg getEP() {
    return EPRecInvoiceReg.getInstance();
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
      RecInvoiceRegServiceAgent.executeTask(processCtx, operation, entity, true, this);
    }
  }

  @Override
  protected void refreshEntity() {
    assert entity != null;
    if (entity.getAccountUnit() == null && getEP().getUserStores().size() == 1) {
      entity.setAccountUnit(new BUCN(getEP().getUserStores().get(0)));
    }
    generalGadget.refresh(entity);

    lineGadget.setEntity(entity);
    lineValidator = new BInvoiceRegLineValidator(entity, lineGadget);
    lineGrid.setEntity(entity);
    lineGrid.refresh();

    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        lineLayoutMgr.restoreHeight();
      }
    });

    invoiceGadget.setEntity(entity);
    invoiceValidator = new BInvoiceRegInvoiceValidator(entity, invoiceGadget);
    invoiceGrid.setEntity(entity);
    invoiceGrid.refresh();

    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        invoiceLayoutMgr.restoreHeight();
      }
    });

    remarkField.setValue(entity.getRemark());

    addStatementAction.setEnabled(getEP().isPermitted(RecInvoiceRegPermDef.ADDFROMSTATEMENT));
    addPaymentNoticeAction.setEnabled(getEP().isPermitted(RecInvoiceRegPermDef.ADDFROMPAYNOTICE));
    addAccBillAction.setEnabled(getEP().isPermitted(RecInvoiceRegPermDef.ADDFROMSRCBILL));
    addAccountAction.setEnabled(getEP().isPermitted(RecInvoiceRegPermDef.ADDFROMACCOUNT));

    clearValidResults();
    setFocusOnFirstField();
  }

  private void setFocusOnFirstField() {
    generalGadget.focusOnFirstField();
  }
}
