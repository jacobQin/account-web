/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BillPaymentTypeViewGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-6 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client.ui.gadget;

import java.util.List;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCashDefrayal;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineCashDef;
import com.hd123.m3.account.gwt.payment.pay.client.PaymentMessages;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;

/**
 * @author subinzhu
 * 
 */
public class CashDefrayalViewGadget extends RCaptionBox {

  private static final String WIDTH_DETAIL_COL0 = "200px";
  private static final String WIDTH_DETAIL_COL1 = "150px";
  private static final String WIDTH_DETAIL_COL2 = "150px";
  private static final String WIDTH_DETAIL_COL3 = "200px";

  /** 是否是按总额收款，用于控制是否显示说明列 */
  private boolean defrayalBybill = true;
  private List<BPaymentCashDefrayal> values;
  private FlexTable tableTitle = new FlexTable();
  private HTML remarkTitle = new HTML(PPaymentLineCashDef.constants.remark());
  private FlexTable detailTable;

  public CashDefrayalViewGadget() {
    super();
    drawSelf();
  }

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();

    vp.add(drawDetailsTitle());
    vp.add(initDetail());

    setCaption(PaymentMessages.M.defrayal());
    setStyleName(RCaptionBox.STYLENAME_CONCISE);
    setWidth("100%");
    setContent(vp);
  }

  private Widget drawDetailsTitle() {
    if (tableTitle == null)
      tableTitle = new FlexTable();

    tableTitle.clear();

    tableTitle.setWidget(0, 0, new HTML(PaymentMessages.M.paymentType()));
    tableTitle.getColumnFormatter().setWidth(0, WIDTH_DETAIL_COL0);

    tableTitle.setWidget(0, 1, new HTML(PPaymentLineCashDef.constants.total()));
    tableTitle.getColumnFormatter().setWidth(1, WIDTH_DETAIL_COL1);

    tableTitle.setWidget(0, 2, new HTML(PPaymentLineCashDef.constants.bankCode()));
    tableTitle.getColumnFormatter().setWidth(2, WIDTH_DETAIL_COL2);

    if (defrayalBybill) {
      remarkTitle.setVisible(true);
      tableTitle.setWidget(0, 3, remarkTitle);
      tableTitle.getColumnFormatter().setWidth(3, WIDTH_DETAIL_COL3);
    } else {
      remarkTitle.setVisible(false);
    }
    return tableTitle;
  }

  private Widget initDetail() {
    if (detailTable == null) {
      detailTable = new FlexTable();
    }

    detailTable.clear();
    detailTable.getColumnFormatter().setWidth(0, WIDTH_DETAIL_COL0);
    detailTable.getColumnFormatter().setWidth(1, WIDTH_DETAIL_COL1);
    detailTable.getColumnFormatter().setWidth(2, WIDTH_DETAIL_COL2);
    if (defrayalBybill)
      detailTable.getColumnFormatter().setWidth(3, WIDTH_DETAIL_COL3);

    return detailTable;
  }

  /** 设置并刷新。defrayalBybill表示是否是按总额收款，是的话会显示说明列，否则不会显示说明列 */
  public void setValues(List<BPaymentCashDefrayal> values, boolean defrayalBybill) {
    this.values = values;
    this.defrayalBybill = defrayalBybill;
    refreshDetail();
  }

  private void refreshDetail() {
    if (defrayalBybill)
      remarkTitle.setVisible(true);
    else
      remarkTitle.setVisible(false);

    initDetail();

    if (values == null)
      return;

    for (int i = 0; i < values.size(); i++)
      addDetail(values.get(i), i);
  }

  private void addDetail(BPaymentCashDefrayal detail, int index) {
    assert detailTable != null;

    RViewStringField paymentTypeField = new RViewStringField();
    paymentTypeField.setWidth(WIDTH_DETAIL_COL0);
    paymentTypeField.setValue(detail.getPaymentType() == null ? null : detail.getPaymentType()
        .toFriendlyStr());
    detailTable.setWidget(index, 0, paymentTypeField);

    RViewNumberField total = new RViewNumberField();
    total.setWidth(WIDTH_DETAIL_COL1);
    total.setFormat(M3Format.fmt_money);
    total.setValue(detail.getTotal());
    detailTable.setWidget(index, 1, total);

    RViewStringField bank = new RViewStringField();
    bank.setWidth(WIDTH_DETAIL_COL2);
    bank.setValue((detail.getBank() == null || StringUtil.isNullOrBlank(detail.getBank().getCode())) ? null
        : detail.getBank().toFriendlyStr());
    detailTable.setWidget(index, 2, bank);

    if (defrayalBybill) {
      RViewStringField remark = new RViewStringField();
      remark.setWidth(WIDTH_DETAIL_COL3);
      remark.setOverflowEllipsis(true);
      remark.setValue(detail.getRemark());
      detailTable.setWidget(index, 3, remark);
    }
  }

}
