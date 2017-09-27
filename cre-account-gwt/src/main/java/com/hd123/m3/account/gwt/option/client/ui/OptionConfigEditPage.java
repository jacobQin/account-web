/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	OptionConfigEditPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.option.client.ui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.commons.client.biz.BConfigOption;
import com.hd123.m3.account.gwt.commons.client.biz.BProcessDefinition;
import com.hd123.m3.account.gwt.commons.client.biz.BReceiptOverdueDefault;
import com.hd123.m3.account.gwt.commons.client.biz.RoundingMode;
import com.hd123.m3.account.gwt.option.client.EPOption;
import com.hd123.m3.account.gwt.option.client.OptionMessages;
import com.hd123.m3.account.gwt.option.client.PaymentDefrayalType;
import com.hd123.m3.account.gwt.option.client.rpc.OptionService;
import com.hd123.m3.account.gwt.option.client.ui.gadget.PaymentTypeComboBox;
import com.hd123.m3.account.gwt.option.intf.client.OptionUrlParams.EditConfig;
import com.hd123.m3.account.gwt.option.intf.client.perm.OptionPermDef;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.history.RHistory;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCheckBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedFlowField;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RHTMLField;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RRadioGroup;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * 账务|结算配置|编辑
 * 
 * @author zhuhairui
 * 
 */
public class OptionConfigEditPage extends BaseContentPage implements EditConfig, RValidatable {
  private static OptionConfigEditPage instance;
  private EPOption ep = EPOption.getInstance();

