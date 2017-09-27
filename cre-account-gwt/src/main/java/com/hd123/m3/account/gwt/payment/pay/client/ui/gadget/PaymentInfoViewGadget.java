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

import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.pay.client.PaymentMessages;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;

/**
 * @author subinzhu
 * 
 */
public class PaymentInfoViewGadget extends RCaptionBox {

  private BPayment bill;

  /** 实收查看控件 */
  private CashDefrayalViewGadget cashDefrayalViewGadget;

  private boolean viewPageUsing;

  public BPayment getBill() {
    return bill;
  }

  public void setBill(BPayment bill) {
    this.bill = bill;
  }

  public void refresh() {
    if (viewPageUsing) {
      setEditing(false);
    } else {
      setEditing(true);
    }

    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      cashDefrayalViewGadget.setValues(bill.getCashs(), true);
    } else if (CPaymentDefrayalType.line.equals(bill.getDefrayalType())) {
      if (!viewPageUsing) {
        // 如果不是查看界面调用，那么需要重新合并，否则直接从bill里面取出来就可以了。
        bill.aggreateCashsAndDepositsFromPaymentLine();
      }

      cashDefrayalViewGadget.setValues(bill.getCashs(), false);
    }
  }

  /** viewPageUsing表示是否是查看界面调用,按科目付款刷新付款信息时会用到。 */
  public PaymentInfoViewGadget(boolean viewPageUsing) {
    super();
    this.viewPageUsing = viewPageUsing;
    drawSelf();
  }

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    cashDefrayalViewGadget = new CashDefrayalViewGadget();
    vp.add(cashDefrayalViewGadget);

    setCaption(PaymentMessages.M.paymentInfo());
    setWidth("100%");
    setContent(vp);
  }

}
