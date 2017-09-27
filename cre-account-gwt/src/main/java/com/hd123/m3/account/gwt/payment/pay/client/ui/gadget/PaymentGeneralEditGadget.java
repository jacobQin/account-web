/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	ReceiveGeneralCreateGadget.java
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BOverdueCalcHelper;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCashDefrayal;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PaymentDefrayalTypeDef;
import com.hd123.m3.account.gwt.payment.pay.client.EPPayment;
import com.hd123.m3.account.gwt.payment.pay.client.PaymentMessages;
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
public class PaymentGeneralEditGadget extends RCaptionBox implements RActionHandler,
    HasRActionHandlers, FocusOnFirstField {

  private EPPayment ep = EPPayment.getInstance();
  private BPayment bill;

  // 基本信息
  private RForm generalForm;
  private RViewStringField billNumberField;
  private RViewStringField accountUnitField;
  private RViewStringField counterpartField;
  private RDateBox paymentDateBox;
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
  private RViewNumberField receiptTotalField;
  private RViewNumberField unpayedTotalField;
  private RViewNumberField totalField;
  private RViewNumberField defrayalTotalField;
  private RViewNumberField overdueTotalField;

  private Handler_change changeHandler = new Handler_change();

  public BPayment getBill() {
    return bill;
  }

  public void setBill(BPayment bill) {
    this.bill = bill;
  }

  public PaymentGeneralEditGadget() {
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

    w = drawOperatePanel();
    mvp.add(1, w);

    permGroupField = new PermGroupEditField(EPPayment.getInstance(EPPayment.class).isPermEnabled(),
        EPPayment.getInstance(EPPayment.class).getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    w = drawTotalPanel();
    mvp.add(1, w);

    setCaption(PaymentMessages.M.generalInfo());
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
    accountUnitField.setCaption(EPPayment.getInstance().getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    generalForm.addField(accountUnitField);

    counterpartField = new RViewStringField(PPaymentDef.counterpart);
    counterpartField.setCaption(EPPayment.getInstance().getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    generalForm.addField(counterpartField);

    paymentDateBox = new RDateBox(PaymentMessages.M.paymentDate());
    paymentDateBox.setCaption(PaymentMessages.M.paymentDate());
    paymentDateBox.setRequired(true);
    paymentDateBox.addChangeHandler(changeHandler);
    generalForm.addField(paymentDateBox);

    employeeBox = new EmployeeUCNBox();
    employeeBox.setCaption(PPaymentDef.constants.dealer());
    employeeBox.addChangeHandler(changeHandler);
    generalForm.addField(employeeBox);

    defrayalTypeField = new RViewStringField(PaymentMessages.M.paymentType());
    defrayalTypeField.setVisible(false);
    generalForm.addField(defrayalTypeField);

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
    box.setCaption(PaymentMessages.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  private Widget drawTotalPanel() {
    RForm form = new RForm(1);
    form.setWidth("50%");

    receiptTotalField = new RViewNumberField(PPaymentDef.receiptTotal_total);
    receiptTotalField.setWidth("50%");
    receiptTotalField.setFormat(GWTFormat.fmt_money);
    receiptTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    //form.addField(receiptTotalField);

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

    refreshGeneral();
    refreshAuthorize();
    refreshOperateInfo();
    refreshTotal();
  }

  private void refreshGeneral() {
    billNumberField.setValue(bill.getBillNumber());
    accountUnitField.setValue(bill.getAccountUnit() == null ? null : bill.getAccountUnit()
        .toFriendlyStr());
    counterpartField.setValue(bill.getCounterpart() == null ? null : bill.getCounterpart()
        .toFriendlyStr(ep.getCounterpartTypeMap()));
    paymentDateBox.setValue(bill.getPaymentDate());
    incomeDateBox.setValue(bill.getIncomeDate());
    employeeBox.setValue(bill.getDealer());
    defrayalTypeField
        .setValue(CPaymentDefrayalType.bill.equals(bill.getDefrayalType()) ? PaymentDefrayalTypeDef.constants
            .bill() : PaymentDefrayalTypeDef.constants.line());
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

    receiptTotalField.setValue(bill.getReceiptTotal() == null ? BigDecimal.ZERO : bill
        .getReceiptTotal().getTotal());
    unpayedTotalField.setValue(bill.getUnpayedTotal() == null ? BigDecimal.ZERO : bill
        .getUnpayedTotal().getTotal());
    totalField.setValue(bill.getTotal() == null ? BigDecimal.ZERO : bill.getTotal().getTotal());
    defrayalTotalField.setValue(bill.getDefrayalTotal() == null ? BigDecimal.ZERO : bill
        .getDefrayalTotal());
    overdueTotalField.setValue(bill.getOverdueTotal() == null ? BigDecimal.ZERO : bill
        .getOverdueTotal().getTotal());
    
    refreshtTotalField();
  }

  @Override
  public void clearValidResults() {
    super.clearValidResults();
    generalForm.clearValidResults();
    permGroupField.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return generalForm.isValid() & permGroupField.isValid();
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
    return generalForm.validate() & permGroupField.validate();
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
    
    BigDecimal defrayalTotal = BigDecimal.ZERO;
    for(BPaymentCashDefrayal cash:bill.getCashs()){
      defrayalTotal = defrayalTotal.add(cash.getTotal());
    }
    
    bill.setDefrayalTotal(defrayalTotal);
    
    totalField.setValue(bill.getTotal().getTotal());
    defrayalTotalField.setValue(bill.getDefrayalTotal());
  }

  /** 合计并刷新应收总额、产生的预存款金额以及滞纳金。 */
  public void calculateRightPanel() {
    //bill.aggregate(ep.getScale(), ep.getRoundingMode());
    
    unpayedTotalField.setValue(bill.getUnpayedTotal().getTotal());
    totalField.setValue(bill.getTotal().getTotal());
    defrayalTotalField.setValue(bill.getDefrayalTotal());
    
    refreshtTotalField();

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
      if (event.getSource() == paymentDateBox) {
        if (paymentDateBox.getValue() != null) {
          bill.setPaymentDate(paymentDateBox.getValue());
          // 重算滞纳金
          BOverdueCalcHelper.calculate(bill, ep.getScale(), ep.getRoundingMode(), null);
          refreshRightPanel();
          RActionEvent.fire(PaymentGeneralEditGadget.this,
              ActionName.ACTION_GENERALEDIT_RECEIVEDATECHANGED);
        }
      } else if (event.getSource() == employeeBox) {
        bill.setDealer(employeeBox.getValue());
      } else if (event.getSource() == incomeDateBox) {
        bill.setIncomeDate(incomeDateBox.getValue());
      }
    }
  }

  /** 刷新右侧的合计以及产生的预存款金额 */
  public void refreshRightPanel() {
    receiptTotalField.setValue(bill.getReceiptTotal() == null ? BigDecimal.ZERO : bill
        .getReceiptTotal().getTotal());
    unpayedTotalField.setValue(bill.getUnpayedTotal() == null ? BigDecimal.ZERO : bill
        .getUnpayedTotal().getTotal());
    totalField.setValue(bill.getTotal().getTotal());
    defrayalTotalField.setValue(bill.getDefrayalTotal());
    overdueTotalField.setValue(bill.getOverdueTotal().getTotal());
  }

  @Override
  public boolean focusOnFirstField() {
    return FocusOnFirstFieldUtil.focusOnFirstField(generalForm);
  }

  public void clearQueryConditions() {
    if (employeeBox != null)
      employeeBox.clearConditions();
  }

}
