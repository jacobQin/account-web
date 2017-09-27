/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PayInvoiceRegViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-27 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.payment.client.ui;

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
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegLogger;
import com.hd123.m3.account.gwt.invoice.intf.client.InvoiceRegMessage;
import com.hd123.m3.account.gwt.invoice.intf.client.dd.PInvoiceRegDef;
import com.hd123.m3.account.gwt.invoice.payment.client.EPPayInvoiceReg;
import com.hd123.m3.account.gwt.invoice.payment.client.rpc.PayInvoiceRegLoader;
import com.hd123.m3.account.gwt.invoice.payment.client.rpc.PayInvoiceRegService;
import com.hd123.m3.account.gwt.invoice.payment.client.rpc.PayInvoiceRegServiceAgent;
import com.hd123.m3.account.gwt.invoice.payment.client.ui.gadget.PayInvoiceRegGeneralViewGadget;
import com.hd123.m3.account.gwt.invoice.payment.client.ui.gadget.PayInvoiceRegInvoiceViewGadget;
import com.hd123.m3.account.gwt.invoice.payment.client.ui.gadget.PayInvoiceRegLineViewGadget;
import com.hd123.m3.account.gwt.invoice.payment.intf.client.PayInvoiceRegUrlParams;
import com.hd123.m3.account.gwt.invoice.payment.intf.client.PayInvoiceRegUrlParams.View;
import com.hd123.m3.account.gwt.invoice.payment.intf.client.perm.PayInvoiceRegPermDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
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
public class PayInvoiceRegViewPage extends BaseBpmViewPage implements View {

  private static PayInvoiceRegViewPage instance = null;

  public static PayInvoiceRegViewPage getInstance() {
    if (instance == null) {
      instance = new PayInvoiceRegViewPage();
    }
    return instance;
  }

  public PayInvoiceRegViewPage() {
    super();
    drawToolbar();
    drawSelf();
  }

  private BInvoiceReg entity;

  private RAction editAction;
  private RAction deleteAction;
  private RAction effectAction;
  private RAction abortAction;

  private EntityNavigator navigator;
  private PrintButton printButton;

  private PayInvoiceRegGeneralViewGadget generalViewGadget;
  private PayInvoiceRegLineViewGadget lineViewGadget;
  private PayInvoiceRegInvoiceViewGadget invoiceViewGadget;

  private RTextArea remarkField;

