/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 *
 * 项目名： M3
 * 文件名： BankEditPage.java
 * 模块说明：
 * 修改历史：
 * 2013-817 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.bank.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.bank.client.BankMessages;
import com.hd123.m3.account.gwt.bank.client.EPBank;
import com.hd123.m3.account.gwt.bank.client.biz.BBank;
import com.hd123.m3.account.gwt.bank.client.biz.BBankLogger;
import com.hd123.m3.account.gwt.bank.client.rpc.BBankLoader;
import com.hd123.m3.account.gwt.bank.client.rpc.BankService;
import com.hd123.m3.account.gwt.bank.intf.client.BankUrlParams.Edit;
import com.hd123.m3.account.gwt.bank.intf.client.dd.PBankDef;
import com.hd123.m3.account.gwt.bank.intf.client.perm.BankPermDef;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.StoreLinkField;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.history.RHistory;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author chenrizhang
 * 
 */
public class BankEditPage extends BaseContentPage implements Edit, RValidatable {
  private static BankEditPage instance;
  private static EPBank ep = EPBank.getInstance();

  public static BankEditPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new BankEditPage();
    return instance;
  }

  public BankEditPage() throws ClientBizException {
    super();
    try {
      drawToolbar();
      drawSelf();
    } catch (Exception e) {
      throw new ClientBizException(BankMessages.M.cannotCreatePage("BankEditPage"), e);
    }
  }

  private BBank entity;

  private RAction saveAction;
  private RAction cancelAction;

  private RForm basicForm;
  private RViewStringField codeField;
  private RTextBox nameField;
  private StoreLinkField storeField;
  private RTextBox bankField;
  private RTextArea addressField;

  private RForm accountForm;
  private RTextBox accountField;

  private RForm operateForm;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;

  // 组件
  private RTextArea remarkField;

  // 句柄
  private ValueHandler valueHandler = new ValueHandler();
  private ActionHandler actionHandler = new ActionHandler();

  private void drawToolbar() {
    saveAction = new RAction(RActionFacade.SAVE, actionHandler);
    RToolbarButton saveButton = new RToolbarButton(saveAction);
    getToolbar().add(saveButton);

    cancelAction = new RAction(RActionFacade.CANCEL, actionHandler);
    RToolbarButton cancelButton = new RToolbarButton(cancelAction);
    getToolbar().add(cancelButton);
  }

  private void drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    initWidget(panel);

    panel.add(drawGeneralGadget());
    panel.add(drawRemarkGadget());
  }

  private Widget drawGeneralGadget() {
    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    mvp.setSpacing(5);

    mvp.add(0, drawBasicGadget());
    mvp.add(1, drawOperateGadget());
    mvp.add(1, drawAccountGadget());

    RCaptionBox box = new RCaptionBox();
    box.setEditing(true);
    box.setCaption(BankMessages.M.generalInfo());
    box.setWidth("100%");
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(mvp);
    return box;
  }

  private Widget drawBasicGadget() {
    basicForm = new RForm(1);
    basicForm.setWidth("100%");

    codeField = new RViewStringField(PBankDef.code);
    codeField.addStyleName(RTextStyles.STYLE_BOLD);
    codeField.setWidth("100%");
    basicForm.addField(codeField);

    nameField = new RTextBox(PBankDef.name);
    nameField.setWidth("100%");
    nameField.addValueChangeHandler(valueHandler);
    basicForm.addField(nameField);

    storeField = new StoreLinkField(PBankDef.constants.store());
    basicForm.addField(storeField);

    bankField = new RTextBox(PBankDef.bank);
    bankField.setWidth("100%");
    bankField.addValueChangeHandler(valueHandler);
    basicForm.addField(bankField);

    addressField = new RTextArea(PBankDef.address);
    addressField.setWidth("100%");
    addressField.addValueChangeHandler(valueHandler);
    basicForm.addField(addressField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(BankMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(basicForm);
    return box;
  }

  private Widget drawAccountGadget() {
    accountForm = new RForm(1);
    accountForm.setWidth("100%");

    accountField = new RTextBox(PBankDef.account);
    accountField.setWidth("100%");
    accountField.addValueChangeHandler(valueHandler);
    accountForm.addField(accountField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(BankMessages.M.account());
    box.setWidth("100%");
    box.setContent(accountForm);
    return box;
  }

  private Widget drawOperateGadget() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    createInfoField = new RSimpleOperateInfoField(PBankDef.createInfo);
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(PBankDef.lastModifyInfo);
    operateForm.addField(lastModifyInfoField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(BankMessages.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea(PBankDef.remark);
    remarkField.setWidth("100%");
    remarkField.addValueChangeHandler(valueHandler);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PBankDef.constants.remark());
    box.setWidth("100%");
    box.setEditing(true);
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }

  @Override
  public void clearValidResults() {
    getMessagePanel().clearMessages();
    basicForm.clearValidResults();
    accountForm.clearValidResults();
    remarkField.clearValidResults();
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> list = new ArrayList<Message>();
    list.addAll(basicForm.getInvalidMessages());
    list.addAll(accountForm.getInvalidMessages());
    list.addAll(remarkField.getInvalidMessages());
    return list;
  }

  @Override
  public boolean isValid() {
    return basicForm.isValid() && accountForm.isValid() && remarkField.isValid();
  }

  @Override
  public boolean validate() {
    getMessagePanel().clearMessages();
    boolean isValid = basicForm.validate();
    isValid &= accountForm.validate();
    isValid &= remarkField.validate();
    if (isValid == false)
      getMessagePanel().putMessages(getInvalidMessages());
    return isValid;
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);
    if (checkIn() == false)
      return;
    BBankLoader.decodeParams(params, new BBankLoader.Callback() {

      @Override
      public void execute(BBank result) {
        entity = result;
        refreshTitle();
        refreshEntity();
        basicForm.focusOnFirstField();
      }
    });
  }

  private boolean checkIn() {
    if (ep.isPermitted(BankPermDef.UPDATE) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(ep.getModuleCaption());
    if (entity != null) {
      ep.getTitleBar().appendAttributeText(entity.getCode());
      ep.getTitleBar().appendAttributeText(entity.getName());
    }
  }

  private void refreshEntity() {
    codeField.setValue(entity.getCode());
    nameField.setValue(entity.getName());
    storeField.setRawValue(entity.getStore());
    bankField.setValue(entity.getBank());
    addressField.setValue(entity.getAddress());
    createInfoField.setOperateInfo(entity.getCreateInfo());
    lastModifyInfoField.setOperateInfo(entity.getLastModifyInfo());
    accountField.setValue(entity.getAccount());
    remarkField.setValue(entity.getRemark());
  }

  private class ValueHandler implements ValueChangeHandler {
    @Override
    public void onValueChange(ValueChangeEvent event) {
      if (event.getSource() == nameField) {
        entity.setName(nameField.getValue());
      } else if (event.getSource() == bankField) {
        entity.setBank(bankField.getValue());
      } else if (event.getSource() == addressField) {
        entity.setAddress(addressField.getValue());
      } else if (event.getSource() == accountField) {
        entity.setAccount(accountField.getValue());
      } else if (event.getSource() == remarkField) {
        entity.setRemark(remarkField.getValue());
      }
    }
  }

  private class ActionHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == cancelAction) {
        doCancel();
      } else if (event.getSource() == saveAction) {
        doSave();
      }
    }
  }

  private void doSave() {
    GWTUtil.blurActiveElement();
    if (!validate())
      return;
    RLoadingDialog.show(BankMessages.M.actionDoing(BankMessages.M.save()));
    BankService.Locator.getService().save(entity, new RBAsyncCallback2<BBank>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = BankMessages.M.actionFailed(BankMessages.M.save(), ep.getModuleCaption());
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(BBank result) {
        RLoadingDialog.hide();
        // 记日志
        BBankLogger.getInstance().log(BankMessages.M.modify(), result);
        JumpParameters params = new JumpParameters(BankViewPage.START_NODE);
        params.getUrlRef().set(BankViewPage.PN_ENTITY_UUID, result.getUuid());
        params.getExtend().put(EPBank.OPN_ENTITY, result);
        params.getMessages().add(
            Message.info("保存" + ep.getModuleCaption() + "(" + result.toFriendlyStr() + ")成功。"));
        ep.jump(params);
      }
    });
  }

  private void doCancel() {
    String msg = BankMessages.M.actionComfirm(BankMessages.M.cancel(), BankMessages.M.edit());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed)
          RHistory.back();
      }
    });
  }

}
