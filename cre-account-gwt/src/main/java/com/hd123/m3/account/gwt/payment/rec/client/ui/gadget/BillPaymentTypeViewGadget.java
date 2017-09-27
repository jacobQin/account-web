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

import java.math.BigDecimal;
import java.util.List;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineCash;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDeduction;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDeposit;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineDefrayalDef;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
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
public class BillPaymentTypeViewGadget extends RCaptionBox {

  private List<BPaymentLineDefrayal> values;
  private FlexTable tableTitle = new FlexTable();
  private FlexTable detailTable;

  public BillPaymentTypeViewGadget() {
    super();
    drawSelf();
  }

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();

    vp.add(drawDetailsTitle());
    vp.add(initDetail());

    setCaption("收款方式");
    setStyleName(RCaptionBox.STYLENAME_CONCISE);
    setWidth("100%");
    setContent(vp);
  }

  private Widget drawDetailsTitle() {
    if (tableTitle == null)
      tableTitle = new FlexTable();

    tableTitle.clear();

    tableTitle.setWidget(0, 0, new HTML("收款方式"));
    tableTitle.getColumnFormatter().setWidth(0, "200px");

    Label totalField = new Label(PPaymentLineDefrayalDef.constants.total());
    totalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    tableTitle.setWidget(0, 1, totalField);
    tableTitle.getColumnFormatter().setWidth(1, "100px");

    return tableTitle;
  }

  public Widget initDetail() {
    if (detailTable == null) {
      detailTable = new FlexTable();
    }

    detailTable.clear();
    detailTable.getColumnFormatter().setWidth(0, "200px");
    detailTable.getColumnFormatter().setWidth(1, "100px");

    return detailTable;
  }

  public void setValues(List<BPaymentLineDefrayal> values) {
    this.values = values;
    refreshDetail();
  }

  private void refreshDetail() {
    initDetail();

    if (values == null)
      return;

    for (int i = 0; i < values.size(); i++)
      addDetail(values.get(i), i);
  }

  private void addDetail(BPaymentLineDefrayal detail, int index) {
    assert detailTable != null;

    RViewStringField paymentTypeField = new RViewStringField();
    if (detail instanceof BPaymentLineCash) {
      paymentTypeField.setValue(((BPaymentLineCash) detail).getPaymentType() == null ? null
          : ((BPaymentLineCash) detail).getPaymentType().toFriendlyStr());
    } else if (detail instanceof BPaymentLineDeduction) {
      paymentTypeField.setValue(ReceiptMessages.M.deduction());
    } else if (detail instanceof BPaymentLineDeposit) {
      if (((BPaymentLineDeposit) detail).getContract() != null
          && !StringUtil.isNullOrBlank(((BPaymentLineDeposit) detail).getContract().getUuid()))
        paymentTypeField.setValue(ReceiptMessages.M.depositRecHasContract());
      else
        paymentTypeField.setValue(ReceiptMessages.M.depositRecHasNoContract());
    }
    detailTable.setWidget(index, 0, paymentTypeField);

    RViewNumberField totalField = new RViewNumberField();
    totalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    totalField.setFormat(M3Format.fmt_money);
    totalField.setValue(detail.getTotal());
    detailTable.setWidget(index, 1, totalField);
  }

  public void addDepositDetail(BigDecimal depositTotal) {
    assert detailTable != null;

    RViewStringField paymentTypeField = new RViewStringField();
    paymentTypeField.setValue(ReceiptMessages.M.generateRec());
    detailTable.setWidget(values.size(), 0, paymentTypeField);

    RViewNumberField depositTotalField = new RViewNumberField();
    depositTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    depositTotalField.setFormat(M3Format.fmt_money);
    depositTotalField.setValue(depositTotal);
    detailTable.setWidget(values.size(), 1, depositTotalField);
  }
}
