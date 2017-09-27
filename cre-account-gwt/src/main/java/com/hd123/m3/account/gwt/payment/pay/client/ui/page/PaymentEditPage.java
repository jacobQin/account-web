/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentEditPage.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-5 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client.ui.page;

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
import com.hd123.m3.account.gwt.payment.commons.client.BPaymentDefrayalValidator;
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
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentOverdueLineDef;
import com.hd123.m3.account.gwt.payment.pay.client.BPaymentValidator;
import com.hd123.m3.account.gwt.payment.pay.client.EPPayment;
import com.hd123.m3.account.gwt.payment.pay.client.PaymentMessages;
import com.hd123.m3.account.gwt.payment.pay.client.rpc.PaymentLoader;
import com.hd123.m3.account.gwt.payment.pay.client.rpc.PaymentService;
import com.hd123.m3.account.gwt.payment.pay.client.rpc.PaymentServiceAgent;
import com.hd123.m3.account.gwt.payment.pay.client.ui.gadget.AccountLineEditGrid;
import com.hd123.m3.account.gwt.payment.pay.client.ui.gadget.OverdueLineEditGrid;
import com.hd123.m3.account.gwt.payment.pay.client.ui.gadget.PaymentGeneralEditGadget;
import com.hd123.m3.account.gwt.payment.pay.client.ui.gadget.PaymentInfoEditGadget;
import com.hd123.m3.account.gwt.payment.pay.intf.client.PaymentUrlParams.Edit;
import com.hd123.m3.account.gwt.payment.pay.intf.client.perm.PaymentPermDef;
import com.hd123.m3.account.gwt.payment.rec.client.biz.ActionName;
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
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author subinzhu
 * 
 */
