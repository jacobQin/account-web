/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： DepositDefrayalGadget.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.payment.commons.client.AdvanceSubjectFilter;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDeposit;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BRemainTotal;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentHasFocusables;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.SubjectComboBox.WidgetRes;
import com.hd123.m3.account.gwt.payment.commons.client.rpc.PaymentCommonsService;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineDepositDef;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * 按科目收款时，收款单明细行中预存款冲扣编辑控件
 * 
 * @author subinzhu
 * 
 */
public class LineDepositDefrayalEditGadget extends Composite implements RValidatable,
    RActionHandler, HasRActionHandlers, PaymentHasFocusables, Focusable {

  private BPayment bill;
  private List<BPaymentLineDeposit> values;
  private BPaymentAccountLine line;
  private FlexTable tableTitle = new FlexTable();
  private RVerticalPanel detailTable;
  private List<LineDepositDefrayalLineEditGadget> detailGadgets = new ArrayList<LineDepositDefrayalLineEditGadget>();

  /** 科目下拉框选项值 */
  private List<BUCN> subjects = new ArrayList<BUCN>();
  /** 合同下拉框选项值 */
  private List<BUCN> contracts = new ArrayList<BUCN>();

  private List<Message> messages = new ArrayList<Message>();

  public BPayment getBill() {
    return bill;
  }

  public void setBill(BPayment bill) {
    this.bill = bill;
  }

  public LineDepositDefrayalEditGadget() {
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

    tableTitle.setWidget(0, 0, new HTML(ReceiptMessages.M.depositRecSubject()));
    tableTitle.getColumnFormatter()
        .setWidth(0, LineDepositDefrayalLineEditGadget.WIDTH_DETAIL_COL0);

    tableTitle.setWidget(0, 1, new HTML(ReceiptMessages.M.contract()));
    tableTitle.getColumnFormatter()
        .setWidth(1, LineDepositDefrayalLineEditGadget.WIDTH_DETAIL_COL1);

    tableTitle.setWidget(0, 2, new HTML(ReceiptMessages.M.remainTotal()));
    tableTitle.getColumnFormatter()
        .setWidth(2, LineDepositDefrayalLineEditGadget.WIDTH_DETAIL_COL2);

    tableTitle.setWidget(0, 3, new HTML(PPaymentLineDepositDef.constants.total()));
    tableTitle.getColumnFormatter()
        .setWidth(3, LineDepositDefrayalLineEditGadget.WIDTH_DETAIL_COL3);

    tableTitle.setWidget(0, 4, new HTML(PPaymentLineDepositDef.constants.remark()));
    tableTitle.getColumnFormatter()
        .setWidth(4, LineDepositDefrayalLineEditGadget.WIDTH_DETAIL_COL4);
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
    messages.clear();
    for (LineDepositDefrayalLineEditGadget g : detailGadgets)
      g.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return messages.isEmpty();
  }

  @Override
  public List<Message> getInvalidMessages() {
    for (LineDepositDefrayalLineEditGadget g : detailGadgets)
      messages.addAll(g.getInvalidMessages());
    return messages;
  }

  @Override
  public boolean validate() {
    clearValidResults();
    boolean isValid = true;
    for (LineDepositDefrayalLineEditGadget g : detailGadgets)
      isValid &= g.validate();
    return isValid;
  }

  public List<BPaymentLineDeposit> getValue() {
    return values;
  }

  public void setValue(BPaymentAccountLine line) {
    this.line=line;
    this.values = line.getDeposits();

    refreshSubjects();
    refreshContracts();

    refreshItemNo();
    refreshDetail();
  }

  private void refreshDetail() {
    initDetail();

    if (values == null)
      return;

    if (values.isEmpty()) {
      BPaymentLineDeposit lineDeposit = new BPaymentLineDeposit();
      lineDeposit.setSubject(EPReceipt.getInstance().getDefaultOption().getPrePaySubject());
      values.add(lineDeposit);
    }

    for (int i = 0; i < values.size(); i++)
      addDetail(values.get(i), i);
  }

  @Override
  public void onAction(RActionEvent event) {
    if (ObjectUtil.equals(event.getActionName(),
        LineDepositDefrayalLineEditGadget.ActionName.ACTION_LINEDEPOSITDEFRAYALLINE_ADDDETAIL)) {
      int index = (Integer) event.getParameters().iterator().next();
      BPaymentLineDeposit newDetail = new BPaymentLineDeposit();

      insertDetail(newDetail, index + 1);
      refreshItemNo();

      RActionEvent.fire(LineDepositDefrayalEditGadget.this,
          ActionName.ACTION_LINEDEPOSITDEFRAYAL_CHANGE);
    }
    if (ObjectUtil.equals(event.getActionName(),
        LineDepositDefrayalLineEditGadget.ActionName.ACTION_LINEDEPOSITDEFRAYALLINE_REMOVEDETAIL)) {
      int index = (Integer) event.getParameters().iterator().next();
      removeDetail(index);
      refreshItemNo();

      RActionEvent.fire(LineDepositDefrayalEditGadget.this,
          ActionName.ACTION_LINEDEPOSITDEFRAYAL_CHANGE);
    }
    if (ObjectUtil.equals(event.getActionName(),
        LineDepositDefrayalLineEditGadget.ActionName.ACTION_LINEDEPOSITDEFRAYALLINE_CHANGE)) {
      RActionEvent.fire(LineDepositDefrayalEditGadget.this,
          ActionName.ACTION_LINEDEPOSITDEFRAYAL_CHANGE);
    }
  }

  private void insertDetail(BPaymentLineDeposit detail, int insertRow) {
    values.add(insertRow, detail);

    addDetail(detail, insertRow);
  }

  private void addDetail(BPaymentLineDeposit detail, int insertRow) {
    LineDepositDefrayalLineEditGadget gadget = new LineDepositDefrayalLineEditGadget();
    gadget.setBill(bill);
    gadget.refreshSubjects(subjects);
    gadget.setValue(detail);
    gadget.addActionHandler(this);
    detailGadgets.add(insertRow, gadget);
    detailTable.insert(gadget, insertRow);
    gadget.refreshContracts(contracts,line);
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

      BPaymentLineDeposit newDetail = new BPaymentLineDeposit();
      newDetail.setSubject(EPReceipt.getInstance().getDefaultOption().getPrePaySubject());
      insertDetail(newDetail, 0);
    }
  }

  private void refreshItemNo() {
    for (int i = 0; i < values.size(); i++) {
      values.get(i).setItemNo(i);
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  public static class ActionName {
    /** 改变值 */
    public static final String ACTION_LINEDEPOSITDEFRAYAL_CHANGE = "LINEDEPOSITDEFRAYAL_change";
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

  /**
   * 刷新预存款科目余额
   */
  public void refreshRemainTotal() {
    if (detailGadgets == null || detailGadgets.isEmpty())
      return;

    if (bill.getAccountUnit() == null || StringUtil.isNullOrBlank(bill.getAccountUnit().getUuid())
        || bill.getCounterpart() == null
        || StringUtil.isNullOrBlank(bill.getCounterpart().getUuid())) {
      for (BPaymentLineDeposit d : values) {
        d.setRemainTotal(BigDecimal.ZERO);
      }
    } else {
      List<BRemainTotal> remainTotalIds = new ArrayList<BRemainTotal>();
      for (BPaymentLineDeposit d : values) {
        if (d.getSubject() == null) {
          d.setRemainTotal(BigDecimal.ZERO);
          continue;
        }
        BRemainTotal r = new BRemainTotal();
        r.setSubject(d.getSubject().getUuid());
        r.setContract((d.getContract() == null || StringUtil.isNullOrBlank(d.getContract()
            .getUuid())) ? "-" : d.getContract().getUuid());
        remainTotalIds.add(r);
      }
      getRemainTotals(remainTotalIds);
    }
    for (LineDepositDefrayalLineEditGadget g : detailGadgets) {
      g.refreshRemainTotal();
    }
  }

  /**
   * 批量获取预存款科目余额
   * 
   * @param ramainTotals
   */
  private void getRemainTotals(List<BRemainTotal> remainTotalIds) {
    if (bill.getAccountUnit() == null || StringUtil.isNullOrBlank(bill.getAccountUnit().getUuid()))
      return;

    GWTUtil.enableSynchronousRPC();
    RLoadingDialog.show(ReceiptMessages.M.loading2(ReceiptMessages.M.depositRecSubject()
        + ReceiptMessages.M.remainTotal()));
    PaymentCommonsService.Locator.getService().getDepositSubjectRemainTotals(
        bill.getAccountUnit().getUuid(), bill.getCounterpart().getUuid(), remainTotalIds,
        new RBAsyncCallback2<List<BRemainTotal>>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.load(),
                ReceiptMessages.M.depositRecSubject() + ReceiptMessages.M.remainTotal());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(List<BRemainTotal> result) {
            RLoadingDialog.hide();
            for (BPaymentLineDeposit value : values) {
              if (value.getSubject() == null)
                continue;

              for (BRemainTotal r : result) {
                if (value.getSubject().getUuid().equals(r.getSubject())
                    && (((value.getContract() == null || StringUtil.isNullOrBlank(value
                        .getContract().getUuid())) && r.getContract().equals("-")) || (value
                        .getContract() != null
                        && !StringUtil.isNullOrBlank(value.getContract().getUuid()) && value
                        .getContract().getUuid().equals(r.getContract())))) {
                  value.setRemainTotal(r.getRemainTotal());
                }
              }
            }
          }
        });
  }

  /**
   * 刷新科目和合同下拉框的选项
   */
  public void refreshSubjectsAndContracts() {
    refreshSubjects();
    refreshContracts();

    if (values == null || values.isEmpty()) {
      detailGadgets.clear();
      return;
    }
    for (LineDepositDefrayalLineEditGadget g : detailGadgets) {
      g.refreshSubjects(subjects);
      g.refreshContracts(contracts,line);
    }
  }

  /**
   * 刷新科目下拉值选项
   */
  private void refreshSubjects() {
    if (bill.getAccountUnit() == null || StringUtil.isNullOrBlank(bill.getAccountUnit().getUuid())
        || bill.getCounterpart() == null
        || StringUtil.isNullOrBlank(bill.getCounterpart().getUuid())) {
      subjects.clear();
      return;
    }

    GWTUtil.enableSynchronousRPC();
    AdvanceSubjectFilter filter = new AdvanceSubjectFilter();
    filter.setAccountUnitUuid(bill.getAccountUnit().getUuid());
    filter.setDirectionType(DirectionType.receipt.getDirectionValue());
    filter.setCounterpartUuid(bill.getCounterpart().getUuid());
    PaymentCommonsService.Locator.getService().getSubjects(filter, new AsyncCallback<List<BUCN>>() {

      @Override
      public void onFailure(Throwable arg0) {
        String msg = CommonsMessages.M.actionFailed(CommonsMessages.M.load(),
            WidgetRes.R.paymentSubject());
        RMsgBox.show(msg);
      }

      @Override
      public void onSuccess(List<BUCN> result) {
        subjects.clear();
        if (result == null || result.isEmpty()) {
          return;
        }
        subjects.addAll(result);
      }
    });
  }

  /**
   * 刷新合同下拉值选项
   */
  private void refreshContracts() {
    if (bill.getAccountUnit() == null || StringUtil.isNullOrBlank(bill.getAccountUnit().getUuid())
        || bill.getCounterpart() == null
        || StringUtil.isNullOrBlank(bill.getCounterpart().getUuid())) {
      contracts.clear();
      return;
    }

    GWTUtil.enableSynchronousRPC();
    PaymentCommonsService.Locator.getService().getContracts(bill.getAccountUnit().getUuid(),
        bill.getCounterpart().getUuid(), new AsyncCallback<List<BUCN>>() {

          @Override
          public void onFailure(Throwable arg0) {
            String msg = CommonsMessages.M.actionFailed(CommonsMessages.M.load(),
                ReceiptMessages.M.contract());
            RMsgBox.show(msg);
          }

          @Override
          public void onSuccess(List<BUCN> result) {
            contracts.clear();
            if (result == null || result.isEmpty()) {
              return;
            }
            contracts.addAll(result);
          }
        });
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
  public void setFocus(boolean focused) {
    Focusable field = getFocusableField();
    if (field != null)
      field.setFocus(focused);
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
