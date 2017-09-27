/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	CashDefrayalLineGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-29 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineCash;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentTypeComboBox;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentCashDefrayalDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineCashDef;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.account.gwt.payment.rec.client.biz.ActionName;
import com.hd123.m3.account.gwt.payment.rec.client.biz.BReceiptConfig;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * 按科目收款时，收款单明细行中收款方式明细行编辑控件
 * 
 * @author subinzhu
 * 
 */
public class LineCashDefrayalLineEditGadget extends Composite implements HasRActionHandlers,
    RValidatable, HasFocusables, Focusable {

  public static final String WIDTH_DETAIL_COL0 = "200px";
  public static final String WIDTH_DETAIL_COL1 = "150px";
  public static final String WIDTH_DETAIL_COL2 = "150px";
  public static final String WIDTH_DETAIL_COL3 = "150px";
  public static final String WIDTH_DETAIL_COL4 = "20px";
  public static final String WIDTH_DETAIL_COL5 = "20px";

  private BPaymentLineCash cash = new BPaymentLineCash();
  private BPayment payment;
  private BReceiptConfig config = EPReceipt.getInstance().getConfig();

  private FlexTable root;
  private PaymentTypeComboBox paymentTypeField;
  private RNumberBox totalField;
  private RComboBox<BBank> bankField;
  private RTextBox remarkField;
  private RToolbarButton addButton;
  private RToolbarButton removeButton;

  private Handler_valueChange valueChangeHandler = new Handler_valueChange();

  /** 项目改变时调用 */
  public void resetOptions() {
    BBank value = bankField.getValue();
    List<BBank> results = new ArrayList<BBank>();
    bankField.clearOptions();
    if (payment == null || payment.getAccountUnit() == null
        || payment.getAccountUnit().getUuid() == null) {
      return;
    }
    for (BBank bank : EPReceipt.getInstance().getBanks()) {
      if (bank.getStore() == null) {
        continue;
      }
      if (payment.getAccountUnit().getUuid().equals(bank.getStore().getUuid())) {
        results.add(bank);
      }
    }
    bankField.addOptionList(results);

    if (value != null) {
      bankField.setValue(value);
    }
  }

  public LineCashDefrayalLineEditGadget() {
    root = new FlexTable();
    root.setCellPadding(0);
    root.setCellSpacing(2);

    paymentTypeField = new PaymentTypeComboBox(EPReceipt.getInstance().getPaymentTypes(true));
    paymentTypeField.setEditable(false);
    paymentTypeField.addValueChangeHandler(valueChangeHandler);
    root.setWidget(0, 0, paymentTypeField);
    root.getColumnFormatter().setWidth(0, WIDTH_DETAIL_COL0);

    totalField = new RNumberBox();
    totalField.setSelectAllOnFocus(true);
    totalField.setScale(2);
    totalField.setFormat(GWTFormat.fmt_money);
    totalField.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
    totalField.addValueChangeHandler(valueChangeHandler);
    totalField.setValidator(new RValidator() {

      @Override
      public Message validate(Widget sender, String value1) {
        if (totalField.getValueAsBigDecimal() != null
            && totalField.getValueAsBigDecimal().compareTo(cash.getTotal()) != 0) {
          cash.setTotal(totalField.getValueAsBigDecimal());
          RActionEvent.fire(LineCashDefrayalLineEditGadget.this,
              ActionName.ACTION_LINECASHDEFRAYALLINE_CHANGE);
        }
        if (totalField.getValueAsBigDecimal() != null
            && (BoundaryValue.BIGDECIMAL_MAXVALUE_S2.compareTo(totalField.getValueAsBigDecimal()) <= 0)) {
          return Message.error(ReceiptMessages.M.cannotMoreThan(
              PPaymentLineCashDef.constants.total(),
              BoundaryValue.BIGDECIMAL_MAXVALUE_S2.toString()));
        }

        if (config.getReceiptPaymentType() != null
            && paymentTypeField.getValue() != null
            && totalField.getValueAsBigDecimal() != null
            && ObjectUtil.equals(paymentTypeField.getValue(), config.getReceiptPaymentType())
            && totalField.getValueAsBigDecimal().abs()
                .compareTo(config.getReceiptPaymentTypeLimit()) > 0) {
          return Message.error(ReceiptMessages.M.errorPaymentLimit(config.getReceiptPaymentType()
              .toFriendlyStr(), config.getReceiptPaymentTypeLimit()));
        }

        if (totalField.getValueAsBigDecimal() == null) {
          totalField.setValue(0, true);
        }
        return null;
      }
    });
    root.setWidget(0, 1, totalField);
    root.getColumnFormatter().setWidth(1, WIDTH_DETAIL_COL1);

    bankField = new RComboBox<BBank>(PPaymentCashDefrayalDef.constants.bankCode());
    bankField.setNullOptionTextToDefault();
    bankField.setNullOptionText("[空]");
    bankField.setMaxDropdownRowCount(10);
    bankField.setEditable(false);
    bankField.setValidator(new BankValidator());
    bankField.addValueChangeHandler(valueChangeHandler);
    resetOptions();
    root.setWidget(0, 2, bankField);
    root.getColumnFormatter().setWidth(2, WIDTH_DETAIL_COL2);

    remarkField = new RTextBox(PPaymentLineCashDef.remark);
    remarkField.setCaption("");
    remarkField.addValueChangeHandler(valueChangeHandler);
    root.setWidget(0, 3, remarkField);
    root.getColumnFormatter().setWidth(3, WIDTH_DETAIL_COL3);

    addButton = new RToolbarButton(RActionFacade.ADD_ITEM, new ClickHandler() {
      @Override
      public void onClick(ClickEvent arg0) {
        GWTUtil.blurActiveElement();
        RActionEvent.fire(LineCashDefrayalLineEditGadget.this,
            ActionName.ACTION_LINECASHDEFRAYALLINE_ADDDETAIL, cash.getItemNo());
      }
    });
    addButton.setShowText(false);
    root.setWidget(0, 4, addButton);
    root.getColumnFormatter().setWidth(4, WIDTH_DETAIL_COL4);

    removeButton = new RToolbarButton(RActionFacade.REMOVE_ITEM, new ClickHandler() {
      @Override
      public void onClick(ClickEvent arg0) {
        GWTUtil.blurActiveElement();
        RActionEvent.fire(LineCashDefrayalLineEditGadget.this,
            ActionName.ACTION_LINECASHDEFRAYALLINE_REMOVEDETAIL, cash.getItemNo());
      }
    });
    removeButton.setShowText(false);
    root.setWidget(0, 5, removeButton);
    root.getColumnFormatter().setWidth(5, WIDTH_DETAIL_COL5);

    initWidget(root);
  }

  public BPaymentLineCash getValue() {
    return cash;
  }

  public void setValue(BPayment payment, BPaymentLineCash cash) {
    setValue(payment, cash, false);
  }

  public void setValue(BPayment payment, BPaymentLineCash cash, boolean fireEvent) {
    this.payment = payment;
    this.cash = cash;

    if (cash.getPaymentType() == null && paymentTypeField.getOptions().size() > 0)
      cash.setPaymentType(paymentTypeField.getOptions().getValue(0));
    paymentTypeField.setValue(cash.getPaymentType(), fireEvent);

    totalField.setValue(cash.getTotal() == null ? null : cash.getTotal().doubleValue(), fireEvent);
    resetOptions();
    bankField.setValue(cash.getBank(), fireEvent);

    remarkField.setValue(cash.getRemark(), fireEvent);

    clearValidResults();
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void clearValidResults() {
    paymentTypeField.clearValidResults();
    totalField.clearValidResults();
    bankField.clearValidResults();
    remarkField.clearValidResults();
  }

  @Override
  public boolean isValid() {
    boolean isValid = paymentTypeField.isValid();
    isValid &= totalField.isValid();
    isValid &= bankField.isValid();
    isValid &= remarkField.isValid();
    return isValid;
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> messages = new ArrayList<Message>();

    messages.addAll(paymentTypeField.getInvalidMessages());
    messages.addAll(bankField.getInvalidMessages());
    messages.addAll(remarkField.getInvalidMessages());

    return messages;
  }

  @Override
  public boolean validate() {
    boolean valid = paymentTypeField.validate();
    valid &= totalField.validate();
    valid &= bankField.validate();
    valid &= remarkField.validate();
    return valid;
  }

  public void focusOnFirstField() {
    paymentTypeField.setFocus(true);
  }

  private class BankValidator implements RValidator {

    @Override
    public Message validate(Widget sender, String value) {
      
      if (EPReceipt.getInstance().getConfig().isBankRequired() == false) {
        return null;
      }
      if (cash.getTotal() == null || cash.getTotal().compareTo(BigDecimal.ZERO) == 0) {
        return null;
      }

      if (bankField.getValue() == null || StringUtil.isNullOrBlank(bankField.getValue().getCode())) {
        return Message.error(ReceiptMessages.M.notNull(bankField.getCaption()));
      }
      
      return null;
    }

  }

  private class Handler_valueChange implements ValueChangeHandler {
    @Override
    public void onValueChange(ValueChangeEvent event) {
      if (cash == null)
        return;

      if (event.getSource() == paymentTypeField) {
        cash.setPaymentType(paymentTypeField.getValue());
        totalField.validate();
        RActionEvent.fire(LineCashDefrayalLineEditGadget.this,
            ActionName.ACTION_LINECASHDEFRAYALLINE_CHANGE);
      } else if (event.getSource() == bankField) {
        cash.setBank(bankField.getValue());
        RActionEvent.fire(LineCashDefrayalLineEditGadget.this,
            ActionName.ACTION_LINECASHDEFRAYALLINE_CHANGE);
      } else if (event.getSource() == remarkField) {
        cash.setRemark(remarkField.getValue());
        RActionEvent.fire(LineCashDefrayalLineEditGadget.this,
            ActionName.ACTION_LINECASHDEFRAYALLINE_CHANGE);
      }
    }
  }

  @Override
  public Focusable getFocusable(String field) {
    if (BPaymentLineCash.FN_LINECASHTOTAL.equals(field))
      return totalField;
    return null;
  }

  @Override
  public int getTabIndex() {
    return paymentTypeField.getTabIndex();
  }

  @Override
  public void setAccessKey(char key) {
    paymentTypeField.setAccessKey(key);
  }

  @Override
  public void setFocus(boolean focused) {
    paymentTypeField.setFocus(focused);
  }

  @Override
  public void setTabIndex(int index) {
    paymentTypeField.setTabIndex(index);
  }
}
