/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-6 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client.ui.page;

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
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLogger;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentOverdueLineDef;
import com.hd123.m3.account.gwt.payment.pay.client.EPPayment;
import com.hd123.m3.account.gwt.payment.pay.client.PaymentMessages;
import com.hd123.m3.account.gwt.payment.pay.client.rpc.PaymentLoader;
import com.hd123.m3.account.gwt.payment.pay.client.rpc.PaymentService;
import com.hd123.m3.account.gwt.payment.pay.client.rpc.PaymentServiceAgent;
import com.hd123.m3.account.gwt.payment.pay.client.ui.gadget.AccountLineViewBox;
import com.hd123.m3.account.gwt.payment.pay.client.ui.gadget.OverdueLineViewBox;
import com.hd123.m3.account.gwt.payment.pay.client.ui.gadget.PaymentGeneralViewGadget;
import com.hd123.m3.account.gwt.payment.pay.client.ui.gadget.PaymentInfoViewGadget;
import com.hd123.m3.account.gwt.payment.pay.intf.client.PaymentUrlParams.View;
import com.hd123.m3.account.gwt.payment.pay.intf.client.perm.PaymentPermDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmViewPage;
import com.hd123.m3.commons.gwt.util.client.dialog.InputBox;
import com.hd123.m3.commons.gwt.widget.client.biz.PrintingTemplate;
import com.hd123.m3.commons.gwt.widget.client.ui.navi.DefaultNavigateHandler;
import com.hd123.m3.commons.gwt.widget.client.ui.navi.EntityNavigator;
import com.hd123.m3.commons.gwt.widget.client.ui.print.PrintButton;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.fielddef.FieldDef;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RSimplePanel;
import com.hd123.rumba.gwt.widget2.client.panel.RTabDef;
import com.hd123.rumba.gwt.widget2.client.panel.RTabPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author subinzhu
 * 
 */
public class PaymentViewPage extends BaseBpmViewPage implements View {

  private static PaymentViewPage instance;

  public static PaymentViewPage getInstance() {
    if (instance == null)
      instance = new PaymentViewPage();
    return instance;
  }

  private BPayment entity;
  private PaymentLoader billLoader;

  // 工具栏
  private RAction editAction;
  private RAction deleteAction;
  private RAction effectAction;
  private RAction abortAction;
  private EntityNavigator navigator;
  private RAction ivcRegisteAction;
  private PrintButton printButton;

  private RTabPanel tabPanel;
  private PaymentGeneralViewGadget generalGadget;
  private AccountLineViewBox accountLineViewBox;
  private OverdueLineViewBox overdueLineViewBox;
  private PaymentInfoViewGadget paymentInfoViewGadget;

  private RTextArea remarkField;

  private Handler_click clickHandler = new Handler_click();

