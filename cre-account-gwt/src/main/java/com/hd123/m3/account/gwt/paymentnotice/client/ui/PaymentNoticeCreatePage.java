/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentNoticeCreatePage.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-15 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.ui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.paymentnotice.client.EPPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.PaymentNoticeMessages;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNoticeLine;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNoticeLogger;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.QStatement;
import com.hd123.m3.account.gwt.paymentnotice.client.rpc.PaymentNoticeService;
import com.hd123.m3.account.gwt.paymentnotice.client.rpc.PaymentNoticeServiceAgent;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.gadget.StatementBrowseDialog;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.PaymentNoticeUrlParams.Create;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.dd.PPaymentNoticeDef;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.dd.PPaymentNoticeLineDef;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.perm.PaymentNoticePermDef;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmCreatePage;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.base.client.BUCN;
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
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.hotkey.RHotKey;
import com.hd123.rumba.gwt.widget2.client.hotkey.RKey;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.TitleBar;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class PaymentNoticeCreatePage extends BaseBpmCreatePage implements Create {

  public static PaymentNoticeCreatePage getInstance() {
    if (instance == null)
      instance = new PaymentNoticeCreatePage();
    return instance;
  }

  public PaymentNoticeCreatePage() {
    super();
    drawToolbar();
    drawSelf();
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
    if (statementDialog != null) {
      statementDialog.clearConditions();
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
        refresh(entity);
        getEP().appendSearchBox();
      }
    });
  }

  private static PaymentNoticeCreatePage instance;
  private BPaymentNotice entity;
  private Handler_click clickHandler = new Handler_click();
  private Handler_textField textClickHandler = new Handler_textField();

  private RAction saveAction;
  private RAction cancleAction;

  private RForm basicForm;
  private AccountUnitUCNBox accountUnitField;
  private RCombinedField countpartField;
  private CounterpartUCNBox counterpartUCNBox;
  private RViewStringField countpartTypeField;

  private RViewNumberField receiptTotalField;
  private RViewNumberField receiptTaxField;
  private RViewNumberField receiptIvcTotalField;
  private RViewNumberField paymentTotalField;
  private RViewNumberField paymentTaxField;
  private RViewNumberField paymentIvcTotalField;

  private PermGroupEditField permGroupField;

  private HTML resultTotalHtml;
  private RAction addAction;
  private StatementBrowseDialog statementDialog;
  private RAction deleteAction;

  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef statementNumCol;
  private RGridColumnDef contractNumCol;
  private RGridColumnDef contractNameCol;
  private RGridColumnDef receiptTotalCol;
  private RGridColumnDef receiptTaxCol;
  private RGridColumnDef receiptIvcTotalCol;
  private RGridColumnDef payTotalCol;
  private RGridColumnDef payTaxCol;
  private RGridColumnDef payIvcTotalCol;
  private RGridColumnDef remarkCol;

  private RCaptionBox remarkBox;
  private RTextArea remarkField;

  private void drawToolbar() {
    saveAction = new RAction(RActionFacade.SAVE, clickHandler);
    getToolbar().add(new RToolbarButton(saveAction));

    // BPM出口按钮
    injectBpmActions();

    cancleAction = new RAction(RActionFacade.CANCEL, clickHandler);
    getToolbar().add(new RToolbarButton(cancleAction));
  }

  private void drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    initWidget(panel);

    panel.add(drawTop());
    panel.add(drawMiddle());
    panel.add(drawBottom());
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
    mvp.add(0, drawAggregate());

    permGroupField = new PermGroupEditField(getEP().isPermEnabled(), getEP().getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PaymentNoticeMessages.M.generalInfo());
    box.setWidth("100%");
    box.setEditing(true);
    box.setContent(vp);
    return box;
  }

  private Widget drawBasic() {
    basicForm = new RForm(1);
    basicForm.setWidth("100%");

    accountUnitField = new AccountUnitUCNBox();
    accountUnitField.addChangeHandler(textClickHandler);
    accountUnitField
        .setCaption(getEP().getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));
    accountUnitField.setRequired(true);
    basicForm.addField(accountUnitField);

    counterpartUCNBox = new CounterpartUCNBox(getEP().getCaptionMap());
    counterpartUCNBox.setCodeBoxWidth(Float.parseFloat("0.44"));
    counterpartUCNBox.addChangeHandler(textClickHandler);
    counterpartUCNBox.setRequired(true);
    counterpartUCNBox.setCounterTypeMap(getEP().getCounterpartTypeMap());
    
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
    basicForm.addField(countpartField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(PaymentNoticeMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(basicForm);
    return box;

  }

  private Widget drawAggregate() {
    RForm form = new RForm(1);
    form.setWidth("50%");

    receiptTotalField = new RViewNumberField();
    receiptTotalField.setFormat(M3Format.fmt_money);
    receiptTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    receiptTaxField = new RViewNumberField();
    receiptTaxField.setFormat(M3Format.fmt_money);
    receiptTaxField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    form.addField(new RCombinedField() {
      {
        setCaption(PPaymentNoticeDef.constants.receiptTotal_total() + "/"
            + PPaymentNoticeDef.constants.receiptTotal_tax());
        addField(receiptTotalField, 0.5f);
        addField(receiptTaxField, 0.5f);
      }
    });

    receiptIvcTotalField = new RViewNumberField();
    receiptIvcTotalField.setFormat(M3Format.fmt_money);
    receiptIvcTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(new RCombinedField() {
      {
        setCaption(PPaymentNoticeDef.constants.receiptIvcTotal_total());
        addField(receiptIvcTotalField, 0.5f);
        addField(new HTML(), 0.5f);
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
        setCaption(PPaymentNoticeDef.constants.paymentTotal_total() + "/"
            + PPaymentNoticeDef.constants.paymentTotal_tax());
        addField(paymentTotalField, 0.5f);
        addField(paymentTaxField, 0.5f);
      }
    });

    paymentIvcTotalField = new RViewNumberField();
    paymentIvcTotalField.setFormat(M3Format.fmt_money);
    paymentIvcTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(new RCombinedField() {
      {
        setCaption(PPaymentNoticeDef.constants.paymentIvcTotal_total());
        addField(paymentIvcTotalField, 0.5f);
        addField(new HTML(), 0.5f);
      }
    });

    form.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(PaymentNoticeMessages.M.aggregate());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private Widget drawMiddle() {
    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(5);

    panel.add(drawGrid());

    RCaptionBox box = new RCaptionBox();
    box.setWidth("100%");
    box.getCaptionBar().setShowCollapse(true);
    box.setEditing(true);
    box.setContent(panel);
    box.setCaption(PaymentNoticeMessages.M.statementLine());

    resultTotalHtml = new HTML(PaymentNoticeMessages.M.resultTotal(0));
    box.getCaptionBar().addButton(resultTotalHtml);

    addAction = new RAction(RActionFacade.APPEND, clickHandler);
    addAction.setHotKey(null);
    RToolbarButton addButton = new RToolbarButton(addAction);
    addButton.setShowText(false);
    box.getCaptionBar().addButton(addButton);

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    deleteAction.setHotKey(new RHotKey(false, false, false, RKey.KEY_CODE_DELETE));
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

    lineNumberCol = new RGridColumnDef(PPaymentNoticeLineDef.lineNumber);
    lineNumberCol.setWidth("40px");
    grid.addColumnDef(lineNumberCol);

    statementNumCol = new RGridColumnDef(PPaymentNoticeLineDef.statement_billNumber);
    statementNumCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    statementNumCol.setWidth("160px");
    grid.addColumnDef(statementNumCol);

    contractNumCol = new RGridColumnDef(PPaymentNoticeLineDef.contract_code);
    contractNumCol.setWidth("160px");
    grid.addColumnDef(contractNumCol);

    contractNameCol = new RGridColumnDef(PPaymentNoticeLineDef.contract_name);
    contractNameCol.setWidth("160px");
    grid.addColumnDef(contractNameCol);

    receiptTotalCol = new RGridColumnDef(PPaymentNoticeLineDef.receiptTotal_total);
    receiptTotalCol.setWidth("90px");
    receiptTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    receiptTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    grid.addColumnDef(receiptTotalCol);

    receiptTaxCol = new RGridColumnDef(PPaymentNoticeLineDef.receiptTotal_tax);
    receiptTaxCol.setWidth("90px");
    receiptTaxCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    receiptTaxCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    grid.addColumnDef(receiptTaxCol);

    receiptIvcTotalCol = new RGridColumnDef(PPaymentNoticeLineDef.receiptIvcTotal_total);
    receiptIvcTotalCol.setWidth("100px");
    receiptIvcTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    receiptIvcTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    grid.addColumnDef(receiptIvcTotalCol);

    payTotalCol = new RGridColumnDef(PPaymentNoticeLineDef.paymentTotal_total);
    payTotalCol.setWidth("90px");
    payTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    payTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    grid.addColumnDef(payTotalCol);

    payTaxCol = new RGridColumnDef(PPaymentNoticeLineDef.paymentTotal_tax);
    payTaxCol.setWidth("90px");
    payTaxCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    payTaxCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    grid.addColumnDef(payTaxCol);

    payIvcTotalCol = new RGridColumnDef(PPaymentNoticeLineDef.paymentIvcTotal_total);
    payIvcTotalCol.setWidth("100px");
    payIvcTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    payIvcTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    grid.addColumnDef(payIvcTotalCol);

    remarkCol = new RGridColumnDef(PPaymentNoticeLineDef.remark);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);

    return grid;
  }

  private Widget drawBottom() {
    remarkField = new RTextArea(PPaymentNoticeDef.remark);
    remarkField.addChangeHandler(textClickHandler);
    remarkField.setWidth("100%");

    remarkBox = new RCaptionBox();
    remarkBox.setWidth("100%");
    remarkBox.setContent(remarkField);
    remarkBox.getCaptionBar().setShowCollapse(true);
    remarkBox.setEditing(true);
    remarkBox.setCaption(PaymentNoticeMessages.M.remark());

    return remarkBox;
  }

  private boolean checkIn() {
    if (getEP().isBpmMode())
      return true;
    if (getEP().isPermitted(PaymentNoticePermDef.CREATE) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void doCreate(final Command callback) {
    assert callback != null;

    RLoadingDialog.show(PaymentNoticeMessages.M.actionDoing(PaymentNoticeMessages.M.create()));
    PaymentNoticeService.Locator.getService().create(new RBAsyncCallback2<BPaymentNotice>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = PaymentNoticeMessages.M.actionFailed(PaymentNoticeMessages.M.create(), getEP()
            .getModuleCaption());
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(BPaymentNotice result) {
        RLoadingDialog.hide();
        entity = result;
        callback.execute();
      }
    });
  }

  @Override
  public void refreshTitle(TitleBar titleBar) {
    titleBar.clearStandardTitle();
    titleBar.setTitleText(PaymentNoticeMessages.M.create());
    titleBar.appendAttributeText(getEP().getModuleCaption());
  }

  private void refresh() {
    accountUnitField.setValue(entity.getAccountUnit());
    counterpartUCNBox.setValue(entity.getCounterpart());
    if (getEP().getCounterpartTypeMap().size() > 1)
      countpartTypeField.setValue(entity.getCounterpart() == null ? null : getEP()
          .getCounterpartTypeMap().get(entity.getCounterpart().getCounterpartType()));
    permGroupField.setPerm(entity);
    remarkField.setValue(entity.getRemark());
    remarkBox.expand();

    refreshAggregate();
    refreshGrid();

    clearValidResults();
  }

  private void refreshAggregate() {
    entity.aggregate();
    receiptTotalField.setValue(entity.getReceiptTotal());
    receiptTaxField.setValue(entity.getReceiptTax());
    receiptIvcTotalField.setValue(entity.getReceiptIvcTotal());
    paymentTotalField.setValue(entity.getPaymentTotal());
    paymentTaxField.setValue(entity.getPaymentTax());
    paymentIvcTotalField.setValue(entity.getPaymentIvcTotal());
  }

  private boolean validate() {
    clearValidResults();

    getMessagePanel().clearMessages();
    boolean valid = basicForm.validate();
    valid &= permGroupField.validate();
    valid &= remarkField.validate();
    valid &= entity.validateLine();
    if (!valid)
      getMessagePanel().putMessages(getInvalidMessages());
    return valid;
  }

  private void clearValidResults() {
    basicForm.clearValidResults();
    permGroupField.clearValidResults();
    remarkField.clearValidResults();
    entity.clearValidResults();
  }

  private List<Message> getInvalidMessages() {
    List<Message> messages = new ArrayList<Message>();
    messages.addAll(basicForm.getInvalidMessages());
    messages.addAll(permGroupField.getInvalidMessages());
    messages.addAll(remarkField.getInvalidMessages());
    messages.addAll(entity.getInvalidMessages());
    return messages;
  }

  private class Handler_click implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == saveAction) {
        doSave();
      } else if (event.getSource() == cancleAction) {
        doCancel();
      } else if (event.getSource() == addAction) {
        doAdd();
      } else if (event.getSource() == deleteAction) {
        doDeleteRows();
      }
    }
  }

  private class Handler_textField implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == accountUnitField) {
        doChangeAccountUnitConfirm();
      } else if (event.getSource() == counterpartUCNBox) {
        doChangeCounterpart();
      } else if (event.getSource() == remarkField) {
        entity.setRemark(remarkField.getValue());
      }
    }
  }

  private void doChangeAccountUnitConfirm() {
    if (entity.getLines().isEmpty() == false) {
      String msg = PaymentNoticeMessages.M.changeConfirm(getEP().getFieldCaption(
          GRes.FIELDNAME_BUSINESS, GRes.R.business()));
      RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
        @Override
        public void onClosed(boolean confirmed) {
          if (confirmed == false) {
            refresh();
            return;
          } else {
            entity.setAccountUnit(accountUnitField.getValue());
            entity.getLines().clear();
            refresh();
          }
        }
      });
    } else {
      entity.setAccountUnit(accountUnitField.getValue());
    }
  }

  private void doChangeCounterpart() {
    if (entity.getLines().isEmpty() == false) {
      String msg = PaymentNoticeMessages.M.changeConfirm(getEP().getFieldCaption(
          GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
      RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
        @Override
        public void onClosed(boolean confirmed) {
          if (confirmed == false) {
            refresh();
            return;
          } else {
            entity.setCounterpart(counterpartUCNBox.getRawValue());
            if (getEP().getCounterpartTypeMap().size() > 1)
              countpartTypeField.setValue(counterpartUCNBox.getRawValue() == null ? null : getEP()
                  .getCounterpartTypeMap()
                  .get(counterpartUCNBox.getRawValue().getCounterpartType()));
            entity.getLines().clear();
            refresh();
          }
        }
      });
    } else {
      entity.setCounterpart(counterpartUCNBox.getRawValue());
      if (getEP().getCounterpartTypeMap().size() > 1)
        countpartTypeField.setValue(counterpartUCNBox.getRawValue() == null ? null : getEP()
            .getCounterpartTypeMap().get(counterpartUCNBox.getRawValue().getCounterpartType()));
    }
  }

  private void doCancel() {
    RMsgBox.showConfirm(PaymentNoticeMessages.M.actionComfirm(PaymentNoticeMessages.M.cancel(),
        PaymentNoticeMessages.M.create()), new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed)
          RHistory.back();
      }
    });
  }

  private void doSave() {
    GWTUtil.blurActiveElement();

    if (!validate())
      return;

    RLoadingDialog.show(PaymentNoticeMessages.M.actionDoing(PaymentNoticeMessages.M.save()));
    PaymentNoticeService.Locator.getService().save(entity, getEP().getProcessCtx(),
        new RBAsyncCallback2<BPaymentNotice>() {
          @Override
          public void onException(Throwable t) {
            RLoadingDialog.hide();
            String msg = PaymentNoticeMessages.M.actionFailed(PaymentNoticeMessages.M.save(),
                getEP().getModuleCaption());
            RMsgBox.showError(msg, t);
          }

          @Override
          public void onSuccess(BPaymentNotice result) {
            RLoadingDialog.hide();
            BPaymentNoticeLogger.getInstance().log(PaymentNoticeMessages.M.create(), result);

            String msg = PaymentNoticeMessages.M.actionSuccess(PaymentNoticeMessages.M.save(),
                getEP().getModuleCaption());
            getEP().jumpToViewPage(result.getUuid(), Message.info(msg));
          }
        });
  }

  private void doDeleteRows() {
    if (grid.getSelections().isEmpty()) {
      RMsgBox.showError(PaymentNoticeMessages.M.seleteDataToAction(
          PaymentNoticeMessages.M.delete(), PaymentNoticeMessages.M.statementLine()));
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

  private void doAdd() {
    GWTUtil.blurActiveElement();
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        if (entity.getAccountUnit() == null
            || StringUtil.isNullOrBlank(entity.getAccountUnit().getUuid())) {
          RMsgBox.showError(PaymentNoticeMessages.M.pleaseFillInFirst(accountUnitField.getCaption()));
          return;
        } else if (entity.getCounterpart() == null
            || StringUtil.isNullOrBlank(entity.getCounterpart().getUuid())) {
          RMsgBox.showError(PaymentNoticeMessages.M.requiredCounterpart(getEP().getFieldCaption(
              GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart())));
          return;
        }

        if (statementDialog == null) {
          statementDialog = new StatementBrowseDialog();
          statementDialog.addValueChangeHandler(new ValueChangeHandler<List<QStatement>>() {
            @Override
            public void onValueChange(ValueChangeEvent<List<QStatement>> event) {
              if (event.getValue().isEmpty() == false) {
                for (QStatement source : event.getValue()) {
                  entity.getLines().add(statementConvertToPayLine(source));
                }
                refreshLineNumber();
                refreshGrid();
                refreshAggregate();
              }
            }
          });
        }
        statementDialog.setAccountUnitUuid(entity.getAccountUnit().getUuid());
        statementDialog.setCounterpartUuid(entity.getCounterpart().getUuid());
        statementDialog.setCounterpartType(entity.getCounterpart().getCounterpartType());
        statementDialog.setStatementUuids(getStatementUuids());
        statementDialog.center();
      }
    });
  }

  private List<String> getStatementUuids() {
    List<String> statementUuids = new ArrayList<String>();

    for (BPaymentNoticeLine line : entity.getLines()) {
      statementUuids.add(line.getStatement().getBillUuid());
    }
    return statementUuids;
  }

  private void refreshLineNumber() {
    if (entity.getLines().isEmpty() == false) {
      for (int i = 0; i < entity.getLines().size(); i++) {
        BPaymentNoticeLine line = entity.getLines().get(i);
        line.setLineNumber(i + 1);
      }
    }
  }

  private void refreshGrid() {
    if (!entity.getLines().isEmpty())
      resultTotalHtml.setText(PaymentNoticeMessages.M.resultTotal(entity.getLines().size()));
    else
      resultTotalHtml.setText(PaymentNoticeMessages.M.resultTotal(0));
    grid.refresh();
  }

  private BPaymentNoticeLine statementConvertToPayLine(QStatement source) {
    BPaymentNoticeLine target = new BPaymentNoticeLine();
    target.setStatement(source.getStatement());
    target.setContract(source.getContract());
    target.setReceiptTotal(source.getReceiptTotal());
    target.setReceiptTax(source.getReceiptTax());
    target.setReceiptIvcTotal(source.getReceiptIvcTotal());
    target.setReceiptIvcTax(source.getReceiptIvcTax());
    target.setPaymentTotal(source.getPaymentTotal());
    target.setPaymentTax(source.getPaymentTax());
    target.setPaymentIvcTotal(source.getPaymentIvcTotal());
    target.setPaymentIvcTax(source.getPaymentIvcTax());
    target.setRemark(source.getRemark());

    return target;
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

      BPaymentNoticeLine rowData = entity.getLines().get(row);
      if (col == lineNumberCol.getIndex())
        return rowData.getLineNumber();
      else if (col == statementNumCol.getIndex()) {
        BDispatch dispatch = new BDispatch("statement");
        dispatch.addParams(GRes.R.dispatch_key(), rowData.getStatement().getBillNumber());
        return dispatch;
      } else if (col == contractNumCol.getIndex()) {
        return rowData.getContract() == null ? null : rowData.getContract().getCode();
      } else if (col == contractNameCol.getIndex()) {
        return rowData.getContract() == null ? null : rowData.getContract().getName();
      } else if (col == receiptTotalCol.getIndex())
        return rowData.getReceiptTotal() == null ? BigDecimal.ZERO : rowData.getReceiptTotal()
            .doubleValue();
      else if (col == receiptTaxCol.getIndex())
        return rowData.getReceiptTax() == null ? BigDecimal.ZERO : rowData.getReceiptTax()
            .doubleValue();
      else if (col == receiptIvcTotalCol.getIndex())
        return rowData.getReceiptIvcTotal() == null ? BigDecimal.ZERO : rowData
            .getReceiptIvcTotal().doubleValue();
      else if (col == payTotalCol.getIndex())
        return rowData.getPaymentTotal() == null ? BigDecimal.ZERO : rowData.getPaymentTotal()
            .doubleValue();
      else if (col == payTaxCol.getIndex())
        return rowData.getPaymentTax() == null ? BigDecimal.ZERO : rowData.getPaymentTax()
            .doubleValue();
      else if (col == payIvcTotalCol.getIndex())
        return rowData.getPaymentIvcTotal() == null ? BigDecimal.ZERO : rowData
            .getPaymentIvcTotal().doubleValue();
      else if (col == remarkCol.getIndex())
        return rowData.getRemark();
      return null;
    }
  }

  @Override
  protected EPPaymentNotice getEP() {
    return EPPaymentNotice.getInstance();
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  @Override
  protected void refreshEntity() {
    if (entity.getAccountUnit() == null && getEP().getUserStores().size() == 1) {
      entity.setAccountUnit(new BUCN(getEP().getUserStores().get(0)));
    }
    refresh();
    accountUnitField.setFocus(true);
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
      PaymentNoticeServiceAgent.executeTask(operation, entity, processCtx, true, this);
    }
  }

}
