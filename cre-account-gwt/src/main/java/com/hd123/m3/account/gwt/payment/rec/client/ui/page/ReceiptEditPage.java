/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	ReceiptEditPage.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-5 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.page;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.payment.commons.client.BPaymentDefrayalValidator2;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCashDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDepositDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineCash;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDeposit;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLogger;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueLine;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentLineDefrayalLineLocator;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentLineLocator;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentRemarkEditGadget;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentCollectionLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentOverdueLineDef;
import com.hd123.m3.account.gwt.payment.rec.client.BPaymentLineDefrayalValidator2;
import com.hd123.m3.account.gwt.payment.rec.client.BReceiptValidator;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.account.gwt.payment.rec.client.biz.ActionName;
import com.hd123.m3.account.gwt.payment.rec.client.biz.BReceiptDefrayalApportionHelper;
import com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptLoader;
import com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptService;
import com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptServiceAgent;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.AccountLineEditGrid;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.AccountLineEditGrid2;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.OverdueLineEditGrid;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.ReceiptGeneralEditGadget;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.ReceiptInfoEditGadget;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.ReceiptInfoViewGadget;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.collection.CollectionLineEditGrid;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.collection.CollectionLineLineEditBox;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.collection.CollectionLineSingleLineEditGrid;
import com.hd123.m3.account.gwt.payment.rec.intf.client.ReceiptUrlParams.Edit;
import com.hd123.m3.account.gwt.payment.rec.intf.client.perm.ReceiptPermDef;
import com.hd123.m3.account.gwt.util.client.ObjectUtil;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmEditPage;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.commandqueue.CommandQueue;
import com.hd123.rumba.gwt.util.client.commandqueue.RPCCommand;
import com.hd123.rumba.gwt.util.client.history.RHistory;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.panel.RSimplePanel;
import com.hd123.rumba.gwt.widget2.client.panel.RTabDef;
import com.hd123.rumba.gwt.widget2.client.panel.RTabPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author subinzhu
 * 
 */
