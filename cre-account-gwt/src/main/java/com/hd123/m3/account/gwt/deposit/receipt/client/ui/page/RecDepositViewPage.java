/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： PayViewPage.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.receipt.client.ui.page;

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
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositLogger;
import com.hd123.m3.account.gwt.deposit.intf.client.DepositMessage;
import com.hd123.m3.account.gwt.deposit.intf.client.dd.PDepositDef;
import com.hd123.m3.account.gwt.deposit.receipt.client.EPRecDeposit;
import com.hd123.m3.account.gwt.deposit.receipt.client.rpc.RecDepositLoader;
import com.hd123.m3.account.gwt.deposit.receipt.client.rpc.RecDepositService;
import com.hd123.m3.account.gwt.deposit.receipt.client.rpc.RecDepositServiceAgent;
import com.hd123.m3.account.gwt.deposit.receipt.client.ui.gadget.RecDepositGeneralViewGadget;
import com.hd123.m3.account.gwt.deposit.receipt.client.ui.gadget.RecDepositLineViewGadget;
import com.hd123.m3.account.gwt.deposit.receipt.intf.client.RecDepositUrlParams;
import com.hd123.m3.account.gwt.deposit.receipt.intf.client.RecDepositUrlParams.View;
import com.hd123.m3.account.gwt.deposit.receipt.intf.client.perm.RecDepositPermDef;
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
 * @author chenpeisi
 * 
 */
public class RecDepositViewPage extends BaseBpmViewPage implements View {
  private static RecDepositViewPage instance = null;

  public static RecDepositViewPage getInstance() {
    if (instance == null)
      instance = new RecDepositViewPage();
    return instance;
  }

  public RecDepositViewPage() {
    super();
    drawToolbar();
    drawSelf();
  }

  private BDeposit entity;

  private RAction editAction;
  private RAction deleteAction;
  private RAction effectAction;
  private RAction abortAction;

  private EntityNavigator navigator;
  private PrintButton printButton;

