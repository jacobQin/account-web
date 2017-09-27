/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	RecDepositMoveCreatePage.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.receipt.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BDepositMove;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BDepositMoveLogger;
import com.hd123.m3.account.gwt.depositmove.intf.client.DepositMoveMessage;
import com.hd123.m3.account.gwt.depositmove.intf.client.dd.PDepositMoveDef;
import com.hd123.m3.account.gwt.depositmove.receipt.client.EPRecDepositMove;
import com.hd123.m3.account.gwt.depositmove.receipt.client.rpc.RecDepositMoveService;
import com.hd123.m3.account.gwt.depositmove.receipt.client.rpc.RecDepositMoveServiceAgent;
import com.hd123.m3.account.gwt.depositmove.receipt.client.ui.gadget.RecDepositMoveInInfoEditGadget;
import com.hd123.m3.account.gwt.depositmove.receipt.client.ui.gadget.RecDepositMoveOutInfoEditGadget;
import com.hd123.m3.account.gwt.depositmove.receipt.intf.client.RecDepositMoveUrlParams;
import com.hd123.m3.account.gwt.depositmove.receipt.intf.client.RecDepositMoveUrlParams.Create;
import com.hd123.m3.account.gwt.depositmove.receipt.intf.client.perm.RecDepositMovePermDef;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmCreatePage;
import com.hd123.m3.commons.gwt.util.client.navigator.ElementNavigatorMres;
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
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class RecDepositMoveCreatePage extends BaseBpmCreatePage implements Create, RValidatable {
  private static RecDepositMoveCreatePage instance = null;

  public static RecDepositMoveCreatePage getInstance() {
    if (instance == null)
      instance = new RecDepositMoveCreatePage();
    return instance;
  }

  public RecDepositMoveCreatePage() {
    super();
    drawToolbar();
    drawSelf();
  }

  private BDepositMove entity;

  private RAction saveAction;
  private RAction cancelAction;

  private RecDepositMoveOutInfoEditGadget outInfoGadget;
  private RecDepositMoveInInfoEditGadget inInfoGadget;

  private PermGroupEditField permGroupField;

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

    panel.add(drawGeneralGadget());

    panel.add(drawRemark());
  }

  private Widget drawGeneralGadget() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    vp.add(mvp);

    Widget w = drawOutInfoPanel();
    mvp.add(0, w);

    w = drawInInfoPanel();
    mvp.add(0, w);

    outInfoGadget.addActionHandler(inInfoGadget);
    inInfoGadget.addActionHandler(outInfoGadget);
    permGroupField = new PermGroupEditField(getEP().isPermEnabled(), getEP().getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(DepositMoveMessage.M.generalInfo());
    box.setWidth("100%");
    box.setEditing(true);
    box.setContent(vp);

    return box;
  }

  private Widget drawOutInfoPanel() {
    outInfoGadget = new RecDepositMoveOutInfoEditGadget();
    return outInfoGadget;
  }

  private Widget drawInInfoPanel() {
    inInfoGadget = new RecDepositMoveInInfoEditGadget();
    return inInfoGadget;
  }

  private Widget drawRemark() {
    remarkField = new RTextArea(PDepositMoveDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        entity.setRemark(remarkField.getValue());
      }
    });

    RCaptionBox box = new RCaptionBox();
    box.setEditing(true);
    box.setWidth("100%");
    box.setCaption(PDepositMoveDef.constants.remark());
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
    outInfoGadget.clearQueryConditions();
    inInfoGadget.clearQueryConditions();
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (checkIn() == false)
      return;

    doCreate(params, new Command() {

      @Override
      public void execute() {
        refresh(entity);
        getEP().appendSearchBox();
      }
    });
  }

  private boolean checkIn() {
    if (getEP().isBpmMode())
      return true;
    if (getEP().isPermitted(RecDepositMovePermDef.CREATE) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void doCreate(JumpParameters params, final Command callback) {
    String advanceUuid = params.getUrlRef().get(RecDepositMoveUrlParams.Create.PN_ADVANCE_UUID);

    if (StringUtil.isNullOrBlank(advanceUuid) == false)
      doCreateByAdvance(advanceUuid, callback);
    else
      doCreate(callback);
  }

  private void doCreate(final Command callback) {
    RLoadingDialog.show(DepositMoveMessage.M.actionDoing(DepositMoveMessage.M.create()));
    RecDepositMoveService.Locator.getService().create(new RBAsyncCallback2<BDepositMove>() {
      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = DepositMoveMessage.M.actionFailed(DepositMoveMessage.M.create(), getEP()
            .getModuleCaption());
        RMsgBox.showErrorAndBack(msg, caught);
      }

      public void onSuccess(BDepositMove result) {
        RLoadingDialog.hide();
        entity = result;
        callback.execute();
      }
    });
  }

  private void doCreateByAdvance(String advanceUuid, final Command callback) {
    RLoadingDialog.show(DepositMoveMessage.M.actionDoing(DepositMoveMessage.M.create()));
    RecDepositMoveService.Locator.getService().createByAdvance(advanceUuid,
        new RBAsyncCallback2<BDepositMove>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMoveMessage.M.actionFailed(DepositMoveMessage.M.create(), getEP()
                .getModuleCaption());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BDepositMove result) {
            RLoadingDialog.hide();
            entity = result;
            callback.execute();
          }
        });
  }

  private class Handler_saveAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      doSave(false);
    }
  }

  private void doSave(final boolean continueCreate) {
    GWTUtil.blurActiveElement();
    if (validate() == false)
      return;
    RLoadingDialog.show(DepositMoveMessage.M.actionDoing(DepositMoveMessage.M.save()));
    RecDepositMoveService.Locator.getService().save(entity, getEP().getProcessCtx(),
        new RBAsyncCallback2<BDepositMove>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMoveMessage.M.actionFailed(DepositMoveMessage.M.save(), getEP()
                .getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(BDepositMove result) {
            RLoadingDialog.hide();
            BDepositMoveLogger.getInstance().log(DepositMoveMessage.M.create(),result);
            
            JumpParameters params = null;
            String msg = DepositMoveMessage.M.onSuccess(DepositMoveMessage.M.save(), getEP()
                .getModuleCaption(), result.toFriendlyStr());
            if (continueCreate == false) {
              getEP().jumpToViewPage(result.getUuid(), Message.info(msg));
            } else {
              params = new JumpParameters(START_NODE);
              params.getMessages().add(Message.info(msg));
              params.getUrlRef().set(ElementNavigatorMres.KEY_SHOW_NAVIGATOR, "false");
              getEP().jump(params);
            }
          }
        });
  }

  private class Handler_cancelAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RMsgBox.showConfirm(DepositMoveMessage.M.confirmCancel(DepositMoveMessage.M.create()),
          new RMsgBox.ConfirmCallback() {
            public void onClosed(boolean confirmed) {
              if (confirmed)
                RHistory.back();
            }
          });
    }
  }

  @Override
  public void clearValidResults() {
    outInfoGadget.clearValidResults();
    inInfoGadget.clearValidResults();
    permGroupField.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return outInfoGadget.isValid() && permGroupField.isValid() && remarkField.isValid()
        && inInfoGadget.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> list = new ArrayList<Message>();
    list.addAll(outInfoGadget.getInvalidMessages());
    list.addAll(inInfoGadget.getInvalidMessages());
    list.addAll(permGroupField.getInvalidMessages());
    list.addAll(remarkField.getInvalidMessages());
    return list;
  }

  @Override
  public boolean validate() {
    getMessagePanel().clearMessages();
    boolean valid = outInfoGadget.validate();
    valid &= inInfoGadget.validate();
    valid &= remarkField.validate();
    valid &= permGroupField.validate();
    valid &= validateRepeat();
    if (valid == false)
      getMessagePanel().putMessages(getInvalidMessages());
    if (validateRepeat() == false) {
      getMessagePanel().putErrorMessage(
          DepositMoveMessage.M.out() + DepositMoveMessage.M.info() + "和"
              + DepositMoveMessage.M.in() + DepositMoveMessage.M.info()
              + DepositMoveMessage.M.notSame() + "。");
    }
    return valid;
  }

  private boolean validateRepeat() {
    if (entity == null || entity.getOutCounterpart() == null
        || entity.getOutCounterpart().getUuid() == null || entity.getOutSubject() == null
        || entity.getOutSubject().getUuid() == null || entity.getInCounterpart() == null
        || entity.getInCounterpart().getUuid() == null || entity.getInSubject() == null
        || entity.getInSubject().getUuid() == null)
      return true;
    if (((entity.getOutContract() == null || entity.getOutContract().getUuid() == null) && (entity
        .getInContract() == null || entity.getInContract().getUuid() == null))
        && entity.getOutCounterpart().getUuid().equals(entity.getInCounterpart().getUuid())
        && entity.getOutSubject().getUuid().equals(entity.getInSubject().getUuid())) {
      return false;
    }
    if (entity.getOutContract() != null && entity.getOutContract().getUuid() != null
        && entity.getInContract() != null && entity.getInContract().getUuid() != null
        && entity.getOutContract().getUuid().equals(entity.getInContract().getUuid())
        && entity.getOutCounterpart().getUuid().equals(entity.getInCounterpart().getUuid())
        && entity.getOutSubject().getUuid().equals(entity.getInSubject().getUuid())) {
      return false;
    }
    return true;
  }

  @Override
  protected EPRecDepositMove getEP() {
    return EPRecDepositMove.getInstance();
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
      RecDepositMoveServiceAgent.executeTask(processCtx, operation, entity, true, this);
    }
  }

  @Override
  protected void refreshEntity() {
    assert entity != null;
    if (entity.getAccountUnit() == null && getEP().getUserStores().size() == 1) {
      entity.setAccountUnit(new BUCN(getEP().getUserStores().get(0)));
    }

    outInfoGadget.refresh(entity);
    inInfoGadget.refresh(entity);
    permGroupField.setPerm(entity);
    remarkField.setValue(entity.getRemark());

    clearValidResults();
    setFocusOnFirstFeild();
  }

  private void setFocusOnFirstFeild() {
    outInfoGadget.setFocusOnFirstField();
  }
}
