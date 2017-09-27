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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.rpc.PaymentCommonsService;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PaymentDefrayalTypeDef;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.account.gwt.payment.rec.client.ui.page.ReceiptLogViewPage;
import com.hd123.m3.account.gwt.payment.rec.intf.client.ReceiptUrlParams;
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
public class ReceiptGeneralViewGadget extends RCaptionBox {

  private BPayment bill;
  private EPReceipt ep = EPReceipt.getInstance();
  // 基本信息
  private RForm generalForm;
  private RViewStringField billNumberField;
  private RViewStringField counterpartField;
  private RViewStringField accountUnitField;
  private RViewDateField receiptDateField;
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
  private RViewNumberField unpayedTotalField;
  //private RViewNumberField totalField;
  private RViewNumberField defrayalTotalField;
  private RViewNumberField overdueTotalField;
  private RViewNumberField depositRemainTotalField;

  // 产生预付款
  private RForm depositForm;
  private RViewNumberField depositTotalField;
  private RViewStringField subjectField;

  public void setBill(BPayment bill) {
    this.bill = bill;
    loadDepositRemainTotalAndRefreshShow();
  }

  public ReceiptGeneralViewGadget() {
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

    permGroupField = new PermGroupViewField();
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    mvp.add(1, drawTotalPanel());

    setCaption(ReceiptMessages.M.generalInfo());
    setWidth("100%");
    setContent(vp);
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

    receiptDateField = new RViewDateField(PPaymentDef.paymentDate);
    generalForm.addField(receiptDateField);

    employeeField = new RViewStringField(PPaymentDef.dealer);
    generalForm.addField(employeeField);

    defrayalTypeField = new RViewStringField(PPaymentDef.defrayalType);
    generalForm.addField(defrayalTypeField);

    incomeDateField = new RViewDateField(PPaymentDef.incomeDate);
    generalForm.add(incomeDateField);

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

    moreInfo = new RHyperlink();
    moreInfo.addClickHandler(new Handler_click());
    moreInfo.setHTML(ReceiptMessages.M.moreInfo());
    operateForm.addField(moreInfo);

    operateForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(ReceiptMessages.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  private Widget drawTotalPanel() {
    totalForm = new RForm(1);
    totalForm.setWidth("50%");

    unpayedTotalField = new RViewNumberField(PPaymentDef.unpayedTotal_total);
    unpayedTotalField.setWidth("50%");
    unpayedTotalField.setFormat(GWTFormat.fmt_money);
    unpayedTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    totalForm.addField(unpayedTotalField);
    
    /*totalField = new RViewNumberField(PPaymentDef.total_total);
    totalField.setWidth("50%");
    totalField.setFormat(GWTFormat.fmt_money);
    totalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    totalForm.addField(totalField);*/

    defrayalTotalField = new RViewNumberField(PPaymentDef.defrayalTotal);
    defrayalTotalField.setWidth("50%");
    defrayalTotalField.setFormat(GWTFormat.fmt_money);
    defrayalTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    totalForm.addField(defrayalTotalField);

    overdueTotalField = new RViewNumberField();
    overdueTotalField.setWidth("50%");
    overdueTotalField.setCaption(PPaymentDef.constants.overdueTotal());
    overdueTotalField.setFormat(GWTFormat.fmt_money);
    overdueTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    totalForm.addField(overdueTotalField);
    
    depositRemainTotalField = new RViewNumberField();
    depositRemainTotalField.setWidth("50%");
    depositRemainTotalField.setCaption(PPaymentDef.constants.depositRemainTotal());
    depositRemainTotalField.setFormat(GWTFormat.fmt_money);
    depositRemainTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    totalForm.addField(depositRemainTotalField);

    totalForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setContent(totalForm);
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

    subjectField = new RViewStringField(PPaymentDef.depositSubject);
    depositForm.addField(subjectField);

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
    refreshOperateInfo();
    refreshAuthorize();
    refreshTotal();
    refreshDeposit();
  }

  private void refreshGeneral() {
    billNumberField.setValue(bill.getBillNumber());
    accountUnitField.setValue(bill.getAccountUnit() == null ? null : bill.getAccountUnit()
        .toFriendlyStr());
    counterpartField.setValue(bill.getCounterpart() == null ? null : bill.getCounterpart()
        .toFriendlyStr(ep.getCounterpartTypeMap()));
    receiptDateField.setValue(bill.getPaymentDate());
    employeeField.setValue(bill.getDealer() == null ? null : bill.getDealer().toFriendlyStr());
    defrayalTypeField
        .setValue(CPaymentDefrayalType.bill.equals(bill.getDefrayalType()) ? PaymentDefrayalTypeDef.constants
            .bill() :(CPaymentDefrayalType.line.equals(bill.getDefrayalType())? PaymentDefrayalTypeDef.constants.line(): PaymentDefrayalTypeDef.constants.lineSingle()));
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
    permGroupField.refresh(EPReceipt.getInstance().isPermEnabled(), bill);
  }

  private void refreshTotal() {
    unpayedTotalField.setValue(bill.getUnpayedTotal() == null ? BigDecimal.ZERO : bill
        .getUnpayedTotal().getTotal());
    //totalField.setValue(bill.getTotal() == null ? BigDecimal.ZERO : bill.getTotal().getTotal());
    defrayalTotalField.setValue(bill.getDefrayalTotal() == null ? BigDecimal.ZERO : bill
        .getDefrayalTotal());
    overdueTotalField.setValue(bill.getOverdueTotal() == null ? BigDecimal.ZERO : bill
        .getOverdueTotal().getTotal());
  }

  private void refreshDeposit() {
    depositTotalField.setValue(bill.getDepositTotal());
    subjectField.setValue(bill.getDepositSubject() == null ? null : bill.getDepositSubject()
        .toFriendlyStr());
  }

  @Override
  public void clearValidResults() {
    super.clearValidResults();
    generalForm.clearValidResults();
    depositForm.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return generalForm.isValid() & depositForm.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> messages = new ArrayList<Message>();
    messages.addAll(generalForm.getInvalidMessages());
    messages.addAll(depositForm.getInvalidMessages());
    return messages;
  }

  public boolean validate() {
    clearValidResults();
    return generalForm.validate() & depositForm.validate();
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
  private class Handler_click implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      JumpParameters params = new JumpParameters(ReceiptLogViewPage.START_NODE);
      params.getUrlRef().set(ReceiptUrlParams.Log.PN_UUID, bill.getUuid());
      EPReceipt.getInstance().jump(params);
    }
  }

}
