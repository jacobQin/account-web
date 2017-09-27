/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	OptionViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.option.client.ui;

import java.math.BigDecimal;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.commons.client.biz.BOption;
import com.hd123.m3.account.gwt.commons.client.biz.BReceiptOverdueDefault;
import com.hd123.m3.account.gwt.commons.client.biz.RoundingMode;
import com.hd123.m3.account.gwt.option.client.EPOption;
import com.hd123.m3.account.gwt.option.client.OptionMessages;
import com.hd123.m3.account.gwt.option.client.PaymentDefrayalType;
import com.hd123.m3.account.gwt.option.client.rpc.OptionService;
import com.hd123.m3.account.gwt.option.client.ui.settle.RebateByBillOptionsDialog;
import com.hd123.m3.account.gwt.option.client.ui.settle.StoreIncompleteMonthByRealDaysOptionsDialog;
import com.hd123.m3.account.gwt.option.client.ui.settle.StoreMonthDaysOptionsDialog;
import com.hd123.m3.account.gwt.option.intf.client.OptionUrlParams;
import com.hd123.m3.account.gwt.option.intf.client.OptionUrlParams.View;
import com.hd123.m3.account.gwt.option.intf.client.perm.OptionPermDef;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCheckBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedFlowField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RHTMLField;
import com.hd123.rumba.gwt.widget2.client.form.RHyperlinkField;
import com.hd123.rumba.gwt.widget2.client.form.RRadioGroup;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.misc.RHyperlink;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class OptionViewPage extends BaseContentPage implements View {

  private StoreMonthDaysOptionsDialog monthDaysOptionsDialog;
  private StoreIncompleteMonthByRealDaysOptionsDialog incompleteMonthByRealDialog;
  private RebateByBillOptionsDialog rebateByBillOptionsDialog;

  public static OptionViewPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new OptionViewPage();
    return instance;
  }

  public OptionViewPage() throws ClientBizException {
    super();
    drawSelf();
  }

  private static OptionViewPage instance;
  private EPOption ep = EPOption.getInstance();
  private BOption option;

  /** 配置 */
  private RToolbarButton editConfigButton;
  private RViewStringField monthDaysField;
  private RCheckBox incompleteMonthByRealDaysField;
  private RCheckBox genZeroStatementField;
  private RHyperlink storeMonthDaysLink;
  private RHyperlink realDaysLink;
  private RViewStringField calculationAccuracyField;
  private RViewStringField roundingModeField;
  private RViewStringField statementDefaultBPMField;
  private RViewStringField settleNoRuleField;
  private RViewStringField paymentNoticeDefaultBPMField;
  private RRadioGroup<BReceiptOverdueDefault> receiptOverdueDefaultsField;
  private RViewStringField receiptPaymentTypeField;
  private RViewNumberField receiptPaymentTypeLimitsField;
  private RCheckBox receiptBankRequiredField;
  private RCheckBox extinvSystemRequiredField;
  private RCheckBox accObjectEnbledField;
  private RCheckBox byDepositEnabledField;
  private RViewStringField defalutReceiptPaymentTypeField;
  private RViewStringField defalutInvoiceTypeField;
  private RCheckBox rebateByBillField;
  private RCheckBox receiptEqualsUnpayedField;
  private RHyperlink rebateByBillLink;

  /** 默认值 */
  private RToolbarButton editDefaultButton;
  private RViewStringField paymentTypeField;
  private RViewStringField taxRateField;
  private RHyperlinkField preReceiveSubjectField;
  private RHyperlinkField prePaySubjectField;

  private Handler_click clickHandler = new Handler_click();

  private void drawSelf() {
    VerticalPanel root = new VerticalPanel();
    root.setWidth("100%");
    root.setSpacing(5);
    initWidget(root);

    root.add(drawConfigGadget());
    root.add(drawDefaultGadget());
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
    configBox.setContent(vp);

    editConfigButton = new RToolbarButton(RActionFacade.EDIT, clickHandler);
    configBox.getCaptionBar().addButton(editConfigButton);
    return configBox;
  }

  private Widget drawConfigPanel() {
    RForm configForm = new RForm(1);
    configForm.setLabelWidth(0.25f);
    configForm.setWidth("100%");

    storeMonthDaysLink = new RHyperlink();
    storeMonthDaysLink.setHTML(OptionMessages.M.setByStore());
    storeMonthDaysLink.addClickHandler(clickHandler);

    monthDaysField = new RViewStringField(OptionMessages.M.monthDays());

    configForm.addField(new RCombinedFlowField() {
      {
        setCaption(OptionMessages.M.monthDays());
        addField(monthDaysField);
        addHorizontalSpacing(8);
        addField(storeMonthDaysLink);
      }
    });
    
    realDaysLink = new RHyperlink();
    realDaysLink.setHTML(OptionMessages.M.setByStore());
    realDaysLink.addClickHandler(clickHandler);

    incompleteMonthByRealDaysField = new RCheckBox(OptionMessages.M.incompleteMonthByRealDays());
    incompleteMonthByRealDaysField.setReadOnly(true);

    configForm.addField(new RCombinedFlowField() {
      {
        setCaption(OptionMessages.M.incompleteMonthByRealDays());
        addField(incompleteMonthByRealDaysField);
        addHorizontalSpacing(54);
        addField(realDaysLink);
      }
    });


    genZeroStatementField = new RCheckBox(OptionMessages.M.genZeroStatement());
    genZeroStatementField.setReadOnly(true);
    configForm.addField(genZeroStatementField);

    RHTMLField genZeroStatementRemark = new RHTMLField();
    genZeroStatementRemark.setText(OptionMessages.M.genZeroStatementMessage());
    genZeroStatementRemark.addTextStyleName(RTextStyles.STYLE_GRAY);
    configForm.addField(genZeroStatementRemark);

    calculationAccuracyField = new RViewStringField(OptionMessages.M.calculationAccuracy());
    configForm.addField(calculationAccuracyField);

    roundingModeField = new RViewStringField(OptionMessages.M.roundingMode());
    configForm.addField(roundingModeField);

    statementDefaultBPMField = new RViewStringField(OptionMessages.M.statementDefaultBPM());
    configForm.addField(statementDefaultBPMField);

    settleNoRuleField = new RViewStringField(OptionMessages.M.settleNoRule());
    configForm.addField(settleNoRuleField);

    RHTMLField settleNoRuleRemark = new RHTMLField();
    settleNoRuleRemark.setText(OptionMessages.M.settleNoRuleRemark());
    settleNoRuleRemark.addTextStyleName(RTextStyles.STYLE_GRAY);
    configForm.addField(settleNoRuleRemark);

    paymentNoticeDefaultBPMField = new RViewStringField(OptionMessages.M.paymentNoticeDefaultBPM());
    configForm.addField(paymentNoticeDefaultBPMField);

    receiptOverdueDefaultsField = new RRadioGroup<BReceiptOverdueDefault>(
        OptionMessages.M.receiptOverdueDefaults());
    receiptOverdueDefaultsField.add(BReceiptOverdueDefault.zero.getCaption(),
        BReceiptOverdueDefault.zero);
    receiptOverdueDefaultsField.add(BReceiptOverdueDefault.calcValue.getCaption(),
        BReceiptOverdueDefault.calcValue);
    receiptOverdueDefaultsField.setReadOnly(true);
    configForm.addField(receiptOverdueDefaultsField);

    receiptPaymentTypeField = new RViewStringField();
    receiptPaymentTypeLimitsField = new RViewNumberField();
    receiptPaymentTypeLimitsField.setFormat(M3Format.fmt_money);

    configForm.add(new RCombinedField() {
      {
        setCaption(OptionMessages.M.receiptDiffAmount());
        addField(receiptPaymentTypeField, 0.5f);
        addField(receiptPaymentTypeLimitsField, 0.5f);
        setWidth("300px");
      }
    });

    receiptBankRequiredField = new RCheckBox(OptionMessages.M.receiptBankRequired());
    receiptBankRequiredField.setReadOnly(true);
    configForm.addField(receiptBankRequiredField);

    extinvSystemRequiredField = new RCheckBox(OptionMessages.M.extinvSystemRequired());
    extinvSystemRequiredField.setReadOnly(true);
    configForm.addField(extinvSystemRequiredField);
    
    accObjectEnbledField = new RCheckBox(OptionMessages.M.accObjectEnbled());
    accObjectEnbledField.setReadOnly(true);
    configForm.addField(accObjectEnbledField);
    
    byDepositEnabledField = new RCheckBox(OptionMessages.M.byDepositEnabled());
    byDepositEnabledField.setReadOnly(true);
    configForm.addField(byDepositEnabledField);
    
    defalutReceiptPaymentTypeField = new RViewStringField(
        OptionMessages.M.defalutReceiptPaymentType());
    configForm.addField(defalutReceiptPaymentTypeField);

    rebateByBillField = new RCheckBox(OptionMessages.M.rebateByBill());
    rebateByBillField.setReadOnly(true);
    
    rebateByBillLink = new RHyperlink();
    rebateByBillLink.setHTML(OptionMessages.M.setByStore());
    rebateByBillLink.addClickHandler(clickHandler);

    configForm.addField(new RCombinedFlowField() {
      {
        setCaption(OptionMessages.M.rebateByBill());
        addField(rebateByBillField);
        addHorizontalSpacing(54);
        addField(rebateByBillLink);
      }
    });
    
    receiptEqualsUnpayedField = new RCheckBox(OptionMessages.M.receiptEqualsUnpayed());
    receiptEqualsUnpayedField.setReadOnly(true);
    configForm.addField(receiptEqualsUnpayedField);

    defalutInvoiceTypeField = new RViewStringField(OptionMessages.M.defalutInvoiceType());
    configForm.addField(defalutInvoiceTypeField);

    configForm.rebuild();
    return configForm;
  }

  private Widget drawDefaultGadget() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    vp.add(drawDefaultPanel());

    RCaptionBox defaultBox = new RCaptionBox();
    defaultBox.setCaption(OptionMessages.M.defaultValue());
    defaultBox.getCaptionBar().setShowCollapse(true);
    defaultBox.setWidth("100%");
    defaultBox.setContent(vp);

    editDefaultButton = new RToolbarButton(RActionFacade.EDIT, clickHandler);
    defaultBox.getCaptionBar().addButton(editDefaultButton);
    return defaultBox;
  }

  private Widget drawDefaultPanel() {
    RForm defaultForm = new RForm(1);
    defaultForm.setLabelWidth(0.25f);
    defaultForm.setWidth("100%");

    paymentTypeField = new RViewStringField(OptionMessages.M.paymentType());
    defaultForm.add(paymentTypeField);

    taxRateField = new RViewStringField(OptionMessages.M.taxRate());
    defaultForm.addField(taxRateField);

    preReceiveSubjectField = new RHyperlinkField(OptionMessages.M.preReceiveSubject());
    preReceiveSubjectField.addClickHandler(clickHandler);
    defaultForm.add(preReceiveSubjectField);

    prePaySubjectField = new RHyperlinkField(OptionMessages.M.prePaySubject());
    prePaySubjectField.addClickHandler(clickHandler);
    defaultForm.add(prePaySubjectField);

    defaultForm.rebuild();
    return defaultForm;
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    if (monthDaysOptionsDialog != null) {
      monthDaysOptionsDialog.onHide();
    }
    if (incompleteMonthByRealDialog != null) {
      incompleteMonthByRealDialog.onHide();
    }
    if (rebateByBillOptionsDialog != null) {
      rebateByBillOptionsDialog.onHide();
    }
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (checkIn() == false)
      return;

    loadOptoin(new Command() {
      public void execute() {
        refreshTitle();
        refreshCommands();
        refreshOption();
      }
    });
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(OptionMessages.M.moduleCaption());
  }

  private void refreshCommands() {
    assert option != null;

    boolean canEdit = ep.isPermitted(OptionPermDef.UPDATE);
    editConfigButton.setEnabled(canEdit);
    editDefaultButton.setEnabled(canEdit);
  }

  private void refreshOption() {
    if (option == null)
      return;

    // 刷新配置项
    monthDaysField
        .setValue(BigDecimal.ZERO.compareTo(option.getConfigOption().getMonthDays()) == 0 ? OptionMessages.M
            .actualMonthDays() : option.getConfigOption().getMonthDays().toString());
    incompleteMonthByRealDaysField.setValue(option.getConfigOption().isIncompleteMonthByRealDays());
    genZeroStatementField.setValue(option.getConfigOption().isGenZeroStatement());
    calculationAccuracyField.setValue(OptionMessages.M.scale(String.valueOf(option
        .getConfigOption().getScale())));
    if (RoundingMode.HALF_UP.toString().equals(option.getConfigOption().getRoundingMode())) {
      roundingModeField.setValue(RoundingMode.HALF_UP.getCaption());
    } else if (RoundingMode.DOWN.toString().equals(option.getConfigOption().getRoundingMode())) {
      roundingModeField.setValue(RoundingMode.DOWN.getCaption());
    } else if (RoundingMode.UP.toString().equals(option.getConfigOption().getRoundingMode())) {
      roundingModeField.setValue(RoundingMode.UP.getCaption());
    }
    statementDefaultBPMField
        .setValue(option.getConfigOption().getStatementDefaultBPM() == null ? null : option
            .getConfigOption().getStatementDefaultBPM());
    paymentNoticeDefaultBPMField
        .setValue(option.getConfigOption().getPaymentNoticeDefaultBPM() == null ? null : option
            .getConfigOption().getPaymentNoticeDefaultBPM());
    settleNoRuleField.setValue(option.getConfigOption().getSettleNoRuleDay() == null ? null
        : OptionMessages.M.settleNoRuleValue(option.getConfigOption().getSettleNoRuleDay()));
    receiptOverdueDefaultsField.setValue(option.getConfigOption().getReceiptOverdueDefaults());
    receiptPaymentTypeField
        .setValue(option.getConfigOption().getReceiptPaymentType() == null ? null : option
            .getConfigOption().getReceiptPaymentType().toFriendlyStr());
    if (option.getConfigOption().getReceiptPaymentType() == null) {
      receiptPaymentTypeLimitsField.setVisible(false);
    } else {
      receiptPaymentTypeLimitsField.setVisible(true);
      receiptPaymentTypeLimitsField
          .setValue(option.getConfigOption().getReceiptPaymentTypeLimits());
    }
    // 刷新默认值
    paymentTypeField.setValue(option.getDefaultOption().getPaymentType() == null ? null : option
        .getDefaultOption().getPaymentType().toFriendlyStr());
    taxRateField.setValue(option.getDefaultOption().getTaxRate() == null ? null : option
        .getDefaultOption().getTaxRate().caption());
    preReceiveSubjectField.setValue(option.getDefaultOption().getPreReceiveSubject() == null ? null
        : option.getDefaultOption().getPreReceiveSubject().toFriendlyStr());
    prePaySubjectField.setValue(option.getDefaultOption().getPrePaySubject() == null ? null
        : option.getDefaultOption().getPrePaySubject().toFriendlyStr());

    receiptBankRequiredField.setValue(option.getConfigOption().isReceiptBankRequired());

    extinvSystemRequiredField.setValue(option.getConfigOption().isExtinvSystemRequired());
    
    accObjectEnbledField.setValue(option.getConfigOption().isAccObjectEnbaled());
    
    byDepositEnabledField.setValue(option.getConfigOption().isByDepositEnabled());

    rebateByBillField.setValue(option.getConfigOption().isRebateByBill());
    
    receiptEqualsUnpayedField.setValue(option.getConfigOption().isReceiptEqualsUnpayed());

    try {
      PaymentDefrayalType type = PaymentDefrayalType.valueOf(option.getConfigOption()
          .getDefalutReceiptPaymentType());
      defalutReceiptPaymentTypeField.setValue(type.getCaption());
    } catch (Exception e) {
      defalutReceiptPaymentTypeField.setValue(null);
    }

    if (StringUtil.isNullOrBlank(option.getConfigOption().getDefalutInvoiceType()) == false) {
      String defaultInvoiceType = ep.getInvoiceTypes().get(
          option.getConfigOption().getDefalutInvoiceType());
      defalutInvoiceTypeField.setValue(defaultInvoiceType);
    } else {
      defalutInvoiceTypeField.setValue(null);
    }
  }

  private boolean checkIn() {
    if (!ep.isPermitted(OptionPermDef.READ)) {
      NoAuthorized.open(OptionMessages.M.moduleCaption());
      return false;
    }
    return true;
  }

  private void loadOptoin(final Command callback) {
    RLoadingDialog.show(CommonsMessages.M.loading());
    OptionService.Locator.getService().getOption(new RBAsyncCallback2<BOption>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = CommonsMessages.M.actionFailed(CommonsMessages.M.load(),
            OptionMessages.M.moduleCaption());
        RMsgBox.showErrorAndBack(msg, caught);
      }

      @Override
      public void onSuccess(BOption result) {
        RLoadingDialog.hide();
        option = result;
        callback.execute();
      }
    });
  }

  private class Handler_click implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource().equals(editConfigButton)) {
        JumpParameters params = new JumpParameters(OptionUrlParams.EditConfig.START_NODE);
        ep.jump(params);
      } else if (event.getSource().equals(editDefaultButton)) {
        JumpParameters params = new JumpParameters(OptionUrlParams.EditDefault.START_NODE);
        ep.jump(params);
      } else if (event.getSource().equals(preReceiveSubjectField)
          || event.getSource().equals(prePaySubjectField)) {
        GwtUrl url = SubjectUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
        String subjectUuid = event.getSource().equals(preReceiveSubjectField) ? option
            .getDefaultOption().getPreReceiveSubject().getUuid() : option.getDefaultOption()
            .getPrePaySubject().getUuid();
        url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID, subjectUuid);
        try {
          RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
        } catch (Exception e) {
          RMsgBox.showError(OptionMessages.M.cannotNavigate(PSubjectDef.TABLE_CAPTION));
        }
      } else if (event.getSource() == storeMonthDaysLink) {
        if (monthDaysOptionsDialog == null) {
          monthDaysOptionsDialog = new StoreMonthDaysOptionsDialog();
        }
        monthDaysOptionsDialog.center();
      }else if (event.getSource() == realDaysLink) {
        if (incompleteMonthByRealDialog == null) {
          incompleteMonthByRealDialog = new StoreIncompleteMonthByRealDaysOptionsDialog();
        }
        incompleteMonthByRealDialog.center();
      }  else if (event.getSource() == rebateByBillLink) {
        if (rebateByBillOptionsDialog == null) {
          rebateByBillOptionsDialog = new RebateByBillOptionsDialog();
        }
        rebateByBillOptionsDialog.center();
      }
    }
  }

}