  private ActionHandler actionHandler = new ActionHandler();

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());

    editAction = new RAction(RActionFacade.EDIT, actionHandler);
    getToolbar().add(new RToolbarButton(editAction));

    deleteAction = new RAction(RActionFacade.DELETE, actionHandler);
    getToolbar().add(new RToolbarButton(deleteAction));

    getToolbar().addSeparator();
    // BPM出口按钮
    injectBpmActions();

    getToolbar().addSeparator();

    effectAction = new RAction(CommonsMessages.M.effect(), actionHandler);
    getToolbar().add(new RToolbarButton(effectAction));

    abortAction = new RAction(CommonsMessages.M.abort(), actionHandler);
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

    generalViewGadget = new PayInvoiceRegGeneralViewGadget();
    panel.add(generalViewGadget);

    lineViewGadget = new PayInvoiceRegLineViewGadget();
    lineViewGadget.getCaptionBar().setShowCollapse(true);
    panel.add(lineViewGadget);

    invoiceViewGadget = new PayInvoiceRegInvoiceViewGadget();
    invoiceViewGadget.getCaptionBar().setShowCollapse(true);
    panel.add(invoiceViewGadget);

    panel.add(drawRemark());
  }

  private Widget drawRemark() {
    remarkField = new RTextArea(PInvoiceRegDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.setReadOnly(true);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceRegDef.constants.remark());
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
  public void onShow(final JumpParameters params) {
    super.onShow(params);

    if (checkIn() == false)
      return;

    PayInvoiceRegLoader.decodeParams(params, new Command() {

      @Override
      public void execute() {
        entity = PayInvoiceRegLoader.getEntity();
        refresh();
      }
    });
  }

  private boolean checkIn() {
    if (getEP().isProcessMode())
      return true;

    if (getEP().isPermitted(PayInvoiceRegPermDef.READ) == false) {
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

  private void refreshEntity() {
    assert entity != null;

    generalViewGadget.refresh(entity);
    lineViewGadget.refresh(entity);
    invoiceViewGadget.refresh(entity);

    remarkField.setValue(entity.getRemark());
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

  private class ActionHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == editAction) {
        doEdit();
      } else if (event.getSource() == deleteAction) {
        doDelete();
      } else if (event.getSource() == effectAction) {
        doEffect();
      } else if (event.getSource() == abortAction) {
        doAbort();
      }
    }
  }

  private void doEdit() {
    JumpParameters params = new JumpParameters(PayInvoiceRegUrlParams.Edit.START_NODE);
    params.getUrlRef().set(PayInvoiceRegUrlParams.Edit.PN_UUID, entity.getUuid());
    getEP().jump(params);
  }

  private void doDelete() {
    getMessagePanel().clearMessages();
    String msg = InvoiceRegMessage.M.actionComfirm2(InvoiceRegMessage.M.delete(),
        PayInvoiceRegUrlParams.MODULE_CAPTION, entity.toFriendlyStr());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doDeleteOne();
      }
    });
  }

  private void doDeleteOne() {
    RLoadingDialog.show(InvoiceRegMessage.M.actionDoing(InvoiceRegMessage.M.delete()));
    PayInvoiceRegService.Locator.getService().remove(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InvoiceRegMessage.M.actionFailed(InvoiceRegMessage.M.delete(),
                PayInvoiceRegUrlParams.MODULE_CAPTION);
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            try {
              BInvoiceRegLogger.getInstance().log(InvoiceRegMessage.M.delete(), entity);
              JumpParameters params = PayInvoiceRegSearchPage.getInstance().getLastParams();
              params.getMessages().add(
                  Message.info(InvoiceRegMessage.M.onSuccess(InvoiceRegMessage.M.delete(),
                      PayInvoiceRegUrlParams.MODULE_CAPTION, entity.toFriendlyStr())));
              getEP().jump(params);
            } catch (Exception e) {
            }
          }
        });
  }

  private void doEffect() {
    getMessagePanel().clearMessages();
    String msg = InvoiceRegMessage.M.actionComfirm2(InvoiceRegMessage.M.effect(), getEP()
        .getModuleCaption(), entity.toFriendlyStr());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doEffectOne();
      }
    });
  }

  private void doEffectOne() {
    RLoadingDialog.show(InvoiceRegMessage.M.actionDoing(InvoiceRegMessage.M.effect()));

    PayInvoiceRegService.Locator.getService().effect(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InvoiceRegMessage.M.actionFailed(InvoiceRegMessage.M.effect(), getEP()
                .getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BInvoiceRegLogger.getInstance().log(InvoiceRegMessage.M.effect(), entity);

            String msg = InvoiceRegMessage.M.onSuccess(InvoiceRegMessage.M.effect(),
                PayInvoiceRegUrlParams.MODULE_CAPTION, entity.toFriendlyStr());
            getEP().jumpToViewPage(entity.getUuid(), Message.info(msg));
          }
        });
  }

  private void doAbort() {
    getMessagePanel().clearMessages();
    String msg = InvoiceRegMessage.M.actionComfirm2(InvoiceRegMessage.M.abort(), getEP()
        .getModuleCaption(), entity.toFriendlyStr());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doAbortOne();
      }
    });
  }

  private void doAbortOne() {
    RLoadingDialog.show(InvoiceRegMessage.M.actionDoing(InvoiceRegMessage.M.abort()));
    PayInvoiceRegService.Locator.getService().abort(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InvoiceRegMessage.M.actionFailed(InvoiceRegMessage.M.abort(), getEP()
                .getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BInvoiceRegLogger.getInstance().log(InvoiceRegMessage.M.abort(), entity);

            String msg = InvoiceRegMessage.M.onSuccess(InvoiceRegMessage.M.abort(),
                PayInvoiceRegUrlParams.MODULE_CAPTION, entity.toFriendlyStr());
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
      PayInvoiceRegServiceAgent.executeTask(processCtx, operation, entity, false, this);
    }
  }

  @Override
  protected EPPayInvoiceReg getEP() {
    return EPPayInvoiceReg.getInstance();
  }

  @Override
  protected void refreshToolButtons() {
    assert entity != null;
    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffect = BBizStates.EFFECT.equals(entity.getBizState());

    boolean canEdit = getEP().isPermitted(PayInvoiceRegPermDef.UPDATE);
    boolean canDelete = getEP().isPermitted(PayInvoiceRegPermDef.DELETE);
    boolean canEffect = getEP().isPermitted(PayInvoiceRegPermDef.EFFECT);
    boolean canAbort = getEP().isPermitted(PayInvoiceRegPermDef.ABORT);

    editAction.setVisible(isInEffect && canEdit);
    deleteAction.setVisible(isInEffect && canDelete);
    effectAction.setVisible(isInEffect && canEffect);
    abortAction.setVisible(isEffect && canAbort);

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
