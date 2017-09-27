/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegGeneralEditGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-29 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.payment.client.ui.gadget;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.invoice.commons.client.actionname.InvoiceRegActionName;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.invoice.intf.client.InvoiceRegMessage;
import com.hd123.m3.account.gwt.invoice.intf.client.dd.PInvoiceRegDef;
import com.hd123.m3.account.gwt.invoice.payment.client.EPPayInvoiceReg;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
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
 * @author chenpeisi
 * 
 */
public class PayInvoiceRegGeneralEditGadget extends RCaptionBox implements HasRActionHandlers,
    RActionHandler {
  private EPPayInvoiceReg ep = EPPayInvoiceReg.getInstance(EPPayInvoiceReg.class);

  public PayInvoiceRegGeneralEditGadget() {
    drawSelf();
  }

  private BInvoiceReg entity;

  private RForm generalForm;
  private RViewStringField billNumberField;
  private RViewStringField accountUnitField;
  private RViewStringField counterpartField;
  private RDateBox regDateField;
  private RTextBox invoiceCodeField;

  private RForm operateForm;
  private RViewStringField bizStateField;
  private RViewStringField reasonField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;

  private RForm aggForm;
  private RViewNumberField accountTotalField;
  private RViewNumberField accountTaxField;
  private RViewNumberField invoiceTotalField;
  private RViewNumberField invoiceTaxField;
  private RViewNumberField totalDiffField;
  private RViewNumberField taxDiffField;

  private PermGroupEditField permGroupField;

  private Handler_textField handler = new Handler_textField();

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

    w = drawAggPanel();
    mvp.add(0, w);

    w = drawOperatePanel();
    mvp.add(1, w);

    permGroupField = new PermGroupEditField(ep.isPermEnabled(), ep.getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    setCaption(InvoiceRegMessage.M.generalInfo());
    setWidth("100%");
    setEditing(true);
    setContent(vp);
  }

  private Widget drawGeneralPanel() {
    generalForm = new RForm(1);
    generalForm.setWidth("100%");

    billNumberField = new RViewStringField(PInvoiceRegDef.billNumber);
    billNumberField.addTextStyleName(RTextStyles.STYLE_BOLD);
    generalForm.addField(billNumberField);

    accountUnitField = new RViewStringField(PInvoiceRegDef.accountUnit);
    generalForm.addField(accountUnitField);

    counterpartField = new RViewStringField();
    counterpartField
        .setCaption(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
    generalForm.addField(counterpartField);

    regDateField = new RDateBox(PInvoiceRegDef.regDate);
    regDateField.addChangeHandler(handler);
    generalForm.addField(regDateField);

    invoiceCodeField = new RTextBox(PInvoiceRegDef.invoiceCode);
    invoiceCodeField.addChangeHandler(handler);
    generalForm.addField(invoiceCodeField);

    RCaptionBox box = new RCaptionBox();
    box.setContent(generalForm);
    box.setCaption(InvoiceRegMessage.M.basicInfo());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    return box;
  }

  private Widget drawOperatePanel() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    bizStateField = new RViewStringField(PInvoiceRegDef.constants.bizState());
    bizStateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(bizStateField);

    reasonField = new RViewStringField(InvoiceRegMessage.M.reason());
    operateForm.addField(reasonField);

    createInfoField = new RSimpleOperateInfoField(PInvoiceRegDef.constants.createInfo());
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(PInvoiceRegDef.constants.lastModifyInfo());
    operateForm.addField(lastModifyInfoField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(InvoiceRegMessage.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  private Widget drawAggPanel() {
    aggForm = new RForm(1);
    aggForm.setWidth("100%");

    accountTotalField = new RViewNumberField<Number>();
    accountTotalField.setFormat(GWTFormat.fmt_money);
    accountTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    accountTaxField = new RViewNumberField<Number>();
    accountTaxField.setFormat(GWTFormat.fmt_money);
    accountTaxField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    aggForm.addField(new RCombinedField() {
      {
        setCaption(PInvoiceRegDef.constants.accountTotal());
        addField(accountTotalField, 0.5f);
        addField(accountTaxField, 0.5f);
      }
    });

    invoiceTotalField = new RViewNumberField<Number>();
    invoiceTotalField.setFormat(GWTFormat.fmt_money);
    invoiceTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    invoiceTaxField = new RViewNumberField<Number>();
    invoiceTaxField.setFormat(GWTFormat.fmt_money);
    invoiceTaxField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    aggForm.addField(new RCombinedField() {
      {
        setCaption(PInvoiceRegDef.constants.invoiceTotal());
        addField(invoiceTotalField, 0.5f);
        addField(invoiceTaxField, 0.5f);
      }
    });

    totalDiffField = new RViewNumberField<Number>();
    totalDiffField.setFormat(GWTFormat.fmt_money);
    totalDiffField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    taxDiffField = new RViewNumberField<Number>();
    taxDiffField.setFormat(GWTFormat.fmt_money);
    taxDiffField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    aggForm.addField(new RCombinedField() {
      {
        setCaption(PInvoiceRegDef.constants.totalDiff() + "/" + PInvoiceRegDef.constants.taxDiff());
        addField(totalDiffField, 0.5f);
        addField(taxDiffField, 0.5f);
      }
    });

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(InvoiceRegMessage.M.aggregate());
    box.setWidth("100%");
    box.setContent(aggForm);
    return box;
  }

  @Override
  public void onAction(RActionEvent event) {
    if (InvoiceRegActionName.AGGREGATE_INVOICE.equals(event.getActionName())) {
      entity.caculateInvoiceTotal();
    } else if (InvoiceRegActionName.AGGREGATE_LINE.equals(event.getActionName())) {
      entity.caculateAccountTotal();
    }
    refreshAgg();
  }

  private void refreshAgg() {
    if (entity.getAccountTotal() == null || entity.getAccountTotal().getTax() == null
        || entity.getAccountTotal().getTotal() == null)
      entity.setAccountTotal(BTotal.zero());
    accountTotalField.setValue(entity.getAccountTotal().getTotal());
    accountTaxField.setValue(entity.getAccountTotal().getTax());

    if (entity.getInvoiceTotal() == null || entity.getInvoiceTotal().getTax() == null
        || entity.getInvoiceTotal().getTotal() == null)
      entity.setInvoiceTotal(BTotal.zero());
    invoiceTotalField.setValue(entity.getInvoiceTotal().getTotal());
    invoiceTaxField.setValue(entity.getInvoiceTotal().getTax());

    if (entity.getTotalDiff() == null)
      entity.setTotalDiff(BigDecimal.ZERO);
    totalDiffField.setValue(entity.getTotalDiff());

    if (entity.getTaxDiff() == null)
      entity.setTaxDiff(BigDecimal.ZERO);
    taxDiffField.setValue(entity.getTaxDiff());
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  public void refresh(BInvoiceReg entity) {
    assert entity != null;
    this.entity = entity;

    billNumberField.setValue(entity.getBillNumber());
    accountUnitField.setValue(entity.getAccountUnit().toFriendlyStr());
    counterpartField.setValue(entity.getCounterpart().toFriendlyStr(ep.getCounterpartTypeMap()));

    if (entity.getRegDate() == null)
      entity.setRegDate(new Date());
    regDateField.setValue(entity.getRegDate());

    invoiceCodeField.setValue(entity.getInvoiceCode());

    bizStateField.setValue(PInvoiceRegDef.bizState.getEnumCaption(entity.getBizState()));
    reasonField.setVisible(entity.getBpmMessage() != null);
    reasonField.setValue(entity.getBpmMessage());
    createInfoField.setOperateInfo(entity.getCreateInfo());
    lastModifyInfoField.setOperateInfo(entity.getLastModifyInfo());

    refreshAgg();

    permGroupField.setPerm(entity);
    operateForm.rebuild();
  }

  public void focusOnFirstField() {
    regDateField.setFocus(true);
  }

  private class Handler_textField implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == regDateField) {
        entity.setRegDate(regDateField.getValue());
      } else if (event.getSource() == invoiceCodeField) {
        entity.setInvoiceCode(invoiceCodeField.getValue());
      }
    }
  }
}
