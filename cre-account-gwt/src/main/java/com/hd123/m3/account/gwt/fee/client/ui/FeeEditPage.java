/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： FeeEditPage.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-16 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.fee.client.EPFee;
import com.hd123.m3.account.gwt.fee.client.FeeMessages;
import com.hd123.m3.account.gwt.fee.client.biz.ActionName;
import com.hd123.m3.account.gwt.fee.client.biz.BAccSettle;
import com.hd123.m3.account.gwt.fee.client.biz.BFee;
import com.hd123.m3.account.gwt.fee.client.biz.BFeeLogger;
import com.hd123.m3.account.gwt.fee.client.rpc.FeeLoader;
import com.hd123.m3.account.gwt.fee.client.rpc.FeeService;
import com.hd123.m3.account.gwt.fee.client.rpc.FeeServiceAgent;
import com.hd123.m3.account.gwt.fee.client.ui.gadget.FeeLineGridGadget;
import com.hd123.m3.account.gwt.fee.intf.client.FeeUrlParams.Edit;
import com.hd123.m3.account.gwt.fee.intf.client.dd.PFeeDef;
import com.hd123.m3.account.gwt.fee.intf.client.perm.FeePermDef;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmEditPage;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.DispatchLinkField;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.history.RHistory;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author subinzhu
 * 
 */
