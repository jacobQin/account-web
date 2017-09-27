/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentInfoGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-5 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client.ui.gadget;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Focusable;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCashDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDepositDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentHasFocusables;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.pay.client.EPPayment;
import com.hd123.m3.account.gwt.payment.pay.client.PaymentMessages;
import com.hd123.m3.account.gwt.payment.rec.client.biz.ActionName;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;

/**
 * @author subinzhu
 * 
 */
public class PaymentInfoEditGadget extends RCaptionBox implements RActionHandler,
    HasRActionHandlers, PaymentHasFocusables {

  private EPPayment ep = EPPayment.getInstance();
  private BPayment bill;

  /** 实收编辑控件 */
  private CashDefrayalEditGadget cashDefrayalEditGadget;

  public BPayment getBill() {
    return bill;
  }

  public void setBill(BPayment bill) {
    this.bill = bill;
  }

  public void refresh() {
    cashDefrayalEditGadget.setValue(bill.getCashs());
    cashDefrayalEditGadget.setStore(bill.getStore(), false);
  }

  public PaymentInfoEditGadget() {
    super();
    drawSelf();
  }

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    cashDefrayalEditGadget = new CashDefrayalEditGadget();
    cashDefrayalEditGadget.addActionHandler(this);
    vp.add(cashDefrayalEditGadget);

    setCaption(PaymentMessages.M.paymentInfo());
    setWidth("100%");
    setContent(vp);
    setEditing(true);
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (ObjectUtil.equals(event.getActionName(), ActionName.ACTION_CASHDEFRAYAL_CHANGE)
        || ObjectUtil.equals(event.getActionName(),
            DepositDefrayalEditGadget.ActionName.ACTION_DEPOSITDEFRAYAL_CHANGE)) {
      doChangeDefrayalTotal();
      RActionEvent.fire(PaymentInfoEditGadget.this, event.getActionName(), "");
      RActionEvent.fire(PaymentInfoEditGadget.this, ActionName.ACTION_PAYMENTINFO_CHANGE);
    } else if (ActionName.ACTION_GENERALCREATE_REFRESH_BANKS.equals(event.getActionName())) {
      cashDefrayalEditGadget.setStore(bill.getStore(), true);
    }
  }

  /** 合计实收总额以及产生的预存款金额 */
  private void doChangeDefrayalTotal() {
    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      BigDecimal defrayalTotal = BigDecimal.ZERO;
      defrayalTotal.setScale(2, RoundingMode.HALF_UP);
      // 合计实收总额
      for (BPaymentCashDefrayal cash : bill.getCashs()) {
        if (cash.getTotal() == null)
          continue;
        defrayalTotal = defrayalTotal.add(cash.getTotal());
      }
      // 合计扣预存款总额
      for (BPaymentDepositDefrayal deposit : bill.getDeposits()) {
        if (deposit.getTotal() == null)
          continue;
        defrayalTotal = defrayalTotal.add(deposit.getTotal());
      }
      bill.setDefrayalTotal(defrayalTotal);
      bill.aggregate(ep.getScale(), ep.getRoundingMode());
    }
  }

  @Override
  public Focusable getFocusable(int lineNumber, String field) {
    Focusable f = cashDefrayalEditGadget.getFocusable(lineNumber, field);
    return f;
  }
}
