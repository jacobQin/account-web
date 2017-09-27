/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	ReceiptViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-6 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.page;

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
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.InvoiceRegPermDef;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.InvoiceRegUrlParams;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLogger;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentCollectionLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentOverdueLineDef;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptLoader;
import com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptService;
import com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptServiceAgent;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.AccountLineMultiViewBox;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.AccountLineViewBox;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.OverdueLineViewBox;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.ReceiptGeneralViewGadget;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.ReceiptInfoViewGadget;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.collection.CollectionLineLineViewBox;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.collection.CollectionLineSingleLineViewGrid;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.collection.CollectionLineViewGrid;
import com.hd123.m3.account.gwt.payment.rec.intf.client.ReceiptUrlParams.View;
import com.hd123.m3.account.gwt.payment.rec.intf.client.perm.ReceiptPermDef;
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
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author subinzhu
 * 
 */
public class ReceiptViewPage extends BaseBpmViewPage implements View {

  private static ReceiptViewPage instance;

  public static ReceiptViewPage getInstance() {
    if (instance == null)
      instance = new ReceiptViewPage();
    return instance;
  }

  private BPayment entity;
  private ReceiptLoader billLoader;
  private EPReceipt ep = EPReceipt.getInstance();

  // 工具栏
  private RAction editAction;
  private RAction deleteAction;
  private RAction effectAction;
  private RAction abortAction;
  private RAction ivcRegisteAction;
  private EntityNavigator navigator;
  private PrintButton printButton;

  private ReceiptGeneralViewGadget generalGadget;
  private RTabPanel tabPanel;
  private RTabDef accountTab;
  private RTabDef accountMultiTab;
  private RTabDef overdueTab;
  private RTabDef collectionTab;
  private RTabDef collectionLineSingleTab;
  private RTabDef collectionLineTab;
  private AccountLineViewBox accountLineViewBox;
  private AccountLineMultiViewBox accountLineMultiViewBox;
  private OverdueLineViewBox overdueLineViewBox;

  private CollectionLineViewGrid collectionLineViewGrid;
  private CollectionLineSingleLineViewGrid collectionLineSingleLineViewGrid;
  private CollectionLineLineViewBox collectionLineLineViewBox;

  private ReceiptInfoViewGadget receiptInfoViewGadget;

  private RTextArea remarkField;

  private Handler_click clickHandler = new Handler_click();

  public ReceiptViewPage() {
    super();
    billLoader = new ReceiptLoader();
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

    // BPM出口按钮
    injectBpmActions();

    getToolbar().addSeparator();

    effectAction = new RAction(ReceiptMessages.M.effect(), clickHandler);
    getToolbar().add(new RToolbarButton(effectAction));
    abortAction = new RAction(ReceiptMessages.M.abort() + "...", clickHandler);
    getToolbar().add(new RToolbarButton(abortAction));
    ivcRegisteAction = new RAction(ReceiptMessages.M.ivcRegiste(), clickHandler);
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

    generalGadget = new ReceiptGeneralViewGadget();
    panel.add(generalGadget);

    panel.add(drawMiddle());

    receiptInfoViewGadget = new ReceiptInfoViewGadget(true);
    panel.add(receiptInfoViewGadget);
    panel.getWidgetIndex(receiptInfoViewGadget);

    panel.add(drawRemark());

    initWidget(panel);
  }

