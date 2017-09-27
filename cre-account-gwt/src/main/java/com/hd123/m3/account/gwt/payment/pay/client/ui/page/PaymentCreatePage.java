/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentCreatePage.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-5 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client.ui.page;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.hd123.m3.account.gwt.payment.commons.client.biz.BOverdueCalcHelper;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCashDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLogger;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentLineDefrayalLineLocator;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentLineLocator;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentRemarkEditGadget;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentOverdueLineDef;
import com.hd123.m3.account.gwt.payment.pay.client.BPaymentValidator;
import com.hd123.m3.account.gwt.payment.pay.client.EPPayment;
import com.hd123.m3.account.gwt.payment.pay.client.PaymentMessages;
import com.hd123.m3.account.gwt.payment.pay.client.rpc.PaymentService;
import com.hd123.m3.account.gwt.payment.pay.client.rpc.PaymentServiceAgent;
import com.hd123.m3.account.gwt.payment.pay.client.ui.gadget.AccountLineEditGrid;
import com.hd123.m3.account.gwt.payment.pay.client.ui.gadget.OverdueLineEditGrid;
import com.hd123.m3.account.gwt.payment.pay.client.ui.gadget.PaymentGeneralCreateGadget;
import com.hd123.m3.account.gwt.payment.pay.client.ui.gadget.PaymentInfoEditGadget;
import com.hd123.m3.account.gwt.payment.pay.intf.client.PaymentUrlParams.Create;
import com.hd123.m3.account.gwt.payment.pay.intf.client.perm.PaymentPermDef;
import com.hd123.m3.account.gwt.payment.rec.client.biz.ActionName;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmCreatePage;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.RDateUtil;
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
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.TitleBar;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author subinzhu
 * 
 */
