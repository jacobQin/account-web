/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StatementAdjustEditPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SettleNoField;
import com.hd123.m3.account.gwt.statement.adjust.client.EPStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjustLine;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjustLogger;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjustValidator;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.QStatementLine;
import com.hd123.m3.account.gwt.statement.adjust.client.rpc.StatementAdjustLoader;
import com.hd123.m3.account.gwt.statement.adjust.client.rpc.StatementAdjustService;
import com.hd123.m3.account.gwt.statement.adjust.client.rpc.StatementAdjustServiceAgent;
import com.hd123.m3.account.gwt.statement.adjust.client.ui.gadget.StatementAdjustLineEditGadget;
import com.hd123.m3.account.gwt.statement.adjust.client.ui.gadget.StatementAdjustLineEditGrid;
import com.hd123.m3.account.gwt.statement.adjust.client.ui.gadget.StatementDialog;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustMessages;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustUrlParams.Edit;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.dd.PStatementAdjustDef;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.perm.StatementAdjustPermDef;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementDef;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmEditPage;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.DispatchLinkField;
import com.hd123.m3.commons.gwt.util.client.layout.BillLayoutManager;
import com.hd123.m3.commons.gwt.util.client.layout.LayoutButtonGroup;
import com.hd123.m3.commons.gwt.util.client.validation.LineLocator;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.history.RHistory;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RHyperlinkField;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.hotkey.RHotKey;
import com.hd123.rumba.gwt.widget2.client.hotkey.RKey;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSplitButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class StatementAdjustEditPage extends BaseBpmEditPage implements Edit, RActionHandler,
    HasRActionHandlers, SelectionHandler<Message>, HasFocusables {

  public static StatementAdjustEditPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new StatementAdjustEditPage();
    return instance;
  }

  public StatementAdjustEditPage() throws ClientBizException {
    super();
    try {
      loader = new StatementAdjustLoader();
      drawToolbar();
      drawSelf();
    } catch (Exception e) {
      throw new ClientBizException(
          StatementAdjustMessages.M.cannotCreatePage("StatementAdjustEditPage"), e);
    }
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    if (lineEditGadget != null) {
      lineEditGadget.onHideClearCondition();
    }
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == StatementAdjustLineEditGrid.ActionName.REFRESH) {
      resultTotalHtml.setText(StatementAdjustMessages.M.resultTotal(statementAdjust.getLines()
          .size()));
    }
    if (event.getActionName() == StatementAdjustLineEditGadget.ActionName.CHANGE_VALUE
        || event.getActionName() == StatementAdjustLineEditGrid.ActionName.CHANGE_LINE) {
      statementAdjust.aggregate();
      refreshTotal();
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onSelection(SelectionEvent<Message> event) {
    LineLocator o = (LineLocator) event.getSelectedItem().getSource();
    if (o != null)
      RActionEvent.fire(StatementAdjustEditPage.this, ActionName.CHANGE_LINE,
          new Integer(o.getLineNumber()));
  }

  @Override
  public Focusable getFocusable(String field) {
    return lineEditGadget.getFocusable(field);
  }

  public static class ActionName {
    /** 插入行 */
    public static final String INSERT_LINE = "insert_value_create";
    /** 新增行 */
    public static final String ADD_LINE = "add_value_create";
    /** 批量删除行 */
    public static final String BATCH_DELETE = "batch_delete_value_create";
    /** 改变行 */
    public static final String CHANGE_LINE = "change_line";
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);
    if (!checkIn()) {
      return;
    }

    loader.decoderParams(params, new Command() {
      @Override
      public void execute() {
        statementAdjust = loader.getEntity();
        validator = new BStatementAdjustValidator(StatementAdjustEditPage.this, statementAdjust);
        refresh(statementAdjust);
        getEP().appendSearchBox();
      }
    });
  }

  private static StatementAdjustEditPage instance;

  private BStatementAdjust statementAdjust;
  private BStatementAdjustValidator validator;
  private StatementAdjustLoader loader;
  private Handler_textField textFieldHandler = new Handler_textField();

  private RAction saveAction;
  private RAction cancleAction;

  private RForm basicInfoForm;
  private RViewStringField billNumberField;
  private RHyperlinkField statementNumberField;
  private RViewStringField accountUnitField;
  private DispatchLinkField contractBillNumberField;
  private RViewStringField contractNameField;
  private RViewStringField counterpartField;
  private SettleNoField settleNoField;

  private RViewNumberField receiptTotalField;
  private RViewNumberField receiptTaxField;
  private RViewNumberField paymentTotalField;
  private RViewNumberField paymentTaxField;

  private RForm operateForm;
  private RViewStringField bizStateField;
  private RViewStringField reasonField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;

  private PermGroupEditField permGroupField;

  private BillLayoutManager layoutMgr;
  private StatementAdjustLineEditGadget lineEditGadget;
  private StatementAdjustLineEditGrid lineEditGrid;
  private RAction addAction;
  private RAction insertAction;
  private RAction impFromBillAction;
  private StatementDialog statementDialog;
  private RPopupMenu addMenu;
  private RToolbarSplitButton addButton;
  private RAction deleteAction;
  private HTML resultTotalHtml;

  private RCaptionBox remarkBox;
  private RTextArea remarkField;

  private void drawToolbar() {
    saveAction = new RAction(RActionFacade.SAVE, new Handler_saveAction());
    RToolbarButton saveButton = new RToolbarButton(saveAction);
    getToolbar().add(saveButton);

    // BPM出口按钮
    injectBpmActions();

    cancleAction = new RAction(RActionFacade.CANCEL, new Handler_cancleAction());
    RToolbarButton cancleButton = new RToolbarButton(cancleAction);
    getToolbar().add(cancleButton);
  }

  private void drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    initWidget(panel);

    panel.add(drawTopPanel());
    panel.add(drawMiddlePanel());
    panel.add(drawRemarkPanel());
  }

  private Widget drawTopPanel() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    vp.add(mvp);

    mvp.add(0, drawBasicInfo());
    mvp.add(0, drawTotalPanel());
    mvp.add(1, drawOperatePanel());
    permGroupField = new PermGroupEditField(getEP().isPermEnabled(), getEP().getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    RCaptionBox box = new RCaptionBox();
    box.setEditing(true);
    box.setCaption(StatementAdjustMessages.M.generalInfo());
    box.setWidth("100%");
    box.setContent(vp);

    return box;
  }

  private Widget drawBasicInfo() {
    basicInfoForm = new RForm(1);
    basicInfoForm.setWidth("100%");

    billNumberField = new RViewStringField(PStatementAdjustDef.billNumber);
    billNumberField.addTextStyleName(RTextStyles.STYLE_BOLD);
    basicInfoForm.addField(billNumberField);

    statementNumberField = new RHyperlinkField(PStatementAdjustDef.constants.statement_billNumber());
    statementNumberField.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        if (statementAdjust == null || statementAdjust.getStatement() == null
            || statementAdjust.getStatement().getBillUuid() == null)
          return;

        GwtUrl url = StatementUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, StatementUrlParams.View.START_NODE);
        url.getQuery().set(StatementUrlParams.View.PN_UUID,
            statementAdjust.getStatement().getBillUuid());
        try {
          RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_NEW);
        } catch (Exception e) {
          String msg = StatementAdjustMessages.M.cannotNavigate(PStatementDef.TABLE_CAPTION);
          RMsgBox.showError(msg, e);
        }
      }
    });
    basicInfoForm.addField(statementNumberField);

    accountUnitField = new RViewStringField(EPStatementAdjust.getInstance().getFieldCaption(
        GRes.FIELDNAME_BUSINESS, GRes.R.business()));
    basicInfoForm.addField(accountUnitField);

    contractBillNumberField = new DispatchLinkField(PStatementAdjustDef.constants.contract_code());
    contractBillNumberField.setLinkKey(GRes.R.dispatch_key());
    basicInfoForm.addField(contractBillNumberField);

    contractNameField = new RViewStringField(PStatementAdjustDef.constants.contract_name());
    basicInfoForm.addField(contractNameField);

    counterpartField = new RViewStringField(getEP().getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    basicInfoForm.addField(counterpartField);

    settleNoField = new SettleNoField(PStatementAdjustDef.settleNo);
    settleNoField.addValueChangeHandler(new SettleNoValueChange());
    settleNoField.setMaxLength(6);
    settleNoField.refreshOption(3);
    settleNoField.setRequired(true);
    basicInfoForm.addField(settleNoField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(StatementAdjustMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(basicInfoForm);
    return box;
  }

  private Widget drawTotalPanel() {
    RForm form = new RForm(1);
    form.setWidth("100%");

    receiptTotalField = new RViewNumberField();
    receiptTotalField.setFormat(M3Format.fmt_money);
    receiptTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    receiptTaxField = new RViewNumberField();
    receiptTaxField.setFormat(M3Format.fmt_money);
    receiptTaxField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(new RCombinedField() {
      {
        setCaption(PStatementAdjustDef.constants.receiptTotal_total() + "/"
            + PStatementAdjustDef.constants.receiptTotal_tax());
        addField(receiptTotalField, 0.5f);
        addField(receiptTaxField, 0.5f);
      }
    });

    paymentTotalField = new RViewNumberField();
    paymentTotalField.setFormat(M3Format.fmt_money);
    paymentTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    paymentTaxField = new RViewNumberField();
    paymentTaxField.setFormat(M3Format.fmt_money);
    paymentTaxField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(new RCombinedField() {
      {
        setCaption(PStatementAdjustDef.constants.paymentTotal_total() + "/"
            + PStatementAdjustDef.constants.paymentTotal_tax());
        addField(paymentTotalField, 0.5f);
        addField(paymentTaxField, 0.5f);
      }
    });

    form.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(StatementAdjustMessages.M.aggregate());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private Widget drawOperatePanel() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    bizStateField = new RViewStringField(PStatementAdjustDef.bizState);
    bizStateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(bizStateField);

    reasonField = new RViewStringField();
    reasonField.setCaption(StatementAdjustMessages.M.reason());
    operateForm.addField(reasonField);

    createInfoField = new RSimpleOperateInfoField(PStatementAdjustDef.createInfo);
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(PStatementAdjustDef.lastModifyInfo);
    operateForm.addField(lastModifyInfoField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(StatementAdjustMessages.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  private Widget drawMiddlePanel() {
    layoutMgr = new BillLayoutManager();

    lineEditGrid = new StatementAdjustLineEditGrid();
    layoutMgr.setMainWidget(lineEditGrid);
    lineEditGrid.addActionHandler(this);
    this.addActionHandler(lineEditGrid);
    this.getMessagePanel().addSelectionHandler(this);

    lineEditGadget = new StatementAdjustLineEditGadget();
    layoutMgr.setSecondaryWidget(lineEditGadget);
    lineEditGadget.addActionHandler(this);

    lineEditGrid.addActionHandler(lineEditGadget);
    lineEditGadget.addActionHandler(lineEditGrid);

    RCaptionBox box = new RCaptionBox() {
      @Override
      public void maximize() {
        super.maximize();
        layoutMgr.maxToHeight(super.getContentHeight() + "px");
      }

      @Override
      public void expand() {
        super.expand();
        layoutMgr.restoreHeight();
      }
    };
    box.getCaptionBar().setShowCollapse(true);
    box.setEditing(true);
    box.setContentSpacing(0);
    box.setCaption(StatementAdjustMessages.M.statementAdjust());
    box.setContent(layoutMgr);
    box.setWidth("100%");

    resultTotalHtml = new HTML(StatementAdjustMessages.M.resultTotal(0));
    box.getCaptionBar().addButton(resultTotalHtml);
    addMenu = new RPopupMenu();
    addAction = new RAction(RActionFacade.CREATE, new Handler_addAction());
    addAction.setCaption(StatementAdjustMessages.M.addLine());
    addAction.setHotKey(new RHotKey(false, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(addAction));
    insertAction = new RAction(StatementAdjustMessages.M.insertLine(), new Handler_insertAction());
    insertAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(insertAction));
    impFromBillAction = new RAction(StatementAdjustMessages.M.impFromBill(),
        new Handler_impFromBill());
    impFromBillAction.setHotKey(null);
    addMenu.addItem(new RMenuItem(impFromBillAction));

    addButton = new RToolbarSplitButton(addAction);
    addButton.setMenu(addMenu);
    addButton.setShowText(false);
    box.getCaptionBar().addButton(addButton);

    deleteAction = new RAction(RActionFacade.DELETE, new Handler_deleteAction());
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteAction.setHotKey(null);
    deleteButton.setShowText(false);
    box.getCaptionBar().addButton(deleteButton);

    box.getCaptionBar().addButton(new RToolbarSeparator());

    LayoutButtonGroup btnGroup = new LayoutButtonGroup(layoutMgr);
    box.getCaptionBar().addButton(btnGroup);

    return box;
  }

  private Widget drawRemarkPanel() {
    remarkField = new RTextArea(PStatementAdjustDef.remark);
    remarkField.addChangeHandler(textFieldHandler);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    remarkBox = new RCaptionBox();
    remarkBox.setCaption(PStatementAdjustDef.constants.remark());
    remarkBox.setEditing(true);
    remarkBox.setWidth("100%");
    remarkBox.setContent(remarkField);
    remarkBox.getCaptionBar().setShowCollapse(true);
    return remarkBox;
  }

  private boolean checkIn() {
    if (getEP().isProcessMode()) {
      return true;
    }
    if (getEP().isPermitted(StatementAdjustPermDef.UPDATE) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void refresh() {
    if (statementAdjust == null)
      return;

    billNumberField.setValue(statementAdjust.getBillNumber());
    statementNumberField.setValue(statementAdjust.getStatement() == null ? "" : statementAdjust
        .getStatement().getBillNumber());
    accountUnitField.setValue(statementAdjust.getAccountUnit() == null ? null : statementAdjust
        .getAccountUnit().toFriendlyStr());
    if (statementAdjust.getBcontract() == null) {
      contractBillNumberField.clearValue();
    } else {
      if (statementAdjust.getCounterpart() != null
          && BCounterpart.COUNPERPART_PROPRIETOR.equals(statementAdjust.getCounterpart()
              .getCounterpartType())) {
        contractBillNumberField.setValue(GRes.FIELDNAME_LEASEBACKCONTRACT_BILLTYPE, statementAdjust
            .getBcontract().getCode());
      } else {
        contractBillNumberField.setValue(GRes.FIELDNAME_CONTRACT_BILLTYPE, statementAdjust
            .getBcontract().getCode());
      }
    }
    contractNameField.setValue(statementAdjust.getBcontract() == null ? null : statementAdjust
        .getBcontract().getName());
    counterpartField.setValue(statementAdjust.getCounterpart().toFriendlyStr(
        getEP().getCounterpartTypeMap()));
    settleNoField.setValue(statementAdjust.getSettleNo());
    remarkField.setValue(statementAdjust.getRemark());
    remarkBox.expand();

    refreshTotal();

    bizStateField.setValue(PStatementAdjustDef.bizState.getEnumCaption(statementAdjust
        .getBizState()));
    if (statementAdjust.getBpmMessage() != null) {
      reasonField.setVisible(true);
      reasonField.setValue(statementAdjust.getBpmMessage());
    } else {
      reasonField.setVisible(false);
    }
    createInfoField.setOperateInfo(statementAdjust.getCreateInfo());
    lastModifyInfoField.setOperateInfo(statementAdjust.getLastModifyInfo());
    operateForm.rebuild();

    permGroupField.setPerm(statementAdjust);

    lineEditGrid.setStatementAdjust(statementAdjust);
    lineEditGadget.setStatementAdjust(statementAdjust);
    lineEditGrid.setAccountDate(statementAdjust.getAccountRanges().isEmpty() ? null
        : statementAdjust.getAccountRanges().get(0).getBeginDate());
    lineEditGrid.refresh();
  }

  private void refreshTotal() {
    if (statementAdjust.getReceiptTotal() != null && statementAdjust.getReceiptTax() != null) {
      receiptTotalField.setValue(statementAdjust.getReceiptTotal());
      receiptTaxField.setValue(statementAdjust.getReceiptTax());
    } else {
      receiptTotalField.clearValue();
      receiptTaxField.clearValue();
    }

    if (statementAdjust.getPaymentTotal() != null) {
      paymentTotalField.setValue(statementAdjust.getPaymentTotal());
      paymentTaxField.setValue(statementAdjust.getPaymentTax());
    } else {
      paymentTotalField.clearValue();
      paymentTaxField.clearValue();
    }
  }

  private void clearValidResults() {
    lineEditGadget.clearValidResults();
    permGroupField.clearValidResults();
    remarkField.clearValidResults();
    lineEditGadget.clearValidResults();
    validator.clearValidResults();
  }

  private List<Message> getInvalidMessages() {
    List<Message> list = new ArrayList<Message>();
    list.addAll(remarkField.getInvalidMessages());
    list.addAll(permGroupField.getInvalidMessages());
    list.addAll(lineEditGadget.getInvalidMessages());
    list.addAll(validator.getInvalidMessages());
    return list;
  }

  public boolean validate() {
    clearValidResults();
    validator.clearValidResults();
    getMessagePanel().clearMessages();
    boolean valid = remarkField.validate();
    valid &= lineEditGadget.validate();
    valid &= validator.validate();
    valid &= permGroupField.validate();
    if (valid == false)
      getMessagePanel().putMessages(getInvalidMessages());
    return valid;
  }

  private class Handler_textField implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == remarkField)
        statementAdjust.setRemark(remarkField.getValue());
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

    if (!validate())
      return;

    RLoadingDialog.show(StatementAdjustMessages.M.actionDoing(StatementAdjustMessages.M.save()));
    StatementAdjustService.Locator.getService().save(statementAdjust, getEP().getProcessCtx(),
        new RBAsyncCallback2<BStatementAdjust>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementAdjustMessages.M.actionFailed(StatementAdjustMessages.M.save(),
                getEP().getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(BStatementAdjust result) {
            RLoadingDialog.hide();
            BStatementAdjustLogger.getInstance().log(StatementAdjustMessages.M.modify(), result);

            String msg = StatementAdjustMessages.M.onSuccess(StatementAdjustMessages.M.save(),
                getEP().getModuleCaption(), result.getBillNumber());
            if (getEP().isProcessMode()) {
              JumpParameters params = new JumpParameters();
              params.getUrlRef().set(BpmModuleUrlParams.KEY_ENTITY_UUID, result.getUuid());
              getEP().getProcessCtx().encodeUrl(params);
              params.getMessages().add(Message.info(msg));
              getEP().jump(params);
            } else {
              getEP().jumpToViewPage(result.getUuid(), Message.info(msg));
            }
          }
        });
  }

  private class Handler_cancleAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RMsgBox.showConfirm(
          StatementAdjustMessages.M.confirmCancel(StatementAdjustMessages.M.edit()),
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
      RActionEvent.fire(StatementAdjustEditPage.this, ActionName.ADD_LINE, "");
    }
  }

  private class Handler_insertAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RActionEvent.fire(StatementAdjustEditPage.this, ActionName.INSERT_LINE, "");
    }
  }

  private class Handler_impFromBill implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (statementAdjust.getStatement() == null) {
        RMsgBox.show(StatementAdjustMessages.M.seleteDataToAction(StatementAdjustMessages.M.imp(),
            StatementAdjustMessages.M.statement()));
        return;
      }
      if (statementDialog == null) {
        statementDialog = new StatementDialog();
        statementDialog.addOKButtonHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            List<QStatementLine> selections = statementDialog.getSelections();
            if (selections.isEmpty()) {
              RMsgBox.show(StatementAdjustMessages.M.seleteDataToAction(
                  StatementAdjustMessages.M.imp(), StatementAdjustMessages.M.statementLine()));
              return;
            }
            for (QStatementLine statementLine : selections) {
              BStatementAdjustLine adjustLine = new BStatementAdjustLine();
              adjustLine.setSubject(statementLine.getSubject());
              adjustLine.setDirection(statementLine.getDirection());
              adjustLine.setTotal(new BTotal(statementLine.getTotal(), statementLine.getTax()));
              adjustLine.setTaxRate(statementLine.getTaxRate());
              adjustLine.setInvoice(statementLine.isInvoice());
              adjustLine.setBeginDate(statementLine.getBeginDate());
              adjustLine.setEndDate(statementLine.getEndDate());
              adjustLine.setRemark(statementLine.getRemark());
              adjustLine.setSourceAccountId(statementLine.getAccId());

              if (statementAdjust.getLines().get(statementAdjust.getLines().size() - 1)
                  .getSubject() == null)
                statementAdjust.getLines().remove(statementAdjust.getLines().size() - 1);
              statementAdjust.getLines().add(adjustLine);
            }

            lineEditGrid.setStatementAdjust(statementAdjust);
            lineEditGrid.refresh();
            statementDialog.hide();
          }
        });
      }
      String uuid = statementAdjust.getStatement().getBillUuid();
      statementDialog.setSourceAccountIds(getAccIds());
      statementDialog.doLoad(uuid);

      statementDialog.center();
    }
  }

  private List<String> getAccIds() {
    List<String> accIds = new ArrayList<String>();
    for (BStatementAdjustLine line : statementAdjust.getLines()) {
      if (line.getSubject() == null || line.getSubject().getUuid() == null)
        continue;
      accIds.add(line.getSourceAccountId());
    }
    return accIds;
  }

  private class Handler_deleteAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RActionEvent.fire(StatementAdjustEditPage.this, ActionName.BATCH_DELETE, "");
    }
  }

  @Override
  public void refreshToolbar(RToolbar toolbar) {
    saveAction.setVisible(getEP().isPermitted(StatementAdjustPermDef.UPDATE));
  }

  @Override
  protected EPStatementAdjust getEP() {
    return EPStatementAdjust.getInstance();
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  @Override
  protected void refreshEntity() {
    refresh();
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        layoutMgr.restoreHeight();
        settleNoField.setFocus(true);
      }
    });
  }

  private class SettleNoValueChange implements ValueChangeHandler {

    @Override
    public void onValueChange(ValueChangeEvent event) {
      if (event.getSource() == settleNoField) {
        statementAdjust.setSettleNo(settleNoField.getValue());
      }
    }

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
      StatementAdjustServiceAgent.executeTask(operation, statementAdjust, processCtx, true, this);
    }
  }

}