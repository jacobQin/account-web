/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StatementAdjustCreatePage.java
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
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.ContractBrowseBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SettleNoField;
import com.hd123.m3.account.gwt.statement.adjust.client.EPStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjustLine;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjustLogger;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjustValidator;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.QStatement;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.QStatementLine;
import com.hd123.m3.account.gwt.statement.adjust.client.rpc.StatementAdjustService;
import com.hd123.m3.account.gwt.statement.adjust.client.rpc.StatementAdjustServiceAgent;
import com.hd123.m3.account.gwt.statement.adjust.client.ui.gadget.StatementAdjustLineEditGadget;
import com.hd123.m3.account.gwt.statement.adjust.client.ui.gadget.StatementAdjustLineEditGrid;
import com.hd123.m3.account.gwt.statement.adjust.client.ui.gadget.StatementBrowseBox;
import com.hd123.m3.account.gwt.statement.adjust.client.ui.gadget.StatementDialog;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustMessages;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustUrlParams;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustUrlParams.Create;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.dd.PStatementAdjustDef;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.perm.StatementAdjustPermDef;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmCreatePage;
import com.hd123.m3.commons.gwt.util.client.layout.BillLayoutManager;
import com.hd123.m3.commons.gwt.util.client.layout.LayoutButtonGroup;
import com.hd123.m3.commons.gwt.util.client.validation.LineLocator;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.history.RHistory;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
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
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSplitButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.TitleBar;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class StatementAdjustCreatePage extends BaseBpmCreatePage implements Create, RActionHandler,
    HasRActionHandlers, SelectionHandler<Message>, HasFocusables {

  public static StatementAdjustCreatePage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new StatementAdjustCreatePage();
    return instance;
  }

  public StatementAdjustCreatePage() throws ClientBizException {
    super();
    try {
      drawToolbar();
      drawSelf();
    } catch (Exception e) {
      throw new ClientBizException(
          StatementAdjustMessages.M.cannotCreatePage("StatementAdjustCreatePage"), e);
    }
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    if (accountUnitUCNBoxField != null) {
      accountUnitUCNBoxField.clearConditions();
    }

    if (statementNumField != null) {
      statementNumField.clearConditions();
    }

    if (contractField != null) {
      contractField.clearConditions();
    }

    if (contractField.getRawValue() != null)
      contractField.getRawValue().getMessages().clear();

    if (counterpartUCNBox != null) {
      counterpartUCNBox.clearConditions();
    }

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
      RActionEvent.fire(StatementAdjustCreatePage.this, ActionName.CHANGE_LINE,
          new Integer(o.getLineNumber()));
  }

  @Override
  public Focusable getFocusable(String field) {
    if (BStatementAdjustLine.FN_STATEMENTBILLNUM.equals(field))
      return statementNumField;
    else if (BStatementAdjustLine.FN_CONTRACTBILLNUM.equals(field))
      return contractField;
    else if (BStatementAdjustLine.FN_COUNTERPART.equals(field))
      return counterpartUCNBox;
    else
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
  public void onShow(final JumpParameters params) {
    super.onShow(params);

    if (!checkIn())
      return;

    if (!StringUtil.isNullOrBlank(decodeParams(params))) {
      doCreateByStatement(decodeParams(params));
    } else {
      doCreate(new Command() {

        @Override
        public void execute() {
          validator = new BStatementAdjustValidator(StatementAdjustCreatePage.this, statementAdjust);
          refresh(statementAdjust);
          getEP().appendSearchBox();
        }
      });
    }
  }

  private static StatementAdjustCreatePage instance = null;

  private BStatementAdjust statementAdjust;
  private BStatementAdjustValidator validator;
  boolean statementValid = false;

  private RAction saveAction;
  private RAction cancleAction;

  private Handler_textField textFieldHandler = new Handler_textField();

  private RForm basicInfoForm;
  private AccountUnitUCNBox accountUnitUCNBoxField;
  private StatementBrowseBox statementNumField;
  private ContractBrowseBox contractField;
  private RViewStringField contractNameField;

  private RCombinedField countpartField;
  private CounterpartUCNBox counterpartUCNBox;
  private RViewStringField countpartTypeField;
  private SettleNoField settleNoField;

  private RViewNumberField receiptTotalField;
  private RViewNumberField receiptTaxField;
  private RViewNumberField paymentTotalField;
  private RViewNumberField paymentTaxField;

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
    RVerticalPanel root = new RVerticalPanel();
    root.setWidth("100%");
    root.setSpacing(8);
    initWidget(root);

    Widget w = drawTopPanel();
    root.add(w);

    w = drawMiddlePanle();
    root.add(w);

    w = drawBottomPanel();
    root.add(w);
  }

  private Widget drawTopPanel() {
    RVerticalPanel topPanel = new RVerticalPanel();
    topPanel.setWidth("100%");
    topPanel.setSpacing(5);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    topPanel.add(mvp);

    mvp.add(0, drawBasicInfoPanel());
    mvp.add(0, drawTotalInfoPanel());
    permGroupField = new PermGroupEditField(getEP().isPermEnabled(), getEP().getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    RCaptionBox box = new RCaptionBox();
    box.setEditing(true);
    box.setCaption(StatementAdjustMessages.M.generalInfo());
    box.setWidth("100%");
    box.setContent(topPanel);

    return box;
  }

  private Widget drawBasicInfoPanel() {
    basicInfoForm = new RForm(1);
    basicInfoForm.setWidth("100%");

    statementNumField = new StatementBrowseBox(getEP().getCaptionMap(), getEP()
        .getCounterpartTypeMap());
    statementNumField.setFieldDef(PStatementAdjustDef.statement_billNumber);
    statementNumField.addValueChangeHandler(new StatementValueChange());
    statementNumField.getBrowser().addSelectionHandler(new SelectionHandler<QStatement>() {
      @Override
      public void onSelection(final SelectionEvent<QStatement> event) {
        if (statementAdjust.getLines().size() > 1) {
          selectAgain(event.getSelectedItem());
        } else if (statementAdjust.getLines().isEmpty() == false
            && statementAdjust.getLines().get(0).getSubject() != null) {
          selectAgain(event.getSelectedItem());
        } else {
          validateStatement(event.getSelectedItem().getStatement().getBillNumber());
          doChangeStatement(event.getSelectedItem());
        }
      }

      private void selectAgain(final QStatement statement) {
        String msg = StatementAdjustMessages.M.selectAgain();
        RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
          @Override
          public void onClosed(boolean confirmed) {
            if (confirmed == false)
              return;
            else {
              statementAdjust.getLines().clear();
              validateStatement(statement.getStatement().getBillNumber());
              doChangeStatement(statement);
            }
          }
        });
      }
    });
    statementNumField.getBrowser().addOpenHandler(new OpenHandler<PopupPanel>() {

      @Override
      public void onOpen(OpenEvent<PopupPanel> event) {
        statementNumField.setContract(statementAdjust.getBcontract() == null ? null
            : statementAdjust.getBcontract().getBillNumber());
        statementNumField.setCounterpart(statementAdjust.getCounterpart() == null ? null
            : statementAdjust.getCounterpart().getCode());
        statementNumField.setAccountUnitCode(statementAdjust.getAccountUnit() == null ? null
            : statementAdjust.getAccountUnit().getCode());
        statementNumField.setCounterpartType(statementAdjust.getCounterpart() == null ? null
            : statementAdjust.getCounterpart().getCounterpartType());
      }
    });

    basicInfoForm.addField(statementNumField);

    accountUnitUCNBoxField = new AccountUnitUCNBox();
    accountUnitUCNBoxField.setCaption(getEP().getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    accountUnitUCNBoxField.setRequired(true);
    accountUnitUCNBoxField.addValueChangeHandler(new AccountUnitValueChange());
    basicInfoForm.addField(accountUnitUCNBoxField);

    contractField = new ContractBrowseBox(PStatementAdjustDef.constants.contract_code(), true,
        new ContractBrowseBox.Callback() {

          @Override
          public void execute(BContract result) {
            if (result == null) {
              statementAdjust.setBcontract(null);
            } else {
              if (statementAdjust.getStatement() != null && statementAdjust.getBcontract() != null) {
                String msg = StatementAdjustMessages.M.selectContract();
                RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
                  @Override
                  public void onClosed(boolean confirmed) {
                    if (confirmed == false)
                      return;
                    else {
                      statementAdjust.getLines().clear();
                      statementAdjust.setStatement(null);
                      refreshGeneral();
                    }
                  }
                });
              }
              statementAdjust.setBcontract(result);
              statementNumField.setContract(result.getBillNumber());
              contractField.setValue(result.getBillNumber());
              contractField.setRawValue(result);
              contractNameField.setValue(result.getName());
            }
            onChangeContract();
          }
        }, getEP().getCaptionMap());
    contractField.setCounterTypeMap(getEP().getCounterpartTypeMap());
    contractField.getBrowser().addOpenHandler(new OpenHandler<PopupPanel>() {

      @Override
      public void onOpen(OpenEvent<PopupPanel> event) {
        contractField.setCounterpartLike(statementAdjust.getCounterpart() == null ? null
            : statementAdjust.getCounterpart().getCode());
        contractField.setAccountUnitLike(statementAdjust.getAccountUnit() == null ? null
            : statementAdjust.getAccountUnit().getCode());
        contractField.setCounterpartEqual(statementAdjust.getCounterpart() == null ? null
            : statementAdjust.getCounterpart().getCounterpartType());
      }
    });
    contractField.setRequired(true);
    basicInfoForm.addField(contractField);

    contractNameField = new RViewStringField(PStatementAdjustDef.constants.contract_name());
    basicInfoForm.addField(contractNameField);

    counterpartUCNBox = new CounterpartUCNBox(getEP().getCaptionMap());
    counterpartUCNBox.setCodeBoxWidth(Float.parseFloat("0.44"));
    counterpartUCNBox.setCounterTypeMap(getEP().getCounterpartTypeMap());
    counterpartUCNBox.setRequired(true);
    counterpartUCNBox.addChangeHandler(new CounterpartValueChange());
    countpartTypeField = new RViewStringField();

    countpartField = new RCombinedField() {
      {
        setCaption(getEP().getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
        if (getEP().getCounterpartTypeMap().size() > 1) {
          addField(counterpartUCNBox, 0.85f);
          addField(new HTML(), 0.05f);
          addField(countpartTypeField, 0.1f);
        } else {
          addField(counterpartUCNBox, 1);
        }
      }

      @Override
      public boolean validate() {
        return counterpartUCNBox.validate();
      }

      @Override
      public void clearMessages() {
        counterpartUCNBox.clearMessages();
      }

      @Override
      public boolean isValid() {
        return counterpartUCNBox.isValid();
      }

      @Override
      public List<Message> getInvalidMessages() {
        return counterpartUCNBox.getInvalidMessages();
      }
    };
    countpartField.setRequired(true);
    basicInfoForm.addField(countpartField);

    settleNoField = new SettleNoField(PStatementAdjustDef.settleNo);
    settleNoField.addValueChangeHandler(new SettleNoValueChange());
    settleNoField.setMaxLength(6);
    settleNoField.refreshOption(3);
    settleNoField.setRequired(true);
    basicInfoForm.addField(settleNoField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(StatementAdjustMessages.M.basicInfo());
    box.setWidth("100%");
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setContent(basicInfoForm);

    return box;
  }

  private Widget drawTotalInfoPanel() {
    RForm totalForm = new RForm(1);
    totalForm.setWidth("100%");

    receiptTotalField = new RViewNumberField();
    receiptTotalField.setFormat(M3Format.fmt_money);
    receiptTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    receiptTaxField = new RViewNumberField();
    receiptTaxField.setFormat(M3Format.fmt_money);
    receiptTaxField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    RCombinedField receiptField = new RCombinedField() {
      {
        setCaption(PStatementAdjustDef.constants.receiptTotal_total() + "/"
            + PStatementAdjustDef.constants.receiptTotal_tax());
        addField(receiptTotalField, 0.5f);
        addField(receiptTaxField, 0.5f);
      }
    };
    totalForm.addField(receiptField);

    paymentTotalField = new RViewNumberField();
    paymentTotalField.setFormat(M3Format.fmt_money);
    paymentTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    paymentTaxField = new RViewNumberField();
    paymentTaxField.setFormat(M3Format.fmt_money);
    paymentTaxField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    RCombinedField payField = new RCombinedField() {
      {
        setCaption(PStatementAdjustDef.constants.paymentTotal_total() + "/"
            + PStatementAdjustDef.constants.paymentTotal_tax());
        addField(paymentTotalField, 0.5f);
        addField(paymentTaxField, 0.5f);
      }
    };
    totalForm.addField(payField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(StatementAdjustMessages.M.aggregate());
    box.setWidth("100%");
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setContent(totalForm);

    return box;
  }

  private Widget drawMiddlePanle() {
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
    addAction = new RAction(RActionFacade.APPEND, new Handler_addAction());
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

  private Widget drawBottomPanel() {
    remarkField = new RTextArea(PStatementAdjustDef.remark);
    remarkField.addChangeHandler(textFieldHandler);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    remarkBox = new RCaptionBox();
    remarkBox.setEditing(true);
    remarkBox.setCaption(PStatementAdjustDef.constants.remark());
    remarkBox.setWidth("100%");
    remarkBox.setContent(remarkField);
    remarkBox.getCaptionBar().setShowCollapse(true);
    return remarkBox;

  }

  private void doCreate(final Command callback) {
    assert callback != null;

    RLoadingDialog.show(StatementAdjustMessages.M.actionDoing(StatementAdjustMessages.M.create()));
    StatementAdjustService.Locator.getService().create(new RBAsyncCallback2<BStatementAdjust>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = StatementAdjustMessages.M.actionFailed(StatementAdjustMessages.M.create(),
            getEP().getModuleCaption());
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(BStatementAdjust result) {
        RLoadingDialog.hide();
        statementAdjust = result;
        callback.execute();
      }
    });
  }

  private String decodeParams(JumpParameters params) {
    return params.getUrlRef().get(StatementAdjustUrlParams.Create.PN_STATEMENT_UUID);
  }

  private void doCreateByStatement(String statementUuid) {
    RLoadingDialog.show(StatementAdjustMessages.M.actionDoing(StatementAdjustMessages.M
        .createByStatement()));
    StatementAdjustService.Locator.getService().createByStatement(statementUuid,
        new RBAsyncCallback2<BStatementAdjust>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementAdjustMessages.M.actionFailed(
                StatementAdjustMessages.M.createByStatement(), getEP().getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(BStatementAdjust result) {
            RLoadingDialog.hide();
            statementAdjust = result;
            validator = new BStatementAdjustValidator(StatementAdjustCreatePage.this,
                statementAdjust);
            refresh(statementAdjust);
            getEP().appendSearchBox();
          }
        });
  }

  private boolean checkIn() {
    if (getEP().isBpmMode()) {
      return true;
    }
    if (getEP().isPermitted(StatementAdjustPermDef.CREATE) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  @Override
  public void refreshTitle(TitleBar titleBar) {
    titleBar.clearStandardTitle();
    titleBar.setTitleText(StatementAdjustMessages.M.create());
    titleBar.appendAttributeText(PStatementAdjustDef.TABLE_CAPTION);
  }

  private void refreshGeneral() {
    if (statementAdjust.getStatement() != null) {
      QStatement statement = new QStatement();
      statement.setStatement(statementAdjust.getStatement());
      statementNumField.setRawValue(statement);
      statementNumField.setValue(statementAdjust.getStatement().getBillNumber());
    } else {
      statementNumField.clearValue();
      statementNumField.clearRawValue();
    }

    if (statementAdjust.getAccountUnit() != null) {
      accountUnitUCNBoxField.setRawValue(statementAdjust.getAccountUnit());
    } else {
      accountUnitUCNBoxField.clearValue();
      accountUnitUCNBoxField.clearValidResults();
    }

    if (statementAdjust.getBcontract() != null) {
      BContract contract = new BContract();
      contract.setUuid(statementAdjust.getBcontract().getUuid());
      contract.setBillNumber(statementAdjust.getBcontract().getBillNumber());
      contract.setTitle(statementAdjust.getBcontract().getTitle());
      contractField.setRawValue(contract);
      contractField.setValue(statementAdjust.getBcontract().getBillNumber());
      contractNameField.setValue(statementAdjust.getBcontract().getName());
    } else {
      contractField.clearValue();
      contractField.clearRawValue();
      contractNameField.clearValue();
    }
    counterpartUCNBox.setValue(statementAdjust.getCounterpart());
    if (getEP().getCounterpartTypeMap().size() > 1)
      countpartTypeField.setValue(statementAdjust.getCounterpart() == null
          || statementAdjust.getCounterpart().getCounterpartType() == null ? null : getEP()
          .getCounterpartTypeMap().get(statementAdjust.getCounterpart().getCounterpartType()));

    if (statementAdjust.getSettleNo() == null) {
      statementAdjust.setSettleNo(SettleNoField.getCurrentSettleNo());
    }
    settleNoField.setValue(statementAdjust.getSettleNo());
    remarkField.setValue(statementAdjust.getRemark());
    remarkBox.expand();

    refreshTotal();

    permGroupField.setPerm(statementAdjust);

    lineEditGrid.setStatementAdjust(statementAdjust);
    lineEditGadget.setStatementAdjust(statementAdjust);
    lineEditGrid.refresh();

    clearValidResult();
  }

  private void refreshTotal() {
    if (statementAdjust.getReceiptTotal() != null && statementAdjust.getReceiptTax() != null) {
      receiptTotalField.setValue(statementAdjust.getReceiptTotal());
      receiptTaxField.setValue(statementAdjust.getReceiptTax());
    } else {
      receiptTotalField.clearValue();
      receiptTaxField.clearValue();
    }

    if (statementAdjust.getPaymentTotal() != null && statementAdjust.getPaymentTax() != null) {
      paymentTotalField.setValue(statementAdjust.getPaymentTotal());
      paymentTaxField.setValue(statementAdjust.getPaymentTax());
    } else {
      paymentTotalField.clearValue();
      paymentTaxField.clearValue();
    }
  }

  private void clearValidResult() {
    basicInfoForm.clearValidResults();
    permGroupField.clearValidResults();
    remarkField.clearValidResults();
    lineEditGadget.clearValidResults();
    validator.clearValidResults();
  }

  private List<Message> getInvalidMessages() {
    List<Message> list = new ArrayList<Message>();
    list.addAll(basicInfoForm.getInvalidMessages());
    list.addAll(permGroupField.getInvalidMessages());
    list.addAll(remarkField.getInvalidMessages());
    list.addAll(validator.getInvalidMessages());
    return list;
  }

  private boolean validate() {
    if (statementAdjust.getStatement() == null)
      statementNumField.clearValue();
    clearValidResult();
    validator.clearValidResults();
    getMessagePanel().clearMessages();
    boolean valid = basicInfoForm.validate();
    valid &= remarkField.validate();
    valid &= validator.validate();
    valid &= permGroupField.validate();
    if (valid == false)
      getMessagePanel().putMessages(getInvalidMessages());
    return valid;
  }

  private void doChangeStatement(QStatement statement) {
    if (statement == null)
      return;

    if (statementValid == false) {
      statementAdjust.setAccountUnit(null);
      accountUnitUCNBoxField.clearValue();
      accountUnitUCNBoxField.clearValidResults();
      statementAdjust.setCounterpart(null);
      counterpartUCNBox.clearValue();
      counterpartUCNBox.clearValidResults();
      countpartTypeField.clearValue();
      statementAdjust.setBcontract(null);
      contractField.clearValue();
      contractField.clearValidResults();
      contractNameField.clearValue();
      return;
    }

    statementNumField.setValue(statement.getStatement().getBillNumber());
    statementAdjust.setStatement(statement.getStatement());
    statementAdjust.setAccountRanges(statement.getAccountRanges());
    statementAdjust.setCounterpart(statement.getCounterpart());
    statementAdjust.setBcontract(statement.getBContract());
    statementAdjust.setAccountRanges(statement.getAccountRanges());
    statementAdjust.setAccountUnit(statement.getAccountUnit());
    // 给新增行添加默认值
    if (statement.getAccountRanges().size() > 0) {
      lineEditGrid.setAccountDate(statement.getAccountRanges().get(0).getBeginDate());
    }
    refreshGeneral();
  }

  /**
   * 验证商户项目合同数据的合法性。
   * 
   * @param statement
   */
  private void validateAccountUnitAndCounterpartAndContract(QStatement statement) {
    GWTUtil.enableSynchronousRPC();
    RLoadingDialog.show(StatementAdjustMessages.M.validate());
    StatementAdjustService.Locator.getService()
        .validateAccountUnitAndCounterpartAndContractByStatement(statement,
            new RBAsyncCallback2<String>() {

              @Override
              public void onException(Throwable caught) {
                RLoadingDialog.hide();
                String msg = StatementAdjustMessages.M.actionFailed(
                    StatementAdjustMessages.M.validate(),
                    PStatementAdjustDef.constants.accountUnit()
                        + PStatementAdjustDef.constants.counterpart()
                        + PStatementAdjustDef.constants.contract());
                RMsgBox.showErrorAndBack(msg, caught);
              }

              @Override
              public void onSuccess(String result) {
                RLoadingDialog.hide();
                if (result != null) {
                  statementValid = false;
                  RMsgBox.showError(result);
                } else {
                  statementValid = true;
                }
              }
            });
  }

  private void onChangeContract() {
    BContract contract = contractField.getRawValue();
    if (contract == null || StringUtil.isNullOrBlank(contract.getUuid()))
      return;

    statementAdjust.setCounterpart(contract.getCounterpart());
    counterpartUCNBox.setValue(statementAdjust.getCounterpart());
    if (getEP().getCounterpartTypeMap().size() > 1)
      countpartTypeField.setValue(statementAdjust.getCounterpart() == null ? null : getEP()
          .getCounterpartTypeMap().get(statementAdjust.getCounterpart().getCounterpartType()));
    statementAdjust.setAccountUnit(contract.getAccountUnit());
    statementNumField.setAccountUnitCode(contract.getAccountUnit() == null ? null : contract
        .getAccountUnit().getCode());
    accountUnitUCNBoxField.setValue(statementAdjust.getAccountUnit());
  }

  private class Handler_textField implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == remarkField) {
        statementAdjust.setRemark(remarkField.getValue());
      }
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
            BStatementAdjustLogger.getInstance().log(StatementAdjustMessages.M.create(), result);

            String msg = StatementAdjustMessages.M.onSuccess(StatementAdjustMessages.M.save(),
                getEP().getModuleCaption(), result.getBillNumber());
            getEP().jumpToViewPage(result.getUuid(), Message.info(msg));
          }
        });
  }

  private class Handler_cancleAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RMsgBox.showConfirm(
          StatementAdjustMessages.M.confirmCancel(StatementAdjustMessages.M.create()),
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
      RActionEvent.fire(StatementAdjustCreatePage.this, ActionName.ADD_LINE, "");
    }
  }

  private class Handler_insertAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RActionEvent.fire(StatementAdjustCreatePage.this, ActionName.INSERT_LINE, "");
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
              adjustLine.setAccountDate(statementLine.getAccountDate());

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

      statementDialog.setSourceAccountIds(getAccIds());
      statementDialog.doLoad(statementAdjust.getStatement().getBillUuid());
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
      RActionEvent.fire(StatementAdjustCreatePage.this, ActionName.BATCH_DELETE, "");
    }
  }

  private class StatementValueChange implements ValueChangeHandler {

    @Override
    public void onValueChange(ValueChangeEvent event) {
      final String billNumber = (String) event.getValue();
      validateStatement(billNumber);
    }
  }

  private void validateStatement(final String billNumber) {
    StatementAdjustService.Locator.getService().queryStatementByBillNum(billNumber,
        new RBAsyncCallback2<QStatement>() {

          @Override
          public void onException(Throwable caught) {
            String msg = StatementAdjustMessages.M.cannotFind(StatementAdjustMessages.M.statement());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(QStatement result) {
            if (result != null) {
              validateAccountUnitAndCounterpartAndContract(result);
              if (statementValid == false) {
                statementAdjust.setAccountUnit(null);
                accountUnitUCNBoxField.clearValue();
                accountUnitUCNBoxField.clearValidResults();
                statementAdjust.setCounterpart(null);
                counterpartUCNBox.clearValue();
                counterpartUCNBox.clearValidResults();
                countpartTypeField.clearValue();
                statementAdjust.setBcontract(null);
                contractField.clearValue();
                contractField.clearValidResults();
                contractNameField.clearValue();
              } else {
                statementAdjust.setStatement(result.getStatement());
                statementAdjust.setAccountUnit(result.getAccountUnit());
                statementAdjust.setCounterpart(result.getCounterpart());
                statementAdjust.setBcontract(result.getBContract());
                statementAdjust.setAccountRanges(result.getAccountRanges());
                // 给新增行添加默认值
                if (result.getAccountRanges().size() > 0) {
                  lineEditGrid.setAccountDate(result.getAccountRanges().get(0).getBeginDate());
                }
                refreshGeneral();
              }
            } else {
              statementNumField.addErrorMessage(StatementAdjustMessages.M
                  .cannotfindStatement(billNumber));
              statementAdjust.setStatement(null);
              statementAdjust.setCounterpart(null);
              statementAdjust.setBcontract(null);
              statementAdjust.getAccountRanges().clear();
              lineEditGrid.setAccountDate(null);
              contractField.clearValue();
              contractField.clearValidResults();
              contractNameField.clearValue();
              counterpartUCNBox.clearValue();
              countpartTypeField.clearValue();
            }
          }
        });
  }

  private class AccountUnitValueChange implements ValueChangeHandler {

    @Override
    public void onValueChange(ValueChangeEvent event) {
      if (statementAdjust.getStatement() != null) {
        String msg = StatementAdjustMessages.M.selectAccountUnit();
        RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {

          @Override
          public void onClosed(boolean confirmed) {
            if (confirmed == false)
              return;
            else {
              statementAdjust.getLines().clear();
              statementAdjust.setStatement(null);
              statementAdjust.setCounterpart(null);
              statementAdjust.setBcontract(null);
              refreshGeneral();
            }
          }
        });
      }

      BUCN accountUnit = (BUCN) accountUnitUCNBoxField.getRawValue();
      if (accountUnit == null)
        return;
      if (statementAdjust != null)
        statementAdjust.setAccountUnit(accountUnit);
      statementNumField.setAccountUnitCode(accountUnit.getCode());
    }
  }

  private class SettleNoValueChange implements ValueChangeHandler {

    @Override
    public void onValueChange(ValueChangeEvent event) {
      if (event.getSource() == settleNoField) {
        statementAdjust.setSettleNo(settleNoField.getValue());
      }
    }
  }

  private class CounterpartValueChange implements ChangeHandler {
    @Override
    public void onChange(ChangeEvent event) {
      if (counterpartUCNBox.isValid() == false) {
        countpartTypeField.clearValue();
        return;
      }

      if (statementAdjust.getStatement() != null) {
        String msg = StatementAdjustMessages.M.selectCounterpart();
        RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
          @Override
          public void onClosed(boolean confirmed) {
            if (confirmed == false)
              return;
            else {
              statementAdjust.getLines().clear();
              statementAdjust.setStatement(null);
              refreshGeneral();
            }
          }
        });
      }

      BCounterpart counterpart = counterpartUCNBox.getRawValue();

      if (counterpart == null)
        return;
      if (statementAdjust != null)
        statementAdjust.setCounterpart(counterpart);
      statementNumField.setCounterpart(counterpart.getCode());

      if (getEP().getCounterpartTypeMap().size() > 1)
        countpartTypeField.setValue(getEP().getCounterpartTypeMap().get(
            counterpart.getCounterpartType()));
      GWTUtil.enableSynchronousRPC();
      RLoadingDialog.show(StatementAdjustMessages.M.loading());
      StatementAdjustService.Locator.getService().getOnlyOneContractByCounterpart(
          counterpart.getUuid(), counterpart.getCounterpartType(), // 对方单位类型
          new RBAsyncCallback2<BContract>() {

            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = StatementAdjustMessages.M.actionFailed(StatementAdjustMessages.M.load(),
                  PStatementAdjustDef.constants.contract());
              RMsgBox.showErrorAndBack(msg, caught);

            }

            @Override
            public void onSuccess(BContract result) {
              RLoadingDialog.hide();
              if (result != null) {
                statementAdjust.setBcontract(result);
                contractField.setRawValue(result);
                contractField.setValue(result.getBillNumber());
                contractNameField.setValue(result.getName());
              } else {
                statementAdjust.setBcontract(null);
                contractField.clearValue();
                contractField.clearRawValue();
                contractField.clearValidResults();
                contractNameField.clearValue();
              }
            }
          });
    }
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
    if (statementAdjust.getAccountUnit() == null && getEP().getUserStores().size() == 1) {
      statementAdjust.setAccountUnit(new BUCN(getEP().getUserStores().get(0)));
    }
    refreshGeneral();
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        layoutMgr.restoreHeight();
        statementNumField.setFocus(true);
      }
    });
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