public class ReceiptEditPage extends BaseBpmEditPage implements Edit, RValidatable, RActionHandler,
    HasRActionHandlers, SelectionHandler<Message> {

  private static ReceiptEditPage instance;

  private BPayment bill;
  private EPReceipt ep = EPReceipt.getInstance();

  private RAction saveAction;
  private RAction cancelAction;

  private ReceiptGeneralEditGadget generalGadget;

  private RTabPanel tabPanel;
  private RTabDef accountTab;
  private RTabDef accountMultiTab;
  private RTabDef overdueTab;
  private RTabDef collectionLineTab;
  private AccountLineEditGrid accountLineEditGrid;
  private AccountLineEditGrid2 accountLineMultiEditGrid;
  private OverdueLineEditGrid overdueLineEditGrid;

  private CollectionLineEditGrid collectionLineEditGrid;
  private CollectionLineSingleLineEditGrid collectionLineSingleLineEditGrid;
  private CollectionLineLineEditBox collectionLineLineEditGrid;

  private RSimplePanel collectionLineEditGridWidget;
  private RSimplePanel collectionLineSingleLineEditGridWidget;
  private RSimplePanel collectionLineLineEditWidget;

  private ReceiptInfoEditGadget paymentInfoEditGadget;
  private ReceiptInfoViewGadget paymentInfoViewGadget;

  private PaymentRemarkEditGadget remarkGadget;

  private ReceiptLoader entityLoader;
  protected BReceiptValidator validator;
  protected BPaymentLineDefrayalValidator2 lineDefrayalValidator;
  protected BPaymentDefrayalValidator2 defrayalValidator;

  private Handler_click clickHandler = new Handler_click();

  public static ReceiptEditPage getInstance() {
    if (instance == null)
      instance = new ReceiptEditPage();
    return instance;
  }

  public ReceiptEditPage() {
    super();
    entityLoader = new ReceiptLoader();
    drawToolbar();
    drawSelf();
    getMessagePanel().addSelectionHandler(this);
  }

  @Override
  public void refreshToolbar(RToolbar toolbar) {
    cancelAction.setVisible(!getEP().isProcessMode()); // 执行任务时，不显示取消按钮。HDCRE-700
    getToolbar().rebuild();
  }

  @Override
  protected EPReceipt getEP() {
    return EPReceipt.getInstance();
  }

  @Override
  protected void refreshEntity() {
    refresh();
    tabPanel.selectTab(0);
  }

  private void drawToolbar() {
    saveAction = new RAction(RActionFacade.SAVE, clickHandler);
    RToolbarButton saveButton = new RToolbarButton(saveAction);
    getToolbar().add(saveButton);

    // BPM出口按钮
    injectBpmActions();

    cancelAction = new RAction(RActionFacade.CANCEL, clickHandler);
    RToolbarButton cancelButton = new RToolbarButton(cancelAction);
    getToolbar().add(cancelButton);
  }

  private void drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    initWidget(panel);

    generalGadget = new ReceiptGeneralEditGadget();
    generalGadget.addActionHandler(this);
    panel.add(generalGadget);

    panel.add(drawMiddle());

    paymentInfoEditGadget = new ReceiptInfoEditGadget();
    paymentInfoEditGadget.addActionHandler(generalGadget);
    paymentInfoEditGadget.addActionHandler(accountLineEditGrid);
    accountLineEditGrid.addActionHandler(paymentInfoEditGadget);

    panel.add(paymentInfoEditGadget);

    paymentInfoViewGadget = new ReceiptInfoViewGadget(false);
    panel.add(paymentInfoViewGadget);

    remarkGadget = new PaymentRemarkEditGadget();
    remarkGadget.setEditing(true);
    remarkGadget.getCaptionBar().setShowCollapse(true);
    panel.add(remarkGadget);
    addActionHandler();
  }

  private Widget drawMiddle() {
    tabPanel = new RTabPanel();
    tabPanel.setWidth("100%");

    accountLineEditGrid = new AccountLineEditGrid();
    accountLineEditGrid.setEditing(true);
    accountLineEditGrid.addActionHandler(this);

    collectionLineEditGrid = new CollectionLineEditGrid();
    collectionLineEditGrid.addActionHandler(this);

    collectionLineSingleLineEditGrid = new CollectionLineSingleLineEditGrid();
    collectionLineSingleLineEditGrid.addActionHandler(this);

    collectionLineLineEditGrid = new CollectionLineLineEditBox();
    collectionLineLineEditGrid.addActionHandler(this);

    overdueLineEditGrid = new OverdueLineEditGrid();
    overdueLineEditGrid.setEditing(true);
    overdueLineEditGrid.addActionHandler(this);

    accountLineMultiEditGrid = new AccountLineEditGrid2();
    accountLineMultiEditGrid.setEditing(true);
    accountLineMultiEditGrid.addActionHandler(this);

    accountTab = new RTabDef(PPaymentAccountLineDef.TABLE_CAPTION,
        RSimplePanel.decorate(accountLineEditGrid));
    accountMultiTab = new RTabDef(PPaymentAccountLineDef.TABLE_CAPTION,
        RSimplePanel.decorate(accountLineMultiEditGrid));
    overdueTab = new RTabDef(PPaymentOverdueLineDef.TABLE_CAPTION,
        RSimplePanel.decorate(overdueLineEditGrid));

    collectionLineEditGridWidget = RSimplePanel.decorate(collectionLineEditGrid);
    collectionLineSingleLineEditGridWidget = RSimplePanel
        .decorate(collectionLineSingleLineEditGrid);
    collectionLineLineEditWidget = RSimplePanel.decorate(collectionLineLineEditGrid);

    collectionLineTab = new RTabDef(PPaymentCollectionLineDef.TABLE_CAPTION,
        collectionLineEditGridWidget);

    tabPanel.addTabDef(accountTab);
    tabPanel.addTabDef(accountMultiTab);
    tabPanel.addTabDef(overdueTab);
    tabPanel.addTabDef(collectionLineTab);

    tabPanel.selectFirstVisibleTab();
    return tabPanel;
  }

  private void addActionHandler() {
    paymentInfoEditGadget.addActionHandler(generalGadget);
    paymentInfoEditGadget.addActionHandler(accountLineEditGrid);

    accountLineEditGrid.addActionHandler(paymentInfoEditGadget);
    accountLineEditGrid.addActionHandler(generalGadget);
    accountLineEditGrid.addActionHandler(accountLineEditGrid);
    accountLineEditGrid.addActionHandler(this);
    accountLineEditGrid.addActionHandler(overdueLineEditGrid);

    collectionLineLineEditGrid.addActionHandler(generalGadget);

    this.addActionHandler(this);
    this.addActionHandler(accountLineEditGrid);

    generalGadget.addActionHandler(paymentInfoEditGadget);
    overdueLineEditGrid.addActionHandler(generalGadget);

    // Multi
    accountLineMultiEditGrid.addActionHandler(this);
    accountLineMultiEditGrid.addActionHandler(generalGadget);
    accountLineMultiEditGrid.addActionHandler(overdueLineEditGrid);
    this.addActionHandler(accountLineMultiEditGrid);
    addActionHandler(accountLineMultiEditGrid);

    this.addActionHandler(generalGadget);
    paymentInfoEditGadget.addActionHandler(accountLineMultiEditGrid);
    accountLineMultiEditGrid.addActionHandler(paymentInfoEditGadget);
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (!checkIn())
      return;

    entityLoader.decodeParams(params, true, new Command() {
      public void execute() {
        bill = entityLoader.getEntity();
        bill.aggregate(ep.getScale(), ep.getRoundingMode());

        validator = new BReceiptValidator(bill, true);
        if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
          defrayalValidator = new BPaymentDefrayalValidator2(bill, paymentInfoEditGadget);
        } else {
          lineDefrayalValidator = new BPaymentLineDefrayalValidator2(bill);
        }
        refresh(bill);
        getEP().appendSearchBox();
      }
    });
  }

  private boolean checkIn() {
    if (getEP().isProcessMode())
      return true;
    if (getEP().isPermitted(ReceiptPermDef.UPDATE) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void refresh() {
    assert bill != null;

    /**
     * paymentInfoViewGadget要在refreshMiddle()之前设置bill，否则refreshMiddle()
     * 后会触发paymentInfoViewGadget的刷新，会出错。
     */
    paymentInfoViewGadget.setBill(bill);
    paymentInfoEditGadget.setBill(bill);
    generalGadget.setBill(bill);
    generalGadget.refresh();
    generalGadget.focusOnFirstField();
    if (!CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      paymentInfoEditGadget.setVisible(false);

      paymentInfoViewGadget.setVisible(true);
      paymentInfoViewGadget.refresh();
    } else {
      paymentInfoViewGadget.setVisible(false);

      paymentInfoEditGadget.setVisible(true);
      paymentInfoEditGadget.refresh();
    }

    refreshMiddle();

    remarkGadget.setEntity(bill);
    remarkGadget.refresh();

    refreshTabVisible();
    clearValidResults();
  }

  private void refreshMiddle() {
    accountLineEditGrid.setBill(bill);
    accountLineEditGrid.setIsEdit(true);
    accountLineEditGrid.refreshData();
    accountLineEditGrid.refreshGrid();

    overdueLineEditGrid.setBill(bill);
    overdueLineEditGrid.refreshData();
    overdueLineEditGrid.refreshGrid();

    // Multi
    accountLineMultiEditGrid.setIsEdit(true);
    accountLineMultiEditGrid.setBill(bill);
    accountLineMultiEditGrid.refreshGrid();
    accountLineMultiEditGrid.refreshData();

    // 代收明细
    resetContract();
    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      collectionLineTab.setWidget(collectionLineEditGridWidget);
      collectionLineEditGrid.setValue(bill);
    } else if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType())) {
      collectionLineTab.setWidget(collectionLineSingleLineEditGridWidget);
      collectionLineSingleLineEditGrid.setPayment(bill);
    } else {
      collectionLineTab.setWidget(collectionLineLineEditWidget);
      collectionLineLineEditGrid.setBill(bill);
    }
  }

  private void refreshTabVisible() {
    boolean isFirstTabPanel = false;
    if (tabPanel.getCurrentTabDef() == accountTab || tabPanel.getCurrentTabDef() == accountMultiTab) {
      isFirstTabPanel = true;
    }
    accountTab.setVisible(!CPaymentDefrayalType.line.equals(bill.getDefrayalType()));
    accountMultiTab.setVisible(CPaymentDefrayalType.line.equals(bill.getDefrayalType()));
    if (isFirstTabPanel) {
      tabPanel.selectFirstVisibleTab();
    }
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    generalGadget.clearQueryConditions();
    accountLineEditGrid.onHide();
    accountLineMultiEditGrid.onHide();
    overdueLineEditGrid.onHide();
    collectionLineEditGrid.onHide();
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  @Override
  public void onSelection(SelectionEvent<Message> event) {
    if (event.getSelectedItem().getSource() instanceof PaymentLineLocator) {
      PaymentLineLocator o = (PaymentLineLocator) event.getSelectedItem().getSource();
      if (o != null) {
        if (o.getTabId() != null) {
          tabPanel.selectTab(o.getTabId());
          RActionEvent.fire(ReceiptEditPage.this, ActionName.EDITPAGE_CHANGE_LINE, o);
        }
      }
    } else if (event.getSelectedItem().getSource() instanceof PaymentLineDefrayalLineLocator) {
      PaymentLineDefrayalLineLocator o = (PaymentLineDefrayalLineLocator) event.getSelectedItem()
          .getSource();
      if (o != null) {
        if (o.getTabId() != null) {
          tabPanel.selectTab(o.getTabId());
          RActionEvent.fire(ReceiptEditPage.this,
              ActionName.EDITPAGE_CHANGE_LINE_AND_LINEDEFRAYALLINE, o);
        }
      }
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == ActionName.ACTION_GENERALCREATE_COUONTERPARTCHANGED) {
      refresh();
    } else if (event.getActionName() == ActionName.ACTION_GENERALEDIT_RECEIVEDATECHANGED) {
      refreshMiddle();
    } else if (ObjectUtil.isEquals(event.getActionName(),
        ActionName.ACTION_REFRESH_AFFTER_APPORTION)) {
      // 分摊重算后刷新滞纳金
      afterShareCalculation();
    } else if (ObjectUtil.isEquals(event.getActionName(), ActionName.ACTION_BILLACCOUNTLINE_CHANGE)
        || ObjectUtil.isEquals(event.getActionName(), ActionName.ACTION_BILLACCOUNTLINE_CHANGE2)) {
      // 刷新统计区域
      reComputeTotal();
    } else if (!CPaymentDefrayalType.bill.equals(bill.getDefrayalType())
        && (event.getActionName() == ActionName.ACTION_LINECASHDEFRAYALLINE_CHANGE || event
            .getActionName() == ActionName.ACTION_LINEACCOUNTLINE_CASHDEFRAYALCHANGE)) {
      reComputeTotal();
    } else if (event.getActionName() == ActionName.ACTION_ACCOUNTLINE_AGGREGATE) {
      reComputeTotal();
    } else if (event.getActionName() == ActionName.ACTION_ACCOUNTLINE_REFRESHOVERDUELINES) {
      overdueLineEditGrid.refreshData();
    } else if (event.getActionName() == ActionName.ACTION_SHARE_CALCULATION) {
      new BReceiptDefrayalApportionHelper().apportion(bill, ep.getScale(), ep.getRoundingMode());
      afterShareCalculation();
    }else if (event.getActionName() == ActionName.ACTION_RECEIVABLE_CHANGE) {
      reComputeTotal();
    }else if (event.getActionName() == "refreshCash") { // 按总额收款时，设置默认收款金额=应收金额，刷新收款信息
      paymentInfoEditGadget.refresh();
      new BReceiptDefrayalApportionHelper().apportion(bill, ep.getScale(), ep.getRoundingMode());
      afterShareCalculation();
    }
  }

  /** 合计本次应收付金额以及账款应收付金额预存款金额,并刷新 */
  private void reComputeTotal() {
    bill.aggregate(ep.getScale(), ep.getRoundingMode());
    generalGadget.refreshRightPanel();
    paymentInfoViewGadget.refresh();
  }

  private void afterShareCalculation() {
    overdueLineEditGrid.refreshData();
    accountLineEditGrid.refreshData();
    collectionLineEditGrid.refresh();
    generalGadget.refreshRightPanel();
  }

  @Override
  public void clearValidResults() {
    generalGadget.clearValidResults();
    paymentInfoEditGadget.clearValidResults();
    remarkGadget.clearValidResults();
    accountLineEditGrid.clearValidResults();
    accountLineMultiEditGrid.clearValidResults();
    overdueLineEditGrid.clearValidResults();
    validator.clearValidResults();
    if (defrayalValidator != null) {
      defrayalValidator.clearValidResults();
    }
    if (lineDefrayalValidator != null) {
      lineDefrayalValidator.clearValidResults();
    }
  }

  @Override
  public boolean isValid() {
    boolean isValid = generalGadget.isValid()
        && paymentInfoEditGadget.isValid()
        && remarkGadget.isValid()
        && validator.isValid()
        && overdueLineEditGrid.isValid()
        && collectionLineEditGrid.isValid()
        && (CPaymentDefrayalType.bill.equals(bill.getDefrayalType()) ? defrayalValidator.isValid()
            & accountLineEditGrid.isValid() : (CPaymentDefrayalType.lineSingle.equals(bill
            .getDefrayalType()) ? lineDefrayalValidator.isValid() & accountLineEditGrid.isValid()
            : lineDefrayalValidator.isValid() & accountLineMultiEditGrid.isValid()));
    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      isValid = isValid && collectionLineEditGrid.isValid();
    } else if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType())) {
      isValid = isValid && collectionLineSingleLineEditGrid.isValid();
    } else {
      isValid = isValid && collectionLineLineEditGrid.isValid();
    }

    return isValid;

  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> list = new ArrayList<Message>();
    list.addAll(generalGadget.getInvalidMessages());
    list.addAll(paymentInfoEditGadget.getInvalidMessages());
    list.addAll(remarkGadget.getInvalidMessages());
    list.addAll(validator.getInvalidMessages());
  
    list.addAll(overdueLineEditGrid.getInvalidMessages());
    list.addAll(collectionLineEditGrid.getInvalidMessages());
    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      list.addAll(defrayalValidator.getInvalidMessages());
      list.addAll(collectionLineEditGrid.getInvalidMessages());
      list.addAll(accountLineEditGrid.getInvalidMessages());
    } else {
      list.addAll(lineDefrayalValidator.getInvalidMessages());
      if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType())) {
        list.addAll(collectionLineSingleLineEditGrid.getInvalidMessages());
        list.addAll(accountLineEditGrid.getInvalidMessages());
      } else {
        list.addAll(accountLineMultiEditGrid.getInvalidMessages());
        list.addAll(collectionLineLineEditGrid.getInvalidMessages());
      }
    }
    return list;
  }

  @Override
  public boolean validate() {
    // 这里验证器的验证放在最后，否则验证器中给控件绑定的错误消息会在控件验证的时候被清空。
    clearValidResults();
    getMessagePanel().clearMessages();
    boolean valid = generalGadget.validate();
    valid &= paymentInfoEditGadget.validate();
    valid &= remarkGadget.validate();
    valid &= overdueLineEditGrid.validate();
    if (CPaymentDefrayalType.line.equals(bill.getDefrayalType())) {
      valid &= accountLineMultiEditGrid.validate();
    }
    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      valid &= defrayalValidator.validate();
    } else {
      valid &= lineDefrayalValidator.validate();
    }

    if (CPaymentDefrayalType.line.equals(bill.getDefrayalType())) {
      valid &= collectionLineLineEditGrid.validate();
    } else if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType())) {
      valid &= collectionLineSingleLineEditGrid.validate();
      valid &= accountLineEditGrid.validate();
    } else {
      valid &= collectionLineEditGrid.validate();
      valid &= accountLineEditGrid.validate();
    }

    valid &= validator.validate();
    if (valid == false)
      getMessagePanel().putMessages(getInvalidMessages());
    return valid;
  }

  private class Handler_click implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource().equals(saveAction)) {
        doSaveCounfirm();
      } else if (event.getSource().equals(cancelAction)) {
        doCancel();
      }
    }
  }

  private void doSaveCounfirm() {
    GWTUtil.blurActiveElement();

    if (!validate()) {
      return;
    }

    if (bill.getDepositTotal().compareTo(BigDecimal.ZERO) > 0) {
      RMsgBox.showConfirm(ReceiptMessages.M.saveRecConfirm(bill.getDepositTotal().doubleValue()),
          new RMsgBox.ConfirmCallback() {
            @Override
            public void onClosed(boolean confirmed) {
              if (!confirmed)
                return;
              else
                doSave();
            }
          });
    } else {
      doSave();
    }
  }

  private void doSave() {
    prepareForSave();

    CommandQueue.offer(new RPCCommand() {
      public void onCall(CommandQueue queue, AsyncCallback callback) {
        RLoadingDialog.show(ReceiptMessages.M.actionDoing(ReceiptMessages.M.save()));
        ReceiptService.Locator.getService().save(bill, getEP().getProcessCtx(), callback);
      }

      public void onFailure(CommandQueue queue, Throwable t) {
        RLoadingDialog.hide();
        String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.save(),
            PPaymentDef.TABLE_CAPTION);
        RMsgBox.showError(msg, t);
        queue.abort();
      }

      public void onSuccess(CommandQueue queue, Object result) {
        RLoadingDialog.hide();
        BPaymentLogger.getInstance().log(ReceiptMessages.M.modify(), bill);

        Message msg = Message.info(ReceiptMessages.M.onSuccess(ReceiptMessages.M.save(),
            ReceiptMessages.M.receipt(), bill.getBillNumber()));
        getEP().jumpToViewPage(((BPayment) result).getUuid(), msg);
        queue.goon();
      }
    });
    CommandQueue.awake();
  }

  /**
   * 保存前的准备工作
   */
  private void prepareForSave() {
    // 移除无效的账款明细行
    Iterator<BPaymentAccountLine> iteratorAccount = bill.getAccountLines().iterator();
    while (iteratorAccount.hasNext()) {
      BPaymentAccountLine line = iteratorAccount.next();
      if (line.getAcc1() == null || line.getAcc1().getSubject() == null
          || StringUtil.isNullOrBlank(line.getAcc1().getSubject().getUuid()))
        iteratorAccount.remove();
    }
    // 移除无效的滞纳金明细行
    Iterator<BPaymentOverdueLine> iteratorOverdue = bill.getOverdueLines().iterator();
    while (iteratorOverdue.hasNext()) {
      BPaymentOverdueLine line = iteratorOverdue.next();
      if (line.getSubject() == null || StringUtil.isNullOrBlank(line.getSubject().getUuid()))
        iteratorOverdue.remove();
    }
    // 移除并合并收款信息
    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      refreshBillPaymentInfo();
    } else {
      refreshLinePaymentInfo();
    }
  }

  private void refreshLines() {
    // 需要保留账款明细
    for (BPaymentLine line : bill.getAccountLines()) {
      // 移除无效的付款方式行
      Iterator<BPaymentLineCash> iteratorCash = line.getCashs().iterator();
      while (iteratorCash.hasNext()) {
        BPaymentLineCash cash = iteratorCash.next();
        if (cash.getPaymentType() == null || cash.getTotal() == null
            || cash.getTotal().compareTo(BigDecimal.ZERO) == 0)
          iteratorCash.remove();
      }
      // 移除无效的预存款冲扣行
      Iterator<BPaymentLineDeposit> iteratorDeposit = line.getDeposits().iterator();
      while (iteratorDeposit.hasNext()) {
        BPaymentLineDeposit deposit = iteratorDeposit.next();
        if (deposit.getRemainTotal() == null
            || deposit.getRemainTotal().compareTo(BigDecimal.ZERO) <= 0
            || deposit.getTotal() == null || deposit.getTotal().compareTo(BigDecimal.ZERO) <= 0
            || deposit.getTotal().compareTo(deposit.getRemainTotal()) > 0)
          iteratorDeposit.remove();
      }
    }

    for (BPaymentLine line : bill.getOverdueLines()) {
      // 移除无效的付款方式行
      Iterator<BPaymentLineCash> iteratorCash = line.getCashs().iterator();
      while (iteratorCash.hasNext()) {
        BPaymentLineCash cash = iteratorCash.next();
        if (cash.getPaymentType() == null || cash.getTotal() == null
            || cash.getTotal().compareTo(BigDecimal.ZERO) == 0)
          iteratorCash.remove();
      }
      // 移除无效的预存款冲扣行
      Iterator<BPaymentLineDeposit> iteratorDeposit = line.getDeposits().iterator();
      while (iteratorDeposit.hasNext()) {
        BPaymentLineDeposit deposit = iteratorDeposit.next();
        if (deposit.getRemainTotal() == null
            || deposit.getRemainTotal().compareTo(BigDecimal.ZERO) <= 0
            || deposit.getTotal() == null || deposit.getTotal().compareTo(BigDecimal.ZERO) <= 0
            || deposit.getTotal().compareTo(deposit.getRemainTotal()) > 0)
          iteratorDeposit.remove();
      }
    }
  }

  /**
   * 刷新并合并按总额收款时的收款信息。
   */
  private void refreshBillPaymentInfo() {
    refreshLines();
    // 移除无效的实收行
    Iterator<BPaymentCashDefrayal> iteratorCash = bill.getCashs().iterator();
    while (iteratorCash.hasNext()) {
      BPaymentCashDefrayal cash = iteratorCash.next();
      if (cash.getPaymentType() == null || cash.getTotal() == null
          || cash.getTotal().compareTo(BigDecimal.ZERO) == 0)
        iteratorCash.remove();
    }
    // 移除无效的扣预存款行
    Iterator<BPaymentDepositDefrayal> iteratorDeposit = bill.getDeposits().iterator();
    while (iteratorDeposit.hasNext()) {
      BPaymentDepositDefrayal deposit = iteratorDeposit.next();
      if (deposit.getRemainTotal() == null
          || deposit.getRemainTotal().compareTo(BigDecimal.ZERO) <= 0 || deposit.getTotal() == null
          || deposit.getTotal().compareTo(BigDecimal.ZERO) <= 0
          || deposit.getTotal().compareTo(deposit.getRemainTotal()) > 0)
        iteratorDeposit.remove();
    }
  }

  /**
   * 刷新并合并按科目收款时明细行中的收款信息。
   */
  private void refreshLinePaymentInfo() {
    refreshLines();
    // 合并出单头的收款信息
    bill.aggreateCashsAndDepositsFromPaymentLine();
  }

  private void doCancel() {
    RMsgBox.showConfirm(
        ReceiptMessages.M.actionComfirm(ReceiptMessages.M.cancel(), ReceiptMessages.M.edit()),
        new RMsgBox.ConfirmCallback() {
          public void onClosed(boolean confirmed) {
            if (confirmed)
              RHistory.back();
          }
        });
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
      ReceiptServiceAgent.executeTask(operation, bill, processCtx, true, this);
    }
  }

  private void resetContract() {
    if (bill.getCounterpart() == null || bill.getCounterpart().getUuid() == null
        || bill.getAccountUnit() == null || bill.getAccountUnit().getUuid() == null) {
      resetCollectionContract(null);
      return;
    }
    GWTUtil.enableSynchronousRPC();
    RLoadingDialog.show(ReceiptMessages.M.loading());
    ReceiptService.Locator.getService().getContract(bill.getAccountUnit().getUuid(),
        bill.getCounterpart().getUuid(), bill.getCounterpart().getCounterpartType(),
        new RBAsyncCallback2<BContract>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.load(),
                PPaymentCollectionLineDef.constants.contract());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BContract result) {
            RLoadingDialog.hide();
            resetCollectionContract(result);
          }
        });
  }

  private void resetCollectionContract(BContract contract) {
    collectionLineEditGrid.setContract(contract);
    collectionLineLineEditGrid.setContract(contract);
    collectionLineSingleLineEditGrid.setContract(contract);
  }
}
