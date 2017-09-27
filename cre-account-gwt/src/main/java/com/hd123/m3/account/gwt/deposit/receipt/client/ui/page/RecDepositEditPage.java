/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： PayEditPage.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.receipt.client.ui.page;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositLogger;
import com.hd123.m3.account.gwt.deposit.intf.client.DepositMessage;
import com.hd123.m3.account.gwt.deposit.intf.client.dd.PDepositDef;
import com.hd123.m3.account.gwt.deposit.receipt.client.EPRecDeposit;
import com.hd123.m3.account.gwt.deposit.receipt.client.rpc.RecDepositLoader;
import com.hd123.m3.account.gwt.deposit.receipt.client.rpc.RecDepositService;
import com.hd123.m3.account.gwt.deposit.receipt.client.rpc.RecDepositServiceAgent;
import com.hd123.m3.account.gwt.deposit.receipt.client.ui.gadget.RecDepositGeneralEditGadget;
import com.hd123.m3.account.gwt.deposit.receipt.client.ui.gadget.RecDepositLineEditGrid;
import com.hd123.m3.account.gwt.deposit.receipt.intf.client.RecDepositUrlParams.Edit;
import com.hd123.m3.account.gwt.deposit.receipt.intf.client.perm.RecDepositPermDef;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmEditPage;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.history.RHistory;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author chenpeisi
 * 
 */
public class RecDepositEditPage extends BaseBpmEditPage implements Edit, RValidatable,
     HasRActionHandlers {

  private static RecDepositEditPage instance = null;

  public static RecDepositEditPage getInstance() {
    if (instance == null)
      instance = new RecDepositEditPage();
    return instance;
  }

  public RecDepositEditPage() {
    super();
    drawToolbar();
    drawSelf();
    afterDraw();
  }

  private BDeposit entity;

  private RAction saveAction;
  private RAction cancelAction;

  private RecDepositGeneralEditGadget generalGadget;
  private RecDepositLineEditGrid lineGridGadget;
  private RTextArea remarkField;

  private void drawToolbar() {
    saveAction = new RAction(RActionFacade.SAVE, new Handler_saveAction());
    RToolbarButton saveButton = new RToolbarButton(saveAction);
    getToolbar().add(saveButton);

    // BPM出口按钮
    injectBpmActions();

    cancelAction = new RAction(RActionFacade.CANCEL, new Handler_cancelAction());
    RToolbarButton cancelButton = new RToolbarButton(cancelAction);
    getToolbar().add(cancelButton);
  }

  private void drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    initWidget(panel);

    generalGadget = new RecDepositGeneralEditGadget();
    panel.add(generalGadget);

    panel.add(drawMiddle());
    panel.add(drawRemark());
  }

  private Widget drawMiddle() {
    lineGridGadget = new RecDepositLineEditGrid();
    return lineGridGadget;
  }

  private Widget drawRemark() {
    remarkField = new RTextArea(PDepositDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        entity.setRemark(remarkField.getValue());
      }
    });

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PDepositDef.constants.remark());
    box.setWidth("100%");
    box.setEditing(true);
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }

  private void afterDraw() {
    // 定位错误消息行。
    this.addActionHandler(lineGridGadget);
    // 改变标题。
    generalGadget.addActionHandler(lineGridGadget);

    // 通知总金额改变
    lineGridGadget.addActionHandler(generalGadget);
  }

  public void onShow(final JumpParameters params) {
    super.onShow(params);

    if (checkIn() == false)
      return;

    RecDepositLoader.decodeParams(params, new Command() {
      @Override
      public void execute() {
        entity = RecDepositLoader.getEntity();
        refresh(entity);
        getEP().appendSearchBox();
      }
    });
  }

  private boolean checkIn() {
    if (getEP().isProcessMode())
      return true;
    if (getEP().isPermitted(RecDepositPermDef.UPDATE) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    generalGadget.clearQueryConditions();
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void clearValidResults() {
    generalGadget.clearValidResults();
    remarkField.clearValidResults();
    lineGridGadget.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return generalGadget.isValid() && remarkField.isValid() && lineGridGadget.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> list = new ArrayList<Message>();
    list.addAll(generalGadget.getInvalidMessages());
    list.addAll(remarkField.getInvalidMessages());
    list.addAll(lineGridGadget.getInvalidMessages());
    return list;
  }

  @Override
  public boolean validate() {
    clearValidResults();
    getMessagePanel().clearMessages();
    boolean valid = generalGadget.validate();
    valid &= remarkField.validate();
    valid &= lineGridGadget.validate();
    if (valid == false)
      getMessagePanel().putMessages(getInvalidMessages());
    return valid;
  }
  
  private void doSave() {
    GWTUtil.blurActiveElement();
    if (validate() == false)
      return;
    RLoadingDialog.show(DepositMessage.M.actionDoing(DepositMessage.M.save()));
    RecDepositService.Locator.getService().save(entity.getFilterData(), getEP().getProcessCtx(),
        new RBAsyncCallback2<BDeposit>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMessage.M.actionFailed(DepositMessage.M.save(), getEP()
                .getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(BDeposit result) {
            RLoadingDialog.hide();
            BDepositLogger.getInstance().log(DepositMessage.M.modify(),entity);
            
            String msg = DepositMessage.M.onSuccess(DepositMessage.M.save(), getEP()
                .getModuleCaption(), result.toFriendlyStr());
            getEP().jumpToViewPage(entity.getUuid(), Message.info(msg));
          }
        });
  }

  private class Handler_saveAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      doSave();
    }
  }

  private class Handler_cancelAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RMsgBox.showConfirm(DepositMessage.M.confirmCancel(DepositMessage.M.edit()),
          new RMsgBox.ConfirmCallback() {
            public void onClosed(boolean confirmed) {
              if (confirmed)
                RHistory.back();
            }
          });
    }
  }

  @Override
  public void refreshToolbar(RToolbar toolbar) {
    saveAction.setVisible(getEP().isPermitted(RecDepositPermDef.UPDATE));
  }

  @Override
  protected EPRecDeposit getEP() {
    return EPRecDeposit.getInstance();
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
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
      RecDepositServiceAgent.executeTask(processCtx, operation, entity, true, this);
    }
  }

  @Override
  protected void refreshEntity() {
    assert entity != null;
    generalGadget.refresh(entity);
    lineGridGadget.setEntity(entity);
    remarkField.setValue(entity.getRemark());
    clearValidResults();
  }
}
