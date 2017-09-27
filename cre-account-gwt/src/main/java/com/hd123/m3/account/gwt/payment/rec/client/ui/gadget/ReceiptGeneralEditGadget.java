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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BOverdueCalcHelper;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.rpc.PaymentCommonsService;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PaymentDefrayalTypeDef;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.account.gwt.payment.rec.client.biz.ActionName;
import com.hd123.m3.account.gwt.util.client.ObjectUtil;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstField;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstFieldUtil;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
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
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * 基本信息新建控件
 * 
 * @author subinzhu
 * 
 */
public class ReceiptGeneralEditGadget extends RCaptionBox implements RActionHandler,
    HasRActionHandlers, FocusOnFirstField {

  private EPReceipt ep = EPReceipt.getInstance();
  private BPayment bill;

  // 基本信息
  private RForm generalForm;
  private RViewStringField billNumberField;
  private RViewStringField accountUnitField;
  private RViewStringField counterpartField;
  private RDateBox receiptDateBox;
  private EmployeeUCNBox employeeBox;
  private RViewStringField defrayalTypeField;
  private RDateBox incomeDateBox;

  // 状态与操作
  private RForm operateForm;
  private RViewStringField stateField;
  private RViewStringField causeField;
  private RSimpleOperateInfoField createInfo;
  private RSimpleOperateInfoField lastModifyInfo;

  private PermGroupEditField permGroupField;

  // 合计
  private RViewNumberField unpayedTotalField;
  //private RViewNumberField totalField;
  private RViewNumberField defrayalTotalField;
  private RViewNumberField overdueTotalField;
  private RViewNumberField depositRemainTotalField;

  // 产生预存款
  private RForm depositForm;
  private RViewNumberField depositTotalField;
  private SubjectUCNBox subjectUCNBox;

  private Handler_change changeHandler = new Handler_change();

  public BPayment getBill() {
    return bill;
  }

  public void setBill(BPayment bill) {
    this.bill = bill;
    loadDepositRemainTotalAndRefreshShow();
  }

  public ReceiptGeneralEditGadget() {
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
    mvp.add(0, drawDepositPanel());
    mvp.add(1, drawOperatePanel());

    permGroupField = new PermGroupEditField(EPReceipt.getInstance(EPReceipt.class).isPermEnabled(),
        EPReceipt.getInstance(EPReceipt.class).getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    mvp.add(1, drawTotalPanel());

    setCaption(ReceiptMessages.M.generalInfo());
    setWidth("100%");
    setContent(vp);
    setEditing(true);
  }

  private Widget drawGeneralPanel() {
    generalForm = new RForm(1);
    generalForm.setWidth("100%");

    billNumberField = new RViewStringField(PPaymentDef.billNumber);
    billNumberField.addStyleName(RTextStyles.STYLE_BOLD);
    generalForm.addField(billNumberField);

    accountUnitField = new RViewStringField(PPaymentDef.accountUnit);
    accountUnitField.setCaption(EPReceipt.getInstance().getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    generalForm.addField(accountUnitField);

    counterpartField = new RViewStringField(PPaymentDef.counterpart);
    counterpartField.setCaption(EPReceipt.getInstance().getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    generalForm.addField(counterpartField);

    receiptDateBox = new RDateBox(PPaymentDef.paymentDate);
    receiptDateBox.setRequired(true);
    receiptDateBox.addChangeHandler(changeHandler);
    generalForm.addField(receiptDateBox);

    employeeBox = new EmployeeUCNBox();
    employeeBox.setCaption(PPaymentDef.constants.dealer());
    employeeBox.addChangeHandler(changeHandler);
    generalForm.addField(employeeBox);

    defrayalTypeField = new RViewStringField(PPaymentDef.defrayalType);
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

  private Widget drawOperatePanel() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    stateField = new RViewStringField(PPaymentDef.finishBizState);
    stateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(stateField);

    causeField = new RViewStringField(PPaymentDef.constants.latestComment());
    operateForm.addField(causeField);

    createInfo = new RSimpleOperateInfoField(PPaymentDef.constants.createInfo());
    operateForm.addField(createInfo);

    lastModifyInfo = new RSimpleOperateInfoField(PPaymentDef.constants.lastModifyInfo());
    operateForm.addField(lastModifyInfo);

    operateForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(ReceiptMessages.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
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

    refreshGeneral();
    refreshAuthorize();
    refreshOperateInfo();
    refreshTotal();
    refreshDeposit();
  }

  private void refreshGeneral() {
    billNumberField.setValue(bill.getBillNumber());
    accountUnitField.setValue(bill.getAccountUnit() == null ? null : bill.getAccountUnit()
        .toFriendlyStr());
    counterpartField.setValue(bill.getCounterpart() == null ? null : bill.getCounterpart()
        .toFriendlyStr(ep.getCounterpartTypeMap()));
    receiptDateBox.setValue(bill.getPaymentDate());
    employeeBox.setValue(bill.getDealer());
    defrayalTypeField
        .setValue(CPaymentDefrayalType.bill.equals(bill.getDefrayalType()) ? PaymentDefrayalTypeDef.constants
            .bill()
            : (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType()) ? PaymentDefrayalTypeDef.constants
                .lineSingle() : PaymentDefrayalTypeDef.constants.line()));
    incomeDateBox.setValue(bill.getIncomeDate());
  }

  private void refreshAuthorize() {
    permGroupField.setPerm(bill);
  }

  private void refreshOperateInfo() {
    stateField.setValue(PPaymentDef.finishBizState.getEnumCaption(bill.getBizState()));
    causeField.setValue(bill.getBpmMessage());
    causeField.setVisible(!StringUtil.isNullOrBlank(bill.getBpmMessage()));
    createInfo.setOperateInfo(bill.getCreateInfo());
    lastModifyInfo.setOperateInfo(bill.getLastModifyInfo());

    operateForm.rebuild();
  }

  private void refreshTotal() {
    assert bill != null;

    unpayedTotalField.setValue(bill.getUnpayedTotal() == null ? BigDecimal.ZERO : bill
        .getUnpayedTotal().getTotal());
    //totalField.setValue(bill.getTotal() == null ? BigDecimal.ZERO : bill.getTotal().getTotal());
    defrayalTotalField.setValue(bill.getDefrayalTotal() == null ? BigDecimal.ZERO : bill
        .getDefrayalTotal());
    overdueTotalField.setValue(bill.getOverdueTotal() == null ? BigDecimal.ZERO : bill
        .getOverdueTotal().getTotal());
  }

  private void refreshDeposit() {
    depositTotalField.setValue(bill.getDepositTotal() == null ? BigDecimal.ZERO : bill
        .getDepositTotal());
    subjectUCNBox.setValue(bill.getDepositSubject());
  }

  @Override
  public void clearValidResults() {
    super.clearValidResults();
    generalForm.clearValidResults();
    permGroupField.clearValidResults();
    depositForm.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return generalForm.isValid() & permGroupField.isValid() & validateDepositSubject()
        & depositForm.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> messages = new ArrayList<Message>();
    messages.addAll(generalForm.getInvalidMessages());
    messages.addAll(permGroupField.getInvalidMessages());
    messages.addAll(depositForm.getInvalidMessages());
    if (bill.getDepositTotal().compareTo(BigDecimal.ZERO) > 0
        && (bill.getDepositSubject() == null || StringUtil.isNullOrBlank(bill.getDepositSubject()
            .getUuid()))) {
      subjectUCNBox.addErrorMessage(ReceiptMessages.M.notNull(PPaymentDef.constants
          .depositSubject()));
      messages.addAll(subjectUCNBox.getInvalidMessages());
    }
    return messages;
  }

  public boolean validate() {
    clearValidResults();
    return generalForm.validate() & permGroupField.validate() & validateDepositSubject()
        & depositForm.validate();
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
    } else if (ObjectUtil.isEquals(event.getActionName(),
        ActionName.ACTION_LINEACCOUNTLINE_TOTALCHANGE)) {
      calculateRightPanel();
    } else if (ObjectUtil.isEquals(event.getActionName(),
        ActionName.ACTION_BILLOVERDUELINE_UNPAYEDTOTALCHANGE)) {
      bill.aggregateOverdue(ep.getScale(), ep.getRoundingMode());
      overdueTotalField.setValue(bill.getOverdueTotal().getTotal());
    } else if (ObjectUtil.isEquals(event.getActionName(),
        ActionName.ACTION_LINEACCOUNTLINE_CASHDEFRAYALCHANGE)
        || ObjectUtil.isEquals(event.getActionName(),
            ActionName.ACTION_LINEACCOUNTLINE_DEPOSITDEFRAYALCHANGE)
        || ObjectUtil.isEquals(event.getActionName(),
            ActionName.ACTION_LINEOVERDUELINE_CASHDEFRAYALCHANGE)
        || ObjectUtil.isEquals(event.getActionName(),
            ActionName.ACTION_LINEOVERDUELINE_DEPOSITDEFRAYALCHANGE)) {
      if (!CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
        doChangeDefrayalAndDepositTotal();
      }
    } else if (ObjectUtil.isEquals(event.getActionName(),
        ActionName.ACTION_REFRESH_AFFTER_APPORTION)) {
     // totalField.setValue(bill.getTotal().getTotal());
      overdueTotalField.setValue(bill.getOverdueTotal().getTotal());
    }
  }

  /** 合计并刷新应收总额、产生的预存款金额以及滞纳金。 */
  public void calculateRightPanel() {
    bill.aggregate(ep.getScale(), ep.getRoundingMode());
    unpayedTotalField.setValue(bill.getUnpayedTotal().getTotal());
    //totalField.setValue(bill.getTotal().getTotal());
    depositTotalField.setValue(bill.getDepositTotal());

    refreshOverdueTotal();
  }

  /** 刷新滞纳金 */
  public void refreshOverdueTotal() {
    bill.aggregateOverdue(ep.getScale(), ep.getRoundingMode());
    overdueTotalField.setValue(bill.getOverdueTotal().getTotal());
  }

  /** 合计按科目收款时的实收总额以及产生的预存款金额 */
  public void doChangeDefrayalAndDepositTotal() {
    bill.aggregate(ep.getScale(), ep.getRoundingMode());
    defrayalTotalField.setValue(bill.getDefrayalTotal());
    depositTotalField.setValue(bill.getDepositTotal());
  }

  private void loadDepositRemainTotalAndRefreshShow(){
    if(bill.getAccountUnit() == null || bill.getCounterpart() ==null){
      depositRemainTotalField.setValue(0);
      return;
    }
    
    PaymentCommonsService.Locator.getService().getDepositCounterpartRemainTotal(bill.getAccountUnit().getUuid(), bill.getCounterpart().getUuid(), new AsyncCallback<BigDecimal>() {
      @Override
      public void onSuccess(BigDecimal result) {
        depositRemainTotalField.setValue(result);
      }
      
      @Override
      public void onFailure(Throwable caught) {
       //Do Nothing
      }
    });
  }

  
  private class Handler_change implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == receiptDateBox) {
        if (receiptDateBox.getValue() != null) {
          bill.setPaymentDate(receiptDateBox.getValue());
          // 重算滞纳金
          BOverdueCalcHelper.calculate(bill, ep.getScale(), ep.getRoundingMode(),
              ep.getOverdueDefault());
          refreshRightPanel();
          RActionEvent.fire(ReceiptGeneralEditGadget.this,
              ActionName.ACTION_GENERALEDIT_RECEIVEDATECHANGED);
        }
      } else if (event.getSource() == employeeBox) {
        bill.setDealer(employeeBox.getValue());
      } else if (event.getSource() == subjectUCNBox) {
        bill.setDepositSubject(subjectUCNBox.getValue());
      } else if (event.getSource() == incomeDateBox) {
        bill.setIncomeDate(incomeDateBox.getValue());
      }
    }
  }

  /** 刷新右侧的合计以及产生的预存款金额 */
  public void refreshRightPanel() {
    bill.aggregate(ep.getScale(), ep.getRoundingMode());
    unpayedTotalField.setValue(bill.getUnpayedTotal() == null ? BigDecimal.ZERO : bill
        .getUnpayedTotal().getTotal());
    defrayalTotalField.setValue(bill.getDefrayalTotal() == null ? BigDecimal.ZERO : bill
        .getDefrayalTotal());
    overdueTotalField.setValue(bill.getOverdueTotal() == null ? BigDecimal.ZERO : bill
        .getOverdueTotal().getTotal());
    depositTotalField.setValue(bill.getDepositTotal() == null ? BigDecimal.ZERO : bill
        .getDepositTotal());
    refreshSubjectUCNBoxRequired(bill.getDepositTotal());
  }

  private void refreshSubjectUCNBoxRequired(BigDecimal depositTotal) {
    if (depositTotal == null || BigDecimal.ZERO.compareTo(depositTotal) == 0) {
      subjectUCNBox.setRequired(false);
    } else {
      subjectUCNBox.setRequired(true);
    }
  }

  @Override
  public boolean focusOnFirstField() {
    return FocusOnFirstFieldUtil.focusOnFirstField(generalForm);
  }

  public void clearQueryConditions() {
    if (employeeBox != null)
      employeeBox.clearConditions();
    if (subjectUCNBox != null)
      subjectUCNBox.clearConditions();
  }

}