public class FeeEditPage extends BaseBpmEditPage implements Edit, RActionHandler,
    HasRActionHandlers, RValidatable {
  private static FeeEditPage instance = null;

  public static FeeEditPage getInstance() {
    if (instance == null)
      instance = new FeeEditPage();
    return instance;
  }

  private FeeLoader entityLoader;
  private BFee entity;

  private RAction saveAction;
  private RAction cancelAction;

  private FeeLineGridGadget lineEditGadget;

  private RForm generalForm;
  private RViewStringField billNumberField;
  private RViewStringField accountUnitField;
  private DispatchLinkField contractField;
  private RViewStringField contractNameField;
  private RViewStringField counterpartField;
  private RDateBox accountDateField;
  private RDateBox beginDateField;
  private RDateBox endDateField;
  private RViewStringField directionTypeField;
  private HTML captionHtml;
  private RViewStringField generateStatementField;

  private RViewNumberField totalField;
  private RViewNumberField taxField;

  private RTextArea remarkField;

  private RForm operateForm;
  private RViewStringField bizStateField;
  private RSimpleOperateInfoField createInfo;
  private RSimpleOperateInfoField lastModifyInfo;

  private PermGroupEditField permGroupField;

  private Handler_click clickHandler = new Handler_click();
  private Handler_textField textFieldHandler = new Handler_textField();

  private BAccSettle accSettle;

  public FeeEditPage() {
    super();
    entityLoader = new FeeLoader();
    drawToolbar();
    drawSelf();
  }

  private void drawToolbar() {
    saveAction = new RAction(RActionFacade.SAVE, clickHandler);
    RToolbarButton saveButton = new RToolbarButton(saveAction);
    getToolbar().add(saveButton);

    // BPM出口按钮
    injectBpmActions();

    cancelAction = new RAction(RActionFacade.CANCEL, clickHandler);
    RToolbarButton cancelButton = new RToolbarButton(cancelAction);
    getToolbar().add(cancelButton);
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
    mvp.add(1, drawOperate());
    permGroupField = new PermGroupEditField(getEP().isPermEnabled(), getEP().getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(CommonsMessages.M.generalInfo());
    box.setWidth("100%");
    box.setEditing(true);
    box.setContent(vp);
    return box;
  }

  private Widget drawBasic() {
    generalForm = new RForm(1);
    generalForm.setWidth("100%");

    billNumberField = new RViewStringField(PFeeDef.constants.billNumber());
    billNumberField.addTextStyleName(RTextStyles.STYLE_BOLD);
    generalForm.addField(billNumberField);

    accountUnitField = new RViewStringField(EPFee.getInstance().getFieldCaption(
        GRes.FIELDNAME_BUSINESS, GRes.R.business()));
    generalForm.addField(accountUnitField);

    contractField = new DispatchLinkField(PFeeDef.constants.contract_code());
    contractField.setLinkKey(GRes.R.dispatch_key());
    generalForm.addField(contractField);

    contractNameField = new RViewStringField(PFeeDef.constants.contract_name());
    generalForm.addField(contractNameField);

    counterpartField = new RViewStringField(EPFee.getInstance().getFieldCaption(
        GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
    generalForm.addField(counterpartField);

    accountDateField = new RDateBox(PFeeDef.accountDate.getCaption());
    accountDateField.setRequired(true);
    accountDateField.addChangeHandler(textFieldHandler);
    accountDateField.setValidator(new RValidator() {
      @Override
      public Message validate(final Widget sender, String value) {
        if (entity.getContract() == null || entity.getContract().getUuid() == null) {
          return null;
        }

        if (StringUtil.isNullOrBlank(value) == false) {
          if (accSettle != null && accSettle.isExists() && accSettle.getLstCalcEndDate() != null) {
            if (GWTFormat.fmt_yMd.parse(value).after(accSettle.getLstCalcEndDate()) == false) {
              return Message.error(PFeeDef.accountDate.getCaption()
                  + " : "
                  + FeeMessages.M.mustLaggerThenAccSettleEndDate(GWTFormat.fmt_yMd.format(accSettle
                      .getLstCalcEndDate())));
            }
          }

          if (GWTFormat.fmt_yMd.parse(value).before(entity.getContract().getBeginDate())) {
            return Message.error(PFeeDef.accountDate.getCaption()
                + " : "
                + FeeMessages.M.mustLaggerThenContractBeginDate(GWTFormat.fmt_yMd.format(entity
                    .getContract().getBeginDate())));
          }

          Date endDate = (Date) entity.getContract().getEndDate().clone();
          Date newDate = entity.addMonth(endDate, 6);
          if (GWTFormat.fmt_yMd.parse(value).after(newDate)) {
            return Message.error(PFeeDef.accountDate.getCaption() + " : "
                + FeeMessages.M.mustLessThenContractEndDate(GWTFormat.fmt_yMd.format(newDate)));
          }
        }
        return null;
      }
    });
    generalForm.addField(accountDateField);

    beginDateField = new RDateBox(PFeeDef.constants.dateRange_beginDate());
    beginDateField.addValueChangeHandler(new ValueChangeHandler<Date>() {
      public void onValueChange(ValueChangeEvent<Date> event) {
        endDateField.validate();
        entity.getDateRange().setBeginDate(event.getValue());
        RActionEvent.fire(FeeEditPage.this, ActionName.CHANGE_BEGIN_DATE);
      }
    });
    generalForm.addField(beginDateField);

    endDateField = new RDateBox(PFeeDef.constants.dateRange_endDate());
    endDateField.setValidator(new RValidator() {

      @Override
      public Message validate(Widget sender, String value) {
        if (endDateField.getValue() == null)
          return null;
        if (beginDateField.getValue() != null) {
          if (beginDateField.getValue().after(endDateField.getValue())) {
            return Message.error(endDateField.getCaption() + "："
                + PFeeDef.constants.dateRange_endDate() + FeeMessages.M.cannot()
                + FeeMessages.M.lessThan(PFeeDef.constants.dateRange_beginDate()));
          }
        }
        return null;
      }
    });
    endDateField.addValueChangeHandler(new ValueChangeHandler<Date>() {
      public void onValueChange(ValueChangeEvent<Date> event) {
        entity.getDateRange().setEndDate(event.getValue());
        RActionEvent.fire(FeeEditPage.this, ActionName.CHANGE_END_DATE);
      }
    });
    generalForm.addField(endDateField);

    directionTypeField = new RViewStringField(PFeeDef.constants.direction());
    
    captionHtml = new HTML();
    captionHtml.setHTML(PFeeDef.constants.generateStatement());
    captionHtml.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    generateStatementField = new RViewStringField();
    RCombinedField combinedField = new RCombinedField() {
      {
        addField(directionTypeField, 0.1f);
        addField(captionHtml, 0.4f);
        addField(generateStatementField, 0.5f);
      }
    };

    combinedField.setCaption(directionTypeField.getCaption());
    generalForm.addField(combinedField, true);

    generalForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(CommonsMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(generalForm);
    return box;
  }

  private Widget drawOperate() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    bizStateField = new RViewStringField(PFeeDef.bizState);
    bizStateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(bizStateField);

    createInfo = new RSimpleOperateInfoField(PFeeDef.constants.createInfo());
    operateForm.addField(createInfo);

    lastModifyInfo = new RSimpleOperateInfoField(PFeeDef.constants.lastModifyInfo());
    operateForm.addField(lastModifyInfo);

    operateForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(FeeMessages.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  private Widget drawAggregate() {
    RForm form = new RForm(1);
    form.setWidth("100%");

    totalField = new RViewNumberField();
    totalField.setFormat(M3Format.fmt_money);
    totalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    
    taxField = new RViewNumberField();
    taxField.setFormat(M3Format.fmt_money);
    taxField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    
    RCombinedField combinField = new RCombinedField() {
      {
        setCaption(PFeeDef.constants.total_total()+"/"+PFeeDef.constants.total_tax());
        addField(totalField, 0.5f);
        addField(taxField, 0.5f);
      }
    };
    
    form.add(combinField);
    
    form.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(FeeMessages.M.aggregate());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private Widget drawMiddle() {
    lineEditGadget = new FeeLineGridGadget(DirectionType.payment.getDirectionValue());
    lineEditGadget.addActionHandler(this);
    this.addActionHandler(lineEditGadget);
    return lineEditGadget;
  }

  private Widget drawBottom() {
    remarkField = new RTextArea(PFeeDef.remark);
    remarkField.addChangeHandler(textFieldHandler);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PFeeDef.constants.remark());
    box.setWidth("100%");
    box.setEditing(true);
    box.setContent(remarkField);
    box.getCaptionBar().setShowCollapse(true);

    return box;
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    lineEditGadget.clear();
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (!checkIn())
      return;

    entityLoader.decodeParams(params, new Command() {
      public void execute() {
        entity = entityLoader.getEntity();
        refresh(entity);
        getEP().appendSearchBox();
        generalForm.focusOnFirstField();
      }
    });
  }

  private boolean checkIn() {
    if (getEP().isProcessMode())
      return true;
    if (getEP().isPermitted(FeePermDef.UPDATE) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void refreshGeneral() {
    assert entity != null;

    billNumberField.setValue(entity.getBillNumber());
    accountUnitField.setValue(entity.getAccountUnit() == null ? null : entity.getAccountUnit()
        .toFriendlyStr());
    if (entity.getContract() == null) {
      contractField.setValue(entity.getContract() == null ? null : entity.getContract()
          .getBillNumber());
    } else {
      if (entity.getCounterpart() != null
          && BCounterpart.COUNPERPART_PROPRIETOR.equals(entity.getCounterpart().getCounterpartType())) {
        contractField.setValue(GRes.FIELDNAME_LEASEBACKCONTRACT_BILLTYPE, entity.getContract()
            .getCode());
      } else {
        contractField.setValue(GRes.FIELDNAME_CONTRACT_BILLTYPE, entity.getContract().getCode());
      }
    }
    contractNameField
        .setValue(entity.getContract() == null ? null : entity.getContract().getName());
    counterpartField.setValue(entity.getCounterpart() == null ? null : entity.getCounterpart()
        .toFriendlyStr(getEP().getCounterpartTypeMap()));
    accountDateField.setValue(entity.getAccountDate());
    beginDateField.setValue(entity.getDateRange().getBeginDate());
    endDateField.setValue(entity.getDateRange().getEndDate());
    directionTypeField.setValue(DirectionType.getCaptionByValue(entity.getDirection()));
    generateStatementField.setValue(entity.isGenerateStatement()?FeeMessages.M.yes():FeeMessages.M.no());

    refreshTotal();
    remarkField.setValue(entity.getRemark());
  }

  private void refreshAuthorize() {
    permGroupField.setPerm(entity);
  }

  private void refreshTotal() {
    totalField.setValue(entity.getTotal().getTotal());
    taxField.setValue(entity.getTotal().getTax());
  }

  private void refreshOperateInfo() {
    bizStateField.setValue(PFeeDef.bizState.getEnumCaption(entity.getBizState()));
    createInfo.setOperateInfo(entity.getCreateInfo());
    lastModifyInfo.setOperateInfo(entity.getLastModifyInfo());

    operateForm.rebuild();
  }

  private void refreshGrid() {
    lineEditGadget.setValue(entity, entity.getDirection());
  }

  public void clearValidResults() {
    getMessagePanel().clearMessages();
    generalForm.clearValidResults();
    permGroupField.clearValidResults();
    lineEditGadget.clearValidResults();
    remarkField.clearValidResults();
  }

  public List<Message> getInvalidMessages() {
    List<Message> messages = new ArrayList<Message>();
    messages.addAll(generalForm.getInvalidMessages());
    messages.addAll(permGroupField.getInvalidMessages());
    messages.addAll(lineEditGadget.getInvalidMessages());
    messages.addAll(remarkField.getInvalidMessages());
    return messages;
  }

  public boolean isValid() {
    return generalForm.isValid() && permGroupField.isValid() && lineEditGadget.isValid()
        && remarkField.isValid();
  }

  public boolean validate() {
    clearValidResults();
    getMessagePanel().clearMessages();
    boolean localValid = generalForm.validate();
    localValid &= permGroupField.validate();
    localValid &= lineEditGadget.validate();
    localValid &= remarkField.validate();
    if (!localValid)
      getMessagePanel().putMessages(getInvalidMessages());
    return localValid;
  }

  private void getAccSettle() {
    if (entity == null || entity.getContract() == null || entity.getContract().getUuid() == null) {
      accSettle = null;
      return;
    }

    GWTUtil.enableSynchronousRPC();
    FeeService.Locator.getService().getAccSettle(entity.getContract().getUuid(),
        new RBAsyncCallback2<BAccSettle>() {
          @Override
          public void onException(Throwable caught) {
            String msg = FeeMessages.M.processError(FeeMessages.M.getAccSettle());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(BAccSettle result) {
            accSettle = result;
          }
        });
  }

  private class Handler_click implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource().equals(saveAction)) {
        doSave();
      } else if (event.getSource().equals(cancelAction)) {
        doCancel();
      }
    }
  }

  private void doSave() {
    GWTUtil.blurActiveElement();

    if (validate() == false)
      return;
    RLoadingDialog.show(FeeMessages.M.actionDoing(FeeMessages.M.save()));
    FeeService.Locator.getService().save(entity, getEP().getProcessCtx(),
        new RBAsyncCallback2<BFee>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = FeeMessages.M.actionFailed2(FeeMessages.M.save(), PFeeDef.TABLE_CAPTION,
                entity.getBillNumber());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(BFee result) {
            RLoadingDialog.hide();
            // 记录日志
            BFeeLogger.getInstance().log(FeeMessages.M.modify(),result);
            Message msg = Message.info(FeeMessages.M.onSuccess(FeeMessages.M.save(),
                PFeeDef.TABLE_CAPTION, entity.getBillNumber()));
            getEP().jumpToViewPage(entity.getUuid(), msg);
          }
        });
  }

  private void doCancel() {
    RMsgBox.showConfirm(FeeMessages.M.actionComfirm(FeeMessages.M.cancel(), FeeMessages.M.edit()),
        new RMsgBox.ConfirmCallback() {
          public void onClosed(boolean confirmed) {
            if (confirmed)
              RHistory.back();
          }
        });
  }

  private class Handler_textField implements ChangeHandler {
    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == remarkField) {
        entity.setRemark(remarkField.getValue());
      } else if (event.getSource() == accountDateField) {
        entity.setAccountDate(accountDateField.getValue());
      }
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == ActionName.REFRESH) {
      entity.aggregate();
      refreshTotal();
    }
  }

  @Override
  public void refreshToolbar(RToolbar toolbar) {
    saveAction.setVisible(getEP().isPermitted(FeePermDef.UPDATE));
  }

  @Override
  protected EPFee getEP() {
    return EPFee.getInstance();
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  @Override
  protected void refreshEntity() {
    entity.initialize();

    getAccSettle();
    refreshGeneral();
    refreshAuthorize();
    refreshOperateInfo();
    refreshGrid();
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
      FeeServiceAgent.executeTask(operation, entity, processCtx, true, this);
    }
  }
}
