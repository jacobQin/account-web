/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InternalFeeEditPage.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.client.ui;

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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.internalfee.client.EPInternalFee;
import com.hd123.m3.account.gwt.internalfee.client.InternalFeeMessages;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFee;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFeeLogger;
import com.hd123.m3.account.gwt.internalfee.client.rpc.InternalFeeLoader;
import com.hd123.m3.account.gwt.internalfee.client.rpc.InternalFeeService;
import com.hd123.m3.account.gwt.internalfee.client.rpc.InternalFeeServiceAgent;
import com.hd123.m3.account.gwt.internalfee.client.ui.gadget.LineEditGridGadget;
import com.hd123.m3.account.gwt.internalfee.intf.client.InternalFeeUrlParams.Edit;
import com.hd123.m3.account.gwt.internalfee.intf.client.dd.PInternalFeeDef;
import com.hd123.m3.account.gwt.internalfee.intf.client.perm.InternalFeePermDef;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmEditPage;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
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
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author liuguilin
 * 
 */
public class InternalFeeEditPage extends BaseBpmEditPage implements Edit, RActionHandler,
    HasRActionHandlers, RValidatable {

  private static InternalFeeEditPage instance = null;

  public static InternalFeeEditPage getInstance() {
    if (instance == null)
      instance = new InternalFeeEditPage();
    return instance;
  }

  public InternalFeeEditPage() {
    super();
    entityLoader = new InternalFeeLoader();
    drawToolbar();
    drawSelf();
  }

  private InternalFeeLoader entityLoader;
  private BInternalFee entity;

  private RAction saveAction;
  private RAction cancelAction;

  private RForm generalForm;
  private RViewStringField billNumberField;
  private RViewStringField storeField;
  private RViewStringField vendorField;
  private RDateBox beginDateField;
  private RDateBox endDateField;
  private RDateBox accountDateField;
  private RViewStringField directionTypeField;

  private RViewNumberField totalField;
  private RViewNumberField taxField;

  private RTextArea remarkField;

  private RForm operateForm;
  private RViewStringField bizStateField;
  private RSimpleOperateInfoField createInfo;
  private RSimpleOperateInfoField lastModifyInfo;

  private PermGroupEditField permGroupField;

  private LineEditGridGadget lineEditGrid;

  private Handler_click clickHandler = new Handler_click();

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

    billNumberField = new RViewStringField(PInternalFeeDef.constants.billNumber());
    billNumberField.addTextStyleName(RTextStyles.STYLE_BOLD);
    generalForm.addField(billNumberField);

    storeField = new RViewStringField(PInternalFeeDef.store);
    generalForm.addField(storeField);

    vendorField = new RViewStringField(PInternalFeeDef.vendor);
    generalForm.addField(vendorField);

    beginDateField = new RDateBox(PInternalFeeDef.constants.beginDate());
    beginDateField.addValueChangeHandler(new ValueChangeHandler<Date>() {
      public void onValueChange(ValueChangeEvent<Date> event) {
        endDateField.validate();
        entity.setBeginDate(event.getValue());
      }
    });
    generalForm.addField(beginDateField);

    endDateField = new RDateBox(PInternalFeeDef.constants.endDate());
    endDateField.setValidator(new RValidator() {
      @Override
      public Message validate(Widget sender, String value) {
        if (endDateField.getValue() == null)
          return null;
        if (beginDateField.getValue() != null) {
          if (beginDateField.getValue().after(endDateField.getValue())) {
            return Message.error(endDateField.getCaption() + "："
                + PInternalFeeDef.constants.endDate() + InternalFeeMessages.M.cannot()
                + InternalFeeMessages.M.lessThan(PInternalFeeDef.constants.beginDate()));
          }
        }
        return null;
      }
    });
    endDateField.addValueChangeHandler(new ValueChangeHandler<Date>() {
      public void onValueChange(ValueChangeEvent<Date> event) {
        entity.setEndDate(event.getValue());
      }
    });
    generalForm.addField(endDateField);

    accountDateField = new RDateBox(PInternalFeeDef.accountDate.getCaption());
    accountDateField.setRequired(true);
    accountDateField.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        entity.setAccountDate(accountDateField.getValue());
      }
    });
    generalForm.addField(accountDateField);

    directionTypeField = new RViewStringField(PInternalFeeDef.constants.direction());
    generalForm.addField(directionTypeField);

    generalForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(CommonsMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(generalForm);
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
    taxField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(new RCombinedField() {
      {
        setCaption(PInternalFeeDef.constants.total());
        addField(totalField, 0.5f);
        addField(taxField, 0.5f);
      }
    });

    form.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(InternalFeeMessages.M.aggregate());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private Widget drawOperate() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    bizStateField = new RViewStringField(PInternalFeeDef.bizState);
    bizStateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(bizStateField);

    createInfo = new RSimpleOperateInfoField(PInternalFeeDef.constants.createInfo());
    operateForm.addField(createInfo);

    lastModifyInfo = new RSimpleOperateInfoField(PInternalFeeDef.constants.lastModifyInfo());
    operateForm.addField(lastModifyInfo);

    operateForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(InternalFeeMessages.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  private Widget drawMiddle() {
    lineEditGrid = new LineEditGridGadget(DirectionType.payment.getDirectionValue());
    lineEditGrid.addActionHandler(this);
    return lineEditGrid;
  }

  private Widget drawBottom() {
    remarkField = new RTextArea(PInternalFeeDef.remark);
    remarkField.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        entity.setRemark(remarkField.getValue());
      }
    });
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInternalFeeDef.constants.remark());
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
    lineEditGrid.clear();
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
    if (getEP().isPermitted(InternalFeePermDef.UPDATE) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  @Override
  protected EPInternalFee getEP() {
    return EPInternalFee.getInstance();
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  @Override
  public void refreshToolbar(RToolbar toolbar) {
    saveAction.setVisible(getEP().isPermitted(InternalFeePermDef.UPDATE));
  }
  
  @Override
  protected void refreshEntity() {
    refreshGeneral();
    refreshAuthorize();
    refreshOperateInfo();
    refreshGrid();
  }

  private void refreshGeneral() {
    assert entity != null;

    billNumberField.setValue(entity.getBillNumber());
    storeField.setValue(entity.getStore() == null ? null : entity.getStore().toFriendlyStr());
    vendorField.setValue(entity.getVendor() == null ? null : entity.getVendor().toFriendlyStr());
    beginDateField.setValue(entity.getBeginDate());
    endDateField.setValue(entity.getEndDate());
    accountDateField.setValue(entity.getAccountDate());
    directionTypeField.setValue(DirectionType.getCaptionByValue(entity.getDirection()));

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
    bizStateField.setValue(PInternalFeeDef.bizState.getEnumCaption(entity.getBizState()));
    createInfo.setOperateInfo(entity.getCreateInfo());
    lastModifyInfo.setOperateInfo(entity.getLastModifyInfo());

    operateForm.rebuild();
  }

  private void refreshGrid() {
    lineEditGrid.setUuid(entity.getStore().getUuid());
    lineEditGrid.setValue(entity.getLines(), entity.getDirection());
  }

  @Override
  public void clearValidResults() {
    getMessagePanel().clearMessages();
    generalForm.clearValidResults();
    permGroupField.clearValidResults();
    lineEditGrid.clearValidResults();
    remarkField.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return generalForm.isValid() && permGroupField.isValid() && lineEditGrid.isValid()
        && remarkField.isValid() ;
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> messages = new ArrayList<Message>();
    messages.addAll(generalForm.getInvalidMessages());
    messages.addAll(permGroupField.getInvalidMessages());
    messages.addAll(lineEditGrid.getInvalidMessages());
    messages.addAll(remarkField.getInvalidMessages());
    return messages;
  }

  @Override
  public boolean validate() {
    clearValidResults();
    getMessagePanel().clearMessages();
    boolean localValid = generalForm.validate();
    localValid &= permGroupField.validate();
    localValid &= lineEditGrid.validate();
    localValid &= remarkField.validate();
    if (!localValid)
      getMessagePanel().putMessages(getInvalidMessages());
    return localValid;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == LineEditGridGadget.ActionName.REFRESH) {
      entity.aggregate();
      refreshTotal();
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
      InternalFeeServiceAgent.executeTask(operation, entity, processCtx, true, this);
    }
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

    RLoadingDialog.show(InternalFeeMessages.M.actionDoing(InternalFeeMessages.M.save()));
    InternalFeeService.Locator.getService().save(entity, getEP().getProcessCtx(),
        new RBAsyncCallback2<BInternalFee>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InternalFeeMessages.M.actionFailed(InternalFeeMessages.M.save(),
                PInternalFeeDef.TABLE_CAPTION);
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(BInternalFee result) {
            RLoadingDialog.hide();
            BInternalFeeLogger.getInstance().log(InternalFeeMessages.M.modify(),result);
            
            Message msg = Message.info(InternalFeeMessages.M.actionSuccess(
                InternalFeeMessages.M.save(), PInternalFeeDef.TABLE_CAPTION));
            getEP().jumpToViewPage(result.getUuid(), msg);
          }
        });
  }

  private void doCancel() {
    RMsgBox.showConfirm(
        InternalFeeMessages.M.actionComfirm(InternalFeeMessages.M.cancel(),
            InternalFeeMessages.M.edit()), new RMsgBox.ConfirmCallback() {
          public void onClosed(boolean confirmed) {
            if (confirmed)
              RHistory.back();
          }
        });
  }
}
