/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	PaymentTypeModifyDialog.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymenttype.client.ui.gadget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.paymenttype.client.EPPaymentType;
import com.hd123.m3.account.gwt.paymenttype.client.PaymentTypeMessages;
import com.hd123.m3.account.gwt.paymenttype.client.biz.BPaymentType;
import com.hd123.m3.account.gwt.paymenttype.client.biz.BPaymentTypeLogger;
import com.hd123.m3.account.gwt.paymenttype.client.rpc.PaymentTypeService;
import com.hd123.m3.account.gwt.paymenttype.client.ui.PaymentTypeViewPage;
import com.hd123.m3.account.gwt.paymenttype.intf.client.dd.PPaymentTypeDef;
import com.hd123.m3.account.gwt.paymenttype.intf.client.perm.PaymentTypePermDef;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.panel.RMessagePanel;
import com.hd123.rumba.gwt.widget2.client.panel.RSimplePanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author zhuhairui
 * 
 */
public class PaymentTypeModifyDialog extends RDialog implements RValidatable {

  public PaymentTypeModifyDialog() {
    super();
    setWidth("450px");

    RVerticalPanel root = new RVerticalPanel();
    root.setWidth("100%");
    setWidget(root);

    messagePanel = new RMessagePanel();
    root.add(messagePanel);

    root.add(drawSelf());

    setButtonsHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

    setButtons(new ButtonConfig[] {
        BUTTON_CONFIRM, BUTTON_CANCEL });
    BUTTON_CONFIRM.setClickToClose(false);

    getButton(BUTTON_CONFIRM).addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        doSave();
      }
    });
  }

  @Override
  public void clearValidResults() {
    form.clearValidResults();
    messagePanel.clearMessages();
  }

  @Override
  public boolean isValid() {
    boolean isValid = form.isValid();
    isValid &= isCodeNotExist;
    return isValid;
  }

  @Override
  public List<Message> getInvalidMessages() {
    messages.clear();
    messages.addAll(form.getInvalidMessages());
    return messages;
  }

  @Override
  public boolean validate() {
    clearValidResults();
    boolean valid = form.validate();
    valid &= isCodeNotExist;

    if (StringUtil.isNullOrBlank(codeField.getValue()) || codeField.isValid() == false) {
      isCodeNotExist = true;
    }

    if (isCodeNotExist == false) {
      codeField.addErrorMessage(PaymentTypeMessages.M.codeRepeat(codeField.getValue()));
    }

    if (valid == false)
      messagePanel.putMessages(getInvalidMessages());
    return valid;
  }

  public void doCreate() {
    doCreate(new Command() {

      @Override
      public void execute() {
        if (ep.isPermitted(PaymentTypePermDef.CREATE) == false)
          return;

        refresh(true);
        clearValidResults();
        codeField.setFocus(true);
      }
    });
  }

  public void refresh(boolean forCreate) {
    if (forCreate) {
      setCaptionText(CommonsMessages.M.create() + PPaymentTypeDef.constants.tableCaption());
      getCaptionBar().setStyleName(RTextStyles.STYLE_BOLD);
      codeField.setVisible(true);
      codeViewField.setVisible(false);
      clearValue();
    } else {
      setCaptionText(CommonsMessages.M.edit() + PPaymentTypeDef.constants.tableCaption());
      getCaptionBar().setStyleName(RTextStyles.STYLE_BOLD);
      codeField.setVisible(false);
      codeViewField.setVisible(true);
      codeViewField.setValue(entity.getCode());
      nameField.setValue(entity.getName());
      remarkField.setValue(entity.getRemark());
    }
    form.rebuild();
  }

  public void doLoad(String uuid) {
    doLoad(uuid, new Command() {

      @Override
      public void execute() {
        refresh(false);
        clearValidResults();
        nameField.setFocus(true);
        nameField.setSelectAllOnFocus(true);
      }
    });
  }

  private BPaymentType entity;
  private EPPaymentType ep = EPPaymentType.getInstance();
  private static final ButtonConfig BUTTON_CONFIRM = new ButtonConfig(
      PaymentTypeMessages.M.confirm());

  private RMessagePanel messagePanel;

  private RForm form;
  private RTextBox codeField;
  private RTextBox nameField;
  private RViewStringField codeViewField;
  private RTextArea remarkField;

  private Handler_textFied handler = new Handler_textFied();
  private List<Message> messages = new ArrayList<Message>();

  private boolean isCodeNotExist = true;

  private Widget drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    // panel.setSpacing(10);

    form = new RForm(1);
    form.setWidth("100%");
    panel.add(RSimplePanel.decorateMargin(form, 10, 0, 0, -10));

    codeField = new RTextBox(PPaymentTypeDef.code);
    codeField.addStyleName(RTextStyles.STYLE_BOLD);
    codeField.setVisible(true);
    codeField.addChangeHandler(handler);
    form.addField(codeField);

    codeViewField = new RViewStringField(PPaymentTypeDef.code);
    codeViewField.addStyleName(RTextStyles.STYLE_BOLD);
    codeViewField.setVisible(false);
    form.addField(codeViewField);

    nameField = new RTextBox(PPaymentTypeDef.name);
    nameField.addChangeHandler(handler);
    form.addField(nameField);

    remarkField = new RTextArea(PPaymentTypeDef.remark);
    remarkField.addChangeHandler(handler);
    form.addField(remarkField);

    form.rebuild();
    return panel;
  }

  private void doCreate(final Command callback) {
    RLoadingDialog.show(PaymentTypeMessages.M.view());
    PaymentTypeService.Locator.getService().create(new RBAsyncCallback2<BPaymentType>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = CommonsMessages.M.processError(CommonsMessages.M.create()
            + PPaymentTypeDef.TABLE_CAPTION);
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(BPaymentType result) {
        RLoadingDialog.hide();
        entity = result;
        callback.execute();
      }
    });
  }

  private void doLoad(String uuid, final Command callback) {
    RLoadingDialog.show(CommonsMessages.M.loading());
    PaymentTypeService.Locator.getService().load(uuid, new RBAsyncCallback2<BPaymentType>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = CommonsMessages.M.processError(CommonsMessages.M.load()
            + PPaymentTypeDef.TABLE_CAPTION);
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(BPaymentType result) {
        RLoadingDialog.hide();
        entity = result;
        callback.execute();
      }
    });
  }

  private void clearValue() {
    codeField.clearValue();
    nameField.clearValue();
    remarkField.clearValue();

    clearValidResults();
  }

  private void doSave() {
    if (validate() == false) {
      messagePanel.putMessages(getInvalidMessages());
      return;
    }

    GWTUtil.enableSynchronousRPC();
    PaymentTypeService.Locator.getService().save(entity, new RBAsyncCallback2<BPaymentType>() {

      @Override
      public void onException(Throwable caught) {
        String msg = CommonsMessages.M.processError(CommonsMessages.M.save()
            + PPaymentTypeDef.TABLE_CAPTION);
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(BPaymentType result) {
        hide();
        BPaymentTypeLogger.getInstance().log(PaymentTypeMessages.M.create(),result);
        
        JumpParameters params = new JumpParameters(PaymentTypeViewPage.START_NODE);
        params.getMessages().add(
            Message.info(CommonsMessages.M.onSuccess(CommonsMessages.M.save(),
                PPaymentTypeDef.TABLE_CAPTION, entity.toFriendlyStr())));
        ep.jump(params);
      }
    });
  }

  private class Handler_textFied implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == codeField) {
        entity.setCode(codeField.getValue());
        checkExist();
      } else if (event.getSource() == nameField) {
        entity.setName(nameField.getValue());
      } else if (event.getSource() == remarkField) {
        entity.setRemark(remarkField.getValue());
      }
    }

    private void checkExist() {
      final String code = entity.getCode();
      if (StringUtil.isNullOrBlank(code)) {
        return;
      }
      codeField.clearValidResults();
      isCodeNotExist = true;
      GWTUtil.enableSynchronousRPC();
      PaymentTypeService.Locator.getService().loadByCode(code,
          new RBAsyncCallback2<BPaymentType>() {

            @Override
            public void onException(Throwable caught) {
              RMsgBox.showError(PaymentTypeMessages.M.queryException(), caught);
            }

            @Override
            public void onSuccess(BPaymentType result) {
              if (result != null) {
                codeField.addErrorMessage(PaymentTypeMessages.M.codeRepeat(code));
                isCodeNotExist = false;
              }
            }
          });
    }
  }

}