  private RecDepositGeneralViewGadget generalGadget;
  private RecDepositLineViewGadget lineViewGadget;
  private RTextArea remarkField;

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());

    editAction = new RAction(RActionFacade.EDIT, new Handler_editAction());
    getToolbar().add(new RToolbarButton(editAction));

    deleteAction = new RAction(RActionFacade.DELETE, new Handler_deleteAction());
    getToolbar().add(new RToolbarButton(deleteAction));

    getToolbar().addSeparator();
    // BPM出口按钮
    injectBpmActions();

    getToolbar().addSeparator();

    effectAction = new RAction(DepositMessage.M.effect(), new Handler_effectAction());
    getToolbar().add(new RToolbarButton(effectAction));

    abortAction = new RAction(DepositMessage.M.abort(), new Handler_abortAction());
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

    generalGadget = new RecDepositGeneralViewGadget();
    panel.add(generalGadget);

    lineViewGadget = new RecDepositLineViewGadget();
    lineViewGadget.getCaptionBar().setShowCollapse(true);
    panel.add(lineViewGadget);

    panel.add(drawRemark());
  }

  private Widget drawRemark() {
    remarkField = new RTextArea(PDepositDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.setReadOnly(true);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PDepositDef.constants.remark());
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

  public void onShow(final JumpParameters params) {
    super.onShow(params);

    if (checkIn() == false)
      return;

    RecDepositLoader.decodeParams(params, new Command() {

      @Override
      public void execute() {
        entity = RecDepositLoader.getEntity();
        refresh();
      }
    });
  }

  private boolean checkIn() {
    if (getEP().isProcessMode())
      return true;

    if (getEP().isPermitted(RecDepositPermDef.READ) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void refresh() {
    refreshBpm(entity);
    refreshToolbar();
    refreshEntity();
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
      if (entity.getStore() != null
          && StringUtil.isNullOrBlank(entity.getStore().getUuid()) == false) {
        printButton.setStoreCodes(entity.getStore().getCode());
      }
    }
  }

  private void refreshEntity() {
    assert entity != null;

    generalGadget.refresh(entity);
    lineViewGadget.refresh(entity);
    remarkField.setValue(entity.getRemark());
  }

  private class Handler_editAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      JumpParameters params = new JumpParameters(RecDepositUrlParams.Edit.START_NODE);
      params.getUrlRef().set(RecDepositUrlParams.Edit.PN_UUID, entity.getUuid());
      getEP().jump(params);
    }
  }

  private class Handler_deleteAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      getMessagePanel().clearMessages();
      String msg = DepositMessage.M.actionComfirm2(DepositMessage.M.delete(),
          RecDepositUrlParams.MODULE_CAPTION, entity.toFriendlyStr());
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
    RLoadingDialog.show(DepositMessage.M.actionDoing(DepositMessage.M.delete()));
    RecDepositService.Locator.getService().remove(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMessage.M.actionFailed(DepositMessage.M.delete(),
                RecDepositUrlParams.MODULE_CAPTION);
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BDepositLogger.getInstance().log(DepositMessage.M.delete(), entity);

            JumpParameters params = RecDepositSearchPage.getInstance().getLastParams();
            params.getMessages().add(
                Message.info(DepositMessage.M.onSuccess(DepositMessage.M.delete(),
                    RecDepositUrlParams.MODULE_CAPTION, entity.toFriendlyStr())));
            getEP().jump(params);
          }
        });
  }

  private class Handler_effectAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      getMessagePanel().clearMessages();
      String msg = DepositMessage.M.actionComfirm2(DepositMessage.M.effect(),
          RecDepositUrlParams.MODULE_CAPTION, entity.toFriendlyStr());
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
    RLoadingDialog.show(DepositMessage.M.actionDoing(DepositMessage.M.effect()));

    RecDepositService.Locator.getService().effect(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMessage.M.actionFailed(DepositMessage.M.effect(), getEP()
                .getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BDepositLogger.getInstance().log(DepositMessage.M.effect(), entity);

            String msg = DepositMessage.M.onSuccess(DepositMessage.M.effect(),
                RecDepositUrlParams.MODULE_CAPTION, entity.toFriendlyStr());
            getEP().jumpToViewPage(entity.getUuid(), Message.info(msg));
          }
        });
  }

  private class Handler_abortAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      getMessagePanel().clearMessages();
      String msg = DepositMessage.M.actionComfirm2(DepositMessage.M.abort(),
          RecDepositUrlParams.MODULE_CAPTION, entity.toFriendlyStr());
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
    RLoadingDialog.show(DepositMessage.M.actionDoing(DepositMessage.M.abort()));

    RecDepositService.Locator.getService().abort(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMessage.M.actionFailed(DepositMessage.M.abort(), getEP()
                .getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BDepositLogger.getInstance().log(DepositMessage.M.abort(), entity);

            String msg = DepositMessage.M.onSuccess(DepositMessage.M.abort(),
                RecDepositUrlParams.MODULE_CAPTION, entity.toFriendlyStr());
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
      RecDepositServiceAgent.executeTask(processCtx, operation, entity, false, this);
    }
  }

  @Override
  protected EPRecDeposit getEP() {
    return EPRecDeposit.getInstance();
  }

  @Override
  protected void refreshToolButtons() {
    assert entity != null;

    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffected = BBizStates.EFFECT.equals(entity.getBizState());

    boolean canEdit = getEP().isPermitted(RecDepositPermDef.UPDATE);
    boolean canDelete = getEP().isPermitted(RecDepositPermDef.DELETE);
    boolean canEffect = getEP().isPermitted(RecDepositPermDef.EFFECT);
    boolean canAbort = getEP().isPermitted(RecDepositPermDef.ABORT);

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
    list.add(deleteAction);
    list.add(effectAction);

    return list;
  }
}
