/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	OptionDefaultEditPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.option.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.commons.client.biz.BDefaultOption;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.option.client.EPOption;
import com.hd123.m3.account.gwt.option.client.OptionMessages;
import com.hd123.m3.account.gwt.option.client.rpc.OptionService;
import com.hd123.m3.account.gwt.option.client.ui.gadget.PaymentTypeUCNBox;
import com.hd123.m3.account.gwt.option.client.ui.gadget.SubjectUCNBox;
import com.hd123.m3.account.gwt.option.intf.client.OptionUrlParams.EditDefault;
import com.hd123.m3.account.gwt.option.intf.client.perm.OptionPermDef;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.widget.client.ui.TaxRateComboBox;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.history.RHistory;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class OptionDefaultEditPage extends BaseContentPage implements EditDefault {

  public static OptionDefaultEditPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new OptionDefaultEditPage();
    return instance;
  }

  public OptionDefaultEditPage() throws ClientBizException {
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
    // do nothing
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (checkIn() == false)
      return;

    loadDefaultOptoin(new Command() {
      public void execute() {
        refreshTitle();
        refreshDefaultOption();
      }
    });
  }

  private void doSave() {
    getMessagePanel().clear();
    GWTUtil.blurActiveElement();
    if (defaultForm.validate() == false) {
      getMessagePanel().putMessages(defaultForm.getInvalidMessages());
      return;
    }
    RLoadingDialog.show(CommonsMessages.M.actionDoing(CommonsMessages.M.save()));
    OptionService.Locator.getService().setDefaultOption(defaultOption,
        new RBAsyncCallback2<Void>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = OptionMessages.M.actionFailed(CommonsMessages.M.save(),
                OptionMessages.M.moduleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters(OptionViewPage.START_NODE);
            params.getMessages().add(
                Message.info(OptionMessages.M.onSuccess(CommonsMessages.M.save(),
                    OptionMessages.M.moduleCaption(), OptionMessages.M.defaultValue())));
            ep.jump(params);
          }
        });
  }

  private static OptionDefaultEditPage instance;
  private EPOption ep = EPOption.getInstance();

  private BDefaultOption defaultOption;
  private RAction saveAction;
  private RAction cancelAction;

  private RForm defaultForm;
  private PaymentTypeUCNBox paymentTypeField;
  private TaxRateComboBox taxRateField;
  private SubjectUCNBox preReceiveSubjectField;
  private SubjectUCNBox prePaySubjectField;

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

    root.add(drawDefaultGadget());
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
    defaultBox.setEditing(true);
    defaultBox.setContent(vp);
    return defaultBox;
  }

  private Widget drawDefaultPanel() {
    defaultForm = new RForm(1);
    defaultForm.setLabelWidth(0.25f);
    defaultForm.setWidth("50%");

    paymentTypeField = new PaymentTypeUCNBox();
    paymentTypeField.setCaption(OptionMessages.M.paymentType());
    paymentTypeField.addValueChangeHandler(changeHandler);
    defaultForm.addField(paymentTypeField);

    taxRateField = new TaxRateComboBox(OptionMessages.M.taxRate());
    taxRateField.refreshOptions();
    taxRateField.addValueChangeHandler(changeHandler);
    defaultForm.addField(taxRateField);

    preReceiveSubjectField = new SubjectUCNBox(BSubjectType.predeposit.toString(),
        DirectionType.receipt.getDirectionValue());
    preReceiveSubjectField.setCaption(OptionMessages.M.preReceiveSubject());
    preReceiveSubjectField.addValueChangeHandler(changeHandler);
    defaultForm.add(preReceiveSubjectField);

    prePaySubjectField = new SubjectUCNBox(BSubjectType.predeposit.toString(),
        DirectionType.payment.getDirectionValue());
    prePaySubjectField.setCaption(OptionMessages.M.prePaySubject());
    prePaySubjectField.addValueChangeHandler(changeHandler);
    defaultForm.add(prePaySubjectField);

    defaultForm.rebuild();
    return defaultForm;
  }

  private boolean checkIn() {
    if (!ep.isPermitted(OptionPermDef.UPDATE)) {
      NoAuthorized.open(OptionMessages.M.moduleCaption());
      return false;
    }
    return true;
  }

  private void loadDefaultOptoin(final Command callback) {
    RLoadingDialog.show(CommonsMessages.M.loading());
    OptionService.Locator.getService().getDefaultOption(new RBAsyncCallback2<BDefaultOption>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = CommonsMessages.M.actionFailed(CommonsMessages.M.load(),
            OptionMessages.M.moduleCaption() + "(" + OptionMessages.M.defaultValue() + ")");
        RMsgBox.showErrorAndBack(msg, caught);
      }

      @Override
      public void onSuccess(BDefaultOption result) {
        RLoadingDialog.hide();
        defaultOption = result;
        callback.execute();
      }
    });
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(OptionMessages.M.moduleCaption());
    ep.getTitleBar().appendAttributeText(OptionMessages.M.defaultValue());
  }

  private void refreshDefaultOption() {
    if (defaultOption == null)
      return;

    paymentTypeField.setValue(defaultOption.getPaymentType());
    taxRateField.setValue(defaultOption.getTaxRate());
    preReceiveSubjectField.setValue(defaultOption.getPreReceiveSubject());
    prePaySubjectField.setValue(defaultOption.getPrePaySubject());
  }

  private class Handler_change implements ValueChangeHandler {

    @Override
    public void onValueChange(ValueChangeEvent event) {
      if (event.getSource().equals(paymentTypeField)) {
        defaultOption.setPaymentType(paymentTypeField.getValue());
      } else if (event.getSource().equals(taxRateField)) {
        defaultOption.setTaxRate(taxRateField.getValue());
      } else if (event.getSource().equals(preReceiveSubjectField)) {
        defaultOption.setPreReceiveSubject(preReceiveSubjectField.getValue());
      } else if (event.getSource().equals(prePaySubjectField)) {
        defaultOption.setPrePaySubject(prePaySubjectField.getValue());
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

}