  private Widget drawMiddle() {
    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");

    tabPanel = new RTabPanel();
    tabPanel.setWidth("100%");

    accountLineViewBox = new AccountLineViewBox();
    accountLineMultiViewBox = new AccountLineMultiViewBox();
    overdueLineViewBox = new OverdueLineViewBox();

    collectionLineViewGrid = new CollectionLineViewGrid();
    collectionLineSingleLineViewGrid = new CollectionLineSingleLineViewGrid();
    collectionLineLineViewBox = new CollectionLineLineViewBox();

    RSimplePanel collectionLineViewGridWidget = RSimplePanel.decorate(collectionLineViewGrid);
    RSimplePanel collectionLineSingleLineViewGridWidget = RSimplePanel
        .decorate(collectionLineSingleLineViewGrid);
    RSimplePanel collectionLineLineViewBoxWidget = RSimplePanel.decorate(collectionLineLineViewBox);

    accountTab = new RTabDef(PPaymentAccountLineDef.TABLE_CAPTION,
        RSimplePanel.decorate(accountLineViewBox));
    accountMultiTab = new RTabDef(PPaymentAccountLineDef.TABLE_CAPTION,
        RSimplePanel.decorate(accountLineMultiViewBox));
    overdueTab = new RTabDef(PPaymentOverdueLineDef.TABLE_CAPTION,
        RSimplePanel.decorate(overdueLineViewBox));

    collectionTab = new RTabDef(PPaymentCollectionLineDef.TABLE_CAPTION,
        collectionLineViewGridWidget);
    collectionLineSingleTab = new RTabDef(PPaymentCollectionLineDef.TABLE_CAPTION,
        collectionLineSingleLineViewGridWidget);

    collectionLineTab = new RTabDef(PPaymentCollectionLineDef.TABLE_CAPTION,
        collectionLineLineViewBoxWidget);

    tabPanel.addTabDef(accountTab);
    tabPanel.addTabDef(accountMultiTab);
    tabPanel.addTabDef(overdueTab);
    tabPanel.addTabDef(collectionTab);
    tabPanel.addTabDef(collectionLineSingleTab);
    tabPanel.addTabDef(collectionLineTab);

    tabPanel.selectFirstVisibleTab();
    panel.add(tabPanel);
    return panel;
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
    getEP().clearConditions();
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
        entity.aggregate(ep.getScale(), ep.getRoundingMode());

        refresh();
        tabPanel.selectTab(0);
        refreshBpm(entity);
        refreshToolbar();
      }
    });
  }

  private void refreshToolbar() {
    getEP().appendSearchBox();
    refreshPrint();
    navigator.start(getEP().getObjectName(), entity.getUuid());
    // 去除导航器消失时多余的分隔符
    getToolbar().minimizeSeparators();
  }

  private boolean checkIn() {
    if (getEP().isProcessMode())
      return true;

    if (getEP().isPermitted(ReceiptPermDef.READ) == false) {
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
    accountLineViewBox.refreshGrid(entity.getDefrayalType());
    accountLineViewBox.refreshData();

    collectionLineViewGrid.setBill(entity);
    collectionLineSingleLineViewGrid.setBill(entity);
    collectionLineLineViewBox.setBill(entity);

    accountLineMultiViewBox.setLines(entity);
    accountLineMultiViewBox.refreshGrid();
    accountLineMultiViewBox.refreshData();

    overdueLineViewBox.refresh(entity);
    overdueLineViewBox.refreshGrid();
    overdueLineViewBox.refreshData();

    receiptInfoViewGadget.setBill(entity);
    receiptInfoViewGadget.refresh();

    remarkField.setValue(entity.getRemark());

    refreshTabVisible();

    refreshShowWhichCollectionGrid();
  }

  private void refreshShowWhichCollectionGrid() {
    if (CPaymentDefrayalType.bill.equals(entity.getDefrayalType())) {
      collectionTab.setVisible(true);
      collectionLineSingleTab.setVisible(false);
      collectionLineTab.setVisible(false);
    } else if (CPaymentDefrayalType.lineSingle.equals(entity.getDefrayalType())) {
      collectionTab.setVisible(false);
      collectionLineSingleTab.setVisible(true);
      collectionLineTab.setVisible(false);
    } else {
      collectionTab.setVisible(false);
      collectionLineSingleTab.setVisible(false);
      collectionLineTab.setVisible(true);
    }
  }

  private void refreshTabVisible() {
    boolean isFirstTabPanel = false;
    if (tabPanel.getCurrentTabDef() == accountTab || tabPanel.getCurrentTabDef() == accountMultiTab) {
      isFirstTabPanel = true;
    }
    accountTab.setVisible(!CPaymentDefrayalType.line.equals(entity.getDefrayalType()));
    accountMultiTab.setVisible(CPaymentDefrayalType.line.equals(entity.getDefrayalType()));
    if (isFirstTabPanel) {
      tabPanel.selectFirstVisibleTab();
    }
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

  public void doCreate() {
    JumpParameters params = new JumpParameters(ReceiptCreatePage.START_NODE);
    getEP().jump(params);
  }

  private void doDeleteConfirm() {
    getMessagePanel().clearMessages();

    String msg = ReceiptMessages.M.actionComfirm2(ReceiptMessages.M.delete(),
        PPaymentDef.TABLE_CAPTION, entity.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doDelete();
      }
    });
  }

  private void doDelete() {
    RLoadingDialog.show(ReceiptMessages.M.beDoing(ReceiptMessages.M.delete(),
        entity.getBillNumber()));
    ReceiptService.Locator.getService().delete(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = ReceiptMessages.M.actionFailed2(ReceiptMessages.M.delete(),
                PPaymentDef.TABLE_CAPTION, entity.getBillNumber());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BPaymentLogger.getInstance().log(ReceiptMessages.M.delete(), entity);

            JumpParameters params = ReceiptSearchPage.getInstance().getLastParams();
            params.getMessages().add(
                Message.info(ReceiptMessages.M.onSuccess(ReceiptMessages.M.delete(),
                    PPaymentDef.TABLE_CAPTION, entity.getBillNumber())));
            getEP().jump(params);
          }
        });
  }

  private void doEdit() {
    JumpParameters params = new JumpParameters(ReceiptEditPage.START_NODE);
    params.getUrlRef().set(ReceiptEditPage.PN_UUID, entity.getUuid());
    getEP().jump(params);
  }

  private void doEffectConfirm() {
    getMessagePanel().clearMessages();

    String msg = ReceiptMessages.M.actionComfirm2(ReceiptMessages.M.effect(),
        PPaymentDef.TABLE_CAPTION, entity.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doEffect();
      }
    });
  }

  protected void doEffect() {
    RLoadingDialog.show(ReceiptMessages.M.beDoing(ReceiptMessages.M.effect(),
        entity.getBillNumber()));
    ReceiptService.Locator.getService().effect(entity.getUuid(), null, entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = ReceiptMessages.M.actionFailed2(ReceiptMessages.M.effect(),
                PPaymentDef.TABLE_CAPTION, entity.getBillNumber());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BPaymentLogger.getInstance().log(ReceiptMessages.M.effect(), entity);

            String msg = ReceiptMessages.M.onSuccess(ReceiptMessages.M.effect(),
                PPaymentDef.TABLE_CAPTION, entity.getBillNumber());

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
    fieldDef.setCaption(ReceiptMessages.M.abortReason());
    InputBox.show(ReceiptMessages.M.abortReason(), null, true, fieldDef, new InputBox.Callback() {
      public void onClosed(boolean ok, String text) {
        if (ok == false)
          return;
        doAbort(text);
      }
    });
  }

  private void doAbort(final String reason) {
    RLoadingDialog
        .show(ReceiptMessages.M.beDoing(ReceiptMessages.M.abort(), entity.getBillNumber()));
    ReceiptService.Locator.getService().abort(entity.getUuid(), reason, entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = ReceiptMessages.M.actionFailed2(ReceiptMessages.M.abort(),
                PPaymentDef.TABLE_CAPTION, entity.getBillNumber());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BPaymentLogger.getInstance().log(ReceiptMessages.M.abort(), entity);

            String msg = ReceiptMessages.M.onSuccess(ReceiptMessages.M.abort(),
                PPaymentDef.TABLE_CAPTION, entity.getBillNumber());

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
      JumpParameters params = new JumpParameters(ReceiptIvcRegViewPage.START_NODE);
      params.getUrlRef().set(ReceiptIvcRegViewPage.PN_UUID, entity.getUuid());
      getEP().jump(params);
    } else {
      GwtUrl url = InvoiceRegUrlParams.ENTRY_URL;
      url.getQuery().set(JumpParameters.PN_START, InvoiceRegUrlParams.START_NODE_CREATE);
      url.getQuery().set(InvoiceRegUrlParams.KEY_CREATE_BY_PAYMENT, entity.getUuid());
      try {
        RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_NEW);
      } catch (Exception e) {
        String msg = ReceiptMessages.M.cannotNavigate(ReceiptMessages.M.registe());
        RMsgBox.showError(msg, e);
      }
    }
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  @Override
  protected EPReceipt getEP() {
    return EPReceipt.getInstance();
  }

  @Override
  protected void refreshToolButtons() {
    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffect = BBizStates.EFFECT.equals(entity.getBizState());
    boolean isFinished = BBizStates.FINISHED.equals(entity.getBizState());

    boolean canEdit = getEP().isPermitted(ReceiptPermDef.UPDATE);
    boolean canDelete = getEP().isPermitted(ReceiptPermDef.DELETE);
    boolean canEffect = getEP().isPermitted(ReceiptPermDef.EFFECT);
    boolean canAbort = getEP().isPermitted(ReceiptPermDef.ABORT);
    boolean canRegiste = getEP().isPermitted(InvoiceRegPermDef.CREATE);

    editAction.setVisible(isInEffect && canEdit);
    deleteAction.setVisible(isInEffect && canDelete);
    effectAction.setVisible(isInEffect && canEffect);
    abortAction.setVisible((isFinished || isEffect) && canAbort);
    ivcRegisteAction.setVisible((isEffect) && canRegiste && entity.isCanInvoice());

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
      ReceiptServiceAgent.executeTask(operation, entity, processCtx, false, this);
    }
  }
}
