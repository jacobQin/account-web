/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	PayDepositRepaymentViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-20 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.payment.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepaymentLogger;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.DepositRepaymentMessage;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.dd.PDepositRepaymentDef;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.EPPayDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.rpc.PayDepositRepaymentLoader;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.rpc.PayDepositRepaymentService;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.rpc.PayDepositRepaymentServiceAgent;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.ui.gadget.PayDepositRepaymentGeneralViewGadget;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.ui.gadget.PayDepositRepaymentLineViewGadget;
import com.hd123.m3.account.gwt.depositrepayment.payment.intf.client.PayDepositRepaymentUrlParams;
import com.hd123.m3.account.gwt.depositrepayment.payment.intf.client.PayDepositRepaymentUrlParams.View;
import com.hd123.m3.account.gwt.depositrepayment.payment.intf.client.perm.PayDepositRepaymentPermDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmViewPage;
import com.hd123.m3.commons.gwt.widget.client.biz.PrintingTemplate;
import com.hd123.m3.commons.gwt.widget.client.ui.navi.DefaultNavigateHandler;
import com.hd123.m3.commons.gwt.widget.client.ui.navi.EntityNavigator;
import com.hd123.m3.commons.gwt.widget.client.ui.print.PrintButton;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class PayDepositRepaymentViewPage extends BaseBpmViewPage implements View {
  private static PayDepositRepaymentViewPage instance = null;

  public static PayDepositRepaymentViewPage getInstance() {
    if (instance == null)
      instance = new PayDepositRepaymentViewPage();
    return instance;
  }

  public PayDepositRepaymentViewPage() {
    super();
    drawToolbar();
    drawSelf();
  }

  private BDepositRepayment entity;

  private RAction editAction;
  private RAction deleteAction;
  private RAction effectAction;
  private RAction abortAction;

  private EntityNavigator navigator;
  private PrintButton printButton;

  private PayDepositRepaymentGeneralViewGadget generalGadget;
  private PayDepositRepaymentLineViewGadget lineViewGadget;
  private RTextArea remarkField;

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());

    editAction = new RAction(RActionFacade.EDIT, new Handler_editAction());
    getToolbar().add(new RToolbarButton(editAction));

    deleteAction = new RAction(RActionFacade.DELETE, new Handler_deleteAction());
    getToolbar().add(new RToolbarButton(deleteAction));

    getToolbar().addSeparator();

    injectBpmActions();

    getToolbar().addSeparator();

    effectAction = new RAction(DepositRepaymentMessage.M.effect(), new Handler_effectAction());
    getToolbar().add(new RToolbarButton(effectAction));
    abortAction = new RAction(DepositRepaymentMessage.M.abort(), new Handler_abortAction());
    getToolbar().add(new RToolbarButton(abortAction));
    // 导航
    drawNaviButtons();
    // 打印
    drawPrintButton();
  }

  private void drawNaviButtons() {
    navigator = new EntityNavigator(getEP().getModuleService());
    navigator.setNavigateHandler(new DefaultNavigateHandler(getEP(), getStartNode(), getEP()
        .getUrlBizKey()));
    getToolbar().addToRight(navigator);
  }

  private void drawPrintButton() {
    getToolbar().addToRight(new RToolbarSeparator());
    printButton = new PrintButton(getEP().getPrintTemplate(), getEP().getCurrentUser().getId());
    getToolbar().addToRight(printButton);
    // 刷新模板
    printButton.refresh();
  }

  private void drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    initWidget(panel);

    generalGadget = new PayDepositRepaymentGeneralViewGadget();
    panel.add(generalGadget);

    lineViewGadget = new PayDepositRepaymentLineViewGadget();
    lineViewGadget.getCaptionBar().setShowCollapse(true);
    panel.add(lineViewGadget);

    panel.add(drawRemark());
  }

  private Widget drawRemark() {
    remarkField = new RTextArea(PDepositRepaymentDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.setReadOnly(true);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PDepositRepaymentDef.constants.remark());
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
    // do nothing

  }

  @Override
  public void onShow(final JumpParameters params) {
    super.onShow(params);

    if (checkIn() == false)
      return;

    PayDepositRepaymentLoader.decodeParams(params, new Command() {

      @Override
      public void execute() {
        entity = PayDepositRepaymentLoader.getEntity();
        refresh();
      }
    });
  }

  private boolean checkIn() {
    if (getEP().isProcessMode())
      return true;

    if (getEP().isPermitted(PayDepositRepaymentPermDef.READ) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void refresh() {
    refreshBpm(entity);
    refreshToolbar();
    refreshEntity();

    if (printButton != null && entity.getStore() != null
        && StringUtil.isNullOrBlank(entity.getStore().getUuid()) == false) {
      printButton.setStoreCodes(entity.getStore().getCode());
    }
  }

  private void refreshToolbar() {
    getEP().appendSearchBox();
    navigator.start(getEP().getObjectName(), entity.getUuid());
    refreshPrint();
    // 去除导航器消失时多余的分隔符
    getToolbar().minimizeSeparators();
  }

  private void refreshPrint() {
    if (printButton == null) {
      return;
    }
    if (entity != null) {
      printButton.clearParameters();
      printButton.setEnabled(getEP().isPermitted(BAction.PRINT.getKey()));
      Map<String, String> map = new HashMap<String, String>();
      map.put(PrintingTemplate.KEY_UUID, entity.getUuid());
      map.put(PrintingTemplate.KEY_BILLNUMBER, entity.getBillNumber());
      printButton.addPrintObject(map);
    }
  }

  private void refreshEntity() {
    assert entity != null;

    generalGadget.setISBPM(getEP().isProcessMode());
    generalGadget.refresh(entity);
    lineViewGadget.refresh(entity);
    lineViewGadget.refreshCommands();
    remarkField.setValue(entity.getRemark());
  }

  private class Handler_editAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      JumpParameters params = new JumpParameters(PayDepositRepaymentUrlParams.Edit.START_NODE);
      params.getUrlRef().set(PayDepositRepaymentUrlParams.Edit.PN_ENTITY_UUID, entity.getUuid());
      getEP().jump(params);
    }
  }

  private class Handler_deleteAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      getMessagePanel().clearMessages();
      String msg = DepositRepaymentMessage.M.actionComfirm2(DepositRepaymentMessage.M.delete(),
          PayDepositRepaymentUrlParams.MODULE_CAPTION, entity.toFriendlyStr());
      RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
        public void onClosed(boolean confirmed) {
          if (confirmed == false)
            return;
          doDelete();
        }
      });
    }
  }

  private void doDelete() {
    RLoadingDialog.show(DepositRepaymentMessage.M.actionDoing(DepositRepaymentMessage.M.delete()));
    PayDepositRepaymentService.Locator.getService().remove(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositRepaymentMessage.M.actionFailed(DepositRepaymentMessage.M.delete(),
                PayDepositRepaymentUrlParams.MODULE_CAPTION);
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BDepositRepaymentLogger.getInstance().log(DepositRepaymentMessage.M.delete(), entity);

            JumpParameters params = PayDepositRepaymentSearchPage.getInstance().getLastParams();
            params.getMessages().add(
                Message.info(DepositRepaymentMessage.M.onSuccess(
                    DepositRepaymentMessage.M.delete(),
                    PayDepositRepaymentUrlParams.MODULE_CAPTION, entity.toFriendlyStr())));
            getEP().jump(params);
          }
        });
  }

  private class Handler_effectAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      getMessagePanel().clearMessages();

      String msg = DepositRepaymentMessage.M.actionComfirm2(DepositRepaymentMessage.M.effect(),
          getEP().getModuleCaption(), entity.getBillNumber());
      RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
        public void onClosed(boolean confirmed) {
          if (confirmed == false)
            return;
          doEffect();
        }
      });
    }
  }

  private void doEffect() {
    RLoadingDialog.show(DepositRepaymentMessage.M.beDoing(DepositRepaymentMessage.M.effect(),
        entity.getBillNumber()));
    PayDepositRepaymentService.Locator.getService().effect(entity.getUuid(), null,
        entity.getVersion(), new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositRepaymentMessage.M.processError2(
                DepositRepaymentMessage.M.effect(), getEP().getModuleCaption(),
                entity.getBillNumber());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BDepositRepaymentLogger.getInstance().log(DepositRepaymentMessage.M.effect(), entity);

            String msg = DepositRepaymentMessage.M.onSuccess(DepositRepaymentMessage.M.effect(),
                getEP().getModuleCaption(), entity.getBillNumber());
            getEP().jumpToViewPage(entity.getUuid(), Message.info(msg));
          }
        });
  }

  private class Handler_abortAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      getMessagePanel().clearMessages();

      String msg = DepositRepaymentMessage.M.actionComfirm2(DepositRepaymentMessage.M.abort(),
          getEP().getModuleCaption(), entity.getBillNumber());
      RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
        public void onClosed(boolean confirmed) {
          if (confirmed == false)
            return;
          doAbort();
        }
      });
    }
  }

  private void doAbort() {
    RLoadingDialog.show(DepositRepaymentMessage.M.beDoing(DepositRepaymentMessage.M.abort(),
        entity.getBillNumber()));
    PayDepositRepaymentService.Locator.getService().abort(entity.getUuid(), null,
        entity.getVersion(), new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositRepaymentMessage.M.processError2(DepositRepaymentMessage.M.abort(),
                getEP().getModuleCaption(), entity.getBillNumber());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BDepositRepaymentLogger.getInstance().log(DepositRepaymentMessage.M.abort(), entity);

            String msg = DepositRepaymentMessage.M.onSuccess(DepositRepaymentMessage.M.abort(),
                getEP().getModuleCaption(), entity.getBillNumber());
            getEP().jumpToViewPage(entity.getUuid(), Message.info(msg));
          }
        });
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  private class ProcessDialog extends M3BaseOutgoingDialog {

    @Override
    protected void doExcute(BOperation operation, AsyncCallback<Object> callback) {
      final BProcessContext processCtx = getEP().getFormBuilder().getProcessCtx();
      PayDepositRepaymentServiceAgent.executeTask(processCtx, operation, entity, false, this);
    }
  }

  @Override
  protected EPPayDepositRepayment getEP() {
    return EPPayDepositRepayment.getInstance();
  }

  @Override
  protected void refreshToolButtons() {
    assert entity != null;

    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffected = BBizStates.EFFECT.equals(entity.getBizState());

    boolean canEdit = getEP().isPermitted(PayDepositRepaymentPermDef.UPDATE);
    boolean canDelete = getEP().isPermitted(PayDepositRepaymentPermDef.DELETE);
    boolean canEffect = getEP().isPermitted(PayDepositRepaymentPermDef.EFFECT);
    boolean canAbort = getEP().isPermitted(PayDepositRepaymentPermDef.ABORT);

    editAction.setVisible(isInEffect && canEdit);
    deleteAction.setVisible(isInEffect && canDelete);
    effectAction.setVisible(isInEffect && canEffect);
    abortAction.setVisible(isEffected && canAbort);

    getToolbar().minimizeSeparators();
  }

  @Override
  protected List<Widget> getRelateWidgets() {
    List<Widget> list = new ArrayList<Widget>();
    list.add(editAction);
    list.add(effectAction);
    list.add(deleteAction);
    list.add(abortAction);

    return list;
  }
}
