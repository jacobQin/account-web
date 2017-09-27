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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.bank.client.BankMessages;
import com.hd123.m3.account.gwt.bank.client.EPBank;
import com.hd123.m3.account.gwt.bank.client.biz.BBank;
import com.hd123.m3.account.gwt.bank.client.biz.BBankLogger;
import com.hd123.m3.account.gwt.bank.client.rpc.BBankLoader;
import com.hd123.m3.account.gwt.bank.client.rpc.BankService;
import com.hd123.m3.account.gwt.bank.intf.client.BankUrlParams;
import com.hd123.m3.account.gwt.bank.intf.client.BankUrlParams.View;
import com.hd123.m3.account.gwt.bank.intf.client.dd.PBankDef;
import com.hd123.m3.account.gwt.bank.intf.client.perm.BankPermDef;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.StoreLinkField;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.misc.RHyperlink;
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
public class BankViewPage extends BaseContentPage implements View {
  private static BankViewPage instance;
  private static EPBank ep = EPBank.getInstance(EPBank.class);

  public static BankViewPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new BankViewPage();
    return instance;
  }

  public BankViewPage() throws ClientBizException {
    super();
    try {
      drawToolbar();
      drawSelf();
    } catch (Exception e) {
      throw new ClientBizException(BankMessages.M.cannotCreatePage("BankViewPage"), e);
    }
  }

  private BBank entity;

  private RAction createAction;
  private RAction editAction;
  private RAction deleteAction;

  private RForm basicForm;
  private RViewStringField codeField;
  private RViewStringField nameField;
  private StoreLinkField storeField;
  private RViewStringField bankField;
  private RViewStringField addressField;

  private RForm accountForm;
  private RViewStringField accountField;

  private RForm operateForm;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;
  private RHyperlink moreInfo;

  // 组件
  private RTextArea remarkField;

  // 句柄
  private ActionHandler actionHandler = new ActionHandler();

  private void drawToolbar() {
    createAction = new RAction(RActionFacade.CREATE, actionHandler);
    getToolbar().add(new RToolbarButton(createAction));

    editAction = new RAction(RActionFacade.EDIT, actionHandler);
    getToolbar().add(new RToolbarButton(editAction));

    deleteAction = new RAction(RActionFacade.DELETE, actionHandler);
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    getToolbar().add(deleteButton);
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
    box.setCaption(BankMessages.M.generalInfo());
    box.getCaptionBar().setShowCollapse(true);
    box.setWidth("100%");
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

    nameField = new RViewStringField(PBankDef.name);
    nameField.setWidth("100%");
    basicForm.addField(nameField);

    storeField = new StoreLinkField(PBankDef.constants.store());
    basicForm.addField(storeField);

    bankField = new RViewStringField(PBankDef.bank);
    bankField.setWidth("100%");
    basicForm.addField(bankField);

    addressField = new RViewStringField(PBankDef.address);
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

    accountField = new RViewStringField(PBankDef.account);
    accountField.setWidth("100%");
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

    moreInfo = new RHyperlink();
    moreInfo.addClickHandler(actionHandler);
    moreInfo.setHTML(BankMessages.M.moreInfo());
    operateForm.addField(moreInfo);

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
    remarkField.setReadOnly(true);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PBankDef.constants.remark());
    box.setWidth("100%");
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
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
        refreshCommand();
        refreshEntity();
      }
    });
  }

  private boolean checkIn() {
    if (ep.isPermitted(BankPermDef.READ) == false) {
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

  private void refreshCommand() {
    createAction.setEnabled(ep.isPermitted(BankPermDef.CREATE));
    editAction.setEnabled(ep.isPermitted(BankPermDef.UPDATE));
    deleteAction.setEnabled(ep.isPermitted(BankPermDef.DELETE));
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

  private class ActionHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == createAction) {
        doCreate();
      } else if (event.getSource() == editAction) {
        doEdit();
      } else if (event.getSource() == deleteAction) {
        doDeleteConfirm();
      } else if (event.getSource() == moreInfo) {
        doViewLog();
      }
    }
  }

  private void doCreate() {
    JumpParameters params = new JumpParameters(BankUrlParams.Create.START_NODE);
    ep.jump(params);
  }

  private void doEdit() {
    JumpParameters params = new JumpParameters(BankEditPage.START_NODE);
    params.getUrlRef().set(BankUrlParams.Edit.PN_ENTITY_UUID, entity.getUuid());
    ep.jump(params);
  }

  private void doDeleteConfirm() {
    getMessagePanel().clearMessages();

    String msg = BankMessages.M.actionComfirm2(BankMessages.M.delete(), ep.getModuleCaption(),
        entity.toFriendlyStr());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doDelete();
      }
    });
  }

  private void doDelete() {
    RLoadingDialog.show(BankMessages.M.actionDoing(BankMessages.M.delete()));
    BankService.Locator.getService().delete(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2<Void>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = BankMessages.M.actionFailed(BankMessages.M.delete(), ep.getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            // 记日志
            BBankLogger.getInstance().log(BankMessages.M.delete(), entity);
            String msg = BankMessages.M.onSuccess(BankMessages.M.delete(), ep.getModuleCaption(),
                entity.toFriendlyStr());
            JumpParameters params = new JumpParameters(BankSearchPage.START_NODE);
            params.getMessages().add(Message.info(msg));
            ep.jump(params);
          }
        });
  }

  private void doViewLog() {
    JumpParameters params = new JumpParameters(BankLogPage.START_NODE);
    params.getUrlRef().set(BankUrlParams.Log.PN_ENTITY_UUID, entity.getUuid());
    ep.jump(params);
  }

}
