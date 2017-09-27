/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegGeneralCreateGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.receipt.client.ui.gadget;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.AccountUnitBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.CounterpartBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.invoice.commons.client.actionname.InvoiceRegActionName;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.invoice.intf.client.InvoiceRegMessage;
import com.hd123.m3.account.gwt.invoice.intf.client.dd.PInvoiceRegDef;
import com.hd123.m3.account.gwt.invoice.receipt.client.EPRecInvoiceReg;
import com.hd123.m3.account.gwt.invoice.receipt.client.rpc.RecInvoiceRegService;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox.ConfirmCallback;
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
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * @author chenpeisi
 * 
 */
public class RecInvoiceRegGeneralCreateGadget extends RCaptionBox implements HasRActionHandlers,
    RActionHandler {
  private EPRecInvoiceReg ep = EPRecInvoiceReg.getInstance(EPRecInvoiceReg.class);

  public RecInvoiceRegGeneralCreateGadget() {
    drawSelf();
  }

  private BInvoiceReg entity;

  private RForm generalForm;
  private AccountUnitUCNBox accountUnitUCNBox;

  private RCombinedField countpartField;
  private CounterpartUCNBox counterpartUCNBox;
  private RViewStringField countpartTypeField;

  private RDateBox regDateField;
  private RTextBox invoiceCodeField;

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

    accountUnitUCNBox = new AccountUnitUCNBox();
    accountUnitUCNBox.setCaption(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));
    accountUnitUCNBox.setRequired(true);
    accountUnitUCNBox.addChangeHandler(handler);
    generalForm.addField(accountUnitUCNBox);

    counterpartUCNBox = new CounterpartUCNBox(ep.getCaptionMap());
    counterpartUCNBox.setCodeBoxWidth(Float.parseFloat("0.44"));
    counterpartUCNBox.setRequired(true);
    counterpartUCNBox.setCounterTypeMap(ep.getCounterpartTypeMap());
    counterpartUCNBox.addChangeHandler(handler);

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
      refreshAgg();
    } else if (InvoiceRegActionName.AGGREGATE_LINE.equals(event.getActionName())) {
      entity.caculateAccountTotal();
      refreshAgg();
    }
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

  public void clearQueryConditions() {
    if (accountUnitUCNBox != null)
      ((AccountUnitBrowserDialog) accountUnitUCNBox.getBrowser()).clearConditions();
    if (counterpartUCNBox != null)
      ((CounterpartBrowserDialog) counterpartUCNBox.getBrowser()).clearConditions();
  }

  public void refresh(BInvoiceReg entity) {
    assert entity != null;
    this.entity = entity;

    accountUnitUCNBox.setValue(entity.getAccountUnit());
    counterpartUCNBox.setValue(entity.getCounterpart());
    if (ep.getCounterpartTypeMap().size() > 1)
      countpartTypeField.setValue(entity.getCounterpart() == null ? null : ep
          .getCounterpartTypeMap().get(entity.getCounterpart().getCounterpartType()));

    if (entity.getRegDate() == null)
      entity.setRegDate(new Date());
    regDateField.setValue(entity.getRegDate());

    invoiceCodeField.setValue(entity.getInvoiceCode());

    refreshAgg();

    permGroupField.setPerm(entity);
  }

  public void focusOnFirstField() {
    accountUnitUCNBox.setFocus(true);
  }

  private class Handler_textField implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == accountUnitUCNBox) {
        refreshAccountUnit();
      } else if (event.getSource() == counterpartUCNBox) {
        refreshCounterpart();
      } else if (event.getSource() == regDateField) {
        entity.setRegDate(regDateField.getValue());
      } else if (event.getSource() == invoiceCodeField) {
        entity.setInvoiceCode(invoiceCodeField.getValue());
      }
    }
  }

  private void refreshAccountUnit() {
    if (ObjectUtil.equals(entity.getAccountUnit(), accountUnitUCNBox.getValue()))
      return;
    if (entity.hasValidateLine() == false) {
      entity.setAccountUnit(accountUnitUCNBox.getValue());
      return;
    }
    String msg = "改变项目将清空付款发票登记单账款单据明细，是否继续？";
    RMsgBox.showConfirm(msg, new ConfirmCallback() {

      @Override
      public void onClosed(boolean confirmed) {
        if (confirmed == false) {
          accountUnitUCNBox.setValue(entity.getAccountUnit());
          return;
        }
        entity.setAccountUnit(accountUnitUCNBox.getValue());
        entity.getLines().clear();
        RActionEvent.fire(RecInvoiceRegGeneralCreateGadget.this, InvoiceRegActionName.REFRESH_LINE);
      }
    });
  }

  private void refreshCounterpart() {
    if (ObjectUtil.equals(entity.getCounterpart(), counterpartUCNBox.getValue()))
      return;
    if (entity.hasValidateLine() == false) {
      entity.setCounterpart(counterpartUCNBox.getRawValue());
      if (ep.getCounterpartTypeMap().size() > 1)
        countpartTypeField.setValue(counterpartUCNBox.getRawValue() == null ? null : ep
            .getCounterpartTypeMap().get(counterpartUCNBox.getRawValue().getCounterpartType()));
      refreshInvoiceCode();
      return;
    }
    String msg = "改变商户将清空付款发票登记单账款单据明细，是否继续？";
    RMsgBox.showConfirm(msg, new ConfirmCallback() {

      @Override
      public void onClosed(boolean confirmed) {
        if (confirmed == false) {
          counterpartUCNBox.setValue(entity.getCounterpart());
          return;
        }
        entity.setCounterpart(counterpartUCNBox.getRawValue());
        if (ep.getCounterpartTypeMap().size() > 1)
          countpartTypeField.setValue(counterpartUCNBox.getRawValue() == null ? null : ep
              .getCounterpartTypeMap().get(counterpartUCNBox.getRawValue().getCounterpartType()));
        refreshInvoiceCode();
        entity.getLines().clear();
        RActionEvent.fire(RecInvoiceRegGeneralCreateGadget.this, InvoiceRegActionName.REFRESH_LINE);
      }
    });
  }

  private void refreshInvoiceCode() {
    if (entity.getCounterpart() == null || entity.getCounterpart().getUuid() == null)
      return;

    GWTUtil.enableSynchronousRPC();
    RLoadingDialog.show(InvoiceRegMessage.M.loading());
    RecInvoiceRegService.Locator.getService().getInvoiceCode(entity.getCounterpart().getUuid(),
        new RBAsyncCallback2<String>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InvoiceRegMessage.M.actionFailed(InvoiceRegMessage.M.load(),
                PInvoiceRegDef.constants.invoiceCode());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(String result) {
            RLoadingDialog.hide();
            entity.setInvoiceCode(result);
            invoiceCodeField.setValue(entity.getInvoiceCode());
            invoiceCodeField.clearValidResults();
          }
        });
  }
}
