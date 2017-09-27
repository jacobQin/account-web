/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	ReceiptGeneralCreateGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.AccountUnitBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.CounterpartBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BOverdueCalcHelper;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCashDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDepositDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineCash;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDeposit;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.DefrayalTypeComboBox;
import com.hd123.m3.account.gwt.payment.commons.client.rpc.PaymentCommonsService;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentCollectionLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.account.gwt.payment.rec.client.biz.ActionName;
import com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptService;
import com.hd123.m3.account.gwt.util.client.ObjectUtil;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstField;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstFieldUtil;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
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
public class ReceiptGeneralCreateGadget extends RCaptionBox implements RActionHandler,
    HasRActionHandlers, HasFocusables, FocusOnFirstField {

  private EPReceipt ep = EPReceipt.getInstance();
  private BPayment bill;

  // 基本信息
  private RForm generalForm;
  private AccountUnitUCNBox accountUnitUCNBox;
  private RCombinedField countpartField;
  private CounterpartUCNBox counterpartUCNBox;
  private RViewStringField countpartTypeField;
  private RDateBox receiptDateBox;
  private EmployeeUCNBox employeeBox;
  private DefrayalTypeComboBox defrayalTypeField;
  private RDateBox incomeDateBox;

  private PermGroupEditField permGroupField;

  // 合计
  private RViewNumberField unpayedTotalField;
  //private RViewNumberField totalField;
  private RViewNumberField defrayalTotalField;
  private RViewNumberField overdueTotalField;
  private RViewNumberField depositRemainTotalField;

  // 产生预付款
  private RForm depositForm;
  private RViewNumberField depositTotalField;
  private SubjectUCNBox subjectUCNBox;

  private Handler_change changeHandler = new Handler_change();

  public BPayment getBill() {
    return bill;
  }

  public void setBill(BPayment bill) {
    this.bill = bill;
  }

  public ReceiptGeneralCreateGadget() {
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

    Widget w = drawGeneralPanel();
    mvp.add(0, w);

    w = drawDepositPanel();
    mvp.add(0, w);

    permGroupField = new PermGroupEditField(ep.isPermEnabled(), ep.getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    w = drawTotalPanel();
    mvp.add(1, w);

    setCaption(ReceiptMessages.M.generalInfo());
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

    receiptDateBox = new RDateBox(PPaymentDef.paymentDate);
    receiptDateBox.setRequired(true);
    receiptDateBox.addChangeHandler(changeHandler);
    generalForm.addField(receiptDateBox);

    employeeBox = new EmployeeUCNBox();
    employeeBox.setCaption(PPaymentDef.constants.dealer());
    employeeBox.addChangeHandler(changeHandler);
    generalForm.addField(employeeBox);

    defrayalTypeField = new DefrayalTypeComboBox(DirectionType.receipt.getDirectionValue());
    defrayalTypeField.addChangeHandler(changeHandler);
    generalForm.addField(defrayalTypeField);

    incomeDateBox = new RDateBox(PPaymentDef.incomeDate);
    incomeDateBox.addChangeHandler(changeHandler);
    generalForm.addField(incomeDateBox);

    generalForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setContent(generalForm);
    box.setCaption(ReceiptMessages.M.basicInfo());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    return box;
  }

  private Widget drawTotalPanel() {
    RForm form = new RForm(1);
    form.setWidth("50%");

    unpayedTotalField = new RViewNumberField(PPaymentDef.unpayedTotal_total);
    unpayedTotalField.setWidth("50%");
    unpayedTotalField.setFormat(GWTFormat.fmt_money);
    unpayedTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(unpayedTotalField);

   /* totalField = new RViewNumberField(PPaymentDef.total_total);
    totalField.setWidth("50%");
    totalField.setFormat(GWTFormat.fmt_money);
    totalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(totalField);*/
    
    defrayalTotalField = new RViewNumberField(PPaymentDef.defrayalTotal);
    defrayalTotalField.setWidth("50%");
    defrayalTotalField.setFormat(GWTFormat.fmt_money);
    defrayalTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(defrayalTotalField);

    overdueTotalField = new RViewNumberField();
    overdueTotalField.setWidth("50%");
    overdueTotalField.setCaption(PPaymentDef.constants.overdueTotal());
    overdueTotalField.setFormat(GWTFormat.fmt_money);
    overdueTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(overdueTotalField);

    depositRemainTotalField = new RViewNumberField();
    depositRemainTotalField.setWidth("50%");
    depositRemainTotalField.setCaption(PPaymentDef.constants.depositRemainTotal());
    depositRemainTotalField.setFormat(GWTFormat.fmt_money);
    depositRemainTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(depositRemainTotalField);

    form.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setContent(form);
    box.setCaption(ReceiptMessages.M.accountTotal());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    return box;
  }

  private Widget drawDepositPanel() {
    depositForm = new RForm(1);
    depositForm.setWidth("50%");

    depositTotalField = new RViewNumberField(PPaymentDef.depositTotal);
    depositTotalField.setWidth("50%");
    depositTotalField.setFormat(GWTFormat.fmt_money);
    depositTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    depositForm.addField(depositTotalField);

    subjectUCNBox = new SubjectUCNBox(BSubjectType.predeposit.toString(),
        DirectionType.receipt.getDirectionValue());
    subjectUCNBox.setCaption(PPaymentDef.constants.depositSubject());
    subjectUCNBox.addChangeHandler(changeHandler);
    depositForm.addField(subjectUCNBox);

    depositForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setContent(depositForm);
    box.setCaption(ReceiptMessages.M.generateRec());
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
    receiptDateBox.setValue(bill.getPaymentDate());
    employeeBox.setValue(bill.getDealer());
    defrayalTypeField.setValue(bill.getDefrayalType(),true);
    incomeDateBox.setValue(bill.getIncomeDate());

    permGroupField.setPerm(bill);
    refreshRightPanel();

    subjectUCNBox.setValue(bill.getDepositSubject());

    doChangeCounterpart(false);
  }

  @Override
  public void clearValidResults() {
    super.clearValidResults();
    generalForm.clearValidResults();
    depositForm.clearValidResults();
    permGroupField.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return generalForm.isValid() & validateDepositSubject() & depositForm.isValid()
        & permGroupField.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> messages = new ArrayList<Message>();
    messages.addAll(generalForm.getInvalidMessages());
    messages.addAll(depositForm.getInvalidMessages());
    messages.addAll(permGroupField.getInvalidMessages());
    messages.addAll(subjectUCNBox.getInvalidMessages());
    return messages;
  }

  public boolean validate() {
    clearValidResults();
    return generalForm.validate() & validateDepositSubject() & depositForm.validate()
        & permGroupField.validate();
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
      depositTotalField.setValue(bill.getDepositTotal());
      refreshSubjectUCNBoxRequired(bill.getDepositTotal());
    } else if (ObjectUtil.isEquals(event.getActionName(),
        ActionName.ACTION_LINEACCOUNTLINE_TOTALCHANGE)
        || ObjectUtil
            .isEquals(event.getActionName(), ActionName.ACTION_LINECASHDEFRAYALLINE_CHANGE)) {
      calculateRightPanel();
    } else if (ObjectUtil.isEquals(event.getActionName(),
        ActionName.ACTION_BILLOVERDUELINE_TOTALCHANGE)) {
      calculateRightPanel();
    } else if (ObjectUtil.isEquals(event.getActionName(),
        ActionName.ACTION_BILLOVERDUELINE_UNPAYEDTOTALCHANGE)) {
      bill.aggregateOverdue(ep.getScale(), ep.getRoundingMode());
      overdueTotalField.setValue(bill.getOverdueTotal().getTotal());
    } else if (ObjectUtil.isEquals(event.getActionName(),
        ActionName.ACTION_LINEACCOUNTLINE_CASHDEFRAYALCHANGE)) {
      if (!CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
        doChangeDefrayalAndDepositTotal();
      }
    } else if (ObjectUtil.isEquals(event.getActionName(),
        ActionName.ACTION_REFRESH_AFFTER_APPORTION)) {
      //totalField.setValue(bill.getTotal().getTotal());
      overdueTotalField.setValue(bill.getOverdueTotal().getTotal());
    }
  }

  /** 合计并刷新应收总额、产生的预存款金额以及滞纳金。 */
  public void calculateRightPanel() {
    bill.aggregate(ep.getScale(), ep.getRoundingMode());
    unpayedTotalField.setValue(bill.getUnpayedTotal().getTotal());
    //totalField.setValue(bill.getTotal().getTotal());
    depositTotalField.setValue(bill.getDepositTotal());
    refreshSubjectUCNBoxRequired(bill.getDepositTotal());
    refreshOverdueTotal();
  }

  private void refreshSubjectUCNBoxRequired(BigDecimal depositTotal) {
    if (depositTotal == null || BigDecimal.ZERO.compareTo(depositTotal) == 0) {
      subjectUCNBox.setRequired(false);
    } else {
      subjectUCNBox.setRequired(true);
    }
  }

  /** 刷新滞纳金 */
  public void refreshOverdueTotal() {
    bill.aggregateOverdue(ep.getScale(), ep.getRoundingMode());
    overdueTotalField.setValue(bill.getOverdueTotal().getTotal());
  }

  /** 合计按科目收款时的实收总额以及产生的预存款金额 */
  public void doChangeDefrayalAndDepositTotal() {
    bill.aggregate(ep.getScale(), ep.getRoundingMode());
    depositTotalField.setValue(bill.getDepositTotal());
    refreshSubjectUCNBoxRequired(bill.getDepositTotal());
  }

  private class Handler_change implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == accountUnitUCNBox) {
        doChangeAccountUnitConfirm();
      } else if (event.getSource() == counterpartUCNBox) {
        doCounterPartConfirm();
      } else if (event.getSource() == receiptDateBox) {
        if (receiptDateBox.getValue() != null) {
          bill.setPaymentDate(receiptDateBox.getValue());
          // 重算滞纳金
          BOverdueCalcHelper.calculate(bill, ep.getScale(), ep.getRoundingMode(),
              ep.getOverdueDefault());
          refreshRightPanel();
          RActionEvent.fire(ReceiptGeneralCreateGadget.this,
              ActionName.ACTION_GENERALCREATE_RECEIVEDATECHANGED);
        }
      } else if (event.getSource() == employeeBox) {
        bill.setDealer(employeeBox.getValue());
      } else if (event.getSource() == defrayalTypeField) {
        if (!existValidDefrayal()) {
          doChangeDefrayalType();
        } else {
          RMsgBox.showConfirm(ReceiptMessages.M.paymentChangeConfirm(), true,
              new RMsgBox.ConfirmCallback() {
                @Override
                public void onClosed(boolean confirmed) {
                  if (confirmed) {
                    doChangeDefrayalType();
                  } else {
                    defrayalTypeField.setValue(bill.getDefrayalType());
                  }
                }
              });
        }
      } else if (event.getSource() == subjectUCNBox) {
        bill.setDepositSubject(subjectUCNBox.getValue());
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
      } else if (!CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
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

        for (BPaymentLine line : bill.getCollectionLines()) {
          for (BPaymentLineCash lineCach : line.getCashs()) {
            if (lineCach.getPaymentType() != null && lineCach.getTotal() != null
                && lineCach.getTotal().compareTo(BigDecimal.ZERO) != 0) {
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

    /** 改变收款方式，清空付款信息 */
    private void doChangeDefrayalType() {
      if (!CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
        for (BPaymentLine line : bill.getAccountLines()) {
          line.getCashs().clear();
          line.getDeposits().clear();
          line.setDefrayalTotal(BigDecimal.ZERO);
        }

        for (BPaymentLine line : bill.getCollectionLines()) {
          line.getCashs().clear();
          line.getTotal().setTotal(BigDecimal.ZERO);
          line.setDefrayalTotal(BigDecimal.ZERO);
          line.setDefrayalTotal(BigDecimal.ZERO);
          line.getTotal().setTotal(BigDecimal.ZERO);
        }

        for (BPaymentLine line : bill.getOverdueLines()) {
          line.getCashs().clear();
          line.getDeposits().clear();
          line.setDefrayalTotal(BigDecimal.ZERO);
        }
      }
      bill.getCashs().clear();
      bill.getDeposits().clear();
      bill.setDefrayalTotal(BigDecimal.ZERO);
      bill.aggregate(ep.getScale(), ep.getRoundingMode());
      refreshRightPanel();

      bill.setDefrayalType(defrayalTypeField.getValue());

      RActionEvent.fire(ReceiptGeneralCreateGadget.this,
          ActionName.ACTION_GENERALCREATE_DEFRAYALTYPECHANGED, defrayalTypeField.getValue());
    }

    private void doChangeAccountUnitConfirm() {
      if (accountUnitUCNBox.getValue() == null || accountUnitUCNBox.getValue().getUuid() == null) {
        return;
      }
      if (bill.getAccountUnit() != null
          && !ObjectUtil.isEquals(bill.getAccountUnit().getUuid(), accountUnitUCNBox.getValue()
              .getUuid())
          && (!bill.getAccountLines().isEmpty() || !bill.getOverdueLines().isEmpty() || existValidDefrayal())) {
        RMsgBox.showConfirm(ReceiptMessages.M.changeConfirm(accountUnitUCNBox.getCaption()),
            new ConfirmCallback() {
              public void onClosed(boolean confirmed) {
                if (confirmed) {
                  doChangeAccountUnit(true);
                  RActionEvent.fire(ReceiptGeneralCreateGadget.this,
                      ActionName.ACTION_GENERALCREATE_REFRESH_SUBJECTS_AND_CONTRACTS);
                  RActionEvent.fire(ReceiptGeneralCreateGadget.this,
                      ActionName.ACTION_GENERALCREATE_REFRESH_REMAINTOTAL);
                  RActionEvent.fire(ReceiptGeneralCreateGadget.this,
                      ActionName.ACTION_GENERALCREATE_REFRESH_BANKS);
                } else {
                  accountUnitUCNBox.setValue(bill.getAccountUnit());
                }
              }
            });
      } else {
        doChangeAccountUnit(false);

        RActionEvent.fire(ReceiptGeneralCreateGadget.this,
            ActionName.ACTION_GENERALCREATE_REFRESH_SUBJECTS_AND_CONTRACTS);
        RActionEvent.fire(ReceiptGeneralCreateGadget.this,
            ActionName.ACTION_GENERALCREATE_REFRESH_REMAINTOTAL);
        RActionEvent.fire(ReceiptGeneralCreateGadget.this,
            ActionName.ACTION_GENERALCREATE_REFRESH_BANKS);
      }
    }

    private void doCounterPartConfirm() {
      if (counterpartUCNBox.getValue() == null || counterpartUCNBox.getValue().getUuid() == null) {
        return;
      }
      if (bill.getCounterpart() != null
          && !ObjectUtil.isEquals(bill.getCounterpart().getUuid(), counterpartUCNBox.getValue()
              .getUuid())
          && (!bill.getAccountLines().isEmpty() || !bill.getOverdueLines().isEmpty() || existValidDefrayal())) {
        RMsgBox.showConfirm(ReceiptMessages.M.changeConfirm(counterpartUCNBox.getCaption()),
            new ConfirmCallback() {
              public void onClosed(boolean confirmed) {
                if (confirmed) {
                  doChangeCounterpart(true);

                  RActionEvent.fire(ReceiptGeneralCreateGadget.this,
                      ActionName.ACTION_GENERALCREATE_REFRESH_SUBJECTS_AND_CONTRACTS);
                  RActionEvent.fire(ReceiptGeneralCreateGadget.this,
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

        RActionEvent.fire(ReceiptGeneralCreateGadget.this,
            ActionName.ACTION_GENERALCREATE_REFRESH_SUBJECTS_AND_CONTRACTS);
        RActionEvent.fire(ReceiptGeneralCreateGadget.this,
            ActionName.ACTION_GENERALCREATE_REFRESH_REMAINTOTAL);
      }
    }
  }

  private void doChangeAccountUnit(boolean clear) {
    bill.setAccountUnit(accountUnitUCNBox.getValue());
    reloadDepositRemainTotalAndRefreshShow();
    loadSingleContract();
    if (clear)
      doClearLinesAndDerayals();
  }

  private void doChangeCounterpart(boolean clear) {
    bill.setCounterpart(counterpartUCNBox.getRawValue());
    if (ep.getCounterpartTypeMap().size() > 1)
      countpartTypeField.setValue(counterpartUCNBox.getRawValue() == null ? null : ep
          .getCounterpartTypeMap().get(counterpartUCNBox.getRawValue().getCounterpartType()));

    reloadDepositRemainTotalAndRefreshShow();
    loadSingleContract();
    if (clear)
      doClearLinesAndDerayals();
  }

  private void reloadDepositRemainTotalAndRefreshShow() {
    if (bill.getAccountUnit() == null || bill.getCounterpart() == null) {
      depositRemainTotalField.setValue(0);
      return;
    }

    PaymentCommonsService.Locator.getService().getDepositCounterpartRemainTotal(
        bill.getAccountUnit().getUuid(), bill.getCounterpart().getUuid(),
        new AsyncCallback<BigDecimal>() {
          @Override
          public void onSuccess(BigDecimal result) {
            depositRemainTotalField.setValue(result);
          }

          @Override
          public void onFailure(Throwable caught) {
            // Do Nothing
          }
        });
  }

  private void loadSingleContract() {
    if (bill.getCounterpart() == null || bill.getCounterpart().getUuid() == null
        || bill.getAccountUnit() == null || bill.getAccountUnit().getUuid() == null)
      return;

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
            RActionEvent.fire(ReceiptGeneralCreateGadget.this, ActionName.ACTION_SET_CONTRACT,
                result);
          }
        });
  }

  /** 清空明细行和支付明细等信息 */
  private void doClearLinesAndDerayals() {
    bill.getAccountLines().clear();
    bill.getOverdueLines().clear();
    bill.getCashs().clear();
    bill.getDeposits().clear();
    bill.getTotal().setTax(BigDecimal.ZERO);
    bill.getUnpayedTotal().setTotal(BigDecimal.ZERO);
    bill.getTotal().setTotal(BigDecimal.ZERO);
    bill.setDefrayalTotal(BigDecimal.ZERO);
    bill.getOverdueTotal().setTax(BigDecimal.ZERO);
    bill.getOverdueTotal().setTotal(BigDecimal.ZERO);
    bill.setDepositTotal(BigDecimal.ZERO);

    RActionEvent.fire(ReceiptGeneralCreateGadget.this,
        ActionName.ACTION_GENERALCREATE_COUONTERPARTCHANGED);
  }

  /** 刷新右侧的合计以及产生的预存款金额 */
  public void refreshRightPanel() {
    assert bill != null;
    bill.aggregate(ep.getScale(), ep.getRoundingMode());
    unpayedTotalField.setValue(bill.getUnpayedTotal() == null ? BigDecimal.ZERO : bill
        .getUnpayedTotal().getTotal());
    //totalField.setValue(bill.getTotal() == null ? BigDecimal.ZERO : bill.getTotal().getTotal());
    defrayalTotalField.setValue(bill.getDefrayalTotal() == null ? BigDecimal.ZERO : bill
        .getDefrayalTotal());
    overdueTotalField.setValue(bill.getOverdueTotal() == null ? BigDecimal.ZERO : bill
        .getOverdueTotal().getTotal());
    depositTotalField.setValue(bill.getDepositTotal() == null ? BigDecimal.ZERO : bill
        .getDepositTotal());
    refreshSubjectUCNBoxRequired(bill.getDepositTotal());
  }

  @Override
  public Focusable getFocusable(String field) {
    if (BPayment.FN_COUNTERPART.equals(field))
      return counterpartUCNBox;
    else if (BPayment.FN_PAYMENTDATE.equals(field))
      return receiptDateBox;
    else if (BPayment.FN_DEALER.equals(field))
      return employeeBox;
    else if (BPayment.FN_DEFRAYALTYPE.equals(field))
      return defrayalTypeField;
    else if (BPayment.FN_SUBJECT.equals(field))
      return subjectUCNBox;
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
    if (subjectUCNBox != null)
      subjectUCNBox.clearConditions();
  }

}
