/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StatementCreatePage.java
 * 模块说明：	
 * 修改历史：
 * 2014年11月4日 - zhr - 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.m3.account.gwt.statement.client.StatementMessages;
import com.hd123.m3.account.gwt.statement.client.biz.BStatement;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementLogger;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementService;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementServiceAgent;
import com.hd123.m3.account.gwt.statement.client.ui.gadget.AttachmentEditGadget;
import com.hd123.m3.account.gwt.statement.client.ui.gadget.StatementGeneralCreateGadget;
import com.hd123.m3.account.gwt.statement.client.ui.gadget.StatementLinePatchEditGridGadget;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams.Create;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementDef;
import com.hd123.m3.account.gwt.statement.intf.client.perm.StatementPermDef;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmCreatePage;
import com.hd123.rumba.gwt.base.client.BUCN;
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
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.TitleBar;
import com.hd123.rumba.webframe.gwt.entrypoint.client.history.HistoryHelper;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhr
 * 
 */
public class StatementCreatePage extends BaseBpmCreatePage implements Create, RValidatable {

  private static StatementCreatePage instance;

  public static StatementCreatePage getInstance() {
    if (instance == null)
      instance = new StatementCreatePage();
    return instance;
  }

  public StatementCreatePage() {
    super();
    drawToolbar();
    drawSelf();
  }

  @Override
  public void onHide() {
    generalGadget.onHide();
  }

  @Override
  public void clearValidResults() {
    getMessagePanel().clearMessages();
    generalGadget.clearValidResults();
    lineEditGrid.clearValidResults();
    remarkField.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return generalGadget.isValid() && lineEditGrid.isValid() && remarkField.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> list = new ArrayList<Message>();
    list.addAll(generalGadget.getInvalidMessages());
    list.addAll(lineEditGrid.getInvalidMessages());
    list.addAll(lineEditGrid.getInvalidMessages());
    list.addAll(remarkField.getInvalidMessages());
    return list;
  }

  @Override
  public boolean validate() {
    getMessagePanel().clearMessages();
    boolean isValid = generalGadget.validate();
    isValid &= lineEditGrid.validate();
    isValid &= lineEditGrid.validateLines();
    isValid &= remarkField.validate();
    if (isValid == false)
      getMessagePanel().putMessages(getInvalidMessages());
    return isValid;
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (!checkIn()) {
      return;
    }

    doCreate(new Command() {

      @Override
      public void execute() {
        refresh(entity);
        getEP().appendSearchBox();
      }
    });
  }

  private BStatement entity;
  private RAction saveAction;
  private RAction cancleAction;

  private StatementGeneralCreateGadget generalGadget;
  private StatementLinePatchEditGridGadget lineEditGrid;
  private AttachmentEditGadget attachmentGadget;
  private RTextArea remarkField;

  private Handler_Click clickHandler = new Handler_Click();

  private void drawToolbar() {
    saveAction = new RAction(RActionFacade.SAVE, clickHandler);
    getToolbar().add(new RToolbarButton(saveAction));

    // BPM出口按钮
    injectBpmActions();

    cancleAction = new RAction(RActionFacade.CANCEL, clickHandler);
    getToolbar().add(new RToolbarButton(cancleAction));
  }

  private void drawSelf() {
    RVerticalPanel root = new RVerticalPanel();
    root.setWidth("100%");
    root.setSpacing(8);
    initWidget(root);

    Widget w = drawGeneralGadget();
    root.add(w);

    w = drawLineEditGridGadget();
    root.add(w);

    generalGadget.addActionHandler(lineEditGrid);
    lineEditGrid.addActionHandler(generalGadget);

    w = drawAttachmentGadget();
    root.add(w);

    w = drawRemarkGadget();
    root.add(w);
  }

  private Widget drawGeneralGadget() {
    generalGadget = new StatementGeneralCreateGadget();
    return generalGadget;
  }

  private Widget drawLineEditGridGadget() {
    lineEditGrid = new StatementLinePatchEditGridGadget();
    return lineEditGrid;
  }

  private Widget drawAttachmentGadget() {
    attachmentGadget = new AttachmentEditGadget();

    RCaptionBox box = new RCaptionBox();
    box.getCaptionBar().setShowCollapse(true);
    box.setEditing(true);
    box.setContentSpacing(0);
    box.setCaption(StatementMessages.M.attachement());
    box.setContent(attachmentGadget);
    box.setWidth("100%");
    return box;
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea(PStatementDef.remark);
    remarkField.addChangeHandler(new RemarkChange());
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    RCaptionBox box = new RCaptionBox();
    box.setEditing(true);
    box.setCaption(PStatementDef.constants.remark());
    box.setWidth("100%");
    box.setContent(remarkField);
    box.getCaptionBar().setShowCollapse(true);
    return box;
  }

  private boolean checkIn() {
    if (getEP().isBpmMode()) {
      return true;
    }
    if (getEP().isPermitted(StatementPermDef.CREATE) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  @Override
  public void refreshTitle(TitleBar titleBar) {
    titleBar.clearStandardTitle();
    titleBar.setTitleText(StatementMessages.M.create());
    titleBar.appendAttributeText(PStatementDef.TABLE_CAPTION);
  }

  private void doCreate(final Command callback) {
    assert callback != null;

    StatementService.Locator.getService().create(new RBAsyncCallback2<BStatement>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = StatementMessages.M.actionFailed(StatementMessages.M.create(), getEP()
            .getModuleCaption());
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(BStatement result) {
        RLoadingDialog.hide();
        entity = result;
        callback.execute();
      }
    });
  }

  private class Handler_Click implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == saveAction) {
        doSave();
      } else if (event.getSource() == cancleAction) {
        doCancle();
      }
    }
  }

  private void doSave() {
    GWTUtil.blurActiveElement();
    if (validate() == false)
      return;

    StatementServiceAgent.save(entity, new StatementServiceAgent.Callback() {
      @Override
      public void execute(BStatement result) {
        BStatementLogger.getInstance().log(StatementMessages.M.create(), result);
        
        Message msg = Message.info(StatementMessages.M.onSuccess2(StatementMessages.M.save(),
            getEP().getModuleCaption()));
        getEP().jumpToViewPage(result.getUuid(), msg);
      }
    });
  }

  private void doCancle() {
    String msg = StatementMessages.M.actionComfirm(StatementMessages.M.cancel(),
        StatementMessages.M.create());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {

        if (confirmed) {
          if (HistoryHelper.getBackingItems() == null || HistoryHelper.getBackingItems().isEmpty()) {
            JumpParameters param = new JumpParameters(StatementUrlParams.Search.START_NODE);
            getEP().jump(param);
          } else {
            RHistory.back();
          }
        }
      }
    });
  }

  private class RemarkChange implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      entity.setRemark(remarkField.getValue());
    }
  }

  @Override
  protected EPStatement getEP() {
    return EPStatement.getInstance();
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  @Override
  protected void refreshEntity() {
    if (entity.getAccountUnit() == null && getEP().getUserStores().size() == 1) {
      entity.setAccountUnit(new BUCN(getEP().getUserStores().get(0)));
    }
    generalGadget.setValue(entity);
    lineEditGrid.setValue(entity.getLines());
    attachmentGadget.setValue(entity.getAttachs());
    remarkField.setValue(entity.getRemark());
    generalGadget.focusOnFirstField();
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
      StatementServiceAgent.executeTask(operation, entity, processCtx, true, this);
    }
  }
}