  public PaymentViewPage() {
    super();
    billLoader = new PaymentLoader();
    drawToolbar();
    drawSelf();
  }

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());

    editAction = new RAction(RActionFacade.EDIT, clickHandler);
    getToolbar().add(new RToolbarButton(editAction));

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    getToolbar().add(new RToolbarButton(deleteAction));

    getToolbar().addSeparator();

    injectBpmActions();

    getToolbar().addSeparator();

    effectAction = new RAction(PaymentMessages.M.effect(), clickHandler);
    getToolbar().add(new RToolbarButton(effectAction));
    abortAction = new RAction(PaymentMessages.M.abort() + "...", clickHandler);
    getToolbar().add(new RToolbarButton(abortAction));
    ivcRegisteAction = new RAction(PaymentMessages.M.ivcRegiste(), clickHandler);
    getToolbar().add(new RToolbarButton(ivcRegisteAction));

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

    generalGadget = new PaymentGeneralViewGadget();
    panel.add(generalGadget);

    panel.add(drawMiddle());

    paymentInfoViewGadget = new PaymentInfoViewGadget(true);
    panel.add(paymentInfoViewGadget);
    panel.getWidgetIndex(paymentInfoViewGadget);

    panel.add(drawRemark());

    initWidget(panel);
  }

  private Widget drawMiddle() {
    tabPanel = new RTabPanel();
    tabPanel.setWidth("100%");

    accountLineViewBox = new AccountLineViewBox();
    overdueLineViewBox = new OverdueLineViewBox();

    RTabDef accountTab = new RTabDef(PPaymentAccountLineDef.TABLE_CAPTION,
        RSimplePanel.decorate(accountLineViewBox));
    tabPanel.addTabDef(accountTab);

    RTabDef overdueTab = new RTabDef(PPaymentOverdueLineDef.TABLE_CAPTION,
        RSimplePanel.decorate(overdueLineViewBox));
    tabPanel.addTabDef(overdueTab);

    tabPanel.selectTab(0);
    return tabPanel;
  }

  private Widget drawRemark() {
    remarkField = new RTextArea(PPaymentDef.remark);
    remarkField.setReadOnly(true);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    RCaptionBox box = new RCaptionBox();
    box.setWidth("100%");
    box.setContent(remarkField);
    box.getCaptionBar().setShowCollapse(true);
    box.setCaption(PPaymentDef.constants.remark());

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
  public void onShow(final JumpParameters params) {
    super.onShow(params);

    if (!checkIn())
      return;

    billLoader.decodeParams(params, false, new Command() {
      @Override
      public void execute() {
        entity = billLoader.getEntity();

        refresh();
        tabPanel.selectTab(0);
        refreshBpm(entity);
        refreshToolbar();
      }
    });
  }

  private void refreshToolbar() {
    getEP().appendSearchBox();
    navigator.start(getEP().getObjectName(), entity.getUuid());
    refreshPrint();
    // 去除导航器消失时多余的分隔符
    getToolbar().minimizeSeparators();
  }

  private boolean checkIn() {
    if (getEP().isProcessMode())
      return true;

    if (getEP().isPermitted(PaymentPermDef.READ) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
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

  private void refresh() {
    assert entity != null;

    generalGadget.setBill(entity);
    generalGadget.refresh();

    accountLineViewBox.setLines(entity.getAccountLines());
    accountLineViewBox.refreshGrid();

    overdueLineViewBox.refresh(entity);
    overdueLineViewBox.refreshGrid();

    paymentInfoViewGadget.setBill(entity);
    paymentInfoViewGadget.refresh();

    remarkField.setValue(entity.getRemark());
  }

  private class Handler_click implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource().equals(editAction)) {
        doEdit();
      } else if (event.getSource().equals(deleteAction)) {
        doDeleteConfirm();
      } else if (event.getSource().equals(effectAction)) {
        doEffectConfirm();
      } else if (event.getSource().equals(abortAction)) {
        doAbortConfirm();
      } else if (event.getSource().equals(ivcRegisteAction)) {
        doIvcRegiste();
      }
    }
  }

  private void doDeleteConfirm() {
    getMessagePanel().clearMessages();

    String msg = PaymentMessages.M.actionComfirm2(PaymentMessages.M.delete(),
        PaymentMessages.M.payment(), entity.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doDelete();
      }
    });
  }

  private void doDelete() {
    RLoadingDialog.show(PaymentMessages.M.beDoing(PaymentMessages.M.delete(),
        entity.getBillNumber()));
    PaymentService.Locator.getService().delete(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = PaymentMessages.M.actionFailed2(PaymentMessages.M.delete(),
                PaymentMessages.M.payment(), entity.getBillNumber());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BPaymentLogger.getInstance().log(PaymentMessages.M.delete(), entity);

            JumpParameters params = PaymentSearchPage.getInstance().getLastParams();
            if (params == null) {
              params = new JumpParameters(PaymentSearchPage.START_NODE);
            }
            params.getMessages().add(
                Message.info(PaymentMessages.M.onSuccess(PaymentMessages.M.delete(),
                    PaymentMessages.M.payment(), entity.getBillNumber())));
            getEP().jump(params);
          }
        });
  }

  private void doEdit() {
    JumpParameters params = new JumpParameters(PaymentEditPage.START_NODE);
    params.getUrlRef().set(PaymentEditPage.PN_UUID, entity.getUuid());
    getEP().jump(params);
  }

  private void doEffectConfirm() {
    getMessagePanel().clearMessages();

    String msg = PaymentMessages.M.actionComfirm2(PaymentMessages.M.effect(),
        PaymentMessages.M.payment(), entity.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doEffect();
      }
    });
  }

  protected void doEffect() {
    RLoadingDialog.show(PaymentMessages.M.beDoing(PaymentMessages.M.effect(),
        entity.getBillNumber()));
    PaymentService.Locator.getService().effect(entity.getUuid(), null, entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = PaymentMessages.M.actionFailed2(PaymentMessages.M.effect(),
                PaymentMessages.M.payment(), entity.getBillNumber());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BPaymentLogger.getInstance().log(PaymentMessages.M.effect(), entity);

            String msg = PaymentMessages.M.onSuccess(PaymentMessages.M.effect(),
                PaymentMessages.M.payment(), entity.getBillNumber());

            JumpParameters params = new JumpParameters(START_NODE);
            params.getUrlRef().set(PN_UUID, entity.getUuid());
            params.getMessages().add(Message.info(msg));
            getEP().jump(params);
          }
        });
  }

  private void doAbortConfirm() {
    getMessagePanel().clearMessages();

    FieldDef fieldDef = PPaymentDef.latestComment;
    fieldDef.setCaption(PaymentMessages.M.abortReason());
    InputBox.show(PaymentMessages.M.abortReason(), null, true, fieldDef, new InputBox.Callback() {
      public void onClosed(boolean ok, String text) {
        if (ok == false)
          return;
        doAbort(text);
      }
    });
  }

  private void doAbort(final String reason) {
    RLoadingDialog
        .show(PaymentMessages.M.beDoing(PaymentMessages.M.abort(), entity.getBillNumber()));
    PaymentService.Locator.getService().abort(entity.getUuid(), reason, entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = PaymentMessages.M.actionFailed2(PaymentMessages.M.abort(),
                PaymentMessages.M.payment(), entity.getBillNumber());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BPaymentLogger.getInstance().log(PaymentMessages.M.abort(), entity);

            String msg = PaymentMessages.M.onSuccess(PaymentMessages.M.abort(),
                PaymentMessages.M.payment(), entity.getBillNumber());

            JumpParameters params = new JumpParameters(START_NODE);
            params.getUrlRef().set(PN_UUID, entity.getUuid());
            params.getMessages().add(Message.info(msg));
            getEP().jump(params);
          }
        });
  }

  private void doIvcRegiste() {
    if ((BBizStates.FINISHED.equals(entity.getBizState()))
        || (BBizStates.ABORTED.equals(entity.getBizState()))) {
      JumpParameters params = new JumpParameters(PaymentIvcRegViewPage.START_NODE);
      params.getUrlRef().set(PaymentIvcRegViewPage.PN_UUID, entity.getUuid());
      getEP().jump(params);
    } else {
      JumpParameters params = new JumpParameters(PaymentIvcRegPage.START_NODE);
      params.getUrlRef().set(PaymentIvcRegPage.PN_UUID, entity.getUuid());
      getEP().jump(params);
    }
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  @Override
  protected EPPayment getEP() {
    return EPPayment.getInstance();
  }

  @Override
  protected void refreshToolButtons() {
    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffect = BBizStates.EFFECT.equals(entity.getBizState());
    boolean isFinished = BBizStates.FINISHED.equals(entity.getBizState());
    boolean isAbort = BBizStates.ABORTED.equals(entity.getBizState());
    boolean canEdit = getEP().isPermitted(PaymentPermDef.UPDATE);
    boolean canDelete = getEP().isPermitted(PaymentPermDef.DELETE);
    boolean canEffect = getEP().isPermitted(PaymentPermDef.EFFECT);
    boolean canAbort = getEP().isPermitted(PaymentPermDef.ABORT);
    boolean canRegiste = getEP().isPermitted(PaymentPermDef.IVCREGISTE);

    editAction.setVisible(isInEffect && canEdit);
    deleteAction.setVisible(isInEffect && canDelete);
    effectAction.setVisible(isInEffect && canEffect);
    abortAction.setVisible((isFinished || isEffect) && canAbort);
    ivcRegisteAction.setVisible((isFinished || isEffect || isAbort) && canRegiste);

    getToolbar().rebuild();
  }

  @Override
  protected List<Widget> getRelateWidgets() {
    List<Widget> list = new ArrayList<Widget>();
    list.add(editAction);
    list.add(deleteAction);
    list.add(effectAction);
    list.add(abortAction);
    list.add(ivcRegisteAction);
    return list;
  }

  private class ProcessDialog extends M3BaseOutgoingDialog {

    @Override
    protected void doExcute(BOperation operation, AsyncCallback<Object> callback) {
      final BProcessContext processCtx = getEP().getFormBuilder().getProcessCtx();
      PaymentServiceAgent.executeTask(operation, entity, processCtx, false, this);
    }
  }
}
