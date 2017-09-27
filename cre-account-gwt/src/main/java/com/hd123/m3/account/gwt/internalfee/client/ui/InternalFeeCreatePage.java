/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InternalFeeCreatePage.java
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
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.internalfee.client.EPInternalFee;
import com.hd123.m3.account.gwt.internalfee.client.InternalFeeMessages;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFee;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFeeLogger;
import com.hd123.m3.account.gwt.internalfee.client.rpc.InternalFeeService;
import com.hd123.m3.account.gwt.internalfee.client.rpc.InternalFeeServiceAgent;
import com.hd123.m3.account.gwt.internalfee.client.ui.gadget.LineEditGridGadget;
import com.hd123.m3.account.gwt.internalfee.client.ui.gadget.VendorUCNBox;
import com.hd123.m3.account.gwt.internalfee.intf.client.InternalFeeUrlParams.Create;
import com.hd123.m3.account.gwt.internalfee.intf.client.dd.PInternalFeeDef;
import com.hd123.m3.account.gwt.internalfee.intf.client.perm.InternalFeePermDef;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmCreatePage;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.base.client.BUCN;
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
import com.hd123.rumba.gwt.widget2.client.form.RRadioGroup;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.TitleBar;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author liuguilin
 * 
 */
public class InternalFeeCreatePage extends BaseBpmCreatePage implements Create, RActionHandler,
    HasRActionHandlers, RValidatable {

  private static InternalFeeCreatePage instance = null;

  public static InternalFeeCreatePage getInstance() {
    if (instance == null)
      instance = new InternalFeeCreatePage();
    return instance;
  }

  public InternalFeeCreatePage() {
    super();
    drawToolbar();
    drawSelf();
  }

  private BInternalFee entity;
  private int oldDirection = DirectionType.payment.getDirectionValue();

  private RAction saveAction;
  private RAction cancelAction;

  private RForm generalForm;
  private AccountUnitUCNBox storeBox;
  private VendorUCNBox vendorBox;
  private RDateBox beginDateField;
  private RDateBox endDateField;
  private RDateBox accountDateField;
  private RRadioGroup<Integer> directionTypeField;
  private PermGroupEditField permGroupField;
  private RViewNumberField totalField;
  private RViewNumberField taxField;
  private RTextArea remarkField;

  private LineEditGridGadget lineEditGrid;

  private Handler_click clickHandler = new Handler_click();

  private void drawToolbar() {
    saveAction = new RAction(RActionFacade.SAVE, clickHandler);
    getToolbar().add(new RToolbarButton(saveAction));

    // BPM出口按钮
    injectBpmActions();

    cancelAction = new RAction(RActionFacade.CANCEL, clickHandler);
    getToolbar().add(new RToolbarButton(cancelAction));
  }

  private void drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    initWidget(panel);

    panel.add(drawTop());
    panel.add(drawMiddle());
    panel.add(drawBottom());
    // TODO
    addActionHandler(lineEditGrid);
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
    permGroupField = new PermGroupEditField(getEP().isPermEnabled(), getEP().getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);
    mvp.add(0, drawAggregate());

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

    storeBox = new AccountUnitUCNBox(true, false);
    storeBox.setCaption(EPInternalFee.getInstance().getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    storeBox.setRequired(true);
    storeBox.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        entity.setStore(storeBox.getValue());
        RActionEvent.fire(InternalFeeCreatePage.this, "storeChangeEent", storeBox.getValue()
            .getUuid());
      }
    });
    generalForm.addField(storeBox);

    vendorBox = new VendorUCNBox(true, false);
    vendorBox.setCaption(PInternalFeeDef.constants.vendor());
    vendorBox.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        entity.setVendor(vendorBox.getValue());
      }
    });
    generalForm.addField(vendorBox);

    beginDateField = new RDateBox(PInternalFeeDef.constants.beginDate());
    beginDateField.addValueChangeHandler(new ValueChangeHandler<Date>() {
      @Override
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

    accountDateField = new RDateBox(PInternalFeeDef.constants.accountDate());
    accountDateField.setRequired(true);
    accountDateField.addValueChangeHandler(new ValueChangeHandler<Date>() {
      public void onValueChange(ValueChangeEvent<Date> event) {
        entity.setAccountDate(accountDateField.getValue());
      }
    });
    generalForm.addField(accountDateField);

    directionTypeField = new RRadioGroup(PInternalFeeDef.constants.direction());
    directionTypeField.clear();
    directionTypeField.add(DirectionType.receipt.getCaption(),
        DirectionType.receipt.getDirectionValue());
    directionTypeField.add(DirectionType.payment.getCaption(),
        DirectionType.payment.getDirectionValue());
    directionTypeField.setRequired(false);
    directionTypeField.addValueChangeHandler(new ValueChangeHandler<Integer>() {
      @Override
      public void onValueChange(ValueChangeEvent<Integer> arg0) {
        if (entity.getLines().isEmpty()
            || (entity.getLines().size() == 1 && (entity.getLines().get(0).getSubject() == null || entity
                .getLines().get(0).getSubject().getUuid() == null))) {
          oldDirection = directionTypeField.getValue().intValue();
          entity.setDirection(directionTypeField.getValue().intValue());
          entity.getLines().clear();
          lineEditGrid.setValue(entity.getLines(), entity.getDirection());
        } else {
          String msg = InternalFeeMessages.M.changeDirectionConfirm();
          RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
            public void onClosed(boolean confirmed) {
              if (confirmed == false) {
                directionTypeField.setValue(oldDirection);
                return;
              }
              oldDirection = directionTypeField.getValue().intValue();
              entity.setDirection(directionTypeField.getValue().intValue());
              // TODO
              entity.getLines().clear();
              lineEditGrid.setValue(entity.getLines(), entity.getDirection());
              refreshTotal();
            }
          });
        }
      }
    });
    generalForm.add(directionTypeField);

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

  private Widget drawMiddle() {
    lineEditGrid = new LineEditGridGadget(DirectionType.payment.getDirectionValue());
    lineEditGrid.addActionHandler(this);
    return lineEditGrid;
  }

  private Widget drawBottom() {
    remarkField = new RTextArea(PInternalFeeDef.remark);
    remarkField.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        entity.setRemark(event.getValue());
      }
    });
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInternalFeeDef.constants.remark());
    box.setEditing(true);
    box.setWidth("100%");
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
    storeBox.clearConditions();
    vendorBox.clearConditions();
    lineEditGrid.clear();
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (!checkIn())
      return;

    try {
      decodeParams(params, new Command() {
        public void execute() {
          refresh(entity);
          getEP().appendSearchBox();

          generalForm.focusOnFirstField();
        }
      });
    } catch (Exception e) {
      RMsgBox.showError(e);
    }
  }

  private boolean checkIn() {
    if (getEP().isBpmMode())
      return true;
    if (getEP().isPermitted(InternalFeePermDef.CREATE) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void decodeParams(JumpParameters params, final Command callback) {
    assert callback != null;

    RLoadingDialog.show(InternalFeeMessages.M.actionDoing(InternalFeeMessages.M.create()));
    InternalFeeService.Locator.getService().create(new RBAsyncCallback2<BInternalFee>() {
      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = InternalFeeMessages.M.actionFailed(InternalFeeMessages.M.create(),
            PInternalFeeDef.TABLE_CAPTION);
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(BInternalFee result) {
        RLoadingDialog.hide();
        entity = result;
        callback.execute();
      }
    });
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
  public void refreshTitle(TitleBar titleBar) {
    titleBar.clearStandardTitle();
    titleBar.setTitleText(InternalFeeMessages.M.create());
    titleBar.appendAttributeText(PInternalFeeDef.TABLE_CAPTION);
  }

  @Override
  protected void refreshEntity() {
    if (entity.getStore() == null && getEP().getUserStores().size() == 1) {
      entity.setStore(new BUCN(getEP().getUserStores().get(0)));
    }

    storeBox.setValue(entity.getStore());
    vendorBox.setValue(entity.getVendor());
    beginDateField.setValue(entity.getBeginDate());
    endDateField.setValue(entity.getEndDate());
    accountDateField.setValue(entity.getAccountDate());
    directionTypeField.setValue(entity.getDirection());
    permGroupField.setPerm(entity);
    remarkField.setValue(entity.getRemark());
    totalField.setValue(entity.getTotal() == null ? null : entity.getTotal().getTotal());
    taxField.setValue(entity.getTotal() == null ? null : entity.getTotal().getTax());
    lineEditGrid.setValue(entity.getLines(), entity.getDirection());
    clearValidResults();
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == LineEditGridGadget.ActionName.REFRESH) {
      refreshTotal();
    }
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
        && remarkField.isValid();
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

  private void refreshTotal() {
    entity.aggregate();
    totalField.setValue(entity.getTotal().getTotal());
    taxField.setValue(entity.getTotal().getTax());
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
      } else if (event.getSource() == cancelAction) {
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
            BInternalFeeLogger.getInstance().log(InternalFeeMessages.M.create(), result);

            Message msg = Message.info(InternalFeeMessages.M.actionSuccess(
                InternalFeeMessages.M.save(), PInternalFeeDef.TABLE_CAPTION));
            getEP().jumpToViewPage(result.getUuid(), msg);
          }
        });
  }

  private void doCancel() {
    RMsgBox.showConfirm(
        InternalFeeMessages.M.actionComfirm(InternalFeeMessages.M.cancel(),
            InternalFeeMessages.M.create()), new RMsgBox.ConfirmCallback() {
          public void onClosed(boolean confirmed) {
            if (confirmed)
              RHistory.back();
          }
        });
  }
}