public class PaymentEditPage extends BaseBpmEditPage implements Edit, RValidatable, RActionHandler,
    HasRActionHandlers, SelectionHandler<Message> {

  private static PaymentEditPage instance;

  public static PaymentEditPage getInstance() {
    if (instance == null)
      instance = new PaymentEditPage();
    return instance;
  }

  private EPPayment ep = EPPayment.getInstance();
  private BPayment bill;

  private RAction saveAction;
  private RAction cancelAction;

  private RTabPanel tabPanel;
  private PaymentGeneralEditGadget generalGadget;
  private AccountLineEditGrid accountLineEditGrid;
  private OverdueLineEditGrid overdueLineEditGrid;
  private PaymentInfoEditGadget paymentInfoEditGadget;
  private PaymentRemarkEditGadget remarkGadget;

  private PaymentLoader entityLoader;
  protected BPaymentValidator validator;
  protected BPaymentDefrayalValidator defrayalValidator;

  private Handler_click clickHandler = new Handler_click();

  public PaymentEditPage() {
    super();
    entityLoader = new PaymentLoader();
    drawToolbar();
    drawSelf();
    getMessagePanel().addSelectionHandler(this);
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

    generalGadget = new PaymentGeneralEditGadget();
    generalGadget.addActionHandler(this);
    panel.add(generalGadget);

    panel.add(drawMiddle());

    paymentInfoEditGadget = new PaymentInfoEditGadget();
    paymentInfoEditGadget.addActionHandler(generalGadget);
    panel.add(paymentInfoEditGadget);

    remarkGadget = new PaymentRemarkEditGadget();
    remarkGadget.setEditing(true);
    remarkGadget.getCaptionBar().setShowCollapse(true);
    panel.add(remarkGadget);
  }

  private Widget drawMiddle() {
    tabPanel = new RTabPanel();
    tabPanel.setWidth("100%");

    RTabDef accountTab = new RTabDef(PPaymentAccountLineDef.TABLE_CAPTION,
        RSimplePanel.decorate(drawAccountBox()));
    tabPanel.addTabDef(accountTab);

    RTabDef overdueTab = new RTabDef(PPaymentOverdueLineDef.TABLE_CAPTION,
        RSimplePanel.decorate(drawOverdueBox()));
    tabPanel.addTabDef(overdueTab);

    tabPanel.selectTab(0);
    return tabPanel;
  }

  private Widget drawAccountBox() {
    accountLineEditGrid = new AccountLineEditGrid();
    accountLineEditGrid.addActionHandler(this);
    accountLineEditGrid.setEdit(true);
    return accountLineEditGrid;
  }

  private Widget drawOverdueBox() {
    overdueLineEditGrid = new OverdueLineEditGrid();
    overdueLineEditGrid.addActionHandler(this);
    return overdueLineEditGrid;
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (!checkIn())
      return;

    entityLoader.decodeParams(params, true, new Command() {
      public void execute() {
        bill = entityLoader.getEntity();

        validator = new BPaymentValidator(bill);
        defrayalValidator = new BPaymentDefrayalValidator(bill, paymentInfoEditGadget);

        refresh(bill);
        getEP().appendSearchBox();
      }
    });
  }

  private boolean checkIn() {
    if (getEP().isProcessMode())
      return true;
    if (getEP().isPermitted(PaymentPermDef.UPDATE) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void refresh() {
    assert bill != null;

    paymentInfoEditGadget.setBill(bill);
    paymentInfoEditGadget.refresh();


    remarkGadget.setEntity(bill);
    remarkGadget.refresh();

    generalGadget.setBill(bill);
    generalGadget.refresh();
    generalGadget.focusOnFirstField();

    refreshMiddle();
    
    accountLineEditGrid.refreshButtons();

    clearValidResults();
  }

  private void refreshMiddle() {
    accountLineEditGrid.setValue(bill);
    overdueLineEditGrid.setValue(bill);
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    generalGadget.clearQueryConditions();
    accountLineEditGrid.clearQueryConditions();
  }

  @Override
  public void onSelection(SelectionEvent<Message> event) {
    if (event.getSelectedItem().getSource() instanceof PaymentLineLocator) {
      PaymentLineLocator o = (PaymentLineLocator) event.getSelectedItem().getSource();
      if (o != null) {
        if (o.getTabId() != null) {
          RActionEvent.fire(PaymentEditPage.this, ActionName.EDITPAGE_CHANGE_LINE, o);
        }
      }
    } else if (event.getSelectedItem().getSource() instanceof PaymentLineDefrayalLineLocator) {
      PaymentLineDefrayalLineLocator o = (PaymentLineDefrayalLineLocator) event.getSelectedItem()
          .getSource();
      if (o != null) {
        if (o.getTabId() != null) {
          RActionEvent.fire(PaymentEditPage.this,
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
    if (event.getActionName() == AccountLineEditGrid.ActionName.ACTION_ACCOUNTLINE_AGGREGATE) {
      bill.aggregate(ep.getScale(), ep.getRoundingMode());
      generalGadget.refreshRightPanel();
      overdueLineEditGrid.refresh();
    } else if (event.getActionName() == AccountLineEditGrid.ActionName.ACTION_ACCOUNTLINE_REFRESHOVERDUE) {
      overdueLineEditGrid.refresh();
    } else if (event.getActionName() == OverdueLineEditGrid.ActionName.ACTION_OVERDUELINE_AGGREGATE) {
      bill.aggregate(ep.getScale(), ep.getRoundingMode());
      generalGadget.refreshRightPanel();
      accountLineEditGrid.refresh();
    } else if (event.getActionName() == OverdueLineEditGrid.ActionName.ACTION_OVERDUELINE_REFRESHACCOUNT) {
      accountLineEditGrid.refresh();
    } else if (event.getActionName() == ActionName.ACTION_GENERALEDIT_RECEIVEDATECHANGED) {
      refreshMiddle();
    } else if (event.getActionName() == AccountLineEditGrid.ActionName.ACTION_ACCOUNTLINE_TOTALCHANGE) {
      recalcCashes();
      generalGadget.calculateRightPanel();
    }
  }

  // 重算实付，将差额金额全填入第一个付款方式
  private void recalcCashes() {
    BigDecimal cashTotal = BigDecimal.ZERO;
    for (BPaymentCashDefrayal cash : bill.getCashs()) {
      cashTotal = cashTotal.add(cash.getTotal());
    }

    BigDecimal lineTotal = BigDecimal.ZERO;
    for (BPaymentAccountLine line : bill.getAccountLines()) {
      lineTotal = lineTotal.add(line.getTotal().getTotal());
    }
    
    bill.getTotal().setTotal(lineTotal);
    generalGadget.refreshRightPanel();

    bill.getCashs().get(0)
        .setTotal(bill.getCashs().get(0).getTotal().add(lineTotal.subtract(cashTotal)));

    paymentInfoEditGadget.refresh();
  }

  @Override
  public void clearValidResults() {
    generalGadget.clearValidResults();
    accountLineEditGrid.clearValidResults();
    overdueLineEditGrid.clearValidResults();
    paymentInfoEditGadget.clearValidResults();
    remarkGadget.clearValidResults();
    validator.clearValidResults();
    if (defrayalValidator != null)
      defrayalValidator.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return generalGadget.isValid()
        && paymentInfoEditGadget.isValid()
        && remarkGadget.isValid()
        && validator.isValid()
        && (defrayalValidator.isValid() & accountLineEditGrid.isValid() & overdueLineEditGrid
            .isValid());
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> list = new ArrayList<Message>();
    list.addAll(generalGadget.getInvalidMessages());
    list.addAll(paymentInfoEditGadget.getInvalidMessages());
    list.addAll(remarkGadget.getInvalidMessages());
    list.addAll(validator.getInvalidMessages());
    list.addAll(defrayalValidator.getInvalidMessages());
    list.addAll(accountLineEditGrid.getInvalidMessages());
    list.addAll(overdueLineEditGrid.getInvalidMessages());
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
    valid &= accountLineEditGrid.validate();
    valid &= overdueLineEditGrid.validate();
    valid &= defrayalValidator.validate();
    valid &= validator.validate();
    if (valid == false) {
      getMessagePanel().putMessages(getInvalidMessages());
    }

    if (valid && bill.getDefrayalTotal().compareTo(bill.getTotal().getTotal()) != 0) {
      getMessagePanel().putErrorMessage("实付金额与账款实付金额不相等。");
      valid = false;
    }
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

    if (!validate())
      return;
    
    //清除预付款
    bill.setDepositTotal(BigDecimal.ZERO);
    bill.getDeposits().clear();
    bill.setDepositSubject(null);
    
    doSave();
  }

  private void doSave() {
    prepareForSave();

    CommandQueue.offer(new RPCCommand() {
      public void onCall(CommandQueue queue, AsyncCallback callback) {
        RLoadingDialog.show(PaymentMessages.M.actionDoing(PaymentMessages.M.save()));
        PaymentService.Locator.getService().save(bill, getEP().getProcessCtx(), callback);
      }

      public void onFailure(CommandQueue queue, Throwable t) {
        RLoadingDialog.hide();
        String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.save(),
            PaymentMessages.M.payment());
        RMsgBox.showError(msg, t);
        queue.abort();
      }

      public void onSuccess(CommandQueue queue, Object result) {
        RLoadingDialog.hide();
        BPaymentLogger.getInstance().log(PaymentMessages.M.modify(), (BPayment) result);

        Message msg = Message.info(PaymentMessages.M.onSuccess(PaymentMessages.M.save(),
            PaymentMessages.M.payment(), bill.getBillNumber()));
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
        PaymentMessages.M.actionComfirm(PaymentMessages.M.cancel(), PaymentMessages.M.edit()),
        new RMsgBox.ConfirmCallback() {
          public void onClosed(boolean confirmed) {
            if (confirmed)
              RHistory.back();
          }
        });
  }

  @Override
  public void refreshToolbar(RToolbar toolbar) {
    cancelAction.setVisible(!getEP().isProcessMode()); // 执行任务时，不显示取消按钮。HDCRE-700
    getToolbar().rebuild();
  }

  @Override
  protected EPPayment getEP() {
    return EPPayment.getInstance();
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  @Override
  protected void refreshEntity() {
    refresh();
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
      PaymentServiceAgent.executeTask(operation, bill, processCtx, true, this);
    }
  }
}
