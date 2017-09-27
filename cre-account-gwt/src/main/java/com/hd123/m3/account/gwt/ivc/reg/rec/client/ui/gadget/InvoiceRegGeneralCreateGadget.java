/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRegGeneralCreateGadget.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.gadget;

import java.util.Arrays;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpart;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.invoice.BInvoiceStockState;
import com.hd123.m3.account.gwt.cpnts.client.ui.invoice.InvoiceStockBrowserBox;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.EPInvoiceRegReceipt;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.InvoiceRegMessages;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BIvcRegLine;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.InvoiceRegContains;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.dd.PInvoiceRegDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBasicState;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;

/**
 * 发票领用单新建页面|基本信息
 * 
 * @author chenrizhang
 * @since 1.7
 * 
 */
public class InvoiceRegGeneralCreateGadget extends RCaptionBox implements HasRActionHandlers,
    RActionHandler {
  private EPInvoiceRegReceipt ep = EPInvoiceRegReceipt.getInstance();
  private BInvoiceReg entity;

  private AccountUnitUCNBox storeField;
  private CounterpartUCNBox counterpartField;
  private RComboBox<String> invoiceTypeField;
  private RDateBox regDateFeild;

  private PermGroupEditField permGroupField;

  private RTextBox invoiceCodeField;
  private InvoiceStockBrowserBox invoiceNumberField;

  private RForm sumForm;
  private RViewNumberField sumOriginTotalField;
  private RViewNumberField sumUnregTotalField;
  private RViewNumberField sumTotalField;

  private FieldChangeHandler fieldHandler = new FieldChangeHandler();

  public InvoiceRegGeneralCreateGadget() {
    super();
    setWidth("100%");
    setEditing(true);
    getCaptionBar().setShowCollapse(true);
    setCaption(CommonsMessages.M.generalInfo());

    drawSelf();
  }

  private void drawSelf() {
    RMultiVerticalPanel mvp = new RMultiVerticalPanel();
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    mvp.setSpacing(5);

    mvp.add(0, drawBasicInfoGadget());
    if (ep.getEnabledExtInvoiceSystem() == false)
      mvp.add(0, drawInvoiceGadget());

    permGroupField = new PermGroupEditField(ep.isPermEnabled(), ep.getUserGroupList());
    mvp.add(1, permGroupField);
    mvp.add(1, drawSummaryGadget());

    setContent(mvp);
  }

  private Widget drawBasicInfoGadget() {
    RForm basicForm = new RForm(1);
    basicForm.setWidth("100%");
    basicForm.setCellSpacing(2);

    storeField = new AccountUnitUCNBox();
    storeField.setRequired(true);
    storeField.setCaption(PInvoiceRegDef.constants.accountUnit());
    storeField.addChangeHandler(fieldHandler);
    basicForm.addField(storeField);

    counterpartField = new CounterpartUCNBox();
    counterpartField.setRequired(true);
    counterpartField.setState(BBasicState.USING);
    counterpartField.setCounterpartTypes(Arrays.asList(BCounterpart.COUNTERPART_TYPE_TENANT));
    counterpartField.addChangeHandler(fieldHandler);
    basicForm.add(counterpartField);

    invoiceTypeField = new RComboBox<String>(PInvoiceRegDef.invoiceType);
    invoiceTypeField.removeNullOption();
    invoiceTypeField.setRequired(true);
    invoiceTypeField.setEditable(false);
    for (Map.Entry<String, String> item : ep.getInvoiceTypes().entrySet()) {
      invoiceTypeField.addOption(item.getKey(), item.getValue());
    }
    invoiceTypeField.addChangeHandler(fieldHandler);
    if (ep.getEnabledExtInvoiceSystem() == false)
      basicForm.addField(invoiceTypeField);

    regDateFeild = new RDateBox(PInvoiceRegDef.regDate);
    regDateFeild.addChangeHandler(fieldHandler);
    basicForm.addField(regDateFeild);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(CommonsMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(basicForm);
    return box;
  }

  private Widget drawInvoiceGadget() {
    RForm form = new RForm(1);
    form.setWidth("100%");
    form.setCellSpacing(2);

    invoiceNumberField = new InvoiceStockBrowserBox();
    invoiceNumberField.setFieldDef(PInvoiceRegDef.invoice_number);
    invoiceNumberField.setAccountUnitControl(true);
    invoiceNumberField.setRequired(false);
    invoiceNumberField.setStates(BInvoiceStockState.received.name());
    invoiceNumberField.addValueChangeHandler(new ValueChangeHandler() {

      @Override
      public void onValueChange(ValueChangeEvent event) {
        entity.setInvoiceNumber(invoiceNumberField.getValue());
        if (StringUtil.isNullOrBlank(invoiceNumberField.getValue()) == false) {
          for (BIvcRegLine line : entity.getRegLines()) {
            line.setInvoiceNumber(entity.getInvoiceNumber());
          }
        }
        if (invoiceNumberField.getRawValue() != null) {
          entity.setInvoiceCode(invoiceNumberField.getRawValue().getInvoiceCode());
          invoiceCodeField.setValue(entity.getInvoiceCode());
          for (BIvcRegLine line : entity.getRegLines()) {
            line.setInvoiceCode(entity.getInvoiceCode());
          }
        } else if (entity.isUseInvoiceStock()) {
          entity.setInvoiceCode(null);
          invoiceCodeField.setValue(null);
        }
        RActionEvent.fire(InvoiceRegGeneralCreateGadget.this,
            InvoiceRegContains.ACTION_REFRESH_DETAILS);
      }
    });
    form.addField(invoiceNumberField);

    invoiceCodeField = new RTextBox(PInvoiceRegDef.invoice_code);
    invoiceCodeField.setRequired(false);
    invoiceCodeField.addValueChangeHandler(new ValueChangeHandler() {

      @Override
      public void onValueChange(ValueChangeEvent event) {
        entity.setInvoiceCode(invoiceCodeField.getValue());
        for (BIvcRegLine line : entity.getRegLines()) {
          line.setInvoiceCode(invoiceCodeField.getValue());
        }
        RActionEvent.fire(InvoiceRegGeneralCreateGadget.this,
            InvoiceRegContains.ACTION_REFRESH_DETAILS);
      }
    });
    form.addField(invoiceCodeField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(InvoiceRegMessages.M.invoiceInfo());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private Widget drawSummaryGadget() {
    sumForm = new RForm(1);
    sumForm.setWidth("100%");
    sumForm.setCellSpacing(2);

    sumOriginTotalField = new RViewNumberField(PInvoiceRegDef.originTotal);
    sumOriginTotalField.setFormat(M3Format.fmt_money);
    sumOriginTotalField.setWidth("50%");
    sumOriginTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    sumForm.addField(sumOriginTotalField);

    sumUnregTotalField = new RViewNumberField(PInvoiceRegDef.unregTotal);
    sumUnregTotalField.setFormat(M3Format.fmt_money);
    sumUnregTotalField.setWidth("50%");
    sumUnregTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    sumForm.addField(sumUnregTotalField);

    sumTotalField = new RViewNumberField(PInvoiceRegDef.total);
    sumTotalField.setWidth("50%");
    sumTotalField.setFormat(M3Format.fmt_money);
    sumTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    sumForm.addField(sumTotalField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(InvoiceRegMessages.M.summary());
    box.setWidth("100%");
    box.setContent(sumForm);
    return box;
  }

  public void setValue(BInvoiceReg entity) {
    this.entity = entity;

    storeField.setValue(entity.getAccountUnit());
    counterpartField
        .setValue(new BCounterpart(entity.getCounterpart(), entity.getCounterpartType()));
    if (entity.getInvoiceType() == null && invoiceTypeField.getOptionCount() > 0) {
      entity.setInvoiceType(invoiceTypeField.getOptions().getValue(0));
      for (BIvcRegLine line : entity.getRegLines()) {
        line.setInvoiceType(entity.getInvoiceType());
      }
    }
    invoiceTypeField.setValue(entity.getInvoiceType());
    regDateFeild.setValue(entity.getRegDate());
    if (ep.getEnabledExtInvoiceSystem() == false) {
      invoiceNumberField.setAccountUnit(entity.getAccountUnit() == null ? null : entity
          .getAccountUnit().getUuid());
      invoiceNumberField.setInvoiceTypes(entity.getInvoiceType());
      invoiceNumberField.setStockControl(entity.isUseInvoiceStock());
      invoiceNumberField.clearValue();
      invoiceCodeField.setReadOnly(entity.isUseInvoiceStock());
      invoiceCodeField.clearValue();
    }
    permGroupField.setPerm(entity);

    sumUnregTotalField.setVisible(entity.isAllowSplitReg());
    sumForm.rebuild();

    refreshTotal();
  }

  public void clearConditions() {
    storeField.clearConditions();
    counterpartField.clearConditions();
    if (ep.getEnabledExtInvoiceSystem() == false)
      invoiceNumberField.clearConditions();
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (InvoiceRegContains.ACTION_REFRESH_SUMMARY.equals(event.getActionName())) {
      refreshTotal();
    }
  }

  private void refreshTotal() {
    if (entity == null)
      return;
    entity.sumTotal();
    sumOriginTotalField.setValue(entity.getOriginTotal().getTotal());
    sumUnregTotalField.setValue(entity.getAccountTotal().getTotal());
    sumTotalField.setValue(entity.getInvoiceTotal().getTotal());
  }

  private class FieldChangeHandler implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (storeField == event.getSource()) {
        changeStore();
      } else if (invoiceTypeField == event.getSource()) {
        changeInvoiceType();
      } else if (counterpartField == event.getSource()) {
        changeCounterpart();
      } else if (regDateFeild == event.getSource()) {
        entity.setRegDate(regDateFeild.getValue());
      }
    }

    private void changeStore() {
      if (entity.getRegLines().isEmpty()) {
        if (ep.getEnabledExtInvoiceSystem() == false) {
          invoiceNumberField.clearValue();
          invoiceCodeField.clearValue();
          invoiceNumberField.setAccountUnit(storeField.getValue().getUuid());
          RActionEvent.fire(InvoiceRegGeneralCreateGadget.this,
              InvoiceRegContains.ACTION_REFRESH_DETAILS);
        }
        entity.setAccountUnit(storeField.getValue());
        return;
      }
      RMsgBox.showConfirm(InvoiceRegMessages.M.confirmToChange(), new RMsgBox.ConfirmCallback() {
        @Override
        public void onClosed(boolean confirmed) {
          if (confirmed) {
            if (ep.getEnabledExtInvoiceSystem() == false) {
              invoiceNumberField.clearValue();
              invoiceCodeField.clearValue();
              invoiceNumberField.setAccountUnit(storeField.getValue().getUuid());
            }
            entity.setAccountUnit(storeField.getValue());
            entity.getRegLines().clear();
            RActionEvent.fire(InvoiceRegGeneralCreateGadget.this,
                InvoiceRegContains.ACTION_REFRESH_DETAILS);
          } else {
            storeField.setValue(entity.getAccountUnit());
          }
        }
      });
    }

    private void changeCounterpart() {
      if (entity.getRegLines().isEmpty()) {
        entity.setCounterpart(counterpartField.getValue());
        entity.setCounterpartType(counterpartField.getValue() == null ? null : counterpartField
            .getValue().getModule());
        return;
      }
      RMsgBox.showConfirm(InvoiceRegMessages.M.confirmToChange(), new RMsgBox.ConfirmCallback() {
        @Override
        public void onClosed(boolean confirmed) {
          if (confirmed) {
            entity.setCounterpart(counterpartField.getValue());
            entity.setCounterpartType(counterpartField.getValue() == null ? null : counterpartField
                .getValue().getModule());
            entity.getRegLines().clear();
            RActionEvent.fire(InvoiceRegGeneralCreateGadget.this,
                InvoiceRegContains.ACTION_REFRESH_DETAILS);
          } else {
            counterpartField.setValue(new BCounterpart(entity.getCounterpart(), entity
                .getCounterpartType()));
          }
        }
      });
    }

    private void changeInvoiceType() {
      if (entity.getRegLines().isEmpty()) {
        entity.setInvoiceType(invoiceTypeField.getValue());
        if (ep.getEnabledExtInvoiceSystem() == false) {
          invoiceNumberField.setInvoiceTypes(entity.getInvoiceType());
          invoiceNumberField.clearValue();
          invoiceCodeField.setValue(null);
        }
        return;
      }
      if (entity.getInvoiceType() != null && invoiceTypeField.getValue() != null
          && entity.getInvoiceType().equals(invoiceTypeField.getValue())) {
        return;
      }

      RMsgBox.showConfirm(InvoiceRegMessages.M.confirmTypeChange(), new RMsgBox.ConfirmCallback() {
        @Override
        public void onClosed(boolean confirmed) {
          if (confirmed) {
            entity.setInvoiceType(invoiceTypeField.getValue());
            clearInvoiceInfo();
            if (ep.getEnabledExtInvoiceSystem() == false)
              invoiceNumberField.setInvoiceTypes(entity.getInvoiceType());
            RActionEvent.fire(InvoiceRegGeneralCreateGadget.this,
                InvoiceRegContains.ACTION_REFRESH_DETAILS);
          } else {
            invoiceTypeField.setValue(entity.getInvoiceType());
          }
        }
      });
    }

    private void clearInvoiceInfo() {
      if (ep.getEnabledExtInvoiceSystem() == false) {
        invoiceNumberField.clearValue();
        invoiceCodeField.setValue(null);
      }
      for (BIvcRegLine line : entity.getRegLines()) {
        line.setInvoiceCode(null);
        line.setInvoiceNumber(null);
        line.setInvoiceType(entity.getInvoiceType());
      }
    }
  }
}
