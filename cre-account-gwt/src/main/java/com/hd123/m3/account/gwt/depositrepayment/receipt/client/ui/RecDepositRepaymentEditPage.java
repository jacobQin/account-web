/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	RecDepositRepaymentEditPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-25 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.receipt.client.ui;

import java.math.BigDecimal;
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
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepaymentLine;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepaymentLogger;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.DepositRepaymentMessage;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.dd.PDepositRepaymentDef;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.EPRecDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.rpc.RecDepositRepaymentLoader;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.rpc.RecDepositRepaymentService;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.rpc.RecDepositRepaymentServiceAgent;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.ui.gadget.RecDepositRepaymentGeneralEditGadget;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.ui.gadget.RecDepositRepaymentLineEditGrid;
import com.hd123.m3.account.gwt.depositrepayment.receipt.intf.client.RecDepositRepaymentUrlParams.Edit;
import com.hd123.m3.account.gwt.depositrepayment.receipt.intf.client.perm.RecDepositRepaymentPermDef;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmEditPage;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.history.RHistory;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.util.client.message.MessageLevel;
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
 * @author zhuhairui
 * 
 */
public class RecDepositRepaymentEditPage extends BaseBpmEditPage implements Edit, RValidatable,
    HasRActionHandlers {
  private static RecDepositRepaymentEditPage instance = null;

  public static RecDepositRepaymentEditPage getInstance() {
    if (instance == null)
      instance = new RecDepositRepaymentEditPage();
    return instance;
  }

  public RecDepositRepaymentEditPage() {
    super();
    drawToolbar();
    drawSelf();
    afterDraw();
  }

  private BDepositRepayment entity;

  private RAction saveAction;
  private RAction cancelAction;

  private RecDepositRepaymentGeneralEditGadget generalGadget;
  private RecDepositRepaymentLineEditGrid lineGridGadget;
  private RTextArea remarkField;

  private void drawToolbar() {
    saveAction = new RAction(RActionFacade.SAVE, new Handler_saveAction());
    RToolbarButton saveButton = new RToolbarButton(saveAction);
    getToolbar().add(saveButton);

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

    generalGadget = new RecDepositRepaymentGeneralEditGadget();
    panel.add(generalGadget);

    panel.add(drawMiddle());
    panel.add(drawRemark());
  }

  private Widget drawMiddle() {
    lineGridGadget = new RecDepositRepaymentLineEditGrid();
    return lineGridGadget;
  }

  private Widget drawRemark() {
    remarkField = new RTextArea(PDepositRepaymentDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        entity.setRemark(remarkField.getValue());
      }
    });

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PDepositRepaymentDef.constants.remark());
    box.setWidth("100%");
    box.setEditing(true);
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }

  private void afterDraw() {
    // 定位错误消息行。
    this.addActionHandler(lineGridGadget);
    generalGadget.addActionHandler(lineGridGadget);

    // 通知总金额改变
    lineGridGadget.addActionHandler(generalGadget);
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (checkIn() == false)
      return;

    RecDepositRepaymentLoader.decodeParams(params, new Command() {
      @Override
      public void execute() {
        entity = RecDepositRepaymentLoader.getEntity();
        refresh(entity);
        getEP().appendSearchBox();
      }
    });
  }

  private boolean checkIn() {
    if (getEP().isProcessMode())
      return true;
    if (getEP().isPermitted(RecDepositRepaymentPermDef.UPDATE) == false) {
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
    if (valid == false) {
      getMessagePanel().putMessages(getInvalidMessages());
    }
    if (getEP().isEnableByDeposit() && !validateLines().isEmpty()) {
      getMessagePanel().putMessages(validateLines());
      return false;
    }
    return valid;
  }

  /**
   * 选择了预存款单，表格不可编辑将手动验证
   * 
   * @return
   */
  private List<Message> validateLines() {
    List<Message> list = new ArrayList<Message>();
    BUCN deposit = entity.getDeposit();
    List<BDepositRepaymentLine> lines = entity.getLines();
    if (deposit != null) {
      for (BDepositRepaymentLine line : lines) {
        if (line.getAmount().compareTo(line.getRemainAmount()) > 0) {
          String errStr = "还款明细/第" + (line.getLineNumber()) + "行/金额：不能大于账户余额";
          Message message = new Message(errStr, MessageLevel.ERROR);
          list.add(message);
        } else if (line.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
          String errStr = "还款明细/第" + (line.getLineNumber()) + "行/金额：必须大于0";
          Message message = new Message(errStr, MessageLevel.ERROR);
          list.add(message);
        }
      }
    }
    return list;
  }

  private class Handler_saveAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      doSave();
    }
  }

  private void doSave() {
    GWTUtil.blurActiveElement();
    if (validate() == false)
      return;
    RLoadingDialog.show(DepositRepaymentMessage.M.actionDoing(DepositRepaymentMessage.M.save()));
    RecDepositRepaymentService.Locator.getService().save(entity.getFilterData(),
        getEP().getProcessCtx(), new RBAsyncCallback2<BDepositRepayment>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositRepaymentMessage.M.actionFailed(DepositRepaymentMessage.M.save(),
                getEP().getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(BDepositRepayment result) {
            RLoadingDialog.hide();
            BDepositRepaymentLogger.getInstance().log(DepositRepaymentMessage.M.modify(), result);

            String msg = DepositRepaymentMessage.M.onSuccess(DepositRepaymentMessage.M.save(),
                getEP().getModuleCaption(), entity.toFriendlyStr());
            getEP().jumpToViewPage(entity.getUuid(), Message.info(msg));
          }
        });
  }

  private class Handler_cancelAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RMsgBox.showConfirm(
          DepositRepaymentMessage.M.confirmCancel(DepositRepaymentMessage.M.edit()),
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
    saveAction.setVisible(getEP().isPermitted(RecDepositRepaymentPermDef.UPDATE));
  }

  @Override
  protected EPRecDepositRepayment getEP() {
    return EPRecDepositRepayment.getInstance();
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
      RecDepositRepaymentServiceAgent.executeTask(processCtx, operation, entity, true, this);
    }
  }

  @Override
  protected void refreshEntity() {
    assert entity != null;

    generalGadget.refresh(entity);
    lineGridGadget.setEntity(entity);
    remarkField.setValue(entity.getRemark());

    clearValidResults();
    setFocusOnFirstFeild();
  }

  private void setFocusOnFirstFeild() {
    generalGadget.setFocusOnFirstField();
  }

}
