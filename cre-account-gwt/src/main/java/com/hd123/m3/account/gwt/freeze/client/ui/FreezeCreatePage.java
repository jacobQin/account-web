/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	FreezeCreatePage.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-3 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.freeze.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.freeze.client.EPFreeze;
import com.hd123.m3.account.gwt.freeze.client.biz.BFreeze;
import com.hd123.m3.account.gwt.freeze.client.biz.BFreezeLine;
import com.hd123.m3.account.gwt.freeze.client.biz.BFreezeLogger;
import com.hd123.m3.account.gwt.freeze.client.rpc.FreezeService;
import com.hd123.m3.account.gwt.freeze.client.ui.gadget.AccBrowseDialog;
import com.hd123.m3.account.gwt.freeze.intf.client.FreezeMessages;
import com.hd123.m3.account.gwt.freeze.intf.client.FreezeUrlParams.Create;
import com.hd123.m3.account.gwt.freeze.intf.client.dd.PFreezeDef;
import com.hd123.m3.account.gwt.freeze.intf.client.dd.PFreezeLineDef;
import com.hd123.m3.account.gwt.freeze.intf.client.perm.FreezePermDef;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
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
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHyperlinkRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class FreezeCreatePage extends BaseContentPage implements Create {

  public static FreezeCreatePage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new FreezeCreatePage();
    return instance;
  }

  public FreezeCreatePage() throws ClientBizException {
    super();
    try {
      drawToolbar();
      drawSelf();
    } catch (Exception e) {
      throw new ClientBizException(FreezeMessages.M.cannotCreatePage("FreezeCreatePage"), e);
    }
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    if (accountUnitField != null) {
      accountUnitField.clearConditions();
    }
    if (counterpartUCNBox != null) {
      counterpartUCNBox.clearConditions();
    }
    if (accDialog != null) {
      accDialog.clearConditions();
    }

  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (!checkIn())
      return;

    doCreate(new Command() {
      @Override
      public void execute() {
        refreshTitle();
        refresh();
        refreshCommands();
        clearValidResults();
        accountUnitField.setFocus(true);
      }
    });
  }

  private static FreezeCreatePage instance;
  private EPFreeze ep = EPFreeze.getInstance();
  private BFreeze entity;
  private Handler_clickAction clickHandler = new Handler_clickAction();
  private Handler_textField textChangeHandler = new Handler_textField();

  private RAction freezeAction;
  private RAction cancleAction;

  private AccountUnitUCNBox accountUnitField;

  private RCombinedField countpartField;
  private CounterpartUCNBox counterpartUCNBox;
  private RViewStringField countpartTypeField;

  private RTextArea freezeReasonField;

  private RViewNumberField payTotalField;
  private RViewNumberField receiptTotalField;

  private PermGroupEditField permGroupField;

  private HTML resultTotalHtml;
  private RAction addAction;
  private RAction deleteAction;
  private AccBrowseDialog accDialog;
  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCodeCol;
  private RGridColumnDef subjectNameCol;
  private RGridColumnDef directionCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef taxCol;
  private RGridColumnDef statementNumCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef sourceBillNumCol;

  private void drawToolbar() {
    freezeAction = new RAction(FreezeMessages.M.freeze(), clickHandler);
    getToolbar().add(new RToolbarButton(freezeAction));

    cancleAction = new RAction(RActionFacade.CANCEL, clickHandler);
    getToolbar().add(new RToolbarButton(cancleAction));
  }

  private void drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    initWidget(panel);

    panel.add(drawTop());
    panel.add(drawSecond());
  }

  private Widget drawTop() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    vp.add(mvp);

    mvp.add(0, drawBasic());
    mvp.add(0, drawFreezeReason());
    mvp.add(1, drawAggregate());
    permGroupField = new PermGroupEditField(ep.isPermEnabled(), ep.getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(FreezeMessages.M.generalInfo());
    box.setWidth("100%");
    box.setEditing(true);
    box.setContent(vp);
    return box;
  }

  private Widget drawBasic() {
    RForm form = new RForm(1);
    form.setWidth("100%");

    accountUnitField = new AccountUnitUCNBox();
    accountUnitField.setCaption(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));
    accountUnitField.setRequired(true);
    accountUnitField.addChangeHandler(textChangeHandler);
    accountUnitField.getBrowser().addSelectionHandler(new SelectionHandler() {
      @Override
      public void onSelection(final SelectionEvent event) {
        if (entity.getLines().isEmpty() == false) {
          String msg = FreezeMessages.M.selectAccountUnit();
          RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
            @Override
            public void onClosed(boolean confirmed) {
              if (confirmed == false) {
                refresh();
                return;
              } else {
                entity.getLines().clear();
                entity.setAccountUnit((BUCN) event.getSelectedItem());
                refreshGrid();
              }
            }
          });
        } else {
          entity.setAccountUnit((BUCN) event.getSelectedItem());
        }
      }
    });
    form.addField(accountUnitField);

    counterpartUCNBox = new CounterpartUCNBox(ep.getCaptionMap());
    counterpartUCNBox.setCodeBoxWidth(Float.parseFloat("0.44"));
    counterpartUCNBox.setRequired(true);
    counterpartUCNBox.setCounterTypeMap(ep.getCounterpartTypeMap());
    counterpartUCNBox.addChangeHandler(textChangeHandler);
    counterpartUCNBox.getBrowser().addSelectionHandler(new SelectionHandler() {
      @Override
      public void onSelection(final SelectionEvent event) {
        if (entity.getLines().isEmpty() == false) {
          String msg = FreezeMessages.M.selectCounterpart();
          RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
            @Override
            public void onClosed(boolean confirmed) {
              if (confirmed == false) {
                refresh();
                return;
              } else {
                entity.getLines().clear();
                entity.setCounterpart((BCounterpart) event.getSelectedItem());
                refreshGrid();
              }
            }
          });
        } else {
          entity.setCounterpart((BCounterpart) event.getSelectedItem());
        }
      }
    });

    countpartTypeField = new RViewStringField();

    countpartField = new RCombinedField() {
      {
        setCaption(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
        if (ep.getCounterpartTypeMap().size() > 1) {
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
    form.addField(countpartField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(FreezeMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private Widget drawFreezeReason() {
    freezeReasonField = new RTextArea(PFreezeDef.freezeReason);
    freezeReasonField.addChangeHandler(textChangeHandler);
    freezeReasonField.setWidth("100%");

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(FreezeMessages.M.freezeReason());
    box.setWidth("100%");
    box.setContent(freezeReasonField);
    return box;
  }

  private Widget drawAggregate() {
    RForm form = new RForm(1);
    form.setWidth("100%");

    payTotalField = new RViewNumberField(PFreezeDef.paymentTotal_total);
    payTotalField.setFormat(M3Format.fmt_money);
    payTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
    form.addField(payTotalField);

    receiptTotalField = new RViewNumberField(PFreezeDef.receiptTotal_total);
    receiptTotalField.setFormat(M3Format.fmt_money);
    receiptTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
    form.addField(receiptTotalField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(FreezeMessages.M.aggregate());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private Widget drawSecond() {
    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(5);

    panel.add(drawGrid());

    RCaptionBox box = new RCaptionBox();
    box.setWidth("100%");
    box.getCaptionBar().setShowCollapse(true);
    box.setEditing(true);
    box.setContent(panel);
    box.setCaption(FreezeMessages.M.accountLine());

    resultTotalHtml = new HTML(FreezeMessages.M.resultTotal(0));
    box.getCaptionBar().addButton(resultTotalHtml);

    addAction = new RAction(RActionFacade.APPEND, clickHandler);
    addAction.setHotKey(null);
    RToolbarButton addButton = new RToolbarButton(addAction);
    addButton.setShowText(false);
    box.getCaptionBar().addButton(addButton);

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    deleteAction.setHotKey(null);
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    box.getCaptionBar().addButton(deleteButton);

    return box;
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.setProvider(new GridDateProvider());
    grid.addClickHandler(new Handler_grid());

    lineNumberCol = new RGridColumnDef(PFreezeLineDef.lineNumber);
    lineNumberCol.setWidth("40px");
    grid.addColumnDef(lineNumberCol);

    subjectCodeCol = new RGridColumnDef(PFreezeLineDef.acc1_subject_code);
    subjectCodeCol.setRendererFactory(new RHyperlinkRendererFactory());
    subjectCodeCol.setWidth("100px");
    grid.addColumnDef(subjectCodeCol);

    subjectNameCol = new RGridColumnDef(PFreezeLineDef.acc1_subject_name);
    subjectNameCol.setWidth("100px");
    grid.addColumnDef(subjectNameCol);

    directionCol = new RGridColumnDef(PFreezeLineDef.acc1_direction);
    directionCol.setWidth("80px");
    grid.addColumnDef(directionCol);

    totalCol = new RGridColumnDef(PFreezeLineDef.total_total);
    totalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    totalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    totalCol.setWidth("80px");
    grid.addColumnDef(totalCol);

    taxCol = new RGridColumnDef(PFreezeLineDef.total_tax);
    taxCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    taxCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    taxCol.setWidth("80px");
    grid.addColumnDef(taxCol);

    statementNumCol = new RGridColumnDef(PFreezeLineDef.acc2_statement_billUuid);
    statementNumCol.setRendererFactory(new RHyperlinkRendererFactory());
    statementNumCol.setWidth("160px");
    grid.addColumnDef(statementNumCol);

    sourceBillTypeCol = new RGridColumnDef(PFreezeLineDef.acc1_sourceBill_billType);
    sourceBillTypeCol.setWidth("160px");
    grid.addColumnDef(sourceBillTypeCol);

    sourceBillNumCol = new RGridColumnDef(PFreezeLineDef.acc1_sourceBill_billUuid);
    sourceBillNumCol.setWidth("160px");
    sourceBillNumCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    grid.addColumnDef(sourceBillNumCol);

    grid.setAllColumnsOverflowEllipsis(true);

    return grid;
  }

  private boolean checkIn() {
    if (ep.isPermitted(FreezePermDef.CREATE) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  private void doCreate(final Command callback) {
    assert callback != null;

    RLoadingDialog.show(FreezeMessages.M.actionDoing(FreezeMessages.M.create()));

    FreezeService.Locator.getService().create(new RBAsyncCallback2<BFreeze>() {
      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = FreezeMessages.M.actionFailed(FreezeMessages.M.create(), ep.getModuleCaption());
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(BFreeze result) {
        RLoadingDialog.hide();
        entity = result;
        callback.execute();
      }
    });
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(FreezeMessages.M.create());
    ep.getTitleBar().appendAttributeText(ep.getModuleCaption());
  }

  private void refreshCommands() {
    freezeAction.setEnabled(ep.isPermitted(FreezePermDef.FREEZE));
  }

  private void refresh() {
    if (entity.getAccountUnit() == null && ep.getUserStores().size() == 1) {
      entity.setAccountUnit(new BUCN(ep.getUserStores().get(0)));
    }
    if (entity.getAccountUnit() != null) {
      accountUnitField.setValue(entity.getAccountUnit());
    } else {
      accountUnitField.clearValue();
      accountUnitField.clearValidResults();
    }

    if (entity.getCounterpart() != null) {
      counterpartUCNBox.setValue(entity.getCounterpart());
      if (ep.getCounterpartTypeMap().size() > 1) {
        countpartTypeField.setValue(ep.getCounterpartTypeMap().get(
            entity.getCounterpart().getCounterpartType()));
      }
    } else {
      counterpartUCNBox.clearValue();
      counterpartUCNBox.clearValidResults();
      if (ep.getCounterpartTypeMap().size() > 1)
        countpartTypeField.clearValue();
    }

    if (entity.getFreezeReason() != null)
      freezeReasonField.setValue(entity.getFreezeReason());
    else {
      freezeReasonField.clearValue();
      freezeReasonField.clearValidResults();
    }

    refreshAggregate();
    permGroupField.setPerm(entity);
    refreshGrid();
  }

  private void refreshAggregate() {
    entity.aggregate();
    payTotalField.setValue(entity.getFreezePayTotal());
    receiptTotalField.setValue(entity.getFreezeRecTotal());
  }

  private void refreshGrid() {
    if (!entity.getLines().isEmpty())
      resultTotalHtml.setText(FreezeMessages.M.resultTotal(entity.getLines().size()));
    else
      resultTotalHtml.setText(FreezeMessages.M.resultTotal(0));
    grid.refresh();
  }

  private class Handler_clickAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == freezeAction) {
        doFreeze(freezeReasonField.getValue());
      } else if (event.getSource() == cancleAction) {
        doCancel();
      } else if (event.getSource() == addAction) {
        doAdd();
      } else if (event.getSource() == deleteAction) {
        doDeleteRows();
      }
    }
  }

  private void doFreeze(String text) {
    GWTUtil.blurActiveElement();

    if (validate() == false)
      return;

    RLoadingDialog.show(FreezeMessages.M.actionDoing(FreezeMessages.M.freeze()));
    FreezeService.Locator.getService().freeze(getAccUuids(), entity, text,
        new RBAsyncCallback2<BFreeze>() {
          @Override
          public void onException(Throwable t) {
            RLoadingDialog.hide();
            String msg = FreezeMessages.M.actionFailed(FreezeMessages.M.freeze(),
                ep.getModuleCaption());
            RMsgBox.showError(msg, t);
          }

          @Override
          public void onSuccess(BFreeze result) {
            RLoadingDialog.hide();
            BFreezeLogger.getInstance().log(FreezeMessages.M.freeze(), result);

            String msg = FreezeMessages.M.actionSuccess(FreezeMessages.M.freeze(),
                ep.getModuleCaption());
            ep.jumpToViewPage(result, Message.info(msg));
          }
        });
  }

  private boolean validate() {
    clearValidResults();
    getMessagePanel().clearMessages();
    boolean valid = accountUnitField.validate();
    valid &= counterpartUCNBox.validate();
    valid &= freezeReasonField.validate();
    valid &= permGroupField.validate();
    valid &= entity.validateLine();
    if (!valid)
      getMessagePanel().putMessages(getInvalidMessages());
    return valid;
  }

  private void clearValidResults() {
    accountUnitField.clearValidResults();
    counterpartUCNBox.clearValidResults();
    freezeReasonField.clearValidResults();
    permGroupField.clearValidResults();
    entity.clearValidResults();
  }

  private List<Message> getInvalidMessages() {
    List<Message> messages = new ArrayList<Message>();
    messages.addAll(accountUnitField.getInvalidMessages());
    messages.addAll(counterpartUCNBox.getInvalidMessages());
    messages.addAll(freezeReasonField.getInvalidMessages());
    messages.addAll(permGroupField.getInvalidMessages());
    messages.addAll(entity.getMessages());
    return messages;
  }

  private List<String> getAccUuids() {
    List<String> accUuids = new ArrayList<String>();

    for (BFreezeLine line : entity.getLines()) {
      accUuids.add(line.getAccountUuid());
    }
    return accUuids;
  }

  private void doCancel() {
    RMsgBox.showConfirm(
        FreezeMessages.M.actionComfirm(FreezeMessages.M.cancel(), FreezeMessages.M.create()),
        new RMsgBox.ConfirmCallback() {
          public void onClosed(boolean confirmed) {
            if (confirmed)
              RHistory.back();
          }
        });
  }

  private void doAdd() {
    if (accountUnitField.getValue() == null
        || StringUtil.isNullOrBlank(accountUnitField.getValue().getUuid())) {
      RMsgBox.show(FreezeMessages.M.seleteData(GRes.R.business()));
      return;
    }

    if (counterpartUCNBox.getValue() == null
        || StringUtil.isNullOrBlank(counterpartUCNBox.getValue().getUuid())) {
      RMsgBox.show(FreezeMessages.M.seleteData(GRes.R.counterpart()));
      return;
    }

    if (accDialog == null) {
      accDialog = new AccBrowseDialog();
      accDialog.addValueChangeHandler(new ValueChangeHandler<List<BAccount>>() {
        @Override
        public void onValueChange(ValueChangeEvent<List<BAccount>> event) {
          if (event.getValue().isEmpty() == false) {
            for (BAccount account : event.getValue()) {
              entity.getLines().add(convertFreezeLine(account));
            }

            refreshLineNumber();
            refreshGrid();
            refreshAggregate();
          }
        }
      });
    }
    accDialog.setAccUuids(getAccUuids());
    accDialog.setCounterpartUuid(entity.getCounterpart().getUuid());
    accDialog.setCounterpartType(entity.getCounterpart().getCounterpartType());
    accDialog.setAccountUnitUuid(entity.getAccountUnit().getUuid());
    accDialog.center();
  }

  private BFreezeLine convertFreezeLine(BAccount source) {
    BFreezeLine target = new BFreezeLine();
    target.setAcc1(source.getAcc1());
    target.setAcc2(source.getAcc2());
    target.setTotal(source.getTotal().getTotal());
    target.setTax(source.getTotal().getTax());
    target.setAccountUuid(source.getUuid());
    return target;
  }

  private void refreshLineNumber() {
    if (entity.getLines().isEmpty() == false) {
      for (int i = 0; i < entity.getLines().size(); i++) {
        BFreezeLine line = entity.getLines().get(i);
        line.setLineNumber(i + 1);
      }
    }
  }

  private void doDeleteRows() {
    if (grid.getSelections().isEmpty()) {
      RMsgBox.showError(FreezeMessages.M.seleteDataToAction(FreezeMessages.M.delete(),
          FreezeMessages.M.accountLine()));
      return;
    }

    List<Integer> selections = grid.getSelections();
    for (int i = selections.size() - 1; i >= 0; i--) {
      Integer row = selections.get(i);
      int dataRow = row.intValue();
      entity.getLines().remove(dataRow);
    }

    refreshLineNumber();
    refreshGrid();
    refreshAggregate();
  }

  private class Handler_textField implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == counterpartUCNBox) {
        entity.setCounterpart(counterpartUCNBox.getRawValue());
        if (ep.getCounterpartTypeMap().size() > 1) {
          String counterpartType = counterpartUCNBox.getRawValue() == null ? null
              : counterpartUCNBox.getRawValue().getCounterpartType();
          countpartTypeField.setValue(counterpartType == null ? null : ep.getCounterpartTypeMap()
              .get(counterpartType));
        }
      } else if (event.getSource() == accountUnitField) {
        entity.setAccountUnit(accountUnitField.getValue());
      }
    }
  }

  private class GridDateProvider implements RGridDataProvider {

    @Override
    public int getRowCount() {
      return entity == null ? 0 : entity.getLines().size();
    }

    @Override
    public Object getData(int row, int col) {
      if (entity == null || entity.getLines().isEmpty())
        return null;

      BFreezeLine rowData = entity.getLines().get(row);
      if (col == lineNumberCol.getIndex())
        return rowData.getLineNumber();
      else if (col == subjectCodeCol.getIndex())
        return rowData.getAcc1() != null && rowData.getAcc1().getSubject() != null ? rowData
            .getAcc1().getSubject().getCode() : null;
      else if (col == subjectNameCol.getIndex())
        return rowData.getAcc1() != null && rowData.getAcc1().getSubject() != null ? rowData
            .getAcc1().getSubject().getName() : null;
      else if (col == directionCol.getIndex())
        return rowData.getAcc1() != null ? DirectionType.getCaptionByValue(rowData.getAcc1()
            .getDirection()) : null;
      else if (col == totalCol.getIndex())
        return rowData.getTotal().doubleValue();
      else if (col == taxCol.getIndex())
        return rowData.getTax().doubleValue();
      else if (col == statementNumCol.getIndex())
        return rowData.getAcc2() != null && rowData.getAcc2().getStatement() != null ? getStatementNumber(rowData
            .getAcc2().getStatement()) : null;
      else if (col == sourceBillTypeCol.getIndex())
        return rowData.getAcc1() != null && rowData.getAcc1().getSourceBill() != null
            && rowData.getAcc1().getSourceBill().getBillType() != null ? ep.getBillType().get(
            rowData.getAcc1().getSourceBill().getBillType()) : null;
      else if (col == sourceBillNumCol.getIndex()) {
        if (rowData.getAcc1() == null || rowData.getAcc1().getSourceBill() == null)
          return null;
        BDispatch dispatch = new BDispatch(rowData.getAcc1().getSourceBill().getBillType());
        dispatch
            .addParams(GRes.R.dispatch_key(), rowData.getAcc1().getSourceBill().getBillNumber());
        return dispatch;
      }
      return null;
    }
  }

  private String getStatementNumber(BBill bill) {
    if (FreezeMessages.M.noneBillNumber().equals(bill.getBillNumber()))
      return null;
    else
      return bill.getBillNumber();
  }

  private class Handler_grid implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      RGridColumnDef colDef = cell.getColumnDef();
      int row = cell.getRow();

      BFreezeLine line = entity.getLines().get(row);
      if (line == null)
        return;

      if (colDef.equals(subjectCodeCol)) {
        GwtUrl url = SubjectUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
        url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID,
            line.getAcc1().getSubject().getUuid());
        try {
          RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
        } catch (Exception e) {
          String msg = FreezeMessages.M.cannotNavigate(url.toString());
          RMsgBox.showError(msg, e);
        }
      } else if (colDef.equals(statementNumCol)) {
        if (!"-".equals(line.getAcc2().getStatement().getBillNumber())) {
          GwtUrl url = StatementUrlParams.ENTRY_URL;
          url.getQuery().set(JumpParameters.PN_START, StatementUrlParams.View.START_NODE);
          url.getQuery().set(StatementUrlParams.View.PN_UUID,
              line.getAcc2().getStatement().getBillUuid());
          try {
            RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
          } catch (Exception e) {
            String msg = FreezeMessages.M.cannotNavigate(url.toString());
            RMsgBox.showError(msg, e);
          }
        }
      }
    }
  }

}
