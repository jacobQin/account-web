/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： FeeCreatePage.java
 * 模块说明：    
 * 修改历史：
 * 2015-9-23 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.ContractBrowseBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.fee.client.EPFee;
import com.hd123.m3.account.gwt.fee.client.FeeMessages;
import com.hd123.m3.account.gwt.fee.client.biz.ActionName;
import com.hd123.m3.account.gwt.fee.client.biz.BAccSettle;
import com.hd123.m3.account.gwt.fee.client.biz.BFee;
import com.hd123.m3.account.gwt.fee.client.biz.BFeeLogger;
import com.hd123.m3.account.gwt.fee.client.rpc.FeeService;
import com.hd123.m3.account.gwt.fee.client.rpc.FeeServiceAgent;
import com.hd123.m3.account.gwt.fee.client.ui.gadget.FeeLineGridGadget;
import com.hd123.m3.account.gwt.fee.intf.client.FeeUrlParams.Create;
import com.hd123.m3.account.gwt.fee.intf.client.dd.PFeeDef;
import com.hd123.m3.account.gwt.fee.intf.client.perm.FeePermDef;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmCreatePage;
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
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RRadioGroup;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.TitleBar;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author lixiaohong
 * 
 */
public class FeeCreatePage extends BaseBpmCreatePage implements Create, RActionHandler,
    HasRActionHandlers, RValidatable {
  private static FeeCreatePage instance = null;

  public static FeeCreatePage getInstance() {
    if (instance == null)
      instance = new FeeCreatePage();
    return instance;
  }

  public FeeCreatePage() {
    super();
    drawToolbar();
    drawSelf();
  }

  private BFee entity;
  private int oldDirection = DirectionType.receipt.getDirectionValue();

  private RAction saveAction;
  private RAction cancelAction;

  private RForm generalForm;
  private AccountUnitUCNBox accountUnitUCNBox;
  private ContractBrowseBox contractBox;
  private RViewStringField contractNameField;

  private RCombinedField countpartField;
  private CounterpartUCNBox counterpartUCNBox;
  private RViewStringField countpartTypeField;
  private RDateBox accountDateField;
  private RDateBox beginDateField;
  private RDateBox endDateField;
  private RRadioGroup<Integer> directionTypeField;
  private HTML captionHtml;
  private RComboBox<String> generateStatementField;

  private RViewNumberField totalField;
  private RViewNumberField taxField;
  private RTextArea remarkField;

  private PermGroupEditField permGroupField;
  private FeeLineGridGadget lineEditGadget;

  private BAccSettle accSettle;

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

    accountUnitUCNBox = new AccountUnitUCNBox();
    accountUnitUCNBox.setCaption(EPFee.getInstance().getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    accountUnitUCNBox.setRequired(true);
    accountUnitUCNBox.addChangeHandler(new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        entity.setAccountUnit(accountUnitUCNBox.getValue());
        fetchContract();
        RActionEvent.fire(FeeCreatePage.this, ActionName.CHANGE_STORE);
      }
    });
    generalForm.addField(accountUnitUCNBox);

    contractBox = new ContractBrowseBox(PFeeDef.constants.contract_code(), true,
        new ContractBrowseBox.Callback() {

          @Override
          public void execute(BContract result) {
            if (result == null) {
              entity.setContract(null);
            } else {
              if (contractBox.isValid() == false) {
                accSettle = null;
                return;
              }

              entity.setContract(result);
              contractBox.setValue(result.getBillNumber());
              contractBox.setRawValue(result);
              contractNameField.setValue(result.getTitle());
            }

            onChangeContract();

            if (entity.getContract() != null && entity.getAccountDate() != null) {
              accountDateField.validate();
            }
          }
        }, getEP().getCaptionMap());
    contractBox.setCounterTypeMap(getEP().getCounterpartTypeMap());
    contractBox.setRequired(true);
    contractBox.getBrowser().addOpenHandler(new OpenHandler<PopupPanel>() {

      @Override
      public void onOpen(OpenEvent<PopupPanel> event) {
        contractBox.setCounterpartLike(entity.getCounterpart() == null ? null : entity
            .getCounterpart().getCode());
        contractBox.setAccountUnitLike(entity.getAccountUnit() == null ? null : entity
            .getAccountUnit().getCode());
        contractBox.setCounterpartEqual(entity.getCounterpart() == null ? null : entity
            .getCounterpart().getCounterpartType());
      }
    });
    contractBox.setValidator(new RValidator() {
      @Override
      public Message validate(Widget sender, String value) {
        if (StringUtil.isNullOrBlank(value) == false && contractBox.getRawValue() != null) {
          if (StringUtil.isNullOrBlank(contractBox.getRawValue().getUuid()) == false) {
            getAccSettle(contractBox.getRawValue().getUuid());
            if (accSettle != null && accSettle.isExists() == false) {
              return Message.error(PFeeDef.constants.contract_code() + " : "
                  + FeeMessages.M.accSettleNoExists());
            }
          } else {
            return Message.error(WidgetRes.M.cannotFindContract() + ":" + value);
          }
        } else {
          accSettle = null;
        }
        return null;
      }
    });
    generalForm.addField(contractBox);

    contractNameField = new RViewStringField(PFeeDef.constants.contract_name());
    generalForm.addField(contractNameField);

    counterpartUCNBox = new CounterpartUCNBox(getEP().getCaptionMap());
    counterpartUCNBox.setCodeBoxWidth(Float.parseFloat("0.44"));
    counterpartUCNBox.setCounterTypeMap(getEP().getCounterpartTypeMap());
    counterpartUCNBox.setRequired(true);
    counterpartUCNBox.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        entity.setCounterpart(counterpartUCNBox.getRawValue());
        String counterpartType = counterpartUCNBox.getRawValue().getCounterpartType();
        countpartTypeField.setValue(counterpartType == null ? null : getEP()
            .getCounterpartTypeMap().get(counterpartType));
        fetchContract();
      }
    });

    countpartTypeField = new RViewStringField();

    countpartField = new RCombinedField() {
      {
        setCaption(EPFee.getInstance().getFieldCaption(GRes.FIELDNAME_COUNTERPART,
            GRes.R.counterpart()));
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
    generalForm.addField(countpartField);

    accountDateField = new RDateBox(PFeeDef.accountDate.getCaption());
    accountDateField.setRequired(true);
    accountDateField.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        entity.setAccountDate(accountDateField.getValue());
      }
    });
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
        RActionEvent.fire(FeeCreatePage.this, ActionName.CHANGE_BEGIN_DATE);
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
            return Message.error(endDateField.getCaption() + " : "
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
        RActionEvent.fire(FeeCreatePage.this, ActionName.CHANGE_END_DATE);
      }
    });
    generalForm.addField(endDateField);

    directionTypeField = new RRadioGroup(PFeeDef.constants.direction());
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
          lineEditGadget.setValue(entity, entity.getDirection());
        } else {
          String msg = FeeMessages.M.changeDirectionConfirm();
          RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
            public void onClosed(boolean confirmed) {
              if (confirmed == false) {
                directionTypeField.setValue(oldDirection);
                return;
              }
              oldDirection = directionTypeField.getValue().intValue();
              entity.setDirection(directionTypeField.getValue().intValue());
              entity.getLines().clear();
              lineEditGadget.setValue(entity, entity.getDirection());
            }
          });
        }
      }
    });

    captionHtml = new HTML();
    captionHtml.setHTML(PFeeDef.constants.generateStatement());
    captionHtml.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    generateStatementField = new RComboBox<String>(PFeeDef.generateStatement);
    generateStatementField.setValue(Boolean.TRUE.toString());
    generateStatementField.addValueChangeHandler(new ValueChangeHandler<String>() {
      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        entity.setGenerateStatement(new Boolean(generateStatementField.getValue()));
      }
    });

    RCombinedField combinedField = new RCombinedField() {
      {
        setCaption(directionTypeField.getCaption());
        addField(directionTypeField, 0.1f);
        addField(captionHtml, 0.4f);
        addField(generateStatementField, 0.5f);
      }
    };

    combinedField.setCaption(directionTypeField.getCaption());
    generalForm.addField(combinedField);

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
    lineEditGadget = new FeeLineGridGadget(DirectionType.receipt.getDirectionValue());
    lineEditGadget.addActionHandler(this);
    this.addActionHandler(lineEditGadget);
    return lineEditGadget;
  }

  private Widget drawBottom() {
    remarkField = new RTextArea(PFeeDef.remark);
    remarkField.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        entity.setRemark(event.getValue());
      }
    });
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PFeeDef.constants.remark());
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
    contractBox.setClearState(false);
    contractBox.clearConditions();
    if (contractBox.getRawValue() != null)
      contractBox.getRawValue().getMessages().clear();
    counterpartUCNBox.clearConditions();
    accountUnitUCNBox.clearConditions();
    lineEditGadget.clear();
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (!checkIn())
      return;

    decodeParams(params, new Command() {

      public void execute() {
        refresh(entity);
        getEP().appendSearchBox();
        generalForm.focusOnFirstField();
      }
    });
  }

  private boolean checkIn() {
    if (getEP().isBpmMode())
      return true;
    if (getEP().isPermitted(FeePermDef.CREATE) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void decodeParams(JumpParameters params, final Command callback) {
    assert callback != null;

    RLoadingDialog.show(FeeMessages.M.actionDoing(FeeMessages.M.create()));
    FeeService.Locator.getService().create(new RBAsyncCallback2<BFee>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = FeeMessages.M.actionFailed(FeeMessages.M.create(), PFeeDef.TABLE_CAPTION);
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(BFee result) {
        RLoadingDialog.hide();
        entity = result;
        callback.execute();
      }
    });
  }

  @Override
  public void refreshTitle(TitleBar titleBar) {
    titleBar.clearStandardTitle();
    titleBar.setTitleText(FeeMessages.M.create());
    titleBar.appendAttributeText(PFeeDef.TABLE_CAPTION);
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

  private void onChangeContract() {
    BContract contract = contractBox.getRawValue();
    if (contract == null || StringUtil.isNullOrBlank(contract.getUuid()))
      return;

    entity.setAccountUnit(contract.getAccountUnit());
    RActionEvent.fire(FeeCreatePage.this, ActionName.CHANGE_STORE);
    entity.setCounterpart(contract.getCounterpart());
    accountUnitUCNBox.setValue(entity.getAccountUnit());
    counterpartUCNBox.setValue(entity.getCounterpart());
    if (getEP().getCounterpartTypeMap().size() > 1)
      countpartTypeField.setValue(entity.getCounterpart() == null ? null : getEP()
          .getCounterpartTypeMap().get(entity.getCounterpart().getCounterpartType()));

  }

  private void getAccSettle(String contractUuid) {
    if (contractUuid == null) {
      accSettle = null;
      return;
    }

    contractBox.clearValidResults();
    GWTUtil.enableSynchronousRPC();
    FeeService.Locator.getService().getAccSettle(contractUuid, new RBAsyncCallback2<BAccSettle>() {
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

  private void fetchContract() {
    refreshContract();
  }

  private void refreshContract() {
    if (entity.getCounterpart() == null || entity.getCounterpart().getUuid() == null
        || entity.getAccountUnit() == null || entity.getAccountUnit().getUuid() == null) {
      return;
    }

    GWTUtil.enableSynchronousRPC();
    RLoadingDialog.show(FeeMessages.M.loading());
    FeeService.Locator.getService().getOnlyOneContract(
        entity.getAccountUnit() == null ? null : entity.getAccountUnit().getUuid(),
        entity.getCounterpart().getUuid(), entity.getCounterpart().getCounterpartType(),
        // 对方单位类型
        new RBAsyncCallback2<BContract>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = FeeMessages.M.actionFailed(FeeMessages.M.load(),
                PFeeDef.constants.contract());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BContract result) {
            RLoadingDialog.hide();
            if (result != null) {
              entity.setContract(result);
              entity.setAccountUnit(result.getAccountUnit());
              contractBox.setRawValue(result);
              contractBox.setValue(result.getBillNumber());
              contractNameField.setValue(result.getTitle());
            } else {
              entity.setContract(null);
              contractBox.clearValue();
              contractBox.clearRawValue();
              contractBox.clearValidResults();
              contractNameField.clearValue();
            }
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

    if (!validate())
      return;
    RLoadingDialog.show(FeeMessages.M.actionDoing(FeeMessages.M.save()));
    FeeService.Locator.getService().save(entity, getEP().getProcessCtx(),
        new RBAsyncCallback2<BFee>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = FeeMessages.M.actionFailed(FeeMessages.M.save(), PFeeDef.TABLE_CAPTION);
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(BFee result) {
            RLoadingDialog.hide();
            // 记录日志
            BFeeLogger.getInstance().log(FeeMessages.M.create(), result);
            Message msg = Message.info(FeeMessages.M.actionSuccess(FeeMessages.M.save(),
                PFeeDef.TABLE_CAPTION));
            getEP().jumpToViewPage(result.getUuid(), msg);
          }
        });
  }

  private void doCancel() {
    RMsgBox.showConfirm(
        FeeMessages.M.actionComfirm(FeeMessages.M.cancel(), FeeMessages.M.create()),
        new RMsgBox.ConfirmCallback() {
          public void onClosed(boolean confirmed) {
            if (confirmed)
              RHistory.back();
          }
        });
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(final RActionEvent event) {
    if (event.getActionName() == ActionName.REFRESH) {
      entity.aggregate();
      refreshTotal();
    }
  }

  private void refreshTotal() {
    totalField.setValue(entity.getTotal().getTotal());
    taxField.setValue(entity.getTotal().getTax());
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
    if (entity.getAccountUnit() == null && getEP().getUserStores().size() == 1) {
      entity.setAccountUnit(new BUCN(getEP().getUserStores().get(0)));
    }
    if (entity.getContract() != null) {
      contractBox.setRawValue(entity.getContract());
      contractBox.setValue(entity.getContract().getBillNumber());
      contractNameField.setValue(entity.getContract().getTitle());
    } else {
      contractBox.clearValue();
      contractBox.clearRawValue();
      contractNameField.clearValue();
    }
    accountUnitUCNBox.setValue(entity.getAccountUnit());
    if (entity.getDateRange() != null && entity.getDateRange().getBeginDate() != null) {
      beginDateField.setValue(entity.getDateRange().getBeginDate());
    } else {
      beginDateField.clearValue();
      beginDateField.clearValidResults();
    }

    if (entity.getDateRange() != null && entity.getDateRange().getEndDate() != null) {
      endDateField.setValue(entity.getDateRange().getEndDate());
    } else {
      endDateField.clearValue();
      endDateField.clearValidResults();
    }
    if (entity.getCounterpart() != null) {
      counterpartUCNBox.setValue(entity.getCounterpart());
      if (getEP().getCounterpartTypeMap().size() > 1)
        countpartTypeField.setValue(getEP().getCounterpartTypeMap().get(
            entity.getCounterpart().getCounterpartType()));
    } else {
      counterpartUCNBox.clearValue();
      counterpartUCNBox.clearValidResults();
      if (getEP().getCounterpartTypeMap().size() > 1)
        countpartTypeField.clearValue();
    }
    if (counterpartUCNBox.getBrowser() != null)
      counterpartUCNBox.getBrowser().getFilterCallback().clearConditions();
    accountDateField.setValue(entity.getAccountDate());
    directionTypeField.setValue(entity.getDirection());
    permGroupField.setPerm(entity);
    remarkField.setValue(entity.getRemark());
    totalField.setValue(entity.getTotal() == null ? null : entity.getTotal().getTotal());
    taxField.setValue(entity.getTotal() == null ? null : entity.getTotal().getTax());
    generateStatementField.setValue(Boolean.valueOf(entity.isGenerateStatement()).toString());

    lineEditGadget.setValue(entity, entity.getDirection());
    clearValidResults();
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
