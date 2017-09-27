/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegInvoiceEditGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.payment.client.ui.gadget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.invoice.commons.client.actionname.InvoiceRegActionName;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegInvoice;
import com.hd123.m3.account.gwt.invoice.intf.client.InvoiceRegMessage;
import com.hd123.m3.account.gwt.invoice.intf.client.dd.PInvoiceRegInvoiceDef;
import com.hd123.m3.account.gwt.invoice.payment.client.EPPayInvoiceReg;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.m3.commons.gwt.widget.client.ui.TaxRateComboBox;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedFlowField;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.hotkey.RKey;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * @author chenpeisi
 * 
 */
public class PayInvoiceRegInvoiceEditGadget extends Composite implements HasRActionHandlers,
    RActionHandler, HasFocusables {

  public PayInvoiceRegInvoiceEditGadget() {
    drawSelf();
  }

  public static final BigDecimal MAX_TOTAL = BoundaryValue.BIGDECIMAL_MAXVALUE_S2;
  private EPPayInvoiceReg ep = EPPayInvoiceReg.getInstance();
  private BInvoiceReg entity;
  private BInvoiceRegInvoice invoice;

  private RCaptionBox box;
  private RAction prevAction;
  private RAction nextAction;
  private RAction deleteAction;

  private RCaptionBox leftBox;
  private RForm leftForm;
  private RComboBox<String> invoiceTypeField;
  private RTextBox invoiceNumberField;
  private RDateBox invoiceDateField;
  private RTextBox remarkField;

  private RCaptionBox rightBox;
  private RForm rightForm;
  private TaxRateComboBox taxRateField;
  private RNumberBox totalField;
  private RNumberBox amountField;
  private RNumberBox taxField;

  private Element element;
  private Handler_textField handler = new Handler_textField();

  public void setEntity(BInvoiceReg entity) {
    this.entity = entity;
    initInvoiceType();
  }

  /** 初始化发票类型 */
  private void initInvoiceType() {
    Map<String, String> billtypeMap = EPPayInvoiceReg.getInstance().getInvoiceTypeMap();

    invoiceTypeField.clearOptions();
    for (String key : billtypeMap.keySet()) {
      invoiceTypeField.addOption(key, billtypeMap.get(key));
    }
  }

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    RCombinedFlowField editor = new RCombinedFlowField() {
      {
        addField(drawLeftPanel());
        addHorizontalSpacing(10);
        addField(drawRightPanel());
      }
    };
    vp.add(editor);

    box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(InvoiceRegMessage.M.lineNumber(0));
    box.setWidth("100%");
    box.setContent(vp);

    box.getCaptionBar().addButton(new RToolbarSeparator());

    prevAction = new RAction(RActionFacade.PREVIOUS_LINE, new Handler_prevAction());
    prevAction.setHotKey(null);
    RToolbarButton prevButton = new RToolbarButton(prevAction);
    prevButton.setShowText(false);
    box.getCaptionBar().addButton(prevButton);

    nextAction = new RAction(RActionFacade.NEXT_LINE, new Handler_nextAction());
    nextAction.setHotKey(null);
    RToolbarButton nextButton = new RToolbarButton(nextAction);
    nextButton.setShowText(false);
    box.getCaptionBar().addButton(nextButton);

    deleteAction = new RAction(RActionFacade.DELETE, new Handler_deleteAction());
    deleteAction.setHotKey(null);
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    box.getCaptionBar().addButton(deleteButton);

    initWidget(box);
  }

  private Widget drawLeftPanel() {
    leftForm = new RForm(1);
    leftForm.setWidth("100%");

    invoiceTypeField = new RComboBox<String>(PInvoiceRegInvoiceDef.invoiceType);
    invoiceTypeField.setEditable(false);
    invoiceTypeField.setRequired(false);
    invoiceTypeField.removeNullOption();
    invoiceTypeField.addChangeHandler(handler);
    leftForm.addField(invoiceTypeField);

    invoiceNumberField = new RTextBox(PInvoiceRegInvoiceDef.invoiceNumber);
    invoiceNumberField.addChangeHandler(handler);
    leftForm.addField(invoiceNumberField);

    invoiceDateField = new RDateBox(PInvoiceRegInvoiceDef.invoiceDate);
    invoiceDateField.addChangeHandler(handler);
    leftForm.addField(invoiceDateField);

    remarkField = new RTextBox(PInvoiceRegInvoiceDef.remark);
    remarkField.addChangeHandler(handler);
    leftForm.addField(remarkField);

    leftForm.rebuild();

    leftBox = new RCaptionBox();
    leftBox.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    leftBox.setCaption(InvoiceRegMessage.M.invoice());
    leftBox.setWidth("49%");
    leftBox.setContent(leftForm);

    return leftBox;
  }

  private Widget drawRightPanel() {
    rightForm = new RForm(1);
    rightForm.setWidth("100%");

    taxRateField = new TaxRateComboBox(PInvoiceRegInvoiceDef.constants.taxRate());
    taxRateField.setMaxDropdownRowCount(10);
    taxRateField.addChangeHandler(handler);
    taxRateField.refreshOptions();
    rightForm.add(taxRateField);

    totalField = new RNumberBox(PInvoiceRegInvoiceDef.constants.total_total());
    totalField.setTextAlignment(RTextBox.ALIGN_RIGHT);
    totalField.setFormat(GWTFormat.fmt_money);
    totalField.setMaxValue(new Double(MAX_TOTAL.doubleValue()), false);
    totalField.setSelectAllOnFocus(true);
    totalField.addChangeHandler(handler);
    rightForm.addField(new RCombinedField() {
      {
        setCaption(PInvoiceRegInvoiceDef.constants.total_total());
        setRequired(true);
        addField(totalField, 0.5f);
        addField(new HTML(), 0.5f);
      }
    });

    amountField = new RNumberBox();
    amountField.setTextAlignment(RTextBox.ALIGN_RIGHT);
    amountField.setFormat(GWTFormat.fmt_money);
    amountField.setMaxValue(new Double(MAX_TOTAL.doubleValue()));
    amountField.setSelectAllOnFocus(true);
    amountField.addChangeHandler(handler);
    taxField = new RNumberBox();
    taxField.setEnterToTab(false);
    taxField.setTextAlignment(RTextBox.ALIGN_RIGHT);
    taxField.setFormat(GWTFormat.fmt_money);
    taxField.setSelectAllOnFocus(true);
    taxField.addKeyDownHandler(new KeyDownHandler() {
      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == RKey.KEY_CODE_ENTER) {
          GWTUtil.blurActiveElement();
          if (entity.getInvoices().get(entity.getInvoices().size() - 1) == invoice) {
            RActionEvent
                .fire(PayInvoiceRegInvoiceEditGadget.this, InvoiceRegActionName.ADD_INVOICE);
          } else {
            invoiceTypeField.setFocus(true);
            element = GWTUtil.getActiveElement();
            RActionEvent.fire(PayInvoiceRegInvoiceEditGadget.this,
                InvoiceRegActionName.NEXT_INVOICE);
          }
        }
      }
    });
    taxField.addChangeHandler(handler);
    rightForm.addField(new RCombinedField() {
      {
        setCaption(InvoiceRegMessage.M.amountTax());
        setRequired(true);
        addField(amountField, 0.5f);
        addField(taxField, 0.5f);
      }
    });

    rightForm.rebuild();

    rightBox = new RCaptionBox();
    rightBox.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    rightBox.setCaption(PInvoiceRegInvoiceDef.constants.total_total());
    rightBox.setWidth("49%");
    rightBox.setContent(rightForm);

    return rightBox;
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == InvoiceRegActionName.CHANGE_INVOICE) {
      int row = ((Integer) event.getParameters().get(0)).intValue();
      Boolean isNew = (Boolean) event.getParameters().get(1);
      box.setCaption(InvoiceRegMessage.M.lineNumber(row + 1));
      invoice = entity.getInvoices().get(row);
      changeLine(isNew.booleanValue());
      if (invoice.getInvoiceNumber() == null)
        clearValidResults();
    }
  }

  @Override
  public Focusable getFocusable(String field) {
    if (BInvoiceRegInvoice.PN_INVOICENUMBER.equals(field)) {
      return invoiceNumberField;
    } else if (BInvoiceRegInvoice.PN_INVOICEDATE.equals(field)) {
      return invoiceDateField;
    } else if (BInvoiceRegInvoice.PN_INVOICETOTAL.equals(field)) {
      return totalField;
    } else if (BInvoiceRegInvoice.PN_INVOICETAX.equals(field)) {
      return taxField;
    }
    return null;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  private void changeLine(boolean isNew) {
    int size = entity.getInvoices().size();
    boolean prevDisable = entity.getInvoices().get(0) == invoice;
    prevAction.setEnabled(!prevDisable);
    boolean nextDisable = entity.getInvoices().get(size - 1) == invoice;
    nextAction.setEnabled(!nextDisable);

    refresh();
    if (isNew)
      invoiceTypeField.setFocus(true);
    else if (element != null)
      GWTUtil.focus(element);
    clearValidResults();
  }

  private void refresh() {
    assert invoice != null;
    if (invoice.getInvoiceType() == null && invoiceTypeField.getOptionCount() > 0)
      invoice.setInvoiceType(invoiceTypeField.getOptions().getValue(0));
    invoiceTypeField.setValue(invoice.getInvoiceType());

    invoiceNumberField.setValue(invoice.getInvoiceNumber());
    invoiceDateField.setValue(invoice.getInvoiceDate());
    remarkField.setValue(invoice.getRemark());
    if (invoice.getTaxRate() == null && taxRateField.getOptionCount() > 0) {
      invoice.setTaxRate(taxRateField.getOptions().getValue(0));
    }
    taxRateField.setValue(invoice.getTaxRate());
    refreshTotalField();

    clearValidResults();
    RActionEvent.fire(PayInvoiceRegInvoiceEditGadget.this,
        InvoiceRegActionName.CHANGE_INVOICE_VALUE);
  }

  private void refreshTotalField() {
    totalField.setValue(invoice.getTotal() == null ? null : invoice.getTotal().getTotal()
        .divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP));
    taxField.setValue(invoice.getTotal() == null ? null : invoice.getTotal().getTax());
    amountField.setValue(invoice.getAmount());
  }

  public void clearValidResults() {
    leftForm.clearValidResults();
    rightForm.clearValidResults();
  }

  private class Handler_prevAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      element = GWTUtil.getActiveElement();
      GWTUtil.blurActiveElement();
      RActionEvent.fire(PayInvoiceRegInvoiceEditGadget.this, InvoiceRegActionName.PREV_INVOICE);
    }
  }

  private class Handler_nextAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      element = GWTUtil.getActiveElement();
      GWTUtil.blurActiveElement();
      RActionEvent.fire(PayInvoiceRegInvoiceEditGadget.this, InvoiceRegActionName.NEXT_INVOICE);
    }
  }

  private class Handler_deleteAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RActionEvent.fire(PayInvoiceRegInvoiceEditGadget.this,
          InvoiceRegActionName.DELETE_CURRENT_INVOICE);
    }
  }

  private class Handler_textField implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == invoiceTypeField) {
        invoice.setInvoiceType(invoiceTypeField.getValue());
      } else if (event.getSource() == invoiceNumberField) {
        invoice.setInvoiceNumber(invoiceNumberField.getValue());
      } else if (event.getSource() == invoiceDateField) {
        invoice.setInvoiceDate(invoiceDateField.getValue());
      } else if (event.getSource() == amountField) {//去税金额
        if (amountField.validate() == false)
          return;
        invoice.changeAmount(amountField.getValueAsBigDecimal());
        refreshTotalField();
      } else if (event.getSource() == taxField) {//税额
        if (taxField.validate() == false)
          return;
        invoice.changeTax(taxField.getValueAsBigDecimal());
        refreshTotalField();
      } else if (event.getSource() == totalField) {//金额
        if (totalField.validate() == false)
          return;
        invoice.changeTotal(totalField.getValueAsBigDecimal(), ep.getScale(), ep.getRoundingMode());
        refreshTotalField();
      } else if (event.getSource() == taxRateField) {//税率
        if (invoice == null)
          return;
        invoice.setTaxRate(taxRateField.getValue());
        invoice.changetaxRate(taxRateField.getValue(), ep.getScale(), ep.getRoundingMode());
        refreshTotalField();
      } else if (event.getSource() == remarkField) {
        invoice.setRemark(remarkField.getValue());
      }
      RActionEvent.fire(PayInvoiceRegInvoiceEditGadget.this,
          InvoiceRegActionName.CHANGE_INVOICE_VALUE);
    }
  }
}