  public static OptionConfigEditPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new OptionConfigEditPage();
    return instance;
  }

  public OptionConfigEditPage() throws ClientBizException {
    super();
    drawToolbar();
    drawSelf();
  }

  private BConfigOption configOption;
  private RAction saveAction;
  private RAction cancelAction;

  private RForm configForm;
  private RNumberBox monthDaysField;
  private RCheckBox incompleteMonthByRealDaysField;
  private RCheckBox genZeroStatementField;
  private RCheckBox actualMonthDaysField;
  private RComboBox calculationAccuracyField;
  private RComboBox roundingModeField;
  private RComboBox<String> statementDefaultBPMField;
  private RComboBox<Integer> settleNoRuleDayField;
  private RComboBox<String> paymentNoticeDefaultBPMField;
  private RRadioGroup<BReceiptOverdueDefault> receiptOverdueDefaultsField;
  private PaymentTypeComboBox receiptPaymentTypeField;
  private RNumberBox receiptPaymentTypeLimitsField;
  private RCheckBox receiptBankRequiredField;
  private RCheckBox extinvSystemRequiredField;
  private RCheckBox accObjectEnbledField;
  private RCheckBox byDepositEnabledField;
  private RComboBox<String> defalutReceiptPaymentTypeField;
  private RComboBox<String> defalutInvoiceTypeField;
  private RCheckBox rebateByBillField;
  private RCheckBox receiptEqualsUnpayedField;

  private Handler_change changeHandler = new Handler_change();
  private Handler_click clickHandler = new Handler_click();

  private void drawToolbar() {
    saveAction = new RAction(RActionFacade.SAVE, clickHandler);
    RToolbarButton saveButton = new RToolbarButton(saveAction);
    getToolbar().add(saveButton);

    cancelAction = new RAction(RActionFacade.CANCEL, clickHandler);
    RToolbarButton cancelButton = new RToolbarButton(cancelAction);
    getToolbar().add(cancelButton);
  }

  private void drawSelf() {
    VerticalPanel root = new VerticalPanel();
    root.setWidth("100%");
    root.setSpacing(5);
    initWidget(root);

    root.add(drawConfigGadget());
  }

  private Widget drawConfigGadget() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    vp.add(drawConfigPanel());

    RCaptionBox configBox = new RCaptionBox();
    configBox.setCaption(OptionMessages.M.config());
    configBox.getCaptionBar().setShowCollapse(true);
    configBox.setWidth("100%");
    configBox.setEditing(true);
    configBox.setContent(vp);
    return configBox;
  }

  private Widget drawConfigPanel() {
    configForm = new RForm(1);
    configForm.setLabelWidth(0.25f);
    configForm.setWidth("100%");

    configForm.addField(new RCombinedFlowField() {
      {
        setCaption(OptionMessages.M.monthDays());
        monthDaysField = new RNumberBox(OptionMessages.M.monthDays());
        monthDaysField.addValueChangeHandler(changeHandler);
        monthDaysField.setMinValue(0, true);
        monthDaysField.setScale(6);
        monthDaysField.selectAll();
        monthDaysField.setTextAlignment(RNumberBox.ALIGN_RIGHT);
        monthDaysField.setWidth("100px");
        addField(monthDaysField);

        addHorizontalSpacing(4);

        actualMonthDaysField = new RCheckBox();
        actualMonthDaysField.addValueChangeHandler(changeHandler);
        actualMonthDaysField.setText(OptionMessages.M.actualMonthDays());
        addField(actualMonthDaysField);

      }
    });
    
    incompleteMonthByRealDaysField = new RCheckBox(OptionMessages.M.incompleteMonthByRealDays());
    incompleteMonthByRealDaysField.addValueChangeHandler(changeHandler);
    configForm.addField(incompleteMonthByRealDaysField);
    
    genZeroStatementField = new RCheckBox(OptionMessages.M.genZeroStatement());
    genZeroStatementField.addValueChangeHandler(changeHandler);
    configForm.addField(genZeroStatementField);

    RHTMLField genZeroStatementRemark = new RHTMLField();
    genZeroStatementRemark.setText(OptionMessages.M.genZeroStatementMessage());
    genZeroStatementRemark.addTextStyleName(RTextStyles.STYLE_GRAY);
    configForm.addField(genZeroStatementRemark);

    calculationAccuracyField = new RComboBox(OptionMessages.M.calculationAccuracy());
    calculationAccuracyField.addOption(2, "2");
    calculationAccuracyField.addOption(1, "1");
    calculationAccuracyField.addOption(0, "0");
    calculationAccuracyField.setEditable(false);
    calculationAccuracyField.removeNullOption();
    calculationAccuracyField.setWidth("100px");
    calculationAccuracyField.setText(OptionMessages.M.scale(""));
    calculationAccuracyField.addValueChangeHandler(changeHandler);
    configForm.addField(calculationAccuracyField);

    roundingModeField = new RComboBox(OptionMessages.M.roundingMode());
    roundingModeField.addOption(RoundingMode.HALF_UP.toString(), RoundingMode.HALF_UP.getCaption());
    roundingModeField.addOption(RoundingMode.DOWN.toString(), RoundingMode.DOWN.getCaption());
    roundingModeField.addOption(RoundingMode.UP.toString(), RoundingMode.UP.getCaption());
    roundingModeField.setEditable(false);
    roundingModeField.removeNullOption();
    roundingModeField.setWidth("100px");
    roundingModeField.addValueChangeHandler(changeHandler);
    configForm.addField(roundingModeField);

    statementDefaultBPMField = new RComboBox<String>();
    statementDefaultBPMField.setCaption(OptionMessages.M.statementDefaultBPM());
    statementDefaultBPMField.setWidth("300px");
    statementDefaultBPMField.setEditable(false);
    statementDefaultBPMField.setNullOptionText(OptionMessages.M.empty_text());
    statementDefaultBPMField.setMaxDropdownRowCount(10);
    statementDefaultBPMField.addValueChangeHandler(changeHandler);
    for (BProcessDefinition def : ep.getStatementDefs()) {
      statementDefaultBPMField.addOption(def.getKey(), def.getName());
    }
    configForm.addField(statementDefaultBPMField);

    settleNoRuleDayField = new RComboBox<Integer>();
    settleNoRuleDayField.setWidth("40px");
    settleNoRuleDayField.setEditable(false);
    settleNoRuleDayField.setMaxDropdownRowCount(10);
    settleNoRuleDayField.setNullOptionText(OptionMessages.M.empty_text());
    settleNoRuleDayField.addValueChangeHandler(changeHandler);
    for (int i = 1; i < 32; i++) {
      settleNoRuleDayField.addOption(i);
    }

    RCombinedFlowField field = new RCombinedFlowField() {
      {
        addField(new RViewStringField("", OptionMessages.M.perMonth()));
        addField(createSpace("5px"));
        addField(settleNoRuleDayField);
        addField(createSpace("5px"));
        addField(new RViewStringField("", OptionMessages.M.day()
            + OptionMessages.M.toLastDayOfMonth()));
      }
    };
    field.setCaption(OptionMessages.M.settleNoRule());
    configForm.addField(field);

    RHTMLField settleNoRemark = new RHTMLField();
    settleNoRemark.setText(OptionMessages.M.settleNoRuleRemark());
    settleNoRemark.addTextStyleName(RTextStyles.STYLE_GRAY);
    configForm.addField(settleNoRemark);

    paymentNoticeDefaultBPMField = new RComboBox<String>();
    paymentNoticeDefaultBPMField.setCaption(OptionMessages.M.paymentNoticeDefaultBPM());
    paymentNoticeDefaultBPMField.setWidth("300px");
    paymentNoticeDefaultBPMField.setEditable(false);
    paymentNoticeDefaultBPMField.setNullOptionText(OptionMessages.M.empty_text());
    paymentNoticeDefaultBPMField.setMaxDropdownRowCount(10);
    paymentNoticeDefaultBPMField.addValueChangeHandler(changeHandler);
    for (BProcessDefinition def : ep.getPaymentNoticeDefs()) {
      paymentNoticeDefaultBPMField.addOption(def.getKey(), def.getName());
    }
    configForm.addField(paymentNoticeDefaultBPMField);

    receiptOverdueDefaultsField = new RRadioGroup<BReceiptOverdueDefault>();
    receiptOverdueDefaultsField.setCaption(OptionMessages.M.receiptOverdueDefaults());
    receiptOverdueDefaultsField.add(BReceiptOverdueDefault.zero.getCaption(),
        BReceiptOverdueDefault.zero);
    receiptOverdueDefaultsField.add(BReceiptOverdueDefault.calcValue.getCaption(),
        BReceiptOverdueDefault.calcValue);
    receiptOverdueDefaultsField.addValueChangeHandler(changeHandler);
    configForm.addField(receiptOverdueDefaultsField);

    receiptPaymentTypeField = new PaymentTypeComboBox(EPOption.getInstance().getPaymentTypes());
    receiptPaymentTypeField.setNullOptionTextToDefault();
    receiptPaymentTypeField.setEditable(false);
    receiptPaymentTypeField.addValueChangeHandler(changeHandler);

    receiptPaymentTypeLimitsField = new RNumberBox();
    receiptPaymentTypeLimitsField.setScale(2);
    receiptPaymentTypeLimitsField.setSelectAllOnFocus(true);
    receiptPaymentTypeLimitsField.setFormat(GWTFormat.fmt_money);
    receiptPaymentTypeLimitsField.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
    receiptPaymentTypeLimitsField.setMaxValue(BoundaryValue.BIGDECIMAL_MAXVALUE_S2, false);
    receiptPaymentTypeLimitsField.addValueChangeHandler(changeHandler);
    receiptPaymentTypeLimitsField.setCaption(OptionMessages.M.paymentTypeLimits());
    receiptPaymentTypeLimitsField.setValidator(new LimitsValidator());

    configForm.add(new RCombinedField() {
      {
        setCaption(OptionMessages.M.receiptDiffAmount());
        addField(receiptPaymentTypeField, 0.5f);
        addField(receiptPaymentTypeLimitsField, 0.5f);
        setWidth("300px");
      }
    });

    receiptBankRequiredField = new RCheckBox(OptionMessages.M.receiptBankRequired());
    receiptBankRequiredField.addValueChangeHandler(changeHandler);
    configForm.addField(receiptBankRequiredField);

    extinvSystemRequiredField = new RCheckBox(OptionMessages.M.extinvSystemRequired());
    extinvSystemRequiredField.addValueChangeHandler(changeHandler);
    configForm.addField(extinvSystemRequiredField);
    
    accObjectEnbledField = new RCheckBox(OptionMessages.M.accObjectEnbled());
    accObjectEnbledField.addValueChangeHandler(changeHandler);
    configForm.addField(accObjectEnbledField);
    
    byDepositEnabledField = new RCheckBox(OptionMessages.M.byDepositEnabled());
    byDepositEnabledField.addValueChangeHandler(changeHandler);
    configForm.addField(byDepositEnabledField);
    
    defalutReceiptPaymentTypeField = new RComboBox<String>();
    defalutReceiptPaymentTypeField.setCaption(OptionMessages.M.defalutReceiptPaymentType());
    defalutReceiptPaymentTypeField.setWidth("300px");
    defalutReceiptPaymentTypeField.setEditable(false);
    defalutReceiptPaymentTypeField.setNullOptionText(OptionMessages.M.empty_text());
    defalutReceiptPaymentTypeField.addValueChangeHandler(changeHandler);

    defalutReceiptPaymentTypeField.addOption(PaymentDefrayalType.bill.name(),
        PaymentDefrayalType.bill.getCaption());
    defalutReceiptPaymentTypeField.addOption(PaymentDefrayalType.lineSingle.name(),
        PaymentDefrayalType.lineSingle.getCaption());

    configForm.addField(defalutReceiptPaymentTypeField);

    rebateByBillField = new RCheckBox(OptionMessages.M.rebateByBill());
    rebateByBillField.addValueChangeHandler(changeHandler);
    configForm.addField(rebateByBillField);
    
    receiptEqualsUnpayedField = new RCheckBox(OptionMessages.M.receiptEqualsUnpayed());
    receiptEqualsUnpayedField.addValueChangeHandler(changeHandler);
    configForm.addField(receiptEqualsUnpayedField);

    defalutInvoiceTypeField = new RComboBox<String>();
    defalutInvoiceTypeField.setCaption(OptionMessages.M.defalutInvoiceType());
    defalutInvoiceTypeField.setWidth("300px");
    defalutInvoiceTypeField.setEditable(false);
    defalutInvoiceTypeField.setNullOptionText(OptionMessages.M.empty_text());
    for (Map.Entry<String, String> item : ep.getInvoiceTypes().entrySet()) {
      defalutInvoiceTypeField.addOption(item.getKey(), item.getValue());
    }
    defalutInvoiceTypeField.addValueChangeHandler(changeHandler);
    configForm.addField(defalutInvoiceTypeField);

    configForm.rebuild();
    return configForm;
  }

  private HTML createSpace(String width) {
    HTML space = new HTML("&nbsp;");
    space.setWidth(width);
    return space;
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (checkIn() == false)
      return;

    loadConfigOptoin(new Command() {
      public void execute() {

        refreshTitle();
        refreshConfigOption();
      }
    });
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    // do nothing
  }

  private boolean checkIn() {
    if (!ep.isPermitted(OptionPermDef.READ)) {
      NoAuthorized.open(OptionMessages.M.moduleCaption());
      return false;
    }
    return true;
  }

  private void loadConfigOptoin(final Command callback) {
    RLoadingDialog.show(CommonsMessages.M.loading());
    OptionService.Locator.getService().getConfigOption(new RBAsyncCallback2<BConfigOption>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = CommonsMessages.M.actionFailed(CommonsMessages.M.load(),
            OptionMessages.M.moduleCaption() + "(" + OptionMessages.M.config() + ")");
        RMsgBox.showErrorAndBack(msg, caught);
      }

      @Override
      public void onSuccess(BConfigOption result) {
        RLoadingDialog.hide();
        configOption = result;
        callback.execute();
      }
    });
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(OptionMessages.M.moduleCaption());
    ep.getTitleBar().appendAttributeText(OptionMessages.M.config());
  }

  private void refreshConfigOption() {
    if (configOption == null)
      return;

    if (BigDecimal.ZERO.compareTo(configOption.getMonthDays()) == 0) {
      monthDaysField.clearValue();
      monthDaysField.setEnabled(false);
      actualMonthDaysField.setValue(true);
    } else {
      monthDaysField.setValue(configOption.getMonthDays());
      monthDaysField.setEnabled(true);
      actualMonthDaysField.setValue(false);
    }
    
    incompleteMonthByRealDaysField.setValue(configOption.isIncompleteMonthByRealDays());
    rebateByBillField.setValue(configOption.isRebateByBill());
    receiptEqualsUnpayedField.setValue(configOption.isReceiptEqualsUnpayed());
    genZeroStatementField.setValue(configOption.isGenZeroStatement());
    calculationAccuracyField.setValue(configOption.getScale());
    roundingModeField.setValue(configOption.getRoundingMode());

    statementDefaultBPMField.setValue(configOption.getStatementDefaultBPMKey());
    paymentNoticeDefaultBPMField.setValue(configOption.getPaymentNoticeDefaultBPMKey());
    receiptOverdueDefaultsField.setValue(configOption.getReceiptOverdueDefaults());
    receiptPaymentTypeField.setValue(configOption.getReceiptPaymentType());
    if (receiptPaymentTypeField.isEmpty()) {
      receiptPaymentTypeLimitsField.setEnabled(false);
    } else {
      receiptPaymentTypeLimitsField.setValue(configOption.getReceiptPaymentTypeLimits());
    }

    settleNoRuleDayField.setValue(configOption.getSettleNoRuleDay());
    receiptBankRequiredField.setValue(configOption.isReceiptBankRequired());
    extinvSystemRequiredField.setValue(configOption.isExtinvSystemRequired());
    accObjectEnbledField.setValue(configOption.isAccObjectEnbaled());
    byDepositEnabledField.setValue(configOption.isByDepositEnabled());
    defalutReceiptPaymentTypeField.setValue(configOption.getDefalutReceiptPaymentType());
    defalutInvoiceTypeField.setValue(configOption.getDefalutInvoiceType());

    clearValidResults();
  }

  private class Handler_change implements ValueChangeHandler {

    @Override
    public void onValueChange(ValueChangeEvent event) {
      if (event.getSource().equals(monthDaysField)) {
        configOption.setMonthDays(monthDaysField.getValueAsBigDecimal());
      } else if (event.getSource().equals(genZeroStatementField)) {
        configOption.setGenZeroStatement(genZeroStatementField.getValue());
      } else if(event.getSource().equals(incompleteMonthByRealDaysField)){
        configOption.setIncompleteMonthByRealDays(incompleteMonthByRealDaysField.getValue());
      } else if (event.getSource().equals(actualMonthDaysField)) {
        if (actualMonthDaysField.getValue()) {
          configOption.setMonthDays(BigDecimal.ZERO);
          monthDaysField.clearValue();
          monthDaysField.clearValidResults();
          monthDaysField.setEnabled(false);
        } else {
          monthDaysField.setEnabled(true);
        }
      } else if (event.getSource().equals(calculationAccuracyField)) {
        configOption.setScale(calculationAccuracyField.getValueAsInteger());
      } else if (event.getSource().equals(roundingModeField)) {
        configOption.setRoundingMode(roundingModeField.getValueAsString());
      } else if (event.getSource().equals(statementDefaultBPMField)) {
        configOption.setStatementDefaultBPMKey(statementDefaultBPMField.getValue());
      } else if (event.getSource().equals(paymentNoticeDefaultBPMField)) {
        configOption.setPaymentNoticeDefaultBPMKey(paymentNoticeDefaultBPMField.getValue());
      } else if (event.getSource().equals(settleNoRuleDayField)) {
        configOption.setSettleNoRuleDay(settleNoRuleDayField.getValue());
      } else if (event.getSource().equals(receiptOverdueDefaultsField)) {
        configOption.setReceiptOverdueDefaults(receiptOverdueDefaultsField.getValue());
      } else if (event.getSource().equals(receiptPaymentTypeField)) {
        configOption.setReceiptPaymentType(receiptPaymentTypeField.getValue());
        if (!receiptPaymentTypeField.isEmpty()) {
          receiptPaymentTypeLimitsField.setEnabled(true);
        } else {
          receiptPaymentTypeLimitsField.clearValidResults();
          receiptPaymentTypeLimitsField.clearValue();
          receiptPaymentTypeLimitsField.setEnabled(false);
        }
      } else if (event.getSource().equals(receiptPaymentTypeLimitsField)) {
        configOption.setReceiptPaymentTypeLimits(receiptPaymentTypeLimitsField
            .getValueAsBigDecimal());
      } else if (event.getSource().equals(receiptBankRequiredField)) {
        configOption.setReceiptBankRequired(receiptBankRequiredField.getValue());
      } else if (event.getSource().equals(extinvSystemRequiredField)) {
        configOption.setExtinvSystemRequired(extinvSystemRequiredField.getValue());
      } else if (event.getSource().equals(accObjectEnbledField)) {
        configOption.setAccObjectEnbaled(accObjectEnbledField.getValue());
      } else if (event.getSource().equals(defalutReceiptPaymentTypeField)) {
        configOption.setDefalutReceiptPaymentType(defalutReceiptPaymentTypeField.getValue());
      } else if (event.getSource().equals(defalutInvoiceTypeField)) {
        configOption.setDefalutInvoiceType(defalutInvoiceTypeField.getValue());
      } else if (event.getSource().equals(rebateByBillField)) {
        configOption.setRebateByBill(rebateByBillField.getValue());
      } else if (event.getSource().equals(receiptEqualsUnpayedField)) {
        configOption.setReceiptEqualsUnpayed(receiptEqualsUnpayedField.getValue());
      } else if(event.getSource().equals(byDepositEnabledField)){
        configOption.setByDepositEnabled(byDepositEnabledField.getValue());
      }
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

  private class LimitsValidator implements RValidator {

    @Override
    public Message validate(Widget sender, String value) {
      if (receiptPaymentTypeField.isValid()) {
        if (receiptPaymentTypeField.getValue() == null) {
          return null;
        } else if (receiptPaymentTypeLimitsField.getValue() == null
            || (receiptPaymentTypeLimitsField.getValueAsBigDecimal() != null && receiptPaymentTypeLimitsField
                .getValueAsBigDecimal().compareTo(BigDecimal.ZERO) <= 0)) {
          return Message.error(OptionMessages.M
              .paymentTypeLimitsNotNull(receiptPaymentTypeLimitsField.getCaption()));
        }
      }
      return null;
    }

  }

  public void doSave() {
    getMessagePanel().clear();
    GWTUtil.blurActiveElement();
    if (validate() == false) {
      getMessagePanel().putMessages(getInvalidMessages());
      return;
    }

    RLoadingDialog.show(OptionMessages.M.actionDoing(OptionMessages.M.save()));
    OptionService.Locator.getService().setConfigOption(configOption, new RBAsyncCallback2<Void>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = OptionMessages.M.actionFailed(OptionMessages.M.save(),
            OptionMessages.M.moduleCaption());
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(Void result) {
        RLoadingDialog.hide();
        JumpParameters params = new JumpParameters(OptionViewPage.START_NODE);
        params.getMessages().add(
            Message.info(CommonsMessages.M.onSuccess(CommonsMessages.M.save(),
                OptionMessages.M.moduleCaption(), OptionMessages.M.config())));
        ep.jump(params);
      }
    });
  }

  private void doCancel() {
    String msg = CommonsMessages.M.actionComfirm(OptionMessages.M.cancel(),
        CommonsMessages.M.edit());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed)
          RHistory.back();
      }
    });
  }

  @Override
  public void clearValidResults() {
    configForm.clearValidResults();
  }

  @Override
  public boolean isValid() {
    boolean isValid = configForm.isValid();
    if (needMonthDaysField()) {
      isValid = false;
    }
    isValid = isValid && receiptPaymentTypeLimitsField.isValid();
    return isValid;
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> messages = new ArrayList<Message>();
    messages.addAll(configForm.getInvalidMessages());
    messages.addAll(monthDaysField.getInvalidMessages());
    messages.addAll(receiptPaymentTypeLimitsField.getInvalidMessages());
    return messages;
  }

  @Override
  public boolean validate() {
    boolean validate = configForm.validate();
    if (needMonthDaysField()) {
      monthDaysField.addErrorMessage(OptionMessages.M.monthDaysIsNull());
      validate = false;
    }
    validate = validate && receiptPaymentTypeLimitsField.validate();
    return validate;
  }

  private boolean needMonthDaysField() {
    return actualMonthDaysField.getValue() == false && monthDaysField.getValue() == null;
  }

}