public class PaymentCreatePage extends BaseBpmCreatePage implements Create, RValidatable,
    RActionHandler, HasRActionHandlers, SelectionHandler<Message> {

  private static PaymentCreatePage instance;

  public static PaymentCreatePage getInstance() {
    if (instance == null)
      instance = new PaymentCreatePage();
    return instance;
  }

  private EPPayment ep = EPPayment.getInstance();
  private BPayment bill;

  private RAction saveAction;
  private RAction cancelAction;

  private RTabPanel tabPanel;
  private PaymentGeneralCreateGadget generalGadget;
  private AccountLineEditGrid accountLineEditGrid;
  private OverdueLineEditGrid overdueLineEditGrid;
  private PaymentInfoEditGadget paymentInfoEditGadget;
  private PaymentRemarkEditGadget remarkGadget;

  protected BPaymentValidator validator;
  /** 按总额收款时验证 */
  protected BPaymentDefrayalValidator defrayalValidator;

  private Handler_click clickHandler = new Handler_click();

  public PaymentCreatePage() {
    super();
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

    generalGadget = new PaymentGeneralCreateGadget();
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

    decodeParams(params, new Command() {
      public void execute() {
        bill.aggregate(ep.getScale(), ep.getRoundingMode());
        refresh(bill);
        tabPanel.selectTab(0);
        getEP().appendSearchBox();
      }
    });
  }

  @Override
  public void refreshTitle(TitleBar titleBar) {
    titleBar.clearStandardTitle();
    titleBar.setTitleText(PaymentMessages.M.create());
    titleBar.appendAttributeText(getEP().getModuleCaption());
  }

  private boolean checkIn() {
    if (getEP().isBpmMode())
      return true;
    if (getEP().isPermitted(PaymentPermDef.CREATE) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void decodeParams(final JumpParameters params, final Command callback) {
    assert callback != null;
    RLoadingDialog.show(PaymentMessages.M.actionDoing(PaymentMessages.M.create()));

    if (params.getExtend().get(PN_CREATEBYSTATEMENT) != null) {
      doCreateByStatement((List<String>) params.getExtend().get(PN_CREATEBYSTATEMENT), callback);
    } else if (params.getExtend().get(PN_CREATEBYINVOICEREG) != null) {
      doCreateByInvoiceReg((List<String>) params.getExtend().get(PN_CREATEBYINVOICEREG), callback);
    } else if (params.getExtend().get(PN_CREATEBYPAYMENTNOTICE) != null) {
      doCreateByPaymentNotice((List<String>) params.getExtend().get(PN_CREATEBYPAYMENTNOTICE),
          callback);
    } else if (params.getExtend().get(PN_CREATEBYSOURCEBILL) != null) {
      doCreateBySourceBill((List<String>) params.getExtend().get(PN_CREATEBYSOURCEBILL), callback);
    } else if (params.getExtend().get(PN_CREATEBYACCOUNT) != null) {
      doCreateByAccount((List<String>) params.getExtend().get(PN_CREATEBYACCOUNT), callback);
    } else if (params.getUrlRef().get(PN_CREATEBYSTATEMENT) != null) {
      List<String> statementUuids = new ArrayList<String>();
      statementUuids.add(params.getUrlRef().get(PN_CREATEBYSTATEMENT));
      doCreateByStatement(statementUuids, callback);
    } else if (params.getUrlRef().get(PN_CREATEBYINVOICEREG) != null) {
      List<String> invoiceUuids = new ArrayList<String>();
      invoiceUuids.add(params.getUrlRef().get(PN_CREATEBYINVOICEREG));
      doCreateByInvoiceReg(invoiceUuids, callback);
    } else if (params.getUrlRef().get(PN_CREATEBYPAYMENTNOTICE) != null) {
      List<String> noticeUuids = new ArrayList<String>();
      noticeUuids.add(params.getUrlRef().get(PN_CREATEBYPAYMENTNOTICE));
      doCreateByPaymentNotice(noticeUuids, callback);
    } else if (params.getUrlRef().get(PN_CREATEBYSOURCEBILL) != null) {
      List<String> sourceBillUuids = new ArrayList<String>();
      sourceBillUuids.add(params.getUrlRef().get(PN_CREATEBYSOURCEBILL));
      doCreateBySourceBill(sourceBillUuids, callback);
    } else if (params.getUrlRef().get(PN_CREATEBYACCOUNT) != null) {
      List<String> accountUuids = new ArrayList<String>();
      accountUuids.add(params.getUrlRef().get(PN_CREATEBYACCOUNT));
      doCreateByAccount(accountUuids, callback);
    } else {
      doCreate(callback);
    }
  }

  private void doCreateByStatement(List<String> statementUuids, final Command callback) {
    assert statementUuids != null;
    assert !statementUuids.isEmpty();

    PaymentService.Locator.getService().createByStatements(statementUuids,
        new RBAsyncCallback2<BPayment>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.create(),
                PaymentMessages.M.payment());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BPayment result) {
            RLoadingDialog.hide();
            bill = result;
            callback.execute();
          }
        });
  }

  private void doCreateByInvoiceReg(List<String> invoiceUuids, final Command callback) {
    assert invoiceUuids != null;
    assert !invoiceUuids.isEmpty();

    PaymentService.Locator.getService().createByInvoices(invoiceUuids,
        new RBAsyncCallback2<BPayment>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.create(),
                PaymentMessages.M.payment());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BPayment result) {
            RLoadingDialog.hide();
            bill = result;
            callback.execute();
          }
        });
  }

  private void doCreateByPaymentNotice(List<String> noticeUuids, final Command callback) {
    assert noticeUuids != null;
    assert !noticeUuids.isEmpty();

    PaymentService.Locator.getService().createByPaymentNotices(noticeUuids,
        new RBAsyncCallback2<BPayment>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.create(),
                PaymentMessages.M.payment());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BPayment result) {
            RLoadingDialog.hide();
            bill = result;
            callback.execute();
          }
        });
  }

  private void doCreateBySourceBill(List<String> sourceBillUuids, final Command callback) {
    assert sourceBillUuids != null;
    assert !sourceBillUuids.isEmpty();

    PaymentService.Locator.getService().createBySourceBills(sourceBillUuids,
        new RBAsyncCallback2<BPayment>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.create(),
                PaymentMessages.M.payment());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BPayment result) {
            RLoadingDialog.hide();
            bill = result;
            callback.execute();
          }
        });
  }

  private void doCreateByAccount(List<String> accountUuids, final Command callback) {
    assert accountUuids != null;
    assert !accountUuids.isEmpty();

    PaymentService.Locator.getService().createByAccounts(accountUuids,
        new RBAsyncCallback2<BPayment>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.create(),
                PaymentMessages.M.payment());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BPayment result) {
            RLoadingDialog.hide();
            bill = result;
            callback.execute();
          }
        });
  }

  private void doCreate(final Command callback) {
    PaymentService.Locator.getService().create(new RBAsyncCallback2<BPayment>() {
      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.create(),
            PaymentMessages.M.payment());
        RMsgBox.showErrorAndBack(msg, caught);
      }

      @Override
      public void onSuccess(BPayment result) {
        RLoadingDialog.hide();
        bill = result;
        callback.execute();
      }
    });
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
  public void onSelection(SelectionEvent<Message> event) {
    if (event.getSelectedItem().getSource() instanceof PaymentLineLocator) {
      PaymentLineLocator o = (PaymentLineLocator) event.getSelectedItem().getSource();
      if (o != null) {
        if (o.getTabId() != null) {
          RActionEvent.fire(PaymentCreatePage.this, ActionName.CREATEPAGE_CHANGE_LINE, o);
        }
      }
    } else if (event.getSelectedItem().getSource() instanceof PaymentLineDefrayalLineLocator) {
      PaymentLineDefrayalLineLocator o = (PaymentLineDefrayalLineLocator) event.getSelectedItem()
          .getSource();
      if (o != null) {
        if (o.getTabId() != null) {
          RActionEvent.fire(PaymentCreatePage.this,
              ActionName.CREATEPAGE_CHANGE_LINE_AND_LINEDEFRAYALLINE, o);
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
      generalGadget.calculateRightPanel();
      generalGadget.refreshRightPanel();
      overdueLineEditGrid.refresh();
    } else if (event.getActionName() == AccountLineEditGrid.ActionName.ACTION_ACCOUNTLINE_REFRESHOVERDUE) {
      overdueLineEditGrid.refresh();
    } else if (event.getActionName() == OverdueLineEditGrid.ActionName.ACTION_OVERDUELINE_AGGREGATE) {
      bill.aggregate(ep.getScale(), ep.getRoundingMode());
      generalGadget.calculateRightPanel();
      generalGadget.refreshRightPanel();
      accountLineEditGrid.refresh();
    } else if (event.getActionName() == OverdueLineEditGrid.ActionName.ACTION_OVERDUELINE_REFRESHACCOUNT) {
      accountLineEditGrid.refresh();
    } else if (event.getActionName() == ActionName.ACTION_GENERALCREATE_COUNTERPARTCHANGED) {
      refresh();
    } else if (event.getActionName() == ActionName.ACTION_GENERALCREATE_RECEIVEDATECHANGED) {
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
    if (defrayalValidator != null) {
      defrayalValidator.clearValidResults();
    }
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

    if (!validate()){
      return;
    }
    
    //清除预付款
    bill.setDepositTotal(BigDecimal.ZERO);
    bill.getDeposits().clear();
    bill.setDepositSubject(null);
    
    doSave();
  }

  private void doSave() {
    GWTUtil.blurActiveElement();

    if (!validate()) {
      return;
    }

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
        BPaymentLogger.getInstance().log(PaymentMessages.M.create(), (BPayment) result);

        Message msg = Message.info(PaymentMessages.M.actionSuccess(PaymentMessages.M.save(),
            PPaymentDef.TABLE_CAPTION));
        getEP().jumpToViewPage(((BPayment) result).getUuid(), msg);
        queue.goon();
      }
    });
    CommandQueue.awake();
  }

  private void doCancel() {
    RMsgBox.showConfirm(
        PaymentMessages.M.actionComfirm(PaymentMessages.M.cancel(), PaymentMessages.M.create()),
        new RMsgBox.ConfirmCallback() {
          public void onClosed(boolean confirmed) {
            if (confirmed)
              RHistory.back();
          }
        });
  }

  @Override
  public void onHide() {
    generalGadget.clearQueryConditions();
    accountLineEditGrid.clearQueryConditions();
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
    if (bill.getAccountUnit() == null && getEP().getUserStores().size() == 1) {
      bill.setAccountUnit(new BUCN(getEP().getUserStores().get(0)));
    }
    bill.setPaymentDate(RDateUtil.truncate(new Date(), RDateUtil.DAY_OF_MONTH));
    bill.setDealer(new BUCN(getEP().getCurrentEmployee().getUuid(), getEP().getCurrentEmployee()
        .getCode(), getEP().getCurrentEmployee().getName()));
    bill.setDefrayalType(CPaymentDefrayalType.bill);
    // 计算滞纳金
    BOverdueCalcHelper.calculate(bill, ep.getScale(), ep.getRoundingMode(), null);
    defrayalValidator = new BPaymentDefrayalValidator(bill, paymentInfoEditGadget);

    validator = new BPaymentValidator(bill);

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
