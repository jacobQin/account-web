/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BillAccountLineEditGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.gadget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BOverdueCalcHelper;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineCash;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDeposit;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentHasFocusables;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentLineDefrayalLineLocator;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentLineLocator;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineDepositDef;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.account.gwt.payment.rec.client.biz.ActionName;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxCalculator;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.hotkey.RHotKey;
import com.hd123.rumba.gwt.widget2.client.hotkey.RKey;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;

/**
 * 按科目收款，账款明细行编辑控件
 * 
 * @author subinzhu
 * 
 */
public class LineAccountLineEditGadget extends RCaptionBox implements RActionHandler,
    HasRActionHandlers, HasFocusables, PaymentHasFocusables {

  private EPReceipt ep = EPReceipt.getInstance();
  private BPayment bill;
  private BPaymentAccountLine line;

  private RAction prevAction;
  private RAction nextAction;
  private RAction deleteAction;

  private RCaptionBox cashDefrayalBox;
  /** 收款单明细行中收款方式编辑控件 */
  private LineCashDefrayalEditGadget cashDefrayalEditGadget;

  private RCaptionBox depositDefrayalBox;
  /** 收款单明细行中预存款冲扣编辑控件 */
  private LineDepositDefrayalEditGadget depositDefrayalEditGadget;

  private RTextArea remarkField;

  private Handler_changeField changeHandler;
  private Element element;

  public void setBill(BPayment bill) {
    this.bill = bill;
    depositDefrayalEditGadget.setBill(bill);
  }

  public LineAccountLineEditGadget() {
    super();
    changeHandler = new Handler_changeField();
    drawSelf();
  }

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    vp.add(drawCashDefrayalPanel());
    vp.add(drawDepositDefrayalPanel());
    vp.add(drawRemark());

    getCaptionBar().addButton(new RToolbarSeparator());

    prevAction = new RAction(RActionFacade.PREVIOUS_LINE, new Handler_prevAction());
    prevAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_UP));
    RToolbarButton prevButton = new RToolbarButton(prevAction);
    prevButton.setShowText(false);
    getCaptionBar().addButton(prevButton);

    nextAction = new RAction(RActionFacade.NEXT_LINE, new Handler_nextAction());
    nextAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_DOWN));
    RToolbarButton nextButton = new RToolbarButton(nextAction);
    nextButton.setShowText(false);
    getCaptionBar().addButton(nextButton);

    deleteAction = new RAction(RActionFacade.DELETE, new Handler_deleteAction());
    deleteAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_DELETE));
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    getCaptionBar().addButton(deleteButton);
    
    setStyleName(RCaptionBox.STYLENAME_CONCISE);
    setCaption(ReceiptMessages.M.lineNumber(0));
    setWidth("100%");
    setContent(vp);
    setEditing(true);
  }

  private Widget drawCashDefrayalPanel() {
    cashDefrayalEditGadget = new LineCashDefrayalEditGadget();
    this.addActionHandler(cashDefrayalEditGadget);
    cashDefrayalEditGadget.addActionHandler(this);

    cashDefrayalBox = new RCaptionBox();
    cashDefrayalBox.setContent(cashDefrayalEditGadget);
    cashDefrayalBox.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    cashDefrayalBox.setCaption(ReceiptMessages.M.paymentType());
    cashDefrayalBox.setWidth("100%");
    return cashDefrayalBox;
  }

  private Widget drawDepositDefrayalPanel() {
    depositDefrayalEditGadget = new LineDepositDefrayalEditGadget();
    depositDefrayalEditGadget.addActionHandler(this);

    depositDefrayalBox = new RCaptionBox();
    depositDefrayalBox.setContent(depositDefrayalEditGadget);
    depositDefrayalBox.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    depositDefrayalBox.setCaption(PPaymentLineDepositDef.TABLE_CAPTION);
    depositDefrayalBox.setWidth("100%");
    return depositDefrayalBox;
  }

  private Widget drawRemark() {
    remarkField = new RTextArea(PPaymentAccountLineDef.remark);
    remarkField.setCaption("");
    remarkField.addChangeHandler(changeHandler);
    remarkField.addKeyDownHandler(new KeyDownHandler() {
      @Override
      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == RKey.KEY_CODE_ENTER) {
          GWTUtil.blurActiveElement();
          if (bill.getAccountLines().get(bill.getAccountLines().size() - 1) != line) {
            element = GWTUtil.getActiveElement();
            RActionEvent.fire(LineAccountLineEditGadget.this,
                ActionName.ACTION_LINEACCOUNTLINE_NEXT);
          } else if (bill.getAccountLines().size() > 0) {
            RActionEvent.fire(LineAccountLineEditGadget.this,
                ActionName.ACTION_LINEACCOUNTLINE_SELECT_FIRST);
          }
        }
      }
    });

    RCaptionBox captionBox = new RCaptionBox();
    captionBox.setContent(remarkField);
    captionBox.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    captionBox.setCaption(PPaymentAccountLineDef.constants.remark());
    captionBox.setWidth("100%");
    return captionBox;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == ActionName.ACTION_ACCOUNTLINE_CHANGELINENO
        && !CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      int row = ((Integer) event.getParameters().get(0)).intValue();
      Boolean isNew = (Boolean) event.getParameters().get(1);
      setCaption(ReceiptMessages.M.lineNumber(row + 1));

      if (row < 0 || row >= bill.getAccountLines().size()) {
        prevAction.setEnabled(false);
        nextAction.setEnabled(false);
        deleteAction.setEnabled(false);
        clearValue();
        clearValidResults();
      } else {
        line = bill.getAccountLines().get(row);
        deleteAction.setEnabled(true);
        changeLine(isNew.booleanValue());
      }
    } else if (event.getActionName() == ActionName.ACTION_LINECASHDEFRAYAL_CHANGE) {
      refreshDefrayalTotal();
      RActionEvent.fire(LineAccountLineEditGadget.this,
          ActionName.ACTION_LINEACCOUNTLINE_CASHDEFRAYALCHANGE);
    } else if (event.getActionName() == LineDepositDefrayalEditGadget.ActionName.ACTION_LINEDEPOSITDEFRAYAL_CHANGE) {
      refreshDefrayalTotal();
      RActionEvent.fire(LineAccountLineEditGadget.this,
          ActionName.ACTION_LINEACCOUNTLINE_DEPOSITDEFRAYALCHANGE);
    } else if ((event.getActionName() == ActionName.CREATEPAGE_CHANGE_LINE || event.getActionName() == ActionName.EDITPAGE_CHANGE_LINE)
        && !CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      PaymentLineLocator locator = (PaymentLineLocator) event.getParameters().get(0);
      if (PaymentLineLocator.ACCOUNTTAB_ID.intValue() == locator.getTabId().intValue()) {
        int row = locator.getLineNumber();
       setCaption(ReceiptMessages.M.lineNumber(row + 1));
        line = bill.getAccountLines().get(row);
        changeLine(false);

        Focusable field = getFocusable(locator.getFieldId());
        field.setFocus(true);
      }
    } else if ((event.getActionName() == ActionName.CREATEPAGE_CHANGE_LINE_AND_LINEDEFRAYALLINE || event
        .getActionName() == ActionName.EDITPAGE_CHANGE_LINE_AND_LINEDEFRAYALLINE)
        && !CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      PaymentLineDefrayalLineLocator locator = (PaymentLineDefrayalLineLocator) event
          .getParameters().get(0);
      if (PaymentLineDefrayalLineLocator.ACCOUNTMULTITAB_ID.intValue() == locator.getTabId().intValue()) {
        int row = locator.getOutLineNumber().intValue();
        setCaption("第 " + (row + 1) + " 行");
        line = bill.getAccountLines().get(row);
        changeLine(false);

        Focusable field = getFocusable(locator.getLineNumber(), locator.getFieldId());
        field.setFocus(true);
      }
    } else if (event.getActionName() == ActionName.ACTION_GENERALCREATE_REFRESH_REMAINTOTAL
        && !CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      depositDefrayalEditGadget.refreshRemainTotal();

      RActionEvent.fire(LineAccountLineEditGadget.this,
          ActionName.ACTION_LINEACCOUNTLINE_DEPOSITDEFRAYALCHANGE);
    } else if (event.getActionName() == ActionName.ACTION_GENERALCREATE_REFRESH_SUBJECTS_AND_CONTRACTS
        && !CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      depositDefrayalEditGadget.refreshSubjectsAndContracts();
    }
    if (ObjectUtil.equals(event.getActionName(), ActionName.ACTION_GENERALCREATE_REFRESH_BANKS)) {
      RActionEvent.fire(LineAccountLineEditGadget.this, event.getActionName());
    }
  }

  /** 刷新当前行的实收金额 */
  private void refreshDefrayalTotal() {
    BigDecimal defrayalTotal = BigDecimal.ZERO;
    defrayalTotal.setScale(2, RoundingMode.HALF_UP);
    for (BPaymentLineCash cash : line.getCashs()) {
      if (cash.getTotal() == null || cash.getTotal().compareTo(BigDecimal.ZERO) == 0)
        continue;
      defrayalTotal = defrayalTotal.add(cash.getTotal());
    }
    boolean isOnlyUpdate = true;
    for (BPaymentLineDeposit deposit : line.getDeposits()) {
      if (deposit.getTotal() == null || deposit.getTotal().compareTo(BigDecimal.ZERO) <= 0)
        continue;
      defrayalTotal = defrayalTotal.add(deposit.getTotal());
    }
    line.setDefrayalTotal(defrayalTotal);
    BigDecimal newTotal = BigDecimal.ZERO;
    if (line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) >= 0) {
      if (line.getUnpayedTotal().getTotal() != null
          && line.getUnpayedTotal().getTotal().compareTo(defrayalTotal) > 0) {
        newTotal = defrayalTotal;
      } else {
        newTotal = line.getUnpayedTotal().getTotal();
      }
    } else {
      if (line.getUnpayedTotal().getTotal() != null
          && line.getUnpayedTotal().getTotal().compareTo(defrayalTotal) > 0) {
        newTotal = line.getUnpayedTotal().getTotal();
      } else {
        newTotal = defrayalTotal;
      }
    }
    if ((line.getTotal().getTotal() == null || line.getTotal().getTotal()
        .compareTo(BigDecimal.ZERO) == 0)
        && newTotal.compareTo(BigDecimal.ZERO) != 0) {
      isOnlyUpdate = false;
    }
    line.getTotal().setTotal(newTotal);
    line.getTotal().setTax(
        BTaxCalculator.tax(line.getTotal().getTotal(), line.getAcc1().getTaxRate(),
            ep.getScale(), ep.getRoundingMode()));
    if (line.getAcc2().getLastPayDate() != null)
      refreshOverdue(line, isOnlyUpdate);
    RActionEvent.fire(LineAccountLineEditGadget.this,
        ActionName.ACTION_LINEACCOUNTLINE_CASHDEFRAYALCHANGE);
  }

  /**
   * 刷新滞纳金
   */
  public void refreshOverdue(BPaymentAccountLine line, boolean isOnlyUpdate) {
    // 计算账款明细滞纳金
    BOverdueCalcHelper.calculate(bill, ep.getScale(),
        ep.getRoundingMode(),ep.getOverdueDefault());
  }
  /**
   * 清除值
   */
  private void clearValue() {
    clearCashDefrayalEditGadget();
    clearDepositDefrayalEditGadget();
    clearRemark();
  }

  private void clearCashDefrayalEditGadget() {
    cashDefrayalEditGadget.clearValue();
  }

  private void clearDepositDefrayalEditGadget() {
    depositDefrayalEditGadget.clearValue();
  }

  private void clearRemark() {
    remarkField.clearValue();
  }

  private void changeLine(boolean isNew) {
    int size = bill.getAccountLines().size();
    boolean prevDisable = bill.getAccountLines().get(0) == line;
    prevAction.setEnabled(!prevDisable);
    boolean nextDisable = bill.getAccountLines().get(size - 1) == line;
    nextAction.setEnabled(!nextDisable);

    refresh();
    if (isNew) {
      element = GWTUtil.getActiveElement();
    } else if (element != null)
      GWTUtil.focus(element);
  }

  private void refresh() {
    assert line != null;
    refreshCashDefrayalEditGadget();
    refreshDepositDefrayalEditGadget();
    refreshRemark();
  }

  private void refreshCashDefrayalEditGadget() {
    cashDefrayalEditGadget.setValue(bill,line.getCashs());
  }

  private void refreshDepositDefrayalEditGadget() {
    depositDefrayalEditGadget.setBill(bill);
    depositDefrayalEditGadget.setValue(line);
  }

  private void refreshRemark() {
    remarkField.setValue(line.getRemark());
  }

  private class Handler_changeField implements ChangeHandler {
    public void onChange(ChangeEvent event) {
      if (line == null)
        return;
      if (event.getSource() == remarkField) {
        line.setRemark(remarkField.getValue());
      }
      RActionEvent.fire(LineAccountLineEditGadget.this, ActionName.ACTION_LINEACCOUNTLINE_CHANGE);
    }
  }

  /**
   * 刷新滞纳金
   */
  public void refreshOverdue() {
    // 计算账款明细滞纳金
    BOverdueCalcHelper.calculate(bill, ep.getScale(), ep.getRoundingMode(),ep.getOverdueDefault());
  }

  private class Handler_prevAction implements ClickHandler {
    public void onClick(ClickEvent event) {
      element = GWTUtil.getActiveElement();
      GWTUtil.blurActiveElement();
      RActionEvent.fire(LineAccountLineEditGadget.this, ActionName.ACTION_LINEACCOUNTLINE_PREV);
    }
  }

  private class Handler_nextAction implements ClickHandler {
    public void onClick(ClickEvent event) {
      element = GWTUtil.getActiveElement();
      GWTUtil.blurActiveElement();
      RActionEvent.fire(LineAccountLineEditGadget.this, ActionName.ACTION_LINEACCOUNTLINE_NEXT);
    }
  }

  private class Handler_deleteAction implements ClickHandler {
    public void onClick(ClickEvent event) {
      RActionEvent.fire(LineAccountLineEditGadget.this, ActionName.ACTION_LINEACCOUNTLINE_DELETE);
    }
  }

  @Override
  public void clearValidResults() {
    cashDefrayalEditGadget.clearValidResults();
    depositDefrayalEditGadget.clearValidResults();
    remarkField.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return cashDefrayalEditGadget.isValid() & depositDefrayalEditGadget.isValid()
        & remarkField.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> messages = new ArrayList<Message>();
    messages.addAll(cashDefrayalEditGadget.getInvalidMessages());
    messages.addAll(depositDefrayalEditGadget.getInvalidMessages());
    return messages;
  }

  @Override
  public boolean validate() {
    return cashDefrayalEditGadget.validate() & depositDefrayalEditGadget.validate()
        & remarkField.validate();
  }

  @Override
  public Focusable getFocusable(String field) {
    if (BPaymentAccountLine.FN_ACCOUNTLINE_REMARK.equals(field))
      return remarkField;

    return null;
  }

  @Override
  public Focusable getFocusable(int lineNumber, String field) {
    Focusable f = cashDefrayalEditGadget.getFocusable(lineNumber, field);
    if (f == null)
      f = depositDefrayalEditGadget.getFocusable(lineNumber, field);
    return f;
  }

}
