/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： CashDefrayalGadget.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.gadget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.payment.commons.client.CommonMessages;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineCash;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentHasFocusables;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentCashDefrayalDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDefrayalDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineCashDef;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.biz.ActionName;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;

/**
 * 按科目收款，收款单明细行中收款方式编辑控件
 * 
 * @author subinzhu
 * 
 */
public class LineCashDefrayalEditGadget extends Composite implements RValidatable, RActionHandler,
    HasRActionHandlers, PaymentHasFocusables, Focusable {

  private List<BPaymentLineCash> values;
  private BPayment payment;
  private FlexTable tableTitle = new FlexTable();
  private RVerticalPanel detailTable;
  private List<LineCashDefrayalLineEditGadget> detailGadgets = new ArrayList<LineCashDefrayalLineEditGadget>();

  public LineCashDefrayalEditGadget() {
    super();
    drawSelf();
  }

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();

    vp.add(drawDetailsTitle());
    vp.add(initDetail());

    initWidget(vp);
  }

  private Widget drawDetailsTitle() {
    if (tableTitle == null)
      tableTitle = new FlexTable();

    tableTitle.clear();

    tableTitle.setWidget(0, 0, new HTML(PPaymentLineCashDef.constants.paymentType()));
    tableTitle.getColumnFormatter().setWidth(0, LineCashDefrayalLineEditGadget.WIDTH_DETAIL_COL0);

    tableTitle.setWidget(0, 1, new HTML(PPaymentLineCashDef.constants.total()));
    tableTitle.getColumnFormatter().setWidth(1, LineCashDefrayalLineEditGadget.WIDTH_DETAIL_COL1);

    tableTitle.setWidget(0, 2, new HTML(PPaymentLineCashDef.constants.bankCode()));
    tableTitle.getColumnFormatter().setWidth(2, LineCashDefrayalLineEditGadget.WIDTH_DETAIL_COL2);

    tableTitle.setWidget(0, 3, new HTML(PPaymentLineCashDef.constants.remark()));
    tableTitle.getColumnFormatter().setWidth(3, LineCashDefrayalLineEditGadget.WIDTH_DETAIL_COL3);
    return tableTitle;
  }

  private Widget initDetail() {
    if (detailTable == null) {
      detailTable = new RVerticalPanel();
    }

    detailTable.clear();
    detailGadgets.clear();

    return detailTable;
  }

  @Override
  public void clearValidResults() {
    for (LineCashDefrayalLineEditGadget g : detailGadgets)
      g.clearValidResults();
  }

  @Override
  public boolean isValid() {
    boolean isValid = true;
    for (LineCashDefrayalLineEditGadget g : detailGadgets)
      isValid &= g.isValid();
    return isValid;
  }

  @Override
  public List<Message> getInvalidMessages() {
    String caption = PPaymentDefrayalDef.TABLE_CAPTION + "/"
        + PPaymentCashDefrayalDef.constants.tableCaption();
    List<Message> messages = new ArrayList<Message>();
    for (int i = 0; i < detailGadgets.size(); i++) {
      for (Message message : detailGadgets.get(i).getInvalidMessages()) {
        message.setText(CommonMessages.M.addCaption(caption, (i + 1), message.getText()));
      }
      messages.addAll(detailGadgets.get(i).getInvalidMessages());
    }
    return messages;
  }

  @Override
  public boolean validate() {
    clearValidResults();
    boolean isValid = true;
    for (LineCashDefrayalLineEditGadget g : detailGadgets)
      isValid &= g.validate();
    return isValid;
  }

  public List<BPaymentLineCash> getValue() {
    return values;
  }

  public void setValue(BPayment payment, List<BPaymentLineCash> values) {
    this.payment = payment;
    this.values = values;
    refrehsItemNo();
    refreshDetail();
  }

  private void refreshDetail() {
    initDetail();

    if (values == null)
      return;

    if (values.isEmpty()) {
      BPaymentLineCash lineCash = new BPaymentLineCash();
      lineCash.setPaymentType(EPReceipt.getInstance().getDefaultOption().getPaymentType());
      values.add(lineCash);
    }

    for (int i = 0; i < values.size(); i++)
      addDetail(values.get(i), i);
  }

  @Override
  public void onAction(RActionEvent event) {
    if (ObjectUtil.equals(event.getActionName(), ActionName.ACTION_LINECASHDEFRAYALLINE_ADDDETAIL)) {
      int index = (Integer) event.getParameters().iterator().next();
      BPaymentLineCash newDetail = new BPaymentLineCash();

      insertDetail(newDetail, index + 1);
      refrehsItemNo();
    }
    if (ObjectUtil.equals(event.getActionName(),
        ActionName.ACTION_LINECASHDEFRAYALLINE_REMOVEDETAIL)) {
      int index = (Integer) event.getParameters().iterator().next();
      removeDetail(index);
      refrehsItemNo();

      RActionEvent.fire(LineCashDefrayalEditGadget.this, ActionName.ACTION_LINECASHDEFRAYAL_CHANGE);
    }
    if (ObjectUtil.equals(event.getActionName(), ActionName.ACTION_LINECASHDEFRAYALLINE_CHANGE)) {
      RActionEvent.fire(LineCashDefrayalEditGadget.this, ActionName.ACTION_LINECASHDEFRAYAL_CHANGE);
    }
    if (ObjectUtil.equals(event.getActionName(), ActionName.ACTION_GENERALCREATE_REFRESH_BANKS)) {
      resetBankOptions();
    }
  }

  /** 重新设置银行选项 */
  private void resetBankOptions() {
    for (LineCashDefrayalLineEditGadget gadget : detailGadgets) {
      gadget.resetOptions();
    }
  }

  private void insertDetail(BPaymentLineCash detail, int insertRow) {
    values.add(insertRow, detail);

    addDetail(detail, insertRow);
  }

  private void addDetail(BPaymentLineCash detail, int insertRow) {
    LineCashDefrayalLineEditGadget gadget = new LineCashDefrayalLineEditGadget();
    gadget.setValue(payment, detail);
    gadget.addActionHandler(this);
    detailGadgets.add(insertRow, gadget);
    detailTable.insert(gadget, insertRow);
  }

  private void removeDetail(int currentRow) {
    if (values.size() > 1) {
      values.remove(currentRow);
      detailGadgets.remove(currentRow);
      detailTable.remove(currentRow);
    } else {
      values.clear();
      detailGadgets.clear();
      detailTable.clear();

      BPaymentLineCash newDetail = new BPaymentLineCash();
      newDetail.setPaymentType(EPReceipt.getInstance().getDefaultOption().getPrePaySubject());
      insertDetail(newDetail, 0);
    }
  }

  private void refrehsItemNo() {
    for (int i = 0; i < values.size(); i++) {
      values.get(i).setItemNo(i);
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  public void clearValue() {
    initDetail();
  }

  @Override
  public Focusable getFocusable(int lineNumber, String field) {
    if (detailGadgets == null || detailGadgets.isEmpty() || lineNumber < 0
        || detailGadgets.size() <= lineNumber)
      return null;
    else
      return detailGadgets.get(lineNumber).getFocusable(field);
  }

  @Override
  public int getTabIndex() {
    Focusable field = getFocusableField();
    return field == null ? 0 : field.getTabIndex();
  }

  @Override
  public void setAccessKey(char key) {
    Focusable field = getFocusableField();
    if (field != null)
      field.setAccessKey(key);
  }

  @Override
  public void setFocus(boolean focus) {
    Focusable field = getFocusableField();
    if (field != null)
      field.setFocus(focus);
  }

  @Override
  public void setTabIndex(int index) {
    Focusable field = getFocusableField();
    if (field != null)
      field.setTabIndex(index);

  }

  private Focusable getFocusableField() {
    if (detailGadgets.isEmpty())
      return null;
    return detailGadgets.get(0);
  }
}
