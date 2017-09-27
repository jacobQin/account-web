/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BillPaymentTypeViewGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-6 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.gadget;

import java.util.List;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDepositDefrayal;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDepositDefrayalDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineDepositDef;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;

/**
 * @author subinzhu
 * 
 */
public class DepositDefrayalViewGadget extends RCaptionBox {

  private static final String WIDTH_DETAIL_COL0 = "200px";
  private static final String WIDTH_DETAIL_COL1 = "300px";
  private static final String WIDTH_DETAIL_COL2 = "100px";
  private static final String WIDTH_DETAIL_COL3 = "100px";
  private static final String WIDTH_DETAIL_COL4 = "200px";

  /** 是否是按总额收款，用于控制是否显示说明列 */
  private boolean defrayalBybill = true;
  private BPayment bill;
  private List<BPaymentDepositDefrayal> values;
  private FlexTable tableTitle = new FlexTable();
  private HTML subjectTitle;
  private HTML contractTitle;
  private Label remainTotalField;
  private Label totalField;
  private HTML remarkTitle;
  private FlexTable detailTable;

  public DepositDefrayalViewGadget() {
    super();
    drawSelf();
  }

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();

    vp.add(drawDetailsTitle());
    vp.add(initDetail());

    setCaption(PPaymentDepositDefrayalDef.TABLE_CAPTION);
    setStyleName(RCaptionBox.STYLENAME_CONCISE);
    setWidth("100%");
    setContent(vp);
  }

  private Widget drawDetailsTitle() {
    if (tableTitle == null)
      tableTitle = new FlexTable();

    if (subjectTitle == null)
      subjectTitle = new HTML(ReceiptMessages.M.depositRecSubject());
    if (contractTitle == null)
      contractTitle = new HTML(ReceiptMessages.M.contract());
    if (remainTotalField == null)
      remainTotalField = new HTML(ReceiptMessages.M.remainTotal());
    if (totalField == null)
      totalField = new HTML(PPaymentLineDepositDef.constants.total());
    if (remarkTitle == null)
      remarkTitle = new HTML(PPaymentLineDepositDef.constants.remark());

    return tableTitle;
  }

  /** 设置并刷新。 */
  public void setValues(BPayment bill) {
    this.bill = bill;
    this.values = bill.getDeposits();
    this.defrayalBybill = CPaymentDefrayalType.bill.equals(bill.getDefrayalType()) ? true : false;
    refreshLineNumber();
    refreshDetail();
  }

  private void refreshLineNumber() {
    for (int i = 0; i < values.size(); i++) {
      values.get(i).setLineNumber(i);
    }
  }

  private void refreshDetail() {
    if (defrayalBybill)
      remarkTitle.setVisible(true);
    else
      remarkTitle.setVisible(false);

    initTitle();
    initDetail();

    if (values == null)
      return;

    for (int i = 0; i < values.size(); i++)
      addDetail(values.get(i), i);
  }

  private void initTitle() {
    tableTitle.clear();

    tableTitle.setWidget(0, 0, subjectTitle);
    tableTitle.getColumnFormatter().setWidth(0, WIDTH_DETAIL_COL0);

    tableTitle.setWidget(0, 1, contractTitle);
    tableTitle.getColumnFormatter()
        .setWidth(1, WIDTH_DETAIL_COL1);

    if (bill != null && BBizStates.INEFFECT.equals(bill.getBizState())) {
      tableTitle.setWidget(0, 2, remainTotalField);
      tableTitle.getColumnFormatter().setWidth(2,
          LineDepositDefrayalLineEditGadget.WIDTH_DETAIL_COL2);

      tableTitle.setWidget(0, 3, totalField);
      tableTitle.getColumnFormatter().setWidth(3,
          LineDepositDefrayalLineEditGadget.WIDTH_DETAIL_COL3);
    } else {
      tableTitle.setWidget(0, 2, totalField);
      tableTitle.getColumnFormatter().setWidth(2,
          LineDepositDefrayalLineEditGadget.WIDTH_DETAIL_COL2);
    }

    if (defrayalBybill) {
      remarkTitle.setVisible(true);
      tableTitle.setWidget(0, 4, remarkTitle);
      tableTitle.getColumnFormatter().setWidth(4,
          LineDepositDefrayalLineEditGadget.WIDTH_DETAIL_COL4);
    } else {
      remarkTitle.setVisible(false);
    }
  }

  private Widget initDetail() {
    if (detailTable == null) {
      detailTable = new FlexTable();
    }

    detailTable.clear();
    detailTable.getColumnFormatter().setWidth(0, WIDTH_DETAIL_COL0);
    detailTable.getColumnFormatter().setWidth(1, WIDTH_DETAIL_COL1);
    if (bill != null && BBizStates.INEFFECT.equals(bill.getBizState())) {
      detailTable.getColumnFormatter().setWidth(2, WIDTH_DETAIL_COL2);
      detailTable.getColumnFormatter().setWidth(3, WIDTH_DETAIL_COL3);
    } else {
      detailTable.getColumnFormatter().setWidth(2, WIDTH_DETAIL_COL2);
    }
    if (defrayalBybill)
      detailTable.getColumnFormatter().setWidth(4, WIDTH_DETAIL_COL4);

    return detailTable;
  }

  private void addDetail(BPaymentDepositDefrayal detail, int index) {
    assert detailTable != null;

    RViewStringField subject = new RViewStringField();
    subject.setWidth(WIDTH_DETAIL_COL0);
    subject.setValue(detail.getSubject() == null ? null : detail.getSubject().toFriendlyStr());
    detailTable.setWidget(index, 0, subject);

    RViewStringField contract = new RViewStringField();
    contract.setWidth(WIDTH_DETAIL_COL1);
    contract.setValue(detail.getContract() == null ? null : detail.getContract().toFriendlyStr());
    detailTable.setWidget(index, 1, contract);

    if (bill != null && BBizStates.INEFFECT.equals(bill.getBizState())) {
      RViewNumberField remainTotal = new RViewNumberField();
      remainTotal.setWidth(WIDTH_DETAIL_COL2);
      remainTotal.setFormat(M3Format.fmt_money);
      remainTotal.setValue(detail.getRemainTotal());
      detailTable.setWidget(index, 2, remainTotal);

      RViewNumberField total = new RViewNumberField();
      total.setWidth(WIDTH_DETAIL_COL3);
      total.setFormat(M3Format.fmt_money);
      total.setValue(detail.getTotal());
      detailTable.setWidget(index, 3, total);
    } else {
      RViewNumberField total = new RViewNumberField();
      total.setWidth(WIDTH_DETAIL_COL3);
      total.setFormat(M3Format.fmt_money);
      total.setValue(detail.getTotal());
      detailTable.setWidget(index, 2, total);
    }

    if (defrayalBybill) {
      RViewStringField remark = new RViewStringField();
      remark.setWidth(WIDTH_DETAIL_COL4);
      remark.setOverflowEllipsis(true);
      remark.setValue(detail.getRemark());
      detailTable.setWidget(index, 4, remark);
    }
  }
}
