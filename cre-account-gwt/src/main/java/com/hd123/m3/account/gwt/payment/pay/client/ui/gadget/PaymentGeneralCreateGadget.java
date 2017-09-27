/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentGeneralCreateGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.AccountUnitBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.CounterpartBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BOverdueCalcHelper;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCashDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDepositDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineCash;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDeposit;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.pay.client.EPPayment;
import com.hd123.m3.account.gwt.payment.pay.client.PaymentMessages;
import com.hd123.m3.account.gwt.payment.pay.client.rpc.PaymentService;
import com.hd123.m3.account.gwt.payment.rec.client.biz.ActionName;
import com.hd123.m3.account.gwt.util.client.ObjectUtil;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstField;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstFieldUtil;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox.ConfirmCallback;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * 基本信息新建控件
 * 
 * @author subinzhu
 * 
 */
public class PaymentGeneralCreateGadget extends RCaptionBox implements RActionHandler,
    HasRActionHandlers, HasFocusables, FocusOnFirstField {

  private EPPayment ep = EPPayment.getInstance();
  private BPayment bill;

  // 基本信息
  private RForm generalForm;
  private AccountUnitUCNBox accountUnitUCNBox;

  private RCombinedField countpartField;
  private CounterpartUCNBox counterpartUCNBox;
  private RViewStringField countpartTypeField;

  private RDateBox paymentDateBox;
  private EmployeeUCNBox employeeBox;
  private RDateBox incomeDateBox;

  private PermGroupEditField permGroupField;

  // 合计
  private RViewNumberField receiptTotalField;
  private RViewNumberField unpayedTotalField;
  private RViewNumberField totalField;
  private RViewNumberField defrayalTotalField;
  private RViewNumberField overdueTotalField;

  // 产生预付款
  private Handler_change changeHandler = new Handler_change();

  public BPayment getBill() {
    return bill;
  }

  public void setBill(BPayment bill) {
    this.bill = bill;
  }

  public PaymentGeneralCreateGadget() {
    super();
    drawSelf();
  }

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    vp.add(mvp);

    mvp.add(0, drawGeneralPanel());

    permGroupField = new PermGroupEditField(ep.isPermEnabled(), ep.getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    mvp.add(1, drawTotalPanel());

    setCaption(PaymentMessages.M.generalInfo());
    setWidth("100%");
    setContent(vp);
    setEditing(true);
  }

  private Widget drawGeneralPanel() {
    generalForm = new RForm(1);
    generalForm.setWidth("100%");

    accountUnitUCNBox = new AccountUnitUCNBox();
    accountUnitUCNBox.setCaption(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));
    accountUnitUCNBox.setRequired(true);
    accountUnitUCNBox.addChangeHandler(changeHandler);
    generalForm.addField(accountUnitUCNBox);

    counterpartUCNBox = new CounterpartUCNBox(ep.getCaptionMap());
    counterpartUCNBox.setCodeBoxWidth(Float.parseFloat("0.44"));
    counterpartUCNBox.setCounterTypeMap(ep.getCounterpartTypeMap());
    counterpartUCNBox.setRequired(true);
    counterpartUCNBox.addChangeHandler(changeHandler);

    countpartTypeField = new RViewStringField();

    countpartField = new RCombinedField() {
      {
        setCaption(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
        if (ep.getCounterpartTypeMap().size() > 1) {
          addField(counterpartUCNBox, 0.85f);
          addField(new HTML(), 0.05f);
          addField(countpartTypeField, 0.1f);
        } else {
          addField(counterpartUCNBox, 1);

        }
      }

      @Override
      public boolean validate() {
        return counterpartUCNBox.validate();
      }

      @Override
      public void clearMessages() {
        counterpartUCNBox.clearMessages();
      }

      @Override
      public boolean isValid() {
        return counterpartUCNBox.isValid();
      }

      @Override
      public List<Message> getInvalidMessages() {
        return counterpartUCNBox.getInvalidMessages();
      }
    };
    countpartField.setRequired(true);
    generalForm.addField(countpartField);

    paymentDateBox = new RDateBox(PaymentMessages.M.paymentDate());
    paymentDateBox.setCaption(PaymentMessages.M.paymentDate());
    paymentDateBox.setRequired(true);
    paymentDateBox.addChangeHandler(changeHandler);
    generalForm.addField(paymentDateBox);

    employeeBox = new EmployeeUCNBox();
    employeeBox.setCaption(PPaymentDef.constants.dealer());
    employeeBox.addChangeHandler(changeHandler);
    generalForm.addField(employeeBox);

    incomeDateBox = new RDateBox(PPaymentDef.incomeDate);
    incomeDateBox.addChangeHandler(changeHandler);
    generalForm.addField(incomeDateBox);

    generalForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setContent(generalForm);
    box.setCaption(PaymentMessages.M.basicInfo());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    return box;
  }

  private Widget drawTotalPanel() {
    RForm form = new RForm(1);
    form.setWidth("50%");

    receiptTotalField = new RViewNumberField(PPaymentDef.receiptTotal_total);
    receiptTotalField.setWidth("50%");
    receiptTotalField.setFormat(GWTFormat.fmt_money);
    receiptTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    // form.addField(receiptTotalField);

    unpayedTotalField = new RViewNumberField(PPaymentDef.unpayedTotal_total);
    unpayedTotalField.setWidth("50%");
    unpayedTotalField.setFormat(GWTFormat.fmt_money);
    unpayedTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(unpayedTotalField);

    totalField = new RViewNumberField(PPaymentDef.total_total);
    totalField.setWidth("50%");
    totalField.setFormat(GWTFormat.fmt_money);
    totalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(totalField);

    defrayalTotalField = new RViewNumberField(PPaymentDef.defrayalTotal);
    defrayalTotalField.setWidth("50%");
    defrayalTotalField.setFormat(GWTFormat.fmt_money);
    defrayalTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(defrayalTotalField);

    overdueTotalField = new RViewNumberField(PPaymentDef.overdueTotal);
    overdueTotalField.setWidth("50%");
    overdueTotalField.setFormat(GWTFormat.fmt_money);
    overdueTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(overdueTotalField);

    form.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setContent(form);
    box.setCaption(PaymentMessages.M.accountTotal());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    return box;
  }

  public void refresh() {
    assert bill != null;

    accountUnitUCNBox.setValue(bill.getAccountUnit());
    counterpartUCNBox.setValue(bill.getCounterpart());
    if (ep.getCounterpartTypeMap().size() > 1)
      countpartTypeField.setValue(bill.getCounterpart() == null ? null : ep.getCounterpartTypeMap()
          .get(bill.getCounterpart().getCounterpartType()));
    paymentDateBox.setValue(bill.getPaymentDate());
    employeeBox.setValue(bill.getDealer());
    incomeDateBox.setValue(bill.getIncomeDate());

    permGroupField.setPerm(bill);
    refreshRightPanel();

    doChangeCounterpart(false);
  }

  @Override
  public void clearValidResults() {
    super.clearValidResults();
    generalForm.clearValidResults();
    permGroupField.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return generalForm.isValid() & validateDepositSubject() & permGroupField.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> messages = new ArrayList<Message>();
    messages.addAll(generalForm.getInvalidMessages());
    messages.addAll(permGroupField.getInvalidMessages());
    return messages;
  }

  public boolean validate() {
    clearValidResults();
    return generalForm.validate() & validateDepositSubject() & permGroupField.validate();
  }

  private boolean validateDepositSubject() {
    if (bill.getDepositTotal().compareTo(BigDecimal.ZERO) > 0
        && (bill.getDepositSubject() == null || StringUtil.isNullOrBlank(bill.getDepositSubject()
            .getUuid()))) {
      return false;
    }
    return true;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (ObjectUtil.isEquals(event.getActionName(), ActionName.ACTION_PAYMENTINFO_CHANGE)) {
      defrayalTotalField.setValue(bill.getDefrayalTotal());
      refreshtTotalField();
    }
  }

  private void refreshtTotalField(){
    BTotal total = BTotal.zero();
    for(BPaymentAccountLine line:bill.getAccountLines()){
      total = total.add(line.getTotal());
    }
    bill.setTotal(total);
    
    totalField.setValue(bill.getTotal().getTotal());
  }

  /** 合计并刷新应收总额、产生的预存款金额以及滞纳金。 */
  public void calculateRightPanel() {
    bill.aggregate(ep.getScale(), ep.getRoundingMode());
    unpayedTotalField.setValue(bill.getUnpayedTotal().getTotal());
    bill.getTotal().setTotal(bill.getDefrayalTotal()); // 不考虑预付款，二者是相等的
    
    defrayalTotalField.setValue(bill.getDefrayalTotal());
    totalField.setValue(bill.getTotal().getTotal());
    refreshOverdueTotal();
  }

  /** 刷新滞纳金 */
  public void refreshOverdueTotal() {
    bill.aggregateOverdue(ep.getScale(), ep.getRoundingMode());
    overdueTotalField.setValue(bill.getOverdueTotal().getTotal());
  }

  private class Handler_change implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == accountUnitUCNBox) {
        doChangeAccountUnitConfirm();
      } else if (event.getSource() == counterpartUCNBox) {
        doCounterPartConfirm();
      } else if (event.getSource() == paymentDateBox) {
        if (paymentDateBox.getValue() != null) {
          bill.setPaymentDate(paymentDateBox.getValue());
          // 重算滞纳金
          BOverdueCalcHelper.calculate(bill, ep.getScale(), ep.getRoundingMode(), null);
          refreshRightPanel();
          RActionEvent.fire(PaymentGeneralCreateGadget.this,
              ActionName.ACTION_GENERALCREATE_RECEIVEDATECHANGED);
        }
      } else if (event.getSource() == employeeBox) {
        bill.setDealer(employeeBox.getValue());
      } else if (event.getSource() == incomeDateBox) {
        bill.setIncomeDate(incomeDateBox.getValue());
      }
    }

    /** 是否存在有效的付款信息 */
    private boolean existValidDefrayal() {
      if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
        for (BPaymentCashDefrayal cash : bill.getCashs()) {
          if (cash.getPaymentType() != null && cash.getTotal() != null
              && cash.getTotal().compareTo(BigDecimal.ZERO) != 0) {
            return true;
          }
        }

        for (BPaymentDepositDefrayal deposit : bill.getDeposits()) {
          if (deposit.getSubject() != null && deposit.getRemainTotal() != null
              && deposit.getRemainTotal().compareTo(BigDecimal.ZERO) > 0
              && deposit.getTotal() != null && deposit.getTotal().compareTo(BigDecimal.ZERO) > 0
              && deposit.getTotal().compareTo(deposit.getRemainTotal()) <= 0) {
            return true;
          }
        }
      } else if (CPaymentDefrayalType.line.equals(bill.getDefrayalType())) {
        for (BPaymentLine line : bill.getAccountLines()) {
          for (BPaymentLineCash lineCach : line.getCashs()) {
            if (lineCach.getPaymentType() != null && lineCach.getTotal() != null
                && lineCach.getTotal().compareTo(BigDecimal.ZERO) != 0) {
              return true;
            }
          }
          for (BPaymentLineDeposit lineDeposit : line.getDeposits()) {
            if (lineDeposit.getSubject() != null && lineDeposit.getRemainTotal() != null
                && lineDeposit.getRemainTotal().compareTo(BigDecimal.ZERO) > 0
                && lineDeposit.getTotal() != null
                && lineDeposit.getTotal().compareTo(BigDecimal.ZERO) > 0
                && lineDeposit.getTotal().compareTo(lineDeposit.getRemainTotal()) <= 0) {
              return true;
            }
          }
        }

        for (BPaymentLine line : bill.getOverdueLines()) {
          for (BPaymentLineCash lineCach : line.getCashs()) {
            if (lineCach.getPaymentType() != null && lineCach.getTotal() != null
                && lineCach.getTotal().compareTo(BigDecimal.ZERO) != 0) {
              return true;
            }
          }
          for (BPaymentLineDeposit lineDeposit : line.getDeposits()) {
            if (lineDeposit.getSubject() != null && lineDeposit.getRemainTotal() != null
                && lineDeposit.getRemainTotal().compareTo(BigDecimal.ZERO) > 0
                && lineDeposit.getTotal() != null
                && lineDeposit.getTotal().compareTo(BigDecimal.ZERO) > 0
                && lineDeposit.getTotal().compareTo(lineDeposit.getRemainTotal()) <= 0) {
              return true;
            }
          }
        }
      }
      return false;
    }

    private void doChangeAccountUnitConfirm() {
      if (bill.getAccountUnit() != null
          && !ObjectUtil.isEquals(bill.getAccountUnit().getUuid(), accountUnitUCNBox.getValue()
              .getUuid())
          && (!bill.getAccountLines().isEmpty() || !bill.getOverdueLines().isEmpty() || existValidDefrayal())) {
        RMsgBox.showConfirm(PaymentMessages.M.changeConfirm(accountUnitUCNBox.getCaption()),
            new ConfirmCallback() {
              public void onClosed(boolean confirmed) {
                if (confirmed) {
                  doChangeAccountUnit(true);

                  fetchReceiptTotal();

                  RActionEvent.fire(PaymentGeneralCreateGadget.this,
                      ActionName.ACTION_GENERALCREATE_REFRESH_SUBJECTS_AND_CONTRACTS);
                  RActionEvent.fire(PaymentGeneralCreateGadget.this,
                      ActionName.ACTION_GENERALCREATE_REFRESH_REMAINTOTAL);
                  RActionEvent.fire(PaymentGeneralCreateGadget.this,
                      ActionName.ACTION_GENERALCREATE_REFRESH_BANKS);
                } else {
                  counterpartUCNBox.setValue(bill.getCounterpart());
                  if (ep.getCounterpartTypeMap().size() > 1)
                    countpartTypeField.setValue(bill.getCounterpart() == null ? null : ep
                        .getCounterpartTypeMap().get(bill.getCounterpart().getCounterpartType()));
                }
              }
            });
      } else {
        doChangeAccountUnit(false);
        fetchReceiptTotal();

        RActionEvent.fire(PaymentGeneralCreateGadget.this,
            ActionName.ACTION_GENERALCREATE_REFRESH_SUBJECTS_AND_CONTRACTS);
        RActionEvent.fire(PaymentGeneralCreateGadget.this,
            ActionName.ACTION_GENERALCREATE_REFRESH_REMAINTOTAL);
        RActionEvent.fire(PaymentGeneralCreateGadget.this,
            ActionName.ACTION_GENERALCREATE_REFRESH_BANKS);
      }
    }

    private void doCounterPartConfirm() {
      if (bill.getCounterpart() != null
          && !ObjectUtil.isEquals(bill.getCounterpart().getUuid(), counterpartUCNBox.getValue()
              .getUuid())
          && (!bill.getAccountLines().isEmpty() || !bill.getOverdueLines().isEmpty() || existValidDefrayal())) {
        RMsgBox.showConfirm(PaymentMessages.M.changeConfirm(counterpartUCNBox.getCaption()),
            new ConfirmCallback() {
              public void onClosed(boolean confirmed) {
                if (confirmed) {
                  doChangeCounterpart(true);
                  fetchReceiptTotal();

                  RActionEvent.fire(PaymentGeneralCreateGadget.this,
                      ActionName.ACTION_GENERALCREATE_REFRESH_SUBJECTS_AND_CONTRACTS);
                  RActionEvent.fire(PaymentGeneralCreateGadget.this,
                      ActionName.ACTION_GENERALCREATE_REFRESH_REMAINTOTAL);
                } else {
                  counterpartUCNBox.setValue(bill.getCounterpart());
                  if (ep.getCounterpartTypeMap().size() > 1)
                    countpartTypeField.setValue(bill.getCounterpart() == null ? null : ep
                        .getCounterpartTypeMap().get(bill.getCounterpart().getCounterpartType()));
                }
              }
            });
      } else {
        doChangeCounterpart(false);
        fetchReceiptTotal();

        RActionEvent.fire(PaymentGeneralCreateGadget.this,
            ActionName.ACTION_GENERALCREATE_REFRESH_SUBJECTS_AND_CONTRACTS);
        RActionEvent.fire(PaymentGeneralCreateGadget.this,
            ActionName.ACTION_GENERALCREATE_REFRESH_REMAINTOTAL);
      }
    }
  }

  private void fetchReceiptTotal() {
    if (bill.getAccountUnit() == null || bill.getCounterpart() == null
        || StringUtil.isNullOrBlank(bill.getAccountUnit().getUuid())
        || StringUtil.isNullOrBlank(bill.getCounterpart().getUuid()))
      return;
    PaymentService.Locator.getService().fetchReceiptTotal(bill.getAccountUnit().getUuid(),
        bill.getCounterpart().getUuid(), new RBAsyncCallback2<BTotal>() {

          @Override
          public void onException(Throwable caught) {
            String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.get(),
                PPaymentDef.constants.receiptTotal());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(BTotal result) {
            bill.setReceiptTotal(result == null ? BTotal.zero() : result);
            receiptTotalField.setValue(result.getTotal() == null ? BigDecimal.ZERO : result
                .getTotal());
          }
        });
  }

  private void doChangeAccountUnit(boolean clear) {
    bill.setAccountUnit(accountUnitUCNBox.getValue());

    if (clear)
      doClearLinesAndDerayals();
  }

  private void doChangeCounterpart(boolean clear) {
    bill.setCounterpart(counterpartUCNBox.getRawValue());
    if (ep.getCounterpartTypeMap().size() > 1)
      countpartTypeField.setValue(counterpartUCNBox.getRawValue() == null ? null : ep
          .getCounterpartTypeMap().get(counterpartUCNBox.getRawValue().getCounterpartType()));

    if (clear)
      doClearLinesAndDerayals();
  }

  /** 清空明细行和支付明细等信息 */
  private void doClearLinesAndDerayals() {
    bill.getAccountLines().clear();
    bill.getOverdueLines().clear();
    bill.getCashs().clear();
    bill.getDeposits().clear();
    bill.getTotal().setTax(BigDecimal.ZERO);
    bill.getTotal().setTotal(BigDecimal.ZERO);
    bill.setDefrayalTotal(BigDecimal.ZERO);
    bill.getOverdueTotal().setTax(BigDecimal.ZERO);
    bill.getOverdueTotal().setTotal(BigDecimal.ZERO);
    bill.setDepositTotal(BigDecimal.ZERO);
    bill.getUnpayedTotal().setTotal(BigDecimal.ZERO);
    bill.getUnpayedTotal().setTax(BigDecimal.ZERO);

    RActionEvent.fire(PaymentGeneralCreateGadget.this,
        ActionName.ACTION_GENERALCREATE_COUNTERPARTCHANGED);
  }

  /** 刷新右侧的合计以及产生的预存款金额 */
  public void refreshRightPanel() {
    assert bill != null;

    fetchReceiptTotal();

    receiptTotalField.setValue(bill.getReceiptTotal() == null ? BigDecimal.ZERO : bill
        .getReceiptTotal().getTotal());
    unpayedTotalField.setValue(bill.getUnpayedTotal() == null ? BigDecimal.ZERO : bill
        .getUnpayedTotal().getTotal());
    totalField.setValue(bill.getTotal() == null ? BigDecimal.ZERO : bill.getTotal().getTotal());
    defrayalTotalField.setValue(bill.getDefrayalTotal() == null ? BigDecimal.ZERO : bill
        .getDefrayalTotal());
    overdueTotalField.setValue(bill.getOverdueTotal() == null ? BigDecimal.ZERO : bill
        .getOverdueTotal().getTotal());
  }

  @Override
  public Focusable getFocusable(String field) {
    if (BPayment.FN_COUNTERPART.equals(field))
      return counterpartUCNBox;
    else if (BPayment.FN_PAYMENTDATE.equals(field))
      return paymentDateBox;
    else if (BPayment.FN_DEALER.equals(field))
      return employeeBox;
    return null;
  }

  @Override
  public boolean focusOnFirstField() {
    return FocusOnFirstFieldUtil.focusOnFirstField(generalForm);
  }

  public void clearQueryConditions() {
    if (accountUnitUCNBox != null)
      ((AccountUnitBrowserDialog) accountUnitUCNBox.getBrowser()).clearConditions();
    if (counterpartUCNBox != null)
      ((CounterpartBrowserDialog) counterpartUCNBox.getBrowser()).clearConditions();
    if (employeeBox != null)
      employeeBox.clearConditions();
  }

}
