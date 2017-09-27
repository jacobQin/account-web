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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.pay.client.EPPayment;
import com.hd123.m3.account.gwt.payment.pay.client.PaymentMessages;
import com.hd123.m3.account.gwt.payment.pay.client.ui.page.PaymentLogViewPage;
import com.hd123.m3.account.gwt.payment.pay.intf.client.PaymentUrlParams;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupViewField;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RViewDateField;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.misc.RHyperlink;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 基本信息新建控件
 * 
 * @author subinzhu
 * 
 */
public class PaymentGeneralViewGadget extends RCaptionBox {

  private EPPayment ep = EPPayment.getInstance();
  private BPayment bill;

  // 基本信息
  private RForm generalForm;
  private RViewStringField billNumberField;
  private RViewStringField counterpartField;
  private RViewStringField accountUnitField;
  private RViewDateField paymentDateField;
  private RViewStringField employeeField;
  private RViewStringField defrayalTypeField;
  private RViewDateField incomeDateField;

  // 状态与操作
  private RForm operateForm;
  private RViewStringField stateField;
  private RViewStringField causeField;
  private RSimpleOperateInfoField createInfo;
  private RSimpleOperateInfoField lastModifyInfo;
  private RHyperlink moreInfo;

  private PermGroupViewField permGroupField;

  // 合计
  private RForm totalForm;
  private RViewNumberField receiptTotalField;
  private RViewNumberField unpayedTotalField;
  private RViewNumberField totalField;
  private RViewNumberField defrayalTotalField;
  private RViewNumberField overdueTotalField;

  public void setBill(BPayment bill) {
    this.bill = bill;
  }

  public PaymentGeneralViewGadget() {
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

    permGroupField = new PermGroupViewField();
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    w = drawTotalPanel();
    mvp.add(1, w);

    setCaption(PaymentMessages.M.generalInfo());
    setWidth("100%");
    setContent(vp);
  }

  private Widget drawGeneralPanel() {
    generalForm = new RForm(1);
    generalForm.setWidth("100%");

    billNumberField = new RViewStringField(PPaymentDef.billNumber);
    billNumberField.addStyleName(RTextStyles.STYLE_BOLD);
    generalForm.addField(billNumberField);

    counterpartField = new RViewStringField(PPaymentDef.counterpart);
    counterpartField.setCaption(EPPayment.getInstance().getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    generalForm.addField(counterpartField);

    accountUnitField = new RViewStringField(PPaymentDef.accountUnit);
    accountUnitField.setCaption(EPPayment.getInstance().getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    generalForm.addField(accountUnitField);

    paymentDateField = new RViewDateField(PaymentMessages.M.paymentDate());
    generalForm.addField(paymentDateField);

    employeeField = new RViewStringField(PPaymentDef.dealer);
    generalForm.addField(employeeField);

    defrayalTypeField = new RViewStringField(PaymentMessages.M.paymentType());
    defrayalTypeField.setVisible(false);
    generalForm.addField(defrayalTypeField);

    incomeDateField = new RViewDateField(PPaymentDef.incomeDate);
    generalForm.addField(incomeDateField);

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

    moreInfo = new RHyperlink();
    moreInfo.addClickHandler(new Handler_click());
    moreInfo.setHTML(PaymentMessages.M.moreInfo());
    operateForm.addField(moreInfo);

    operateForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(PaymentMessages.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  private Widget drawTotalPanel() {
    totalForm = new RForm(1);
    totalForm.setWidth("50%");

    receiptTotalField = new RViewNumberField(PPaymentDef.receiptTotal_total);
    receiptTotalField.setWidth("50%");
    receiptTotalField.setFormat(GWTFormat.fmt_money);
    receiptTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    //totalForm.addField(receiptTotalField);

    unpayedTotalField = new RViewNumberField(PPaymentDef.unpayedTotal_total);
    unpayedTotalField.setWidth("50%");
    unpayedTotalField.setFormat(GWTFormat.fmt_money);
    unpayedTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    totalForm.addField(unpayedTotalField);

    totalField = new RViewNumberField(PPaymentDef.total_total);
    totalField.setWidth("50%");
    totalField.setFormat(GWTFormat.fmt_money);
    totalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    totalForm.addField(totalField);

    defrayalTotalField = new RViewNumberField(PPaymentDef.defrayalTotal);
    defrayalTotalField.setWidth("50%");
    defrayalTotalField.setFormat(GWTFormat.fmt_money);
    defrayalTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    totalForm.add(defrayalTotalField);

    overdueTotalField = new RViewNumberField(PPaymentDef.overdueTotal);
    overdueTotalField.setWidth("50%");
    overdueTotalField.setFormat(GWTFormat.fmt_money);
    overdueTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    totalForm.addField(overdueTotalField);

    totalForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setContent(totalForm);
    box.setCaption(PaymentMessages.M.accountTotal());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    return box;
  }

  public void refresh() {
    assert bill != null;

    refreshGeneral();
    refreshOperateInfo();
    refreshAuthorize();
    refreshTotal();
  }

  private void refreshGeneral() {
    billNumberField.setValue(bill.getBillNumber());
    accountUnitField.setValue(bill.getAccountUnit() == null ? null : bill.getAccountUnit()
        .toFriendlyStr());
    counterpartField.setValue(bill.getCounterpart() == null ? null : bill.getCounterpart()
        .toFriendlyStr(ep.getCounterpartTypeMap()));
    paymentDateField.setValue(bill.getPaymentDate());
    employeeField.setValue(bill.getDealer() == null ? null : bill.getDealer().toFriendlyStr());
    defrayalTypeField
        .setValue(CPaymentDefrayalType.bill.equals(bill.getDefrayalType()) ? PaymentMessages.M
            .billPay() : PaymentMessages.M.linePay());
    incomeDateField.setValue(bill.getIncomeDate());
  }

  private void refreshOperateInfo() {
    stateField.setValue(PPaymentDef.finishBizState.getEnumCaption(bill.getBizState()));
    causeField.setValue(bill.getBpmMessage());
    causeField.setVisible(!StringUtil.isNullOrBlank(bill.getBpmMessage()));
    createInfo.setOperateInfo(bill.getCreateInfo());
    lastModifyInfo.setOperateInfo(bill.getLastModifyInfo());

    operateForm.rebuild();
  }

  private void refreshAuthorize() {
    permGroupField.refresh(EPPayment.getInstance().isPermEnabled(), bill);
  }

  private void refreshTotal() {
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
  public void clearValidResults() {
    super.clearValidResults();
    generalForm.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return generalForm.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> messages = new ArrayList<Message>();
    messages.addAll(generalForm.getInvalidMessages());
    return messages;
  }

  public boolean validate() {
    clearValidResults();
    return generalForm.validate();
  }

  private class Handler_click implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      JumpParameters params = new JumpParameters(PaymentLogViewPage.START_NODE);
      params.getUrlRef().set(PaymentUrlParams.Log.PN_UUID, bill.getUuid());
      EPPayment.getInstance().jump(params);
    }
  }

}
